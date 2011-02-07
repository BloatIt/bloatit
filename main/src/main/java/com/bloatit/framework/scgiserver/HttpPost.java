package com.bloatit.framework.scgiserver;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.FatalErrorException;
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
        while (true) {
            try {
                PostParameter pp = parser.readNext();
                return pp;
            } catch (MalformedPostException e) {
                Log.web().error("Error in the post. We try to continue, but may have errors later in the page");
                return null;
            } catch (EOFException e ){
                throw new FatalErrorException("Reached EOF in multipart/mine when not expected", e);
            }
        }
    }
}
