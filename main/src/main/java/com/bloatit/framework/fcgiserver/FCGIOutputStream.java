package com.bloatit.framework.fcgiserver;
/*
 * @(#)FCGIOutputStream.java
 *
 *      FastCGi compatibility package Interface
 *
 *  Copyright (c) 1996 Open Market, Inc.
 *
 * See the file "LICENSE.TERMS" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 *
 * $Id: FCGIOutputStream.java,v 1.3 2000/03/21 12:12:26 robs Exp $
 */

import java.io.EOFException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This stream understands FCGI prototcol.
 */
public class FCGIOutputStream extends OutputStream {
    /* Stream vars */
    public int wrNext;
    public int stop;
    public boolean isClosed;

    /* require methods to set, get and clear */
    private int errno;
    private Exception errex;

    /* data vars */
    public byte buff[];
    public int buffLen;
    public int buffStop;
    public int type;
    public boolean isAnythingWritten;
    public boolean rawWrite;
    public FCGIRequest request;

    public FileOutputStream out;

    /**
     * Creates a new output stream to manage fcgi prototcol stuff
     * 
     * @param out
     *            the output stream buflen length of buffer streamType
     */
    public FCGIOutputStream(FileOutputStream outStream, int bufLen, int streamType, FCGIRequest inreq) {
        out = outStream;
        buffLen = Math.min(bufLen, FCGIGlobalDefs.FCGI_MAX_LEN);
        buff = new byte[buffLen];
        type = streamType;
        stop = buffStop = buffLen;
        isAnythingWritten = false;
        rawWrite = false;
        wrNext = FCGIGlobalDefs.FCGI_HEADER_LEN;
        isClosed = false;
        request = inreq;
    }

    /**
     * Writes a byte to the output stream.
     */
    public void write(int c) throws IOException {
        if (wrNext != stop) {
            buff[wrNext++] = (byte) c;
            return;
        }
        if (isClosed) {
            throw new EOFException();
        }
        empty(false);
        if (wrNext != stop) {
            buff[wrNext++] = (byte) c;
            return;
        }
        /* NOTE: ASSERT(stream->isClosed); */
        /* bug in emptyBuffProc if not */
        throw new EOFException();
    }

    /**
     * Writes an array of bytes. This method will block until the bytes are
     * actually written.
     * 
     * @param b
     *            the data to be written
     */
    public void write(byte b[]) throws IOException {
        write(b, 0, b.length);
    }

    /**
     * Writes len consecutive bytes from off in the array b into the output
     * stream. Performs no interpretation of the output bytes. Making the user
     * convert the string to bytes is in line with current Java practice.
     */
    public void write(byte b[], int off, int len) throws IOException {
        int m, bytesMoved;
        /*
         * Fast path: room for n bytes in the buffer
         */
        if (len <= (stop - wrNext)) {
            System.arraycopy(b, off, buff, wrNext, len);
            wrNext += len;
            return;
        }
        /*
         * General case: stream is closed or buffer empty procedure needs to be
         * called
         */
        bytesMoved = 0;
        for (;;) {
            if (wrNext != stop) {
                m = Math.min(len - bytesMoved, stop - wrNext);
                System.arraycopy(b, off, buff, wrNext, m);
                bytesMoved += m;
                wrNext += m;
                if (bytesMoved == len) {
                    return;
                }
                off += m;
            }
            if (isClosed) {
                throw new EOFException();
            }
            empty(false);
        }
    }

    /**
     * Encapsulates any buffered stream content in a FastCGI record. If
     * !doClose, writes the data, making the buffer empty.
     */
    public void empty(boolean doClose) throws IOException {
        int cLen;
        /*
         * Alignment padding omitted in Java
         */
        if (!rawWrite) {
            cLen = wrNext - FCGIGlobalDefs.FCGI_HEADER_LEN;
            if (cLen > 0) {
                System.arraycopy(new FCGIMessage().makeHeader(type, request.requestID, cLen, 0), 0, buff, 0, FCGIGlobalDefs.FCGI_HEADER_LEN);
            } else {
                wrNext = 0;
            }
        }
        if (doClose) {
            writeCloseRecords();
        }
        if (wrNext != 0) {
            isAnythingWritten = true;
            try {
                out.write(buff, 0, wrNext);
            } catch (IOException e) {
                setException(e);
                return;
            }
            wrNext = 0;
        }
        /*
         * The buffer is empty.
         */
        if (!rawWrite) {
            wrNext += FCGIGlobalDefs.FCGI_HEADER_LEN;
        }
    }

