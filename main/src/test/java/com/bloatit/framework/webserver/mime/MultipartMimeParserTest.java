/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.framework.webserver.mime;

import org.junit.Test;

/**
 *
 */
public class MultipartMimeParserTest {

    @Test
    public void testOKParsing() {
        // // String toParse =
        // // "-----------------------------112924560112292093081138430544\r\n"
        // +
        // // //
        // // "Content-Disposition: form-data; name=\"wow\"\r\n" + //
        // // "\r\n" + //
        // // "dqsd qsd qsd \r\n" + //
        // // "-----------------------------112924560112292093081138430544\r\n"
        // +
        // // //
        // //
        // //
        // "Content-Disposition: form-data; name=\"fichier\"; filename=\"test.upload\"\r\n"
        // // + //
        // // "Content-Type: application/octet-stream\r\n" + //
        // // "\r\n" + //
        // //
        // //
        // "qsd qd qsd qsd qsd qsd sd qsd qsd z sdsqqqqqqqqqqqq sqd        qsddddddddddddddddd qsd qsssssss\n"
        // // + //
        // // "qsd\nqsd qsabcd \n" + //
        // // "sdq aerf df sdf sa\n\r\n" + //
        // // "-----------------------------112924560112292093081138430544--";
        // //
        //
        // String toParse =
        // "This is a message with multiple parts in MIME format.\r\n" +
        // "--frontier\r\n" + "Content-Type: text/plain\r\n" + "\r\n"
        // + "This is the body of the message.\r\n" + "--frontier\r\n" +
        // "Content-Type: application/octet-stream; filename=\"boo\"\r\n"
        // + "Content-Transfer-Encoding: base64\r\n" + "\r\n" +
        // "PGh0bWw+CiAgPGhlYWQ+CiAgPC9oZWFkPgogIDxib2R5PgogICAgPHA+VGhpcyBpcyB0aGUg"
        // + "Ym9keSBvZiB0aGUgbWVzc2FnZS48L3A+CiAgPC9ib2R5Pgo8L2h0bWw+Cg==\r\n"
        // + "--frontier--";
        //
        // String contentType =
        // "Content-Type: multipart/mixed; boundary=frontier";
        // // String contentType =
        // //
        // "multipart/form-data; boundary=---------------------------112924560112292093081138430544";
        // System.out.println(contentType);
        // System.out.println(toParse);
        // ByteArrayInputStream bais = new
        // ByteArrayInputStream(toParse.getBytes());
        //
        // MultipartMimeParser mmp = new MultipartMimeParser(bais, contentType,
        // new UUIDFileNameGenerator(), UPLOAD_TEMP_DIRECTORY);
        // MimeElement me;
        // while ((me = mmp.readContent()) != null) {
        // System.out.println("CONTENT");
        // System.out.println(me);
        // System.out.println("[ENCODING !! : ]" + me.getEncoding());
        // }
        // System.out.println("DONE");
    }
}
