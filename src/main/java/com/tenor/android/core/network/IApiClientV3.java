package com.tenor.android.core.network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tenor.android.core.model.impl.klipy.KlipyCategoriesData;
import com.tenor.android.core.model.impl.klipy.KlipyResponse;
import com.tenor.android.core.model.impl.klipy.KlipySearchData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * The v3 (Klipy) network calls
 */
public interface IApiClientV3 {

    /**
     * Get trending GIFs
     */
    @GET("gifs/trending")
    @NonNull
    Call<KlipyResponse<KlipySearchData>> getTrending(
            @Query("page") int page,
            @Query("per_page") int perPage,
            @Query("customer_id") @NonNull String customerId,
            @Query("locale") @NonNull String locale,
            @Query("content_filter") @NonNull String contentFilter);

    /**
     * Search for GIFs
     */
    @GET("gifs/search")
    @NonNull
    Call<KlipyResponse<KlipySearchData>> search(
            @Query("page") int page,
            @Query("per_page") int perPage,
            @Query("q") @NonNull String query,
            @Query("customer_id") @NonNull String customerId,
            @Query("locale") @NonNull String locale,
            @Query("content_filter") @NonNull String contentFilter);

    /**
     * Get GIF categories
     */
    @GET("gifs/categories")
    @NonNull
    Call<KlipyResponse<KlipyCategoriesData>> getCategories(
            @Query("locale") @NonNull String locale);

    /**
     * Get GIFs by IDs
     */
    @GET("gifs/items")
    @NonNull
    Call<KlipyResponse<KlipySearchData>> getItems(
            @Query("ids") @NonNull String ids,
            @Query("customer_id") @NonNull String customerId,
            @Query("locale") @NonNull String locale);

    /**
     * Register a share event
     */
    @POST("gifs/share-trigger")
    @FormUrlEncoded
    @NonNull
    Call<KlipyResponse<Void>> postShareTrigger(
            @Field("id") long id,
            @Field("q") @Nullable String query,
            @Field("customer_id") @NonNull String customerId,
            @Field("locale") @NonNull String locale);

    /**
     * Get search suggestions for a query
     */
    @GET("search_suggestions")
    @NonNull
    Call<KlipyResponse<KlipySearchData>> getSearchSuggestions(
            @Query("q") @NonNull String query,
            @Query("locale") @NonNull String locale,
            @Query("limit") Integer limit);

    /**
     * Get autocomplete suggestions for a partial query
     */
    @GET("autocomplete")
    @NonNull
    Call<KlipyResponse<KlipySearchData>> getAutocomplete(
            @Query("q") @NonNull String query,
            @Query("locale") @NonNull String locale,
            @Query("limit") Integer limit);

    /**
     * Get trending search terms
     */
    @GET("trending_terms")
    @NonNull
    Call<KlipyResponse<KlipySearchData>> getTrendingTerms(
            @Query("locale") @NonNull String locale,
            @Query("limit") Integer limit);
}
