package com.bronytunes.app.data.api;

import com.bronytunes.API;
import com.bronytunes.app.BuildConfig;
import com.bronytunes.app.ui.adapters.ListenNowFeaturedItem;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * A module encapsulating API-related resources
 */
@Module(
        complete = false,
        library = true,
        injects = {
                APIRoute.class,
                ListenNowFeaturedItem.HeaderListAdapter.class
        }
)
public class ApiModule {
    public static final String PRODUCTION_API_URL = "https://api.github.com";

    @Provides
    @Singleton
    Endpoint provideEndpoint() {
        return Endpoints.newFixedEndpoint(PRODUCTION_API_URL);
    }

    @Provides
    @Singleton
    @Named("Api")
    OkHttpClient provideApiClient(OkHttpClient client) {
        return client.clone();
    }

    @Provides
    @Singleton
    RestAdapter provideRestAdapter(Endpoint endpoint,
                                   @Named("Api") OkHttpClient client, Gson gson) {
        return new RestAdapter.Builder()
                .setClient(new OkClient(client))
                .setEndpoint(endpoint)
                .setConverter(new GsonConverter(gson))
                .setRequestInterceptor(request -> request.addHeader("User-Agent", "bronytunes.apk/" + BuildConfig.VERSION_NAME))
                .build();
    }

    @Provides
    @Singleton
    API provideAPI(RestAdapter restAdapter) {
        return restAdapter.create(API.class);
    }
}
