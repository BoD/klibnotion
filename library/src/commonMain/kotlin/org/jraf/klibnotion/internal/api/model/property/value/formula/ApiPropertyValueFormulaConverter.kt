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

package org.jraf.klibnotion.internal.api.model.property.value.formula

import org.jraf.klibnotion.internal.api.model.ApiConverter
import org.jraf.klibnotion.internal.api.model.base.ApiNumberConverter
import org.jraf.klibnotion.internal.api.model.property.value.ApiPropertyValue
import org.jraf.klibnotion.internal.model.property.value.formula.BooleanFormulaPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.formula.NumberFormulaPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.formula.StringFormulaPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.formula.UnknownTypeFormulaPropertyValueImpl
import org.jraf.klibnotion.model.property.value.formula.FormulaPropertyValue

internal object ApiPropertyValueFormulaConverter :
    ApiConverter<Pair<String, ApiPropertyValue>, FormulaPropertyValue>() {
    override fun apiToModel(apiModel: Pair<String, ApiPropertyValue>): FormulaPropertyValue {
        val (name, apiPropertyValue) = apiModel
        val id = apiPropertyValue.id
        val formula = apiPropertyValue.formula!!
        return when (val type = formula.type) {
            "string" -> StringFormulaPropertyValueImpl(
                id = id,
                name = name,
                value = formula.string!!
            )
            "number" -> NumberFormulaPropertyValueImpl(
                id = id,
                name = name,
                value = ApiNumberConverter.apiToModel(formula.number!!)
            )
            "boolean" -> BooleanFormulaPropertyValueImpl(
                id = id,
                name = name,
                value = formula.boolean!!,
            )
            else -> UnknownTypeFormulaPropertyValueImpl(
                id = id,
                name = name,
                type = type,
            )
        }
    }
}