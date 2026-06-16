package com.tenor.android.core.loader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.request.target.Target;
import com.tenor.android.core.model.impl.Media;

/**
 * Configuration of loading a gif
 */
public class GlideTaskParams<T extends ImageView> extends DrawableLoaderTaskParams<T> {

    private static final long serialVersionUID = -5195385185012871394L;

    @Nullable
    private Media mMedia;

    private float mThumbnailMultiplier = 1f;
    @Nullable
    private String mThumbnailUrl;
    private int mCurrentRetry;
    private int mMaxRetry = 3;

    private int mWidth = Target.SIZE_ORIGINAL;
    private int mHeight = Target.SIZE_ORIGINAL;

    public GlideTaskParams(@NonNull T imageView,
                           @NonNull String path) {
        super(imageView, path);
    }

    @Nullable
    public Media getMedia() {
        return mMedia;
    }

    public GlideTaskParams<T> setMedia(@Nullable final Media media) {
        if (media != null) {
            mMedia = media;
            setWidth(media.getWidth());
            setHeight(media.getHeight());
        }
        return this;
    }

    public int getWidth() {
        return mWidth;
    }

    public GlideTaskParams<T> setWidth(int width) {
        if (width > 0) {
            this.mWidth = width;
        }
        return this;
    }

    public int getHeight() {
        return mHeight;
    }

    public GlideTaskParams<T> setHeight(int height) {
        if (height > 0) {
            this.mHeight = height;
        }
        return this;
    }

    public boolean isThumbnail() {
        return mThumbnailMultiplier >= 0f && mThumbnailMultiplier < 1f;
    }

    public float getThumbnailMultiplier() {
        return mThumbnailMultiplier;
    }

    /**
     * default is 1f
     */
    public GlideTaskParams<T> setThumbnailMultiplier(float multiplier) {
        if (multiplier >= 0f && multiplier <= 1f) {
            mThumbnailMultiplier = multiplier;
        }
        return this;
    }

    @Nullable
    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    /**
     * Set a thumbnail URL to load as a fast placeholder while the
     * full-size GIF decodes. When set, Glide loads this URL first
     * via {@code requestBuilder.thumbnail(thumbRequest)}.
     * <p>
     * Prefer this over {@link #setThumbnailMultiplier(float)} when
     * a separate low-resolution URL is available (e.g. Klipy xs tier).
     */
    public GlideTaskParams<T> setThumbnailUrl(@Nullable String url) {
        if (!TextUtils.isEmpty(url)) {
            mThumbnailUrl = url;
        }
        return this;
    }

    public int getCurrentRetry() {
        return mCurrentRetry;
    }

    /**
     * default is 0
     */
    public GlideTaskParams<T> setCurrentRetry(int retry) {
        if (retry >= 0) {
            mCurrentRetry = retry;
        }
        return this;
    }

    public GlideTaskParams<T> incrementCurrentRetry() {
        mCurrentRetry++;
        return this;
    }

    public int getMaxRetry() {
        return mMaxRetry;
    }

    /**
     * default is 3
     */
    public GlideTaskParams<T> setMaxRetry(int maxRetry) {
        mMaxRetry = maxRetry;
        return this;
    }
}
