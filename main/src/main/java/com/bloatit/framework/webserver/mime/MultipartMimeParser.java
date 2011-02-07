package com.bloatit.framework.webserver.mime;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import com.bloatit.framework.exceptions.NonOptionalParameterException;
import com.bloatit.framework.webserver.mime.filenaming.FileNamingGenerator;

/**
 * <p>
 * MultipartMime fields are used to describe complex data in textual form. They are used for mails or as a way to send results from form data (when
 * input contains a file).
 * </p>
 * <p>
 * An example of a multipart mime :
 *
 * <pre>
 * MIME-Version: 1.0\r\n
 * Content-Type: multipart/mixed; boundary="frontier"\r\n
 * \r\n
 * This is a message with multiple parts in MIME format.\r\n
 * --frontier\r\n
 * Content-Type: text/plain\r\n
 * \r\n
 * This is the body of the message.\r\n
 * --frontier\r\n
 * Content-Type: application/octet-stream\r\n
 * Content-Transfer-Encoding: base64\r\n
 * \r\n
 * PGh0bWw+CiAgPGhlYWQ+CiAgPC9oZWFkPgogIDxib2R5PgogICAgPHA+VGhpcyBpcyB0aGUg
 * Ym9keSBvZiB0aGUgbWVzc2FnZS48L3A+CiAgPC9ib2R5Pgo8L2h0bWw+Cg==\r\n
 * --frontier--\r\n
 * </pre>
 *
 * </p>
 * <p>
 * <code>MultipartMimeParser</code> handles reading the input stream, parsing the content, decoding if needed (mostly shouldn't be needed), and saving
 * files to the disk.
 * </p>
 * <p>
 * Example of use :
 *
 * <pre>
 * final BufferedInputStream bis = new BufferedInputStream(clientSocket.getInputStream(), 4096);
 * final Map&lt;String, String&gt; env = SCGIUtils.parse(bis);
 * final HttpHeader header = new HttpHeader(env);
 * MultipartMimeParser mmp = new MultipartMimeParser(bis, header.getContentType(), new UUIDFileNameGenerator(), UPLOAD_TEMP_DIRECTORY);
 * MimeElement me;
 * while ((me = mmp.readContent()) != null) {
 *     // Do something with the MimeElement
 *     System.out.println(me);
 * }
 * </pre>
 *
 * </p>
 */
public class MultipartMimeParser {
    private static final String DOUBLE_HYPHEN = "--";
    private static final byte HYPHEN = (byte) '-';
    private static final byte CR = (byte) '\r';
    private static final byte LF = (byte) '\n';

    /**
     * <p>
     * Describe the various states of the parser.
     * </p>
     * <p>
     * Chronological order is :
     * <li><code>MULTIPART_HEADER</code>: Nothing parsed yet, the whole header is yet to be parsed</li>
     * <li><code>MULTIPART_PREAMBLE</code>: Header parsed, preamble may be available (note preamble is to be ignored)</li>
     * <li><code>CONTENT_HEADER</code>: Header & Preamble read, now is the turn of content. Note, this state is used many time.</li>
     * <li><code>CONTENT_CONTENT</code>: Right after we finished reading content header, we can find the real content</li>
     * <li><code>END</code>: When we reach the end of file (--boundary--)</li>
     * </p>
     * <p>
     * <code>CONTENT_HEADER</code> and <code>CONTENT_CONTENT</code> are used many time during parsing. If the mime contains 3 elements, each of the
     * two previous states will be used 3 times.
     * </p>
     */
    private enum State {
        MULTIPART_HEADER, MULTIPART_PREAMBLE, CONTENT_HEADER, CONTENT_CONTENT, END
    }

    private State currentState;
    private final byte[] boundary;
    private final ByteReader multipartStream;
    private final byte[] completeBoundary;
    private final FileNamingGenerator nameGen;
    private final String fileSavingDirectory;

