package com.bloatit.framework;


public final class Lock {
    private static Lock lock = new Lock();

    private Lock() {
        // do nothing
    }

    public static void doLock() throws InterruptedException {
        lock.wait();
    }

    public static void doUnLock() {
        lock.notify();
    }

}
