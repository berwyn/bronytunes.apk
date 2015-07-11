package com.bronytunes.app.data;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * With <3 from JakeWharton/u2020
 */
@Qualifier
@Retention(RUNTIME)
public @interface ApiEndpoint {
}
