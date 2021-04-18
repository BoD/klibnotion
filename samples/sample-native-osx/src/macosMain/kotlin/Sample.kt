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

import kotlinx.coroutines.runBlocking
import org.jraf.klibnotion.client.Authentication
import org.jraf.klibnotion.client.ClientConfiguration
import org.jraf.klibnotion.client.HttpConfiguration
import org.jraf.klibnotion.client.HttpLoggingLevel
import org.jraf.klibnotion.client.HttpProxy
import org.jraf.klibnotion.client.NotionClient
import org.jraf.klibnotion.model.block.Block
import org.jraf.klibnotion.model.block.BulletedListItemBlock
import org.jraf.klibnotion.model.block.ChildPageBlock
import org.jraf.klibnotion.model.block.Heading1Block
import org.jraf.klibnotion.model.block.Heading2Block
import org.jraf.klibnotion.model.block.Heading3Block
import org.jraf.klibnotion.model.block.NumberedListItemBlock
import org.jraf.klibnotion.model.block.ParagraphBlock
import org.jraf.klibnotion.model.block.ToDoBlock
import org.jraf.klibnotion.model.block.ToggleBlock
import org.jraf.klibnotion.model.color.Color
import org.jraf.klibnotion.model.database.Database
import org.jraf.klibnotion.model.database.query.DatabaseQuery
import org.jraf.klibnotion.model.database.query.DatabaseQuerySort
import org.jraf.klibnotion.model.database.query.filter.DatabaseQueryPredicate
import org.jraf.klibnotion.model.database.query.filter.DatabaseQueryPropertyFilter
import org.jraf.klibnotion.model.date.Date
import org.jraf.klibnotion.model.date.DateOrDateRange
import org.jraf.klibnotion.model.date.DateTime
import org.jraf.klibnotion.model.page.Page
import org.jraf.klibnotion.model.pagination.ResultPage
import org.jraf.klibnotion.model.property.SelectOption
import org.jraf.klibnotion.model.property.value.PropertyValueList
import org.jraf.klibnotion.model.richtext.Annotations
import org.jraf.klibnotion.model.richtext.RichTextList
import org.jraf.klibnotion.model.user.User
import platform.Foundation.NSDate
import kotlin.random.Random
import kotlin.system.exitProcess

// !!!!! DO THIS FIRST !!!!!
// Replace this constant with your API key
// that you will find by following the instructions here: https://www.notion.so/Getting-started-da32a6fc1bcc4403a6126ee735710d89
private const val API_KEY = "secret_XXX"

// Replace this constant with a user id that exists
private const val USER_ID = "00000000-0000-0000-0000-000000000000"

// Replace this constant with a database id that exists
private const val DATABASE_ID = "00000000-0000-0000-0000-000000000000"

// Replace this constant with a page id that exists
private const val PAGE_ID = "00000000-0000-0000-0000-000000000000"

class Sample {
    private val client: NotionClient by lazy {
        // Create the client
        NotionClient.newInstance(
            ClientConfiguration(
                Authentication(API_KEY),
                HttpConfiguration(
                    // Uncomment to see more logs
                    // loggingLevel = HttpLoggingLevel.BODY,
                    loggingLevel = HttpLoggingLevel.INFO,
                    // This is only needed to debug with, e.g., Charles Proxy
                    httpProxy = HttpProxy("localhost", 8888),
                    // Can be useful in certain circumstances, but unwise to use in production
                    bypassSslChecks = true
                )
            )
        )
    }

