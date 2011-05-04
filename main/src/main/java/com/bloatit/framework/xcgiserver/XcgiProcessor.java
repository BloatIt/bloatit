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

import java.io.IOException;


public interface XcgiProcessor {

    /**
     * Initialize this processor.
     * 
     * @return true if the initialization process is sucessfull. False
     *         otherwise.
     */
    boolean initialize();

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
