package com.tenor.android.core.weakref;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

/**
 * Interface of {@link WeakRefObject}
 */
public interface IWeakRefObject<CTX> {

    @NonNull
    WeakReference<CTX> getWeakRef();

    boolean hasRef();
}
