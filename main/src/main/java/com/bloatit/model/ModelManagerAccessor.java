package com.bloatit.model;

import com.bloatit.framework.exceptions.FatalErrorException;

public class ModelManagerAccessor {

    private static AbstractModelManager modelManager = null;

    private static void setModelManager(AbstractModelManager manager) {
        if (modelManager == null) {
            modelManager = manager;
        } else {
            throw new FatalErrorException("You can set the modelManager only once !");
        }
    }

    /**
     * @see com.bloatit.model.AbstractModelManager#launch()
     */
    public static void launch(AbstractModelManager manager) {
        setModelManager(manager);
        modelManager.launch();
    }

    /**
     * @see com.bloatit.model.AbstractModelManager#shutdown()
     */
    public static void shutdown() {
        modelManager.shutdown();
    }

    /**
     * @see com.bloatit.model.AbstractModelManager#setReadOnly()
     */
    public static void setReadOnly() {
        modelManager.setReadOnly();
    }

    /**
     * @see com.bloatit.model.AbstractModelManager#open()
     */
    public static void open() {
        modelManager.open();
    }

    /**
     * @see com.bloatit.model.AbstractModelManager#close()
     */
    public static void close() {
        modelManager.close();
    }

    /**
     * @throws InterruptedException
     * @see com.bloatit.model.AbstractModelManager#lock()
     */
    public static void lock() throws InterruptedException {
        modelManager.lock();
    }

    /**
     * @see com.bloatit.model.AbstractModelManager#unLock()
     */
    public static void unLock() {
        modelManager.unLock();
    }

}
