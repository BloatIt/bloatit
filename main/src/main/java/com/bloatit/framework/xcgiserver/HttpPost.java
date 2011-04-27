/*
 * Copyright (C) 2011 Linkeos.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.framework.xcgiserver;

import static com.bloatit.framework.utils.StringUtils.isEmpty;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.highlevel.MeanUserException;
import com.bloatit.framework.utils.parameters.Parameters;
import com.bloatit.framework.xcgiserver.postparsing.PostParameter;
import com.bloatit.framework.xcgiserver.postparsing.PostParser;
import com.bloatit.framework.xcgiserver.postparsing.exceptions.MalformedPostException;

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
     * @param is The stream to read containing the post Data
     * @param length The length of the post content
     * @param contentType The contentType of the post (obtained previouysly from
     *            the request)
     * @throws IOException
     */
    protected HttpPost(final InputStream is, final int length, final String contentType) throws IOException {
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
     * @param postStream the stream to read post from
     * @param length the length of the post
     * @param contentType the contentType of the post (text/plain,
     *            multipart/form-data ...)
     * @throws IOException
     */
    private void readBytes(final InputStream postStream, final int length, final String contentType) throws IOException {
        final PostParser parser = new PostParser(postStream, length, contentType, UPLOAD_TEMP_DIRECTORY);
        PostParameter pp;
        while ((pp = getNext(parser)) != null) {
            if (!isEmpty(pp.getName()) && !isEmpty(pp.getValue())) {
                parameters.add(pp.getName(), pp.getValue());
            }
        }
    }

    /**
     * Gets the next PostParameter in the parser, ignoring the exceptions
     * 
     * @param parser the <code>POST</code> parser from which content is read
     * @return the next <code>PostParameter</code> or <code>null</code> if no
     *         more content is available
     */
    private PostParameter getNext(final PostParser parser) {
        while (true) {
            try {
                final PostParameter pp = parser.readNext();
                return pp;
            } catch (final MalformedPostException e) {
                Log.web().error("Error in the post. We try to continue, but may have errors later in the page");
                return null;
            } catch (final EOFException e) {
                throw new MeanUserException("Reached EOF in multipart/mine when not expected", e);
            }
        }
    }
}
