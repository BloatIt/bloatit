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
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.mailsender.MailServer;
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

    private void init() throws IOException {
        SessionManager.loadSessions();
        Runtime.getRuntime().addShutdownHook(new ShutdownHook(clientSocket));

        try {
            Thread.sleep(100);
        } catch (final InterruptedException ex) {
            Log.framework().warn("Init: Waiting has been interupted.", ex);
        }

        Log.framework().info("Init: Start BloatIt serveur");
        providerSocket = new ServerSocket(SCGI_PORT);
    }

    public void run() throws IOException {
        init();
        while (true) {
            // Wait for connection
            Log.framework().info("Waiting connection");

            // Load the SCGI headers.
            clientSocket = providerSocket.accept();
            Log.framework().trace("Received a connection");

            final long startTime = System.nanoTime();

            final BufferedInputStream bis = new BufferedInputStream(clientSocket.getInputStream(), 4096);
            final Map<String, String> env = SCGIUtils.parse(bis);

            // for(Entry<String, String>item : env.entrySet()){
            // System.out.println("[" + item.getKey() + "] : " + item.getValue());
            // }

            final HttpHeader header = new HttpHeader(env);
            final HttpPost post = new HttpPost(bis, header.getContentLength(), header.getContentType());

            SessionManager.clearExpiredSessions();

            try {

                for (ScgiProcessor processor : processors) {
                    if (processor.process(header, post, new HttpResponse(clientSocket.getOutputStream()))) {
                        break;
                    }
                }
            } catch (final FatalErrorException e) {
                webPrintException(e);
                Log.framework().fatal("Unknown Fatal exception", e);
            } catch (final SCGIRequestAbordedException e) {
                webPrintException(e);
                Log.framework().info("SCGIUtils request aborded", e);
            } catch (final Exception e) {
                webPrintException(e);
                Log.framework().fatal("Unknown exception", e);
            }finally {
                Log.framework().trace("Closing connection");
                clientSocket.close();    
            }

            
            final long endTime = System.nanoTime();
            final double duration = ((endTime - startTime)) / 1000000.;
            Log.framework().debug("Page generated in " + duration + " ms");
        }
        
    }

    private void webPrintException(final Exception e) {
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
            clientSocket.getOutputStream().write(display.toString().getBytes());
        } catch (final IOException e1) {
            Log.framework().fatal("Cannot send exception through the SCGI soket.", e1);
        }
    }

    private static final class ShutdownHook extends Thread {
        private final Socket clientSocket;

        public ShutdownHook(final Socket clientSocket) {
            super();
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            // TODO: lock to wait transaction end
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (final IOException e) {
                Log.framework().error("Fail to close the socket on shutdown.", e);
            }

            SessionManager.saveSessions();
            MailServer.getInstance().quickStop();
        }
    }
}
