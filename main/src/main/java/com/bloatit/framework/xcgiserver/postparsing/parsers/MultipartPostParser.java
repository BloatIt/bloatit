package com.bloatit.framework.xcgiserver.postparsing.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;

import com.bloatit.framework.xcgiserver.mime.InvalidMimeEncodingException;
import com.bloatit.framework.xcgiserver.mime.MalformedMimeException;
import com.bloatit.framework.xcgiserver.mime.MimeElement;
import com.bloatit.framework.xcgiserver.mime.MultipartMimeParser;
import com.bloatit.framework.xcgiserver.mime.filenaming.UUIDFileNameGenerator;
import com.bloatit.framework.xcgiserver.postparsing.PostParameter;

/**
 * Parser used to handle multipart posts (mixed or form-data alike).
 */
public class MultipartPostParser extends PostParameterParser {
    MultipartMimeParser parser;

    /**
     * <p>
     * Creates a new MultipartPostParser
     * </p>
     * 
     * @param contentType
     *            the contentType of the mime, including its boundary
     * @param postStream
     *            the stream from which the post is read
     * @param fileSavingDirectory
     *            the directory where uploaded files will be saved
     * @throws InvalidParameterException
     *             if any parameter is null
     */
    public MultipartPostParser(String contentType, InputStream postStream, String fileSavingDirectory) {
        parser = new MultipartMimeParser(postStream, contentType, new UUIDFileNameGenerator(), fileSavingDirectory);
    }

    /**
     * <p>
     * Reads the next element of a multipart mime post.
     * </p>
     * <p>
     * If element is not a file, returns a <code>PageParameter</code> containing
     * the whole content of the element. If element is a file, the
     * <code>PageParameter</code> contains only the location of the file
     * 
     * @return a <code>MimeElement</code> representing the content of this mime,
     *         or <code>null</code> if end has been reached.
     * @throws MalformedMimeException
     *             if the format is incorrect
     * @throws InvalidMimeEncodingException
     *             if the encoding is not handled
     * @throws IOException
     *             if an IO error occurs while reading the stream or saving to
     *             the file
     */
    @Override
    public PostParameter readNext() throws IOException, InvalidMimeEncodingException, MalformedMimeException {
        MimeElement next = parser.readContent();
        if (next == null) {
            return null;
        }

        if (next.isFile()) {
            return new PostParameter(next.getName(), next.getDestination().getAbsolutePath());
        }

        InputStream is = next.getContent();
        StringBuilder sb = new StringBuilder();
        int read;
        while ((read = is.read()) != -1) {
            sb.append((char) (read & 0xff));
        }

        return new PostParameter(next.getName(), sb.toString());
    }
}
