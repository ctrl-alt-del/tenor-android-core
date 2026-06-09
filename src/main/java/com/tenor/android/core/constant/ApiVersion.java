package com.tenor.android.core.constant;

public enum ApiVersion {
    V1("%1$s://%2$s.tenor.com/v1/"),
    V2("https://tenor.googleapis.com/v2/");

    private final String mEndpointFormat;

    ApiVersion(String endpointFormat) {
        mEndpointFormat = endpointFormat;
    }

    public String getEndpointFormat() {
        return mEndpointFormat;
    }
}
