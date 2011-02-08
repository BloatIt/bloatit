package com.bloatit.framework.webserver.mime.decoders;

import java.io.FileOutputStream;
import java.io.IOException;

import com.bloatit.framework.xcgiserver.mime.DecodingOuputStream;
import com.bloatit.framework.xcgiserver.mime.decoders.MimeBase64Decoder;

public class TestMimeBase64Decoder {

    public static void main(String[] args) throws IOException {
        String b64 = "VW4gYWxwaGFiZXQgZGUgNjUgY2FyYWN0w6hyZXMgZXN0IHV0aWxpc8OpIHBvdXIgcGVybWV0dHJlI"
                + "GxhIHJlcHLDqXNlbnRhdGlvbiBkZSA2IGJpdHMgcGFyIHVuIGNhcmFjdMOocmUgc2ltcGxlLiBMZSA2NWUgY2"
                + "FyYWN0w6hyZSAoc2lnbmUgJz0nKSBuJ2VzdCB1dGlsaXPDqSBxdSdlbiBjb21wbMOpbWVudCBmaW5hbCBkY"
                + "W5zIGxlIHByb2Nlc3N1cyBkZSBjb2RhZ2UgZCd1biBtZXNzYWdlLg==";

        //ByteArrayOutputStream output = new ByteArrayOutputStream();
        FileOutputStream output = new FileOutputStream("/home/yoann/plop");
        MimeBase64Decoder codec = new MimeBase64Decoder();
        DecodingOuputStream dos = new DecodingOuputStream(output, codec);
        dos.write(b64.getBytes());
        dos.flush();

        //System.out.println(new String(output.toByteArray()));
    }
}
