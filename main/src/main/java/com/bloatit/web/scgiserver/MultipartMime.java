package com.bloatit.web.scgiserver;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.bloatit.model.data.util.NonOptionalParameterException;

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
 * MIME-Version: 1.0
 * Content-Type: multipart/mixed; boundary="frontier"
 * 
 * This is a message with multiple parts in MIME format.
 * --frontier
 * Content-Type: text/plain
 * 
 * This is the body of the message.
 * --frontier
 * Content-Type: application/octet-stream
 * Content-Transfer-Encoding: base64
 * 
 * PGh0bWw+CiAgPGhlYWQ+CiAgPC9oZWFkPgogIDxib2R5PgogICAgPHA+VGhpcyBpcyB0aGUg
 * Ym9keSBvZiB0aGUgbWVzc2FnZS48L3A+CiAgPC9ib2R5Pgo8L2h0bWw+Cg==
 * --frontier--
 * </pre>
 * 
 * </p>
 * <p>
 * When constructing a MultipartMime from a byte array, parsing will occur and
 * build a tree containing each elements. Note: class can only handle single
 * level right now, and ignores character encoding.
 * </p>
 */
public class MultipartMime {
    private static final byte HYPHEN = (byte)'-';
    private static final byte CR = (byte) '\r';
    private static final byte LF = (byte) '\n';
    
