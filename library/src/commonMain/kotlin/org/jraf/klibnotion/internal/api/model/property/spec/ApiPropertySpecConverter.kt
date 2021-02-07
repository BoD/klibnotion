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
import org.jraf.klibnotion.internal.api.model.property.ApiSelectOptionConverter
import org.jraf.klibnotion.internal.model.property.spec.CheckboxPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.CreatedByPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.CreatedTimePropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.DatePropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.EmailPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.FilePropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.FormulaPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.LastEditedByPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.LastEditedTimePropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.MultiSelectPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.NumberPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.PeoplePropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.PhoneNumberPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.RelationPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.RollupPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.SelectPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.TextPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.TitlePropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.UnknownTypePropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.UrlPropertySpecImpl
import org.jraf.klibnotion.model.property.spec.NumberPropertySpec.NumberFormat
import org.jraf.klibnotion.model.property.spec.PropertySpec
import org.jraf.klibnotion.model.property.spec.RollupPropertySpec.RollupFunction

internal object ApiPropertySpecConverter : ApiConverter<Pair<String, ApiPropertySpec>, PropertySpec>() {
    override fun apiToModel(apiModel: Pair<String, ApiPropertySpec>): PropertySpec {
        val (name, apiPropertySpec) = apiModel
        val id = apiPropertySpec.id
        return when (apiPropertySpec.type) {
            "title" -> TitlePropertySpecImpl(name = name, id = id)
            "text" -> TextPropertySpecImpl(name = name, id = id)
            "number" -> NumberPropertySpecImpl(
                name = name,
                id = id,
                format = when (apiPropertySpec.number!!.format) {
                    "number" -> NumberFormat.NUMBER
                    "number_with_commas" -> NumberFormat.NUMBER_WITH_COMMAS
                    "percent" -> NumberFormat.PERCENT
                    "dollar" -> NumberFormat.DOLLAR
                    "euro" -> NumberFormat.EURO
                    "pound" -> NumberFormat.POUND
                    "yen" -> NumberFormat.YEN
                    "ruble" -> NumberFormat.RUBLE
                    "rupee" -> NumberFormat.RUPEE
                    "won" -> NumberFormat.WON
                    "yuan" -> NumberFormat.YUAN
                    else -> NumberFormat._UNKNOWN
                }
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
            "file" -> FilePropertySpecImpl(name = name, id = id)
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
                function = when (apiPropertySpec.rollup.function) {
                    "count_all" -> RollupFunction.COUNT_ALL
                    "count_values" -> RollupFunction.COUNT_VALUES
                    "count_unique_values" -> RollupFunction.COUNT_UNIQUE_VALUES
                    "count_empty" -> RollupFunction.COUNT_EMPTY
                    "count_not_empty" -> RollupFunction.COUNT_NOT_EMPTY
                    "percent_empty" -> RollupFunction.PERCENT_EMPTY
                    "percent_not_empty" -> RollupFunction.PERCENT_NOT_EMPTY
                    "sum" -> RollupFunction.SUM
                    "average" -> RollupFunction.AVERAGE
                    "median" -> RollupFunction.MEDIAN
                    "min" -> RollupFunction.MIN
                    "max" -> RollupFunction.MAX
                    "range" -> RollupFunction.RANGE
                    else -> RollupFunction._UNKNOWN
                }
            )
            "created_time" -> CreatedTimePropertySpecImpl(name = name, id = id)
            "created_by" -> CreatedByPropertySpecImpl(name = name, id = id)
            "last_edited_time" -> LastEditedTimePropertySpecImpl(name = name, id = id)
            "last_edited_by" -> LastEditedByPropertySpecImpl(name = name, id = id)
            else -> UnknownTypePropertySpecImpl(name = name, id = id, type = apiPropertySpec.type)
        }
    }
}