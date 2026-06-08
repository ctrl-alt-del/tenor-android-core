package com.tenor.android.core.network;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Callback for asynchronous anon id API request is done
 */
public interface IAnonIdListener {

    /**
     * @param anonId the keyboard id
     */
    void onReceiveAnonIdSucceeded(@NonNull String anonId);

    /**
     * @param throwable the {@link Throwable}
     */
    void onReceiveAnonIdFailed(@Nullable Throwable throwable);
}
