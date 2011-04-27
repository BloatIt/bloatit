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
package com.bloatit.framework.xcgiserver.mime.decoders;

import java.util.Arrays;

/**
 * <p>
 * A blank decoder
 * </p>
 * <p>
 * One byte of encoded data => the same byte of decoded data
 * </p>
 */
public class MimeBinaryDecoder implements MimeDecoder {

    @Override
    public byte[] decode(final byte[] b, final int offset, final int length) {
        return Arrays.copyOfRange(b, offset, offset + length);
    }

    @Override
    public int decodeStep() {
        return 1;
    }

}
