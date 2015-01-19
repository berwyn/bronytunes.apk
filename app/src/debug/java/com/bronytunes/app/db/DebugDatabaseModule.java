package com.bronytunes.app.db;

import dagger.Module;

/**
 * Created by berwyn on 18/01/15.
 */
@Module(
        addsTo = DatabaseModule.class,
        overrides = true,
        complete = false,
        library = true
)
public class DebugDatabaseModule {
}
