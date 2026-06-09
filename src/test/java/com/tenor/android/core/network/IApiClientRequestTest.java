package com.tenor.android.core.network;

import androidx.collection.ArrayMap;

import com.tenor.android.core.constant.AspectRatioRange;
import com.tenor.android.core.constant.ContentFilter;
import com.tenor.android.core.constant.MediaFilter;
import com.tenor.android.core.model.impl.Result;
import com.tenor.android.core.response.impl.GifsResponse;
import com.tenor.android.core.response.impl.TagsResponse;

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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class IApiClientRequestTest {

    private MockWebServer mockWebServer;
    private IApiClient client;
    private Map<String, String> serviceIds;

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/v1/"))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        client = retrofit.create(IApiClient.class);

        serviceIds = new ArrayMap<>();
        serviceIds.put("key", "test-api-key");
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
                serviceIds, "excited", 8, "0", MediaFilter.MINIMAL, AspectRatioRange.ALL).execute();

        assertTrue(response.isSuccessful());
        assertNotNull(response.body());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/v1/search", request.getRequestUrl().encodedPath());
        assertEquals("excited", request.getRequestUrl().queryParameter("q"));
        assertEquals("8", request.getRequestUrl().queryParameter("limit"));
        assertEquals("0", request.getRequestUrl().queryParameter("pos"));
        assertEquals(MediaFilter.MINIMAL, request.getRequestUrl().queryParameter("media_filter"));
        assertEquals(AspectRatioRange.ALL, request.getRequestUrl().queryParameter("ar_range"));
        assertEquals("test-api-key", request.getRequestUrl().queryParameter("key"));
        assertEquals("en_US", request.getRequestUrl().queryParameter("locale"));
        assertEquals(ContentFilter.OFF, request.getRequestUrl().queryParameter("contentfilter"));
    }

    @Test
    public void testSearch_v1ResponseDeserialization() throws Exception {
        String v1Json = "{\"next\":\"10\",\"results\":[{" +
                "\"id\":\"8776030\"," +
                "\"title\":\"v1 GIF\"," +
                "\"media\":[" +
                "{\"gif\":{\"url\":\"https://media.tenor.com/g.gif\",\"dims\":[498,280],\"size\":150000,\"preview\":\"https://media.tenor.com/p.png\"}}" +
                "]}]}";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(v1Json));

        Response<GifsResponse> response = client.search(
                serviceIds, "test", 1, "", MediaFilter.MINIMAL, AspectRatioRange.ALL).execute();

        assertTrue(response.isSuccessful());
        GifsResponse body = response.body();
        assertNotNull(body);
        assertEquals("10", body.getNext());
        assertEquals(1, body.getResults().size());

        Result result = body.getResults().get(0);
        assertEquals("8776030", result.getId());
        assertEquals("v1 GIF", result.getTitle());
        assertFalse(result.isV2());
        assertEquals(1, result.getMedias().size());
    }

    @Test
    public void testGetTrending_requestParams() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"next\":\"\",\"results\":[]}"));

        Response<com.tenor.android.core.response.impl.TrendingGifResponse> response = client.getTrending(
                serviceIds, 10, "", MediaFilter.MINIMAL, AspectRatioRange.ALL).execute();

        assertTrue(response.isSuccessful());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/v1/trending", request.getRequestUrl().encodedPath());
        assertEquals("10", request.getRequestUrl().queryParameter("limit"));
        assertEquals("", request.getRequestUrl().queryParameter("pos"));
        assertEquals(MediaFilter.MINIMAL, request.getRequestUrl().queryParameter("media_filter"));
        assertEquals(AspectRatioRange.ALL, request.getRequestUrl().queryParameter("ar_range"));
    }

    @Test
    public void testGetGifs_requestParams() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"next\":\"\",\"results\":[]}"));

        Response<GifsResponse> response = client.getGifs(
                serviceIds, "123,456", MediaFilter.MINIMAL, AspectRatioRange.ALL).execute();

        assertTrue(response.isSuccessful());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/v1/gifs", request.getRequestUrl().encodedPath());
        assertEquals("123,456", request.getRequestUrl().queryParameter("ids"));
        assertEquals(MediaFilter.MINIMAL, request.getRequestUrl().queryParameter("media_filter"));
    }

    @Test
    public void testGetTags_requestParams() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"tags\":[]}"));

        Response<TagsResponse> response = client.getTags(serviceIds, "featured", "+08:00").execute();

        assertTrue(response.isSuccessful());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/v1/tags", request.getRequestUrl().encodedPath());
        assertEquals("featured", request.getRequestUrl().queryParameter("type"));
        assertEquals("+08:00", request.getRequestUrl().queryParameter("timezone"));
    }

    @Test
    public void testGetSearchSuggestions_requestParams() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"results\":[]}"));

        Response<com.tenor.android.core.response.impl.SearchSuggestionResponse> response = client
                .getSearchSuggestions(serviceIds, "smile", 5).execute();

        assertTrue(response.isSuccessful());

        RecordedRequest request = mockWebServer.takeRequest();
        assertTrue(request.getRequestUrl().encodedPath().contains("search_suggestions"));
        assertEquals("smile", request.getRequestUrl().queryParameter("tag"));
        assertEquals("5", request.getRequestUrl().queryParameter("limit"));
    }

    @Test
    public void testRegisterShare_idType() throws Exception {
        // Commit 3: verify id is sent as a string
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{}"));

        Response<Void> response = client.registerShare(serviceIds, "8776030", "excited").execute();

        assertTrue(response.isSuccessful());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/v1/registershare", request.getRequestUrl().encodedPath());
        assertEquals("8776030", request.getRequestUrl().queryParameter("id"));
        assertEquals("excited", request.getRequestUrl().queryParameter("q"));
    }

    @Test
    public void testRegisterShare_noQuery() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{}"));

        Response<Void> response = client.registerShare(serviceIds, "12345", "").execute();

        assertTrue(response.isSuccessful());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/v1/registershare", request.getRequestUrl().encodedPath());
        assertEquals("12345", request.getRequestUrl().queryParameter("id"));
        assertEquals("", request.getRequestUrl().queryParameter("q"));
    }

    @Test
    public void testGetAnonId_requestParams() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"anon_id\":\"abc123\"}"));

        Response<com.tenor.android.core.response.impl.AnonIdResponse> response = client
                .getAnonId("test-key", "en_US").execute();

        assertTrue(response.isSuccessful());

        RecordedRequest request = mockWebServer.takeRequest();
        assertTrue(request.getRequestUrl().encodedPath().contains("anonid"));
        assertEquals("test-key", request.getRequestUrl().queryParameter("key"));
        assertEquals("en_US", request.getRequestUrl().queryParameter("locale"));
    }
}
