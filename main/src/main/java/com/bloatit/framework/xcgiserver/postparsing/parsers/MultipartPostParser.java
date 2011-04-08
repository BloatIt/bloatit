package com.bloatit.framework.xcgiserver.postparsing.parsers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.Queue;

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
    private static final String MULTIPART_SAVED_URL = "";
    private static final String MULTIPART_ORIGINAL_FILENAME = "/filename";
    private static final String MULTIPART_CONTENTYPE = "/contenttype";

    private final MultipartMimeParser parser;
    private final Queue<PostParameter> buffer;

    /**
     * <p>
     * Creates a new MultipartPostParser
     * </p>
     * 
     * @param contentType the contentType of the mime, including its boundary
     * @param postStream the stream from which the post is read
     * @param fileSavingDirectory the directory where uploaded files will be
     *            saved
     * @throws InvalidParameterException if any parameter is null
     */
    public MultipartPostParser(final String contentType, final InputStream postStream, final String fileSavingDirectory) {
        parser = new MultipartMimeParser(postStream, contentType, new UUIDFileNameGenerator(), fileSavingDirectory);
        buffer = new LinkedList<PostParameter>();
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
     * @throws MalformedMimeException if the format is incorrect
     * @throws InvalidMimeEncodingException if the encoding is not handled
     * @throws IOException if an IO error occurs while reading the stream or
     *             saving to the file
     */
    @Override
    public PostParameter readNext() throws IOException, InvalidMimeEncodingException, MalformedMimeException {
        if (!buffer.isEmpty()) {
            return buffer.poll();
        }

        final MimeElement next = parser.readContent();
        if (next == null) {
            return null;
        }

        if (next.isFile()) {
            if (next.getDestination() == null) {
                return readNext();
            }
            buffer.add(new PostParameter(next.getName() + MULTIPART_ORIGINAL_FILENAME, next.getFilename()));
            buffer.add(new PostParameter(next.getName() + MULTIPART_CONTENTYPE, next.getContentType()));
            return new PostParameter(next.getName() + MULTIPART_SAVED_URL, next.getDestination().getAbsolutePath());
        }

        final InputStream is = next.getContent();
        int read;
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((read = is.read()) != -1) {
            bos.write((byte) read);
        }

        final String s = new String(bos.toByteArray(), "UTF-8");

        return new PostParameter(next.getName(), s);
    }
}
