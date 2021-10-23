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

package org.jraf.klibnotion.internal.api.model.property.value.rollup

import org.jraf.klibnotion.internal.api.model.ApiConverter
import org.jraf.klibnotion.internal.api.model.apiToModel
import org.jraf.klibnotion.internal.api.model.base.ApiNumberConverter
import org.jraf.klibnotion.internal.api.model.date.ApiDateConverter
import org.jraf.klibnotion.internal.api.model.property.value.ApiPropertyValue
import org.jraf.klibnotion.internal.api.model.property.value.ApiPropertyValueConverter
import org.jraf.klibnotion.internal.model.property.value.rollup.ArrayRollupPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.rollup.DateRollupPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.rollup.NumberRollupPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.rollup.UnknownTypeRollupPropertyValueImpl
import org.jraf.klibnotion.model.property.value.RollupPropertyValue

internal object ApiPropertyValueRollupConverter :
    ApiConverter<Pair<String, ApiPropertyValue>, RollupPropertyValue<*>>() {
    override fun apiToModel(apiModel: Pair<String, ApiPropertyValue>): RollupPropertyValue<*> {
        val (name, apiPropertyValue) = apiModel
        val id = apiPropertyValue.id
        val rollup = apiPropertyValue.rollup!!
        return when (val type = rollup.type) {
            "number" -> NumberRollupPropertyValueImpl(
                id = id,
                name = name,
                value = rollup.number?.apiToModel(ApiNumberConverter)
            )
            "date" -> DateRollupPropertyValueImpl(
                id = id,
                name = name,
                value = rollup.date?.apiToModel(ApiDateConverter)
            )
            "array" -> ArrayRollupPropertyValueImpl(
                id = id,
                name = name,
                value = rollup.array?.map { ("" to it).apiToModel(ApiPropertyValueConverter) }
            )

            else -> UnknownTypeRollupPropertyValueImpl(
                id = id,
                name = name,
                type = type,
            )
        }
    }
}