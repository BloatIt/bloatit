package com.bloatit.framework.webserver;

import java.util.concurrent.Semaphore;

import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.model.AbstractModel;

/**
 * Thread safe class. It calls to the model initialization and open close
 * methods.
 */
public class ModelAccessor {
    /**
     * Mutex that protect this class. Every public method is callable in a
     * multi-threaded environment.
     */
    private static Semaphore mutex = new Semaphore(1);

    /**
     * This static variable is protected by the {@link #mutex}.
     */
    private static AbstractModel model = null;

    private static void setModelManager(final AbstractModel manager) {
        if (model == null) {
            model = manager;
        } else {
            throw new FatalErrorException("You can set the modelManager only once !");
        }
    }

    /**
     * This method is the init method. Call it only once, in a non MultiThreaded
     * environment.
     *
     * @see com.bloatit.model.AbstractModel#initialize()
     */
    public static void initialize(final AbstractModel manager) {
        try {
            mutex.acquire();
            setModelManager(manager);
            model.initialize();
        } catch (final InterruptedException e) {
            throw new FatalErrorException(e);
        } finally {
            mutex.release();
        }
    }

    /**
     * @see com.bloatit.model.AbstractModel#shutdown()
     */
    public static void shutdown() {
        if (model != null) {
            try {
                mutex.acquire();
                model.shutdown();
            } catch (final InterruptedException e) {
                throw new FatalErrorException(e);
            } finally {
                mutex.release();
            }
        }
    }

    /**
     * @see com.bloatit.model.AbstractModel#setReadOnly()
     */
    public static void setReadOnly() {
        try {
            mutex.acquire();
            model.setReadOnly();
        } catch (final InterruptedException e) {
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
            model.open();
        } catch (final InterruptedException e) {
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
            model.close();
        } catch (final InterruptedException e) {
            throw new FatalErrorException(e);
        } finally {
            mutex.release();
        }
    }

    /**
     * @see com.bloatit.model.AbstractModel#close()
     */
    public static void rollback() {
        try {
            mutex.acquire();
            model.rollback();
        } catch (final InterruptedException e) {
            throw new FatalErrorException(e);
        } finally {
            mutex.release();
        }
    }
}
