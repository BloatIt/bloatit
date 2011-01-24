package com.bloatit.framework;

import java.util.concurrent.Semaphore;

/**
 * This mutex is used to lock all the framework. It is used in {@link PlannedTask}.
 */
public final class FrameworkMutex {
    private static Semaphore mutex = new Semaphore(1, false);

    private FrameworkMutex() {
        // do nothing
    }

    /**
     * Reserve the Framework and make sure nobody else is using it.
     * 
     * @throws InterruptedException
     */
    public static void lock() throws InterruptedException {
        mutex.acquire();
    }

    /**
     * Release the framework after we had reserved it.
     */
    public static void unLock() {
        mutex.release();
    }

}
