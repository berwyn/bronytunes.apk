package com.bronytunes.app;

import android.app.Application;
import android.content.Context;

import java.util.concurrent.TimeUnit;

import timber.log.Timber;

/**
 * An app delegate which will update the track DB when the
 * application launches and hosts the object graph for
 * dependency injection.
 */
public class BronyTunesApp extends Application {

    private BronyTunesComponent objectGraph;

    public static BronyTunesApp get(Context context) {
        return (BronyTunesApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            // TODO Crashalytics
        }

        buildObjectGraphAndInject();
    }

    public void buildObjectGraphAndInject() {
        long start = System.nanoTime();

        if (BuildConfig.DEBUG) {
            objectGraph = DaggerDebugBronyTunesComponent.builder()
                                                        .build();
        } else {
            objectGraph = DaggerBronyTunesComponent.builder()
                                                   .build();
        }

        long diff = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        Timber.i("Global object graph creation took %sms", diff);
    }

    public BronyTunesComponent getObjectGraph() {
        return objectGraph;
    }
}
