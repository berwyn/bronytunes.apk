package com.bronytunes.app;

import android.app.Application;
import android.support.annotation.NonNull;

import com.bronytunes.app.data.Injector;
import com.bronytunes.app.data.LumberYard;
import com.bronytunes.app.ui.ActivityHierarchyServer;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

import net.danlew.android.joda.JodaTimeAndroid;

import javax.inject.Inject;

import dagger.ObjectGraph;
import timber.log.Timber;

/**
 * An app delegate which will update the track DB when the
 * application launches and hosts the object graph for
 * dependency injection.
 */
public final class BronyTunesApp extends Application {

    @Inject
    ActivityHierarchyServer activityHierarchyServer;
    @Inject
    LumberYard              lumberYard;

    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        LeakCanary.install(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Stetho.initialize(Stetho.newInitializerBuilder(this)
                    .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                    .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                    .build());
        } else {
            // Crashalytics
        }

        objectGraph = ObjectGraph.create(Modules.list(this));
        objectGraph.inject(this);

        lumberYard.cleanUp();
        Timber.plant(lumberYard.tree());

        registerActivityLifecycleCallbacks(activityHierarchyServer);
    }

    @Override
    public Object getSystemService(@NonNull String name) {
        if (Injector.matchesService(name)) {
            return objectGraph;
        }
        return super.getSystemService(name);
    }
}
