package com.bloatit.framework.xcgiserver;

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
            final long endTime = System.currentTimeMillis();
            return endTime - startTime;
        } finally {
            reset();
        }
    }

    private void reset() {
        startTime = System.currentTimeMillis();
    }

}
