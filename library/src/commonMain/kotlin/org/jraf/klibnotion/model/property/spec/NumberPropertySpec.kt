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
 * See [Reference](https://developers.notion.com/reference/database).
 */
interface NumberPropertySpec : PropertySpec {
    /**
     * How the number is displayed in Notion.
     */
    val format: NumberFormat

    enum class NumberFormat {
        /**
         * This type is returned when a type unknown to this library is returned by the Notion API.
         */
        _UNKNOWN,

        NUMBER,
        NUMBER_WITH_COMMAS,
        PERCENT,
        DOLLAR,
        EURO,
        POUND,
        YEN,
        RUBLE,
        RUPEE,
        WON,
        YUAN,
        CANADIAN_DOLLAR,
        REAL,
        LIRA,
        RUPIAH,
        FRANC,
        HONG_KONG_DOLLAR,
        NEW_ZEALAND_DOLLAR,
        KRONA,
        NORWEGIAN_KRONE,
        MEXICAN_PESO,
        RAND,
        NEW_TAIWAN_DOLLAR,
        DANISH_KRONE,
        ZLOTY,
        BAHT,
        FORINT,
        KORUNA,
        SHEKEL,
        CHILEAN_PESO,
        PHILIPPINE_PESO,
        DIRHAM,
        COLOMBIAN_PESO,
        RIYAL,
        RINGGIT,
        LEU
    }
}
