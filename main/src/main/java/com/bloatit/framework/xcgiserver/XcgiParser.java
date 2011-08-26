/*
 * Copyright (C) 2011 Linkeos.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.framework.xcgiserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.bloatit.framework.xcgiserver.fcgi.FCGIParser;
import com.bloatit.framework.xcgiserver.scgi.SCGIParser;

/**
 * XcgiParcer is an interface to encode and decode a duplex stream corresponding
 * to a cgi protocol using a socket. 2 implementations already exists:
 * {@link SCGIParser} and {@link FCGIParser}
 * 
 * @author fred
 */
public interface XcgiParser {

    /**
     * Generate a map corresponding to http params. In really CGI, there are set
     * in environnement variables
     * 
     * @return key-value map for http params
     * @throws IOException in case of problem in read or parsing
     */
    Map<String, String> getHeaders() throws IOException;

    /**
     * Return a stream where the post http data can be read
     * 
     * @return post stream
     * @throws IOException in case of problem in read or parsing
     */
    InputStream getPostStream() throws IOException;

    /**
     * Return a stream where the http response can be write
     * 
     * @return response stream
     * @throws IOException in case of problem in write
     */
    OutputStream getResponseStream() throws IOException;

}
