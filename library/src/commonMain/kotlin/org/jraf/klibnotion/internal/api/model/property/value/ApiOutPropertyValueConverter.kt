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
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.jraf.klibnotion.internal.api.model.ApiConverter
import org.jraf.klibnotion.internal.api.model.date.ApiDateStringConverter
import org.jraf.klibnotion.internal.api.model.modelToApi
import org.jraf.klibnotion.internal.api.model.richtext.ApiOutRichTextListConverter
import org.jraf.klibnotion.model.base.hyphened
import org.jraf.klibnotion.model.property.value.CheckboxPropertyValue
import org.jraf.klibnotion.model.property.value.CreatedByPropertyValue
import org.jraf.klibnotion.model.property.value.CreatedTimePropertyValue
import org.jraf.klibnotion.model.property.value.DatePropertyValue
import org.jraf.klibnotion.model.property.value.EmailPropertyValue
import org.jraf.klibnotion.model.property.value.FilesPropertyValue
import org.jraf.klibnotion.model.property.value.FormulaPropertyValue
import org.jraf.klibnotion.model.property.value.LastEditedByPropertyValue
import org.jraf.klibnotion.model.property.value.LastEditedTimePropertyValue
import org.jraf.klibnotion.model.property.value.MultiSelectPropertyValue
import org.jraf.klibnotion.model.property.value.NumberPropertyValue
import org.jraf.klibnotion.model.property.value.PeoplePropertyValue
import org.jraf.klibnotion.model.property.value.PhoneNumberPropertyValue
import org.jraf.klibnotion.model.property.value.PropertyValue
import org.jraf.klibnotion.model.property.value.RelationPropertyValue
import org.jraf.klibnotion.model.property.value.RichTextPropertyValue
import org.jraf.klibnotion.model.property.value.RollupPropertyValue
import org.jraf.klibnotion.model.property.value.SelectPropertyValue
import org.jraf.klibnotion.model.property.value.TitlePropertyValue
import org.jraf.klibnotion.model.property.value.UnknownTypePropertyValue
import org.jraf.klibnotion.model.property.value.UrlPropertyValue

internal object ApiOutPropertyValueConverter :
    ApiConverter<Pair<String, JsonElement>, PropertyValue<*>>() {

    override fun modelToApi(model: PropertyValue<*>): Pair<String, JsonElement> {
        return model.name to when (model) {
            is NumberPropertyValue -> buildJsonObject {
                put("number", model.value)
            }

            is RichTextPropertyValue -> buildJsonObject {
                put("rich_text", model.value.modelToApi(ApiOutRichTextListConverter))
            }

            is TitlePropertyValue -> buildJsonObject {
                put("title", model.value.modelToApi(ApiOutRichTextListConverter))
            }

            is SelectPropertyValue -> buildJsonObject {
                put(
                    "select",
                    model.value?.let {
                        buildJsonObject {
                            if (it.name.isNotEmpty()) {
                                put("name", it.name)
                            } else {
                                put("id", it.id)
                            }
                        }
                    } ?: JsonNull
                )
            }

            is MultiSelectPropertyValue -> buildJsonObject {
                put(
                    "multi_select",
                    model.value.let {
                        buildJsonArray {
                            for (option in it)
                                addJsonObject {
                                    if (option.name.isNotEmpty()) {
                                        put("name", option.name)
                                    } else {
                                        put("id", option.id)
                                    }
                                }
                        }
                    }
                )
            }

            is DatePropertyValue -> buildJsonObject {
                put(
                    "date",
                    model.value?.let {
                        buildJsonObject {
                            put("start", it.start.modelToApi(ApiDateStringConverter))
                            it.end?.let { put("end", it.modelToApi(ApiDateStringConverter)) }
                        }
                    } ?: JsonNull
                )
            }

            is RelationPropertyValue -> buildJsonObject {
                put(
                    "relation",
                    model.value.let {
                        buildJsonArray {
                            for (id in it)
                                addJsonObject {
                                    put("id", id.hyphened())
                                }
                        }
                    }
                )
            }

            is PeoplePropertyValue -> buildJsonObject {
                put(
                    "people",
                    model.value.let {
                        buildJsonArray {
                            for (user in it)
                                addJsonObject {
                                    put("id", user.id.hyphened())
                                }
                        }
                    }
                )
            }

            is CheckboxPropertyValue -> buildJsonObject {
                put("checkbox", model.value)
            }

            is UrlPropertyValue -> buildJsonObject {
                put("url", model.value)
            }

            is PhoneNumberPropertyValue -> buildJsonObject {
                put("phone_number", model.value)
            }

            is EmailPropertyValue -> buildJsonObject {
                put("email", model.value)
            }

            // These are all read only
            is FormulaPropertyValue,
            is CreatedByPropertyValue,
            is RollupPropertyValue,
            is LastEditedTimePropertyValue,
            is CreatedTimePropertyValue,
            is LastEditedByPropertyValue,
            is FilesPropertyValue,
            is UnknownTypePropertyValue,
            -> throw IllegalStateException()
        }
    }
}