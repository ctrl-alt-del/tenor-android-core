package com.tenor.android.core.constant;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ContentFilter {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({OFF, LOW, MEDIUM, HIGH})
    public @interface Value {
    }

    public static final String OFF = "off";
    public static final String LOW = "low";
    public static final String MEDIUM = "medium";
    public static final String HIGH = "high";
}
