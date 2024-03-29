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

package org.jraf.klibnotion.internal.api.model.color

import org.jraf.klibnotion.internal.api.model.ApiConverter
import org.jraf.klibnotion.model.color.Color

internal object ApiColorConverter : ApiConverter<String, Color>() {
    override fun apiToModel(apiModel: String) = when (apiModel) {
        "default" -> Color.DEFAULT
        "gray" -> Color.GRAY
        "brown" -> Color.BROWN
        "orange" -> Color.ORANGE
        "yellow" -> Color.YELLOW
        "green" -> Color.GREEN
        "blue" -> Color.BLUE
        "purple" -> Color.PURPLE
        "pink" -> Color.PINK
        "red" -> Color.RED
        "gray_background" -> Color.GRAY_BACKGROUND
        "brown_background" -> Color.BROWN_BACKGROUND
        "orange_background" -> Color.ORANGE_BACKGROUND
        "yellow_background" -> Color.YELLOW_BACKGROUND
        "green_background" -> Color.GREEN_BACKGROUND
        "blue_background" -> Color.BLUE_BACKGROUND
        "purple_background" -> Color.PURPLE_BACKGROUND
        "pink_background" -> Color.PINK_BACKGROUND
        "red_background" -> Color.RED_BACKGROUND
        else -> Color._UNKNOWN
    }

    override fun modelToApi(model: Color): String = when (model) {
        Color._UNKNOWN, Color.DEFAULT -> "default"
        Color.GRAY -> "gray"
        Color.BROWN -> "brown"
        Color.ORANGE -> "orange"
        Color.YELLOW -> "yellow"
        Color.GREEN -> "green"
        Color.BLUE -> "blue"
        Color.PURPLE -> "purple"
        Color.PINK -> "pink"
        Color.RED -> "red"
        Color.GRAY_BACKGROUND -> "gray_background"
        Color.BROWN_BACKGROUND -> "brown_background"
        Color.ORANGE_BACKGROUND -> "orange_background"
        Color.YELLOW_BACKGROUND -> "yellow_background"
        Color.GREEN_BACKGROUND -> "green_background"
        Color.BLUE_BACKGROUND -> "blue_background"
        Color.PURPLE_BACKGROUND -> "purple_background"
        Color.PINK_BACKGROUND -> "pink_background"
        Color.RED_BACKGROUND -> "red_background"
    }
}
