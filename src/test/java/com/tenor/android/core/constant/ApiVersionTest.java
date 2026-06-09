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
        // V2 format has no placeholders — it's a fixed URL
        String v2Url = String.format(ApiVersion.V2.getEndpointFormat());
        assertEquals("https://tenor.googleapis.com/v2/", v2Url);

        // Extra args (as passed by ApiService.Builder) are harmlessly ignored
        String v2UrlWithArgs = String.format(ApiVersion.V2.getEndpointFormat(), "https", "g");
        assertEquals("https://tenor.googleapis.com/v2/", v2UrlWithArgs);
    }
}
