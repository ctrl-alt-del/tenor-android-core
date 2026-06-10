package com.tenor.android.core.model.impl.klipy;

import java.io.Serializable;

public class KlipyResponse<T> implements Serializable {
    private static final long serialVersionUID = 6337603066456168093L;

    private boolean result;
    private T data;

    public boolean isResult() { return result; }
    public T getData() { return data; }
}
