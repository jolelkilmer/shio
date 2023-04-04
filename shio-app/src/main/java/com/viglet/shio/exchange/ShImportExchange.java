/*
 * Copyright (C) 2016-2021 the original author or authors.
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
package com.viglet.shio.exchange;

import com.viglet.shio.exchange.post.ShPostImport;
import com.viglet.shio.exchange.post.type.ShPostTypeImport;
import com.viglet.shio.exchange.site.ShSiteImport;
import com.viglet.shio.exchange.utils.ShExchangeUtils;
import com.viglet.shio.persistence.model.site.ShSite;
import com.viglet.shio.utils.ShStaticFileUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Alexandre Oliveira
 */
@Component
public class ShImportExchange {
  private static final Logger logger = LogManager.getLogger(ShImportExchange.class);
  @Autowired private ShSiteImport shSiteImport;
  @Autowired private ShPostTypeImport shPostTypeImport;
  @Autowired private ShPostImport shPostImport;
  @Autowired private ShExchangeUtils shExchangeUtils;
  @Autowired private ResourceLoader resourceloader;
  @Autowired private ShCloneExchange shCloneExchange;
  @Autowired private ShStaticFileUtils shStaticFileUtils;

  public ShExchangeData getDefaultTemplateToSite(ShSite shSite) {

    ShExchangeData shExchangeData = null;

    File templateSiteFile =
        new File(
            shStaticFileUtils
                .getTmpDir()
                .getAbsolutePath()
                .concat(File.separator + "template-site-" + UUID.randomUUID() + ".zip"));

    try {
      Resource resource = resourceloader.getResource("classpath:/import/bootstrap-site.zip");

      if (resource.exists()) {
        InputStream is = resource.getInputStream();
        FileUtils.copyInputStreamToFile(is, templateSiteFile);
      } else {
        FileUtils.copyURLToFile(
            new URL("https://github.com/ShioCMS/bootstrap-site/archive/0.3.7.zip"),
            templateSiteFile);
      }
      shExchangeData = shCloneExchange.getTemplateAsCloneFromFile(templateSiteFile, shSite);
    } catch (IllegalStateException | IOException e) {

      logger.error(e);
    }
    if (shExchangeData != null
        && shExchangeData.getShExchange() != null
        && shExchangeData.getShExchange().getSites() != null) {
      shSite.setId(shExchangeData.getShExchange().getSites().get(0).getId());
    }
    FileUtils.deleteQuietly(templateSiteFile);

    return shExchangeData;
  }

  public ShExchange importFromMultipartFile(MultipartFile multipartFile) {
    logger.info("Unzip Package");
    ShExchangeFilesDirs shExchangeFilesDirs = this.extractZipFile(multipartFile);

    if (shExchangeFilesDirs.getExportDir() != null) {
      ShExchange shExchange = shExchangeFilesDirs.readExportFile();
      this.importObjects(new ShExchangeData(shExchange, shExchangeFilesDirs));

      shExchangeFilesDirs.deleteExport();
      return shExchange;
    } else {
      return null;
    }
  }

  private void importObjects(ShExchangeData shExchangeData) {
    ShExchange shExchange = shExchangeData.getShExchange();
    File extractFolder = shExchangeData.getShExchangeFilesDirs().getExportDir();

    if (shExchange != null) {
      if (shExchange.getPostTypes() != null && !shExchange.getPostTypes().isEmpty())
        shPostTypeImport.importPostType(shExchange, false);

      if (shExchange.getSites() != null && !shExchange.getSites().isEmpty()) {
        shSiteImport.importSite(shExchange, extractFolder);
      } else if (shExchange.getFolders() == null && shExchange.getPosts() != null) {
        ShExchangeObjectMap shExchangeObjectMap = shSiteImport.prepareImport(shExchange);
        File extractFolderInner = extractFolder;
        shExchange
            .getPosts()
            .forEach(
                shPostExchange ->
                    shPostImport.createShPost(
                        new ShExchangeContext(extractFolderInner, false),
                        shPostExchange,
                        shExchangeObjectMap));
      }
    }
  }

  public ShExchange importFromFile(File file) {

    MultipartFile multipartFile = null;
    try {
      FileInputStream input = new FileInputStream(file);
      multipartFile = new MockMultipartFile(file.getName(), IOUtils.toByteArray(input));
    } catch (IOException e) {
      logger.error(e);
    }

    return this.importFromMultipartFile(multipartFile);
  }

  public ShExchangeFilesDirs extractZipFile(MultipartFile file) {
    return shExchangeUtils.extractZipFile(file);
  }

  public ShExchangeFilesDirs getExtratedImport(File directory) {
    return shExchangeUtils.getExtratedImport(directory);
  }
}
