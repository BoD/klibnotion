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

package org.jraf.klibnotion.internal.api.model.database.query

import org.jraf.klibnotion.internal.api.model.ApiConverter
import org.jraf.klibnotion.internal.api.model.date.ApiDateStringConverter
import org.jraf.klibnotion.internal.api.model.modelToApi
import org.jraf.klibnotion.model.database.query.DatabaseQuery
import org.jraf.klibnotion.model.database.query.DatabaseQuerySort
import org.jraf.klibnotion.model.database.query.filter.DatabaseQueryPredicate
import org.jraf.klibnotion.model.database.query.filter.DatabaseQueryPropertyFilter

internal object ApiDatabaseQueryConverter :
    ApiConverter<ApiDatabaseQuery, Pair<DatabaseQuery?, DatabaseQuerySort?>>() {
    override fun modelToApi(model: Pair<DatabaseQuery?, DatabaseQuerySort?>): ApiDatabaseQuery {
        val query = model.first
        val sort = model.second
        return ApiDatabaseQuery(
            filter = query?.let {
                ApiDatabaseQueryFilters(
                    or = query.anyFilters.toList().modelToApi(ApiDatabaseQueryFilterConverter)
                        .ifEmpty { null },
                    and = query.allFilters.toList().modelToApi(ApiDatabaseQueryFilterConverter)
                        .ifEmpty { null },
                )
            }?.let { if (it.or == null && it.and == null) null else it },
            sorts = sort?.let { sortElem ->
                sortElem.sorting.map { (propertyName, direction) ->
                    ApiDatabaseQuerySort(
                        property = propertyName,
                        direction = when (direction) {
                            DatabaseQuerySort.Direction.ASCENDING -> "ascending"
                            DatabaseQuerySort.Direction.DESCENDING -> "descending"
                        }
                    )
                }
            }
        )
    }
}

internal object ApiDatabaseQueryFilterConverter : ApiConverter<ApiDatabaseQueryFilter, DatabaseQueryPropertyFilter>() {
    override fun modelToApi(model: DatabaseQueryPropertyFilter): ApiDatabaseQueryFilter {
        return ApiDatabaseQueryFilter(
            property = model.propertyIdOrName,
            title = (model as? DatabaseQueryPropertyFilter.Title)?.predicate?.modelToApi(
                ApiTextDatabaseQueryFilterConverter),
            text = (model as? DatabaseQueryPropertyFilter.Text)?.predicate?.modelToApi(
                ApiTextDatabaseQueryFilterConverter),
            number = (model as? DatabaseQueryPropertyFilter.Number)?.predicate?.modelToApi(
                ApiNumberDatabaseQueryFilterConverter),
            checkbox = (model as? DatabaseQueryPropertyFilter.Checkbox)?.predicate?.modelToApi(
                ApiCheckboxDatabaseQueryFilterConverter),
            select = (model as? DatabaseQueryPropertyFilter.Select)?.predicate?.modelToApi(
                ApiSelectDatabaseQueryFilterConverter),
            multi_select = (model as? DatabaseQueryPropertyFilter.MultiSelect)?.predicate?.modelToApi(
                ApiMultiSelectDatabaseQueryFilterConverter),
            date = (model as? DatabaseQueryPropertyFilter.Date)?.predicate?.modelToApi(
                ApiDateDatabaseQueryFilterConverter),
            people = (model as? DatabaseQueryPropertyFilter.People)?.predicate?.modelToApi(
                ApiPeopleDatabaseQueryFilterConverter),
            files = (model as? DatabaseQueryPropertyFilter.Files)?.predicate?.modelToApi(
                ApiFilesDatabaseQueryFilterConverter),
            url = (model as? DatabaseQueryPropertyFilter.Url)?.predicate?.modelToApi(
                ApiTextDatabaseQueryFilterConverter),
            email = (model as? DatabaseQueryPropertyFilter.Email)?.predicate?.modelToApi(
                ApiTextDatabaseQueryFilterConverter),
            phone = (model as? DatabaseQueryPropertyFilter.Phone)?.predicate?.modelToApi(
                ApiTextDatabaseQueryFilterConverter),
            relation = (model as? DatabaseQueryPropertyFilter.Relation)?.predicate?.modelToApi(
                ApiRelationDatabaseQueryFilterConverter),
            formula = (model as? DatabaseQueryPropertyFilter.Formula)?.predicate?.modelToApi(
                ApiFormulaDatabaseQueryFilterConverter),
            created_by = (model as? DatabaseQueryPropertyFilter.CreatedBy)?.predicate?.modelToApi(
                ApiPeopleDatabaseQueryFilterConverter),
            created_time = (model as? DatabaseQueryPropertyFilter.CreatedTime)?.predicate?.modelToApi(
                ApiDateDatabaseQueryFilterConverter),
            last_edited_by = (model as? DatabaseQueryPropertyFilter.LastEditedBy)?.predicate?.modelToApi(
                ApiPeopleDatabaseQueryFilterConverter),
            last_edited_time = (model as? DatabaseQueryPropertyFilter.LastEditedTime)?.predicate?.modelToApi(
                ApiDateDatabaseQueryFilterConverter),
        )
    }
}

