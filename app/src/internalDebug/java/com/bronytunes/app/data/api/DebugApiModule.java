package com.bronytunes.app.data.api;

import android.content.SharedPreferences;

import com.bronytunes.API;
import com.bronytunes.app.data.ApiEndpoint;
import com.bronytunes.app.data.IsMockMode;
import com.bronytunes.app.data.prefs.StringPreference;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.MockRestAdapter;
import retrofit.RestAdapter;
import retrofit.android.AndroidMockValuePersistence;

@Module(
        complete = false,
        library = true,
        overrides = true
)
public final class DebugApiModule {

    @Provides
    @Singleton
    Endpoint provideEndpoint(@ApiEndpoint StringPreference apiEndpoint) {
        return Endpoints.newFixedEndpoint(apiEndpoint.get());
    }

    @Provides
    @Singleton
    @Named("Api")
    OkHttpClient provideApiClient(OkHttpClient client, LoggingInterceptor loggingInterceptor) {
        client = client.clone();
        client.interceptors().add(loggingInterceptor);
        return client;
    }


    @Provides
    @Singleton
    MockRestAdapter provideMockRestAdapter(RestAdapter restAdapter, SharedPreferences preferences) {
        MockRestAdapter mockRestAdapter = MockRestAdapter.from(restAdapter);
        AndroidMockValuePersistence.install(mockRestAdapter, preferences);
        return mockRestAdapter;
    }

    @Provides
    @Singleton
    API provideAPI(RestAdapter restAdapter, MockRestAdapter mockRestAdapter,
                   @IsMockMode boolean isMockMode, MockBronyTunesService mockService) {
        if (isMockMode) {
            return mockRestAdapter.create(API.class, mockService);
        }
        return restAdapter.create(API.class);
    }
}
