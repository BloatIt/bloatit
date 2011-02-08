package com.bloatit.framework.xcgiserver.mime.decoders;

/**
 * <p>
 * Common interface used to provide tools to encode and decode data based on
 * various encoding methods of the <a
 * href="http://tools.ietf.org/html/rfc2045">RFC 2045</a>
 * </p>
 */
public interface MimeDecoder {
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
     * When using this method, one should try to provide at least
     * {@link #decodeStep()} bytes at once. If not possible, the decode method
     * will be performed using dummy data resulting in possibly erroneous
     * output.
     * </p>
     * 
     * @param b
     *            the array of bytes to decode
     * @param offset
     *            the first byte to decode, bytes before offset will be ignored
     * @param length
     *            the number of byte to decode, bytes after <code>off</code>+
     *            <code>length</code> will be ignored
     * @return the array of decoded bytes.
     * @throws IndexOutOfBoundsException
     *             If <code>off</code> is negative, or <code>length</code> is
     *             negative, or <code>off</code>+<code>length</code> is greater
     *             than the length of the array <code>b</code>
     * @throws NullPointerException
     *             If <code>b</code> is null
     * @throws IllegalArgumentException
     *             If some of the data in <code>b</code> shouldn't be provided
     *             for the implemented decoding algorithm
     */
    public byte[] decode(byte[] b, int offset, int length);

    /**
     * <p>
     * Gives the minimal amount of bytes handled at once to perform decoding.
     * </p>
     * <p>
     * If there is no such thing as a minimal amount of bytes to perform
     * decoding, returns 0.
     * </p>
     * 
     * @return the amount of byte to turn in to perform decoding, or
     *         <code>0</code> if there is no minimum number of bytes to turn in
     */
    public int decodeStep();
}
