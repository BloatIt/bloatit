package com.bloatit.framework;

import java.util.concurrent.Semaphore;


public final class FrameworkMutex {
    private static Semaphore mutex = new Semaphore(1, false);

    private FrameworkMutex() {
        // do nothing
    }

    public static void lock() throws InterruptedException {
        mutex.acquire();
    }

    public static void unLock() {
        mutex.release();
    }

}
