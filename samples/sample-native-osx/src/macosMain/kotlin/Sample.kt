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
 * Copyright (C) 2021-present Yu Jinyan (i@yujinyan.me)
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
import org.jraf.klibnotion.model.base.UuidString
import org.jraf.klibnotion.model.base.reference.DatabaseReference
import org.jraf.klibnotion.model.base.reference.PageReference
import org.jraf.klibnotion.model.block.Block
import org.jraf.klibnotion.model.block.BookmarkBlock
import org.jraf.klibnotion.model.block.BulletedListItemBlock
import org.jraf.klibnotion.model.block.CalloutBlock
import org.jraf.klibnotion.model.block.ChildDatabaseBlock
import org.jraf.klibnotion.model.block.ChildPageBlock
import org.jraf.klibnotion.model.block.CodeBlock
import org.jraf.klibnotion.model.block.DividerBlock
import org.jraf.klibnotion.model.block.EmbedBlock
import org.jraf.klibnotion.model.block.EquationBlock
import org.jraf.klibnotion.model.block.Heading1Block
import org.jraf.klibnotion.model.block.Heading2Block
import org.jraf.klibnotion.model.block.Heading3Block
import org.jraf.klibnotion.model.block.NumberedListItemBlock
import org.jraf.klibnotion.model.block.ParagraphBlock
import org.jraf.klibnotion.model.block.QuoteBlock
import org.jraf.klibnotion.model.block.TableOfContentsBlock
import org.jraf.klibnotion.model.block.ToDoBlock
import org.jraf.klibnotion.model.block.ToggleBlock
import org.jraf.klibnotion.model.block.UnknownTypeBlock
import org.jraf.klibnotion.model.block.paragraph
import org.jraf.klibnotion.model.color.Color
import org.jraf.klibnotion.model.database.Database
import org.jraf.klibnotion.model.database.query.DatabaseQuery
import org.jraf.klibnotion.model.database.query.filter.DatabaseQueryPredicate
import org.jraf.klibnotion.model.database.query.filter.DatabaseQueryPropertyFilter
import org.jraf.klibnotion.model.date.Date
import org.jraf.klibnotion.model.date.DateOrDateRange
import org.jraf.klibnotion.model.date.DateTime
import org.jraf.klibnotion.model.emoji.Emoji
import org.jraf.klibnotion.model.file.File
import org.jraf.klibnotion.model.oauth.OAuthCredentials
import org.jraf.klibnotion.model.page.Page
import org.jraf.klibnotion.model.pagination.ResultPage
import org.jraf.klibnotion.model.property.SelectOption
import org.jraf.klibnotion.model.property.SelectOptionList
import org.jraf.klibnotion.model.property.sort.PropertySort
import org.jraf.klibnotion.model.property.spec.NumberPropertySpec
import org.jraf.klibnotion.model.property.spec.PropertySpecList
import org.jraf.klibnotion.model.property.value.PropertyValueList
import org.jraf.klibnotion.model.richtext.Annotations
import org.jraf.klibnotion.model.richtext.RichTextList
import org.jraf.klibnotion.model.richtext.text
import org.jraf.klibnotion.model.user.Person
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

