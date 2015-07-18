package com.bronytunes.app.data.prefs;

import android.content.SharedPreferences;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

/**
 * Created by berwyn on 17/07/2015.
 */
public final class RxSharedPreferences {
    private final SharedPreferences  sharedPreferences;
    private final Observable<String> changedKeys;

    private RxSharedPreferences(final SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        this.changedKeys = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                final SharedPreferences.OnSharedPreferenceChangeListener listener
                        = (sharedPreferences1, key) -> subscriber.onNext(key);

                Subscription subscription = Subscriptions.create(
                        () -> sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener));

                subscriber.add(subscription);

                sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
            }
        }).share();
    }

    public static RxSharedPreferences create(SharedPreferences prefs) {
        return new RxSharedPreferences(prefs);
    }

    public Observable<String> getString(String key) {
        return getString(key, null);
    }

    public Observable<String> getString(String key, final String defaultValue) {
        return changedKeys.filter(matchesKey(key))
                          .startWith(key)
                          .map(changedKey -> sharedPreferences.getString(changedKey, defaultValue));
    }

    private static Func1<String, Boolean> matchesKey(final String key) {
        return value -> key.equals(value);
    }

    public Action1<String> setString(final String key) {
        return string -> sharedPreferences.edit().putString(key, string).apply();
    }

    public Observable<Boolean> getBoolean(String key) {
        return getBoolean(key, null);
    }

    public Observable<Boolean> getBoolean(String key, final Boolean defaultValue) {
        return changedKeys.filter(matchesKey(key))
                          .startWith(key)
                          .map(changedKey -> sharedPreferences.getBoolean(changedKey, defaultValue));
    }

    public Action1<Boolean> setBoolean(final String key) {
        return bool -> sharedPreferences.edit().putBoolean(key, bool).apply();
    }

    public Observable<Integer> getInt(String key) {
        return getInt(key, null);
    }

    public Observable<Integer> getInt(String key, final Integer defaultValue) {
        return changedKeys.filter(matchesKey(key))
                          .startWith(key)
                          .map(changedKey -> sharedPreferences.getInt(changedKey, defaultValue));
    }
}
