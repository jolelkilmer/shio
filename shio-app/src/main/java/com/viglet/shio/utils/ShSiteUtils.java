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
package com.viglet.shio.utils;

import com.viglet.shio.exchange.ShCloneExchange;
import com.viglet.shio.persistence.model.site.ShSite;
import com.viglet.shio.url.ShURLScheme;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

/**
 * @author Alexandre Oliveira
 */
@Component
public class ShSiteUtils {
  private static final Log logger = LogFactory.getLog(ShSiteUtils.class);
  @Autowired ShURLScheme shURLScheme;
  @Autowired private ShCloneExchange shCloneExchange;
  @Autowired private ResourceLoader resourceloader;
  @Autowired private ShStaticFileUtils shStaticFileUtils;

  private static final String COULD_NOT_CREATE_SAMPLE_SITE = "Could not create sample site";

  public ShSite importSiteFromResourceOrURL(String classpathFile, URL url, String slug) {
    try {
      Resource resource = resourceloader.getResource("classpath:" + classpathFile);

      File siteFile =
          new File(
              shStaticFileUtils
                  .getTmpDir()
                  .getAbsolutePath()
                  .concat(File.separator + slug + UUID.randomUUID() + ".zip"));
      if (resource.exists()) {
        InputStream is = resource.getInputStream();
        FileUtils.copyInputStreamToFile(is, siteFile);

      } else {
        FileUtils.copyURLToFile(url, siteFile);
      }
      return shCloneExchange.importNewSiteFromTemplateFile(siteFile);
    } catch (IllegalStateException | IOException e) {
      logger.error(COULD_NOT_CREATE_SAMPLE_SITE, e);
    }

    return null;
  }

  public Map<String, Object> toSystemMap(ShSite shSite) {
    Map<String, Object> shSiteItemSystemAttrs = new HashMap<>();
    shSiteItemSystemAttrs.put("id", shSite.getId());
    shSiteItemSystemAttrs.put("title", shSite.getName());
    shSiteItemSystemAttrs.put("summary", shSite.getDescription());
    shSiteItemSystemAttrs.put("link", shURLScheme.get(shSite));

    Map<String, Object> shSiteItemAttrs = new HashMap<>();

    shSiteItemAttrs.put("system", shSiteItemSystemAttrs);
    return shSiteItemAttrs;
  }

  public JSONObject toJSON(ShSite shSite) {
    JSONObject shSiteItemSystemAttrs = new JSONObject();
    shSiteItemSystemAttrs.put("id", shSite.getId());
    shSiteItemSystemAttrs.put("title", shSite.getName());
    shSiteItemSystemAttrs.put("summary", shSite.getDescription());
    shSiteItemSystemAttrs.put("link", shURLScheme.get(shSite));

    JSONObject shSiteItemAttrs = new JSONObject();

    shSiteItemAttrs.put("system", shSiteItemSystemAttrs);
    return shSiteItemAttrs;
  }

  public String generatePostLink(ShSite shSite) {
    return shURLScheme.get(shSite);
  }
}
