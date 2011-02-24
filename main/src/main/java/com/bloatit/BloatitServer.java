package com.bloatit;

import com.bloatit.framework.Framework;
import com.bloatit.model.Model;
import com.bloatit.rest.BloatitRestServer;
import com.bloatit.web.BloatitWebServer;

public class BloatitServer {

    public static void main(final String[] args) {
        final Framework framework = new Framework(new Model());
        // framework.addProcessor(new ResourceServer());
        framework.addProcessor(new BloatitRestServer());
        framework.addProcessor(new BloatitWebServer());

        try {
            if (framework.initialize()) {
                framework.run();
            }
        } finally {
            // framework.shutdown(); done in the hook (ook OoOk oOk!!)
            System.exit(0);
        }

    }

}
