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

package org.jraf.klibnotion.model.database.query.filter

import org.jraf.klibnotion.model.base.UuidString

interface DatabaseQueryPropertyFilter {
    val propertyIdOrName: String
    val predicate: DatabaseQueryPredicate

    data class Title(
        override val propertyIdOrName: String,
        override val predicate: DatabaseQueryPredicate.Text,
    ) : DatabaseQueryPropertyFilter

    data class Text(
        override val propertyIdOrName: String,
        override val predicate: DatabaseQueryPredicate.Text,
    ) : DatabaseQueryPropertyFilter

    data class Number(
        override val propertyIdOrName: String,
        override val predicate: DatabaseQueryPredicate.Number,
    ) : DatabaseQueryPropertyFilter

    data class Checkbox(
        override val propertyIdOrName: String,
        override val predicate: DatabaseQueryPredicate.Checkbox,
    ) : DatabaseQueryPropertyFilter

    data class Select(
        override val propertyIdOrName: String,
        override val predicate: DatabaseQueryPredicate.Select,
    ) : DatabaseQueryPropertyFilter

    data class MultiSelect(
        override val propertyIdOrName: String,
        override val predicate: DatabaseQueryPredicate.MultiSelect,
    ) : DatabaseQueryPropertyFilter

    data class Date(
        override val propertyIdOrName: String,
        override val predicate: DatabaseQueryPredicate.Date,
    ) : DatabaseQueryPropertyFilter

    data class People(
        override val propertyIdOrName: String,
        override val predicate: DatabaseQueryPredicate.People,
    ) : DatabaseQueryPropertyFilter

    data class Files(
        override val propertyIdOrName: String,
        override val predicate: DatabaseQueryPredicate.Files,
    ) : DatabaseQueryPropertyFilter

    data class Relation(
        override val propertyIdOrName: String,
        override val predicate: DatabaseQueryPredicate.Relation,
    ) : DatabaseQueryPropertyFilter

    data class Formula(
        override val propertyIdOrName: String,
        override val predicate: DatabaseQueryPredicate.Formula,
    ) : DatabaseQueryPropertyFilter

    data class Url(
        override val propertyIdOrName: String,
        override val predicate: DatabaseQueryPredicate.Text,
    ) : DatabaseQueryPropertyFilter

    data class Email(
        override val propertyIdOrName: String,
        override val predicate: DatabaseQueryPredicate.Text,
    ) : DatabaseQueryPropertyFilter

    data class Phone(
        override val propertyIdOrName: String,
        override val predicate: DatabaseQueryPredicate.Text,
    ) : DatabaseQueryPropertyFilter

    data class CreatedBy(
        override val propertyIdOrName: String,
        override val predicate: DatabaseQueryPredicate.People,
    ) : DatabaseQueryPropertyFilter

    data class CreatedTime(
        override val propertyIdOrName: String,
        override val predicate: DatabaseQueryPredicate.Date,
    ) : DatabaseQueryPropertyFilter

    data class LastEditedBy(
        override val propertyIdOrName: String,
        override val predicate: DatabaseQueryPredicate.People,
    ) : DatabaseQueryPropertyFilter

    data class LastEditedTime(
        override val propertyIdOrName: String,
        override val predicate: DatabaseQueryPredicate.Date,
    ) : DatabaseQueryPropertyFilter
}

interface DatabaseQueryPredicate {
    interface Text : DatabaseQueryPredicate {
        data class Equals(
            val value: String,
        ) : Text

        data class DoesNotEqual(
            val value: String,
        ) : Text

        data class Contains(
            val value: String,
        ) : Text

        data class DoesNotContain(
            val value: String,
        ) : Text

        data class StartsWith(
            val value: String,
        ) : Text

        data class EndsWith(
            val value: String,
        ) : Text

        object IsEmpty : Text

        object IsNotEmpty : Text
    }

    interface Number : DatabaseQueryPredicate {
        data class Equals(
            val value: kotlin.Number,
        ) : Number

        data class DoesNotEqual(
            val value: kotlin.Number,
        ) : Number

        data class GreaterThan(
            val value: kotlin.Number,
        ) : Number

        data class LessThan(
            val value: kotlin.Number,
        ) : Number

        data class GreaterThanOrEqualTo(
            val value: kotlin.Number,
        ) : Number

