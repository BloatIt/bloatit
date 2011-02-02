package com.bloatit.framework.webserver.masters;

import java.io.IOException;
import java.io.OutputStream;

import com.bloatit.common.Log;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.components.writers.IndentedHtmlText;

public final class HttpResponse {

    private final OutputStream output;
    private final IndentedHtmlText htmlText;

    public HttpResponse(final OutputStream output) {
        this.output = output;
        this.htmlText = new IndentedHtmlText(output);
    }

    public void writeException(final Exception e) {
        final StringBuilder display = new StringBuilder();
        display.append("Content-type: text/plain\r\n\r\n");
        display.append(e.toString());
        display.append(" :\n");

        for (final StackTraceElement s : e.getStackTrace()) {
            display.append('\t');
            display.append(s);
            display.append('\n');
        }

        try {
            output.write(display.toString().getBytes());
            output.close();
        } catch (final IOException e1) {
            Log.framework().fatal("Cannot send exception through the SCGI soket.", e1);
        }
    }

    public void writeRedirect(final String url) throws IOException {
        writeCookies();
        output.write("Location: ".getBytes());
        output.write(url.getBytes());
        output.write("\r\n".getBytes());
        closeHeaders();
        output.close();
    }

    public void writePage(final com.bloatit.framework.webserver.masters.Page page) throws IOException {
        writeCookies();
        output.write("Content-Type: text/html\r\n".getBytes());

        closeHeaders();

        page.write(htmlText);
        output.close();
    }

    private void closeHeaders() throws IOException {
        output.write("\r\n".getBytes());
    }

    private void writeCookies() throws IOException {
        output.write("Set-Cookie: session_key=".getBytes());
        output.write(Context.getSession().getKey().toString().getBytes());
        output.write("; path=/; Max-Age=1296000; Version=1 \r\n".getBytes());
    }

}
