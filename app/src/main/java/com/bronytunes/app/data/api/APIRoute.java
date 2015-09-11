package com.bronytunes.app.data.api;

import android.content.Context;

import com.bronytunes.app.data.Injector;

import javax.inject.Inject;

import retrofit.Endpoint;

/**
 * Created by berwyn on 10/09/2015.
 */
public class APIRoute {

    private static APIRoute instance;

    public static APIRoute getInstance(Context context) {
        if(instance == null) {
            instance = new APIRoute(context);
        }
        return instance;
    }

    @Inject
    Endpoint endpoint;

    private APIRoute(Context context) {
        Injector.obtain(context).inject(this);
    }

    public String albumArt(int id, String aspect, int size) {
        return endpoint.getUrl() + "/retrieve_artwork.php?song_id=" + id + "&aspect=" + aspect + "&size" + size;
    }

}
