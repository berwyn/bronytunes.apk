package com.bronytunes.app.util;

import android.content.SharedPreferences;

/**
 * With <3 from JakeWharton/u2020
 */
public final class EnumPreferences {
    private EnumPreferences(){}

    public static <T extends Enum<T>> T getValue(SharedPreferences prefs,
                                                 Class<T> type,
                                                 String key,
                                                 T defaultValue) {
        String name = prefs.getString(key, null);
        if(name != null) {
            try {
                return Enum.valueOf(type, name);
            } catch (IllegalArgumentException ignored){}
        }
        return defaultValue;
    }

    public static void saveEnumValue(SharedPreferences prefs, String key, Enum value) {
        prefs.edit().putString(key, value.name()).apply();
    }
}
