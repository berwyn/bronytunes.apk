package com.bronytunes.app;

/**
 * Created by berwyn on 22/07/2015.
 */
public class Modules {
    static Object[] list(BronyTunesApp app) {
        return new Object[] {
                new BronyTunesModule(app),
                new DebugBronyTunesModule()
        };
    }

    private Modules() {
        // No instances.
    }
}
