package com.bloatit.web.scgiserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.bloatit.common.Log;
import com.bloatit.web.utils.url.Parameters;

public class HttpPost {

    private final Parameters parameters = new Parameters();

    public HttpPost(InputStream is, int length) throws IOException {
        final byte[] postBytes = new byte[length];
        int read = is.read(postBytes);
        if (read == length) {
            Log.server().debug("Post value read correctly.");
        } else {
            Log.server().error("End of strem reading the postBytes. There may be difficulties to generate the page.");
        }
        readBytes(postBytes);
    }

    private void readBytes(byte[] postBytes) {
        String string = new String(postBytes);
        for (final String param : string.split("&")) {
            try {
                final String[] pair = param.split("=");
                if (pair.length >= 2) {
                    final String key = URLDecoder.decode(pair[0], "UTF-8");
                    String value;
                    value = URLDecoder.decode(pair[1], "UTF-8");
                    parameters.put(key, value);
                }
            } catch (UnsupportedEncodingException e) {
                Log.web().error(e);
            }
        }
    }

    public final Parameters getParameters() {
        return parameters;
    }

}
