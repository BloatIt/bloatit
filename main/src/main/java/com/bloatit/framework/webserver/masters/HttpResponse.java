package com.bloatit.framework.webserver.masters;

import java.io.IOException;
import java.io.OutputStream;

import com.bloatit.common.Log;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.components.writers.IndentedHtmlStream;
import com.bloatit.rest.RestResource;

public final class HttpResponse {
    public enum StatusCode {
        OK_200("200"), //
        ERROR_301_MOVED_PERMANENTLY("301"), //
        ERROR_302_FOUND("302"), //
        ERROR_401_UNAUTHORIZED("401"), //
        ERROR_403_FORBIDDEN("403"), //
        ERROR_404_NOT_FOUND("404"), //
        ERROR_405_METHOD_NOT_ALLOWED("405"), //
        ERROR_500_INTERNAL_SERVER_ERROR("500"), //
        ERROR_501_NOT_IMPLEMENTED("501"), //
        ERROR_503_SERVICE_UNAVAILABLE("503");//

        private String code;

        StatusCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    private final OutputStream output;
    private final IndentedHtmlStream htmlText;
    private StatusCode status = StatusCode.OK_200;

    public HttpResponse(final OutputStream output) {
        this.output = output;
        this.htmlText = new IndentedHtmlStream(output);
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
    }

    public void writePage(final Page page) throws IOException {
        output.write(("Status: " + status.getCode() + " Not Found\r\n").getBytes());
        writeCookies();
        output.write("Content-Type: text/html\r\n".getBytes());

        closeHeaders();

        page.write(htmlText);
    }

    public void writeResource(final String path, final long size, final String fileName) throws IOException {
        final String sendString1 = "Content-Disposition: inline; filename=" + fileName + "\r\n";

        final String sendString2 = "X-Sendfile2: " + path + " 0-" + size + "\r\n";
        output.write(sendString1.getBytes());
        output.write(sendString2.getBytes());

        closeHeaders();
    }

    /**
     * <p>
     * Writes a rest resource into an HttpResponse
     * </p>
     * <p>
     * Will generate either a {@code <rest result="ok">} or a
     * {@code <rest result="fail">} depending on the value of its internal
     * <code>status</code> (which can be set using
     * {@link #setStatus(StatusCode)}. <br />
     * Any value other than OK_200 will result in a fail.
     * </p>
     * <p>
     * Before writing make sure the statusCode has been correctly set. <br />
     * The default value of the status code is OK_200 hence when everything goes
     * well, there is no need to change it. Whenever generating the restResource
     * goes haywire, think to set a correct status using the method
     * {@link #setStatus(StatusCode)}
     * </p>
     * 
     * @param resource the resource to write
     * @throws IOException whenever an IO error occurs on the underlying stream
     * @see #setStatus(StatusCode)
     */
    public void writeRestResource(final RestResource resource) throws IOException {
        output.write("Content-Type: text/xml\r\n".getBytes());
        closeHeaders();
        htmlText.writeLine("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
        if (!(status == StatusCode.OK_200)) {
            htmlText.writeLine("<rest result=\"fail\">");

        } else {
            htmlText.writeLine("<rest result=\"ok\">");
        }
        htmlText.indent();
        resource.write(htmlText);
        htmlText.unindent();
        htmlText.writeLine("</rest>");
    }

    public void setStatus(StatusCode status) {
        this.status = status;
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
