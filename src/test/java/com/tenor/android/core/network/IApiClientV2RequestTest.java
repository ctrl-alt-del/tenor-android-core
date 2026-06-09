package com.tenor.android.core.network;

import androidx.collection.ArrayMap;

import com.tenor.android.core.constant.AspectRatioRange;
import com.tenor.android.core.constant.ContentFilter;
import com.tenor.android.core.model.impl.Result;
import com.tenor.android.core.response.impl.CategoriesResponse;
import com.tenor.android.core.response.impl.GifsResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class IApiClientV2RequestTest {

    private MockWebServer mockWebServer;
    private IApiClientV2 client;
    private Map<String, String> serviceIds;

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/v2/"))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        client = retrofit.create(IApiClientV2.class);

        // v2 service IDs — no anon_id, aaId, screen_density
        serviceIds = new ArrayMap<>();
        serviceIds.put("key", "test-api-key");
        serviceIds.put("client_key", "my_test_app");
        serviceIds.put("country", "US");
        serviceIds.put("locale", "en_US");
        serviceIds.put("contentfilter", ContentFilter.OFF);
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    public void testSearch_requestParams() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"next\":\"\",\"results\":[]}"));

        Response<GifsResponse> response = client.search(
                serviceIds, "excited", 8, "0",
                "gif,tinygif,mp4", AspectRatioRange.ALL, false, null).execute();

        assertTrue(response.isSuccessful());
        assertNotNull(response.body());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/v2/search", request.getRequestUrl().encodedPath());
        assertEquals("excited", request.getRequestUrl().queryParameter("q"));
        assertEquals("8", request.getRequestUrl().queryParameter("limit"));
        assertEquals("0", request.getRequestUrl().queryParameter("pos"));
        assertEquals("gif,tinygif,mp4", request.getRequestUrl().queryParameter("media_filter"));
        assertEquals(AspectRatioRange.ALL, request.getRequestUrl().queryParameter("ar_range"));
        assertEquals("false", request.getRequestUrl().queryParameter("random"));

        // Service IDs passed as query params
        assertEquals("test-api-key", request.getRequestUrl().queryParameter("key"));
        assertEquals("my_test_app", request.getRequestUrl().queryParameter("client_key"));
        assertEquals("US", request.getRequestUrl().queryParameter("country"));
        assertEquals("en_US", request.getRequestUrl().queryParameter("locale"));
        assertEquals(ContentFilter.OFF, request.getRequestUrl().queryParameter("contentfilter"));

        // v2 must NOT have legacy params
        assertNull(request.getRequestUrl().queryParameter("anon_id"));
        assertNull(request.getRequestUrl().queryParameter("aaid"));
        assertNull(request.getRequestUrl().queryParameter("keyboardid"));
        assertNull(request.getRequestUrl().queryParameter("screen_density"));
    }

    @Test
    public void testSearch_v2ResponseDeserialization() throws Exception {
        String v2Json = "{\"next\":\"5\",\"results\":[{" +
                "\"id\":\"9876\"," +
                "\"title\":\"v2 result\"," +
                "\"media_formats\":{" +
                "\"tinygif\":{\"url\":\"https://media.tenor.com/v2t.gif\",\"dims\":[220,124],\"size\":50000,\"preview\":\"https://media.tenor.com/p.png\"}," +
                "\"gif\":{\"url\":\"https://media.tenor.com/v2gif.gif\",\"dims\":[498,280],\"size\":150000,\"preview\":\"https://media.tenor.com/p.png\"}" +
                "}}]}";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(v2Json));

        Response<GifsResponse> response = client.search(
                serviceIds, "test", 1, "", "minimal", AspectRatioRange.ALL, false, null).execute();

        assertTrue(response.isSuccessful());
        GifsResponse body = response.body();
        assertNotNull(body);
        assertEquals("5", body.getNext());
        assertEquals(1, body.getResults().size());

        Result result = body.getResults().get(0);
        assertEquals("9876", result.getId());
        assertEquals("v2 result", result.getTitle());
        assertTrue(result.isV2());

        Map<String, com.tenor.android.core.model.impl.Media> formats = result.getMediaFormats();
        assertEquals(2, formats.size());
        assertTrue(formats.containsKey("gif"));
        assertTrue(formats.containsKey("tinygif"));

        com.tenor.android.core.model.impl.Media gif = formats.get("gif");
        assertEquals("https://media.tenor.com/v2gif.gif", gif.getUrl());
        assertEquals(498, gif.getWidth());
        assertEquals(280, gif.getHeight());
        assertEquals(150000, gif.getSize());

        // v1 paths should be empty for v2 response
        assertTrue(result.getMedias().isEmpty());
    }

    @Test
    public void testGetFeatured_requestParams() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"next\":\"\",\"results\":[]}"));

        Response<GifsResponse> response = client.getFeatured(
                serviceIds, 10, "", "gif,tinygif", AspectRatioRange.ALL, null).execute();

        assertTrue(response.isSuccessful());

        RecordedRequest request = mockWebServer.takeRequest();
        // v2 uses /featured, not /trending
        assertEquals("/v2/featured", request.getRequestUrl().encodedPath());
        assertEquals("10", request.getRequestUrl().queryParameter("limit"));
        assertEquals("", request.getRequestUrl().queryParameter("pos"));
        assertEquals("gif,tinygif", request.getRequestUrl().queryParameter("media_filter"));
        assertEquals(AspectRatioRange.ALL, request.getRequestUrl().queryParameter("ar_range"));
    }

    @Test
    public void testGetCategories_requestParams() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"tags\":[]}"));

        Response<CategoriesResponse> response = client.getCategories(serviceIds, "featured").execute();

        assertTrue(response.isSuccessful());

        RecordedRequest request = mockWebServer.takeRequest();
        // v2 uses /categories, not /tags
        assertEquals("/v2/categories", request.getRequestUrl().encodedPath());
        assertEquals("featured", request.getRequestUrl().queryParameter("type"));
    }

    @Test
    public void testGetPosts_requestParams() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"next\":\"\",\"results\":[]}"));

        Response<GifsResponse> response = client.getPosts(
                serviceIds, "123,456", "gif,tinygif").execute();

        assertTrue(response.isSuccessful());

        RecordedRequest request = mockWebServer.takeRequest();
        // v2 uses /posts, not /gifs
        assertEquals("/v2/posts", request.getRequestUrl().encodedPath());
        assertEquals("123,456", request.getRequestUrl().queryParameter("ids"));
        assertEquals("gif,tinygif", request.getRequestUrl().queryParameter("media_filter"));
        // v2 posts endpoint does NOT have ar_range param
        assertNull(request.getRequestUrl().queryParameter("ar_range"));
    }

    @Test
    public void testRegisterShare_requestParams() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{}"));

        Response<Void> response = client.registerShare(serviceIds, "8776030", "excited").execute();

        assertTrue(response.isSuccessful());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/v2/registershare", request.getRequestUrl().encodedPath());
        assertEquals("8776030", request.getRequestUrl().queryParameter("id"));
        assertEquals("excited", request.getRequestUrl().queryParameter("q"));
        // v2 sends country param on registershare
        assertEquals("US", request.getRequestUrl().queryParameter("country"));
    }
}
