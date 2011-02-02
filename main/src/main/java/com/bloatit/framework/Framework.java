package com.bloatit.framework;

import java.io.IOException;

import com.bloatit.common.Log;
import com.bloatit.framework.scgiserver.SCGIServer;
import com.bloatit.framework.scgiserver.ScgiProcessor;
import com.bloatit.framework.webserver.ModelManagerAccessor;
import com.bloatit.model.AbstractModel;

/**
 * This class represent the whole framework. 
 * @author Thomas Guyard
 *
 */
public class Framework {
    
    private final AbstractModel model;
    private SCGIServer scgiServer;

    public Framework(AbstractModel model) {
        this.model = model;
        this.scgiServer = new SCGIServer();
    }
    
    
    public void addProcessor(ScgiProcessor processor) {
        scgiServer.addProcessor(processor);
    }


    public void initialize(){
        ModelManagerAccessor.launch(model);
    }
    
    public void run(){
        try {
            scgiServer.run();
        } catch (final IOException e) {
            Log.framework().fatal(e);
        }
    }
    
    public void shutdown(){
        ModelManagerAccessor.shutdown();
    }
    
}
