package com.tenor.android.core.constant;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AspectRatioRange {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({ALL, STANDARD, WIDE})
    public @interface Value {
    }

    public static final String ALL = "all";
    public static final String STANDARD = "standard";
    public static final String WIDE = "wide";
}
