package com.bloatit.framework.webserver;

import java.util.concurrent.Semaphore;

import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.model.AbstractModel;

/**
 * Thread safe class. It calls to the model initialization and open close methods.
 */
public class ModelManagerAccessor {
    /**
     * Mutex that protect this class. Every public method is callable in a multi-threaded
     * environment.
     */
    private static Semaphore mutex = new Semaphore(1);

    /**
     * This static variable is protected by the {@link #mutex}.
     */
    private static AbstractModel modelManager = null;

    private static void setModelManager(AbstractModel manager) {
        if (modelManager == null) {
            modelManager = manager;
        } else {
            throw new FatalErrorException("You can set the modelManager only once !");
        }
    }

    /**
     * This method is the init method. Call it only once, in a non MultiThreaded
     * environment.
     *
     * @see com.bloatit.model.AbstractModel#init()
     */
    public static void init(AbstractModel manager) {
        try {
            mutex.acquire();
            setModelManager(manager);
            modelManager.init();
        } catch (InterruptedException e) {
            throw new FatalErrorException(e);
        } finally {
            mutex.release();
        }
    }

    /**
     * @see com.bloatit.model.AbstractModel#shutdown()
     */
    public static void shutdown() {
        try {
            mutex.acquire();
            modelManager.shutdown();
        } catch (InterruptedException e) {
            throw new FatalErrorException(e);
        } finally {
            mutex.release();
        }
    }

    /**
     * @see com.bloatit.model.AbstractModel#setReadOnly()
     */
    public static void setReadOnly() {
        try {
            mutex.acquire();
            modelManager.setReadOnly();
        } catch (InterruptedException e) {
            throw new FatalErrorException(e);
        } finally {
            mutex.release();
        }
    }

    /**
     * @see com.bloatit.model.AbstractModel#open()
     */
    public static void open() {
        try {
            mutex.acquire();
            modelManager.open();
        } catch (InterruptedException e) {
            throw new FatalErrorException(e);
        } finally {
            mutex.release();
        }
    }

    /**
     * @see com.bloatit.model.AbstractModel#close()
     */
    public static void close() {
        try {
            mutex.acquire();
            modelManager.close();
        } catch (InterruptedException e) {
            throw new FatalErrorException(e);
        } finally {
            mutex.release();
        }
    }
}
