/*
 * This source is part of the
 *      _____  ___   ____
 *  __ / / _ \/ _ | / __/___  _______ _
 * / // / , _/ __ |/ _/_/ _ \/ __/ _ `/
 * \___/_/|_/_/ |_/_/ (_)___/_/  \_, /
 *                              /___/
 * repository.
 *
 * Copyright (C) 2021-present Benoit 'BoD' Lubek (BoD@JRAF.org)
 * and contributors (https://github.com/BoD/klibnotion/graphs/contributors)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jraf.klibnotion.internal.api.model.property.spec

import org.jraf.klibnotion.internal.api.model.ApiConverter
import org.jraf.klibnotion.internal.api.model.apiToModel
import org.jraf.klibnotion.internal.api.model.modelToApi
import org.jraf.klibnotion.internal.api.model.property.ApiSelectOptionConverter
import org.jraf.klibnotion.internal.model.property.spec.CheckboxPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.CreatedByPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.CreatedTimePropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.DatePropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.EmailPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.FilesPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.FormulaPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.LastEditedByPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.LastEditedTimePropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.MultiSelectPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.NumberPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.PeoplePropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.PhoneNumberPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.RelationPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.RichTextPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.RollupPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.SelectPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.TitlePropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.UnknownTypePropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.UrlPropertySpecImpl
import org.jraf.klibnotion.model.property.spec.CheckboxPropertySpec
import org.jraf.klibnotion.model.property.spec.CreatedByPropertySpec
import org.jraf.klibnotion.model.property.spec.CreatedTimePropertySpec
import org.jraf.klibnotion.model.property.spec.DatePropertySpec
import org.jraf.klibnotion.model.property.spec.EmailPropertySpec
import org.jraf.klibnotion.model.property.spec.FilesPropertySpec
import org.jraf.klibnotion.model.property.spec.FormulaPropertySpec
import org.jraf.klibnotion.model.property.spec.LastEditedByPropertySpec
import org.jraf.klibnotion.model.property.spec.LastEditedTimePropertySpec
import org.jraf.klibnotion.model.property.spec.MultiSelectPropertySpec
import org.jraf.klibnotion.model.property.spec.NumberPropertySpec
import org.jraf.klibnotion.model.property.spec.PeoplePropertySpec
import org.jraf.klibnotion.model.property.spec.PhoneNumberPropertySpec
import org.jraf.klibnotion.model.property.spec.PropertySpec
import org.jraf.klibnotion.model.property.spec.RelationPropertySpec
import org.jraf.klibnotion.model.property.spec.RichTextPropertySpec
import org.jraf.klibnotion.model.property.spec.RollupPropertySpec
import org.jraf.klibnotion.model.property.spec.SelectPropertySpec
import org.jraf.klibnotion.model.property.spec.TitlePropertySpec
import org.jraf.klibnotion.model.property.spec.UnknownTypePropertySpec
import org.jraf.klibnotion.model.property.spec.UrlPropertySpec

internal object ApiPropertySpecConverter : ApiConverter<Pair<String, ApiPropertySpec>, PropertySpec>() {
    override fun apiToModel(apiModel: Pair<String, ApiPropertySpec>): PropertySpec {
        val (name, apiPropertySpec) = apiModel
        val id = apiPropertySpec.id!!
        return when (apiPropertySpec.type) {
            "title" -> TitlePropertySpecImpl(name = name, id = id)
            "rich_text" -> RichTextPropertySpecImpl(name = name, id = id)
            "number" -> NumberPropertySpecImpl(
                name = name,
                id = id,
                format = apiPropertySpec.number!!.format.apiToModel(ApiPropertySpecNumberFormatConverter)

            )
            "select" -> SelectPropertySpecImpl(
                name = name,
                id = id,
                options = apiPropertySpec.select!!.options.apiToModel(ApiSelectOptionConverter)
            )
            "multi_select" -> MultiSelectPropertySpecImpl(
                name = name,
                id = id,
                options = apiPropertySpec.multi_select!!.options.apiToModel(ApiSelectOptionConverter)
            )

            "date" -> DatePropertySpecImpl(name = name, id = id)
            "people" -> PeoplePropertySpecImpl(name = name, id = id)
            "files" -> FilesPropertySpecImpl(name = name, id = id)
            "checkbox" -> CheckboxPropertySpecImpl(name = name, id = id)
            "url" -> UrlPropertySpecImpl(name = name, id = id)
            "email" -> EmailPropertySpecImpl(name = name, id = id)
            "phone_number" -> PhoneNumberPropertySpecImpl(name = name, id = id)
            "formula" -> FormulaPropertySpecImpl(
                name = name,
                id = id,
                expression = apiPropertySpec.formula!!.expression
            )
            "relation" -> RelationPropertySpecImpl(
                name = name,
                id = id,
                databaseId = apiPropertySpec.relation!!.database_id,
                syncedPropertyName = apiPropertySpec.relation.synced_property_name,
                syncedPropertyId = apiPropertySpec.relation.synced_property_id
            )
            "rollup" -> RollupPropertySpecImpl(
                name = name,
                id = id,
                relationPropertyName = apiPropertySpec.rollup!!.relation_property_name,
                relationPropertyId = apiPropertySpec.rollup.relation_property_id,
                rollupPropertyName = apiPropertySpec.rollup.rollup_property_name,
                rollupPropertyId = apiPropertySpec.rollup.rollup_property_id,
                function = apiPropertySpec.rollup.function.apiToModel(ApiPropertySpecRollupFunctionConverter)
            )
            "created_time" -> CreatedTimePropertySpecImpl(name = name, id = id)
            "created_by" -> CreatedByPropertySpecImpl(name = name, id = id)
            "last_edited_time" -> LastEditedTimePropertySpecImpl(name = name, id = id)
            "last_edited_by" -> LastEditedByPropertySpecImpl(name = name, id = id)
            else -> UnknownTypePropertySpecImpl(name = name, id = id, type = apiPropertySpec.type)
        }
    }

    override fun modelToApi(model: PropertySpec): Pair<String, ApiPropertySpec> {
        return model.name to when (model) {
            is PhoneNumberPropertySpec -> ApiPropertySpec(type = "phone_number", phone_number = ApiEmpty())
            is FormulaPropertySpec -> ApiPropertySpec(
                type = "formula",
                formula = ApiPropertySpecFormula(model.expression)
            )
            is RollupPropertySpec -> ApiPropertySpec(
                type = "rollup",
                rollup = ApiPropertySpecRollup(
                    relation_property_name = model.relationPropertyName,
                    relation_property_id = model.relationPropertyId,
                    rollup_property_name = model.rollupPropertyName,
                    rollup_property_id = model.rollupPropertyId,
                    function = model.function.modelToApi(ApiPropertySpecRollupFunctionConverter),
                )
            )
            is PeoplePropertySpec -> ApiPropertySpec(type = "people", people = ApiEmpty())
            is SelectPropertySpec -> ApiPropertySpec(
                type = "select",
                select = ApiPropertySpecSelect(model.options.modelToApi(ApiSelectOptionConverter))
            )
            is CheckboxPropertySpec -> ApiPropertySpec(type = "checkbox", checkbox = ApiEmpty())
            is RelationPropertySpec -> ApiPropertySpec(
                type = "relation",
                relation = ApiPropertySpecRelation(
                    database_id = model.databaseId,
                    synced_property_name = model.syncedPropertyName,
                    synced_property_id = model.syncedPropertyId,
                )
            )
            is MultiSelectPropertySpec -> ApiPropertySpec(
                type = "multi_select",
                multi_select = ApiPropertySpecMultiSelect(model.options.modelToApi(ApiSelectOptionConverter))
            )
            is DatePropertySpec -> ApiPropertySpec(type = "date", date = ApiEmpty())
            is LastEditedTimePropertySpec -> ApiPropertySpec(type = "last_edited_time", last_edited_time = ApiEmpty())
            is RichTextPropertySpec -> ApiPropertySpec(type = "rich_text", rich_text = ApiEmpty())
            is NumberPropertySpec -> ApiPropertySpec(
                type = "number",
                number = ApiPropertySpecNumber(model.format.modelToApi(ApiPropertySpecNumberFormatConverter))
            )
            is LastEditedByPropertySpec -> ApiPropertySpec(type = "last_edited_by", last_edited_by = ApiEmpty())
            is UrlPropertySpec -> ApiPropertySpec(type = "url", url = ApiEmpty())
            is CreatedTimePropertySpec -> ApiPropertySpec(type = "created_time", created_time = ApiEmpty())
            is TitlePropertySpec -> ApiPropertySpec(type = "title", title = ApiEmpty())
            is CreatedByPropertySpec -> ApiPropertySpec(type = "created_by", created_by = ApiEmpty())
            is FilesPropertySpec -> ApiPropertySpec(type = "files", files = ApiEmpty())
            is EmailPropertySpec -> ApiPropertySpec(type = "email", email = ApiEmpty())

            is UnknownTypePropertySpec -> throw IllegalStateException()
        }
    }
}
