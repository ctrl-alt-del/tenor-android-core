package com.tenor.android.core.constant;

public enum ApiVersion {
    V1("%1$s://%2$s.tenor.com/v1/", "g"),
    V2("%1$s://%2$s.googleapis.com/v2/", "tenor");

    private final String mEndpointFormat;
    private final String mDefaultServer;

    ApiVersion(String endpointFormat, String defaultServer) {
        mEndpointFormat = endpointFormat;
        mDefaultServer = defaultServer;
    }

    public String getEndpointFormat() {
        return mEndpointFormat;
    }

    public String getDefaultServer() {
        return mDefaultServer;
    }
}
