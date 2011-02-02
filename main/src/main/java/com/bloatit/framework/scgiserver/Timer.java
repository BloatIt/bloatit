package com.bloatit.framework.scgiserver;

final class Timer {

    private long startTime;

    public Timer() {
        startTime = 0;
    }

    public void start() {
        reset();
    }

    public long elapsed() {
        try {
            long endTime = System.currentTimeMillis();
            return endTime - startTime;
        } finally {
            reset();
        }
    }

    public void reset() {
        startTime = System.currentTimeMillis();
    }

}
