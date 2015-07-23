package com.bronytunes.app;

final class Modules {
  static Object[] list(BronyTunesApp app) {
    return new Object[] {
        new BronyTunesModule(app),
        new com.jakewharton.u2020.InternalReleaseBronyTunesModule()
    };
  }

  private Modules() {
    // No instances.
  }
}
