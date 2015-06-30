package com.bronytunes.app.data;

import android.content.Intent;
import android.net.Uri;

/**
 * Created by berwyn on 29/06/2015.
 */
public interface IntentFactory {
    Intent createUrlIntent(String url);

    IntentFactory REAL = (url) -> {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return intent;
    };
}
