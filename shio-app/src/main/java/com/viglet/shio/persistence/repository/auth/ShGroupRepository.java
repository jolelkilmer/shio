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
package com.viglet.shio.persistence.repository.auth;

import com.viglet.shio.persistence.model.auth.ShGroup;
import com.viglet.shio.persistence.model.auth.ShUser;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Alexandre Oliveira
 */
@Repository
public interface ShGroupRepository extends JpaRepository<ShGroup, String> {

  List<ShGroup> findAll();

  @SuppressWarnings("unchecked")
  ShGroup save(ShGroup shGroup);

  ShGroup findByName(String name);

  Set<ShGroup> findByShUsersIn(Collection<ShUser> users);

  int countByNameAndShUsersIn(String name, Collection<ShUser> shUsers);

  @Modifying
  @Query("delete from ShGroup g where g.id = ?1")
  void delete(String id);
}
