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

package org.jraf.klibnotion.internal.api.model.property

import org.jraf.klibnotion.internal.api.model.ApiConverter
import org.jraf.klibnotion.internal.api.model.apiToModel
import org.jraf.klibnotion.internal.model.property.CheckboxPropertyImpl
import org.jraf.klibnotion.internal.model.property.CreatedByPropertyImpl
import org.jraf.klibnotion.internal.model.property.CreatedTimePropertyImpl
import org.jraf.klibnotion.internal.model.property.DatePropertyImpl
import org.jraf.klibnotion.internal.model.property.EmailPropertyImpl
import org.jraf.klibnotion.internal.model.property.FilePropertyImpl
import org.jraf.klibnotion.internal.model.property.FormulaPropertyImpl
import org.jraf.klibnotion.internal.model.property.LastEditedByPropertyImpl
import org.jraf.klibnotion.internal.model.property.LastEditedTimePropertyImpl
import org.jraf.klibnotion.internal.model.property.MultiSelectPropertyImpl
import org.jraf.klibnotion.internal.model.property.NumberPropertyImpl
import org.jraf.klibnotion.internal.model.property.PeoplePropertyImpl
import org.jraf.klibnotion.internal.model.property.PhonePropertyImpl
import org.jraf.klibnotion.internal.model.property.RelationPropertyImpl
import org.jraf.klibnotion.internal.model.property.RollupPropertyImpl
import org.jraf.klibnotion.internal.model.property.SelectPropertyImpl
import org.jraf.klibnotion.internal.model.property.TextPropertyImpl
import org.jraf.klibnotion.internal.model.property.TitlePropertyImpl
import org.jraf.klibnotion.internal.model.property.UnknownTypePropertyImpl
import org.jraf.klibnotion.internal.model.property.UrlPropertyImpl
import org.jraf.klibnotion.model.property.NumberProperty.NumberFormat
import org.jraf.klibnotion.model.property.Property
import org.jraf.klibnotion.model.property.RollupProperty.RollupFunction

internal object ApiPropertyConverter : ApiConverter<Pair<String, ApiProperty>, Property>() {
    override fun apiToModel(apiModel: Pair<String, ApiProperty>): Property {
        val (name, apiProperty) = apiModel
        val id = apiProperty.id
        return when (apiProperty.type) {
            "title" -> TitlePropertyImpl(name = name, id = id)
            "text" -> TextPropertyImpl(name = name, id = id)
            "number" -> NumberPropertyImpl(
                name = name,
                id = id,
                format = when (apiProperty.number!!.format) {
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
            "select" -> SelectPropertyImpl(
                name = name,
                id = id,
                options = apiProperty.select!!.options.apiToModel(ApiPropertySelectOptionConverter)
            )
            "multi_select" -> MultiSelectPropertyImpl(
                name = name,
                id = id,
                options = apiProperty.select!!.options.apiToModel(ApiPropertySelectOptionConverter)
            )

            "date" -> DatePropertyImpl(name = name, id = id)
            "people" -> PeoplePropertyImpl(name = name, id = id)
            "file" -> FilePropertyImpl(name = name, id = id)
            "checkbox" -> CheckboxPropertyImpl(name = name, id = id)
            "url" -> UrlPropertyImpl(name = name, id = id)
            "email" -> EmailPropertyImpl(name = name, id = id)
            "phone" -> PhonePropertyImpl(name = name, id = id)
            "formula" -> FormulaPropertyImpl(
                name = name,
                id = id,
                value = apiProperty.formula!!.value
            )
            "relation" -> RelationPropertyImpl(
                name = name,
                id = id,
                databaseId = apiProperty.relation!!.database_id,
                syncedPropertyName = apiProperty.relation.synced_property_name,
                syncedPropertyId = apiProperty.relation.synced_property_id
            )
            "rollup" -> RollupPropertyImpl(
                name = name,
                id = id,
                relationPropertyName = apiProperty.rollup!!.relation_property_name,
                relationPropertyId = apiProperty.rollup.relation_property_id,
                rollupPropertyName = apiProperty.rollup.rollup_property_name,
                rollupPropertyId = apiProperty.rollup.rollup_property_id,
                function = when (apiProperty.rollup.function) {
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
            "created_time" -> CreatedTimePropertyImpl(name = name, id = id)
            "created_by" -> CreatedByPropertyImpl(name = name, id = id)
            "last_edited_time" -> LastEditedTimePropertyImpl(name = name, id = id)
            "last_edited_by" -> LastEditedByPropertyImpl(name = name, id = id)
            else -> UnknownTypePropertyImpl(name = name, id = id, type = apiProperty.type)
        }
    }
}