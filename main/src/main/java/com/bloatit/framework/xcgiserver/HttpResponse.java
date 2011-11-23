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
package com.bloatit.framework.xcgiserver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

import com.bloatit.common.Log;
import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.restprocessor.RestResource;
import com.bloatit.framework.restprocessor.exception.RestException;
import com.bloatit.framework.utils.datetime.DateUtils;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.masters.AtomFeed;
import com.bloatit.framework.webprocessor.masters.SiteMap;
import com.bloatit.framework.xcgiserver.HttpReponseField.StatusCode;
import com.bloatit.web.HtmlTools;

public final class HttpResponse {
    static final byte[] EOL = "\r\n".getBytes();

    private static enum Encoding {
        NONE, GZIP, DEFLATE,
    }

    private final OutputStream output;
    private final SimpleDateFormat httpDateformat;
    private Encoding encoding = Encoding.NONE;

    private Set<HttpReponseField> headers = new HashSet<HttpReponseField>();

    public HttpResponse(final OutputStream output, final HttpHeader header) {
        httpDateformat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
        httpDateformat.setTimeZone(TimeZone.getTimeZone("GMT"));

        if (header.getHttpAcceptEncoding().contains("deflate")) {
            encoding = Encoding.DEFLATE;
        } else if (header.getHttpAcceptEncoding().contains("gzip")) {
            encoding = Encoding.GZIP;
        } else {
            encoding = Encoding.NONE;
        }

        this.output = output;
    }

    public void addField(HttpReponseField headerField) {
        headers.add(headerField);
    }

    /**
     * Writes an exception to the page
     */
    public void writeException(final Exception e) {
        // Construct header
        addField(HttpReponseField.status(StatusCode.ERROR_SERV_500_INTERNAL_SERVER_ERROR));
        addField(HttpReponseField.contentType("Content-type: text/plain"));

        // Construct body
        final StringBuilder display = new StringBuilder();
        display.append(e.toString());
        display.append(" :\n");
        for (final StackTraceElement s : e.getStackTrace()) {
            display.append('\t');
            display.append(s);
            display.append('\n');
        }

        try {
            writeHeader();
            output.write(display.toString().getBytes());
        } catch (final IOException e1) {
            Log.framework().fatal("Cannot send exception through the SCGI socket.", e1);
        }
    }

    public void writeRedirect(StatusCode status, final String url) throws IOException {
        addField(HttpReponseField.status(status));
        addField(HttpReponseField.location(url));
        addSessionCookie();

        writeHeader();
    }

    public void writeOAuthRedirect(int status, final String url) throws IOException {
        addField(new HttpReponseField("status", String.valueOf(status)));
        addField(HttpReponseField.location(url));
        writeHeader();
    }

    public void writeOAuth(int status, Map<String, String> headers, final String body) throws IOException {
        addField(new HttpReponseField("status", String.valueOf(status)));
        addField(HttpReponseField.contentType("application/json;charset=UTF-8"));
        addField(HttpReponseField.cacheControl("no-store"));
        addField(HttpReponseField.pragma("no-cache"));
        for (Entry<String, String> header : headers.entrySet()) {
            addField(new HttpReponseField(header.getKey(), header.getValue()));
        }
        writeHeader();

        output.write(body.getBytes());
    }

    public void writePage(StatusCode status, String contentType, final HtmlElement page) throws IOException {
        addField(HttpReponseField.status(status));
        addSessionCookie();
        addField(HttpReponseField.vary("Accept-Encoding"));
        addField(HttpReponseField.contentType(contentType));
        addField(HttpReponseField.acceptRanges("bytes"));
        String languageCode = Context.getLocalizator().getCode();
        if (!languageCode.isEmpty()) {
            addField(HttpReponseField.contentLanguage(languageCode));
        }

        switch (encoding) {
            case DEFLATE:
            case GZIP:
                final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                OutputStream encodedStream;
                if (encoding == Encoding.GZIP) {
                    addField(HttpReponseField.contentEncoding("gzip"));
                    encodedStream = new GZIPOutputStream(buffer);
                } else {
                    addField(HttpReponseField.contentEncoding("deflate"));
                    encodedStream = new DeflaterOutputStream(buffer);
                }

                page.write(encodedStream);
                encodedStream.flush();
                encodedStream.close();
                final byte[] byteArray = buffer.toByteArray();
                addField(HttpReponseField.contentLength(String.valueOf(byteArray.length)));

                writeHeader();
                output.write(byteArray);
                break;
            case NONE:
            default:
                writeHeader();
                page.write(output);
                break;
        }
    }

    public void writeResource(final String path, final long size, final String fileName) throws IOException {
        // addField(HttpReponseField.status(status));
        addField(HttpReponseField.contentDisposition("inline; filename=" + fileName));
        addField(HttpReponseField.vary("Accept-Encoding"));
        addField(HttpReponseField.acceptRanges("bytes"));

        // Last-Modified
        final File file = new File(path);
        final long lastModified = file.lastModified();

        writeLine("Last-Modified: " + httpDateformat.format(new Date(lastModified)));

        // Expires (360 days)
        writeLine("Expires: " + httpDateformat.format(DateUtils.nowPlusSomeYears(1)));

        // Content type
        inferContentType(fileName);

        // Send file
        addField(new HttpReponseField("X-sendfile2", path + " 0-" + (size - 1)));
        writeHeader();
    }

