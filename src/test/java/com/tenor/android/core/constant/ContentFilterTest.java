package com.tenor.android.core.constant;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ContentFilterTest {

    @Test
    public void testContentFilterValues() {
        assertEquals("off", ContentFilter.OFF);
        assertEquals("low", ContentFilter.LOW);
        assertEquals("medium", ContentFilter.MEDIUM);
        assertEquals("high", ContentFilter.HIGH);
    }
}