        data class LessThanOrEqualTo(
            val value: kotlin.Number,
        ) : Number

        object IsEmpty : Number

        object IsNotEmpty : Number
    }

    data class Checkbox(
        val isChecked: Boolean,
    ) : DatabaseQueryPredicate

    interface Select : DatabaseQueryPredicate {
        data class Equals(
            val value: String,
        ) : Select

        data class DoesNotEqual(
            val value: String,
        ) : Select

        object IsEmpty : Select

        object IsNotEmpty : Select
    }

    interface MultiSelect : DatabaseQueryPredicate {
        data class Contains(
            val value: String,
        ) : MultiSelect

        data class DoesNotContain(
            val value: String,
        ) : MultiSelect

        object IsEmpty : MultiSelect

        object IsNotEmpty : MultiSelect
    }

    interface Date : DatabaseQueryPredicate {
        data class Equals(
            val value: org.jraf.klibnotion.model.date.Date,
        ) : Date

        data class Before(
            val value: org.jraf.klibnotion.model.date.Date,
        ) : Date

        data class After(
            val value: org.jraf.klibnotion.model.date.Date,
        ) : Date

        data class OnOrBefore(
            val value: org.jraf.klibnotion.model.date.Date,
        ) : Date

        data class OnOrAfter(
            val value: org.jraf.klibnotion.model.date.Date,
        ) : Date

        object IsPastWeek : Date

        object IsPastMonth : Date

        object IsPastYear : Date

        object IsNextWeek : Date

        object IsNextMonth : Date

        object IsNextYear : Date

        object IsEmpty : Date

        object IsNotEmpty : Date
    }

    interface People : DatabaseQueryPredicate {
        data class Contains(
            val value: UuidString,
        ) : People

        data class DoesNotContain(
            val value: UuidString,
        ) : People

        object IsEmpty : People

        object IsNotEmpty : People
    }

    interface Files : DatabaseQueryPredicate {
        object IsEmpty : Files

        object IsNotEmpty : Files
    }

    interface Relation : DatabaseQueryPredicate {
        data class Contains(
            val value: UuidString,
        ) : Relation

        data class DoesNotContain(
            val value: UuidString,
        ) : Relation

        object IsEmpty : Relation

        object IsNotEmpty : Relation
    }

    interface Formula : DatabaseQueryPredicate {
        interface Text : Formula {
            data class Equals(
                val value: String,
            ) : Text

            data class DoesNotEqual(
                val value: String,
            ) : Text

            data class Contains(
                val value: String,
            ) : Text

            data class DoesNotContain(
                val value: String,
            ) : Text

            data class StartsWith(
                val value: String,
            ) : Text

            data class EndsWith(
                val value: String,
            ) : Text

            object IsEmpty : Text

            object IsNotEmpty : Text
        }

        data class Checkbox(
            val isChecked: Boolean,
        ) : Formula

        interface Number : Formula {
            data class Equals(
                val value: kotlin.Number,
            ) : Number

            data class DoesNotEqual(
                val value: kotlin.Number,
            ) : Number

            data class GreaterThan(
                val value: kotlin.Number,
            ) : Number

            data class LessThan(
                val value: kotlin.Number,
            ) : Number

            data class GreaterThanOrEqualTo(
                val value: kotlin.Number,
            ) : Number

            data class LessThanOrEqualTo(
                val value: kotlin.Number,
            ) : Number

            object IsEmpty : Number

            object IsNotEmpty : Number
        }

        interface Date : Formula {
            data class Equals(
                val value: org.jraf.klibnotion.model.date.Date,
            ) : Date

            data class Before(
                val value: org.jraf.klibnotion.model.date.Date,
            ) : Date

            data class After(
                val value: org.jraf.klibnotion.model.date.Date,
            ) : Date

            data class OnOrBefore(
                val value: org.jraf.klibnotion.model.date.Date,
            ) : Date

            data class OnOrAfter(
                val value: org.jraf.klibnotion.model.date.Date,
            ) : Date

            object IsPastWeek : Date

            object IsPastMonth : Date

            object IsPastYear : Date

            object IsNextWeek : Date

            object IsNextMonth : Date

            object IsNextYear : Date

            object IsEmpty : Date

            object IsNotEmpty : Date
        }
    }
}