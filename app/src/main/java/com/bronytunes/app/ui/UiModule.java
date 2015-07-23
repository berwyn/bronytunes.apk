package com.bronytunes.app.ui;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by berwyn on 03/09/14.
 */
@Module(
        injects = {
                MainActivity.class,
        },
        complete = false,
        library = true
)
public final class UiModule {
    @Provides
    @Singleton
    AppContainer provideAppContainer() {
        return AppContainer.DEFAULT;
    }

    @Provides
    @Singleton
    ActivityHierarchyServer provideActivityHierarchyServer() {
        return ActivityHierarchyServer.NONE;
    }
}
