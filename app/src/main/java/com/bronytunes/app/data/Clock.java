package com.bronytunes.app.data;

/**
 * Created by berwyn on 22/07/2015.
 */
public interface Clock {
    long millis();
    long nanos();

    Clock REAL = new Clock() {
        @Override public long millis() {
            return System.currentTimeMillis();
        }

        @Override public long nanos() {
            return System.nanoTime();
        }
    };
}