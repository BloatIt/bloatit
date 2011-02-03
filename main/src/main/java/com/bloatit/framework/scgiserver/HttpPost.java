package com.bloatit.framework.scgiserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.activation.MimeTypeParseException;

import com.bloatit.common.Log;
import com.bloatit.framework.scgiserver.mime.MultipartMime;
import com.bloatit.framework.utils.Parameters;

/**
 * A class to describe elements transmitted by an http POST query
 */
public class HttpPost {
    private final Parameters parameters = new Parameters();

    /**
     * <p>
     * Construct an HttpPost from a POST request
     * </p>
     * 
     * @param is
     *            The stream to read containing the post Data
     * @param length
     *            The length of the post content
     * @param contentType
     *            The contentType of the post (obtained previouysly from the
     *            request)
     * @throws IOException
     */
    public HttpPost(final InputStream is, final int length, final String contentType) throws IOException {
        readBytes(is, length, contentType);
    }

    /**
     * <p>
     * Parses the post and fills the list of parameters
     * </p>
     * 
     * @param postStream
     *            the stream to read post from
     * @param length
     *            the length of the post
     * @param contentType
     *            the contentType of the post (text/plain, multipart/form-data
     *            ...
     * @throws IOException
     */
    private void readBytes(final InputStream postStream, final int length, final String contentType) throws IOException {
        if (contentType != null && !contentType.equals("") && contentType.startsWith("multipart/form-data")) {
            
            try {
                processMultipart(postStream, contentType);
            } catch (MimeTypeParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            final byte[] postBytes = new byte[length];
            final int read = postStream.read(postBytes);
            if (read == length) {
                Log.framework().debug("Post value read correctly.");
            } else {
                Log.framework().error("End of strem reading the postBytes. There may be difficulties to generate the page.");
            }

            final String string = new String(postBytes);
            for (final String param : string.split("&")) {
                try {
                    final String[] pair = param.split("=");
                    if (pair.length >= 2) {
                        final String key = URLDecoder.decode(pair[0], "UTF-8");
                        String value;
                        value = URLDecoder.decode(pair[1], "UTF-8");
                        parameters.put(key, value);
                    }
                } catch (final UnsupportedEncodingException e) {
                    Log.framework().error(e);
                }
            }
        }
    }

    /**
     * Gets the list of parameters
     * 
     * @return the list of post parameters for the page
     */
    public final Parameters getParameters() {
        return parameters;
    }

    private final void processMultipart(InputStream postStream, final String contentType) throws MimeTypeParseException {
        Log.web().trace("Received a form-data, starting parsing");
        final MultipartMime mm = new MultipartMime(postStream, contentType);
        Log.web().trace("Parsing of post data over");
        System.out.println(mm);
        /*
         * for (MimeElement me : mm) {
         * 
         * }
         */
    }

}
