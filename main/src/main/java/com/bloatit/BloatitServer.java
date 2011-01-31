package com.bloatit;

import java.io.IOException;

import com.bloatit.common.Log;
import com.bloatit.framework.scgiserver.SCGIServer;
import com.bloatit.model.Model;
import com.bloatit.web.BloatitWebServer;

public class BloatitServer {

    public static void main(String[] args) {
        Model.launch();
        try {
            SCGIServer scgiServer = new SCGIServer();
            scgiServer.addProcessor(new BloatitWebServer());
            scgiServer.run();
        } catch (final IOException e) {
            Log.framework().fatal(e);
        } finally {
            Model.shutdown();
        }
    }

}
