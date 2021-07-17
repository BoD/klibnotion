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

import org.jraf.klibnotion.internal.TimeZoneIdParser
import org.jraf.klibnotion.internal.TimestampFormatter
import org.jraf.klibnotion.internal.TimestampParser
import org.jraf.klibnotion.internal.api.model.ApiConverter
import org.jraf.klibnotion.model.date.Date
import org.jraf.klibnotion.model.date.DateOrDateTime
import org.jraf.klibnotion.model.date.DateTime

internal object ApiDateStringConverter : ApiConverter<String, DateOrDateTime>() {
    private const val DATE_FORMAT = "yyyy-MM-dd"
    private const val DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"

    override fun apiToModel(apiModel: String): DateOrDateTime {
        // Try date + time first, fallback to date only
        return try {
            val timestamp = TimestampParser(DATE_TIME_FORMAT).parse(apiModel)
            val timeZoneId = TimeZoneIdParser().parse(apiModel)
            DateTime(timestamp = timestamp, timeZoneId = timeZoneId)
        } catch (e: Exception) {
            Date(TimestampParser(DATE_FORMAT).parse(apiModel))
        }
    }

    override fun modelToApi(model: DateOrDateTime): String {
        return when (model) {
            is DateTime -> TimestampFormatter(DATE_TIME_FORMAT, model.timeZoneId).format(model.timestamp)
            is Date -> TimestampFormatter(DATE_FORMAT).format(model.timestamp)
        }
    }
}
