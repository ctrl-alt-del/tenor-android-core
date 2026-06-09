package com.tenor.android.core.weakref;

import androidx.annotation.NonNull;

/**
 * Interface of {@link WeakRefHandler}
 */
public interface IWeakRefHandler<CTX> {
    @NonNull
    WeakRefHandler<CTX> getHandler();
}