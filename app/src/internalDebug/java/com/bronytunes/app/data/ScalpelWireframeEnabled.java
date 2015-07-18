package com.bronytunes.app.data;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * With <3 from JakeWharton/u2020
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ScalpelWireframeEnabled {
}
