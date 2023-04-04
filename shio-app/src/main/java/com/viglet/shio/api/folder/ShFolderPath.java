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
package com.viglet.shio.api.folder;

import com.viglet.shio.persistence.model.folder.ShFolder;
import com.viglet.shio.persistence.model.post.impl.ShPostImpl;
import com.viglet.shio.persistence.model.site.ShSite;
import java.util.List;

/**
 * @author Alexandre Oliveira
 */
public class ShFolderPath {

  private String folderPath;

  private ShFolder currentFolder;

  private List<ShFolder> breadcrumb;

  private ShPostImpl shPost;

  private ShSite shSite;

  public String getFolderPath() {
    return folderPath;
  }

  public void setFolderPath(String folderPath) {
    this.folderPath = folderPath;
  }

  public ShFolder getCurrentFolder() {
    return currentFolder;
  }

  public void setCurrentFolder(ShFolder currentFolder) {
    this.currentFolder = currentFolder;
  }

  public List<ShFolder> getBreadcrumb() {
    return breadcrumb;
  }

  public void setBreadcrumb(List<ShFolder> breadcrumb) {
    this.breadcrumb = breadcrumb;
  }

  public ShSite getShSite() {
    return shSite;
  }

  public void setShSite(ShSite shSite) {
    this.shSite = shSite;
  }

  public ShPostImpl getShPost() {
    return shPost;
  }

  public void setShPost(ShPostImpl shPost) {
    this.shPost = shPost;
  }
}