// Replace with a page id that your integration has access to
private const val ROOT_PAGE_ID = "00000000-0000-0000-0000-000000000000"

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
            setupAuthentication()

            // Users
            val userId = getFirstUserId()
            getUserById(userId)

            // Databases
            val databaseId = createDatabase()
            updateDatabaseTitleAndIcon(databaseId)
            updateDatabaseProperties(databaseId)
            getDatabaseList()
            getDatabaseById(databaseId)

            // Pages
            val pageId = createPageInDatabase(databaseId = databaseId, userId = userId)
            getPage(pageId)
            updatePage(pageId)
            archivePage(pageId)
            unarchivePage(pageId)
            createPageInPageNoContent()
            createPageInPageWithContent()
            createPageInDatabaseWithNoProperties(databaseId = databaseId)

            // Database query
            queryDatabaseSimple(databaseId)
            queryDatabaseFilters(databaseId)

            // Blocks
            val blockId = getPageContents(pageId)
            appendContentToPage(pageId)
            getBlockById(blockId)
            getBlockByIdWithChildren(blockId)
            updateBlock(blockId)

            // Search
            searchPagesSimple()
            searchPagesQuerySort()
            searchDatabasesSimple()
            searchDatabaseQuerySort()

            // Close
            client.close()

            // Exit process
            exitProcess(0)
        }
    }

    private suspend fun setupAuthentication() {
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
                throw Exception()
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
    }

    private suspend fun getFirstUserId(): UuidString {
        println("User list first page:")
        val userResultPage: ResultPage<User> = client.users.getUserList()
        println(userResultPage)
        return userResultPage.results.first { it is Person }.id
    }

    private suspend fun getUserById(userId: UuidString) {
        println("User:")
        val user: User = client.users.getUser(userId)
        println(user)
    }

    private suspend fun createDatabase(): UuidString {
        // Create a database
        println("Created database:")
        val createdDatabase = client.databases.createDatabase(
            parentPageId = ROOT_PAGE_ID,
            title = text("A database in a page ${newDateNow()}"),
            icon = Emoji("1️⃣"),
            cover = File("https://upload.wikimedia.org/wikipedia/commons/4/45/Notion_app_logo.png"),
            properties = PropertySpecList()
                .title("The title")
                .checkbox("Is checked")
                .createdBy("Created by")
                .createdTime("Created time")
                .date("Some date")
                .date("Some empty date")
                .email("Email")
                .file("File")
                .lastEditedBy("Last edited by")
                .lastEditedTime("Last edited time")
                .multiSelect("Multi", SelectOptionList()
                    .option("Red", Color.RED)
                    .option("Green", Color.GREEN)
                    .option("Blue", Color.BLUE)
                )
                .number("Number", NumberPropertySpec.NumberFormat.REAL)
                .number("Number 2", NumberPropertySpec.NumberFormat.NUMBER)
                .number("Empty number", NumberPropertySpec.NumberFormat.CANADIAN_DOLLAR)
                .people("People")
                .phoneNumber("Phone")
                .select("Select", SelectOptionList()
                    .option("First", Color.RED)
                    .option("Second", Color.GREEN)
                    .option("Third", Color.BLUE)
                )
                .text("Text 1")
                .text("Text 2")
                .text("Text 3")
                .text("Text 4")
                .url("Url")
                .formula("Url or no url", """if (empty(prop("Url")), "No URL", prop("Url"))""")
            // Unsupported for now
            //.relation(...)
            //.rollup(...)
        )
        println(createdDatabase)
        return createdDatabase.id
    }

    private suspend fun updateDatabaseTitleAndIcon(databaseId: UuidString) {
        println("Updated database:")
        val updatedDatabase = client.databases.updateDatabase(
            databaseId,
            title = text("A database in a page (updated) ${newDateNow()}"),
            icon = Emoji("2️⃣"),
        )
        println(updatedDatabase)
    }

    private suspend fun updateDatabaseProperties(databaseId: UuidString) {
        println("Updated database:")
        val updatedDatabase = client.databases.updateDatabase(
            id = databaseId,
            properties = PropertySpecList().select("Select",
                SelectOptionList()
                    .option("First 2", Color.PURPLE)
                    .option("Second 2", Color.GREEN)
                    .option("Third 2", Color.BLUE)
                    .option("Fourth", Color.ORANGE)
            )
        )
        println(updatedDatabase)
    }

    private suspend fun getDatabaseList() {
        println("Database list first page:")
        val databasePage: ResultPage<Database> = client.databases.getDatabaseList()
        println(databasePage)
    }

    private suspend fun getDatabaseById(databaseId: UuidString) {
        println("Database:")
        val database: Database = client.databases.getDatabase(databaseId)
        println(database)
        println("title=${database.title.plainText}")
    }

    private suspend fun createPageInDatabase(databaseId: UuidString, userId: UuidString): UuidString {
        println("Created page in database:")
        val createdPageInDb: Page = client.pages.createPage(
            parentDatabase = DatabaseReference(databaseId),
            icon = Emoji("⚙️"),
            cover = File("https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__480.jpg️"),
            properties = PropertyValueList()
                .title("The title", "A page in a database ${Random.nextInt()}")
                .checkbox("Is checked", Random.nextBoolean())
                .date(
                    "Some date",
                    DateOrDateRange(
                        start = DateTime(newDateNow()),
                        end = Date(newDateTomorrow())
                    )
                )
                .email("Email", "aaa@aaa.com")
                .multiSelectByNames("Multi", "Red", "Green")
                .number("Number", Random.nextInt())
                .number("Number 2", null)
                .people("People", userId)
                .phoneNumber("Phone", "+1 424 2424 266")
                .selectByName("Select", "Third")

                .text("Text 1", "Title ${Random.nextInt()}", annotations = Annotations(color = Color.BLUE))
                .text(
                    "Text 2", RichTextList()
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
                    "Text 3", RichTextList()
                        .text("some url", linkUrl = "https://JRAF.org").text("\n")
                        .userMention(userId).text("\n")
                        .databaseMention(databaseId).text("\n")
                        .pageMention(ROOT_PAGE_ID).text("\n")
                        .dateMention(DateTime(newDateNow()), annotations = Annotations(color = Color.GREEN))
                        .text("\n")
                        .equation(
                            "f(\\relax{x}) = \\int_{-\\infty}^\\infty \\hat f(\\xi)\\,e^{2 \\pi i \\xi x} \\,d\\xi",
                            Annotations(color = Color.YELLOW)
                        )
                )
                .url("Url", "https://zgluteks.com")
            // Unsupported for now
            // .relation("Relation", ROOT_PAGE_ID)
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
            paragraph(text("This ").text("word", Annotations(color = Color.RED)).text(" is red"))

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

            code("kotlin", """
                data class Person(
                    val firstName: String,
                    val lastName: String,
                )
            """.trimIndent())

            divider()

            equation("f(\\relax{x}) = \\int_{-\\infty}^\\infty \\hat f(\\xi)\\,e^{2 \\pi i \\xi x} \\,d\\xi")

            callout("Warning, this is a callout", Emoji("⚠️"))
            callout("A callout without an emoji, and with children") {
                paragraph("Indeed")
            }

            quote("This is a quote") {
                paragraph("Hello, World!")
                quote("A quote within a quote?")
            }

            embed("https://JRAF.org")

            bookmark("https://JRAF.org", "The caption of this bookmark")

            tableOfContents()
        }

        println(createdPageInDb)
        return createdPageInDb.id
    }

    private suspend fun getPage(pageId: UuidString) {
        println("Page:")
        val page = client.pages.getPage(pageId)
        println(page)
    }

    private suspend fun updatePage(pageId: UuidString) {
        println("Updated page:")
        val updatedPage: Page = client.pages.updatePage(
            id = pageId,
            icon = Emoji("❤️"),
            properties = PropertyValueList()
                .title("The title", "A page in a database (updated) ${Random.nextInt()}")
                .checkbox("Is checked", Random.nextBoolean())
                .text("Text 1", null)
                .number("Number 2", null)
        )
        println(updatedPage)
    }

    private suspend fun archivePage(pageId: UuidString) {
        println("Archived page:")
        val archivedPage: Page = client.pages.setPageArchived(
            id = pageId,
            archived = true,
        )
        println(archivedPage)
    }

    private suspend fun unarchivePage(pageId: UuidString) {
        println("Unarchived page:")
        val unarchivedPage: Page = client.pages.setPageArchived(
            id = pageId,
            archived = false,
        )
        println(unarchivedPage)
    }

    private suspend fun createPageInPageNoContent() {
        println("Created page in page (no content):")
        val createdPageInPage: Page = client.pages.createPage(
            parentPage = PageReference(ROOT_PAGE_ID),
            title = text("A page in a page ${Random.nextInt()}"),
            icon = Emoji("⚙️"),
            cover = File("https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__480.jpg️"),
        )
        println(createdPageInPage)
    }

    private suspend fun createPageInPageWithContent() {
        println("Created page in page (with content):")
        val createdPageInPage: Page = client.pages.createPage(
            parentPage = PageReference(ROOT_PAGE_ID),
            title = text("A page in a page ${Random.nextInt()}"),
            icon = Emoji("⚙️"),
            cover = File("https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__480.jpg️"),
        ) {
            paragraph("Hello, World!")
        }
        println(createdPageInPage)
    }

    private suspend fun createPageInDatabaseWithNoProperties(databaseId: UuidString) {
        println("Created page in database (with no properties):")
        val createdPageInDb: Page = client.pages.createPage(
            parentDatabase = DatabaseReference(databaseId),
            icon = Emoji("⚙️"),
            cover = File("https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__480.jpg️"),
        )
        println(createdPageInDb)
    }

    private suspend fun queryDatabaseSimple(databaseId: UuidString) {
        println("Simple query results:")
        val simpleQueryResultPage: ResultPage<Page> = client.databases.queryDatabase(databaseId)
        println(simpleQueryResultPage.results.joinToString("") { it.toFormattedString() })
    }

    private suspend fun queryDatabaseFilters(databaseId: UuidString) {
        println("Filtered query results:")
        val filteredQueryResultPage: ResultPage<Page> = client.databases.queryDatabase(
            databaseId,
            query = DatabaseQuery()
                .any(
                    DatabaseQueryPropertyFilter.Text(
                        propertyIdOrName = "Text 1",
                        predicate = DatabaseQueryPredicate.Text.Contains("Title")
                    ),
                    DatabaseQueryPropertyFilter.Text(
                        propertyIdOrName = "Text 2",
                        predicate = DatabaseQueryPredicate.Text.Equals("default red pink background bold italic strikethrough underline code mixed")
                    ),
                    DatabaseQueryPropertyFilter.Number(
                        propertyIdOrName = "Number",
                        predicate = DatabaseQueryPredicate.Number.GreaterThanOrEqualTo(1)
                    ),
                    DatabaseQueryPropertyFilter.Formula(
                        propertyIdOrName = "Url or no url",
                        predicate = DatabaseQueryPredicate.Formula.Text.IsNotEmpty
                    ),
                    DatabaseQueryPropertyFilter.Checkbox(
                        propertyIdOrName = "Is checked",
                        predicate = DatabaseQueryPredicate.Checkbox(true)
                    ),
                ),
            sort = PropertySort()
                .ascending("Created time")
                .descending("title")
        )
        println(filteredQueryResultPage.results.joinToString("") { it.toFormattedString() })
    }

    private suspend fun getPageContents(pageId: UuidString): UuidString {
        println("Page contents:")
        val pageContents = client.blocks.getAllBlockListRecursively(pageId)
        println(pageContents.toFormattedString())
        return pageContents[3].id
    }

    private suspend fun appendContentToPage(pageId: UuidString) {
        println("Appending contents")
        client.blocks.appendBlockList(pageId) { paragraph("This paragraph was added on ${newDateNow()}") }
    }

    private suspend fun getBlockById(blockId: UuidString) {
        println("Get specific block:")
        val block = client.blocks.getBlock(blockId)
        println(block)
    }

    private suspend fun getBlockByIdWithChildren(blockId: UuidString) {
        println("Get specific block, with children:")
        val block = client.blocks.getBlock(blockId, retrieveChildrenRecursively = true)
        println(block)
    }

    private suspend fun updateBlock(blockId: UuidString) {
        println("Updated block:")
        val block = client.blocks.updateBlock(blockId, paragraph("A random number: ${Random.nextInt()}"))
        println(block)
    }

    private suspend fun searchPagesSimple() {
        println("Page search results (simple):")
        val simplePageSearchResults = client.search.searchPages()
        println(simplePageSearchResults)
    }

    private suspend fun searchPagesQuerySort() {
        println("Page search results (query):")
        val queryPageSearchResults =
            client.search.searchPages(
                query = "page",
                sort = PropertySort().descending("last_edited_time")
            )
        println(queryPageSearchResults)
    }

    private suspend fun searchDatabasesSimple() {
        println("Databases search results (simple):")
        val simpleDatabasesSearchResults = client.search.searchDatabases()
        println(simpleDatabasesSearchResults)
    }

    private suspend fun searchDatabaseQuerySort() {
        println("Databases search results (query):")
        val queryDatabasesSearchResults = client.search.searchDatabases(
            query = "database",
            sort = PropertySort().descending("last_edited_time")
        )
        println(queryDatabasesSearchResults)
    }

    private fun newDateNow() = NSDate()

    private fun newDateTomorrow() = NSDate(newDateNow().timeIntervalSinceReferenceDate + 24L * 3600L)

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
                    is ChildPageBlock -> "-> ${block.title}"
                    is ChildDatabaseBlock -> "-> ${block.title}"
                    is Heading1Block -> "#"
                    is Heading2Block -> "##"
                    is Heading3Block -> "###"
                    is NumberedListItemBlock -> "${numberedListIndex}."
                    is ParagraphBlock -> "¶"
                    is ToDoBlock -> if (block.checked) "[X]" else "[ ]"
                    is ToggleBlock -> "▼"
                    is CalloutBlock -> "> ${block.icon}"
                    is CodeBlock -> "```${block.language}"
                    is EquationBlock -> "$$"
                    is BookmarkBlock -> "Bookmark: ${block.url}"
                    is EmbedBlock -> "Embed: ${block.url}"
                    is QuoteBlock -> ">"
                    is DividerBlock -> "---"
                    is TableOfContentsBlock -> "toc"
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
