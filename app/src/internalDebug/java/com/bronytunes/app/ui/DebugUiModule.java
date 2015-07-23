package com.bronytunes.app.ui;

import com.bronytunes.app.IsInstrumentationTest;
import com.bronytunes.app.ui.debug.DebugAppContainer;
import com.bronytunes.app.ui.debug.DebugView;
import com.bronytunes.app.ui.debug.SocketActivityHierarchyServer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                DebugAppContainer.class,
                DebugView.class,
        },
        complete = false,
        library = true,
        overrides = true
)
public class DebugUiModule {
    @Provides
    @Singleton
    AppContainer provideAppContainer(DebugAppContainer debugAppContainer,
                                     @IsInstrumentationTest boolean isInstrumentationTest) {
        // Do not add the debug controls for when we are running inside of an instrumentation test.
        return isInstrumentationTest ? AppContainer.DEFAULT : debugAppContainer;
    }

    @Provides
    @Singleton
    ActivityHierarchyServer provideActivityHierarchyServer() {
        return new SocketActivityHierarchyServer();
    }
}
