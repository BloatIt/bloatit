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
package com.bloatit.framework.xcgiserver.fcgi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.util.HashMap;
import java.util.Map;

import com.bloatit.framework.xcgiserver.XcgiParser;

/**
 * <p>
 * Parse a fastcgi input stream to extract http params and post stream.
 * </p>
 * <p>
 * The fastcgi protocol specification can be found here:
 * {@link "http://www.fastcgi.com/devkit/doc/fcgi-spec.html"}
 * </p>
 * <p>
 * The fastcgi protocol use 2 stream, 1 input stream and 1 output stream,
 * composed by records. The input stream contains the request and the
 * outputstream contains the response.
 * </p>
 * <p>
 * In the input stream, 2 virtuals stream are composed by 2 type of record: the
 * params records and the stdin record.
 * </p>
 */
public class FCGIParser implements XcgiParser {

    /**
     * The FCGI version is always 1.
     */
    final static byte FCGI_VERSION_1 = 1;

    /**
     * Values of type component of record
     */
    private final static byte FCGI_BEGIN_REQUEST = 1;
    final static byte FCGI_ABORT_REQUEST = 2;
    final static byte FCGI_END_REQUEST = 3;
    final static byte FCGI_PARAMS = 4;
    private final static byte FCGI_STDIN = 5;
    final static byte FCGI_STDOUT = 6;
    final static byte FCGI_STDERR = 7;
    final static byte FCGI_DATA = 8;
    private final static byte FCGI_GET_VALUES = 9;
    final static byte FCGI_GET_VALUES_RESULT = 10;
    final static byte FCGI_UNKNOWN_TYPE = 11;
    final static byte FCGI_MAXTYPE = FCGI_UNKNOWN_TYPE;

    /**
     * Mask for flags component of FCGI_BEGIN_REQUEST body
     */
    private final static byte FCGI_KEEP_CONN = 1;

    /**
     * Values for role component of FCGI_BeginRequestBody
     */
    private final static byte FCGI_RESPONDER = 1;
    final static byte FCGI_AUTHORIZER = 2; // NO_UCD
    final static byte FCGI_FILTER = 3; // NO_UCD

    /**
     * Input stream
     */
    private final ByteChannel dataInput;

    /**
     * Status of param input stream. The param input stream is closed when a
     * empty FCGI_PARAMS record is received.
     */
    private boolean paramStreamOpen = true;

    /**
     * Status of post input stream. The post input stream is closed when a empty
     * FCGI_STDIN record is received.
     */
    private boolean postStreamOpen = true;

    /**
     * Http header params. This map is full with param input stream record's
     * content.
     */
    private final Map<String, String> headers;

    /**
     * Stream give to users to write their response.
     */
    private final OutputStream responseStream;

    /**
     * Stream give to users to read the post content
     */
    private final FCGIPostStream postStream;

    /**
     * Create a fcgi parser with the 2 stream of the web server's socket.
     * 
     * @param input stream containing data from the web server
     * @param output stream where to write the response to the web server
     * @throws IOException
     */
    public FCGIParser(final ByteChannel channel) throws IOException {
        // The FCGIOutputStream has a BufferedOutputStream before and a
        // BufferedOutputStream after.
        // The first avoid to give too small or too big packet to put in on
        // record and the
        // second avoid to
        responseStream = new FCGIOutputStream(this, channel);
        postStream = new FCGIPostStream(this);

        this.dataInput = channel;
        headers = new HashMap<String, String>();

    }

    private static class FCGIException extends IOException {

        private static final long serialVersionUID = 1L;

        private FCGIException(final String message) {
            super(message);
        }
    }

    public OutputStream getResponseStream() {
        return responseStream;
    }

    public Map<String, String> getHeaders() throws IOException {
        while (paramStreamOpen) {
            parseRecord();
        }
        return headers;
    }

    /**
     * Fetch one post record if possible
     * 
     * @throws IOException
     */
    protected void fetchPostRecord() throws IOException {

        while (postStreamOpen && parseRecord() != FCGI_STDIN) {
            // If the post stream is not closed, parse some record until finding
            // a new post record.
        }
    }

    protected void fetchAll() throws IOException {
        while (paramStreamOpen || postStreamOpen) {
            parseRecord();
        }
    }

    public ByteBuffer readRecordHeader() throws IOException {

        ByteBuffer header = ByteBuffer.allocateDirect(8);

        // final byte[] header = new byte[8];
        dataInput.read(header);
        header.flip();
        return header;
    }