    fun main() {
        runBlocking {
            // Get user
            println("User:")
            val user: User = client.users.getUser(USER_ID)
            println(user)

            // Get user list
            println("User list first page:")
            val userResultPage: ResultPage<User> = client.users.getUserList()
            println(userResultPage)

            // Get database
            println("Database:")
            val database: Database = client.databases.getDatabase(DATABASE_ID)
            println(database)
            println("title=${database.title.plainText}")

            // Get database list
            println("Database list first page:")
            val databasePage: ResultPage<Database> = client.databases.getDatabaseList()
            println(databasePage)

            // Query database (simple)
            println("Simple query results:")
            val simpleQueryResultPage: ResultPage<Page> =
                client.databases.queryDatabase(DATABASE_ID)
            println(simpleQueryResultPage.results.joinToString("") { it.toFormattedString() })

            // Query database (filters)
            println("Filtered query results:")
            val filteredQueryResultPage: ResultPage<Page> = client.databases.queryDatabase(
                DATABASE_ID,
                query = DatabaseQuery()
                    .any(
                        DatabaseQueryPropertyFilter.Text(
                            propertyIdOrName = "Famous quote",
                            predicate = DatabaseQueryPredicate.Text.Equals("a")
                        ),
                        DatabaseQueryPropertyFilter.Text(
                            propertyIdOrName = "Famous quote",
                            predicate = DatabaseQueryPredicate.Text.Contains("imp")
                        ),
                        DatabaseQueryPropertyFilter.Number(
                            propertyIdOrName = "Legs",
                            predicate = DatabaseQueryPredicate.Number.GreaterThanOrEqualTo(4)
                        ),
                        DatabaseQueryPropertyFilter.Formula(
                            propertyIdOrName = "Legs plus one",
                            predicate = DatabaseQueryPredicate.Formula.Number.GreaterThan(4)
                        ),
                        DatabaseQueryPropertyFilter.Checkbox(
                            propertyIdOrName = "Is Greedo",
                            predicate = DatabaseQueryPredicate.Checkbox(true)
                        ),
                    ),
                sort = DatabaseQuerySort()
                    .ascending("Created time")
                    .descending("title")
            )
            println(filteredQueryResultPage.results.joinToString("") { it.toFormattedString() })

            // Get page
            println("Page:")
            val page: Page = client.pages.getPage(PAGE_ID)
            println(page)

            // Create page
            println("Created page:")
            val createdPage: Page = client.pages.createPage(
                parentDatabaseId = DATABASE_ID,
                properties = PropertyValueList()
                    .number("Legs", Random.nextInt())
                    .text("Name", "Name ${Random.nextInt()}")
                    .text("title", "Title ${Random.nextInt()}", annotations = Annotations(color = Color.BLUE))
                    .text("Oui", RichTextList()
                        .text("default ")
                        .text("red ", Annotations(color = Color.RED))
                        .text("pink background ", Annotations(color = Color.PINK_BACKGROUND))
                        .text("bold ", Annotations.BOLD)
                        .text("italic ", Annotations.ITALIC)
                        .text("strikethrough ", Annotations.STRIKETHROUGH)
                        .text("underline ", Annotations.UNDERLINE)
                        .text("code ", Annotations.CODE)
                        .text("mixed", Annotations(bold = true, italic = true, color = Color.PURPLE))
                    )
                    .text("Non", RichTextList()
                        .text("some url", linkUrl = "https://JRAF.org").text("\n")
                        .userMention(USER_ID).text("\n")
                        .databaseMention(DATABASE_ID).text("\n")
                        .pageMention(PAGE_ID).text("\n")
                        .dateMention(DateTime(NSDate()), annotations = Annotations(color = Color.GREEN))
                        .text("\n")
                        .equation("f(\\relax{x}) = \\int_{-\\infty}^\\infty \\hat f(\\xi)\\,e^{2 \\pi i \\xi x} \\,d\\xi",
                            Annotations(color = Color.YELLOW))
                    )
                    .selectByName("Species", "Alien")
                    .multiSelectByNames("Planets", "Tatooine", "Bespin")
                    .date("Some date",
                        DateOrDateRange(
                            start = DateTime(NSDate()),
                            end = Date(NSDate(NSDate().timeIntervalSinceReferenceDate + 24L * 3600L)))
                    )
                    .relation("Android version", PAGE_ID)
                    .people("User", USER_ID)
                    .checkbox("Is Greedo", Random.nextBoolean())
                    .string("Email", "aaa@aaa.com")
                    .string("Phone", "+1 424 2424 266")
                    .string("Url", "https://zgluteks.com")
            ) {
                heading1("First section")
                paragraph("Hello, World!")

                heading1("Second section")
                paragraph("This paragraph is bold", annotations = Annotations.BOLD) {
                    paragraph("Sub paragraph 1")
                    paragraph("Sub paragraph 2") {
                        paragraph("Sub sub paragraph") {

                        }
                    }
                }

                heading2("But then again")
                heading3("Actually")
                paragraph("That's the case")

                heading3("But really")
                paragraph(RichTextList().text("This ").text("word", Annotations(color = Color.RED)).text(" is red"))

                bullet("There's this,")
                bullet("there's that,")
                bullet("then there's...") {
                    paragraph("Will this work?")
                }
                bullet("indentation?") {
                    bullet("indentation? 2") {
                        bullet("indentation? 3")
                    }
                }

                number("First")
                number("Second") {
                    number("Second second")
                }
                number("Third")

                toDo("This one is checked", true)
                toDo("This one is not checked", false)

                toggle("This is a toggle!") {
                    paragraph("This first paragraph is inside the toggle")
                    paragraph("This second paragraph is inside the toggle")
                    heading3("This too!")
                }
            }

            println(createdPage)

            // Update page
            println("Updated page:")
            val updatedPage: Page = client.pages.updatePage(
                id = PAGE_ID,
                PropertyValueList()
                    .number("Legs", Random.nextInt())
                    .text("Name", "Updated page ${Random.nextInt()}")
                    .text("title", "Updated page ${Random.nextInt()}")
                    .selectByName("Species", "Alien")
                    .multiSelectByNames("Planets", "Tatooine", "Bespin")
                    .date("Some date",
                        DateOrDateRange(
                            start = DateTime(NSDate()),
                            end = Date(NSDate(NSDate().timeIntervalSinceReferenceDate + 24L * 3600L)))
                    )
                    .relation("Android version", PAGE_ID)
                    .people("User", USER_ID)
                    .checkbox("Is Greedo", Random.nextBoolean())
                    .string("Email", "aaa@aaa.com")
                    .string("Phone", "+1 424 2424 266")
                    .string("Url", "https://zgluteks.com")
            )
            println(updatedPage)

            // Get page contents
            println("Page contents:")
            val pageContents = client.blocks.getBlockList(PAGE_ID)
            println(pageContents.results.toFormattedString())


            // Append contents to page
            println("Appending contents")
            client.blocks.appendBlockList(PAGE_ID) { paragraph("This paragraph was added on ${NSDate()}") }
        }

        // Close
        client.close()

        // Exit process
        exitProcess(0)
    }

