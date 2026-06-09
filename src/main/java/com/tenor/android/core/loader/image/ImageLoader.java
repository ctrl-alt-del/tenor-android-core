package com.tenor.android.core.loader.image;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tenor.android.core.loader.GlideLoader;
import com.tenor.android.core.loader.GlideTaskParams;
import com.tenor.android.core.util.AbstractWeakReferenceUtils;

import java.lang.ref.WeakReference;

public abstract class ImageLoader extends GlideLoader {

    public static <CTX extends Context, T extends ImageView> void loadImage(@NonNull CTX ctx,
                                                                            @NonNull GlideTaskParams<T> params) {
        loadImage(new WeakReference<>(ctx), params);
    }

    public static <CTX extends Context, T extends ImageView> void loadImage(@NonNull WeakReference<CTX> weakRef,
                                                                            @NonNull GlideTaskParams<T> params) {

        if (!AbstractWeakReferenceUtils.isAlive(weakRef)) {
            return;
        }

        RequestBuilder<Drawable> requestBuilder = Glide.with(weakRef.get()).load(params.getPath())
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        load(applyDimens(requestBuilder, params), params);
    }
}
