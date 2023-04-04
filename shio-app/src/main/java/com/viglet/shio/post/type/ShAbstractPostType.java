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
package com.viglet.shio.post.type;

import com.viglet.shio.exchange.post.ShPostExchange;
import com.viglet.shio.url.ShURLFormatter;
import java.util.Date;

/**
 * Abstract Post Type
 *
 * @author Alexandre Oliveira
 * @since 0.3.4
 */
public class ShAbstractPostType {

  private String id;

  private String furl;

  private Date date;

  private String owner;

  private String folder;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFurl() {
    return furl;
  }

  public void setFurl(String furl) {
    this.furl = furl;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getFolder() {
    return folder;
  }

  public void setFolder(String folder) {
    this.folder = folder;
  }

  public ShPostExchange getShPostExchange() {

    ShPostExchange shPostExchange = new ShPostExchange();

    shPostExchange.setId(this.getId());
    shPostExchange.setOwner(this.getOwner());
    shPostExchange.setDate(this.getDate());
    shPostExchange.setPostType(ShSystemPostType.TEXT);
    shPostExchange.setFurl(ShURLFormatter.format(this.getFurl()));
    shPostExchange.setFolder(this.getFolder());

    return shPostExchange;
  }
}
