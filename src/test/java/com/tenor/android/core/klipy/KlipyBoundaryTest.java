package com.tenor.android.core.klipy;

import com.tenor.android.core.klipy.model.KlipyCategoriesData;
import com.tenor.android.core.klipy.model.KlipyResponse;
import com.tenor.android.core.klipy.model.KlipySearchData;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Verifies the Klipy package boundary: Klipy model classes do not
 * extend or reference any Tenor-specific types.
 */
public class KlipyBoundaryTest {

    @Test
    public void testKlipyModelsIndependentFromTenor() {
        // Klipy models have their own type hierarchy
        KlipyResponse<KlipySearchData> response = new KlipyResponse<>();
        assertNotNull(response);

        // Verify KlipyResponse is NOT an AbstractResponse subclass
        assertTrue(response instanceof java.io.Serializable);
        assertTrue(response.getClass().getSuperclass() == Object.class);

        // KlipySearchData and KlipyCategoriesData are standalone
        KlipySearchData searchData = new KlipySearchData();
        assertTrue(searchData.getClass().getSuperclass() == Object.class);

        KlipyCategoriesData categoriesData = new KlipyCategoriesData();
        assertTrue(categoriesData.getClass().getSuperclass() == Object.class);
    }
}
