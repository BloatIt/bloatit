package com.bloatit.model;

import java.util.concurrent.Semaphore;

import com.bloatit.common.Log;
import com.bloatit.data.DataManager;
import com.bloatit.framework.mailsender.MailServer;

public class Model implements AbstractModel {
    private static final Semaphore mutex = new Semaphore(1, false);

    public Model() {
        // do nothing
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.AbstractModelManager#launch()
     */
    @Override
    public void launch() {
        MailServer.init();
        Log.model().trace("Launching the Model.");
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.AbstractModelManager#shutdown()
     */
    @Override
    public void shutdown() {
        Log.model().trace("Shutdowning the Model.");
        DataManager.shutdown();
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.AbstractModelManager#openReadOnly()
     */
    @Override
    public void setReadOnly() {
        Log.model().trace("This transaction is Read Only.");
        DataManager.setReadOnly();
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.AbstractModelManager#open()
     */
    @Override
    public void open() {
        Log.model().trace("Open a new transaction.");
        DataManager.open();
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.AbstractModelManager#close()
     */
    @Override
    public void close() {
        Log.model().trace("Close the current transaction.");
        CacheManager.clear();
        DataManager.close();
    }
}
