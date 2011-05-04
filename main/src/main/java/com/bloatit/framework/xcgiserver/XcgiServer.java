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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bloatit.common.Log;
import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.webprocessor.context.SessionManager;
import com.bloatit.framework.xcgiserver.fcgi.FCGIParser;

public final class XcgiServer {

    private static final int SCGI_PORT = FrameworkConfiguration.getXcgiListenport();
    private static final int NB_THREADS = FrameworkConfiguration.getXcgiThreadsNumber();

    private final List<XcgiThread> threads = new ArrayList<XcgiThread>(NB_THREADS);
    private final List<XcgiProcessor> processors = new ArrayList<XcgiProcessor>();

    public XcgiServer() {
        // Nothing ?
    }

    public void addProcessor(final XcgiProcessor processor) {
        this.processors.add(processor);
    }

    private List<XcgiProcessor> getProcessors() {
        return processors;
    }

    public void initialize() throws IOException {
        SessionManager.loadSessions();
        Log.framework().info("Init: Start BloatIt serveur");

        Log.framework().info("-> initializing all processors");
        for (final XcgiProcessor processor : processors) {
            Log.framework().info("--> initialization processor: " + processor.getClass().getSimpleName());
            if (!processor.initialize()) {
                throw new BadProgrammerException("Initialization of processor failed");
            }
        }

        Log.framework().info("-> launching " + NB_THREADS + " threads on ports " + SCGI_PORT + " ...");
        for (int i = SCGI_PORT; i < SCGI_PORT + NB_THREADS; ++i) {
            threads.add(new XcgiThread(i));
        }
    }

    public void start() {
        for (final XcgiThread thread : threads) {
            thread.start();
        }

        for (final XcgiThread thread : threads) {
            try {
                thread.join();
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private final class XcgiThread extends Thread {
        private static final int NB_MAX_SOCKET_ERROR = 12;
        private Socket socket;
        private final ServerSocket provider;
        private final Timer timer;

        private XcgiThread(final int port) throws IOException {
            super();
            provider = new ServerSocket(port);
            timer = new Timer();
        }

        @Override
        public void run() {
            int nbError = 0;
            while (true) {
                try {
                    generateAndSendReponse();
                } catch (final IOException e) {
                    nbError++;
                    if (nbError > NB_MAX_SOCKET_ERROR) {
                        throw new BadProgrammerException("Too much errors on this socket.", e);
                    }
                    Log.framework().fatal("soket error on port: " + provider.getLocalPort(), e);
                }
            }
        }

        private void kill() {
            if (socket != null && !socket.isClosed()) {
                try {
                    socket.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void generateAndSendReponse() throws IOException {
            // Wait for connection
            Log.framework().trace("Waiting connection");

            // Wait for a connection.
            socket = provider.accept();
            Log.framework().trace("Received a connection");
            timer.start();

            // Parse the header and the post data.
            final BufferedInputStream bis = new BufferedInputStream(socket.getInputStream(), 4096);
            final XcgiParser parser = getXCGIParser(bis, socket.getOutputStream());

            final Map<String, String> env = parser.getEnv();
            final HttpHeader header = new HttpHeader(env);
            final HttpPost post = new HttpPost(parser.getPostStream(), header.getContentLength(), header.getContentType());

            Log.framework().debug(env);

            // LOGGING REQUESTS
            StringBuilder request = new StringBuilder();
            request.append(header.getRequestMethod());
            request.append(' ');
            request.append(header.getRequestUri());
            request.append("  ");
            request.append('[');
            request.append("REMOTE_ADDR=");
            request.append(header.getRemoteAddr());
            request.append(", ");
            request.append("USER_AGENT=");
            request.append(header.getHttpUserAgent());
            request.append(", ");
            request.append("ACCEPT_LANGUAGES=");
            request.append(header.getHttpAcceptLanguage());
            request.append(", ");
            request.append("PATH_TRANSLATED=");
            request.append(header.getPathTranslated());
            request.append(']');
            Log.framework().info(request.toString());

            // TODO: use timer ?
            SessionManager.clearExpiredSessions();

            try {
                for (final XcgiProcessor processor : getProcessors()) {
                    if (processor.process(header, post, new HttpResponse(parser.getResponseStream(), header))) {
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
                parser.getResponseStream().close();
            }

            Log.framework().debug("Page generated in " + timer.elapsed() + " ms");
        }

        private XcgiParser getXCGIParser(final InputStream is, final OutputStream os) throws IOException {
            // You can also use scgi parser
            return new FCGIParser(is, os);
        }
    }

    public void stop() {
        // TODO: lock to wait transaction end
        for (final XcgiThread thread : threads) {
            if (thread.isAlive()) {
                thread.kill();
            }
        }
    }
}
