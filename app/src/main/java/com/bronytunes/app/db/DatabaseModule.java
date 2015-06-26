package com.bronytunes.app.db;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * An injector module for DB-related classes
 */
@Module
public class DatabaseModule {

    /**
     * Provides an instance of the Track DB helper, given a context to run inside of
     *
     * @param app The application context the DB helper operates out of
     * @return a DB helper related to track management
     */
    @Provides
    @Singleton
    TrackDatabaseHelper provideTrackDatabaseHeloper(Application app) {
        return new TrackDatabaseHelper(app);
    }

}
