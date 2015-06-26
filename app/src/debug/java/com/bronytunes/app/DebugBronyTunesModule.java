package com.bronytunes.app;

import com.bronytunes.app.db.DebugDatabaseModule;

import dagger.Module;

/**
 * Created by berwyn on 17.01.15.
 */
@Module(
        includes = {
                DebugDatabaseModule.class
        }
)
public class DebugBronyTunesModule {
}
