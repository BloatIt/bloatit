package com.bloatit.framework.xcgiserver.scgi;

/*
 * Copyright (c) 2008 ArtemGr Permission to use, copy, modify, and/or distribute this
 * software for any purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies. THE SOFTWARE IS
 * PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE
 * INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE
 * AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY
 * DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF
 * CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE
 * USE OR PERFORMANCE OF THIS SOFTWARE.
 */
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.bloatit.framework.xcgiserver.XcgiParser;

/**
 * SCGIUtils connector.<br>
 * Version: 1.0<br>
 * Home page: http://gist.github.com/38425 See also: http://en.wikipedia.org/wiki/SCGIUtils
 */
public class SCGIParser implements XcgiParser {

    /** Used to decode the headers. */
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");


    private final InputStream input;


    private final OutputStream ouput;

    public SCGIParser(final InputStream input, final OutputStream ouput) {
        this.input = input;
        this.ouput = ouput;
    }

    public static class SCGIException extends IOException {

        private static final long serialVersionUID = 1L;

        public SCGIException(final String message) {
            super(message);
        }
    }



    /**
     * Read the <a href="http://python.ca/scgi/protocol.txt">SCGIUtils</a> request headers.<br>
     * After the headers had been loaded, you can read the body of the request manually from the same {@code input} stream:
     *
     * <pre>
     * // Load the SCGIUtils headers.
     * Socket clientSocket = socket.accept();
     * BufferedInputStream bis = new BufferedInputStream(clientSocket.getInputStream(), 4096);
     * HashMap&lt;String, String&gt; env = SCGIUtils.parse(bis);
     * // Read the body of the request.
     * bis.read(new byte[Integer.parseInt(env.get(&quot;CONTENT_LENGTH&quot;))]);
     * </pre>
     *
     *            an efficient (buffered) input stream.
     * @return strings passed via the SCGIUtils request.
     */
    @Override
    public Map<String, String> getEnv() throws IOException {

        final StringBuilder lengthString = new StringBuilder(12);
        String headers = "";
        for (;;) {
            final char ch = (char) input.read();
            if (ch >= '0' && ch <= '9') {
                lengthString.append(ch);
            } else if (ch == ':') {
                final int length = Integer.parseInt(lengthString.toString());
                final byte[] headersBuf = new byte[length];
                final int read = input.read(headersBuf);
                if (read != headersBuf.length) {
                    throw new SCGIException("Couldn't read all the headers (" + length + ").");
                }
                headers = ISO_8859_1.decode(ByteBuffer.wrap(headersBuf)).toString();
                if (input.read() != ',') {
                    throw new SCGIException("Wrong SCGIUtils header length: " + lengthString);
                }
                break;
            } else {
                lengthString.append(ch);
                throw new SCGIException("Wrong SCGIUtils header length: " + lengthString);
            }
        }
        final Map<String, String> env = new HashMap<String, String>();
        while (headers.length() != 0) {
            final int sep1 = headers.indexOf(0);
            final int sep2 = headers.indexOf(0, sep1 + 1);
            env.put(headers.substring(0, sep1), headers.substring(sep1 + 1, sep2));
            headers = headers.substring(sep2 + 1);
        }
        return env;
    }

    @Override
    public InputStream getPostStream() throws IOException {
        return input;
    }

    @Override
    public OutputStream getResponseStream() throws IOException {
        return new BufferedOutputStream(ouput, 1024);
    }
}