internal object ApiTextDatabaseQueryFilterConverter :
    ApiConverter<ApiTextDatabaseQueryFilter, DatabaseQueryPredicate.Text>() {
    override fun modelToApi(model: DatabaseQueryPredicate.Text): ApiTextDatabaseQueryFilter {
        return when (model) {
            is DatabaseQueryPredicate.Text.Equals -> ApiTextDatabaseQueryFilter(equals = model.value)
            is DatabaseQueryPredicate.Text.DoesNotEqual -> ApiTextDatabaseQueryFilter(does_not_equal = model.value)
            is DatabaseQueryPredicate.Text.Contains -> ApiTextDatabaseQueryFilter(contains = model.value)
            is DatabaseQueryPredicate.Text.DoesNotContain -> ApiTextDatabaseQueryFilter(does_not_contain = model.value)
            is DatabaseQueryPredicate.Text.StartsWith -> ApiTextDatabaseQueryFilter(starts_with = model.value)
            is DatabaseQueryPredicate.Text.EndsWith -> ApiTextDatabaseQueryFilter(ends_with = model.value)
            is DatabaseQueryPredicate.Text.IsEmpty -> ApiTextDatabaseQueryFilter(is_empty = true)
            is DatabaseQueryPredicate.Text.IsNotEmpty -> ApiTextDatabaseQueryFilter(is_not_empty = true)
            else -> throw AssertionError()
        }
    }
}

internal object ApiNumberDatabaseQueryFilterConverter :
    ApiConverter<ApiNumberDatabaseQueryFilter, DatabaseQueryPredicate.Number>() {
    override fun modelToApi(model: DatabaseQueryPredicate.Number): ApiNumberDatabaseQueryFilter {
        return when (model) {
            is DatabaseQueryPredicate.Number.Equals ->
                ApiNumberDatabaseQueryFilter(equals = model.value.toDouble())
            is DatabaseQueryPredicate.Number.DoesNotEqual ->
                ApiNumberDatabaseQueryFilter(does_not_equal = model.value.toDouble())
            is DatabaseQueryPredicate.Number.GreaterThan ->
                ApiNumberDatabaseQueryFilter(greater_than = model.value.toDouble())
            is DatabaseQueryPredicate.Number.LessThan ->
                ApiNumberDatabaseQueryFilter(less_than = model.value.toDouble())
            is DatabaseQueryPredicate.Number.GreaterThanOrEqualTo ->
                ApiNumberDatabaseQueryFilter(greater_than_or_equal_to = model.value.toDouble())
            is DatabaseQueryPredicate.Number.LessThanOrEqualTo ->
                ApiNumberDatabaseQueryFilter(less_than_or_equal_to = model.value.toDouble())
            is DatabaseQueryPredicate.Number.IsEmpty ->
                ApiNumberDatabaseQueryFilter(is_empty = true)
            is DatabaseQueryPredicate.Number.IsNotEmpty ->
                ApiNumberDatabaseQueryFilter(is_not_empty = true)
            else -> throw AssertionError()
        }
    }
}

internal object ApiCheckboxDatabaseQueryFilterConverter :
    ApiConverter<ApiCheckboxDatabaseQueryFilter, DatabaseQueryPredicate.Checkbox>() {
    override fun modelToApi(model: DatabaseQueryPredicate.Checkbox): ApiCheckboxDatabaseQueryFilter {
        return if (model.isChecked) {
            ApiCheckboxDatabaseQueryFilter(equals = true)
        } else {
            ApiCheckboxDatabaseQueryFilter(does_not_equal = true)
        }
    }
}

internal object ApiSelectDatabaseQueryFilterConverter :
    ApiConverter<ApiSelectDatabaseQueryFilter, DatabaseQueryPredicate.Select>() {
    override fun modelToApi(model: DatabaseQueryPredicate.Select): ApiSelectDatabaseQueryFilter {
        return when (model) {
            is DatabaseQueryPredicate.Select.Equals -> ApiSelectDatabaseQueryFilter(equals = model.value)
            is DatabaseQueryPredicate.Select.DoesNotEqual -> ApiSelectDatabaseQueryFilter(does_not_equal = model.value)
            is DatabaseQueryPredicate.Select.IsEmpty -> ApiSelectDatabaseQueryFilter(is_empty = true)
            is DatabaseQueryPredicate.Select.IsNotEmpty -> ApiSelectDatabaseQueryFilter(is_not_empty = true)
            else -> throw AssertionError()
        }
    }
}

