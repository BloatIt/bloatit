//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.framework.xcgiserver.mime;

import java.io.IOException;
import java.io.OutputStream;

import com.bloatit.framework.xcgiserver.mime.decoders.MimeDecoder;

/**
 * <p>
 * An output stream that will decode text (using MimeDecoder) before it is
 * written.
 * </p>
 */
public class DecodingOuputStream extends OutputStream {
    private static final int DEFAULT_BUFFER_MULTIPLY = 1024;
    private final MimeDecoder codec;
    private final OutputStream output;
    private final int bufferSize;
    private final byte[] buffer;
    private int bufferIndex;

    /**
     * <p>
     * Creates a new DecodingOutputStream with a default buffer size.
     * </p>
     * 
     * @param output The stream used to write the decoded text
     * @param codec The codec used to decode the text
     */
    public DecodingOuputStream(final OutputStream output, final MimeDecoder codec) {
        this(output, codec, DEFAULT_BUFFER_MULTIPLY);
    }

    /**
     * <p>
     * Creates a new DecodingOutputStream with a given buffer size.
     * </p>
     * 
     * @param output The stream used to write the decoded text
     * @param codec The codec used to decode the text
     * @param bufferMultiply The multiplicator used to compute the buffer size.
     *            To find the real bufferSize, you need to do
     *            <code> {@link MimeDecoder#decodeStep()}</code>*
     *            <code>bufferMultiply</code>
     */
    private DecodingOuputStream(final OutputStream output, final MimeDecoder codec, final int bufferMultiply) {
        super();
        this.output = output;
        this.codec = codec;
        this.bufferSize = bufferMultiply;
        buffer = new byte[bufferMultiply * codec.decodeStep()];
    }

    @Override
    public void close() throws IOException {
        flush();
        output.close();
    }

    @Override
    public void flush() throws IOException {
        output.write(codec.decode(buffer, 0, bufferIndex));
        bufferIndex = 0;
        output.flush();
    }

    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        for (int i = off; i < len; i++) {
            write(b[i]);
        }
    }

    @Override
    public void write(final byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    @Override
    public void write(final int b) throws IOException {
        write((byte) b);
    }

    /**
     * <p>
     * Writes one byte to the stream.
     * </p>
     * <p>
     * This method has the same result as calling the {@link #write(int)}
     * method, except it will result in VERY slightly less overehead so should
     * be prefered ...
     * </p>
     * 
     * @param b the byte to write
     * @throws IOException when an IO error occurs
     */
    private void write(final byte b) throws IOException {
        if (bufferIndex >= bufferSize) {
            output.write(codec.decode(buffer, 0, bufferIndex));
            bufferIndex = 0;
        }
        buffer[bufferIndex++] = b;
    }
}