    private byte parseRecord() throws IOException {

        final ByteBuffer header = readRecordHeader();

        final byte version = header.get();
        final byte type = header.get();
        final int requestId = readUnsignedShort(header.get(), header.get());
        final int contentLength = readUnsignedShort(header.get(), header.get());
        final int paddingLength = readUnsignedByte(header.get());
        // 1 Reserved byte is not read

        if (version != FCGI_VERSION_1) {
            throw new FCGIException("Bad FCGI version code. Found '" + version + "' but '" + FCGI_VERSION_1 + "' excepted.");
        }

        if (requestId != 1) {
            throw new FCGIException("Bad request ID. Found '" + requestId + "' but '" + 1 + "' excepted.");
        }

        ByteBuffer record = ByteBuffer.allocateDirect(contentLength);
        dataInput.read(record);
        record.flip();

        switch (type) {
            case FCGI_BEGIN_REQUEST:
                parseBeginRequestRecord(contentLength, record);
                break;
            case FCGI_ABORT_REQUEST:
                throw new FCGIException("Not implemented type: " + type);
                // break;
            case FCGI_PARAMS:
                parseParamsRecord(contentLength, record);
                break;
            case FCGI_STDIN:
                parseStdinRecord(contentLength, record);
                break;
            case FCGI_DATA:
                throw new FCGIException("Not implemented type: " + type);
                // break;
            case FCGI_GET_VALUES:
                throw new FCGIException("Not implemented type: " + type);
                // break;
            default:
                throw new FCGIException("Invalid type code for a record: " + type);
        }

        // Skip padding
        skipBytes(paddingLength);
        return type;
    }

    private void skipBytes(int paddingLength) throws IOException {
        ByteBuffer padding = ByteBuffer.allocateDirect(paddingLength);
        dataInput.read(padding);
    }

    private void parseBeginRequestRecord(final int contentLenght, ByteBuffer record) throws IOException {
        if (contentLenght != 8) {
            throw new FCGIException("Bad lenght for begin request record. Found '" + contentLenght + "' but '8' excepted.");
        }

        final short role = record.getShort();
        final byte flags = record.get();
        // Skip 5 reserved bytes

        if ((flags & FCGI_KEEP_CONN) != 0) {
            throw new FCGIException("Keep Connection mode is not implemented");
        }

        if (role != FCGI_RESPONDER) {
            throw new FCGIException("Only FCGI responder role is implemented");
        }

    }

    private void parseParamsRecord(final int contentLength, ByteBuffer record) throws IOException {
        if (contentLength == 0) {
            // End of param stream
            paramStreamOpen = false;
            return;
        }

        int remainingLength = contentLength;

        while (remainingLength > 0) {
            final int usedLength = parseNameValuePair(record);
            if (usedLength > remainingLength) {
                throw new FCGIException("Bad format un params record");
            }
            remainingLength -= usedLength;
        }
    }

    private int parseNameValuePair(ByteBuffer dbuffer) throws IOException {
        int usedLength = 0;
        long nameLength = 0;
        final int firstNameLengthByte = dbuffer.get() & 0xff;
        usedLength++;

        if ((firstNameLengthByte & 0x80) != 0) {// 10000000
            final int[] lengthArray = new int[4];
            lengthArray[3] = firstNameLengthByte;
            lengthArray[2] = dbuffer.get() & 0xff;
            lengthArray[1] = dbuffer.get() & 0xff;
            lengthArray[0] = dbuffer.get() & 0xff;
            usedLength += 3;

            nameLength = unsignedIntToLong(lengthArray);
        } else {
            nameLength = firstNameLengthByte;
        }

        long valueLength = 0;

        final int firstValueLengthByte = dbuffer.get() & 0xff;
        usedLength++;

        if ((firstValueLengthByte >> 7) == 1) { // 10000000
            final int[] lengthArray = new int[4];
            lengthArray[3] = firstValueLengthByte;
            lengthArray[2] = dbuffer.get() & 0xff;
            lengthArray[1] = dbuffer.get() & 0xff;
            lengthArray[0] = dbuffer.get() & 0xff;
            usedLength += 3;

            valueLength = unsignedIntToLong(lengthArray);
        } else {
            valueLength = firstValueLengthByte;
        }

        final byte[] nameArray = new byte[(int) nameLength];
        dbuffer.get(nameArray);
        final String name = new String(nameArray);
        usedLength += nameLength;

        final byte[] valueArray = new byte[(int) valueLength];
        dbuffer.get(valueArray);
        final String value = new String(valueArray);
        usedLength += valueLength;

        headers.put(name, value);

        return usedLength;
    }

    private void parseStdinRecord(final int contentLength, ByteBuffer record) throws IOException {
        if (contentLength == 0) {
            // End of stdin stream
            postStreamOpen = false;
            return;
        }
        postStream.pushData(record);
    }

    /**
     * Converts a 4 byte array of unsigned bytes to an long
     * 
     * @param b an array of 4 unsigned bytes
     * @return a long representing the unsigned int
     */
    private static final long unsignedIntToLong(final int[] b) {
        long l = 0;

        l += b[0];
        l += b[1] << 8;
        l += b[2] << 16;
        l += (b[3] & 0x7F) << 24;

        return l;
    }

    public InputStream getPostStream() {
        return postStream;
    }

    public boolean isPostStreamOpen() {
        return postStreamOpen;
    }

    public static int readUnsignedByte(final byte b) {
        return b & 0xFF;
    }

    public static int readUnsignedShort(final byte b1, final byte b2) {
        final int firstByte = 0x000000FF & b1;
        final int secondByte = 0x000000FF & b2;
        return (firstByte << 8 | secondByte);
    }
}
