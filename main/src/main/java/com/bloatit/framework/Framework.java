package com.bloatit.framework;

import java.util.concurrent.Semaphore;

import com.bloatit.mail.MailServer;

public class Framework {
    private static Semaphore mutex = new Semaphore(1, false);
    private Framework() {
        // do nothing
    }

    public static void launch(){
        MailServer.init();
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
