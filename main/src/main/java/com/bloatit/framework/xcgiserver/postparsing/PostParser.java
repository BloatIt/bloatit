package com.bloatit.framework.xcgiserver.postparsing;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.xcgiserver.mime.InvalidMimeEncodingException;
import com.bloatit.framework.xcgiserver.mime.MalformedMimeException;
import com.bloatit.framework.xcgiserver.postparsing.exceptions.MalformedPostException;
import com.bloatit.framework.xcgiserver.postparsing.parsers.MultipartPostParser;
import com.bloatit.framework.xcgiserver.postparsing.parsers.PostParameterParser;
import com.bloatit.framework.xcgiserver.postparsing.parsers.SimplePostParser;

/**
 *
 */
public class PostParser {
    private final static String MULTIPART_FORM_DATA = "multipart/form-data";
    private final static String MULTIPART_MIXED = "multipart/mixed";
    private final static String DEFAULT_TYPE = "plain/text";
    private final InputStream postStream;
    private final String contentType;
    private final int length;
    private final PostParameterParser parser;
    private final String fileSavingDirectory;

    private static final Set<String> availableParsers = Collections.unmodifiableSet(new HashSet<String>() {
        private static final long serialVersionUID = 4766633781652307823L;
        {
            add(MULTIPART_FORM_DATA);
            add(MULTIPART_MIXED);
            add(DEFAULT_TYPE);
        }
    });

    /**
     * <p>
     * Creates a new Parser that can be used to get all the elements contained
     * in a post
     * </p>
     *
     * @param postStream the stream from which the <code>POST</code> will be
     *            read
     * @param length the length of the <code>POST</code> (the number of data
     *            left to read)
     * @param contentType the type of the post, including its
     *            <code>boundary</code> in the case of a multipart
     *            <code>POST</code>
     * @param fileSavingDirectory the directory in which the uploaded files will
     *            be saved
     */
    public PostParser(final InputStream postStream, final int length, final String contentType, final String fileSavingDirectory) {
        this.postStream = postStream;
        this.length = length;
        this.contentType = contentType;
        this.fileSavingDirectory = fileSavingDirectory;
        this.parser = getParser();
    }

    /**
     * <p>
     * Reads the next parameter of the post
     * </p>
     *
     * @return the next parameter of the post, or null if no parameter is
     *         available
     * @throws MalformedPostException whenever there is an exception in the
     *             <code>POST</code> format. It is usually advised to keep on
     *             analyzing the <code>POST</code> (using readNext) till it
     *             returns <code>null</code> as some parameters may be valid
     *             after this.
     */
    public PostParameter readNext() throws MalformedPostException, EOFException {
        try {
            return parser.readNext();
        } catch (final EOFException e) {
            Log.framework().warn("Reached EOF before end of Mime, aborting parsing", e);
            throw new EOFException();
        } catch (final IOException e) {
            throw new BadProgrammerException("IO Error when parsing post, either reading post stream or writing uploaded file to the disk", e);
        } catch (final InvalidMimeEncodingException e) {
            Log.framework().error("Mime encoding not supported", e);
            throw new MalformedPostException();
        } catch (final MalformedMimeException e) {
            Log.framework().warn("Malformed mime", e);
            throw new MalformedPostException();
        } catch (final MalformedPostException e) {
            Log.framework().warn("Malformed post", e);
            throw new MalformedPostException();
        }
    }

    /**
     * @return a set containing all the parsers that can be used to Handle Post
     *         data
     */
    public Set<String> getAvailableParsers() {
        return availableParsers;
    }

    /**
     * Finds the parser matching this <code>POST</code> data
     *
     * @return the parser to use to parse this data
     */
    private PostParameterParser getParser() {
        final int indexOfType = contentType.indexOf(';');
        if (indexOfType == -1) {
            return new SimplePostParser(postStream, length);
        }
        final String realType = (contentType.substring(0, indexOfType)).trim();
        if (!availableParsers.contains(realType) || realType.equals(DEFAULT_TYPE)) {
            return new SimplePostParser(postStream, length);
        }
        if (realType.equals(MULTIPART_FORM_DATA)) {
            return new MultipartPostParser(contentType, postStream, fileSavingDirectory);
        }
        if (realType.equals(MULTIPART_FORM_DATA)) {
            return new MultipartPostParser(contentType, postStream, fileSavingDirectory);
        }
        return new SimplePostParser(postStream, length);
    }
}