internal object ApiMultiSelectDatabaseQueryFilterConverter :
    ApiConverter<ApiMultiSelectDatabaseQueryFilter, DatabaseQueryPredicate.MultiSelect>() {
    override fun modelToApi(model: DatabaseQueryPredicate.MultiSelect): ApiMultiSelectDatabaseQueryFilter {
        return when (model) {
            is DatabaseQueryPredicate.MultiSelect.Contains -> ApiMultiSelectDatabaseQueryFilter(contains = model.value)
            is DatabaseQueryPredicate.MultiSelect.DoesNotContain -> ApiMultiSelectDatabaseQueryFilter(does_not_contain = model.value)
            is DatabaseQueryPredicate.MultiSelect.IsEmpty -> ApiMultiSelectDatabaseQueryFilter(is_empty = true)
            is DatabaseQueryPredicate.MultiSelect.IsNotEmpty -> ApiMultiSelectDatabaseQueryFilter(is_not_empty = true)
            else -> throw AssertionError()
        }
    }
}

internal object ApiDateDatabaseQueryFilterConverter :
    ApiConverter<ApiDateDatabaseQueryFilter, DatabaseQueryPredicate.Date>() {
    override fun modelToApi(model: DatabaseQueryPredicate.Date): ApiDateDatabaseQueryFilter {
        return when (model) {
            is DatabaseQueryPredicate.Date.Equals ->
                ApiDateDatabaseQueryFilter(equals = model.value.modelToApi(ApiDateStringConverter))
            is DatabaseQueryPredicate.Date.Before ->
                ApiDateDatabaseQueryFilter(before = model.value.modelToApi(ApiDateStringConverter))
            is DatabaseQueryPredicate.Date.After ->
                ApiDateDatabaseQueryFilter(after = model.value.modelToApi(ApiDateStringConverter))
            is DatabaseQueryPredicate.Date.OnOrBefore ->
                ApiDateDatabaseQueryFilter(on_or_before = model.value.modelToApi(ApiDateStringConverter))
            is DatabaseQueryPredicate.Date.OnOrAfter ->
                ApiDateDatabaseQueryFilter(on_or_after = model.value.modelToApi(ApiDateStringConverter))
            is DatabaseQueryPredicate.Date.IsPastWeek ->
                ApiDateDatabaseQueryFilter(past_week = mapOf())
            is DatabaseQueryPredicate.Date.IsPastMonth ->
                ApiDateDatabaseQueryFilter(past_week = mapOf())
            is DatabaseQueryPredicate.Date.IsPastYear ->
                ApiDateDatabaseQueryFilter(past_week = mapOf())
            is DatabaseQueryPredicate.Date.IsNextWeek ->
                ApiDateDatabaseQueryFilter(past_week = mapOf())
            is DatabaseQueryPredicate.Date.IsNextMonth ->
                ApiDateDatabaseQueryFilter(past_week = mapOf())
            is DatabaseQueryPredicate.Date.IsNextYear ->
                ApiDateDatabaseQueryFilter(past_week = mapOf())
            is DatabaseQueryPredicate.Date.IsEmpty ->
                ApiDateDatabaseQueryFilter(is_empty = true)
            is DatabaseQueryPredicate.Date.IsNotEmpty ->
                ApiDateDatabaseQueryFilter(is_not_empty = true)
            else -> throw AssertionError()
        }
    }
}

internal object ApiPeopleDatabaseQueryFilterConverter :
    ApiConverter<ApiPeopleDatabaseQueryFilter, DatabaseQueryPredicate.People>() {
    override fun modelToApi(model: DatabaseQueryPredicate.People): ApiPeopleDatabaseQueryFilter {
        return when (model) {
            is DatabaseQueryPredicate.People.Contains -> ApiPeopleDatabaseQueryFilter(contains = model.value)
            is DatabaseQueryPredicate.People.DoesNotContain -> ApiPeopleDatabaseQueryFilter(does_not_contain = model.value)
            is DatabaseQueryPredicate.People.IsEmpty -> ApiPeopleDatabaseQueryFilter(is_empty = true)
            is DatabaseQueryPredicate.People.IsNotEmpty -> ApiPeopleDatabaseQueryFilter(is_not_empty = true)
            else -> throw AssertionError()
        }
    }
}

