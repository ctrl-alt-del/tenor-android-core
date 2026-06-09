package com.tenor.android.core.network;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;

import android.text.TextUtils;

import com.tenor.android.core.constant.ApiVersion;
import com.tenor.android.core.constant.ContentFilter;
import com.tenor.android.core.constant.StringConstant;
import com.tenor.android.core.util.AbstractLocaleUtils;

import java.util.Map;

import retrofit2.Call;

/**
 * V2 API Client for retrieving content via the v2 Tenor API
 */
public class ApiClientV2 {

    private static volatile IApiService<IApiClientV2> sApiService;
    @ContentFilter.Value
    private static volatile String sContentFilter = ContentFilter.OFF;
    private static volatile String sClientKey = StringConstant.EMPTY;
    private static volatile String sCountry = "US";

    /**
     * Initialize the v2 ApiClient
     *
     * @param apiService the configured {@link IApiService} instance
     */
    public static synchronized void init(@NonNull IApiService<IApiClientV2> apiService) {
        if (sApiService == null) {
            sApiService = apiService;
        }
    }

    /**
     * Convenience initializer with default builder configuration for v2
     *
     * @param context the application context
     * @param apiKey  the Tenor API key
     */
    public static synchronized void init(@NonNull Context context, @NonNull String apiKey) {
        if (sApiService == null) {
            sApiService = new ApiService.Builder<>(context, IApiClientV2.class)
                    .apiKey(apiKey)
                    .apiVersion(ApiVersion.V2)
                    .build();
        }
    }

    @NonNull
    public static String getApiKey() {
        if (sApiService == null) {
            throw new IllegalStateException("ApiClientV2 has not been initialized");
        }
        return sApiService.getApiKey();
    }

    /**
     * Retrieve the v2 API client instance
     *
     * @return the {@link IApiClientV2} instance
     */
    @NonNull
    public static synchronized IApiClientV2 getInstance() {
        if (sApiService == null) {
            throw new IllegalStateException("ApiClientV2 has not been initialized");
        }
        return sApiService.get();
    }

    /**
     * Set the content safety filter level
     *
     * @param filter one of {@link ContentFilter}
     */
    public static void setContentFilter(@ContentFilter.Value String filter) {
        sContentFilter = filter;
    }

    /**
     * Set the client key identifying this integration
     * <p>
     * Use the same client_key value for all API calls within an integration.
     *
     * @param clientKey a string representing this integration
     */
    public static void setClientKey(@NonNull String clientKey) {
        sClientKey = clientKey;
    }

    /**
     * Set the country of origin for requests
     *
     * @param country two-letter ISO 3166-1 country code (default "US")
     */
    public static void setCountry(@NonNull String country) {
        if (!TextUtils.isEmpty(country)) {
            sCountry = country;
        }
    }

    /**
     * Build a map of service-level parameters for v2 API calls
     *
     * @return a {@link Map} with key, client_key, country, locale, and contentfilter
     */
    @NonNull
    public static Map<String, String> getServiceIds(@NonNull Context context) {
        final ArrayMap<String, String> map = new ArrayMap<>(5);

        if (sApiService == null) {
            return new ArrayMap<>(0);
        }
        map.put("key", sApiService.getApiKey());
        map.put("client_key", sClientKey);
        map.put("country", sCountry);
        map.put("locale", AbstractLocaleUtils.getCurrentLocaleName(context));
        map.put("contentfilter", sContentFilter);
        return map;
    }

    /**
     * Register a user's sharing of a GIF or sticker
     *
     * @param context the application context
     * @param id      the GIF id
     * @param query   the search query that led to this GIF, or null for trending/featured
     * @return {@link Call}{@link Void}
     */
    public static Call<Void> registerShare(@NonNull Context context,
                                           @NonNull String id,
                                           @Nullable String query) {
        Call<Void> call = getInstance()
                .registerShare(getServiceIds(context), id, StringConstant.getOrEmpty(query));
        call.enqueue(new VoidCallBack());
        return call;
    }
}
