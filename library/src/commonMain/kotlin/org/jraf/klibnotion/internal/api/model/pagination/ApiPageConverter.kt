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

package org.jraf.klibnotion.internal.api.model.pagination

import org.jraf.klibnotion.internal.api.model.ApiConverter
import org.jraf.klibnotion.model.pagination.Page
import org.jraf.klibnotion.model.pagination.Pagination

internal abstract class ApiPageConverter<API_MODEL : Any, MODEL : Any>(
    private val apiConverter: ApiConverter<API_MODEL, MODEL>
) : ApiConverter<ApiPage<API_MODEL>, Page<MODEL>>() {
    override fun apiToModel(apiModel: ApiPage<API_MODEL>): Page<MODEL> {
        return Page(
            results = apiConverter.apiToModel(apiModel.results),
            nextPagination = apiModel.next_cursor?.let { Pagination(startCursor = it) }
        )
    }
}