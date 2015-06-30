package com.bronytunes.app.data;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import timber.log.Timber;

import static com.jakewharton.byteunits.DecimalByteUnit.MEGABYTES;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by berwyn on 25/06/2015.
 */
@Module
public class DataModule {
    static final int DISK_CACHE_SIZE = (int) MEGABYTES.toBytes(50);

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Application app) {
        return app.getSharedPreferences("bronytunes", Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .create();
    }

    @Provides
    @Singleton
    IntentFactory provideIntentFactory() {
        return IntentFactory.REAL;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Application app) {
        return createOkClient(app);
    }

    @Provides
    @Singleton
    Picasso providePicasso(Application app, OkHttpClient okClient) {
        return new Picasso.Builder(app)
                .downloader(new OkHttpDownloader(okClient))
                .listener((picasso, uri, exception) -> Timber.e(exception, "Failed to load image: %s", uri))
                .build();
    }

    static OkHttpClient createOkClient(Application app) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(10, SECONDS);
        client.setReadTimeout(10, SECONDS);
        client.setWriteTimeout(10, SECONDS);

        File cacheDir = new File(app.getCacheDir(), "http");
        Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);
        client.setCache(cache);

        return client;
    }
}
