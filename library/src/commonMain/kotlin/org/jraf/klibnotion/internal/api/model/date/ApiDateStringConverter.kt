/*
 * This source is part of the
 *      _____  ___   ____
 *  __ / / _ \/ _ | / __/___  _______ _
 * / // / , _/ __ |/ _/_/ _ \/ __/ _ `/
 * \___/_/|_/_/ |_/_/ (_)___/_/  \_, /
 *                              /___/
 * repository.
 *
 * Copyright (C) 2019-present Benoit 'BoD' Lubek (BoD@JRAF.org)
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

package org.jraf.klibnotion.internal.api.model.date

import org.jraf.klibnotion.internal.api.model.ApiConverter
import org.jraf.klibnotion.model.date.Date

internal object ApiDateStringConverter : ApiConverter<String, Date>() {
    private const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"

    override fun apiToModel(apiModel: String): Date {
        return SimpleDateFormat(DATE_FORMAT).parse(apiModel)
    }

    override fun modelToApi(model: Date): String {
        return SimpleDateFormat(DATE_FORMAT).format(model)
    }
}

internal expect class SimpleDateFormat(format: String) {
    fun parse(formattedDate: String): Date
    fun format(dateToFormat: Date): String
}
