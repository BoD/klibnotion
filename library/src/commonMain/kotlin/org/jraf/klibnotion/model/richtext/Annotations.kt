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

package org.jraf.klibnotion.model.richtext

import org.jraf.klibnotion.model.color.Color
import kotlin.jvm.JvmField

/**
 * See [https://developers.notion.com/reference/rich-text].
 */
data class Annotations(
    val bold: Boolean = false,
    val italic: Boolean = false,
    val strikethrough: Boolean = false,
    val underline: Boolean = false,
    val code: Boolean = false,
    val color: Color = Color.DEFAULT,
) {
    constructor(color: Color) : this(false, false, false, false, false, color)

    companion object {
        @JvmField
        val DEFAULT: Annotations = Annotations()

        @JvmField
        val BOLD: Annotations = Annotations(bold = true)

        @JvmField
        val ITALIC: Annotations = Annotations(italic = true)

        @JvmField
        val STRIKETHROUGH: Annotations = Annotations(strikethrough = true)

        @JvmField
        val UNDERLINE: Annotations = Annotations(underline = true)

        @JvmField
        val CODE: Annotations = Annotations(code = true)
    }
}
