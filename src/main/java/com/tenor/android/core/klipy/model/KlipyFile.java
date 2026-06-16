package com.tenor.android.core.klipy.model;

import java.io.Serializable;

public class KlipyFile implements Serializable {
    private static final long serialVersionUID = 7380533386579885255L;

    private KlipySizeTier hd;
    private KlipySizeTier md;
    private KlipySizeTier sm;
    private KlipySizeTier xs;

    public KlipySizeTier getHd() { return hd; }
    public KlipySizeTier getMd() { return md; }
    public KlipySizeTier getSm() { return sm; }
    public KlipySizeTier getXs() { return xs; }
}
