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

public class MultipartMime {
    private final String contentType;
    private byte[] boundary;

    private final List<MimeElement> elements;

    /**
     * @param postBytes
     * @param contentType
     * @throws NonOptionalParameterException
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
     * @return
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @return
     */
    public List<MimeElement> getElements() {
        return elements;
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
        ByteArrayInputStream input = new ByteArrayInputStream(postBytes);
        ArrayList<Byte> boundBuff = new ArrayList<Byte>();
        ArrayList<Byte> contentBuff = new ArrayList<Byte>();
        int hyphenRead = 0;
        byte b;
        int i = 0;
        while (input.available() > 0) {
            b = (byte) input.read();
            if (hyphenRead == 2) {
                // Might be start inside a boundary
                if (i < boundary.length && boundary[i] == b) {
                    // Still possible
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
                    if (b == (byte) '-') {
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
            } else if (b == (byte) '-') {
                boundBuff.add(b);
                hyphenRead++;
            } else {
                // Content
                contentBuff.add(b);
            }
        }
    }

    /**
     * Parses a multipartContent
     * 
     * @param content
     * @return
     */
    private void parseMultipartContent(List<Byte> content) {
        byte CR = (byte) '\r';
        byte LF = (byte) '\n';

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

    public String toString() {
        String result = "";

        result += "[CONTENT-TYPE]: " + contentType + "\n";
        result += "[BOUNDARY]: " + new String(boundary) + "\n";
        for (MimeElement element : elements) {
            result += element.toString() + "\n";
        }

        return result;
    }
}
