package com.bloatit;

import java.io.IOException;

import com.bloatit.common.Log;
import com.bloatit.framework.scgiserver.SCGIServer;
import com.bloatit.model.ModelManager;
import com.bloatit.model.ModelManagerAccessor;
import com.bloatit.web.BloatitWebServer;

public class BloatitServer {

    public static void main(String[] args) {
        ModelManagerAccessor.launch(new ModelManager());
        try {
            SCGIServer scgiServer = new SCGIServer();
            scgiServer.addProcessor(new BloatitWebServer());
            scgiServer.run();
        } catch (final IOException e) {
            Log.framework().fatal(e);
        } finally {
            ModelManagerAccessor.shutdown();
        }
    }

}
