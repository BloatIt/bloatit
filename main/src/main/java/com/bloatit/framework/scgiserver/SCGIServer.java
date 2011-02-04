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
import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.webserver.SessionManager;
import com.bloatit.framework.webserver.masters.HttpResponse;

public final class SCGIServer {

    private static final int SCGI_PORT = 4000;
    private static final int NB_THREADS = 100;

    private final List<SCGIThread> threads = new ArrayList<SCGIThread>(NB_THREADS);
    private final List<ScgiProcessor> processors = new ArrayList<ScgiProcessor>();

    public SCGIServer() {
        // Nothing ?
    }

    public void addProcessor(ScgiProcessor processor) {
        this.processors.add(processor);
    }

    List<ScgiProcessor> getProcessors() {
        return processors;
    }

    public void init() throws IOException {
        SessionManager.loadSessions();
        Log.framework().info("Init: Start BloatIt serveur");

        for (int i = SCGI_PORT; i < SCGI_PORT + NB_THREADS; ++i) {
            threads.add(new SCGIThread(i));
        }
    }

    public void start() {
        for (SCGIThread thread : threads) {
            thread.start();
        }

        for (SCGIThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    final class SCGIThread extends Thread {

        private static final int NB_MAX_SOCKET_ERROR = 12;
        private Socket socket;
        private final ServerSocket provider;
        private final Timer timer;

        public SCGIThread(int port) throws IOException {
            super();
            provider = new ServerSocket(port);
            timer = new Timer();
        }

        @Override
        public void run() {
            try {
                int nbError = 0;
                while (true) {
                    try {
                        generateAndSendReponse();
                    } catch (IOException e) {
                        nbError++;
                        if (nbError > NB_MAX_SOCKET_ERROR) {
                            throw new FatalErrorException("Too much errors on this socket.", e);
                        }
                        Log.framework().fatal("soket error on port: " + provider.getLocalPort(), e);
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        private void generateAndSendReponse() throws IOException {
            // Wait for connection
            Log.framework().info("Waiting connection");

            // Wait for a connection.
            socket = provider.accept();
            Log.framework().trace("Received a connection");
            timer.start();

            // Parse the header and the post data.
            final BufferedInputStream bis = new BufferedInputStream(socket.getInputStream(), 4096);
            final Map<String, String> env = SCGIUtils.parse(bis);
            final HttpHeader header = new HttpHeader(env);
            final HttpPost post = new HttpPost(bis, header.getContentLength(), header.getContentType());

            // FIXME: use timer ?
            SessionManager.clearExpiredSessions();

            try {
                for (ScgiProcessor processor : getProcessors()) {
                    if (processor.process(header, post, new HttpResponse(new BufferedOutputStream(socket.getOutputStream(), 1024)))) {
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
                socket.close();
            }

            Log.framework().debug("Page generated in " + timer.elapsed() + " ms");
        }

    }

    public void stop() {
        // TODO: lock to wait transaction end
        for (SCGIThread thread : threads) {
            if (thread.isAlive()) {
                thread.interrupt();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