internal object ApiFilesDatabaseQueryFilterConverter :
    ApiConverter<ApiFilesDatabaseQueryFilter, DatabaseQueryPredicate.Files>() {
    override fun modelToApi(model: DatabaseQueryPredicate.Files): ApiFilesDatabaseQueryFilter {
        return when (model) {
            is DatabaseQueryPredicate.Files.IsEmpty -> ApiFilesDatabaseQueryFilter(is_empty = true)
            is DatabaseQueryPredicate.Files.IsNotEmpty -> ApiFilesDatabaseQueryFilter(is_not_empty = true)
            else -> throw AssertionError()
        }
    }
}

internal object ApiRelationDatabaseQueryFilterConverter :
    ApiConverter<ApiRelationDatabaseQueryFilter, DatabaseQueryPredicate.Relation>() {
    override fun modelToApi(model: DatabaseQueryPredicate.Relation): ApiRelationDatabaseQueryFilter {
        return when (model) {
            is DatabaseQueryPredicate.Relation.Contains -> ApiRelationDatabaseQueryFilter(contains = model.value)
            is DatabaseQueryPredicate.Relation.DoesNotContain -> ApiRelationDatabaseQueryFilter(does_not_contain = model.value)
            is DatabaseQueryPredicate.Relation.IsEmpty -> ApiRelationDatabaseQueryFilter(is_empty = true)
            is DatabaseQueryPredicate.Relation.IsNotEmpty -> ApiRelationDatabaseQueryFilter(is_not_empty = true)
            else -> throw AssertionError()
        }
    }
}

internal object ApiFormulaDatabaseQueryFilterConverter :
    ApiConverter<ApiFormulaDatabaseQueryFilter, DatabaseQueryPredicate.Formula>() {
    override fun modelToApi(model: DatabaseQueryPredicate.Formula): ApiFormulaDatabaseQueryFilter {
        return when (model) {
            is DatabaseQueryPredicate.Formula.Text ->
                ApiFormulaDatabaseQueryFilter(text = model.modelToApi(ApiFormulaTextDatabaseQueryFilterConverter))
            is DatabaseQueryPredicate.Formula.Checkbox ->
                ApiFormulaDatabaseQueryFilter(checkbox = model.modelToApi(ApiFormulaCheckboxDatabaseQueryFilterConverter))
            is DatabaseQueryPredicate.Formula.Number ->
                ApiFormulaDatabaseQueryFilter(number = model.modelToApi(ApiFormulaNumberDatabaseQueryFilterConverter))
            is DatabaseQueryPredicate.Formula.Date ->
                ApiFormulaDatabaseQueryFilter(date = model.modelToApi(ApiFormulaDateDatabaseQueryFilterConverter))
            else -> throw AssertionError()
        }
    }
}

internal object ApiFormulaTextDatabaseQueryFilterConverter :
    ApiConverter<ApiTextDatabaseQueryFilter, DatabaseQueryPredicate.Formula.Text>() {
    override fun modelToApi(model: DatabaseQueryPredicate.Formula.Text): ApiTextDatabaseQueryFilter {
        return when (model) {
            is DatabaseQueryPredicate.Formula.Text.Equals ->
                ApiTextDatabaseQueryFilter(equals = model.value)
            is DatabaseQueryPredicate.Formula.Text.DoesNotEqual ->
                ApiTextDatabaseQueryFilter(does_not_equal = model.value)
            is DatabaseQueryPredicate.Formula.Text.Contains ->
                ApiTextDatabaseQueryFilter(contains = model.value)
            is DatabaseQueryPredicate.Formula.Text.DoesNotContain ->
                ApiTextDatabaseQueryFilter(does_not_contain = model.value)
            is DatabaseQueryPredicate.Formula.Text.StartsWith ->
                ApiTextDatabaseQueryFilter(starts_with = model.value)
            is DatabaseQueryPredicate.Formula.Text.EndsWith ->
                ApiTextDatabaseQueryFilter(ends_with = model.value)
            is DatabaseQueryPredicate.Formula.Text.IsEmpty ->
                ApiTextDatabaseQueryFilter(is_empty = true)
            is DatabaseQueryPredicate.Formula.Text.IsNotEmpty ->
                ApiTextDatabaseQueryFilter(is_not_empty = true)
            else -> throw AssertionError()
        }
    }
}

