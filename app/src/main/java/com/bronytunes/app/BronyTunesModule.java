package com.bronytunes.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.bronytunes.app.data.DataModule;
import com.bronytunes.app.db.DatabaseModule;
import com.bronytunes.app.net.NetModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by berwyn on 03/09/14.
 */
@Module(
        includes = {
                DataModule.class,
                DatabaseModule.class,
                NetModule.class
        }
)
public class BronyTunesModule {

    private final BronyTunesApp app;
    public BronyTunesModule(BronyTunesApp app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return app;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPrefs(Application app) {
        return app.getSharedPreferences("bronytunes", Context.MODE_PRIVATE);
    }

}
