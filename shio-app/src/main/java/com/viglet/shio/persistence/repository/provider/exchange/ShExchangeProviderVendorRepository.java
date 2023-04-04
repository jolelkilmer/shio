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
package com.viglet.shio.persistence.repository.provider.exchange;

import com.viglet.shio.persistence.model.provider.exchange.ShExchangeProviderVendor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Alexandre Oliveira
 */
@Repository
public interface ShExchangeProviderVendorRepository
    extends JpaRepository<ShExchangeProviderVendor, String> {

  List<ShExchangeProviderVendor> findAll();

  Optional<ShExchangeProviderVendor> findById(String id);

  @SuppressWarnings("unchecked")
  ShExchangeProviderVendor save(ShExchangeProviderVendor shExchangeProviderVendor);

  @Modifying
  @Query("delete from ShExchangeProviderVendor epv where epv.id = ?1")
  void delete(String id);
}
