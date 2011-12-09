package com.bloatit.framework.feedbackworker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.bloatit.framework.exceptions.highlevel.ExternalErrorException;
import com.bloatit.framework.model.ModelAccessor;

public class FeedbackServer extends Thread {

    private final List<FeedBackWorker<?>> workers = new ArrayList<FeedBackWorker<?>>();
    private final BlockingQueue<Object> messages = new LinkedBlockingQueue<Object>();

    private static FeedbackServer instance = null;

    public static synchronized FeedbackServer getInstance() {
        if (instance == null) {
            return instance = new FeedbackServer();
        }
        return instance;
    }

    private FeedbackServer() {
        // Nothing to do
    }

    public void addWorker(final FeedBackWorker<?> worker) {
        workers.add(worker);
    }

    /**
     * Must be launched before the start() method.
     */
    public void initialize() {
        // Nothing yet
    }

    public synchronized void sendMessage(final Object obj) {
        messages.add(obj);
    }

    @Override
    public void run() {
        for (;;) {
            Object element;
            try {
                element = messages.take();
                ModelAccessor.open();
                ModelAccessor.setReadOnly();
                if (element instanceof StopMessage) {
                    return;
                }
                for (final FeedBackWorker<?> worker : workers) {
                    if (worker.work(element)) {
                        break;
                    }
                }
                ModelAccessor.close();
            } catch (final InterruptedException e) {
                throw new ExternalErrorException(e);
            }
        }
    }

    /**
     * Every messages after this point will be trashed.
     */
    public void terminate() {
        sendMessage(new StopMessage());
    }

    private static class StopMessage {
    }

}
