
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
package com.viglet.shio.graphql;

import static graphql.Scalars.GraphQLID;
import static graphql.schema.FieldCoordinates.coordinates;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLInputObjectType.newInputObject;
import static graphql.schema.GraphQLList.list;
import static graphql.schema.GraphQLNonNull.nonNull;
import static graphql.schema.GraphqlTypeComparatorRegistry.BY_NAME_REGISTRY;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shio.persistence.model.object.ShObject;
import com.viglet.shio.persistence.model.post.ShPost;
import com.viglet.shio.persistence.model.post.type.ShPostType;
import com.viglet.shio.persistence.repository.object.ShObjectRepository;
import com.viglet.shio.utils.ShPostUtils;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLInputObjectType;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLObjectType.Builder;

/**
 * GraphQL Query Type Unique.
 *
 * @author Alexandre Oliveira
 * @since 0.3.7
 */
@Component
public class ShGraphQLQTUnique {

	@Autowired
	private ShObjectRepository shObjectRepository;
	@Autowired
	private ShGraphQLUtils shGraphQLUtils;
	@Autowired
	private ShPostUtils shPostUtils;

	private String getPostTypeNameUnique(ShPostType shPostType) {
		return shGraphQLUtils.normalizedPostType(shPostType.getName());
	}

	public void createQueryTypeUnique(Builder queryTypeBuilder,
			graphql.schema.GraphQLCodeRegistry.Builder codeRegistryBuilder, ShPostType shPostType,
			GraphQLObjectType graphQLObjectType) {
		String postTypeName = this.getPostTypeNameUnique(shPostType);

		GraphQLInputObjectType.Builder postTypeWhereUniqueInputBuilder = newInputObject()
				.name(shPostType.getName().concat(ShGraphQLConstants.WHERE_UNIQUE_INPUT))
				.description(String.format("References %s record uniquely", shPostType.getName()));

		this.whereFieldsUnique(postTypeWhereUniqueInputBuilder);

		GraphQLInputObjectType postTypeWhereUniqueInput = postTypeWhereUniqueInputBuilder
				.comparatorRegistry(BY_NAME_REGISTRY).build();

		queryTypeBuilder.field(newFieldDefinition().name(postTypeName).type(graphQLObjectType)
				.argument(newArgument().name(ShGraphQLConstants.STAGE_ARG)
						.description("A required enumeration indicating the current content Stage (defaults to DRAFT)")
						.type(nonNull(ShGraphQLConstants.stageEnum)).defaultValue(20))
				.argument(newArgument().name(ShGraphQLConstants.LOCALES_ARG)
						.description("A required array of one or more locales, defaults to the project's default.")
						.type(nonNull(list(ShGraphQLConstants.localeEnum))).defaultValue("EN"))
				.argument(newArgument().name(ShGraphQLConstants.WHERE_ARG)
						.description("An optional object type to filter the content based on a nested set of criteria.")
						.type(nonNull(postTypeWhereUniqueInput))));

		codeRegistryBuilder.dataFetcher(coordinates(ShGraphQLConstants.QUERY_TYPE, postTypeName),
				this.getPostTypeAllDataFetcherUnique(shPostType));
	}

	private void whereFieldsUnique(GraphQLInputObjectType.Builder postTypeWhereInputBuilder) {
		shGraphQLUtils.createInputObjectFieldCondition(postTypeWhereInputBuilder, ShGraphQLConstants.ID, null,
				GraphQLID, "Identifier");
	}

	private DataFetcher<Map<String, String>> getPostTypeAllDataFetcherUnique(ShPostType shPostType) {
		return dataFetchingEnvironment -> {
			Map<String, String> post = new HashMap<>();

			Map<String, Object> whereMap = dataFetchingEnvironment.getArgument(ShGraphQLConstants.WHERE_ARG);

			if (whereMap != null) {
				for (Entry<String, Object> whereArgItem : whereMap.entrySet()) {
					String arg = whereArgItem.getKey();
					if (arg.equals(ShGraphQLConstants.ID)) {
						String objectId = whereMap.get(ShGraphQLConstants.ID).toString();
						ShObject shObject = shObjectRepository.findById(objectId).orElse(null);
						post = shPostUtils.postAttrGraphQL((ShPost) shObject);
					}
				}
			}
			return post;
		};
	}
}
