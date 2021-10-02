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

package org.jraf.klibnotion.internal.api.model.base

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import org.jraf.klibnotion.internal.api.model.ApiConverter
import org.jraf.klibnotion.model.base.EmojiOrFile
import org.jraf.klibnotion.model.emoji.Emoji
import org.jraf.klibnotion.model.file.File

internal object ApiOutEmojiOrFileConverter : ApiConverter<JsonElement, EmojiOrFile>() {
    override fun modelToApi(model: EmojiOrFile): JsonElement {
        return buildJsonObject {
            when (model) {
                is Emoji -> {
                    put("type", "emoji")
                    put("emoji", model.value)
                }
                is File -> {
                    put("type", "external")
                    putJsonObject("external") {
                        put("url", model.url)
                    }
                }
                else -> throw AssertionError("Should never happen")
            }
        }
    }
}