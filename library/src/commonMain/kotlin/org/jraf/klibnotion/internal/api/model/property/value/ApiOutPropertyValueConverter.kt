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

package org.jraf.klibnotion.internal.api.model.property.value

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import org.jraf.klibnotion.internal.api.model.ApiConverter
import org.jraf.klibnotion.model.property.value.MultiSelectPropertyValue
import org.jraf.klibnotion.model.property.value.NumberPropertyValue
import org.jraf.klibnotion.model.property.value.PropertyValue
import org.jraf.klibnotion.model.property.value.SelectPropertyValue
import org.jraf.klibnotion.model.property.value.TextPropertyValue

internal object ApiOutPropertyValueConverter :
    ApiConverter<Pair<String, JsonElement>, PropertyValue<*>>() {

    override fun modelToApi(model: PropertyValue<*>): Pair<String, JsonElement> {
        return model.name to when (model) {
            is NumberPropertyValue -> JsonPrimitive(model.value)

            is TextPropertyValue -> buildJsonArray {
                for (richText in model.value) {
                    addJsonObject {
                        putJsonObject("text") {
                            put("content", richText.plainText)
                        }
                    }
                }
            }

            is SelectPropertyValue -> buildJsonObject {
                if (model.value.name.isNotEmpty()) {
                    put("name", model.value.name)
                } else {
                    put("id", model.value.id)
                }
            }

            is MultiSelectPropertyValue -> buildJsonArray {
                for (option in model.value)
                    addJsonObject {
                        if (option.name.isNotEmpty()) {
                            put("name", option.name)
                        } else {
                            put("id", option.id)
                        }
                    }
            }

            else -> TODO()
        }
    }
}