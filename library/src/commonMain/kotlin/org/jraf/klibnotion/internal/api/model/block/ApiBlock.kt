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
 * Copyright (C) 2021-present Yu Jinyan (i@yujinyan.me)
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

package org.jraf.klibnotion.internal.api.model.block

import kotlinx.serialization.Serializable

/**
 * See [Reference](https://developers.notion.com/reference/block).
 */
@Serializable
internal data class ApiBlock(
    val id: String,
    val created_time: String,
    val last_edited_time: String,
    val has_children: Boolean,
    val type: String,
    val paragraph: ApiBlockText? = null,
    val heading_1: ApiBlockText? = null,
    val heading_2: ApiBlockText? = null,
    val heading_3: ApiBlockText? = null,
    val bulleted_list_item: ApiBlockText? = null,
    val numbered_list_item: ApiBlockText? = null,
    val toggle: ApiBlockText? = null,
    val to_do: ApiBlockTodo? = null,
    val child_page: ApiBlockChildPage? = null,
    val child_database: ApiBlockChildDatabase? = null,
    val code: ApiBlockCode? = null,
    val equation: ApiBlockEquation? = null,
    val callout: ApiBlockCallout? = null,
    val embed: ApiBlockEmbed? = null,
    val quote: ApiBlockText? = null,
    val bookmark: ApiBlockBookmark? = null,
)
