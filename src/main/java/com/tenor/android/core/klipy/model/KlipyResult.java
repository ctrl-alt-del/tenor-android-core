package com.tenor.android.core.klipy.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class KlipyResult implements Serializable {
    private static final long serialVersionUID = 6980480839000864880L;

    private long id;
    private String slug;
    private String title;
    private KlipyFile file;
    private List<String> tags;
    private String type;

    @SerializedName("blur_preview")
    private String blurPreview;

    public long getId() { return id; }
    public String getSlug() { return slug != null ? slug : ""; }
    public String getTitle() { return title != null ? title : ""; }
    public KlipyFile getFile() { return file; }
    public List<String> getTags() { return tags != null ? tags : Collections.<String>emptyList(); }
    public String getType() { return type != null ? type : ""; }
    public String getBlurPreview() { return blurPreview != null ? blurPreview : ""; }
}
