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
package com.viglet.shio.api;

import java.io.Serializable;

/**
 * @author Alexandre Oliveira
 */
public class ShJsonView implements Serializable {
  private static final long serialVersionUID = 3989499187492868996L;

  public static interface ShJsonViewGenericType {}

  public static interface ShJsonViewObject extends ShJsonViewGenericType {}

  public static interface ShJsonViewReference extends ShJsonViewGenericType {}

  public static interface ShJsonViewPostType extends ShJsonViewGenericType {}

  public static interface ShJsonViewPost extends ShJsonViewGenericType {}

  public static interface ShJsonViewPostTypeAttr extends ShJsonViewGenericType {}
}
