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

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSTimeZone
import platform.Foundation.defaultTimeZone
import platform.Foundation.timeZoneWithName

internal actual fun getLocalTimeZoneId(): String {
    return NSTimeZone.defaultTimeZone.name
}

internal actual class TimestampFormatter actual constructor(format: String, timeZoneId: String?) {
    private val nsDateFormatter = NSDateFormatter().apply {
        dateFormat = format
        if (timeZoneId != null) timeZone = NSTimeZone.timeZoneWithName(timeZoneId)!!
    }

    actual fun format(timestampToFormat: NSDate): String {
        return nsDateFormatter.stringFromDate(timestampToFormat)
    }
}

internal actual class TimestampParser actual constructor(format: String) {
    private val nsDateFormatter = NSDateFormatter().apply {
        dateFormat = format
        // Set the default timezone to GMT for the case where it's not present in the date to parse
        // which is the case when it's a date without a time.
        timeZone = NSTimeZone.timeZoneWithName("GMT")!!
    }

    actual fun parse(formattedDate: String): NSDate {
        return nsDateFormatter.dateFromString(formattedDate)!!
    }
}

internal actual val IRRELEVANT_TIMESTAMP = NSDate()