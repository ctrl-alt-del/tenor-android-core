# Consumer ProGuard/R8 rules for tenor-android-core

# Keep all model classes — fields are deserialized via Gson reflection
-keep class com.tenor.android.core.model.impl.** { *; }
-keep class com.tenor.android.core.response.impl.** { *; }
-keep class com.tenor.android.core.klipy.model.** { *; }

# Keep Retrofit interfaces
-keep,allowobfuscation interface com.tenor.android.core.network.IApiClient
-keep,allowobfuscation interface com.tenor.android.core.network.IApiClientV2
-keep,allowobfuscation interface com.tenor.android.core.klipy.network.IApiClientV3
