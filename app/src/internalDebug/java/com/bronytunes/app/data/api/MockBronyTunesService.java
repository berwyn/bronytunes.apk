package com.bronytunes.app.data.api;

import android.content.SharedPreferences;

import com.bronytunes.API;
import com.bronytunes.model.Album;
import com.bronytunes.model.Song;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.client.Response;
import rx.Observable;

/**
 * Created by berwyn on 22/07/2015.
 */
@Singleton
public class MockBronyTunesService implements API {
    private final SharedPreferences preferences;

    @Inject
    public MockBronyTunesService(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public Observable<Album[]> getFeaturedAlbums() {
        return null;
    }

    @Override
    public Observable<Album[]> getTrendingAlbums() {
        return null;
    }

    @Override
    public Observable<Album[]> getNewAlbums() {
        return null;
    }

    @Override
    public Observable<Song[]> getLibrary() {
        return null;
    }

    @Override
    public Observable<Song> getSong(int i) {
        return null;
    }

    @Override
    public Observable<Response> getImage(int i) {
        return null;
    }
}
