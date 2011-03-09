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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FCGIOutputStream extends OutputStream {

    private static final int WAIT_BUFFER_SIZE = 60000;
    private static final int PREPARE_BUFFER_SIZE = 65000;
    final static byte FCGI_REQUEST_COMPLETE = 0;
    final static byte FCGI_CANT_MPX_CONN = 1;
    final static byte FCGI_OVERLOADED = 2;
    final static byte FCGI_UNKNOWN_ROLE = 3;

    // private final DataOutputStream outputStream;
    private final FCGIParser fcgiParser;
    private final DataOutputStream prepareStream;
    private final ByteArrayOutputStream prepareBuffer;
    private final ByteArrayOutputStream waitBuffer;
    private final OutputStream outputStream;

    public FCGIOutputStream(final FCGIParser fcgiParser, final OutputStream outputStream) {
        this.fcgiParser = fcgiParser;
        this.outputStream = outputStream;
        prepareBuffer = new ByteArrayOutputStream(PREPARE_BUFFER_SIZE);
        waitBuffer = new ByteArrayOutputStream(WAIT_BUFFER_SIZE);

        this.prepareStream = new DataOutputStream(prepareBuffer);

    }

    @Override
    public void write(final int b) throws IOException {
        final byte[] bArray = new byte[1];
        bArray[0] = (byte) b;
        writeBytes(bArray, 0, 1);
    }

    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        writeBytes(b, off, len);
    }

    @Override
    public void write(final byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    @Override
    public void close() throws IOException {
        // Close stdout stream
        fcgiParser.fetchAll();
        sendStdoutRecord();
        sendEndRecord();
        outputStream.close();
    }

    private void writeBytes(final byte[] b, final int off, final int len) throws IOException {

        int wroteLen = 0;

        while (wroteLen < len) {

            int currentOffset = off + wroteLen;
            int lenToWrite = len - wroteLen;

            // Check if left size in next record
            int leftSize = WAIT_BUFFER_SIZE - waitBuffer.size();
            if (leftSize > 0) {
                // Check if there too bytes for the current record
                if (lenToWrite <= leftSize) {
                    waitBuffer.write(b, currentOffset, lenToWrite);
                    wroteLen += len;
                } else {
                    waitBuffer.write(b, currentOffset, leftSize);
                    wroteLen += leftSize;
                }
            }

            sendStdoutRecordIfNeeded();
        }

    }

    private void sendStdoutRecordIfNeeded() throws IOException {
        int leftSize = WAIT_BUFFER_SIZE - waitBuffer.size();
        if (leftSize <= 0) {
            sendStdoutRecord();
        }
    }

    private void sendStdoutRecord() throws IOException {
        if (waitBuffer.size() > 0) {
            sendRecordHeader(FCGIParser.FCGI_STDOUT, waitBuffer.size());
            prepareStream.write(waitBuffer.toByteArray(), 0, waitBuffer.size());

            flushRecord();
        }
    }

    private void flushRecord() throws IOException {
        // Write record in the socket
        outputStream.write(prepareBuffer.toByteArray());

        prepareBuffer.reset();
        waitBuffer.reset();
    }

    private void sendRecordHeader(final byte fcgiType, final int length) throws IOException {
        // FCGI version
        prepareStream.writeByte(FCGIParser.FCGI_VERSION_1);
        // FCGI record type
        prepareStream.writeByte(fcgiType);
        // request id : 1
        prepareStream.writeShort(1);
        // End recod Length
        prepareStream.writeShort(length);
        // No padding; length : 0
        prepareStream.writeByte(0);
        // Reserved
        prepareStream.writeByte(0);

    }

    private void sendEndRecord() throws IOException {
        sendRecordHeader(FCGIParser.FCGI_END_REQUEST, 8);

        // Write end record content
        // App state
        prepareStream.writeInt(0);
        // Protocol status
        prepareStream.writeByte(FCGI_REQUEST_COMPLETE);

        // 3 reserved bytes
        prepareStream.writeByte(0);
        prepareStream.writeByte(0);
        prepareStream.writeByte(0);

        flushRecord();

    }

}