    public void writeData(final byte[] data, final long size, final String fileName) throws IOException {
        // addField(HttpReponseField.status(status));
        addField(HttpReponseField.contentDisposition("inline; filename=" + fileName));
        addField(HttpReponseField.vary("Accept-Encoding"));
        addField(HttpReponseField.acceptRanges("bytes"));

        // Content type
        inferContentType(fileName);

        writeHeader();

        // Send file
        output.write(data);
    }

    private void inferContentType(final String fileName) {
        if (fileName.endsWith(".css")) {
            addField(HttpReponseField.contentType("text/css"));
        } else if (fileName.endsWith(".png")) {
            addField(HttpReponseField.contentType("image/png"));
        } else if (fileName.endsWith(".pdf")) {
            addField(HttpReponseField.contentType("application/pdf"));
        } else if (fileName.endsWith(".zip")) {
            addField(HttpReponseField.contentType("multipart/x-zip"));
        } else {
            
            // FIXME manage other content types !!
            Log.framework().warn("FIXME: Unknown content type for file '" + fileName + "' in HttpResponse.writeResource");
        }
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
        try {
            final String resourceXml = resource.getXmlString();
            // addField(HttpReponseField.status(status));
            addField(HttpReponseField.contentType("text/xml"));
            addField(HttpReponseField.vary("Accept-Encoding"));
            addField(HttpReponseField.acceptRanges("bytes"));
            // In modern browser allow external website to request our site (for
            // ajax requests).
            addField(new HttpReponseField("Access-Control-Allow-Origin", "*"));
            writeHeader();

            output.write("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>\n".getBytes());
            output.write(("<rest result=\"ok\" request=\"" + HtmlTools.escape(resource.getRequest()) + "\" >\n").getBytes());
            output.write(resourceXml.getBytes());
            output.write("</rest>\n".getBytes());
        } catch (final Exception e) {
            Log.rest().fatal("Exception while marshalling RestResource " + resource.getUnderlying(), e);
            writeRestError(StatusCode.ERROR_SERV_500_INTERNAL_SERVER_ERROR, "Error while marhsalling the Object", e);
        }
    }

    /**
     * Writes a rest error based on the <code>exception</code>
     * 
     * @param exception the exception describing the error
     * @throws IOException when an IO error occurs
     */
    public void writeRestError(final RestException exception) throws IOException {
        writeRestError(exception.getStatus(), exception.getMessage(), exception);
    }

    /**
     * <p>
     * Writes a rest error
     * </p>
     * 
     * @see {@link #writeRestError(RestException)}
     */
    private void writeRestError(final StatusCode status, final String message, final Exception e) throws IOException {
        addField(HttpReponseField.status(status));
        addField(HttpReponseField.contentType("text/xml"));
        addField(HttpReponseField.vary("Accept-Encoding"));
        addField(HttpReponseField.acceptRanges("bytes"));
        // In modern browser allow external website to request our site (for
        // ajax requests).
        addField(new HttpReponseField("Access-Control-Allow-Origin", "*"));
        writeHeader();
        output.write("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>\n".getBytes());
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        final String stackTrace = sw.toString();

        if (stackTrace != null && !stackTrace.isEmpty()) {
            output.write(("<error code=\"" + status.toString() + "\" reason=\"" + message + "\" >\n").getBytes());
            output.write((HtmlTools.escape(stackTrace) + "\n").getBytes());
            output.write("</error>\n".getBytes());
        } else {
            output.write(("<error reason=\"" + HtmlTools.escape(status.toString()) + "\" />\n").getBytes());
        }
    }

    private void writeHeader() throws IOException {
        for (HttpReponseField field : headers) {
            field.write(output);
        }
        output.write(EOL);
    }

    private void writeLine(final String string) throws IOException {
        output.write(string.getBytes());
        output.write(EOL);
    }

    private void addSessionCookie() throws IOException {
        if (FrameworkConfiguration.isHttpsEnabled()) {
            headers.add(HttpReponseField.setCookie("session_key=" + Context.getSession().getKey() + "; path=/; Max-Age=1296000; Secure; HttpOnly "));
        } else {
            headers.add(HttpReponseField.setCookie("session_key=" + Context.getSession().getKey() + "; path=/; Max-Age=1296000; HttpOnly "));
        }
    }

    public void writeAtomFeed(AtomFeed feed) throws IOException {
        addField(HttpReponseField.contentType("text/xml"));
        addField(HttpReponseField.vary("Accept-Encoding"));
        addField(HttpReponseField.acceptRanges("bytes"));
        writeHeader();

        feed.write(output);
    }

    public void writeSiteMap(SiteMap siteMap) throws IOException {
        addField(HttpReponseField.contentType("text/xml"));
        addField(HttpReponseField.vary("Accept-Encoding"));
        addField(HttpReponseField.acceptRanges("bytes"));
        writeHeader();

        siteMap.write(output);
    }
}
