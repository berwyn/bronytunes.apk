package com.bronytunes.app.data;

import android.content.Context;

import dagger.ObjectGraph;

/**
 * Created by berwyn on 22/07/2015.
 */
public final class Injector {
    private static final String INJECTOR_SERVICE = "com.bronytunes.app.injector";

    @SuppressWarnings("ResourceType") // Explicitly doing a custom service.
    public static ObjectGraph obtain(Context context) {
        return (ObjectGraph) context.getSystemService(INJECTOR_SERVICE);
    }

    public static boolean matchesService(String name) {
        return INJECTOR_SERVICE.equals(name);
    }

    private Injector() {
        throw new AssertionError("No instances.");
    }
}
