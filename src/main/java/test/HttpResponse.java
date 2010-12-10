package test;

import com.sun.org.apache.xalan.internal.xsltc.trax.OutputSettings;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.search.FieldComparator.StringOrdValComparator;
import test.pages.master.Page;

public class HttpResponse {

    OutputStreamWriter outputWriter;

    public HttpResponse(OutputStream output) {
        outputWriter = new OutputStreamWriter(output);
    }

    public void writeRedirect(String url) throws IOException {
        writeCookies();

        outputWriter.write("Location: ");
        outputWriter.write(url);
        outputWriter.write("\r\n");

        closeHeaders();
    }

    public void writePage(Page page) throws IOException {
        writeCookies();
        outputWriter.write("Content-Type: text/html\r\n");

        page.write(new IndentedHtmlText() {

            @Override
            protected void append(String text) {
                try {
                    outputWriter.write(text);
                } catch (IOException ex) {
                    //TODO: log
                }
            }
        });

        closeHeaders();
    }


    private void closeHeaders() throws IOException {
        outputWriter.write("\r\n");
    }

    private void writeCookies() throws IOException {
        outputWriter.write("Set-Cookie: session_key=");
        outputWriter.write(Context.getSession().getKey());
        outputWriter.write("; path=/; Max-Age=1296000; Version=1 \r\n");
    }
}