    private fun Page.toFormattedString(): String {
        val res = StringBuilder("-----------\n")
        res.append("Id: $id\n")
        for (propertyValue in propertyValues) {
            res.append("${propertyValue.name}: ${propertyValue.value.toFormattedString()}\n")
        }
        return res.toString()
    }

    private fun <T> T.toFormattedString(): String {
        return when (this) {
            is RichTextList -> plainText ?: ""
            is SelectOption -> name
            else -> toString()
        }
    }

    private fun List<Block>.toFormattedString(level: Int = 0): String {
        val res = StringBuilder()
        val levelStr = "  ".repeat(level)
        var numberedListIndex = 1
        for (block in this) {
            res.appendLine(levelStr + when (block) {
                is BulletedListItemBlock -> "-"
                is ChildPageBlock -> "->"
                is Heading1Block -> "#"
                is Heading2Block -> "##"
                is Heading3Block -> "###"
                is NumberedListItemBlock -> "${numberedListIndex}."
                is ParagraphBlock -> "¶"
                is ToDoBlock -> if (block.checked) "[X]" else "[ ]"
                is ToggleBlock -> "▼"
                else -> "?"
            } + " " + block.text.toFormattedString())

            // Recurse
            if (!block.children.isNullOrEmpty()) {
                res.append(block.children!!.toFormattedString(level + 1))
            }

            if (block is NumberedListItemBlock) numberedListIndex++ else numberedListIndex = 1
        }
        return res.toString()
    }
}

fun main() = Sample().main()
