package com.tenor.android.core.constant;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ApiVersionTest {

    @Test
    public void testV1EndpointFormat() {
        String v1Url = String.format(ApiVersion.V1.getEndpointFormat(), "https", "api");
        assertEquals("https://api.tenor.com/v1/", v1Url);

        String v1UrlG = String.format(ApiVersion.V1.getEndpointFormat(), "https", "g");
        assertEquals("https://g.tenor.com/v1/", v1UrlG);

        String v1UrlHttp = String.format(ApiVersion.V1.getEndpointFormat(), "http", "api");
        assertEquals("http://api.tenor.com/v1/", v1UrlHttp);
    }

    @Test
    public void testV2EndpointFormat() {
        // V2 default: protocol=https, server=tenor
        String v2Url = String.format(ApiVersion.V2.getEndpointFormat(), "https", "tenor");
        assertEquals("https://tenor.googleapis.com/v2/", v2Url);

        // protocol override still works for v2
        String v2UrlHttp = String.format(ApiVersion.V2.getEndpointFormat(), "http", "tenor");
        assertEquals("http://tenor.googleapis.com/v2/", v2UrlHttp);

        // staging: change the host prefix via server
        String v2UrlStaging = String.format(ApiVersion.V2.getEndpointFormat(), "https", "staging-tenor");
        assertEquals("https://staging-tenor.googleapis.com/v2/", v2UrlStaging);
    }

    @Test
    public void testDefaultServer() {
        assertEquals("g", ApiVersion.V1.getDefaultServer());
        assertEquals("tenor", ApiVersion.V2.getDefaultServer());
    }
}
