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
import java.util.Map;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.mailsender.MailServer;
import com.bloatit.framework.webserver.DispatchServer;
import com.bloatit.framework.webserver.SessionManager;
import com.bloatit.framework.webserver.masters.HttpResponse;
import com.bloatit.model.Model;
import com.bloatit.web.url.AccountChargingPageUrl;
import com.bloatit.web.url.CommentCommentActionUrl;
import com.bloatit.web.url.CommentReplyPageUrl;
import com.bloatit.web.url.ContributePageUrl;
import com.bloatit.web.url.ContributionActionUrl;
import com.bloatit.web.url.CreateIdeaActionUrl;
import com.bloatit.web.url.CreateIdeaPageUrl;
import com.bloatit.web.url.DemandListUrl;
import com.bloatit.web.url.DemandPageUrl;
import com.bloatit.web.url.FileUploadPageUrl;
import com.bloatit.web.url.IdeaCommentActionUrl;
import com.bloatit.web.url.IndexPageUrl;
import com.bloatit.web.url.KudoActionUrl;
import com.bloatit.web.url.LoginActionUrl;
import com.bloatit.web.url.LoginPageUrl;
import com.bloatit.web.url.LogoutActionUrl;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.MembersListPageUrl;
import com.bloatit.web.url.MyAccountPageUrl;
import com.bloatit.web.url.OfferActionUrl;
import com.bloatit.web.url.OfferPageUrl;
import com.bloatit.web.url.PaylineActionUrl;
import com.bloatit.web.url.PaylineNotifyActionUrl;
import com.bloatit.web.url.PaylinePageUrl;
import com.bloatit.web.url.RegisterActionUrl;
import com.bloatit.web.url.RegisterPageUrl;
import com.bloatit.web.url.SpecialsPageUrl;
import com.bloatit.web.url.TestPageUrl;

public final class SCGIServer {

    private static final int SCGI_PORT = 4000;

    public static void main(final String[] args) {
        Model.launch();
        try {
            new SCGIServer().run();
        } catch (final IOException e) {
            Log.framework().fatal(e);
        } finally {
            Model.shutdown();
        }
    }

    private ServerSocket providerSocket;
    private Socket clientSocket;
    private final DispatchServer dispatchServer;

    public SCGIServer() {
        clientSocket = null;
        providerSocket = null;

        dispatchServer = new DispatchServer();
        dispatchServer.addLinkable(IndexPageUrl.getName(), IndexPageUrl.class);
        dispatchServer.addLinkable(LoginPageUrl.getName(), LoginPageUrl.class);
        dispatchServer.addLinkable(DemandListUrl.getName(), DemandListUrl.class);
        dispatchServer.addLinkable(CreateIdeaPageUrl.getName(), CreateIdeaPageUrl.class);
        dispatchServer.addLinkable(DemandPageUrl.getName(), DemandPageUrl.class);
        dispatchServer.addLinkable(MyAccountPageUrl.getName(), MyAccountPageUrl.class);
        dispatchServer.addLinkable(SpecialsPageUrl.getName(), SpecialsPageUrl.class);
        dispatchServer.addLinkable(MembersListPageUrl.getName(), MembersListPageUrl.class);
        dispatchServer.addLinkable(MemberPageUrl.getName(), MemberPageUrl.class);
        dispatchServer.addLinkable(ContributePageUrl.getName(), ContributePageUrl.class);
        dispatchServer.addLinkable(OfferPageUrl.getName(), OfferPageUrl.class);
        dispatchServer.addLinkable(TestPageUrl.getName(), TestPageUrl.class);
        dispatchServer.addLinkable(AccountChargingPageUrl.getName(), AccountChargingPageUrl.class);
        dispatchServer.addLinkable(RegisterPageUrl.getName(), RegisterPageUrl.class);
        dispatchServer.addLinkable(PaylinePageUrl.getName(), PaylinePageUrl.class);
        dispatchServer.addLinkable(CommentReplyPageUrl.getName(), CommentReplyPageUrl.class);
        dispatchServer.addLinkable(FileUploadPageUrl.getName(), FileUploadPageUrl.class);

        dispatchServer.addLinkable(LoginActionUrl.getName(), LoginActionUrl.class);
        dispatchServer.addLinkable(LogoutActionUrl.getName(), LogoutActionUrl.class);
        dispatchServer.addLinkable(ContributionActionUrl.getName(), ContributionActionUrl.class);
        dispatchServer.addLinkable(OfferActionUrl.getName(), OfferActionUrl.class);
        dispatchServer.addLinkable(CreateIdeaActionUrl.getName(), CreateIdeaActionUrl.class);
        dispatchServer.addLinkable(RegisterActionUrl.getName(), RegisterActionUrl.class);
        dispatchServer.addLinkable(KudoActionUrl.getName(), KudoActionUrl.class);
        dispatchServer.addLinkable(IdeaCommentActionUrl.getName(), IdeaCommentActionUrl.class);
        dispatchServer.addLinkable(PaylineActionUrl.getName(), PaylineActionUrl.class);
        dispatchServer.addLinkable(PaylineNotifyActionUrl.getName(), PaylineNotifyActionUrl.class);
        dispatchServer.addLinkable(IdeaCommentActionUrl.getName(), IdeaCommentActionUrl.class);
        dispatchServer.addLinkable(CommentCommentActionUrl.getName(), CommentCommentActionUrl.class);
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

    private void run() throws IOException {
        init();
        while (true) {
            // Wait for connection
            Log.framework().info("Waiting connection");

            // Load the SCGI headers.
            clientSocket = providerSocket.accept();

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
                dispatchServer.process(header, post, new HttpResponse(clientSocket.getOutputStream()));
            } catch (final FatalErrorException e) {
                webPrintException(e);
                Log.framework().fatal("Unknown Fatal exception", e);
            } catch (final SCGIRequestAbordedException e) {
                webPrintException(e);
                Log.framework().info("SCGIUtils request aborded", e);
            } catch (final Exception e) {
                webPrintException(e);
                Log.framework().fatal("Unknown exception", e);
            }

            clientSocket.close();

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
