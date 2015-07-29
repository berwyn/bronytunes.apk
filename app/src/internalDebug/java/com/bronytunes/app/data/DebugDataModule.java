package com.bronytunes.app.data;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.bronytunes.app.data.api.DebugApiModule;
import com.bronytunes.app.data.prefs.BooleanPreference;
import com.bronytunes.app.data.prefs.IntPreference;
import com.bronytunes.app.data.prefs.NetworkProxyPreference;
import com.bronytunes.app.data.prefs.RxSharedPreferences;
import com.bronytunes.app.data.prefs.StringPreference;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import retrofit.MockRestAdapter;
import rx.Observable;
import timber.log.Timber;

/**
 * Created by berwyn on 22/07/2015.
 */
@Module(
        includes = {DebugApiModule.class},
        complete = false,
        library = true,
        overrides = true
)
public class DebugDataModule {
    private static final int     DEFAULT_ANIMATION_SPEED           = 1; // 1x (normal) speed.
    private static final boolean DEFAULT_PICASSO_DEBUGGING         = false; // Debug indicators displayed
    private static final boolean DEFAULT_PIXEL_GRID_ENABLED        = false; // No pixel grid overlay.
    private static final boolean DEFAULT_PIXEL_RATIO_ENABLED       = false; // No pixel ratio overlay.
    private static final boolean DEFAULT_SCALPEL_ENABLED           = false; // No crazy 3D view tree.
    private static final boolean DEFAULT_SCALPEL_WIREFRAME_ENABLED = false; // Draw views by default.
    private static final boolean DEFAULT_SEEN_DEBUG_DRAWER         = false; // Show debug drawer first time.
    private static final boolean DEFAULT_CAPTURE_INTENTS           = true; // Capture external intents.

    @Provides
    @Singleton
    RxSharedPreferences provideRxSharedPreferences(SharedPreferences preferences) {
        return RxSharedPreferences.create(preferences);
    }

    @Provides
    @Singleton
    IntentFactory provideIntentFactory(@IsMockMode boolean isMockMode,
                                       @CaptureIntents BooleanPreference captureIntents) {
        return new DebugIntentFactory(IntentFactory.REAL, isMockMode, captureIntents);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Application app,
                                     NetworkProxyPreference networkProxy) {
        OkHttpClient client = DataModule.createOkHttpClient(app);
        client.setSslSocketFactory(createBadSslSocketFactory());
        client.setProxy(networkProxy.getProxy());
        return client;
    }

    private static SSLSocketFactory createBadSslSocketFactory() {
        try {
            // Construct SSLSocketFactory that accepts any cert.
            SSLContext context = SSLContext.getInstance("TLS");
            TrustManager permissive = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            context.init(null, new TrustManager[]{permissive}, null);
            return context.getSocketFactory();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    @Provides
    @Singleton
    @ApiEndpoint
    StringPreference provideEndpointPreference(SharedPreferences preferences) {
        return new StringPreference(preferences, "debug_endpoint", ApiEndpoints.MOCK_MODE.url);
    }

    @Provides
    @Singleton
    @IsMockMode
    boolean provideIsMockMode(@ApiEndpoint StringPreference endpoint) {
        return ApiEndpoints.isMockMode(endpoint.get());
    }

    @Provides
    @Singleton
    NetworkProxyPreference provideNetworkProxy(SharedPreferences preferences) {
        return new NetworkProxyPreference(preferences, "debug_network_proxy");
    }

    @Provides
    @Singleton
    @CaptureIntents
    BooleanPreference provideCaptureIntentsPreference(SharedPreferences preferences) {
        return new BooleanPreference(preferences, "debug_capture_intents", DEFAULT_CAPTURE_INTENTS);
    }

    @Provides
    @Singleton
    @AnimationSpeed
    IntPreference provideAnimationSpeed(SharedPreferences preferences) {
        return new IntPreference(preferences, "debug_animation_speed", DEFAULT_ANIMATION_SPEED);
    }

    @Provides
    @Singleton
    @PicassoDebugging
    BooleanPreference providePicassoDebugging(SharedPreferences preferences) {
        return new BooleanPreference(preferences, "debug_picasso_debugging", DEFAULT_PICASSO_DEBUGGING);
    }

    @Provides
    @Singleton
    @PixelGridEnabled
    BooleanPreference providePixelGridEnabled(SharedPreferences preferences) {
        return new BooleanPreference(preferences, "debug_pixel_grid_enabled",
                DEFAULT_PIXEL_GRID_ENABLED);
    }

    @Provides
    @Singleton
    @PixelGridEnabled
    Observable<Boolean> provideObservablePixelGridEnabled(RxSharedPreferences preferences) {
        return preferences.getBoolean("debug_pixel_grid_enabled", DEFAULT_PIXEL_GRID_ENABLED);
    }

    @Provides
    @Singleton
    @PixelRatioEnabled
    BooleanPreference providePixelRatioEnabled(SharedPreferences preferences) {
        return new BooleanPreference(preferences, "debug_pixel_ratio_enabled",
                DEFAULT_PIXEL_RATIO_ENABLED);
    }

    @Provides
    @Singleton
    @PixelRatioEnabled
    Observable<Boolean> provideObservablePixelRatioEnabled(RxSharedPreferences preferences) {
        return preferences.getBoolean("debug_pixel_ratio_enabled", DEFAULT_PIXEL_RATIO_ENABLED);
    }

    @Provides
    @Singleton
    @SeenDebugDrawer
    BooleanPreference provideSeenDebugDrawer(SharedPreferences preferences) {
        return new BooleanPreference(preferences, "debug_seen_debug_drawer", DEFAULT_SEEN_DEBUG_DRAWER);
    }

    @Provides
    @Singleton
    @ScalpelEnabled
    BooleanPreference provideScalpelEnabled(SharedPreferences preferences) {
        return new BooleanPreference(preferences, "debug_scalpel_enabled", DEFAULT_SCALPEL_ENABLED);
    }

    @Provides
    @Singleton
    @ScalpelEnabled
    Observable<Boolean> provideObservableScalpelEnabled(RxSharedPreferences preferences) {
        return preferences.getBoolean("debug_scalpel_enabled", DEFAULT_SCALPEL_ENABLED);
    }

    @Provides
    @Singleton
    @ScalpelWireframeEnabled
    BooleanPreference provideScalpelWireframeEnabled(SharedPreferences preferences) {
        return new BooleanPreference(preferences, "debug_scalpel_wireframe_drawer",
                DEFAULT_SCALPEL_WIREFRAME_ENABLED);
    }

    @Provides
    @Singleton
    @ScalpelWireframeEnabled
    Observable<Boolean> provideObservableScalpelWireframeEnabled(RxSharedPreferences preferences) {
        return preferences.getBoolean("debug_scalpel_wireframe_drawer",
                DEFAULT_SCALPEL_WIREFRAME_ENABLED);
    }

    @Provides
    @Singleton
    Picasso providePicasso(OkHttpClient client, MockRestAdapter mockRestAdapter,
                           @IsMockMode boolean isMockMode, Application app) {
        Picasso.Builder builder = new Picasso.Builder(app).downloader(new OkHttpDownloader(client));
        if (isMockMode) {
            builder.addRequestHandler(new MockRequestHandler(mockRestAdapter, app.getAssets()));
        }
        builder.listener((picasso, uri, exception) -> Timber.e(exception, "Error while loading image " + uri));
        return builder.build();
    }
}