    /**
     * <p>
     * Creates a multipart mime element based on the given bytes and contentType
     * </p>
     * <p>
     * The content of the <code>multiPartStream</code> will be parsed using the boundary contained in <code>contentType</code>
     * </p>
     * <p>
     * Example contentType:
     *
     * <pre>
     * multipart/form-data; boundary=---------------------------19995485819364555411863804163
     * </pre>
     *
     * </p>
     * <p>
     * <code>nameGen</code> and <code>fileSavingDirectory</code> are used to parameter the place where uploaded files will be saved.
     * </p>
     *
     * @param multiPartStream
     *            the stream from which the post will be parsed
     * @param contentType
     *            The contentType of the mimeElement, including it's boundary
     * @param nameGen
     *            The name generator used to generate names of the files used to store uploaded files
     * @param fileSavingDirectory
     *            The name of the directory used to store files used to store uploaded files
     * @throws NonOptionalParameterException
     *             if any parameter is null
     */
    public MultipartMimeParser(InputStream multiPartStream, final String contentType, final FileNamingGenerator nameGen, String fileSavingDirectory) {
        if (multiPartStream == null || contentType == null || nameGen == null || fileSavingDirectory == null) {
            throw new NonOptionalParameterException();
        }

        final int boundaryIndex = contentType.indexOf("boundary=");
        this.boundary = (contentType.substring(boundaryIndex + 9)).getBytes();
        this.multipartStream = new ByteReader(multiPartStream);
        this.nameGen = nameGen;
        this.fileSavingDirectory = fileSavingDirectory;

        currentState = State.MULTIPART_HEADER;

        // Initializing an array with the "complete boundary" : CRLF--boundary
        completeBoundary = new byte[boundary.length + 4];
        completeBoundary[0] = CR;
        completeBoundary[1] = LF;
        completeBoundary[2] = HYPHEN;
        completeBoundary[3] = HYPHEN;
        for (int i = 0; i < boundary.length; i++) {
            completeBoundary[i + 4] = boundary[i];
        }
    }

    /**
     * <p>
     * Reads the header of the multipart
     * </p>
     * <p>
     * <b>note</b>: The contentType header is not part of this
     * </p>
     *
     * @return the String representing the header of the multipart. String can be empty if no header is left to read (either because it's already been
     *         read or because no header have ever been included in this mime)
     * @throws IOException
     *             When the <code>multipartStream</code> can't be read
     */
    public String getHeader() throws IOException {
        if (currentState != State.MULTIPART_HEADER) {
            return "";
        }

        StringBuilder header = new StringBuilder();
        while (currentState == State.MULTIPART_HEADER) {
            String line = multipartStream.readString();
            if (line.isEmpty()) {
                currentState = State.MULTIPART_PREAMBLE;
            } else if (line.equals(DOUBLE_HYPHEN + new String(boundary))) {
                currentState = State.CONTENT_HEADER;
            } else {
                header.append(line);
            }
        }
        return header.toString();
    }

    /**
     * <p>
     * Reads the preamble of the multipart. Preamble is usually ignored.
     * </p>
     *
     * @return the String representing the preamble of the multipart. String can be empty if no preamble is left to read (either because it's already
     *         been read or because no header have ever been included in this mime)
     * @throws IOException
     *             When the <code>multipartStream</code> can't be read
     */
    public String getPreamble() throws IOException {
        if (currentState == State.MULTIPART_HEADER) {
            getHeader();
        }
        if (currentState != State.MULTIPART_PREAMBLE) {
            return "";
        }

        getHeader(); // Skip header

        StringBuilder preamble = new StringBuilder();
        while (currentState == State.MULTIPART_PREAMBLE) {
            try {
                String line = multipartStream.readString();
                if (line.equals(DOUBLE_HYPHEN + new String(boundary))) {
                    currentState = State.CONTENT_HEADER;
                } else {
                    preamble.append(line);
                }
            } catch (EOFException e) {
                return preamble.toString();
            }
        }
        return preamble.toString();
    }

