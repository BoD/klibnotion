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

package org.jraf.klibnotion.internal.api.model.database.create

import org.jraf.klibnotion.internal.api.model.ApiConverter
import org.jraf.klibnotion.internal.api.model.base.ApiOutEmojiOrFileConverter
import org.jraf.klibnotion.internal.api.model.base.ApiReferenceConverter
import org.jraf.klibnotion.internal.api.model.file.ApiOutFileConverter
import org.jraf.klibnotion.internal.api.model.modelToApi
import org.jraf.klibnotion.internal.api.model.property.spec.ApiPropertySpecConverter
import org.jraf.klibnotion.internal.api.model.richtext.ApiOutRichTextListConverter
import org.jraf.klibnotion.model.base.reference.PageReference

internal object ApiDatabaseCreateParametersConverter :
    ApiConverter<ApiDatabaseCreateParameters, DatabaseCreateParameters>() {
    override fun modelToApi(model: DatabaseCreateParameters): ApiDatabaseCreateParameters {
        return ApiDatabaseCreateParameters(
            parent = PageReference(model.parentPageId).modelToApi(ApiReferenceConverter),
            title = model.title.modelToApi(ApiOutRichTextListConverter),
            properties = model.properties.propertySpecList.modelToApi(ApiPropertySpecConverter).toMap(),
            icon = model.icon?.modelToApi(ApiOutEmojiOrFileConverter),
            cover = model.cover?.modelToApi(ApiOutFileConverter),
        )
    }
}
