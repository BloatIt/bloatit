package com.bloatit.model;

import java.util.concurrent.Semaphore;

import com.bloatit.data.Model;
import com.bloatit.framework.mailsender.MailServer;

public class Framework {
    private static Semaphore mutex = new Semaphore(1, false);

    private Framework() {
        // do nothing
    }

    public static void launch() {
        MailServer.init();
    }

    public static void shutdown() {
        Model.shutdown();
    }

    public static void openReadOnly() {
        Model.openReadOnly();
    }

    public static void open() {
        Model.open();
    }

    public static void close() {
        Model.close();
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
