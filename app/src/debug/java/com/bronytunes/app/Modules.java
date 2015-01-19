package com.bronytunes.app;

/**
 * Created by berwyn on 17.01.15.
 */
public class Modules {
    public static Object[] list(BronyTunesApp app) {
        return new Object[] {
            new BronyTunesModule(app),
            new DebugBronyTunesModule()
        };
    }
}
