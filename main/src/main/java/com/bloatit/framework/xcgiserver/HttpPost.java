package com.bloatit.framework.xcgiserver;

import java.io.IOException;
import java.io.InputStream;

import com.bloatit.common.Log;
import com.bloatit.framework.utils.Parameters;
import com.bloatit.framework.webserver.postparsing.PostParameter;
import com.bloatit.framework.webserver.postparsing.PostParser;
import com.bloatit.framework.webserver.postparsing.exceptions.MalformedPostException;

/**
 * A class to describe elements transmitted by an http POST query
 */
public class HttpPost {
    private final static String UPLOAD_TEMP_DIRECTORY = System.getProperty("user.home") + "/.local/share/bloatit/uploads_temp/";
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
     * Gets the list of parameters
     *
     * @return the list of post parameters for the page
     */
    public final Parameters getParameters() {
        return parameters;
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
     *            ...)
     * @throws IOException
     */
    private void readBytes(final InputStream postStream, final int length, final String contentType) throws IOException {
        PostParser parser = new PostParser(postStream, length, contentType, UPLOAD_TEMP_DIRECTORY);
        PostParameter pp;
        while ((pp = getNext(parser)) != null) {
            parameters.add(pp.getName(), pp.getValue());
        }

        // if (contentType != null && !contentType.equals("") &&
        // contentType.startsWith("multipart/form-data")) {
        // System.out.println(contentType);
        // try {
        // processMultipart(postStream, contentType);
        // } catch (MimeTypeParseException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // } catch (InvalidMimeEncodingException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // } catch (MalformedMimeException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // } else {
        //
        // final byte[] postBytes = new byte[length];
        // final int read = postStream.read(postBytes);
        // if (read == length) {
        // Log.framework().debug("Post value read correctly.");
        // } else {
        // Log.framework().error("End of strem reading the postBytes. There may be difficulties to generate the page.");
        // }
        //
        // final String string = new String(postBytes);
        // for (final String param : string.split("&")) {
        // try {
        // final String[] pair = param.split("=");
        // if (pair.length >= 2) {
        // final String key = URLDecoder.decode(pair[0], "UTF-8");
        // String value;
        // value = URLDecoder.decode(pair[1], "UTF-8");
        // parameters.put(key, value);
        // }
        // } catch (final UnsupportedEncodingException e) {
        // Log.framework().error(e);
        // }
        // }
        //
        // }
    }

    /**
     * Gets the next PostParameter in the parser, ignoring the exceptions
     *
     * @param parser
     *            the <code>POST</code> parser from which content is read
     * @return the next <code>PostParameter</code> or <code>null</code> if no
     *         more content is available
     */
    private PostParameter getNext(PostParser parser) {
        // TODO: remonter différentes execption selon les cas d'erreurs
        // while (true) {
        try {
            PostParameter pp = parser.readNext();
            return pp;
        } catch (MalformedPostException e) {
            Log.web().error("Error in the post. We try to continue, but may have errors later in the page");
            return null;
        }
        // }
    }

    // private final void processMultipart(InputStream postStream, final String
    // contentType) throws MimeTypeParseException, IOException,
    // InvalidMimeEncodingException, MalformedMimeException {
    // Log.web().trace("Received a form-data, starting parsing");
    // // final MultipartMime mm = new MultipartMime(postStream, contentType);$
    // final MultipartMimeParser mmp = new MultipartMimeParser(postStream,
    // contentType, new UUIDFileNameGenerator(), UPLOAD_TEMP_DIRECTORY);
    // Log.web().trace("Parsing of post data over");
    // MimeElement me;
    // while ((me = mmp.readContent()) != null) {
    // System.out.println(me);
    // }
    // }

}
