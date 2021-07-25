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

import kotlinx.serialization.Serializable

/**
 * See [Reference](https://developers.notion.com/reference/database).
 */
@Serializable
internal data class ApiPropertySpec(
    val id: String? = null,
    val type: String? = null,
    val number: ApiPropertySpecNumber? = null,
    val select: ApiPropertySpecSelect? = null,
    val multi_select: ApiPropertySpecMultiSelect? = null,
    val formula: ApiPropertySpecFormula? = null,
    val relation: ApiPropertySpecRelation? = null,
    val rollup: ApiPropertySpecRollup? = null,
    val title: ApiEmpty? = null,
    val checkbox: ApiEmpty? = null,
    val created_by: ApiEmpty? = null,
    val created_time: ApiEmpty? = null,
    val date: ApiEmpty? = null,
    val email: ApiEmpty? = null,
    val files: ApiEmpty? = null,
    val last_edited_by: ApiEmpty? = null,
    val last_edited_time: ApiEmpty? = null,
    val people: ApiEmpty? = null,
    val phone_number: ApiEmpty? = null,
    val rich_text: ApiEmpty? = null,
    val url: ApiEmpty? = null,
)

@Serializable
internal class ApiEmpty