    /**
     * <p>
     * Reads one content of the multipart
     * </p>
     * <p>
     * Core function of the parser, this function reads the next available content of the multipart mime. Content is in fact 2 parts, first its header
     * (describing the kind of content) then the real content.
     * </p>
     * <p>
     * Method takes care of decoding the characters (based on the encoding indicated in the content header) and saves files received to
     * <code>fileSavingDirectory+nameGen.generateName()</code>.
     * </p>
     * <p>
     * When no more data is available (CRLF--boundary--) has been found, the next call to this method will return null.
     * </p>
     *
     * @return a <code>MimeElement</code> representing the content of this mime, or <code>null</code> if end has been reached.
     * @throws IOException
     *             Whenever the multipartStream is not available
     * @throws InvalidMimeEncodingException
     *             When the mime encoding used is not handled
     * @throws MalformedMimeException
     *             When the mime is ill-formed, for example is we reach EOF while expecting more data, or if a header happens to be after a content.
     */
    public MimeElement readContent() throws IOException, InvalidMimeEncodingException, MalformedMimeException {
        if (currentState == State.MULTIPART_HEADER || currentState == State.MULTIPART_PREAMBLE) {
            getPreamble();
        }
        if (currentState == State.END) {
            return null;
        }

        int i = 0;
        MimeElement me = new MimeElement(nameGen, fileSavingDirectory);
        boolean readBoundary = false;

        try {
            while (!readBoundary) {
                if (currentState == State.CONTENT_HEADER) {
                    // Reading one of the multipart header
                    String line = multipartStream.readString();
                    if (line.isEmpty()) {
                        currentState = State.CONTENT_CONTENT;
                        i = 0;
                    } else if (line.startsWith(DOUBLE_HYPHEN)) {
                        // End of file
                        currentState = State.END;
                        return null;
                    } else {
                        final String[] elems = line.split(";");
                        for (final String elem : elems) {
                            final String[] headerElements = elem.split("[:=]");
                            if (headerElements.length > 1) {
                                String headerContent = headerElements[1].trim();
                                if (headerContent.startsWith("\"") && headerContent.endsWith("\"")) {
                                    // Remove the '"' surrounding some of the
                                    // fields
                                    headerContent = headerContent.substring(1, headerContent.length() - 1);
                                }
                                me.addHeader(headerElements[0].trim(), headerContent);
                            }
                        }
                    }
                } else /* (currentState == State.CONTENT_CONTENT) */{
                    byte b = multipartStream.read();
                    if (i < completeBoundary.length && b == completeBoundary[i]) {
                        i++;
                        if (i == completeBoundary.length) {
                            i = 0;
                            readBoundary = true;
                        }
                    } else {
                        if (i != 0) {
                            for (int j = 0; j < i; j++) {
                                me.addContent(completeBoundary[j]);
                            }
                        }
                        i = 0;
                        me.addContent(b);
                    }
                }
            }
        } catch (EOFException e) {
            throw new MalformedMimeException("Malformed mime, expected data, received EOF", e);
        }
        // Do boundary related stuff here
        byte b0 = multipartStream.read();
        byte b1 = multipartStream.read();
        if (b0 == CR && b1 == LF) {
            currentState = State.CONTENT_HEADER;
        } else if (b0 == HYPHEN && b1 == HYPHEN) {
            currentState = State.END;
            try {
                while (true) {
                    // Consume epilogue
                    multipartStream.read();
                }
            } catch (EOFException e) {
            }
        } else {
            throw new MalformedMimeException("Malformed mime, expected \"--\" or \\r\\n and read '" + (char) (b0 & 0xff) + (char) (b1 & 0xff) + "'");
        }

        if (me != null) {
            me.close();
        }
        return me;
    }

    /**
     * <p>
     * Indicates if any data is left to read
     * </p>
     * <p>
     * Note, even if <code>available</code> returns <code>true</code> there may be no more data to read. Calling this method without using
     * {@link #readContent()} first will result in useless data (as the parser doesn't know yet if some data is available). <br />
     * After using {@link #readContent()} once the results of this method can be trusted. <br />
     * It is still better to check if the content returned by {@link #readContent()} is null or not (and it is a more fiable indicator of end of mime
     * than this).
     * </p>
     *
     * @return <code>true</code> if some data is still available in the stream <code>false</code> otherwise.
     */
    public boolean available() {
        return currentState != State.END;
    }
}
