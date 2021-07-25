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
import org.jraf.klibnotion.model.property.spec.NumberPropertySpec.NumberFormat

internal object ApiPropertySpecNumberFormatConverter : ApiConverter<String, NumberFormat>() {
    override fun apiToModel(apiModel: String): NumberFormat {
        return when (apiModel) {
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
            "canadian_dollar" -> NumberFormat.CANADIAN_DOLLAR
            "real" -> NumberFormat.REAL
            "lira" -> NumberFormat.LIRA
            "rupiah" -> NumberFormat.RUPIAH
            "franc" -> NumberFormat.FRANC
            "hong_kong_dollar" -> NumberFormat.HONG_KONG_DOLLAR
            "new_zealand_dollar" -> NumberFormat.NEW_ZEALAND_DOLLAR
            "krona" -> NumberFormat.KRONA
            "norwegian_krone" -> NumberFormat.NORWEGIAN_KRONE
            "mexican_peso" -> NumberFormat.MEXICAN_PESO
            "rand" -> NumberFormat.RAND
            "new_taiwan_dollar" -> NumberFormat.NEW_TAIWAN_DOLLAR
            "danish_krone" -> NumberFormat.DANISH_KRONE
            "zloty" -> NumberFormat.ZLOTY
            "baht" -> NumberFormat.BAHT
            "forint" -> NumberFormat.FORINT
            "koruna" -> NumberFormat.KORUNA
            "shekel" -> NumberFormat.SHEKEL
            "chilean_peso" -> NumberFormat.CHILEAN_PESO
            "philippine_peso" -> NumberFormat.PHILIPPINE_PESO
            "dirham" -> NumberFormat.DIRHAM
            "colombian_peso" -> NumberFormat.COLOMBIAN_PESO
            "riyal" -> NumberFormat.RIYAL
            "ringgit" -> NumberFormat.RINGGIT
            "leu" -> NumberFormat.LEU

            else -> NumberFormat._UNKNOWN
        }
    }

    override fun modelToApi(model: NumberFormat): String {
        return when (model) {
            NumberFormat.NUMBER -> "number"
            NumberFormat.NUMBER_WITH_COMMAS -> "number_with_commas"
            NumberFormat.PERCENT -> "percent"
            NumberFormat.DOLLAR -> "dollar"
            NumberFormat.EURO -> "euro"
            NumberFormat.POUND -> "pound"
            NumberFormat.YEN -> "yen"
            NumberFormat.RUBLE -> "ruble"
            NumberFormat.RUPEE -> "rupee"
            NumberFormat.WON -> "won"
            NumberFormat.YUAN -> "yuan"
            NumberFormat.CANADIAN_DOLLAR -> "canadian_dollar"
            NumberFormat.REAL -> "real"
            NumberFormat.LIRA -> "lira"
            NumberFormat.RUPIAH -> "rupiah"
            NumberFormat.FRANC -> "franc"
            NumberFormat.HONG_KONG_DOLLAR -> "hong_kong_dollar"
            NumberFormat.NEW_ZEALAND_DOLLAR -> "new_zealand_dollar"
            NumberFormat.KRONA -> "krona"
            NumberFormat.NORWEGIAN_KRONE -> "norwegian_krone"
            NumberFormat.MEXICAN_PESO -> "mexican_peso"
            NumberFormat.RAND -> "rand"
            NumberFormat.NEW_TAIWAN_DOLLAR -> "new_taiwan_dollar"
            NumberFormat.DANISH_KRONE -> "danish_krone"
            NumberFormat.ZLOTY -> "zloty"
            NumberFormat.BAHT -> "baht"
            NumberFormat.FORINT -> "forint"
            NumberFormat.KORUNA -> "koruna"
            NumberFormat.SHEKEL -> "shekel"
            NumberFormat.CHILEAN_PESO -> "chilean_peso"
            NumberFormat.PHILIPPINE_PESO -> "philippine_peso"
            NumberFormat.DIRHAM -> "dirham"
            NumberFormat.COLOMBIAN_PESO -> "colombian_peso"
            NumberFormat.RIYAL -> "riyal"
            NumberFormat.RINGGIT -> "ringgit"
            NumberFormat.LEU -> "leu"

            NumberFormat._UNKNOWN -> throw IllegalStateException()
        }

    }
}