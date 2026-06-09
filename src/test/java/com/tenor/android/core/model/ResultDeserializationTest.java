package com.tenor.android.core.model;

import com.google.gson.Gson;
import com.tenor.android.core.model.impl.Result;
import com.tenor.android.core.response.impl.GifsResponse;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ResultDeserializationTest {

    private static final Gson GSON = new Gson();

    @Test
    public void testV1Result_mediaDeserialization() {
        String json = "{\"next\":\"\",\"results\":[{" +
                "\"id\":\"8776030\"," +
                "\"title\":\"excited GIF\"," +
                "\"hasaudio\":false," +
                "\"hascaption\":true," +
                "\"media\":[" +
                "{\"gif\":{\"url\":\"https://media.tenor.com/gif.gif\",\"dims\":[498,280],\"size\":150000,\"preview\":\"https://media.tenor.com/p.png\"}}," +
                "{\"tinygif\":{\"url\":\"https://media.tenor.com/t.gif\",\"dims\":[220,124],\"size\":50000,\"preview\":\"https://media.tenor.com/p.png\"}}" +
                "]}]}";

        GifsResponse response = GSON.fromJson(json, GifsResponse.class);
        assertNotNull(response);
        List<Result> results = response.getResults();
        assertEquals(1, results.size());

        Result result = results.get(0);
        assertEquals("8776030", result.getId());
        assertEquals("excited GIF", result.getTitle());
        assertFalse(result.isHasAudio());
        assertTrue(result.isHasCaption());

        // v1: should have medias, not mediaFormats
        assertFalse(result.isV2());
        assertEquals(2, result.getMedias().size());
        Map<String, com.tenor.android.core.model.impl.Media> formats = result.getMediaFormats();
        assertTrue(formats.isEmpty());
    }

    @Test
    public void testV2Result_mediaFormatsDeserialization() {
        String json = "{\"next\":\"\",\"results\":[{" +
                "\"id\":\"9876\"," +
                "\"title\":\"v2 GIF\"," +
                "\"hasaudio\":true," +
                "\"hascaption\":false," +
                "\"media_formats\":{" +
                "\"gif\":{\"url\":\"https://media.tenor.com/v2gif.gif\",\"dims\":[300,400],\"size\":8000,\"preview\":\"https://media.tenor.com/p.png\"}," +
                "\"tinygif\":{\"url\":\"https://media.tenor.com/v2t.gif\",\"dims\":[150,200],\"size\":3000,\"preview\":\"https://media.tenor.com/p.png\"}" +
                "}}]}";

        GifsResponse response = GSON.fromJson(json, GifsResponse.class);
        assertNotNull(response);
        List<Result> results = response.getResults();
        assertEquals(1, results.size());

        Result result = results.get(0);
        assertEquals("9876", result.getId());
        assertEquals("v2 GIF", result.getTitle());
        assertTrue(result.isHasAudio());
        assertFalse(result.isHasCaption());

        // v2: should have mediaFormats populated
        assertTrue(result.isV2());
        Map<String, com.tenor.android.core.model.impl.Media> formats = result.getMediaFormats();
        assertEquals(2, formats.size());

        com.tenor.android.core.model.impl.Media gifMedia = formats.get("gif");
        assertNotNull(gifMedia);
        assertEquals("https://media.tenor.com/v2gif.gif", gifMedia.getUrl());
        assertEquals(300, gifMedia.getWidth());
        assertEquals(400, gifMedia.getHeight());
        assertEquals(8000, gifMedia.getSize());

        com.tenor.android.core.model.impl.Media tinyMedia = formats.get("tinygif");
        assertNotNull(tinyMedia);
        assertEquals("https://media.tenor.com/v2t.gif", tinyMedia.getUrl());
        assertEquals(150, tinyMedia.getWidth());
        assertEquals(200, tinyMedia.getHeight());
        assertEquals(3000, tinyMedia.getSize());

        // v1 paths should be empty for v2 response
        assertTrue(result.getMedias().isEmpty());
    }

    @Test
    public void testResult_hascaption() {
        String json = "{\"id\":\"1\",\"hascaption\":true}";
        Result result = GSON.fromJson(json, Result.class);
        assertTrue(result.isHasCaption());

        String json2 = "{\"id\":\"2\",\"hascaption\":false}";
        Result result2 = GSON.fromJson(json2, Result.class);
        assertFalse(result2.isHasCaption());
    }

    @Test
    public void testResult_commonFields() {
        String json = "{" +
                "\"id\":\"12345\"," +
                "\"title\":\"hello world\"," +
                "\"url\":\"https://tenor.com/view/hello-12345\"," +
                "\"itemurl\":\"https://tenor.com/view/hello-12345\"," +
                "\"created\":1550000000.0," +
                "\"shares\":42," +
                "\"tags\":[\"hello\",\"world\"]," +
                "\"aspect_ratio\":1.5," +
                "\"bg_color\":\"#FF0000\"," +
                "\"source_id\":\"src123\"" +
                "}";

        Result result = GSON.fromJson(json, Result.class);
        assertEquals("12345", result.getId());
        assertEquals("hello world", result.getTitle());
        assertEquals("https://tenor.com/view/hello-12345", result.getUrl());
        assertEquals("https://tenor.com/view/hello-12345", result.getItemUrl());
        assertEquals(1550000000.0, result.getCreated(), 0.0);
        assertEquals(42, result.getShares());
        assertEquals(2, result.getTags().size());
        assertTrue(result.getTags().contains("hello"));
        assertTrue(result.getTags().contains("world"));
        assertEquals("src123", result.getSourceId());
    }
}
