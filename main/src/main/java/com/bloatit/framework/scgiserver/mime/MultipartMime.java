package com.bloatit.framework.scgiserver.mime;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.activation.MimeTypeParseException;

import com.bloatit.framework.exceptions.NonOptionalParameterException;
import com.bloatit.framework.scgiserver.ByteReader;

/**
 * <p>
 * MultipartMime fields are used to describe complex data in textual form. They
 * are used for mails or as a way to send results from form data (when input
 * contains a file).
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
 * When constructing a MultipartMime from a byte array, parsing will occur and
 * build a tree containing each elements. Note: class can only handle single
 * level right now, and ignores character encoding.
 * </p>
 */
public class MultipartMime implements Iterable<MimeElement> {

    private enum State {
        MULTIPART_HEADER, CONTENT_HEADER, MULTIPART_IGNORE, CONTENT_CONTENT, END, COMPLETE_BOUNDARY
    }

    private static final byte HYPHEN = (byte) '-';
    private static final byte CR = (byte) '\r';
    private static final byte LF = (byte) '\n';

    /**
     * The character sequence used to separate 2 MimeElements in the multipart
     */
    private final byte[] boundary;
    /**
     * the List of mimeElements contained in the multipart
     */
    private final List<MimeElement> elements;
    /**
     * The contentType. Can be multipart/mixed, multipart/form-data ...
     */
    private final String contentType;

