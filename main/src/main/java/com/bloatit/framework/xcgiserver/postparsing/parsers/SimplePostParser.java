//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.framework.xcgiserver.postparsing.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.bloatit.common.Log;
import com.bloatit.framework.xcgiserver.postparsing.PostParameter;
import com.bloatit.framework.xcgiserver.postparsing.exceptions.MalformedPostException;

public class SimplePostParser extends PostParameterParser {
    private final int length;
    private final InputStream postStream;
    private String[] postData;
    private int i;

    public SimplePostParser(final InputStream postStream, final int length) {
        this.postStream = postStream;
        this.length = length;
    }

    private void initParsing() throws IOException {
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
    public PostParameter readNext() throws MalformedPostException, IOException {
        if (length == 0) {
            return null;
        }
        if (postData == null) {
            initParsing();
        }
        if (i >= postData.length) {
            return null;
        }
        Log.framework().trace("Reading simple POST data n°" + i);
        while (i < postData.length) {
            final String param = postData[i];
            try {
                final String[] pair = param.split("=");
                if (pair.length >= 2) {
                    final String key = URLDecoder.decode(pair[0], "UTF-8");
                    String value;
                    value = URLDecoder.decode(pair[1], "UTF-8");
                    final PostParameter postParameter = new PostParameter(key, value);
                    i++;
                    return postParameter;
                }
                i++;
            } catch (final UnsupportedEncodingException e) {
                Log.framework().fatal("Encoding UTF-8 is not supported. This is bad, check system parameters", e);
                throw new MalformedPostException("Encoding UTF-8 is not supported. This is bad, check system parameters", e);
            }
        }
        return null;
    }
}
