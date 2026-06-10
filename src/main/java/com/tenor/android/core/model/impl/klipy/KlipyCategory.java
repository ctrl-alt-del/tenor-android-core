package com.tenor.android.core.model.impl.klipy;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class KlipyCategory implements Serializable {
    private static final long serialVersionUID = -7374020362633185424L;

    private String category;
    private String query;

    @SerializedName("preview_url")
    private String previewUrl;

    public String getCategory() { return category != null ? category : ""; }
    public String getQuery() { return query != null ? query : ""; }
    public String getPreviewUrl() { return previewUrl != null ? previewUrl : ""; }
}
