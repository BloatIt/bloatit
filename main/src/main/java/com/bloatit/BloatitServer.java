package com.bloatit;

import com.bloatit.framework.Framework;
import com.bloatit.model.Model;
import com.bloatit.web.BloatitWebServer;

public class BloatitServer {

    public static void main(String[] args) {
        Framework framework = new Framework(new Model());
        framework.addProcessor(new BloatitWebServer());
        
        framework.initialize();
        try {
            framework.run();
        } finally {
            framework.shutdown();
            System.exit(0);
        }
    }

}
