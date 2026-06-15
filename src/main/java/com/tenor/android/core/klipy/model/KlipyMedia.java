package com.tenor.android.core.klipy.model;

import java.io.Serializable;

public class KlipyMedia implements Serializable {
    private static final long serialVersionUID = 5916901270889142559L;

    private String url;
    private int width;
    private int height;
    private int size;

    public String getUrl() {
        return url != null ? url : "";
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getSize() {
        return size;
    }
}
