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

package org.jraf.klibnotion.internal

import org.jraf.klibnotion.model.date.Timestamp

internal expect fun getLocalTimeZoneId(): String

internal expect class TimestampFormatter(format: String, timeZoneId: String? = null) {
    fun format(timestampToFormat: Timestamp): String
}

internal expect class TimestampParser(format: String) {
    fun parse(formattedDate: String): Timestamp
}

internal class TimeZoneIdParser {
    // Example value: 2021-07-17T00:00:00.000-05:00
    // The time zone offset start at index 23
    fun parse(formattedDate: String): String = formattedDate.substring(23)
}
