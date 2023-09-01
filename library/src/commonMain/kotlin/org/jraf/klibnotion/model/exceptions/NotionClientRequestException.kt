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

package org.jraf.klibnotion.model.exceptions

import io.ktor.client.features.ClientRequestException
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

class NotionClientRequestException(
    override val cause: ClientRequestException,
    jsonBody: String,
) :
    NotionClientException(cause) {
    private val notionError: NotionError? = NotionError.fromJsonString(jsonBody)

    val code: String = notionError?.code ?: "unexpected"
    override val message: String = notionError?.message ?: "Unexpected error: ${cause.message}"
    val detailsJson: String = notionError?.details?.toString() ?: "{}"
    val status: Int? = notionError?.status

    override fun toString(): String {
        return "NotionClientRequestException(status=$status, code='$code', message='$message', detailsJson='$detailsJson')"
    }
}

@Serializable
private data class NotionError(
    val code: String,
    val message: String,
    val status: Int? = null,
    val details: JsonObject? = null,
) {
    companion object {
        private val json = Json { ignoreUnknownKeys = true }

        fun fromJsonString(jsonString: String): NotionError? =
            try {
                json.decodeFromString(jsonString)
            } catch (e: Throwable) {
                null
            }
    }
}
