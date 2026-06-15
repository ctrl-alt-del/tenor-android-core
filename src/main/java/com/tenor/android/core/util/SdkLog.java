package com.tenor.android.core.util;

import android.util.Log;

import com.tenor.android.core.BuildConfig;

public final class SdkLog {

    private SdkLog() {}

    public static void w(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, msg);
        }
    }
}
