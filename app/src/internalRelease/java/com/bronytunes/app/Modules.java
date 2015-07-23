package com.jakewharton.u2020;

import com.bronytunes.app.BronyTunesModule;

final class Modules {
  static Object[] list(U2020App app) {
    return new Object[] {
        new BronyTunesModule(app),
        new InternalReleaseBronyTunesModule()
    };
  }

  private Modules() {
    // No instances.
  }
}
