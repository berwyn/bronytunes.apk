package com.bronytunes.app.ui;

import android.app.Activity;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * An indirection which allows controlling the root container used for each activity
 *
 * With <3 from JakeWharton/u2020
 */
public interface AppContainer {
    /** The root {@link ViewGroup} into which the activity should place its contents **/
    ViewGroup bind(Activity activity);

    /** An {@link AppContainer} which returns the normal activity content view **/
    AppContainer DEFAULT = activity -> ButterKnife.findById(activity, android.R.id.content);
}
