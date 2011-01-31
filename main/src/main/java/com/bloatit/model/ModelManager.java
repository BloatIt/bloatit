package com.bloatit.model;

import java.util.concurrent.Semaphore;

import com.bloatit.data.DataManager;
import com.bloatit.framework.mailsender.MailServer;

public class ModelManager implements AbstractModelManager {
    private static Semaphore mutex = new Semaphore(1, false);

    public ModelManager() {
        // do nothing
    }

    /* (non-Javadoc)
     * @see com.bloatit.model.AbstractModelManager#launch()
     */
    @Override
    public void launch() {
        MailServer.init();
    }

    /* (non-Javadoc)
     * @see com.bloatit.model.AbstractModelManager#shutdown()
     */
    @Override
    public void shutdown() {
        DataManager.shutdown();
    }

    /* (non-Javadoc)
     * @see com.bloatit.model.AbstractModelManager#openReadOnly()
     */
    @Override
    public void setReadOnly() {
        DataManager.setReadOnly();
    }

    /* (non-Javadoc)
     * @see com.bloatit.model.AbstractModelManager#open()
     */
    @Override
    public void open() {
        DataManager.open();
    }

    /* (non-Javadoc)
     * @see com.bloatit.model.AbstractModelManager#close()
     */
    @Override
    public void close() {
        DataManager.close();
    }

    /* (non-Javadoc)
     * @see com.bloatit.model.AbstractModelManager#lock()
     */
    @Override
    public void lock() throws InterruptedException {
        mutex.acquire();
    }

    /* (non-Javadoc)
     * @see com.bloatit.model.AbstractModelManager#unLock()
     */
    @Override
    public void unLock() {
        mutex.release();
    }
}
