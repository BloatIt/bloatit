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
package com.bloatit.framework.scgiserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bloatit.common.Log;
import com.bloatit.framework.webserver.SessionManager;
import com.bloatit.framework.webserver.masters.HttpResponse;

public final class SCGIServer {

    private static final int SCGI_PORT = 4000;

    private ServerSocket providerSocket;
    private Socket clientSocket;
    private final List<ScgiProcessor> processors = new ArrayList<ScgiProcessor>();

    public SCGIServer() {
        clientSocket = null;
        providerSocket = null;
    }

    public void addProcessor(ScgiProcessor processor) {
        this.processors.add(processor);
    }

    public void init() {
        SessionManager.loadSessions();
        Log.framework().info("Init: Start BloatIt serveur");
    }

    public void start() throws IOException {
        Timer timer = new Timer();
        providerSocket = new ServerSocket(SCGI_PORT);
        while (true) {
            // Wait for connection
            Log.framework().info("Waiting connection");

            // Wait for a connection.
            clientSocket = providerSocket.accept();
            Log.framework().trace("Received a connection");
            timer.start();

            // Parse the header and the post data.
            final BufferedInputStream bis = new BufferedInputStream(clientSocket.getInputStream(), 4096);
            final Map<String, String> env = SCGIUtils.parse(bis);
            final HttpHeader header = new HttpHeader(env);
            final HttpPost post = new HttpPost(bis, header.getContentLength(), header.getContentType());

            // FIXME: use timer ?
            SessionManager.clearExpiredSessions();

            try {
                for (ScgiProcessor processor : processors) {
                    if (processor.process(header, post, new HttpResponse(new BufferedOutputStream(clientSocket.getOutputStream(), 1024)))) {
                        break;
                    }
                }
            } catch (final IOException e) {
                Log.framework().fatal("SCGIServer: IOException on the socket output", e);
            } catch (final RuntimeException e) {
                Log.framework().fatal("SCGIServer: Unknown RuntimeException", e);
            } catch (final Exception e) {
                Log.framework().fatal("SCGIServer: Unknown Exception", e);
            } finally {
                Log.framework().trace("Closing connection");
                clientSocket.close();
            }

            Log.framework().debug("Page generated in " + timer.elapsed() + " ms");
        }

    }

    public void stop() {
        // TODO: lock to wait transaction end
        try {
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (final IOException e) {
            Log.framework().fatal("Fail to close the socket on shutdown.", e);
        }
    }
}
