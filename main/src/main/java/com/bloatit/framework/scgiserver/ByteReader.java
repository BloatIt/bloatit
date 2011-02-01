package com.bloatit.framework.scgiserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * A class allowing to read an input stream as line of bytes.
 * </p>
 * <p>
 * A line of byte end whenever the bytes representing the characters CRLF (\r\n)
 * are found.
 * </p>
 */
public class ByteReader {
    private static final byte CR = (byte) '\r';
    private static final byte LF = (byte) '\n';
    private InputStream input;

    public ByteReader(InputStream input) {
        this.input = input;
    }

    /**
     * <p>
     * Reads the next byte of data from the input stream.
     * </p>
     * <p>
     * The value byte is returned as a byte. If no byte is available because the
     * end of the stream has been reached, the value -1 is returned. This method
     * blocks until input data is available, the end of the stream is detected,
     * or an exception is thrown.
     * </p>
     * 
     * @return
     * @throws IOException
     */
    public byte read() throws IOException {
        return (byte) input.read();
    }

    /**
     * <p>
     * Reads a line. Any single <code>'\n'</code> or <code>'\r'</code> will be
     * ignored (they won't even be shown in the byte array returned)
     * </p>
     * 
     * @return
     * @throws IOException
     */
    public byte[] readLine() throws IOException {
        boolean end = false;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        boolean previousWasCR = false;
        while (!end) {
            if(available() == 0 ){
                throw new IOException();
            }
            byte b = read();
            if (b == CR) {
                previousWasCR = true;
            } else if (b == LF) {
                if (previousWasCR) {
                    end = true;
                }
            } else {
                if (previousWasCR) {
                    baos.write(CR);
                    previousWasCR = false;
                }
                baos.write(b);
            }
        }
        return baos.toByteArray();
    }

    /**
     * <p>
     * Reads a line (i.e: reads to the next CRLF sequence) and converts data to
     * a string
     * </p>
     * 
     * @return the string representation of the line
     * @throws IOException
     *             when the stream is not accessible
     * @see #readLine()
     */
    public String readString() throws IOException {
        return new String(readLine());
    }

    /**
     * Returns an estimate of the number of bytes that can be read (or skipped
     * over) from this input stream without blocking by the next invocation of a
     * method for this input stream. The next invocation might be the same
     * thread or another thread. A single read or skip of this many bytes will
     * not block, but may read or skip fewer bytes. Note that while some
     * implementations of InputStream will return the total number of bytes in
     * the stream, many will not. It is never correct to use the return value of
     * this method to allocate a buffer intended to hold all data in this
     * stream. A subclass' implementation of this method may choose to throw an
     * IOException if this input stream has been closed by invoking the close()
     * method. The available method for class InputStream always returns 0. This
     * method should be overridden by subclasses.
     * 
     * @return an estimate of the number of bytes that can be read (or skipped
     *         over) from this input stream without blocking or 0 when it
     *         reaches the end of the input stream.
     * @throws IOException
     *             - if an I/O error occurs.
     */
    public int available() throws IOException {
        return input.available();
    }

    /**
     * Closes this input stream and releases any system resources associated
     * with the stream. The close method of InputStream does nothing.
     * 
     * @throws IOException
     *             if an IO error occurs
     */
    public void close() throws IOException {
        input.close();
    }

}
