package com.bloatit.model;

import java.util.concurrent.Semaphore;

import com.bloatit.data.Data;
import com.bloatit.framework.mailsender.MailServer;

public class Model {
    private static Semaphore mutex = new Semaphore(1, false);

    private Model() {
        // do nothing
    }

    public static void launch() {
        MailServer.init();
    }

    public static void shutdown() {
        Data.shutdown();
    }

    public static void openReadOnly() {
        Data.openReadOnly();
    }

    public static void open() {
        Data.open();
    }

    public static void close() {
        Data.close();
    }

    /**
     * Reserve the Data and make sure nobody else is using it.
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
