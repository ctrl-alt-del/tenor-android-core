package com.tenor.android.core.klipy.network;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;

import android.text.TextUtils;

import com.tenor.android.core.constant.ApiVersion;
import com.tenor.android.core.constant.ContentFilter;
import com.tenor.android.core.constant.StringConstant;
import com.tenor.android.core.klipy.model.KlipyResponse;
import com.tenor.android.core.network.ApiService;
import com.tenor.android.core.network.IApiService;
import com.tenor.android.core.util.AbstractLocaleUtils;
import com.tenor.android.core.util.SdkLog;

import java.util.Map;

import com.tenor.android.core.klipy.model.KlipyResponse;

import retrofit2.Call;

/**
 * V3 (Klipy) API Client
 */
public class ApiClientV3 {

    private static final String TAG = "ApiClientV3";

    private static volatile IApiService<IApiClientV3> sApiService;
    @ContentFilter.Value
    private static volatile String sContentFilter = ContentFilter.OFF;
    private static volatile String sCustomerId = StringConstant.EMPTY;

    /**
     * Initialize the v3 ApiClient
     *
     * @param context the application context
     * @param appKey  the Klipy application key
     */
    public static synchronized void init(@NonNull Context context, @NonNull String appKey) {
        if (sApiService == null) {
            if (TextUtils.isEmpty(appKey)) {
                throw new IllegalArgumentException("appKey must not be empty");
            }
            sApiService = new ApiService.Builder<>(context, IApiClientV3.class)
                    .apiVersion(ApiVersion.V3)
                    .server(appKey)
                    .build();
        }
    }

    /**
     * Initialize with a pre-configured {@link IApiService}
     */
    public static synchronized void init(@NonNull IApiService<IApiClientV3> apiService) {
        if (sApiService == null) {
            sApiService = apiService;
        }
    }

    @NonNull
    public static synchronized IApiClientV3 getInstance() {
        if (sApiService == null) {
            throw new IllegalStateException("ApiClientV3 has not been initialized");
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
     * Set the customer identifier for user-specific content
     *
     * @param customerId a unique customer identifier
     */
    public static void setCustomerId(@NonNull String customerId) {
        sCustomerId = customerId;
    }

    /**
     * Build a map of service-level parameters for v3 API calls
     */
    @NonNull
    public static Map<String, String> getServiceIds(@NonNull Context context) {
        final ArrayMap<String, String> map = new ArrayMap<>(3);
        map.put("locale", AbstractLocaleUtils.getCurrentLocaleName(context));
        map.put("content_filter", sContentFilter);
        if (!TextUtils.isEmpty(sCustomerId)) {
            map.put("customer_id", sCustomerId);
        } else {
            SdkLog.w(TAG, "customer_id is not set; user-specific features will be unavailable");
        }
        return map;
    }

    /**
     * Register a share event
     *
     * @param context the application context
     * @param id      the GIF id
     * @param query   the search query that led to this share
     * @return {@link Call}
     */
    public static Call<KlipyResponse<Void>> registerShare(
            @NonNull Context context, long id, @Nullable String query) {
        Map<String, String> ids = getServiceIds(context);
        Call<KlipyResponse<Void>> call = getInstance()
                .postShareTrigger(id, query,
                        ids.get("customer_id") != null ? ids.get("customer_id") : "",
                        ids.get("locale") != null ? ids.get("locale") : "en_US");
        call.enqueue(new retrofit2.Callback<KlipyResponse<Void>>() {
            @Override
            public void onResponse(retrofit2.Call<KlipyResponse<Void>> c,
                                   retrofit2.Response<KlipyResponse<Void>> r) {
            }

            @Override
            public void onFailure(retrofit2.Call<KlipyResponse<Void>> c,
                                  Throwable t) {
            }
        });
        return call;
    }
}
