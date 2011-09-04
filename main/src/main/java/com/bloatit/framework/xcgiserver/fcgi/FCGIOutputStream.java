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
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

public class FCGIOutputStream extends OutputStream {

    private static final int WAIT_BUFFER_SIZE = 60000;
    private final static byte FCGI_REQUEST_COMPLETE = 0;
    final static byte FCGI_CANT_MPX_CONN = 1;
    final static byte FCGI_OVERLOADED = 2;
    final static byte FCGI_UNKNOWN_ROLE = 3; // NO_UCD

    private final FCGIParser fcgiParser;
    private final byte[] waitBuffer;
    private int waitOffset;
    private final ByteChannel outputStream;

    protected FCGIOutputStream(final FCGIParser fcgiParser, final ByteChannel channel) {
        this.fcgiParser = fcgiParser;
        this.outputStream = channel;
        waitBuffer = new byte[WAIT_BUFFER_SIZE];
        waitOffset = 0;
    }

    @Override
    public void write(final int b) throws IOException {
        final byte[] bArray = new byte[1];
        bArray[0] = (byte) b;
        write(bArray, 0, 1);
    }

    @Override
    public void write(final byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    @Override
    public void close() throws IOException {
        // Close stdout stream
        fcgiParser.fetchAll();
        if (waitOffset > 0) {
            sendStdoutRecord();
        }
        // EOF stream
        sendStdoutRecord();
        try {
        sendEndRecord();
        outputStream.close();
        } catch( IOException e){
            // Normal case: the fcgi server close quickly the connection after the end of the out stream
        }
    }

    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {

        if (waitOffset + len < WAIT_BUFFER_SIZE) {
            System.arraycopy(b, off, waitBuffer, waitOffset, len);
            waitOffset += len;
            return;
        }

        int writeOffset = 0;
        while (writeOffset < len) {
            // Complete the wait buffer
            final int leftSize = WAIT_BUFFER_SIZE - waitOffset;
            final int sizeTowrite = len - writeOffset;
            final int minSize = Math.min(leftSize, sizeTowrite);

            System.arraycopy(b, off + writeOffset, waitBuffer, waitOffset, minSize);

            writeOffset += minSize;
            waitOffset += minSize;

            if (waitOffset == WAIT_BUFFER_SIZE) {
                sendStdoutRecord();
            }
        }
    }

    private void sendStdoutRecord() throws IOException {
        sendRecordHeader(FCGIParser.FCGI_STDOUT, waitOffset);
        outputStream.write(ByteBuffer.wrap(waitBuffer, 0, waitOffset));
        waitOffset = 0;
    }

    private void sendRecordHeader(final byte fcgiType, final int length) throws IOException {

        ByteBuffer buffer = ByteBuffer.allocateDirect(8);

        // FCGI version
        buffer.put(FCGIParser.FCGI_VERSION_1);
        // FCGI record type
        buffer.put(fcgiType);
        // request id : 1
        buffer.putShort((short) 1);
        // End recod Length
        buffer.putShort((short) length);
        // No padding; length : 0
        buffer.put((byte) 0);
        // Reserved
        buffer.put((byte) 0);

        // Write buffer
        buffer.flip();
        outputStream.write(buffer);
    }

    private void sendEndRecord() throws IOException {
        sendRecordHeader(FCGIParser.FCGI_END_REQUEST, 8);

        ByteBuffer buffer = ByteBuffer.allocateDirect(8);

        // Write end record content
        // App state
        buffer.putInt(0);
        // Protocol status
        buffer.put(FCGI_REQUEST_COMPLETE);

        // 3 reserved bytes
        buffer.put((byte) 0);
        buffer.put((byte) 0);
        buffer.put((byte) 0);

        // Write buffer
        buffer.flip();
        outputStream.write(buffer);
    }
}
