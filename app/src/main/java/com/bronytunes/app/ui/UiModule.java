package com.bronytunes.app.ui;

import com.bronytunes.app.ui.adapters.ListenNowAdapter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by berwyn on 03/09/14.
 */
@Module(
        injects = {
                MainActivity.class,
                ListenNowAdapter.class
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
