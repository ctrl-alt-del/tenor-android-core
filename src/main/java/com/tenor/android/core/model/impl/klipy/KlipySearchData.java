package com.tenor.android.core.model.impl.klipy;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class KlipySearchData implements Serializable {
    private static final long serialVersionUID = -4563276383974428097L;

    @SerializedName("data")
    private List<KlipyResult> results;

    @SerializedName("current_page")
    private int currentPage;

    @SerializedName("per_page")
    private int perPage;

    @SerializedName("has_next")
    private boolean hasNext;

    public List<KlipyResult> getResults() {
        return results != null ? results : Collections.<KlipyResult>emptyList();
    }

    public int getCurrentPage() { return currentPage; }
    public int getPerPage() { return perPage; }
    public boolean isHasNext() { return hasNext; }
}
