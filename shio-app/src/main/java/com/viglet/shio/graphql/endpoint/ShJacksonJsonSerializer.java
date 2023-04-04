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
package com.viglet.shio.graphql.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.spring.web.servlet.JsonSerializer;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShJacksonJsonSerializer implements JsonSerializer {
  private static final Logger logger = LogManager.getLogger(ShJacksonJsonSerializer.class);
  private ObjectMapper objectMapper;

  @Autowired
  public ShJacksonJsonSerializer(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public String serialize(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (IOException e) {
      logger.error("Error serializing object to JSON: {}", e.getMessage(), e);
    }
    return null;
  }

  @Override
  public <T> T deserialize(String json, Class<T> requiredType) {
    try {
      return objectMapper.readValue(json, requiredType);
    } catch (IOException e) {
      logger.error("Error deserializing object from JSON: {}", e.getMessage(), e);
    }
    return null;
  }
}
