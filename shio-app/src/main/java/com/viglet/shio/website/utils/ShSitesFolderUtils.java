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
package com.viglet.shio.website.utils;

import com.viglet.shio.persistence.model.folder.ShFolder;
import com.viglet.shio.persistence.model.post.ShPost;
import com.viglet.shio.persistence.model.post.type.ShPostType;
import com.viglet.shio.persistence.repository.folder.ShFolderRepository;
import com.viglet.shio.persistence.repository.post.ShPostRepository;
import com.viglet.shio.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shio.post.type.ShSystemPostType;
import com.viglet.shio.url.ShURLScheme;
import com.viglet.shio.utils.ShFolderUtils;
import com.viglet.shio.website.ShContent;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Alexandre Oliveira
 */
@Component
public class ShSitesFolderUtils {
  @Autowired private ShFolderRepository shFolderRepository;
  @Autowired private ShPostRepository shPostRepository;
  @Autowired private ShURLScheme shURLScheme;
  @Autowired private ShPostTypeRepository shPostTypeRepository;
  @Autowired private ShSitesPostUtils shSitesPostUtils;
  @Autowired private ShFolderUtils shFolderUtils;

  public ShFolder getParentFolder(String shFolderId) {
    return shFolderUtils.getParentFolder(shFolderId);
  }

  public ShPost getFolderIndex(ShFolder shFolder) {
    ShPostType shPostType = shPostTypeRepository.findByName(ShSystemPostType.FOLDER_INDEX);
    List<ShPost> shFolderIndexPosts =
        shPostRepository.findByShFolderAndShPostTypeOrderByPositionAsc(shFolder, shPostType);
    if (!shFolderIndexPosts.isEmpty())
      return shSitesPostUtils.getPostByStage(shFolderIndexPosts.get(0));
    return null;
  }

  public Map<String, Object> toMap(String shFolderId) {
    Optional<ShFolder> shFolder = shFolderRepository.findById(shFolderId);
    if (shFolder.isPresent()) return this.toMap(shFolder.get());
    return Collections.emptyMap();
  }

  public ShContent toSystemMap(ShFolder shFolder) {
    ShContent shFolderItemAttrs = new ShContent();

    shFolderItemAttrs.put("system", this.toMap(shFolder));

    return shFolderItemAttrs;
  }

  public Map<String, Object> toMap(ShFolder shFolder) {
    Map<String, Object> shFolderItemAttrs = new HashMap<>();

    shFolderItemAttrs.put("id", shFolder.getId());
    shFolderItemAttrs.put("title", shFolder.getName());
    shFolderItemAttrs.put("link", shFolderUtils.folderPath(shFolder, true, false));

    return shFolderItemAttrs;
  }

  public JSONObject toJSON(ShFolder shFolder) {
    JSONObject shFolderItemAttrs = new JSONObject();

    JSONObject shFolderItemSystemAttrs = new JSONObject();
    shFolderItemSystemAttrs.put("id", shFolder.getId());
    shFolderItemSystemAttrs.put("title", shFolder.getName());
    shFolderItemSystemAttrs.put("link", shFolderUtils.folderPath(shFolder, true, false));

    shFolderItemAttrs.put("system", shFolderItemSystemAttrs);

    return shFolderItemAttrs;
  }

  public String generateFolderLink(ShFolder shFolder) {
    String link = shURLScheme.get(shFolder);
    link = link + shFolderUtils.folderPath(shFolder, true, false);
    return link;
  }

  public String generateFolderLinkById(String folderID) {
    Optional<ShFolder> shFolderOptional = shFolderRepository.findById(folderID);
    if (shFolderOptional.isPresent()) {
      ShFolder shFolder = shFolderOptional.get();
      return this.generateFolderLink(shFolder);
    }
    return null;
  }
}