internal object ApiFormulaCheckboxDatabaseQueryFilterConverter :
    ApiConverter<ApiCheckboxDatabaseQueryFilter, DatabaseQueryPredicate.Formula.Checkbox>() {
    override fun modelToApi(model: DatabaseQueryPredicate.Formula.Checkbox): ApiCheckboxDatabaseQueryFilter {
        return if (model.isChecked) {
            ApiCheckboxDatabaseQueryFilter(equals = true)
        } else {
            ApiCheckboxDatabaseQueryFilter(does_not_equal = true)
        }
    }
}

internal object ApiFormulaNumberDatabaseQueryFilterConverter :
    ApiConverter<ApiNumberDatabaseQueryFilter, DatabaseQueryPredicate.Formula.Number>() {
    override fun modelToApi(model: DatabaseQueryPredicate.Formula.Number): ApiNumberDatabaseQueryFilter {
        return when (model) {
            is DatabaseQueryPredicate.Formula.Number.Equals ->
                ApiNumberDatabaseQueryFilter(equals = model.value.toDouble())
            is DatabaseQueryPredicate.Formula.Number.DoesNotEqual ->
                ApiNumberDatabaseQueryFilter(does_not_equal = model.value.toDouble())
            is DatabaseQueryPredicate.Formula.Number.GreaterThan ->
                ApiNumberDatabaseQueryFilter(greater_than = model.value.toDouble())
            is DatabaseQueryPredicate.Formula.Number.LessThan ->
                ApiNumberDatabaseQueryFilter(less_than = model.value.toDouble())
            is DatabaseQueryPredicate.Formula.Number.GreaterThanOrEqualTo ->
                ApiNumberDatabaseQueryFilter(greater_than_or_equal_to = model.value.toDouble())
            is DatabaseQueryPredicate.Formula.Number.LessThanOrEqualTo ->
                ApiNumberDatabaseQueryFilter(less_than_or_equal_to = model.value.toDouble())
            is DatabaseQueryPredicate.Formula.Number.IsEmpty ->
                ApiNumberDatabaseQueryFilter(is_empty = true)
            is DatabaseQueryPredicate.Formula.Number.IsNotEmpty ->
                ApiNumberDatabaseQueryFilter(is_not_empty = true)
            else -> throw AssertionError()
        }
    }
}

internal object ApiFormulaDateDatabaseQueryFilterConverter :
    ApiConverter<ApiDateDatabaseQueryFilter, DatabaseQueryPredicate.Formula.Date>() {
    override fun modelToApi(model: DatabaseQueryPredicate.Formula.Date): ApiDateDatabaseQueryFilter {
        return when (model) {
            is DatabaseQueryPredicate.Formula.Date.Equals ->
                ApiDateDatabaseQueryFilter(equals = model.value.modelToApi(ApiDateStringConverter))
            is DatabaseQueryPredicate.Formula.Date.Before ->
                ApiDateDatabaseQueryFilter(before = model.value.modelToApi(ApiDateStringConverter))
            is DatabaseQueryPredicate.Formula.Date.After ->
                ApiDateDatabaseQueryFilter(after = model.value.modelToApi(ApiDateStringConverter))
            is DatabaseQueryPredicate.Formula.Date.OnOrBefore ->
                ApiDateDatabaseQueryFilter(on_or_before = model.value.modelToApi(ApiDateStringConverter))
            is DatabaseQueryPredicate.Formula.Date.OnOrAfter ->
                ApiDateDatabaseQueryFilter(on_or_after = model.value.modelToApi(ApiDateStringConverter))
            is DatabaseQueryPredicate.Formula.Date.IsPastWeek ->
                ApiDateDatabaseQueryFilter(past_week = mapOf())
            is DatabaseQueryPredicate.Formula.Date.IsPastMonth ->
                ApiDateDatabaseQueryFilter(past_week = mapOf())
            is DatabaseQueryPredicate.Formula.Date.IsPastYear ->
                ApiDateDatabaseQueryFilter(past_week = mapOf())
            is DatabaseQueryPredicate.Formula.Date.IsNextWeek ->
                ApiDateDatabaseQueryFilter(past_week = mapOf())
            is DatabaseQueryPredicate.Formula.Date.IsNextMonth ->
                ApiDateDatabaseQueryFilter(past_week = mapOf())
            is DatabaseQueryPredicate.Formula.Date.IsNextYear ->
                ApiDateDatabaseQueryFilter(past_week = mapOf())
            is DatabaseQueryPredicate.Formula.Date.IsEmpty ->
                ApiDateDatabaseQueryFilter(is_empty = true)
            is DatabaseQueryPredicate.Formula.Date.IsNotEmpty ->
                ApiDateDatabaseQueryFilter(is_not_empty = true)
            else -> throw AssertionError()
        }
    }
}
