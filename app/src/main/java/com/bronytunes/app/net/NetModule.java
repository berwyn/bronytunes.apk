package com.bronytunes.app.net;

import com.bronytunes.API;
import com.bronytunes.app.BuildConfig;
import com.bronytunes.app.ui.TestActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

/**
 * Created by berwyn on 03/09/14.
 */
@Module(
        injects = {
                TestActivity.class
        },
        complete = false,
        library = true
)
public class NetModule {

    @Provides
    @Singleton
    API provideAPI() {
        RestAdapter.LogLevel logLevel = BuildConfig.DEBUG
                ? RestAdapter.LogLevel.FULL
                : RestAdapter.LogLevel.NONE;

        return new API.Builder()
                .setLogLevel(logLevel)
                .build();
    }

}
