package com.bronytunes.app.data.prefs;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * With <3 from JakeWharton/u2020
 */
public class NetworkProxyPreference extends StringPreference {
    public NetworkProxyPreference(SharedPreferences prefs, String key) {
        super(prefs, key);
    }

    /** Creates a {@code Proxy} for the current host to pass to a {@code Client}. */
    public @Nullable Proxy getProxy() {
        if(!isSet()) return null;

        String[] parts = get().split(":", 2);
        String host = parts[0];
        int port = parts.length > 1 ? Integer.parseInt(parts[1]) : 80;

        return new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(host, port));
    }
}
