package com.bronytunes.app;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by berwyn on 25/06/2015.
 */
@Singleton
@Component(modules = {
        BronyTunesModule.class,
        DebugBronyTunesModule.class
})
public interface DebugBronyTunesComponent extends BronyTunesComponent {
}
