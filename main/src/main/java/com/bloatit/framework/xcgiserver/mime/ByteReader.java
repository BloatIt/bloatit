package com.bloatit.framework.xcgiserver.mime;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
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
    private final InputStream input;

    public ByteReader(final InputStream input) {
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
    public byte read() throws EOFException, IOException {
        final int i = input.read();
        if (i == -1) {
            throw new EOFException();
        }
        return (byte) i;
    }

    /**
     * <p>
     * Reads a line. Any single <code>'\n'</code> or <code>'\r'</code> will be
     * ignored (they won't even be shown in the byte array returned)
     * </p>
     * <p>
     * If end of stream is reached before a line end is reached, the content
     * will be returned, and considered as a normal line. Next call to readLine
     * will then throw an EOFException indicating the end of the stream has been
     * reached.
     * </p>
     * 
     * @return
     * @throws IOException
     * @throws EOFException
     */
    public byte[] readLine() throws EOFException, IOException {
        boolean end = false;
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        boolean previousWasCR = false;
        while (!end) {
            try {
                final byte b = read();
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
            } catch (final EOFException e) {
                if (baos.size() == 0) {
                    throw new EOFException();
                } else {
                    return baos.toByteArray();
                }
            }
        }
        return baos.toByteArray();
    }

    /**
     * <p>
     * Reads a line (i.e: reads to the next CRLF sequence) and converts data to
     * a string
     * </p>
     * *
     * <p>
     * If end of stream is reached before a line end is reached, the content
     * will be returned, and considered as a normal line. Next call to
     * readString or readLine will then throw an EOFException indicating the end
     * of the stream has been reached.
     * </p>
     * 
     * @return the string representation of the line
     * @throws IOException when the stream is not accessible
     * @see #readLine()
     */
    public String readString() throws EOFException, IOException {
        return new String(readLine());
    }

    /**
     * <p>
     * Returns an estimate of the number of bytes that can be read (or skipped
     * over) from this input stream without blocking by the next invocation of a
     * method for this input stream. The next invocation might be the same
     * thread or another thread. A single read or skip of this many bytes will
     * not block, but may read or skip fewer bytes.
     * </p>
     * 
     * @return an estimate of the number of bytes that can be read (or skipped
     *         over) from this input stream without blocking or 0 when it
     *         reaches the end of the input stream.
     * @throws IOException - if an I/O error occurs.
     */
    public int available() throws IOException {
        return input.available();
    }

    /**
     * Closes this input stream and releases any system resources associated
     * with the stream. The close method of InputStream does nothing.
     * 
     * @throws IOException if an IO error occurs
     */
    public void close() throws IOException {
        input.close();
    }

}
