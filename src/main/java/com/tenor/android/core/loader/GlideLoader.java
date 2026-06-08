package com.tenor.android.core.loader;

import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tenor.android.core.model.impl.Media;

public class GlideLoader {

    public static RequestBuilder<Drawable> applyDimens(@NonNull RequestBuilder<Drawable> requestBuilder,
                                                        @NonNull GlideTaskParams payload) {
        final Media media = payload.getMedia();
        if (media != null) {
            requestBuilder.override(media.getWidth(), media.getHeight());
        }
        return requestBuilder;
    }

    public static <T extends ImageView> void load(@NonNull final RequestBuilder<Drawable> requestBuilder,
                                                  @NonNull final GlideTaskParams<T> payload) {

        if (payload.isThumbnail()) {
            requestBuilder.thumbnail(payload.getThumbnailMultiplier());
        }

        requestBuilder.placeholder(payload.getPlaceholder())
                .into(new CustomViewTarget<ImageView, Drawable>(payload.getTarget()) {
                    @Override
                    protected void onResourceCleared(@Nullable Drawable placeholder) {
                        payload.getTarget().setImageDrawable(placeholder);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        if (payload.getCurrentRetry() < payload.getMaxRetry()) {
                            payload.incrementCurrentRetry();
                            load(requestBuilder, payload);
                        } else {
                            payload.getListener().failure(payload.getTarget(), errorDrawable);
                        }
                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        payload.getListener().success(payload.getTarget(), resource);
                    }
                });
    }
}
