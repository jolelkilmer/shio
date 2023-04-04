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
package com.viglet.shio.provider.exchange.otcs.bean.object;

import com.viglet.shio.provider.exchange.otcs.bean.link.ShOTCSLinksBean;
import com.viglet.shio.provider.exchange.otcs.bean.result.ShOTCSResultsBean;

/**
 * @author Alexandre Oliveira
 * @since 0.3.6
 */
public class ShOTCSObjectBean {

  private ShOTCSLinksBean links;

  private ShOTCSResultsBean results;

  public ShOTCSLinksBean getLinks() {
    return links;
  }

  public void setLinks(ShOTCSLinksBean links) {
    this.links = links;
  }

  public ShOTCSResultsBean getResults() {
    return results;
  }

  public void setResults(ShOTCSResultsBean results) {
    this.results = results;
  }
}
