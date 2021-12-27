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
import org.jraf.klibnotion.model.property.spec.RollupPropertySpec.RollupFunction

internal object ApiPropertySpecRollupFunctionConverter : ApiConverter<String, RollupFunction>() {
    override fun apiToModel(apiModel: String): RollupFunction {
        return when (apiModel) {
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
    }

    override fun modelToApi(model: RollupFunction): String {
        return when (model) {
            RollupFunction.COUNT_ALL -> "count_all"
            RollupFunction.COUNT_VALUES -> "count_values"
            RollupFunction.COUNT_UNIQUE_VALUES -> "count_unique_values"
            RollupFunction.COUNT_EMPTY -> "count_empty"
            RollupFunction.COUNT_NOT_EMPTY -> "count_not_empty"
            RollupFunction.PERCENT_EMPTY -> "percent_empty"
            RollupFunction.PERCENT_NOT_EMPTY -> "percent_not_empty"
            RollupFunction.SUM -> "sum"
            RollupFunction.AVERAGE -> "average"
            RollupFunction.MEDIAN -> "median"
            RollupFunction.MIN -> "min"
            RollupFunction.MAX -> "max"
            RollupFunction.RANGE -> "range"
            RollupFunction._UNKNOWN -> throw IllegalStateException()
        }

    }
}
