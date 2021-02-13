# klibnotion

A [Notion API (currently beta)](https://www.notion.so/Notion-API-spec-c29dd39d851543b49a24e1571f63c488) client library
for Kotlin, Java and more.

This library is written in [Kotlin Multiplatform](https://kotlinlang.org/docs/reference/multiplatform.html)
so _in theory_ it can be used from the JVM, Android, iOS, Linux, MacOS, Windows, Javascript and more. In practice this
library has been tested and has samples for the JVM (Kotlin and Java), and MacOS (Kotlin).

Several flavors of the client are available to match your needs:

- [Coroutines (`suspend`) based](https://github.com/BoD/klibnotion/blob/master/library/src/commonMain/kotlin/org/jraf/klibnotion/client/NotionClient.kt):
  the default client for Kotlin projects
- [Blocking](https://github.com/BoD/klibnotion/blob/master/library/src/commonMain/kotlin/org/jraf/klibnotion/client/blocking/BlockingNotionClient.kt):
  useful for Java projects, or if you have your own async mechanism
- [`Future` based (JVM only)](https://github.com/BoD/klibnotion/blob/master/library/src/jvmMain/kotlin/org/jraf/klibnotion/client/future/FutureNotionClient.kt):
  useful for Java projects

## Usage

### 1/ Add the dependencies to your project

#### Gradle based projects

The artifact is hosted on the Maven Central repository.

```groovy
repositories {
    /* ... */
    mavenCentral()
}
```

```groovy
dependencies {
    /* ... */
    implementation 'org.jraf:klibnotion:1.0.0'
}
```

### 2/ Use the client

The easiest way to see how to use it is to look at the samples:

- [Coroutines (Kotlin)](samples/sample-jvm/src/main/kotlin/org/jraf/klibnotion/sample/Sample.kt)
- [Blocking (Java)](samples/sample-jvm/src/main/java/org/jraf/klibnotion/sample/BlockingSample.java)
- [Future (Java)](samples/sample-jvm/src/main/java/org/jraf/klibnotion/sample/FutureSample.java)

#### Get your API key

**Note:** currently the Notion API is in a **private beta**, and only accessible to certain users. Please contact Notion
directly to get more information about the beta. The author of this library is not affiliated with Notion.

You will find your **API key** by following the
instructions [here](https://www.notion.so/Getting-started-da32a6fc1bcc4403a6126ee735710d89).

#### Instantiate a `NotionClient`

```kotlin
val notionClient = NotionClient.newInstance(
    ClientConfiguration(
        Authentication(API_KEY)
    )
)
```

To get other flavors of the client:

- Blocking: `BlockingNotionClient blockingClient = BlockingNotionClientUtils.asBlockingNotionClient(notionClient)`
- Future: `FutureNotionClient futureClient = FutureNotionClientUtils.asFutureNotionClient(notionClient)`

#### Use the `NotionClient`

The client gives access to several API "areas":

- `users`
- `databases`
- `pages`

Each area exposes related APIs, for instance: `notionClient.pages.getPage`.

#### Pagination

The APIs that are paginated all follow the same principle:

- take
  a [`Pagination`](https://github.com/BoD/klibnotion/blob/master/library/src/commonMain/kotlin/org/jraf/klibnotion/model/pagination/Pagination.kt)
  object as a parameter, which defines the page to retrieve
- return
  a [`ResultPage<T>`](https://github.com/BoD/klibnotion/blob/master/library/src/commonMain/kotlin/org/jraf/klibnotion/model/pagination/ResultPage.kt)
  with the result list but also a reference to the next `Pagination` objects (handy when retrieving several pages).

#### Logging

To log HTTP requests/response, pass
a [`HttpConfiguration`](https://github.com/BoD/klibnotion/blob/master/library/src/commonMain/kotlin/org/jraf/klibnotion/client/HttpConfiguration.kt)
to `NotionClient.newInstance()`.

Several levels are available: `NONE`, `INFO`, `HEADERS`, `BODY` and `ALL`

#### Proxy

A proxy can be configured by passing
a [`HttpConfiguration`](https://github.com/BoD/klibnotion/blob/master/library/src/commonMain/kotlin/org/jraf/klibnotion/client/HttpConfiguration.kt)
to `NotionClient.newInstance()`.

## Javascript support

In theory Kotlin Multiplatform projects can also target Javascript but as of today the author couldn't understand how to
make that work. Please [contact the author](mailto:BoD@JRAF.org) if you want to help :)

## Author and License

*Note: this project is not officially related to or endorsed by Notion.*

```
Copyright (C) 2021-present Benoit 'BoD' Lubek (BoD@JRAF.org)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