    /**
     * The character sequence used to separate 2 MimeElements in the multipart
     */
    private byte[] boundary;
    /**
     * the List of mimeElements contained in the multipart
     */
    private final List<MimeElement> elements;
    /**
     * The contentType. Can be multipart/mixed, multipart/form-data ...
     */
    private String contentType;

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
     *            the bytes to parse
     * @param contentType
     *            The contentType of the mimeElement, including it's boundary
     * @throws NonOptionalParameterException
     *             if any parameter (<code>postBytes</code> or
     *             <code>contentType</code> is null)
     */
    public MultipartMime(final byte[] postBytes, String contentType) {
        if (postBytes == null || contentType == null) {
            throw new NonOptionalParameterException();
        }

        this.elements = new ArrayList<MimeElement>();
        int boundaryIndex = contentType.indexOf("boundary=");
        this.boundary = (contentType.substring(boundaryIndex + 9)).getBytes();
        this.contentType = new String((contentType.substring(0, boundaryIndex)).getBytes());

        parseMultipart(postBytes, contentType);
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
    public String toString() {
        String result = "";

        result += "[CONTENT-TYPE]: " + contentType + "\n";
        result += "[BOUNDARY]: " + new String(boundary) + "\n";
        for (MimeElement element : elements) {
            result += element.toString() + "\n";
        }

        return result;
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
     */
    private void parseMultipart(final byte[] postBytes, String contentType) {
        // An example of multipart Mime :
        //
        // MIME-Version: 1.0
        // Content-Type: multipart/mixed; boundary="frontier"
        //
        // This is a message with multiple parts in MIME format. --frontier
        // Content-Type: text/plain
        //
        // This is the body of the message. --frontier
        // Content-Type: application/octet-stream Content-Transfer-Encoding:
        //  base64
        //
        // PGh0bWw+CiAgPGhlYWQ+CiAgPC9oZWFkPgogIDxib2R5PgogICAgPHA+VGhpcyBpcyB0aGUg
        // Ym9keSBvZiB0aGUgbWVzc2FnZS48L3A+CiAgPC9ib2R5Pgo8L2h0bWw+Cg==
        // --frontier--
        //
        // Algorithm used :
        // - We try to spot any occurence of double hyphens 
        // - Whenever we find double hyphens, we check if next part matches boundary
        // - If it does match boundary, we move to the next part of the element
        // - If it doesn't, it means we are still into the previous element
        ByteArrayInputStream input = new ByteArrayInputStream(postBytes);
        ArrayList<Byte> boundBuff = new ArrayList<Byte>();
        ArrayList<Byte> contentBuff = new ArrayList<Byte>();
        int hyphenRead = 0;
        byte b;
        int i = 0;
        while (input.available() > 0) {
            b = (byte) input.read();
            if (hyphenRead == 2) {
                // We might be inside a boundary
                if (i < boundary.length && boundary[i] == b) {
                    boundBuff.add(b);
                    i++;
                } else if (i == boundary.length) {
                    // Boundary complete ... content after this
                    boundBuff.clear();
                    int j = 0;
                    byte[] d = new byte[contentBuff.size()];
                    for (Byte c : contentBuff) {
                        d[j] = c;
                        j++;
                    }
                    parseMultipartContent(contentBuff);
                    contentBuff.clear();
                    hyphenRead = 0;
                    i = 0;
                } else {
                    // Not part of bounday, dump the possible content
                    contentBuff.addAll(boundBuff);
                    if (b == HYPHEN) {
                        // It's an hyphen, we have to count it as part of a
                        // possible boundary marker
                        hyphenRead = 1;
                        boundBuff.add(b);
                    } else {
                        hyphenRead = 0;
                        contentBuff.add(b);
                    }
                    i = 0;
                }
            } else if (b == HYPHEN) {
                boundBuff.add(b);
                hyphenRead++;
            } else {
                // Content
                contentBuff.add(b);
            }
        }
    }

    /**
     * <p>
     * Parses a multipartContent
     * </p>
     * 
     * @param content
     *            the content to parse
     */
    private void parseMultipartContent(List<Byte> content) {


        if (content.size() > 0) {
            boolean foundCR = false;
            boolean newLine = true;
            boolean isContent = false;
            List<Byte> contentBytes = new ArrayList<Byte>();
            List<Byte> headerBytes = new ArrayList<Byte>();

            for (byte b : content) {

                if (isContent) {
                    contentBytes.add(b);
                } else if (newLine) {
                    // Previous characters were CRLF

                    if (b == CR) {
                        if (!foundCR) {
                            foundCR = true;
                        } else {
                            newLine = false;
                        }
                    } else {
                        if (b == LF) {
                            if (foundCR) {
                                isContent = true;
                            } else {
                                newLine = false;
                                headerBytes.add(b);
                            }
                        } else {
                            newLine = false;
                            headerBytes.add(b);
                        }
                    }
                } else if (foundCR) {
                    // Previous character was CR
                    if (b == LF) {
                        newLine = true;
                    } else {
                        newLine = false;
                    }
                    foundCR = false;
                    headerBytes.add(b);
                } else {
                    if (b == CR) {
                        foundCR = true;
                    }
                    // Header
                    headerBytes.add(b);
                }
            }
            MimeElement me = new MimeElement(contentBytes, parseHeader(headerBytes));
            elements.add(me);
        }
    }

    /**
     * <p>
     * Parses the header of a MultipartMime content
     * </p>
     * 
     * @param headerBytes
     *            the bytes containing the header
     * @return a map of header-name -> value
     */
    private Map<String, String> parseHeader(List<Byte> headerBytes) {
        Map<String, String> result = new HashMap<String, String>();

        byte[] bytes = new byte[headerBytes.size()];
        int i = 0;
        for (byte b : headerBytes) {
            bytes[i++] = b;
        }

        InputStream plip = new ByteArrayInputStream(bytes);
        InputStreamReader isr = new InputStreamReader(plip);
        BufferedReader plop = new BufferedReader(isr);

        try {
            while (plop.ready()) {
                String line = plop.readLine();
                String[] elems = line.split(";");
                for (String elem : elems) {
                    String[] headerElements = elem.split("[:=]");
                    if (headerElements.length > 1) {
                        result.put(headerElements[0].trim(), headerElements[1].trim());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int l = 0;
        for (Entry<String, String> entry : result.entrySet()) {
            l += entry.getKey().length() + entry.getValue().length();
        }

        return result;
    }
}
