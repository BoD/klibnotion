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
import org.jraf.klibnotion.model.base.reference.DatabaseReference
import org.jraf.klibnotion.model.base.reference.PageReference
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
import org.jraf.klibnotion.model.block.UnknownTypeBlock
import org.jraf.klibnotion.model.color.Color
import org.jraf.klibnotion.model.database.Database
import org.jraf.klibnotion.model.database.query.DatabaseQuery
import org.jraf.klibnotion.model.database.query.filter.DatabaseQueryPredicate
import org.jraf.klibnotion.model.database.query.filter.DatabaseQueryPropertyFilter
import org.jraf.klibnotion.model.date.Date
import org.jraf.klibnotion.model.date.DateOrDateRange
import org.jraf.klibnotion.model.date.DateTime
import org.jraf.klibnotion.model.oauth.OAuthCredentials
import org.jraf.klibnotion.model.page.Page
import org.jraf.klibnotion.model.pagination.ResultPage
import org.jraf.klibnotion.model.property.SelectOption
import org.jraf.klibnotion.model.property.sort.PropertySort
import org.jraf.klibnotion.model.property.value.PropertyValueList
import org.jraf.klibnotion.model.richtext.Annotations
import org.jraf.klibnotion.model.richtext.RichTextList
import org.jraf.klibnotion.model.user.User
import platform.Foundation.NSDate
import kotlin.random.Random
import kotlin.system.exitProcess

// !!!!! DO THIS FIRST !!!!!
// Replace this constant with your Internal Integration Token
// that you will find by following the instructions here: https://developers.notion.com/docs/getting-started
private const val TOKEN = "secret_XXX"

// Or, alternatively, if you have registered your app integration to be public, use OAuth.
// More information about OAuth here:
// https://developers.notion.com/docs/authorization#authorizing-public-integrations
private const val OAUTH_CLIENT_ID = "00000000-0000-0000-0000-000000000000"
private const val OAUTH_CLIENT_SECRET = "secret_XXX"
private const val OAUTH_REDIRECT_URI = "https://example.com"

// Set to false to use the TOKEN above, true to setup and use OAuth
private const val USE_OAUTH = false


// Replace this constant with a user id that exists
private const val USER_ID = "00000000-0000-0000-0000-000000000000"

// Replace this constant with a database id that exists
private const val DATABASE_ID = "00000000-0000-0000-0000-000000000000"

// Replace this constant with a page id that exists
private const val PAGE_ID = "00000000-0000-0000-0000-000000000000"

class Sample {
    private val authentication = Authentication()