    /**
     * Close the stream.
     */
    public void close() throws IOException {
        if (isClosed) {
            return;
        }
        empty(true);
        /*
         * if isClosed, will return with EOFException from write.
         */
        isClosed = true;
        stop = wrNext;
        return;
    }

    /**
     * Flushes any buffered output. Server-push is a legitimate application of
     * flush. Otherwise, it is not very useful, since FCGIAccept does it
     * implicitly. flush may reduce performance by increasing the total number
     * of operating system calls the application makes.
     */
    public void flush() throws IOException {
        if (isClosed) {
            return;
        }
        empty(false);
        /*
         * if isClosed, will return with EOFException from write.
         */
        return;
    }

    /**
     * An FCGI error has occurred. Save the error code in the stream for
     * diagnostic purposes and set the stream state so that reads return EOF
     */
    public void setFCGIError(int errnum) {
        /*
         * Preserve only the first error.
         */
        if (errno == 0) {
            errno = errnum;
        }
        isClosed = true;
    }

    /**
     * An Exception has occurred. Save the Exception in the stream for
     * diagnostic purposes and set the stream state so that reads return EOF
     */
    public void setException(Exception errexpt) {
        /*
         * Preserve only the first error.
         */
        if (errex == null) {
            errex = errexpt;
        }
        isClosed = true;
    }

    /**
     * Clear the stream error code and end-of-file indication.
     */
    public void clearFCGIError() {
        errno = 0;
        /*
         * isClosed = false; XXX: should clear isClosed but work is needed to
         * make it safe to do so.
         */
    }

    /**
     * Clear the stream error code and end-of-file indication.
     */
    public void clearException() {
        errex = null;
        /*
         * isClosed = false; XXX: should clear isClosed but work is needed to
         * make it safe to do so.
         */
    }

    /**
     * accessor method since var is private
     */
    public int etFCGIError() {
        return errno;
    }

    /**
     * accessor method since var is private
     */
    public Exception getException() {
        return errex;
    }

    /**
     * Writes an EOF record for the stream content if necessary. If this is the
     * last writer to close, writes an FCGI_END_REQUEST record.
     */
    public void writeCloseRecords() throws IOException {
        FCGIMessage msg = new FCGIMessage();
        /*
         * Enter rawWrite mode so final records won't be encapsulated as stream
         * data.
         */
        rawWrite = true;
        /*
         * Generate EOF for stream content if needed.
         */
        if (!(type == FCGIGlobalDefs.FCGI_STDERR && wrNext == 0 && !isAnythingWritten)) {
            byte hdr[] = new byte[FCGIGlobalDefs.FCGI_HEADER_LEN];
            System.arraycopy(msg.makeHeader(type, request.requestID, 0, 0), 0, hdr, 0, FCGIGlobalDefs.FCGI_HEADER_LEN);
            write(hdr, 0, hdr.length);
        }
        /*
         * Generate FCGI_END_REQUEST record if needed.
         */
        if (request.numWriters == 1) {
            byte endReq[] = new byte[FCGIGlobalDefs.FCGI_HEADER_LEN + FCGIGlobalDefs.FCGI_END_REQ_BODY_LEN];
            System.arraycopy(msg.makeHeader(FCGIGlobalDefs.FCGI_END_REQUEST, request.requestID, FCGIGlobalDefs.FCGI_END_REQ_BODY_LEN, 0), 0,
                    endReq, 0, FCGIGlobalDefs.FCGI_HEADER_LEN);
            System.arraycopy(msg.makeEndrequestBody(request.appStatus, FCGIGlobalDefs.FCGI_REQUEST_COMPLETE), 0, endReq,
                    FCGIGlobalDefs.FCGI_HEADER_LEN, FCGIGlobalDefs.FCGI_END_REQ_BODY_LEN);
            write(endReq, 0, FCGIGlobalDefs.FCGI_HEADER_LEN + FCGIGlobalDefs.FCGI_END_REQ_BODY_LEN);
        }
        request.numWriters--;
    }
}
