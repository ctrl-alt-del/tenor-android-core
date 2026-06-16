package com.tenor.android.core.klipy.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class KlipyCategoriesData implements Serializable {
    private static final long serialVersionUID = 7839680730002780927L;

    private String locale;
    private List<KlipyCategory> categories;

    public String getLocale() { return locale != null ? locale : ""; }

    public List<KlipyCategory> getCategories() {
        return categories != null ? categories : Collections.<KlipyCategory>emptyList();
    }
}
