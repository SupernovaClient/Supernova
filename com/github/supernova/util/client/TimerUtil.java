package com.github.supernova.util.client;

public class TimerUtil {
    private long initTime;

    public TimerUtil() {
        this.initTime = System.currentTimeMillis();
    }

    public boolean elapsed(long time) {
        long totalElapsed = System.currentTimeMillis() - initTime;
        return totalElapsed >= time;
    }
    public boolean elapsed(long time, boolean reset) {
        long totalElapsed = System.currentTimeMillis() - initTime;
        if(totalElapsed >= time) {
            if(reset) {
                initTime = System.currentTimeMillis();
            }
            return true;
        }
        return false;
    }
    public long elapsed() {
        return System.currentTimeMillis() - initTime;
    }
    public void reset() {
        initTime = System.currentTimeMillis();
    }
}
