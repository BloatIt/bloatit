package com.bloatit.framework.xcgiserver.fcgi;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FCGIOutputStream extends OutputStream {

    final static byte FCGI_REQUEST_COMPLETE = 0;
    final static byte FCGI_CANT_MPX_CONN = 1;
    final static byte FCGI_OVERLOADED = 2;
    final static byte FCGI_UNKNOWN_ROLE = 3;

    private final DataOutputStream outputStream;
    private final FCGIParser fcgiParser;

    public FCGIOutputStream(FCGIParser fcgiParser, OutputStream outputStream) {
        this.fcgiParser = fcgiParser;
        this.outputStream = new DataOutputStream(outputStream);

    }

    @Override
    public void write(int b) throws IOException {
        byte[] bArray = new byte[1];
        bArray[0] = (byte) b;
        sendStdoutRecord(bArray, 0, 1);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        sendStdoutRecord(b, off, len);
    }

    @Override
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    @Override
    public void close() throws IOException {
        // Close stdout stream
        fcgiParser.fetchAll();
        byte[] b = new byte[0];
        sendStdoutRecord(b, 0, 0);
        sendEndRecord();
        outputStream.close();
    }

    private void sendEndRecord() throws IOException {
        sendRecordHeader(FCGIParser.FCGI_END_REQUEST, 8);

        // Write end record content
        // App state
        outputStream.writeInt(0);
        // Protocol status
        outputStream.writeByte(FCGI_REQUEST_COMPLETE);

        // 3 reserved bytes
        outputStream.writeByte(0);
        outputStream.writeByte(0);
        outputStream.writeByte(0);

    }

    private void sendStdoutRecord(byte[] b, int off, int len) throws IOException {
        sendRecordHeader(FCGIParser.FCGI_STDOUT, len);

        outputStream.write(b, off, len);
    }

    private void sendRecordHeader(byte fcgiType, int length) throws IOException {
        // FCGI version
        outputStream.writeByte(FCGIParser.FCGI_VERSION_1);
        // FCGI record type
        outputStream.writeByte(fcgiType);
        // request id : 1
        outputStream.writeShort(1);
        // End recod Length
        outputStream.writeShort(length);
        // No padding; length : 0
        outputStream.writeByte(0);
        // Reserved
        outputStream.writeByte(0);

    }

}
