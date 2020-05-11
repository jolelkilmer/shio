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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.viglet.shio.graphql.schema.ShGraphQLConstants;
import com.viglet.shio.persistence.model.post.ShPost;
import com.viglet.shio.persistence.model.post.ShPostAttr;
import com.viglet.shio.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shio.persistence.model.site.ShSite;

/**
 * @author Alexandre Oliveira
 * @since 0.3.7
 */
public class ShPostAttrSpecs {
	private final static String EQUAL = "equal";
	private final static String IN = "in";
	private final static String NOT_IN = "not_in";
	private final static String CONTAINS = "contains";
	private final static String NOT_CONTAINS = "not_contains";
	private final static String STARTS_WITH = "starts_with";
	private final static String NOT_STARTS_WITH = "not_starts_with";
	private final static String ENDS_WITH = "ends_with";
	private final static String NOT_ENDS_WITH = "not_ends_with";

	public static Specification<ShPostAttr> hasShPostTypeAttr(ShPostTypeAttr shPostTypeAttr) {
		return (shPostAttr, query, cb) -> cb.equal(shPostAttr.get("shPostTypeAttr"), shPostTypeAttr);
	}

	public static Specification<ShPostAttr> hasSites(List<ShSite> shSites) {
		return (shPostAttr, query, cb) -> {
			final Path<ShPost> shPost = shPostAttr.<ShPost>get("shPost");

			query.distinct(true);
			Subquery<ShPost> postSubQuery = query.subquery(ShPost.class);
			Root<ShPost> post = postSubQuery.from(ShPost.class);

			final Path<ShSite> shSitePath = post.<ShSite>get("shSite");

			postSubQuery.select(post);
			postSubQuery.where(shSitePath.in(shSites));

			return shPost.in(postSubQuery);
		};
	}

	public static Specification<ShPostAttr> conditionParams(String attrName, String attrValue, String condition) {
		return new Specification<ShPostAttr>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<ShPostAttr> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				if (attrName.startsWith("_")) {
					if (attrName.equals(ShGraphQLConstants.FURL)) {
					}
				} else {
					if (StringUtils.isEmpty(condition) || condition.equals(EQUAL)) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("strValue"), attrValue)));
					} else if (condition.equals(IN)) {

					} else if (condition.equals(NOT_IN)) {

					} else if (condition.equals(CONTAINS)) {
						predicates.add(criteriaBuilder
								.and(criteriaBuilder.like(root.get("strValue"), String.format("%%%s%%", attrValue))));
					} else if (condition.equals(NOT_CONTAINS)) {
						predicates.add(criteriaBuilder.and(
								criteriaBuilder.notLike(root.get("strValue"), String.format("%%%s%%", attrValue))));
					} else if (condition.equals(STARTS_WITH)) {
						predicates.add(criteriaBuilder
								.and(criteriaBuilder.like(root.get("strValue"), String.format("%s%%", attrValue))));
					} else if (condition.equals(NOT_STARTS_WITH)) {
						predicates.add(criteriaBuilder
								.and(criteriaBuilder.notLike(root.get("strValue"), String.format("%s%%", attrValue))));
					} else if (condition.equals(ENDS_WITH)) {
						predicates.add(criteriaBuilder
								.and(criteriaBuilder.like(root.get("strValue"), String.format("%%%s", attrValue))));
					} else if (condition.equals(NOT_ENDS_WITH)) {
						predicates.add(criteriaBuilder
								.and(criteriaBuilder.notLike(root.get("strValue"), String.format("%%%s", attrValue))));
					}
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
}
