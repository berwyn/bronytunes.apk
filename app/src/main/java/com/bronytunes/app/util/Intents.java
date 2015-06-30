package com.bronytunes.app.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * With <3 from JakeWharton/u2020
 */
public final class Intents {
    private Intents(){}

    public static boolean maybeStartActivity(Context context, Intent intent) {
        if(hasHandler(context, intent)) {
            context.startActivity(intent);
            return true;
        } else { return false; }
    }

    public static boolean hasHandler(Context context, Intent intent) {
        List<ResolveInfo> handlers = context.getPackageManager().queryIntentActivities(intent, 0);
        return !handlers.isEmpty();
    }
}
