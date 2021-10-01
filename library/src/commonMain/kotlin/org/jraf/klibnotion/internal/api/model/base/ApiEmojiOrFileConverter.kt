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

import org.jraf.klibnotion.internal.api.model.ApiConverter
import org.jraf.klibnotion.internal.model.emoji.EmojiImpl
import org.jraf.klibnotion.internal.model.file.FileImpl
import org.jraf.klibnotion.model.base.EmojiOrFile

internal object ApiEmojiOrFileConverter : ApiConverter<ApiEmojiOrFile?, EmojiOrFile?>() {
    override fun apiToModel(apiModel: ApiEmojiOrFile?): EmojiOrFile? {
        if (apiModel == null) return null
        return when (apiModel.type) {
            "emoji" -> EmojiImpl(value = apiModel.emoji!![0])
            "file" -> FileImpl(name = null, url = apiModel.file!!.url)
            "external" -> FileImpl(name = null, url = apiModel.external!!.url)
            else -> null
        }
    }
}