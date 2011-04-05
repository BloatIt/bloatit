package com.bloatit.framework;

import java.net.BindException;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.highlevel.ExternalErrorException;
import com.bloatit.framework.mailsender.MailServer;
import com.bloatit.framework.webserver.ModelAccessor;
import com.bloatit.framework.webserver.SessionManager;
import com.bloatit.framework.xcgiserver.XcgiProcessor;
import com.bloatit.framework.xcgiserver.XcgiServer;
import com.bloatit.model.AbstractModel;

/**
 * This class represent the whole framework.
 * 
 * @author Thomas Guyard
 */
public class Framework {

    private final AbstractModel model;
    private final XcgiServer scgiServer;
    private final MailServer mailServer;

    public Framework(final AbstractModel model) {
        this.model = model;
        this.scgiServer = new XcgiServer();
        this.mailServer = MailServer.getInstance();
    }

    public void addProcessor(final XcgiProcessor processor) {
        scgiServer.addProcessor(processor);
    }

    public boolean initialize() {
        FrameworkConfiguration.loadConfiguration();
        try {
            mailServer.initialize();
            scgiServer.initialize();
            ModelAccessor.initialize(model);
        }catch (final ExternalErrorException e){
            Log.framework().fatal("Error loading configuration file", e);
            return false;
        } catch (final BindException e) {
            Log.framework().fatal("Are you sure you have killed previous instance? ", e);
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
        } finally {
            Runtime.getRuntime().addShutdownHook(new ShutdownHook(this));
        }

        return true;
    }

    public void run() {
        try {
            mailServer.start();
            scgiServer.start();
        } catch (final RuntimeException e) {
            Log.framework().fatal("Unknown RuntimeException", e);
        } catch (final Exception e) {
            Log.framework().fatal("Unknown Exception", e);
        } catch (final Error e) {
            Log.framework().fatal("Unknown error", e);
        }
    }

    void shutdown() {
        SessionManager.saveSessions();
        scgiServer.stop();
        MailServer.getInstance().quickStop();
        ModelAccessor.shutdown();
    }

    private static final class ShutdownHook extends Thread {
        private final Framework framework;

        public ShutdownHook(final Framework framework) {
            super();
            this.framework = framework;
        }

        @Override
        public void run() {
            framework.shutdown();
        }
    }

}
