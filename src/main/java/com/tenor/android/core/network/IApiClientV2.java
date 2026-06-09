package com.tenor.android.core.network;

import androidx.annotation.NonNull;

import com.tenor.android.core.constant.AspectRatioRange;
import com.tenor.android.core.constant.ContentFilter;
import com.tenor.android.core.constant.MediaFilter;
import com.tenor.android.core.model.impl.Result;
import com.tenor.android.core.response.impl.CategoriesResponse;
import com.tenor.android.core.response.impl.GifsResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * The v2 network calls
 */
public interface IApiClientV2 {

    /**
     * Search for GIFs or stickers based on a query
     *
     * @param serviceIds  a {@link Map} of service-level parameters
     * @param query       search term
     * @param limit       bucket size of each response
     * @param pos         pagination cursor
     * @param mediaFilter comma-separated list of format names (e.g. "gif,tinygif,mp4,tinymp4")
     * @param aspectRatioRange one of {@link AspectRatioRange}
     * @param random      true to randomize result order
     * @param searchFilter filter for non-GIF content ("sticker", "static", "-static")
     * @return {@link Call}{@link GifsResponse}
     */
    @GET("search")
    @NonNull
    Call<GifsResponse> search(@QueryMap Map<String, String> serviceIds,
                              @Query("q") @NonNull String query,
                              @Query("limit") int limit,
                              @Query("pos") @NonNull String pos,
                              @Query("media_filter") String mediaFilter,
                              @Query("ar_range") @AspectRatioRange.Value String aspectRatioRange,
                              @Query("random") boolean random,
                              @Query("searchfilter") String searchFilter);

    /**
     * Retrieve currently featured GIFs or stickers
     *
     * @param serviceIds  a {@link Map} of service-level parameters
     * @param limit       bucket size of each response
     * @param pos         pagination cursor
     * @param mediaFilter comma-separated list of format names
     * @param aspectRatioRange one of {@link AspectRatioRange}
     * @param searchFilter filter for non-GIF content ("sticker", "static", "-static")
     * @return {@link Call}{@link GifsResponse}
     */
    @GET("featured")
    @NonNull
    Call<GifsResponse> getFeatured(@QueryMap Map<String, String> serviceIds,
                                   @Query("limit") Integer limit,
                                   @Query("pos") String pos,
                                   @Query("media_filter") String mediaFilter,
                                   @Query("ar_range") @AspectRatioRange.Value String aspectRatioRange,
                                   @Query("searchfilter") String searchFilter);

    /**
     * Retrieve GIF categories
     *
     * @param serviceIds  a {@link Map} of service-level parameters
     * @param type        category type ("featured" or "trending")
     * @return {@link Call}{@link CategoriesResponse}
     */
    @GET("categories")
    @NonNull
    Call<CategoriesResponse> getCategories(@QueryMap Map<String, String> serviceIds,
                                           @Query("type") String type);

    /**
     * Retrieve GIFs or stickers by their IDs
     *
     * @param serviceIds  a {@link Map} of service-level parameters
     * @param ids         comma-separated list of {@link Result} IDs (max: 50)
     * @param mediaFilter comma-separated list of format names
     * @return {@link Call}{@link GifsResponse}
     */
    @GET("posts")
    @NonNull
    Call<GifsResponse> getPosts(@QueryMap Map<String, String> serviceIds,
                                @Query("ids") String ids,
                                @Query("media_filter") String mediaFilter);

    /**
     * Register a user's sharing of a GIF or sticker
     *
     * @param serviceIds  a {@link Map} of service-level parameters
     * @param id          the GIF id
     * @param query       the search query that led to this share
     * @return {@link Call}{@link Void}
     */
    @GET("registershare")
    @NonNull
    Call<Void> registerShare(@QueryMap Map<String, String> serviceIds,
                             @Query("id") String id,
                             @Query("q") String query);
}
