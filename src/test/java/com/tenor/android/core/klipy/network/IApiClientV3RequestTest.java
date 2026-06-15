package com.tenor.android.core.klipy.network;

import com.tenor.android.core.constant.ContentFilter;
import com.tenor.android.core.klipy.model.KlipyCategoriesData;
import com.tenor.android.core.klipy.model.KlipyResponse;
import com.tenor.android.core.klipy.model.KlipyResult;
import com.tenor.android.core.klipy.model.KlipySearchData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.*;

public class IApiClientV3RequestTest {

    private static final String LITE_SEARCH_JSON = "{\"result\":true,\"data\":{" +
            "\"data\":[{\"id\":1,\"slug\":\"test\",\"title\":\"TestGif\",\"type\":\"gif\"," +
            "\"file\":{\"md\":{\"gif\":{\"url\":\"http://x.com/g.gif\",\"width\":200,\"height\":200,\"size\":9999}}}," +
            "\"tags\":[],\"blur_preview\":\"\"}]," +
            "\"current_page\":1,\"per_page\":24,\"has_next\":true}}";

    private static final String LITE_CATEGORIES_JSON = "{\"result\":true,\"data\":{" +
            "\"locale\":\"en_US\",\"categories\":[" +
            "{\"category\":\"smile\",\"query\":\"smile\",\"preview_url\":\"http://x.com/p.gif\"}]}}";

    private MockWebServer mockWebServer;
    private IApiClientV3 client;

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/api/v1/test-app-key/"))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        client = retrofit.create(IApiClientV3.class);
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    public void testGetTrending_requestParams() throws Exception {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(LITE_SEARCH_JSON));

        Response<KlipyResponse<KlipySearchData>> response = client.getTrending(
                1, 24, "cust-1", "en_US", ContentFilter.OFF).execute();

