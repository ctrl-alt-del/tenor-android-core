package com.tenor.android.core.constant;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MediaFilter {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({MINIMAL, BASIC})
    public @interface Value {
    }

    public static final String MINIMAL = "minimal";
    public static final String BASIC = "basic";
}
