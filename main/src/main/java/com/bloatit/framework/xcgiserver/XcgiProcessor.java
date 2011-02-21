package com.bloatit.framework.xcgiserver;

import java.io.IOException;

import com.bloatit.framework.webserver.masters.HttpResponse;

public interface XcgiProcessor {

    /**
     * Take a request and use the {@link HttpResponse} object to create a
     * result.
     * 
     * @param header Is the header part of the HTTP request.
     * @param postData is the Post data from the HTTP request.
     * @param response is where you will have to put the response to this
     *            request.
     * @return true if you can answer to this request and have done, false
     *         otherwise.
     * @throws IOException if there is an error writing to the response.
     */
    boolean process(HttpHeader header, HttpPost postData, HttpResponse response) throws IOException;
}
