package com.bloatit.framework.webserver.mime.decoders;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.bloatit.framework.FrameworkTestUnit;
import com.bloatit.framework.xcgiserver.mime.DecodingOuputStream;
import com.bloatit.framework.xcgiserver.mime.decoders.MimeBase64Decoder;

public class MimeBase64DecoderTest extends FrameworkTestUnit {

    public void testOKBase64() {
        String b64 = "VW4gYWxwaGFiZXQgZGUgNjUgY2FyYWN0w6hyZXMgZXN0IHV0aWxpc8OpIHBvdXIgcGVybWV0dHJlI"
                + "GxhIHJlcHLDqXNlbnRhdGlvbiBkZSA2IGJpdHMgcGFyIHVuIGNhcmFjdMOocmUgc2ltcGxlLiBMZSA2NWUgY2"
                + "FyYWN0w6hyZSAoc2lnbmUgJz0nKSBuJ2VzdCB1dGlsaXPDqSBxdSdlbiBjb21wbMOpbWVudCBmaW5hbCBkY"
                + "W5zIGxlIHByb2Nlc3N1cyBkZSBjb2RhZ2UgZCd1biBtZXNzYWdlLg==";

        String decoded = "Un alphabet de 65 caractères est utilisé pour permettre la représentation de "
                + "6 bits par un caractère simple. Le 65e caractère (signe '=') n'est utilisé qu'en "
                + "complément final dans le processus de codage d'un message.";

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        MimeBase64Decoder codec = new MimeBase64Decoder();
        DecodingOuputStream dos = new DecodingOuputStream(output, codec);
        try {
            dos.write(b64.getBytes());
            dos.flush();
            assertTrue(output.toString().equals(decoded));
        } catch (IOException e) {
            fail();
        }
    }

    public void testFailBase64() {
        String b64 = "VW4gYWxwaGFiZXQgZGUgNjUgY2FyYWN0w6hyZXMgZXN0IHV0aWxpc8OpIHBvdXIgcGVybWV0dHJlI"
                + "GxhIHJlcHLDqXNlbnRhdGlvbiBkZSA2IGJpdHMgcGFyIHVuIGNhcmFjdMOocmUgc2ltcGxlLiBMZSA2NWUgY2"
                + "FyYWN0w6hyZSAoc2lnbmUgJz0nKSBuJ2VzdCB1dGlsaXPDqSBxdSdlbiBjb21wbMOpbWVudCBmaW5hbCBkY"
                + "W5zIGxlIHByb2Nlc3N1cyBkZSBjb2RhZ2UgZCd1biBtZXNzYWdlL==";

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        MimeBase64Decoder codec = new MimeBase64Decoder();
        DecodingOuputStream dos = new DecodingOuputStream(output, codec);
        try {
            dos.write(b64.getBytes());
            dos.flush();
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (IOException e) {
            fail();
        }
    }

}
