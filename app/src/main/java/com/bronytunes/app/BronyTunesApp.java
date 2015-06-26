package com.bronytunes.app;

import android.app.Application;
import android.content.Context;

import com.bronytunes.app.ui.ActivityHierarchyServer;
import com.bronytunes.app.data.LumberYard;
import com.squareup.leakcanary.LeakCanary;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * An app delegate which will update the track DB when the
 * application launches and hosts the object graph for
 * dependency injection.
 */
public class BronyTunesApp extends Application {

    @Inject
    ActivityHierarchyServer activityHierarchyServer;
    @Inject
    LumberYard              lumberYard;

    private BronyTunesComponent objectGraph;

    public static BronyTunesApp get(Context context) {
        return (BronyTunesApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            objectGraph = DaggerDebugBronyTunesComponent.builder()
                                                        .build();
        } else {
            // TODO Crashalytics
            objectGraph = DaggerBronyTunesComponent.builder()
                                                   .build();
        }

        lumberYard.cleanUp();
        Timber.plant(lumberYard.tree());

        registerActivityLifecycleCallbacks(activityHierarchyServer);
    }

    public BronyTunesComponent getObjectGraph() {
        return objectGraph;
    }
}
