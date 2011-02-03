package com.bloatit.framework;

import java.io.IOException;

import com.bloatit.common.Log;
import com.bloatit.framework.mailsender.MailServer;
import com.bloatit.framework.scgiserver.SCGIServer;
import com.bloatit.framework.scgiserver.ScgiProcessor;
import com.bloatit.framework.webserver.ModelManagerAccessor;
import com.bloatit.model.AbstractModel;

/**
 * This class represent the whole framework.
 *
 * @author Thomas Guyard
 */
public class Framework {

    private final AbstractModel model;
    private final SCGIServer scgiServer;
    private final MailServer mailServer;

    public Framework(AbstractModel model) {
        this.model = model;
        this.scgiServer = new SCGIServer();
        this.mailServer = MailServer.getInstance();
    }

    public void addProcessor(ScgiProcessor processor) {
        scgiServer.addProcessor(processor);
    }

    public boolean initialize() {

        try {
            mailServer.init();
            scgiServer.init();
        } catch (final IOException e) {
            Log.framework().fatal("IOException on the socket output", e);
            return false;
        } catch (final RuntimeException e) {
            Log.framework().fatal("Unknown RuntimeException", e);
            return false;
        } catch (final Exception e) {
            Log.framework().fatal("Unknown Exception", e);
            return false;
        } catch (final Error e) {
            Log.framework().fatal("Unknown error", e);
            return false;
        }

        ModelManagerAccessor.launch(model);


        return true;
    }

    public void run() {
        try {
            mailServer.start();
            scgiServer.start();
        } catch (final IOException e) {
            Log.framework().fatal("IOException on the socket output", e);
        } catch (final RuntimeException e) {
            Log.framework().fatal("Unknown RuntimeException", e);
        } catch (final Exception e) {
            Log.framework().fatal("Unknown Exception", e);
        } catch (final Error e) {
            Log.framework().fatal("Unknown error", e);
        }
    }

    public void shutdown() {
        ModelManagerAccessor.shutdown();
    }

}
