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
package com.bloatit;

import com.bloatit.common.ConfigurationManager;
import com.bloatit.common.Log;
import com.bloatit.framework.Framework;
import com.bloatit.framework.oauthprocessor.OAuthProcessor;
import com.bloatit.mail.EventDataworker;
import com.bloatit.model.Model;
import com.bloatit.oauth.ElveosAuthenticator;
import com.bloatit.rest.BloatitRestServer;
import com.bloatit.web.BloatitWebServer;

public class BloatitServer {

    public static void main(final String[] args) {
        System.setProperty("log4J.path", ConfigurationManager.SHARE_DIR + "/log");
        final Framework framework = new Framework(new Model());
        framework.addProcessor(new OAuthProcessor(new ElveosAuthenticator()));
        framework.addProcessor(new BloatitRestServer());
        framework.addProcessor(new BloatitWebServer());
        framework.addWorker(new EventDataworker());

        try {
            if (framework.initialize()) {
                framework.run();
            }
        } catch (final Throwable e) {
            e.printStackTrace();
            Log.framework().fatal("It's the lose... :(", e);
        } finally {
            // framework.shutdown(); done in the hook (ook OoOk oOk!!)
            System.exit(0);
        }
    }
}
