package com.bronytunes.app;

import com.bronytunes.API;
import com.bronytunes.app.db.TrackDatabaseHelper;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by berwyn on 25/06/2015.
 */
@Singleton
@Component(modules = BronyTunesModule.class)
public interface BronyTunesComponent {
    API getAPI();
    TrackDatabaseHelper getTrackDatabaseHelper();
}
