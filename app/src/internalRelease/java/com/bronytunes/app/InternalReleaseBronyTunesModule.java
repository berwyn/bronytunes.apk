package com.jakewharton.u2020;

import com.bronytunes.app.BronyTunesModule;
import com.bronytunes.app.ui.InternalReleaseUiModule;
import dagger.Module;

@Module(
    addsTo = BronyTunesModule.class,
    includes = InternalReleaseUiModule.class,
    overrides = true
)
public final class InternalReleaseBronyTunesModule {
}
