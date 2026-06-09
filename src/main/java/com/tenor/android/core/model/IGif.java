package com.tenor.android.core.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * The Interface of GIF
 */
public interface IGif extends Serializable {
    /**
     * Get the id of this {@link IGif}
     */
    @NonNull
    String getId();

    /**
     * Get the name of this {@link IGif}
     */
    @NonNull
    String getName();
}
