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

package org.jraf.klibnotion.model.property.spec

/**
 * A rollup uses a relation property that exists on the current database and extracts a property of
 * the related pages as an input to a chosen function.
 *
 * See [Reference](https://developers.notion.com/reference/database).
 */
interface RollupPropertySpec : PropertySpec {
    /**
     * The name of the relation property this property is responsible for rolling up.
     */
    val relationPropertyName: String

    /**
     * The ID of the relation property this property is responsible for rolling up.
     */
    val relationPropertyId: String

    /**
     * The name of the property of the pages in the related database that is used as an input to [function].
     */
    val rollupPropertyName: String

    /**
     * The ID of the property of the pages in the related database that is used as an input to [function].
     */
    val rollupPropertyId: String

    /**
     * The function that is evaluated for every page in the relation of the rollup.
     */
    val function: RollupFunction

    enum class RollupFunction {
        /**
         * This type is returned when a type unknown to this library is returned by the Notion API.
         */
        _UNKNOWN,

        COUNT_ALL,
        COUNT_VALUES,
        COUNT_UNIQUE_VALUES,
        COUNT_EMPTY,
        COUNT_NOT_EMPTY,
        PERCENT_EMPTY,
        PERCENT_NOT_EMPTY,
        SUM,
        AVERAGE,
        MEDIAN,
        MIN,
        MAX,
        RANGE,
    }
}
