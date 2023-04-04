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
package com.viglet.shio.spring;

import com.viglet.shio.utils.ShStaticFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Alexandre Oliveira
 */
@Configuration
@AutoConfigureAfter(DispatcherServletAutoConfiguration.class)
public class ShStaticResourceConfiguration implements WebMvcConfigurer {

  private static final String THIRDPARTY_FOLDER = "/thirdparty/**";
  @Autowired private ShStaticFileUtils shStaticFileUtils;

  @Value("${shio.allowedOrigins:localhost}")
  private String allowedOrigins;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {

    String resourceLocation = "file:" + shStaticFileUtils.getFileSource().getAbsolutePath();
    registry
        .addResourceHandler("/store/**")
        .addResourceLocations(resourceLocation)
        .setCachePeriod(3600 * 24);
    registry
        .addResourceHandler("/file_source/**")
        .addResourceLocations(resourceLocation + "/")
        .setCachePeriod(3600 * 24);

    if (!registry.hasMappingForPattern(THIRDPARTY_FOLDER)) {
      registry
          .addResourceHandler(THIRDPARTY_FOLDER)
          .addResourceLocations("classpath:/META-INF/resources/webjars/")
          .setCachePeriod(3600 * 24);
    }
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        .addMapping(THIRDPARTY_FOLDER)
        .allowedOrigins(allowedOrigins)
        .allowedMethods("PUT", "DELETE", "GET", "POST")
        .allowCredentials(false)
        .maxAge(3600);
    registry
        .addMapping("/api/**")
        .allowedOrigins(allowedOrigins)
        .allowedMethods("PUT", "DELETE", "GET", "POST")
        .allowCredentials(false)
        .maxAge(3600);
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/content").setViewName("forward:/content/index.html");
    registry.addViewController("/content/").setViewName("forward:/content/index.html");
    registry.addViewController("/welcome").setViewName("forward:/welcome/index.html");
    registry.addViewController("/welcome/").setViewName("forward:/welcome/index.html");
    registry.addViewController("/preview").setViewName("forward:/preview/index.html");
    registry.addViewController("/preview/").setViewName("forward:/preview/index.html");
    registry.addViewController("/media").setViewName("forward:/media/index.html");
    registry.addViewController("/media/").setViewName("forward:/media/index.html");
  }

  @Override
  public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
    configurer.setDefaultTimeout(-1);
  }
}
