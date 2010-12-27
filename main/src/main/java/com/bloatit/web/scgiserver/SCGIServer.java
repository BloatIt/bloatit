/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. BloatIt is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details. You should have received a copy of the GNU Affero General
 * Public License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.scgiserver;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bloatit.common.FatalErrorException;
import com.bloatit.common.Log;
import com.bloatit.web.server.DispatchServer;
import com.bloatit.web.server.HttpResponse;

public class SCGIServer {

    private static ServerSocket providerSocket;

    public static void main(final String[] args) {
        final SCGIServer server = new SCGIServer();
        server.serve();
    }

    private void serve() {

        // Find a better way to clean the socket
        try {
            final Socket cleanSocket = new Socket("127.0.0.1", 4000);
            cleanSocket.close();
        } catch (final IOException ex) {
        }
        try {
            Thread.sleep(100);
        } catch (final InterruptedException ex) {
            Logger.getLogger(SCGIServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            System.err.println("Start BloatIt serveur");
            providerSocket = new ServerSocket(4000);

            while (true) {
                // Wait for connection
                System.out.println("Waiting for connection");

                // Load the SCGI headers.
                final Socket clientSocket = providerSocket.accept();

                final long startTime = System.nanoTime();

                BufferedInputStream bis;
                bis = new BufferedInputStream(clientSocket.getInputStream(), 4096);
                final Map<String, String> env = SCGI.parse(bis);
                // SCGI.parse(bis);
                // Read the body of the request.

                final byte[] postBytes = new byte[Integer.parseInt(env.get("CONTENT_LENGTH"))];
                bis.read(postBytes);

                /*
                 * for (String key : env.keySet()) { System.err.println("" + key + " -> "
                 * + env.get(key)); }
                 */

                final Map<String, String> query = parseQueryString(env.get("QUERY_STRING"));
                final Map<String, String> post = parseQueryString(new String(postBytes));
                final Map<String, String> cookies = parseCookiesString(env.get("HTTP_COOKIE"));
                final List<String> preferredLangs = parseLanguageString(env.get("HTTP_ACCEPT_LANGUAGE"));

                final DispatchServer dispatchServer = new DispatchServer(query, post, cookies, preferredLangs);

                try {
                    dispatchServer.process(new HttpResponse(clientSocket.getOutputStream()));
                } catch (final FatalErrorException e) {
                    String display;
                    display = "Content-type: text/plain\r\n\r\n" + e.toString() + " :\n";
                    for (final StackTraceElement s : e.getStackTrace()) {
                        display += "\t" + s + "\n";
                    }

                    clientSocket.getOutputStream().write(display.getBytes());

                    // TODO Debug Only
                    Log.web().fatal("Unknown Fatal exception", e);
                } catch (final Exception e) {
                    // Protects the server
                    String display;
                    display = "Content-type: text/plain\r\n\r\n" + e.toString() + " :\n";
                    for (final StackTraceElement s : e.getStackTrace()) {
                        display += "\t" + s + "\n";
                    }

                    clientSocket.getOutputStream().write(display.getBytes());
                    Log.web().fatal("Unknown exception", e);
                }

                clientSocket.close();

                final long endTime = System.nanoTime();
                final double duration = ((endTime - startTime)) / 1000000.;
                System.err.println("Page generated in " + duration + " ms");
            }

        } catch (final IOException ex) {
            Log.web().fatal("Cannot connect with SCGI client.", ex);
        }

    }

    private Map<String, String> parseQueryString(final String url) {
        final Map<String, String> params = new HashMap<String, String>();
        for (final String param : url.split("&")) {
            try {
                final String[] pair = param.split("=");
                String key;
                if (pair.length >= 2) {
                    key = URLDecoder.decode(pair[0], "UTF-8");
                    final String value = URLDecoder.decode(pair[1], "UTF-8");

                    params.put(key, value);
                }
            } catch (final UnsupportedEncodingException ex) {
                Log.web().error("Cannot parse url", ex);
            }
        }

        return params;
    }

    private Map<String, String> parseCookiesString(final String cookiesString) {
        final Map<String, String> cookiesMap = new HashMap<String, String>();

        if (cookiesString != null) {
            final String[] cookies = cookiesString.split(";");
            for (final String cookie : cookies) {
                final String[] cookieParts = cookie.split("=");
                if (cookieParts.length == 2) {
                    cookiesMap.put(cookieParts[0].trim(), cookieParts[1].trim());
                }
            }
        }
        return cookiesMap;
    }

    private List<String> parseLanguageString(final String languages) {
        if (languages == null) {
            return new ArrayList<String>();
        } else {
            return Arrays.asList(languages.split(","));
        }
    }
}
