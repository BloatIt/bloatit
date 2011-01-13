package com.bloatit.web.scgiserver;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.bloatit.common.Log;
import com.bloatit.web.utils.url.Parameters;

public class HttpPost {

    private final Parameters parameters = new Parameters();

    public HttpPost(byte[] postBytes) {
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
