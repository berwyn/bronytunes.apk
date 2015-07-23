package com.bronytunes.app;

import com.bronytunes.app.BronyTunesApp;
import com.bronytunes.app.BronyTunesModule;

final class Modules {
  static Object[] list(BronyTunesApp app) {
    return new Object[] {
        new BronyTunesModule(app)
    };
  }

  private Modules() {
    // No instances.
  }
}
