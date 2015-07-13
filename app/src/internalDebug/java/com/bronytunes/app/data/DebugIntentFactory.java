package com.bronytunes.app.data;

import android.content.Intent;

import com.bronytunes.app.ui.ExternalIntentActivity;
import com.bronytunes.app.data.prefs.BooleanPreference;

/**
 * Created by berwyn on 29/06/2015.
 */
public class DebugIntentFactory implements IntentFactory {
    private final IntentFactory realIntentFactory;
    private final boolean isMockMode;
    private final BooleanPreference captureIntents;

    public DebugIntentFactory(IntentFactory realIntentFactory,
                              boolean isMockMode,
                              BooleanPreference captureIntents) {
        this.realIntentFactory = realIntentFactory;
        this.isMockMode = isMockMode;
        this.captureIntents = captureIntents;
    }

    @Override
    public Intent createUrlIntent(String url) {
        Intent baseIntent = realIntentFactory.createUrlIntent(url);
        if(!isMockMode || !captureIntents.get()) {
            return baseIntent;
        } else {
            return ExternalIntentActivity.createIntent(baseIntent);
        }
    }
}
