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
package com.viglet.shio.persistence.repository.post;

import com.viglet.shio.persistence.model.post.ShPostAttr;
import com.viglet.shio.persistence.model.post.impl.ShPostImpl;
import com.viglet.shio.persistence.model.post.relator.impl.ShRelatorItemImpl;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Alexandre Oliveira
 */
@Repository
public interface ShPostAttrRepository
    extends JpaRepository<ShPostAttr, String>, JpaSpecificationExecutor<ShPostAttr> {

  Set<ShPostAttr> findByArrayValueIn(Collection<String> values);

  List<ShPostAttr> findAll();

  @Query("select p from ShPostAttr p JOIN FETCH p.shPostTypeAttr where p.shPost = ?1")
  Set<ShPostAttr> findByShPostAll(ShPostImpl shPost);

  Set<ShPostAttr> findByShPost(ShPostImpl shPost);

  @Query("select p from ShPostAttr p JOIN FETCH p.shPostTypeAttr where p.shParentRelatorItem = ?1")
  Set<ShPostAttr> findByShParentRelatorItemJoin(ShRelatorItemImpl shRelatorItem);

  Set<ShPostAttr> findByShParentRelatorItem(ShRelatorItemImpl shRelatorItem);

  @Query("select p from ShPostAttr p JOIN FETCH p.shPostTypeAttr where p.id = ?1")
  Optional<ShPostAttr> findByIdAll(String id);

  Optional<ShPostAttr> findById(String id);

  @SuppressWarnings("unchecked")
  ShPostAttr save(ShPostAttr shPostAttr);

  @Modifying
  @Query("delete from ShPostAttr pa where pa.id = ?1")
  void delete(String shPostAttrId);
}
