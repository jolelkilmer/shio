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
package com.viglet.shio.persistence.spec.post;

import java.util.List;

import javax.persistence.criteria.Path;

import org.springframework.data.jpa.domain.Specification;

import com.viglet.shio.persistence.model.post.ShPost;
import com.viglet.shio.persistence.model.post.type.ShPostType;
import com.viglet.shio.persistence.model.site.ShSite;

/**
 * @author Alexandre Oliveira
 * @since 0.3.7
 */
public class ShPostSpecs {
	
	public static Specification<ShPost> hasShPostType(ShPostType shPostType) {
		return (shPost, query, cb) -> cb.equal(shPost.get("shPostType"), shPostType);
	}

	public static Specification<ShPost> hasSites(List<ShSite> shSites) {
		return (shPost, query, cb) -> {
			query.distinct(true);
			final Path<ShSite> shSitePath = shPost.<ShSite>get("shSite");

			return shSitePath.in(shSites);
		};
	}

	public static Specification<ShPost> hasPosts(List<ShPost> shPosts) {
		return (shPost, query, cb) -> {
			query.distinct(true);
			return shPost.in(shPosts);
		};
	}
}
