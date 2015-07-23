package com.bronytunes.app;

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
