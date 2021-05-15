# Changelog

## Next (????-??-??)

- Add a `getAllBlockListRecursively` API to easily retrieve a whole document.
- (Breaking change) use of Sealed Interfaces - a few interfaces were moved to their parent package due to this change
- Use the new syntax for writing property values
- Add ability to create a page inside a page (but currently doesn't work on the Notion API side)
- Add `Notion-Version` header to all calls

## v1.4.0 (2021-04-18)

- Remove `isArchived` parameter from `getPage` following the Notion API change.
- Fix write content API following the Notion API change.
- Support for "Retrieve block children" API
- Support for "Append block children" API

## v1.3.0 (2021-04-03)

- Support for "get database list" API

## v1.2.0 (2021-03-21)

- Support for content in page create API

## v1.1.0 (2021-02-27)

- Support for Array Rollup property type
- Slightly improve `DatabaseQuery` API (`any` instead of `addAnyFilters`, `all` instead of `addAllFilters`)
- Slightly improve `DatabaseQuerySort` API (`ascending` and `descending` instead of `add`)
- Add a `bypassSslChecks` option on `HttpConfiguration` (should never be used in production)

## v1.0.0 (2021-02-14)

Initial release.
