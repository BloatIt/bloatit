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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bloatit.common.FatalErrorException;
import com.bloatit.common.Log;
import com.bloatit.web.server.Context;
import com.bloatit.web.server.DispatchServer;
import com.bloatit.web.server.HttpHeader;
import com.bloatit.web.server.HttpResponse;
import com.bloatit.web.server.SessionManager;

public class SCGIServer {

    private static ServerSocket providerSocket;

    public static void main(final String[] args) {
        final SCGIServer server = new SCGIServer();
        server.init();
        server.serve();
    }

    private Socket clientSocket;

    private void init() {

        SessionManager.LoadSessions();

        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {

                SessionManager.SaveSessions();

                // TODO: lock to wait transaction end
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

        });
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
            Log.web().info("Start BloatIt serveur");
            providerSocket = new ServerSocket(4000);

            while (true) {
                // Wait for connection
                System.out.println("Waiting for connection");

                // Load the SCGI headers.
                clientSocket = providerSocket.accept();

                final long startTime = System.nanoTime();

                BufferedInputStream bis;
                bis = new BufferedInputStream(clientSocket.getInputStream(), 4096);
                final Map<String, String> env = SCGI.parse(bis);
                // SCGI.parse(bis);
                // Read the body of the request.

                final byte[] postBytes = new byte[Integer.parseInt(env.get("CONTENT_LENGTH"))];
                bis.read(postBytes);

                // for (String key : env.keySet()) {
                // System.err.println("" + key + " -> " + env.get(key));
                // }

                HttpHeader header = new HttpHeader(env);
                HttpPost post = new HttpPost(postBytes);
                Context.setHeader(header);

                final DispatchServer dispatchServer = new DispatchServer(header, post);

                try {
                    dispatchServer.process(new HttpResponse(clientSocket.getOutputStream()));
                } catch (final FatalErrorException e) {
                    StringBuilder display = new StringBuilder();
                    display.append("Content-type: text/plain\r\n\r\n");
                    display.append(e.toString());
                    display.append(" :\n");

                    for (final StackTraceElement s : e.getStackTrace()) {
                        display.append("\t");
                        display.append(s);
                        display.append("\n");
                    }

                    clientSocket.getOutputStream().write(display.toString().getBytes());

                    // TODO Debug Only
                    Log.web().fatal("Unknown Fatal exception", e);
                } catch (final SCGIRequestAbordedException e) {
                    Log.web().info("SCGI request aborded", e);
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
            // TODO procedure de reprise
        }

    }
}
