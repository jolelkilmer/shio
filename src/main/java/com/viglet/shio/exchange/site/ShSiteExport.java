/*
 * Copyright (C) 2016-2020 the original author or authors. 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.viglet.shio.exchange.site;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viglet.shio.exchange.ShExchange;
import com.viglet.shio.exchange.ShExchangeFilesDirs;
import com.viglet.shio.exchange.folder.ShFolderExport;
import com.viglet.shio.exchange.post.type.ShPostTypeExchange;
import com.viglet.shio.exchange.post.type.ShPostTypeExport;
import com.viglet.shio.exchange.utils.ShExchangeUtils;
import com.viglet.shio.persistence.model.folder.ShFolder;
import com.viglet.shio.persistence.model.post.type.ShPostType;
import com.viglet.shio.persistence.model.site.ShSite;
import com.viglet.shio.persistence.repository.folder.ShFolderRepository;
import com.viglet.shio.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shio.persistence.repository.site.ShSiteRepository;
import com.viglet.shio.utils.ShUtils;

/**
 * Export Site.
 *
 * @author Alexandre Oliveira
 * @since 0.3.0
 */
@Component
public class ShSiteExport {
	private static final Log logger = LogFactory.getLog(ShSiteExport.class);

	@Autowired
	private ShSiteRepository shSiteRepository;
	@Autowired
	private ShFolderRepository shFolderRepository;
	@Autowired
	private ShUtils shUtils;
	@Autowired
	private ShFolderExport shFolderExport;
	@Autowired
	private ShPostTypeExport shPostTypeExport;
	@Autowired
	private ShPostTypeRepository shPostTypeRepository;
	@Autowired
	private ShExchangeUtils shExchangeUtils;

	public StreamingResponseBody exportObject(@PathVariable String id, HttpServletResponse response) {
		ShExchange shExchange = new ShExchange();
		Optional<ShSite> shSiteOptional = shSiteRepository.findById(id);

		ShExchangeFilesDirs shExchangeFilesDirs = new ShExchangeFilesDirs();
		if (shSiteOptional.isPresent() && shExchangeFilesDirs.generate()) {
			ShSite shSite = shSiteOptional.get();

			List<String> rootFoldersUUID = new ArrayList<>();
			Set<ShFolder> rootFolders = shFolderRepository.findByShSiteAndRootFolder(shSite, (byte) 1);

			rootFolders.forEach(shFolder -> rootFoldersUUID.add(shFolder.getId()));

			ShSiteExchange shSiteExchange = new ShSiteExchange();
			shSiteExchange.setId(shSite.getId());
			shSiteExchange.setName(shSite.getName());
			shSiteExchange.setUrl(shSite.getUrl());
			shSiteExchange.setDescription(shSite.getDescription());
			shSiteExchange.setPostTypeLayout(shSite.getPostTypeLayout());
			shSiteExchange.setSearchablePostTypes(shSite.getSearchablePostTypes());
			shSiteExchange.setDate(shSite.getDate());
			shSiteExchange.setRootFolders(rootFoldersUUID);
			shSiteExchange.setOwner(shSite.getOwner());
			shSiteExchange.setFurl(shSite.getFurl());

			List<ShSiteExchange> shSiteExchanges = new ArrayList<>();
			shSiteExchanges.add(shSiteExchange);
			shExchange.setSites(shSiteExchanges);

			ShExchange shExchangeFolder = shFolderExport.shFolderExchangeIterate(rootFolders);

			shExchange.setFolders(shExchangeFolder.getFolders());
			shExchange.setPosts(shExchangeFolder.getPosts());
			shExchange.setPostTypes(exportPostTypes(shSiteExchange, shExchangeFolder));
			exportStaticFiles(shExchangeFilesDirs.getExportDir(), shExchangeFolder);
			return shExchangeUtils.downloadZipFile(String.format("%s_site", shSite.getFurl()), response, shExchange,
					shExchangeFilesDirs);
		} else {
			return null;
		}

	}

	private List<ShPostTypeExchange> exportPostTypes(ShSiteExchange shSiteExchange, ShExchange shExchangeFolder) {
		List<ShPostTypeExchange> shExchangePostTypes = shExchangeFolder.getPostTypes();

		if (shUtils.isJSONValid(shSiteExchange.getPostTypeLayout())) {
			Gson gson = new Gson();
			Type type = new TypeToken<Set<String>>() {
			}.getType();
			Set<String> postTypes = gson.fromJson(shSiteExchange.getPostTypeLayout(), type);

			Set<String> shExchangePostTypeMap = new HashSet<>();
			if (shExchangePostTypes != null)
				shExchangePostTypes
						.forEach(shPostTypeExchange -> shExchangePostTypeMap.add(shPostTypeExchange.getName()));
			else
				shExchangePostTypes = new ArrayList<>();

			for (String postType : postTypes) {
				if (!shExchangePostTypeMap.contains(postType)) {
					ShPostType shPostType = shPostTypeRepository.findByName(postType);
					if (shPostType != null) {
						shExchangePostTypes.add(shPostTypeExport.exportPostType(shPostType));
					}
				}
			}
		}
		return shExchangePostTypes;
	}

	private void exportStaticFiles(File exportDir, ShExchange shExchange) {
		shExchange.getFiles().forEach(fileExchange -> {
			try {
				File fileSource = fileExchange.getFile();
				File fileDestination = new File(
						exportDir.getAbsolutePath().concat(File.separator + fileExchange.getId()));
				if (fileSource.exists())
					FileUtils.copyFile(fileSource, fileDestination);
				else {
					logger.warn(String.format(
							"Exporting the file %s, but it does not exist, so it is creating a new empty file to export.",
							fileSource.getAbsoluteFile()));
					if (fileDestination.createNewFile())
						logger.debug(String.format("File was created %s", fileDestination));
					else
						logger.error(String.format("File was not created: %s", fileDestination));
				}

			} catch (IOException e) {
				logger.error("exportObject: ", e);
			}

		});
	}
}
