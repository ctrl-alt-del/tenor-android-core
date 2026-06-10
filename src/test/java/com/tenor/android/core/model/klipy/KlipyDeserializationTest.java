package com.tenor.android.core.model.klipy;

import com.google.gson.Gson;
import com.tenor.android.core.model.impl.klipy.*;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class KlipyDeserializationTest {

    private static final Gson GSON = new Gson();

    @Test
    public void testSearchResponse_deserialization() {
        String json = "{\"result\":true,\"data\":{" +
                "\"data\":[{" +
                "\"id\":8041071659142944," +
                "\"slug\":\"hello-hi-662\"," +
                "\"title\":\"Hello\"," +
                "\"type\":\"gif\"," +
                "\"tags\":[]," +
                "\"blur_preview\":\"data:image/jpeg;base64,abc\"," +
                "\"file\":{" +
                "\"hd\":{" +
                "\"gif\":{\"url\":\"https://static.klipy.com/gif.gif\",\"width\":498,\"height\":498,\"size\":4001918}," +
                "\"webp\":{\"url\":\"https://static.klipy.com/webp.webp\",\"width\":498,\"height\":498,\"size\":285228}" +
                "}," +
                "\"xs\":{" +
                "\"gif\":{\"url\":\"https://static.klipy.com/xs.gif\",\"width\":90,\"height\":90,\"size\":71468}" +
                "}" +
                "}" +
                "}]," +
                "\"current_page\":1," +
                "\"per_page\":24," +
                "\"has_next\":true" +
                "}}";

        KlipyResponse<KlipySearchData> response = GSON.fromJson(json,
                new com.google.gson.reflect.TypeToken<KlipyResponse<KlipySearchData>>() {}.getType());

        assertNotNull(response);
        assertTrue(response.isResult());

        KlipySearchData data = response.getData();
        assertNotNull(data);
        assertEquals(1, data.getCurrentPage());
        assertEquals(24, data.getPerPage());
        assertTrue(data.isHasNext());

        List<KlipyResult> results = data.getResults();
        assertEquals(1, results.size());

        KlipyResult result = results.get(0);
        assertEquals(8041071659142944L, result.getId());
        assertEquals("hello-hi-662", result.getSlug());
        assertEquals("Hello", result.getTitle());
        assertEquals("gif", result.getType());
        assertEquals("data:image/jpeg;base64,abc", result.getBlurPreview());

        assertNotNull(result.getFile());
        assertNotNull(result.getFile().getHd());
        assertNotNull(result.getFile().getHd().getGif());
        assertEquals("https://static.klipy.com/gif.gif", result.getFile().getHd().getGif().getUrl());
        assertEquals(498, result.getFile().getHd().getGif().getWidth());
        assertEquals(498, result.getFile().getHd().getGif().getHeight());
        assertEquals(4001918, result.getFile().getHd().getGif().getSize());

        assertNotNull(result.getFile().getHd().getWebp());
        assertEquals("https://static.klipy.com/webp.webp", result.getFile().getHd().getWebp().getUrl());
        assertEquals(285228, result.getFile().getHd().getWebp().getSize());

        assertNotNull(result.getFile().getXs());
        assertNotNull(result.getFile().getXs().getGif());
        assertEquals(90, result.getFile().getXs().getGif().getWidth());
        assertEquals(71468, result.getFile().getXs().getGif().getSize());
    }

    @Test
    public void testCategoriesResponse_deserialization() {
        String json = "{\"result\":true,\"data\":{" +
                "\"locale\":\"en_US\"," +
                "\"categories\":[" +
                "{\"category\":\"smile\",\"query\":\"smile\",\"preview_url\":\"https://static.klipy.com/smile.gif\"}," +
                "{\"category\":\"aww\",\"query\":\"aww\",\"preview_url\":\"https://static.klipy.com/aww.gif\"}" +
                "]}}";

        KlipyResponse<KlipyCategoriesData> response = GSON.fromJson(json,
                new com.google.gson.reflect.TypeToken<KlipyResponse<KlipyCategoriesData>>() {}.getType());

        assertNotNull(response);
        assertTrue(response.isResult());

        KlipyCategoriesData data = response.getData();
        assertNotNull(data);
        assertEquals("en_US", data.getLocale());

        List<KlipyCategory> categories = data.getCategories();
        assertEquals(2, categories.size());

        assertEquals("smile", categories.get(0).getCategory());
        assertEquals("smile", categories.get(0).getQuery());
        assertEquals("https://static.klipy.com/smile.gif", categories.get(0).getPreviewUrl());

        assertEquals("aww", categories.get(1).getCategory());
        assertEquals("aww", categories.get(1).getQuery());
        assertEquals("https://static.klipy.com/aww.gif", categories.get(1).getPreviewUrl());
    }

    @Test
    public void testFullTrendingResponse_fromDocSample() {
        // Real doc sample: id=8041071659142944 with all 4 size tiers and 5 format types each
        String json = "{\"result\":true,\"data\":{" +
                "\"data\":[{" +
                "\"id\":8041071659142944," +
                "\"slug\":\"hello-hi-662\"," +
                "\"title\":\"Hello\"," +
                "\"type\":\"gif\"," +
                "\"tags\":[]," +
                "\"blur_preview\":\"data:image/jpeg;base64,/9j/\"," +
                "\"file\":{" +
                "\"hd\":{" +
                "\"gif\":{\"url\":\"https://static.klipy.com/ii/935d7ab9/hd.gif\",\"width\":498,\"height\":498,\"size\":4001918}," +
                "\"webp\":{\"url\":\"https://static.klipy.com/ii/935d7ab9/hd.webp\",\"width\":498,\"height\":498,\"size\":285228}," +
                "\"jpg\":{\"url\":\"https://static.klipy.com/ii/935d7ab9/hd.jpg\",\"width\":498,\"height\":498,\"size\":19255}," +
                "\"mp4\":{\"url\":\"https://static.klipy.com/ii/935d7ab9/hd.mp4\",\"width\":498,\"height\":498,\"size\":119294}," +
                "\"webm\":{\"url\":\"https://static.klipy.com/ii/935d7ab9/hd.webm\",\"width\":498,\"height\":498,\"size\":79936}" +
                "}," +
                "\"md\":{" +
                "\"gif\":{\"url\":\"https://static.klipy.com/ii/935d7ab9/md.gif\",\"width\":498,\"height\":498,\"size\":3721260}," +
                "\"webp\":{\"url\":\"https://static.klipy.com/ii/935d7ab9/md.webp\",\"width\":498,\"height\":498,\"size\":643490}," +
                "\"jpg\":{\"url\":\"https://static.klipy.com/ii/935d7ab9/md.jpg\",\"width\":498,\"height\":498,\"size\":20086}," +
                "\"mp4\":{\"url\":\"https://static.klipy.com/ii/935d7ab9/md.mp4\",\"width\":498,\"height\":498,\"size\":119294}," +
                "\"webm\":{\"url\":\"https://static.klipy.com/ii/935d7ab9/md.webm\",\"width\":498,\"height\":498,\"size\":79936}" +
                "}," +
                "\"sm\":{" +
                "\"gif\":{\"url\":\"https://static.klipy.com/ii/935d7ab9/sm.gif\",\"width\":220,\"height\":220,\"size\":314884}," +
                "\"webp\":{\"url\":\"https://static.klipy.com/ii/935d7ab9/sm.webp\",\"width\":220,\"height\":220,\"size\":80118}," +
                "\"jpg\":{\"url\":\"https://static.klipy.com/ii/935d7ab9/sm.jpg\",\"width\":220,\"height\":220,\"size\":8560}," +
                "\"mp4\":{\"url\":\"https://static.klipy.com/ii/935d7ab9/sm.mp4\",\"width\":320,\"height\":320,\"size\":49565}," +
                "\"webm\":{\"url\":\"https://static.klipy.com/ii/935d7ab9/sm.webm\",\"width\":320,\"height\":320,\"size\":48827}" +
                "}," +
                "\"xs\":{" +
                "\"gif\":{\"url\":\"https://static.klipy.com/ii/935d7ab9/xs.gif\",\"width\":90,\"height\":90,\"size\":71468}," +
                "\"webp\":{\"url\":\"https://static.klipy.com/ii/935d7ab9/xs.webp\",\"width\":90,\"height\":90,\"size\":25340}," +
                "\"jpg\":{\"url\":\"https://static.klipy.com/ii/935d7ab9/xs.jpg\",\"width\":90,\"height\":90,\"size\":2949}," +
                "\"mp4\":{\"url\":\"https://static.klipy.com/ii/935d7ab9/xs.mp4\",\"width\":150,\"height\":150,\"size\":20257}," +
                "\"webm\":{\"url\":\"https://static.klipy.com/ii/935d7ab9/xs.webm\",\"width\":150,\"height\":150,\"size\":38333}" +
                "}" +
                "}" +
                "}]," +
                "\"current_page\":1," +
                "\"per_page\":24," +
                "\"has_next\":true" +
                "}}";

        KlipyResponse<KlipySearchData> response = GSON.fromJson(json,
                new com.google.gson.reflect.TypeToken<KlipyResponse<KlipySearchData>>() {}.getType());

        assertTrue(response.isResult());
        KlipyResult result = response.getData().getResults().get(0);
        assertEquals(8041071659142944L, result.getId());
        assertEquals("hello-hi-662", result.getSlug());
        assertEquals("Hello", result.getTitle());
        assertEquals("gif", result.getType());
        assertTrue(result.getBlurPreview().startsWith("data:image/jpeg;base64"));

        KlipyFile file = result.getFile();
        assertNotNull(file);

        // hd tier: all 5 formats present with doc-accurate sizes
        assertNotNull(file.getHd());
        assertEquals(4001918, file.getHd().getGif().getSize());
        assertEquals(285228, file.getHd().getWebp().getSize());
        assertEquals(19255, file.getHd().getJpg().getSize());
        assertEquals(119294, file.getHd().getMp4().getSize());
        assertEquals(79936, file.getHd().getWebm().getSize());

        // md tier: has gif at 3,721,260 bytes (largest of all tiers — less compressed)
        assertNotNull(file.getMd());
        assertEquals(3721260, file.getMd().getGif().getSize());
        assertEquals(643490, file.getMd().getWebp().getSize());

        // sm tier: smaller dimensions (220×220 for images, 320×320 for video)
        assertNotNull(file.getSm());
        assertEquals(220, file.getSm().getGif().getWidth());
        assertEquals(220, file.getSm().getGif().getHeight());
        assertEquals(320, file.getSm().getMp4().getWidth());
        assertEquals(320, file.getSm().getMp4().getHeight());

        // xs tier: smallest (90×90 images, 150×150 video)
        assertNotNull(file.getXs());
        assertEquals(90, file.getXs().getGif().getWidth());
        assertEquals(90, file.getXs().getGif().getHeight());
        assertEquals(71468, file.getXs().getGif().getSize());
        assertEquals(150, file.getXs().getMp4().getWidth());
        assertEquals(150, file.getXs().getMp4().getHeight());
        assertEquals(38333, file.getXs().getWebm().getSize());
    }

    @Test
    public void testEmptyResults_noCrash() {
        String json = "{\"result\":true,\"data\":{" +
                "\"data\":[]," +
                "\"current_page\":1," +
                "\"per_page\":24," +
                "\"has_next\":false" +
                "}}";

        KlipyResponse<KlipySearchData> response = GSON.fromJson(json,
                new com.google.gson.reflect.TypeToken<KlipyResponse<KlipySearchData>>() {}.getType());

        assertTrue(response.isResult());
        assertNotNull(response.getData());
        assertEquals(0, response.getData().getResults().size());
        assertFalse(response.getData().isHasNext());
    }

    @Test
    public void testNullData_noCrash() {
        String json = "{\"result\":false,\"data\":null}";

        KlipyResponse<KlipySearchData> response = GSON.fromJson(json,
                new com.google.gson.reflect.TypeToken<KlipyResponse<KlipySearchData>>() {}.getType());

        assertFalse(response.isResult());
        assertNull(response.getData());
    }
}
