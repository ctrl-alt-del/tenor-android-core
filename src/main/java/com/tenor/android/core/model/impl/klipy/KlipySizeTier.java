package com.tenor.android.core.model.impl.klipy;

import java.io.Serializable;

public class KlipySizeTier implements Serializable {
    private static final long serialVersionUID = -488512270302951792L;

    private KlipyMedia gif;
    private KlipyMedia webp;
    private KlipyMedia jpg;
    private KlipyMedia mp4;
    private KlipyMedia webm;

    public KlipyMedia getGif() { return gif; }
    public KlipyMedia getWebp() { return webp; }
    public KlipyMedia getJpg() { return jpg; }
    public KlipyMedia getMp4() { return mp4; }
    public KlipyMedia getWebm() { return webm; }
}
