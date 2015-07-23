package com.bronytunes.app;

import com.bronytunes.app.data.DebugDataModule;
import com.bronytunes.app.ui.DebugUiModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by berwyn on 22/07/2015.
 */
@Module(
        addsTo = BronyTunesModule.class,
        includes = {
                DebugUiModule.class,
                DebugDataModule.class
        },
        overrides = true
)
public final class DebugBronyTunesModule {
    // Low-tech flag to force certain debug build behaviors when running in an instrumentation test.
    // This value is used in the creation of singletons so it must be set before the graph is created.
    static boolean instrumentationTest = false;

    @Provides
    @Singleton
    @IsInstrumentationTest boolean provideIsInstrumentationTest() {
        return instrumentationTest;
    }
}
