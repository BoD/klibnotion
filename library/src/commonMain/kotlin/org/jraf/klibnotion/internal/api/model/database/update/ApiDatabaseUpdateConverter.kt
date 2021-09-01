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

package org.jraf.klibnotion.internal.api.model.database.update

import org.jraf.klibnotion.internal.api.model.ApiConverter
import org.jraf.klibnotion.internal.api.model.modelToApi
import org.jraf.klibnotion.internal.api.model.property.spec.ApiPropertySpecConverter
import org.jraf.klibnotion.internal.api.model.richtext.ApiOutRichTextListConverter
import org.jraf.klibnotion.model.property.spec.PropertySpecList
import org.jraf.klibnotion.model.richtext.RichTextList

internal object ApiDatabaseUpdateConverter :
    ApiConverter<ApiDatabaseUpdate, Pair<RichTextList?, PropertySpecList?>>() {
    override fun modelToApi(model: Pair<RichTextList?, PropertySpecList?>): ApiDatabaseUpdate {
        return ApiDatabaseUpdate(
            title = model.first?.modelToApi(ApiOutRichTextListConverter),
            properties = model.second?.propertySpecList?.modelToApi(ApiPropertySpecConverter)?.toMap()
        )
    }
}