        assertTrue(response.isSuccessful());
        assertNotNull(response.body());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/v1/test-app-key/gifs/trending", request.getRequestUrl().encodedPath());
        assertEquals("1", request.getRequestUrl().queryParameter("page"));
        assertEquals("24", request.getRequestUrl().queryParameter("per_page"));
        assertEquals("cust-1", request.getRequestUrl().queryParameter("customer_id"));
        assertEquals("en_US", request.getRequestUrl().queryParameter("locale"));
        assertEquals(ContentFilter.OFF, request.getRequestUrl().queryParameter("content_filter"));
    }

    @Test
    public void testGetTrending_responseDeserialization() throws Exception {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(LITE_SEARCH_JSON));

        Response<KlipyResponse<KlipySearchData>> response = client.getTrending(
                1, 24, "cust-1", "en_US", ContentFilter.OFF).execute();

        KlipyResponse<KlipySearchData> body = response.body();
        assertTrue(body.isResult());
        assertNotNull(body.getData());
        assertEquals(1, body.getData().getResults().size());
        assertEquals("TestGif", body.getData().getResults().get(0).getTitle());
    }

    @Test
    public void testSearch_requestParams() throws Exception {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(LITE_SEARCH_JSON));

        Response<KlipyResponse<KlipySearchData>> response = client.search(
                2, 10, "excited", "cust-1", "en_US", ContentFilter.LOW).execute();

        assertTrue(response.isSuccessful());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/v1/test-app-key/gifs/search", request.getRequestUrl().encodedPath());
        assertEquals("2", request.getRequestUrl().queryParameter("page"));
        assertEquals("10", request.getRequestUrl().queryParameter("per_page"));
        assertEquals("excited", request.getRequestUrl().queryParameter("q"));
        assertEquals("cust-1", request.getRequestUrl().queryParameter("customer_id"));
        assertEquals("en_US", request.getRequestUrl().queryParameter("locale"));
        assertEquals(ContentFilter.LOW, request.getRequestUrl().queryParameter("content_filter"));
    }

    @Test
    public void testGetCategories_requestParams() throws Exception {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(LITE_CATEGORIES_JSON));

        Response<KlipyResponse<KlipyCategoriesData>> response = client.getCategories("en_US").execute();

        assertTrue(response.isSuccessful());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/v1/test-app-key/gifs/categories", request.getRequestUrl().encodedPath());
        assertEquals("en_US", request.getRequestUrl().queryParameter("locale"));
    }

    @Test
    public void testGetCategories_responseDeserialization() throws Exception {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(LITE_CATEGORIES_JSON));

        Response<KlipyResponse<KlipyCategoriesData>> response = client.getCategories("en_US").execute();

        KlipyResponse<KlipyCategoriesData> body = response.body();
        assertTrue(body.isResult());
        assertEquals("en_US", body.getData().getLocale());
        assertEquals(1, body.getData().getCategories().size());
        assertEquals("smile", body.getData().getCategories().get(0).getCategory());
    }

    @Test
    public void testGetItems_requestParams() throws Exception {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(LITE_SEARCH_JSON));

        Response<KlipyResponse<KlipySearchData>> response = client.getItems("123,456", "cust-1", "en_US").execute();

        assertTrue(response.isSuccessful());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/v1/test-app-key/gifs/items", request.getRequestUrl().encodedPath());
        assertEquals("123,456", request.getRequestUrl().queryParameter("ids"));
        assertEquals("cust-1", request.getRequestUrl().queryParameter("customer_id"));
        assertEquals("en_US", request.getRequestUrl().queryParameter("locale"));
    }

    @Test
    public void testPostShareTrigger_requestParams() throws Exception {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody("{\"result\":true,\"data\":null}"));

        Response<KlipyResponse<Void>> response = client.postShareTrigger(123L, "excited", "cust-1", "en_US").execute();

        assertTrue(response.isSuccessful());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/v1/test-app-key/gifs/share-trigger", request.getRequestUrl().encodedPath());
        assertEquals("POST", request.getMethod());
        String body = request.getBody().readUtf8();
        assertTrue(body.contains("id=123"));
        assertTrue(body.contains("q=excited"));
        assertTrue(body.contains("customer_id=cust-1"));
    }

    @Test
    public void testPostShareTrigger_responseDeserialization() throws Exception {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody("{\"result\":true,\"data\":null}"));

        Response<KlipyResponse<Void>> response = client.postShareTrigger(123L, "excited", "cust-1", "en_US").execute();

        assertTrue(response.isSuccessful());
        KlipyResponse<Void> body = response.body();
        assertNotNull(body);
        assertTrue(body.isResult());
        assertNull(body.getData());
        // No crash — null data is valid for share-trigger (fire-and-forget)
    }

    @Test
    public void testGetSearchSuggestions_requestParams() throws Exception {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(LITE_SEARCH_JSON));

        Response<KlipyResponse<KlipySearchData>> response = client.getSearchSuggestions(
                "smile", "en_US", 5).execute();

        assertTrue(response.isSuccessful());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/v1/test-app-key/search_suggestions", request.getRequestUrl().encodedPath());
        assertEquals("smile", request.getRequestUrl().queryParameter("q"));
        assertEquals("en_US", request.getRequestUrl().queryParameter("locale"));
        assertEquals("5", request.getRequestUrl().queryParameter("limit"));
    }

    @Test
    public void testGetAutocomplete_requestParams() throws Exception {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(LITE_SEARCH_JSON));

        Response<KlipyResponse<KlipySearchData>> response = client.getAutocomplete(
                "exc", "en_US", 5).execute();

        assertTrue(response.isSuccessful());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/v1/test-app-key/autocomplete", request.getRequestUrl().encodedPath());
        assertEquals("exc", request.getRequestUrl().queryParameter("q"));
        assertEquals("en_US", request.getRequestUrl().queryParameter("locale"));
        assertEquals("5", request.getRequestUrl().queryParameter("limit"));
    }

    @Test
    public void testGetTrendingTerms_requestParams() throws Exception {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(LITE_SEARCH_JSON));

        Response<KlipyResponse<KlipySearchData>> response = client.getTrendingTerms(
                "en_US", 10).execute();

        assertTrue(response.isSuccessful());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/v1/test-app-key/trending_terms", request.getRequestUrl().encodedPath());
        assertEquals("en_US", request.getRequestUrl().queryParameter("locale"));
        assertEquals("10", request.getRequestUrl().queryParameter("limit"));
    }
}
