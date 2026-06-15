package com.tenor.android.core.constant;

/**
 * API version and endpoint configuration.
 * <p>
 * Each version defines its own endpoint format string and default server
 * name. The format string is a {@link String#format(String, Object...)}
 * pattern where {@code %1$s} is the protocol (http/https) and
 * {@code %2$s} is the server name (the subdomain for V1/V2, or the
 * app key for V3).
 *
 * <h3>Versions</h3>
 * <ul>
 *   <li><b>V1</b> — Tenor v1 API ({@code g.tenor.com/v1/})</li>
 *   <li><b>V2</b> — Tenor v2 API ({@code tenor.googleapis.com/v2/})</li>
 *   <li><b>V3</b> — Klipy API ({@code api.klipy.com/api/v1/})</li>
 * </ul>
 *
 * @see com.tenor.android.core.network.ApiClient  V1 client
 * @see com.tenor.android.core.network.ApiClientV2 V2 client
 * @see com.tenor.android.core.klipy.network.ApiClientV3 V3 client
 */
public enum ApiVersion {
    /** Tenor v1 API endpoint ({@code https://g.tenor.com/v1/}) */
    V1("%1$s://%2$s.tenor.com/v1/", "g"),
    /** Tenor v2 API endpoint ({@code https://tenor.googleapis.com/v2/}) */
    V2("%1$s://%2$s.googleapis.com/v2/", "tenor"),
    /** Klipy v3 API endpoint ({@code https://api.klipy.com/api/v1/}) */
    V3("%1$s://api.klipy.com/api/v1/%2$s/", "YOUR_APP_KEY");

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
