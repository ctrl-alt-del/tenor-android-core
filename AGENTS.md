# AGENTS.md

## Project Overview

`tenor-android-core` is an Android library that provides GIF search and display capabilities for three API versions: **Tenor v1**, **Tenor v2**, and **Klipy v3**. It uses Retrofit for HTTP, Gson for JSON deserialization, and Glide for image loading.

- **Language:** Java 17
- **Build:** Gradle 9.5.1, AGP 9.2.1, version catalog (`gradle/libs.versions.toml`)
- **Tests:** JUnit 4 + MockWebServer, `./gradlew test` (~40 tests)
- **AAR output:** `./gradlew assembleRelease` → `build/outputs/aar/`

## Package Architecture

```
com.tenor.android.core/              ← Tenor v1/v2 + shared infra
├── network/
│   ├── ApiService.java              shared HTTP/Retrofit builder (used by all versions)
│   ├── IApiService.java             generic service interface
│   ├── IApiClient.java              Tenor v1 Retrofit interface
│   ├── ApiClient.java               Tenor v1 static wrapper
│   ├── IApiClientV2.java            Tenor v2 Retrofit interface
│   ├── ApiClientV2.java             Tenor v2 static wrapper
│   ├── CallStub.java, VoidCallBack.java  shared callbacks
│   └── constant/Protocol.java, Protocols.java
├── model/
│   ├── IGif.java                    shared interface
│   └── impl/
│       ├── Result.java              Tenor GIF result (v1 "media" field, v2 "media_formats" field)
│       ├── Image.java, Media.java, MediaCollection.java, Tag.java
│       └── ...                      other Tenor-specific models
├── response/
│   ├── AbstractResponse.java        base class for Tenor responses
│   ├── WeakRefCallback.java        shared callback infrastructure
│   └── impl/                        Tenor response classes (GifsResponse, etc.)
├── constant/
│   ├── ApiVersion.java              V1/V2/V3 endpoint format strings & default servers
│   ├── ContentFilter.java           shared: OFF, LOW, MEDIUM, HIGH
│   └── ...                          other constants (Tenor-specific)
├── util/
│   ├── SdkLog.java                  gated logging (BuildConfig.DEBUG only)
│   └── Abstract*Utils.java          extensible utility classes

com.tenor.android.core.klipy/        ← Klipy v3 (separate root package)
├── network/
│   ├── IApiClientV3.java            Klipy Retrofit interface
│   └── ApiClientV3.java             Klipy static wrapper
├── model/
│   ├── KlipyResponse.java           generically wrapped { result, data }
│   ├── KlipySearchData.java         paginated results
│   ├── KlipyCategoriesData.java     category listings
│   ├── KlipyResult.java             single GIF result
│   ├── KlipyFile.java               4-tier file sizes
│   ├── KlipySizeTier.java           format types per tier
│   ├── KlipyMedia.java              individual media object
│   └── KlipyCategory.java           category entry
└── package-info.java
```

## Key Design Patterns

### Adding a New API Version

1. Add a new enum value to `constant/ApiVersion.java` with endpoint format + default server
2. Create a new Retrofit interface (e.g., `IApiClientV4`)
3. Create model classes in a dedicated package (e.g., `klipy/model/` or `v4/model/`)
4. Create a static wrapper (e.g., `ApiClientV4`) that builds `ApiService` with the new version
5. Add tests: deserialization tests for models, MockWebServer tests for requests
6. Update `AGENTS.md` and `README.md`

### Adding an Endpoint

1. Add a `@GET` or `@POST` method to the Retrofit interface
2. Define request parameters as `@Query` or `@Field` annotations
3. Define return type — reuse existing response models or create new ones
4. Add a convenience method on the static wrapper class
5. Add MockWebServer tests verifying request path, query params, and response deserialization

### Model Versioning

- **Tenor models** extend `Image` → `Media` → `MediaCollection` and use `@SerializedName("media")` (v1) or `@SerializedName("media_formats")` (v2) on `Result`
- **Klipy models** are independent — no inheritance from Tenor classes, own type hierarchy
- All models implement `Serializable`
- Gson fields without `@SerializedName` default to the Java field name

### Service ID Maps

- **V1**: `key`, `anon_id`/`keyboardid`, `aaid`, `locale`, `screen_density`, `contentfilter`
- **V2**: `key`, `client_key`, `country`, `locale`, `contentfilter` (no anon_id/aaid)
- **V3**: `customer_id`, `locale`, `content_filter` (no key — app_key is in the URL path)

### Endpoint URL Formats

```
V1: https://{server}.tenor.com/v1/         (default: g.tenor.com)
V2: https://{server}.googleapis.com/v2/     (default: tenor.googleapis.com)
V3: https://api.klipy.com/api/v1/{app_key}/ (default server: "")
```

Set via `ApiService.Builder.server()` and `ApiVersion`.

## Testing

```bash
./gradlew test        # run all tests
./gradlew test --tests "com.tenor.android.core.klipy.*"  # Klipy tests only
```

Tests use:
- **MockWebServer** for HTTP request verification
- **Gson** for deserialization testing
- **JUnit 4** assertions

No Android emulator or Robolectric required — all tests run as plain JVM unit tests.

## Build & Release

```bash
./gradlew assembleDebug    # debug AAR
./gradlew assembleRelease  # release AAR (with ProGuard optimization)
./gradlew clean test       # clean build + tests
```

Release builds gate all `SdkLog` warnings behind `BuildConfig.DEBUG`.

## Conventions

- `@NonNull` on all return types (Kotlin interop)
- Static wrapper classes use `volatile` + `synchronized` for thread safety
- `getServiceIds()` pattern for service-level parameters
- `ContentFilter`, `MediaFilter`, `AspectRatioRange` use `@StringDef` for type safety
- New APIs get their own root package (e.g., `klipy/`) — never mix with existing
- Shared infrastructure stays in `com.tenor.android.core`
