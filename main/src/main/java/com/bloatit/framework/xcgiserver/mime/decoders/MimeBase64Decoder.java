/*
 * Copyright 2003-2010 Christian d'Heureuse, Inventec Informatik AG, Zurich,
 * Switzerland www.source-code.biz, www.inventec.ch/chdh
 * 
 * This module is multi-licensed and may be used under the terms of any of the
 * following licenses:
 * 
 * EPL, Eclipse Public License, V1.0 or later, http://www.eclipse.org/legal
 * LGPL, GNU Lesser General Public License, V2.1 or later,
 * http://www.gnu.org/licenses/lgpl.html GPL, GNU General Public License, V2 or
 * later, http://www.gnu.org/licenses/gpl.html AL, Apache License, V2.0 or
 * later, http://www.apache.org/licenses BSD, BSD License,
 * http://www.opensource.org/licenses/bsd-license.php
 * 
 * Please contact the author if you need another license. This module is
 * provided "as is", without warranties of any kind.
 */
package com.bloatit.framework.xcgiserver.mime.decoders;

/**
 * Decoder that takes data encoded in base64 and returns the data in non base64
 * binary form
 */
public class MimeBase64Decoder implements MimeDecoder {

    /**
     * Mapping table from 6-bit nibbles to Base64 characters.
     */
    private static char[] encodeMap = new char[64];
    static {
        int i = 0;
        for (char c = 'A'; c <= 'Z'; c++) {
            encodeMap[i++] = c;
        }
        for (char c = 'a'; c <= 'z'; c++) {
            encodeMap[i++] = c;
        }
        for (char c = '0'; c <= '9'; c++) {
            encodeMap[i++] = c;
        }
        encodeMap[i++] = '+';
        encodeMap[i++] = '/';
    }

    /**
     * Mapping table from Base64 characters to 6-bit nibbles.
     */
    private static byte[] decodeMap = new byte[128];
    static {
        for (int i = 0; i < decodeMap.length; i++) {
            decodeMap[i] = -1;
        }
        for (int i = 0; i < 64; i++) {
            decodeMap[encodeMap[i]] = (byte) i;
        }
    }

    /**
     * <p>
     * Decodes byte from an array of bytes using a decoding algorithm
     * </p>
     * <p>
     * Any bytes not included in the range <code>off</code> -> <code>off</code>+
     * <code>length</code> are not included. Therefore the array has exactly the
     * <b>size of its useful content</b>
     * </p>
     * <p>
     * <b> Note :</b> the size of the result array will most likely not be equal
     * to the size of the input array
     * </p>
     * <p>
     * When using this method, one should provide a multiple of exactly 4 bytes
     * of data, or an exception will be thrown.
     * </p>
     * <p>
     * Based on the algorithm from <a
     * href="http://www.source-code.biz/base64coder/java/"
     * >http://www.source-code.biz/base64coder/java/</a>
     * </p>
     * 
     * @param b the array of bytes to decode
     * @param offset the first byte to decode, bytes before offset will be
     *            ignored
     * @param length the number of byte to decode, bytes after <code>off</code>+
     *            <code>length</code> will be ignored
     * @return the array of decoded bytes.
     * @throws IndexOutOfBoundsException If <code>off</code> is negative, or
     *             <code>length</code> is negative, or <code>off</code>+
     *             <code>length</code> is greater than the length of the array
     *             <code>b</code>
     * @throws NullPointerException If <code>b</code> is null
     * @throws IllegalArgumentException If some of the data in <code>b</code>
     *             shouldn't be provided for the implemented decoding algorithm
     */
    @Override
    public byte[] decode(final byte[] input, final int offset, int length) {
        if (length % 4 != 0) {
            throw new IllegalArgumentException("Length of Base64 encoded input string is not a multiple of 4.");
        }
        while (length > 0 && input[offset + length - 1] == '=') {
            // Remove the '=' from the decoded string
            length--;
        }
        final int outputLen = (length * 3) / 4;
        final byte[] out = new byte[outputLen];
        final int decodeEnd = offset + length;
        int i = offset; // Input index
        int j = 0; // Output index
        while (i < decodeEnd) {
            final int i0 = input[i++];
            final int i1 = input[i++];
            final int i2 = i < decodeEnd ? input[i++] : 'A';
            final int i3 = i < decodeEnd ? input[i++] : 'A';
            if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127) {
                throw new IllegalArgumentException("Illegal character in Base64 encoded data");
            }

            final int b0 = decodeMap[i0];
            final int b1 = decodeMap[i1];
            final int b2 = decodeMap[i2];
            final int b3 = decodeMap[i3];
            if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0) {
                throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
            }

            final int decoded0 = (b0 << 2) | (b1 >>> 4);
            final int decoded1 = ((b1 & 0xf) << 4) | (b2 >>> 2);
            final int decoded2 = ((b2 & 3) << 6) | b3;
            out[j++] = (byte) decoded0;
            if (j < outputLen) {
                out[j++] = (byte) decoded1;
            }
            if (j < outputLen) {
                out[j++] = (byte) decoded2;
            }
        }
        return out;
    }

    @Override
    public int decodeStep() {
        return 4;
    }
}
