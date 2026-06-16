# Tenor Android Core

Android SDK for integrating Tenor GIF search and the Klipy GIF API into Android applications. Supports **Tenor v1**, **Tenor v2**, and **Klipy v3** APIs side-by-side.

## Setup

Add the AAR to your project, then include these dependencies (versions managed via [libs.versions.toml](gradle/libs.versions.toml)):

```groovy
implementation 'com.squareup.retrofit2:converter-gson:3.0.0'
implementation 'com.github.bumptech.glide:glide:5.0.7'
implementation 'androidx.annotation:annotation-jvm:1.10.0'
implementation 'androidx.recyclerview:recyclerview:1.4.0'
compileOnly 'com.google.android.gms:play-services-ads-identifier:18.1.0'
```

If your app targets Google Play, declare the Advertising ID permission in your manifest:

```xml
<uses-permission android:name="com.google.android.gms.permission.AD_ID" />
```

## Package Structure

```
com.tenor.android.core/              ← Tenor v1/v2 + shared infrastructure
├── network/
│   ├── ApiService.java              shared HTTP layer
│   ├── IApiClient / ApiClient.java  Tenor v1
│   └── IApiClientV2 / ApiClientV2.java Tenor v2
├── model/impl/                      Tenor data models (Result, Image, etc.)
├── response/                        Tenor response wrappers
├── constant/
│   └── ApiVersion.java              V1 / V2 / V3 endpoint config

com.tenor.android.core.klipy/        ← Klipy v3 (separate package)
├── network/
│   └── IApiClientV3 / ApiClientV3.java
└── model/                           Klipy data models (KlipyResult, etc.)
```

## Quick Start

### Tenor v1

```java
ApiClient.init(context,
    new ApiService.Builder<>(context, IApiClient.class)
        .apiKey("YOUR_TENOR_API_KEY")
        .build());

IApiClient client = ApiClient.getInstance();
Call<GifsResponse> call = client.search(
    ApiClient.getServiceIds(context), "excited", 10, "", "minimal", "all");
```

### Tenor v2

```java
ApiClientV2.init(context, "YOUR_TENOR_API_KEY");
ApiClientV2.setClientKey("my_app");
ApiClientV2.setContentFilter(ContentFilter.OFF);

IApiClientV2 client = ApiClientV2.getInstance();
Call<GifsResponse> call = client.search(
    ApiClientV2.getServiceIds(context), "excited", 10, "", "gif,tinygif,mp4", "all", false, null);
```

### Klipy (a.k.a. v3)

```java
ApiClientV3.init(context, "YOUR_KLIPY_APP_KEY");
ApiClientV3.setCustomerId("customer-123");

IApiClientV3 client = ApiClientV3.getInstance();
Call<KlipyResponse<KlipySearchData>> call = client.search(
    1, 24, "excited", "customer-123", "en_US", ContentFilter.OFF);
```

## Content Filtering

All API versions support content filtering via `ContentFilter`:

| Value | Rating |
|-------|--------|
| `OFF` | G, PG, PG-13, R (no nudity) |
| `LOW` | G, PG, PG-13 |
| `MEDIUM` | G, PG |
| `HIGH` | G only |

```java
ApiClientV2.setContentFilter(ContentFilter.MEDIUM);
```

## API Version Selection

The `ApiVersion` enum in `ApiService.Builder` controls the endpoint. The default is V1:

```java
new ApiService.Builder<>(context, IApiClientV2.class)
    .apiKey("key")
    .apiVersion(ApiVersion.V2)  // switches to tenor.googleapis.com
    .build();
```

For V3 staging/testing, override the server:

```java
.apiVersion(ApiVersion.V3)
.server("staging-app-key")
```

## Testing

```bash
./gradlew test
```

Tests use MockWebServer for request verification and Gson for model deserialization. Tests are organized by package, mirroring the source layout.

## Documentation

- [Tenor v1 API](https://tenor.com/gifapi/documentation)
- [Tenor v2 API](https://developers.google.com/tenor)
- [Klipy v3 API](https://docs.klipy.com/gifs-api)

## Security

- All APIs enforce HTTPS by default
- `SdkLog` gates diagnostic warnings behind `BuildConfig.DEBUG` — no log leakage in release builds
- AAID retrieval requires `com.google.android.gms.permission.AD_ID`
- Content filtering is strongly recommended for all integrations

## License

Apache 2.0 — see [LICENSE](LICENSE)
