package com.bloatit.framework.webserver.postparsing.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.bloatit.common.Log;
import com.bloatit.framework.webserver.postparsing.PostParameter;
import com.bloatit.framework.webserver.postparsing.exceptions.MalformedPostException;

public class SimplePostParser extends PostParameterParser {
    private final int length;
    private final InputStream postStream;
    private String[] postData;
    private int i;

    public SimplePostParser(final InputStream postStream, final int length) {
        this.postStream = postStream;
        this.length = length;
    }

    public void initParsing() throws IOException {
        final byte[] postBytes = new byte[length];
        final int read = postStream.read(postBytes);
        if (read == length) {
            Log.framework().debug("Post value read correctly.");
        } else {
            Log.framework().fatal("Post is not of the expected size, we may encounter problems later during the page generation.");
        }

        final String string = new String(postBytes);
        postData = string.split("&");
        i = 0;
    }

    @Override
    public PostParameter readNext() throws MalformedPostException {
        if(i >= postData.length ){
            return null;
        }
        String param = postData[i];
        try {
            final String[] pair = param.split("=");
            if (pair.length >= 2) {
                final String key = URLDecoder.decode(pair[0], "UTF-8");
                String value;
                value = URLDecoder.decode(pair[1], "UTF-8");
                return new PostParameter(key, value);
            } else {
                throw new MalformedPostException("Found post data that doesn't match the expected format: [" + param + "]");
            }
        } catch (final UnsupportedEncodingException e) {
            Log.framework().fatal("Encoding is UTF-8 not supported. This is bad, check system parameters", e);
            throw new MalformedPostException("Encoding is UTF-8 not supported. This is bad, check system parameters", e);
        }
    }
}