    private val client: NotionClient by lazy {
        // Create the client
        NotionClient.newInstance(
            ClientConfiguration(
                authentication,
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
            if (USE_OAUTH) {
                val oAuthCredentials = OAuthCredentials(
                    clientId = OAUTH_CLIENT_ID,
                    clientSecret = OAUTH_CLIENT_SECRET,
                    redirectUri = OAUTH_REDIRECT_URI
                )

                // 1/ Authenticate the user / integration
                val uniqueState = Random.nextLong().toString()
                println("Navigate to this URL in a browser:")
                println(client.oAuth.getUserPromptUri(oAuthCredentials = oAuthCredentials, uniqueState = uniqueState))

                // 2/ Extract code
                println("After successful authentication please paste the URL in the browser's bar, and press enter:")
                val redirectUri = readLine()!!
                val codeAndUniqueState = client.oAuth.extractCodeAndStateFromRedirectUri(redirectUri)
                println(codeAndUniqueState)
                if (codeAndUniqueState == null || codeAndUniqueState.state != uniqueState) {
                    println("Something is wrong! Giving up.")
                    return@runBlocking
                }

                // 3/ Exchange code for an access token
                val getAccessTokenResult = client.oAuth.getAccessToken(
                    oAuthCredentials = oAuthCredentials,
                    code = codeAndUniqueState.code
                )
                println(getAccessTokenResult)

                // 4/ Use obtained access token for subsequent API calls
                authentication.accessToken = getAccessTokenResult.accessToken
            } else {
                authentication.accessToken = TOKEN
            }

            // Get user list
            println("User list first page:")
            val userResultPage: ResultPage<User> = client.users.getUserList()
            println(userResultPage)

            // Get user
            println("User:")
            val user: User = client.users.getUser(USER_ID)
            println(user)

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
                sort = PropertySort()
                    .ascending("Created time")
                    .descending("title")
            )
            println(filteredQueryResultPage.results.joinToString("") { it.toFormattedString() })

            // Get page
            println("Page:")
            val page: Page = client.pages.getPage(PAGE_ID)
            println(page)

            // Create page in database
            println("Created page in database:")
            val createdPageInDb: Page = client.pages.createPage(
                parentDatabase = DatabaseReference(DATABASE_ID),
                properties = PropertyValueList()
                    .number("Legs", Random.nextInt())
                    .title("Name", "Name ${Random.nextInt()}")
                    .text("title", "Title ${Random.nextInt()}", annotations = Annotations(color = Color.BLUE))
                    .text(
                        "Oui", RichTextList()
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
                    .text(
                        "Non", RichTextList()
                            .text("some url", linkUrl = "https://JRAF.org").text("\n")
                            .userMention(USER_ID).text("\n")
                            .databaseMention(DATABASE_ID).text("\n")
                            .pageMention(PAGE_ID).text("\n")
                            .dateMention(DateTime(NSDate()), annotations = Annotations(color = Color.GREEN))
                            .text("\n")
                            .equation(
                                "f(\\relax{x}) = \\int_{-\\infty}^\\infty \\hat f(\\xi)\\,e^{2 \\pi i \\xi x} \\,d\\xi",
                                Annotations(color = Color.YELLOW)
                            )
                    )
                    .selectByName("Species", "Alien")
                    .multiSelectByNames("Planets", "Tatooine", "Bespin")
                    .date(
                        "Some date",
                        DateOrDateRange(
                            start = DateTime(NSDate()),
                            end = Date(NSDate(NSDate().timeIntervalSinceReferenceDate + 24L * 3600L))
                        )
                    )
                    .relation("Android version", PAGE_ID)
                    .people("User", USER_ID)
                    .checkbox("Is Greedo", Random.nextBoolean())
                    .email("Email", "aaa@aaa.com")
                    .phoneNumber("Phone", "+1 424 2424 266")
                    .url("Url", "https://zgluteks.com")
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

            println(createdPageInDb)

            // Create page in page (without content)
            println("Created page in page (no content):")
            var createdPageInPage: Page = client.pages.createPage(
                parentPage = PageReference(PAGE_ID),
                title = RichTextList().text("The title of my new page! ${Random.nextInt()}")
            )
            println(createdPageInPage)

            // Create page in page (with content)
            println("Created page in page (with content):")
            createdPageInPage = client.pages.createPage(
                parentPage = PageReference(PAGE_ID),
                title = RichTextList().text("The title of my new page! ${Random.nextInt()}")
            )
            {
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

            println(createdPageInPage)

            // Update page
            println("Updated page:")
            val updatedPage: Page = client.pages.updatePage(
                id = PAGE_ID,
                PropertyValueList()
                    .number("Legs", Random.nextInt())
                    .title("Name", "Updated page ${Random.nextInt()}")
                    .text("title", "Updated page ${Random.nextInt()}")
                    .selectByName("Species", "Alien")
                    .multiSelectByNames("Planets", "Tatooine", "Bespin")
                    .date(
                        "Some date",
                        DateOrDateRange(
                            start = DateTime(NSDate()),
                            end = Date(NSDate(NSDate().timeIntervalSinceReferenceDate + 24L * 3600L))
                        )
                    )
                    .relation("Android version", PAGE_ID)
                    .people("User", USER_ID)
                    .checkbox("Is Greedo", Random.nextBoolean())
                    .email("Email", "aaa@aaa.com")
                    .phoneNumber("Phone", "+1 424 2424 266")
                    .url("Url", "https://zgluteks.com")
            )
            println(updatedPage)

            // Get page contents
            println("Page contents:")
            val pageContents = client.blocks.getAllBlockListRecursively(PAGE_ID)
            println(pageContents.toFormattedString())

            // Append contents to page
            println("Appending contents")
            client.blocks.appendBlockList(PAGE_ID) { paragraph("This paragraph was added on ${NSDate()}") }

            // Search pages (simple)
            println("Page search results (simple):")
            val simplePageSearchResults = client.search.searchPages()
            println(simplePageSearchResults)

            // Search pages (query and sort)
            println("Page search results (query):")
            val queryPageSearchResults =
                client.search.searchPages(
                    query = "smith",
                    sort = PropertySort().descending("last_edited_time")
                )
            println(queryPageSearchResults)

            // Search databases (simple)
            println("Databases search results (simple):")
            val simpleDatabasesSearchResults = client.search.searchDatabases()
            println(simpleDatabasesSearchResults)

            // Search databases (query and sort)
            println("Databases search results (query):")
            val queryDatabasesSearchResults =
                client.search.searchDatabases(
                    query = "test",
                    sort = PropertySort().descending("last_edited_time")
                )
            println(queryDatabasesSearchResults)
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
            res.appendLine(
                levelStr + when (block) {
                    is BulletedListItemBlock -> "-"
                    is ChildPageBlock -> "->"
                    is Heading1Block -> "#"
                    is Heading2Block -> "##"
                    is Heading3Block -> "###"
                    is NumberedListItemBlock -> "${numberedListIndex}."
                    is ParagraphBlock -> "¶"
                    is ToDoBlock -> if (block.checked) "[X]" else "[ ]"
                    is ToggleBlock -> "▼"
                    is UnknownTypeBlock -> "?"
                } + " " + block.text.toFormattedString()
            )

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
