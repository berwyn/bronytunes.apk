package com.bronytunes.app;

import android.app.Application;
import android.content.Context;

import java.util.concurrent.TimeUnit;

import dagger.ObjectGraph;
import timber.log.Timber;

/**
 * An app delegate which will update the track DB when the
 * application launches and hosts the object graph for
 * dependency injection.
 */
public class BronyTunesApp extends Application {

    private ObjectGraph objectGraph;

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

        objectGraph = ObjectGraph.create(Modules.list(this));
        objectGraph.inject(this);

        long diff = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        Timber.i("Global object graph creation took %sms", diff);
    }

    public void inject(Object o) {
        objectGraph.inject(o);
    }

    public static BronyTunesApp get(Context context) {
        return (BronyTunesApp) context.getApplicationContext();
    }
}
