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
package com.viglet.shio.swagger;

import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Alexandre Oliveira
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
  @Bean
  public Docket api() {
    ShCustomPathProvider pathProvider = new ShCustomPathProvider();

    return new Docket(DocumentationType.SWAGGER_2)
        .pathProvider(pathProvider)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.viglet.shio.api"))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiInfo())
        .enable(false);
  }

  private ApiInfo apiInfo() {
    return new ApiInfo(
        "Viglet Shio CMS",
        "Model Content and Create Site using Javascript with Native Cache and Search.",
        "0.3.6",
        "Terms of service",
        new Contact("Viglet Team", "http://www.viglet.com", "opensource@viglet.com"),
        "License of API",
        "API license URL",
        Collections.emptyList());
  }
}
