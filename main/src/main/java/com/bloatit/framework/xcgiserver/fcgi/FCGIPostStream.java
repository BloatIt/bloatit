/*
 * Copyright (C) 2011 Linkeos.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General
 * Public License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.framework.xcgiserver.fcgi;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class FCGIPostStream extends InputStream {

    PipedInputStream pipeIn;
    PipedOutputStream pipeOut;
    private final FCGIParser fcgiParser;

    public FCGIPostStream(FCGIParser fcgiParser) throws IOException {
        this.fcgiParser = fcgiParser;
        pipeIn = new PipedInputStream(66000);
        pipeOut = new PipedOutputStream(pipeIn);
    }

    @Override
    public int read() throws IOException {
        byte[] b = new byte[1];
        if (read(b) == 1) {
            int plop =  (0x000000FF & (b[0]));
            if(plop == -1) {
                System.err.println("blabla");
            }
            return plop;
        }
        return -1;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (len == 0) {
            return 0;
        }
        if (pipeIn.available() == 0) {
            if (fcgiParser.isPostStreamOpen()) {
                fcgiParser.fetchPostRecord();
            } else {
                return -1;
            }
            if (!fcgiParser.isPostStreamOpen() || pipeIn.available() == 0) {
                return -1;
            }
        }
        int plop = pipeIn.read(b, off, len);
        if(plop == -1){
            System.out.println("PLOP");
        }
        return plop;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    public void pushData(byte[] data) throws IOException {
        pipeOut.write(data);
    }
}
