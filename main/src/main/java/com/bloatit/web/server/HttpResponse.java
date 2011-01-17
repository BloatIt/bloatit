package com.bloatit.web.server;

import java.io.IOException;
import java.io.OutputStream;

import com.bloatit.web.html.pages.master.Page;
import com.bloatit.web.scgiserver.SCGIRequestAbordedException;

public final class HttpResponse {

    private final OutputStream output;

    public HttpResponse(final OutputStream output) {
        this.output = output;
    }

    public void writeRedirect(final String url) throws IOException {
        writeCookies();

        output.write("Location: ".getBytes());
        output.write(url.getBytes());
        output.write("\r\n".getBytes());

        closeHeaders();
    }

    public void writePage(final Page page) throws IOException {
        writeCookies();
        output.write("Content-Type: text/html\r\n".getBytes());

        closeHeaders();

        page.write(new IndentedHtmlText() {
            @Override
            protected void append(final String text) {
                try {
                    output.write(text.getBytes());
                } catch (final IOException ex) {
                    throw new SCGIRequestAbordedException(ex);
                }
            }
        });

    }

    private void closeHeaders() throws IOException {
        output.write("\r\n".getBytes());
    }

    private void writeCookies() throws IOException {
        output.write("Set-Cookie: session_key=".getBytes());
        output.write(Context.getSession().getKey().getBytes());
        output.write("; path=/; Max-Age=1296000; Version=1 \r\n".getBytes());
    }
}