    /**
     * <p>
     * Creates a multipart mime element based on the given bytes and contentType
     * </p>
     * <p>
     * The <code>postBytes</code> will be parsed using the <code>boundary</code>
     * contained in <code>contentType</code>
     * </p>
     * <p>
     * Example contentType:
     * 
     * <pre>
     * multipart/form-data; boundary=---------------------------19995485819364555411863804163
     * </pre>
     * <p>
     * 
     * @param postBytes
     *            the stream from which the post will be parsed
     * @param contentType
     *            The contentType of the mimeElement, including it's boundary
     * @throws MimeTypeParseException
     * @throws NonOptionalParameterException
     *             if any parameter (<code>postStream</code> or
     *             <code>contentType</code> is null)
     */
    public MultipartMime(InputStream postStream, final String contentType) throws MimeTypeParseException {
        if (postStream == null || contentType == null) {
            throw new NonOptionalParameterException();
        }

        this.elements = new ArrayList<MimeElement>();
        final int boundaryIndex = contentType.indexOf("boundary=");
        this.boundary = (contentType.substring(boundaryIndex + 9)).getBytes();
        this.contentType = new String((contentType.substring(0, boundaryIndex)).getBytes());

        try {
            parseMultipart(postStream, contentType);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedMimeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * <p>
     * Finds the mimeElement in the Multipart with a given name
     * </p>
     * <p>
     * Name should be given using the html attribute name
     * </p>
     * <p>
     * Name is in fact one of the parameters of the mimeElement header (part of
     * ContentDisposition according to RFC)
     * </p>
     * 
     * @param name
     *            the name of the element to find
     * @return the element with a matching name, or null if no element has such
     *         a name
     */
    public MimeElement getByName(String name) {
        for (MimeElement me : elements) {
            String value = me.getHeaderField("name");
            if (value == name) {
                return me;
            }
        }
        return null;
    }

    /**
     * Returns a string representation of the content type. This string is build
     * to be displayed in a console for debug and not to be used in any other
     * situation. Perform your own tree parsing if you need to display in any
     * other fashion.
     */
    @Override
    public String toString() {
        String result = "";

        result += "[CONTENT-TYPE]: " + contentType + "\n";
        result += "[BOUNDARY]: " + new String(boundary) + "\n";
        for (MimeElement element : elements) {
            result += element.toString() + "\n";
        }

        return result;
    }

    @Override
    public Iterator<MimeElement> iterator() {
        return elements.iterator();
    }

    /**
     * <p>
     * Takes a whole multipart mime and parses it entirely
     * </p>
     * 
     * @param postBytes
     *            the bytes to parse (i.e. the multipart mime)
     * @param contentType
     *            The content type String for the mime element, including the
     *            boundary
     * @return a Parsed MimeMultipart
     * @throws IOException
     * @throws MimeTypeParseException
     * @throws MalformedMimeException
     */
    private void parseMultipart(InputStream postStream, String contentType) throws IOException, MimeTypeParseException, MalformedMimeException {
        // MIME-Version: 1.0
        // Content-Type: multipart/mixed; boundary="frontier"
        //
        // This is a message with multiple parts in MIME format.
        // --frontier
        // Content-Type: text/plain
        //
        // This is the body of the message.
        // --frontier
        // Content-Type: application/octet-stream
        // Content-Transfer-Encoding: base64
        //
        // PGh0bWw+CiAgPGhlYWQ+CiAgPC9oZWFkPgogIDxib2R5PgogICAgPHA+VGhpcyBpcyB0aGUg
        // Ym9keSBvZiB0aGUgbWVzc2FnZS48L3A+CiAgPC9ib2R5Pgo8L2h0bWw+Cg==
        // --frontier--

        byte[] completeBoundary = new byte[boundary.length + 4];
        completeBoundary[0] = CR;
        completeBoundary[1] = LF;
        completeBoundary[2] = HYPHEN;
        completeBoundary[3] = HYPHEN;
        for (int i = 0; i < boundary.length; i++) {
            completeBoundary[i + 4] = boundary[i];
        }

        ByteReader reader = new ByteReader(postStream);

        State currentState = State.MULTIPART_HEADER;

        // First, parsing the multipart header (if there is one left) and
        // the multipart content
        try {
            while (currentState == State.MULTIPART_HEADER) {
                String line = reader.readString();
                if (line.isEmpty()) {
                    currentState = State.MULTIPART_IGNORE;
                } else if (line.equals("--" + new String(boundary))) {
                    currentState = State.CONTENT_HEADER;
                }
            }

            while (currentState == State.MULTIPART_IGNORE) {
                String line = reader.readString();
                if (line.equals("--" + new String(boundary))) {
                    currentState = State.CONTENT_HEADER;
                }
            }
        } catch (EOFException e) {
            currentState = State.END;
        }

        int i = 0;
        MimeElement me = null;

        int hyphens = 0;
        try {
            while (currentState != State.END) {
                if (currentState == State.COMPLETE_BOUNDARY) {
                    if (me != null) {
                        elements.add(me);
                        me.close();
                        me = null;
                    }
                    // Just finished parsing a boundary
                    byte b;
                    b = reader.read();
                    if (b == CR) {
                        // Do nothing
                    } else if (b == LF) {
                        currentState = State.CONTENT_HEADER;
                    } else if (b == HYPHEN) {
                        hyphens++;
                        if (hyphens == 2) {
                            currentState = State.END;
                        }
                    }
                } else if (currentState == State.CONTENT_HEADER) {
                    // Reading one of the multipart header
                    String line = reader.readString();
                    if (line.isEmpty()) {
                        currentState = State.CONTENT_CONTENT;
                        i = 0;
                    } else {
                        final String[] elems = line.split(";");
                        for (final String elem : elems) {
                            final String[] headerElements = elem.split("[:=]");
                            if (headerElements.length > 1) {
                                if (me == null) {
                                    me = new MimeElement();
                                }
                                me.addHeader(headerElements[0].trim(), headerElements[1].trim());
                            }
                        }
                    }
                } else /* (currentState == State.CONTENT_CONTENT) */{
                    byte b = reader.read();
                    if (i < completeBoundary.length && b == completeBoundary[i]) {
                        i++;
                        if (i == completeBoundary.length) {
                            i = 0;
                            currentState = State.COMPLETE_BOUNDARY;
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
            /* currentState == State.END; */
            // Insert here code to execute when we reach end, if needed
        } catch (final EOFException e) {
            // We reached end of file
            throw new MimeTypeParseException("Reached end of file too early (expected --boundary-- and received EOF)");
        } catch (IOException e) {
            throw new IOException();
        } catch (InvalidMimeEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (me != null) {
                me.close();
            }
        }
    }
}
