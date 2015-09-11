package com.bronytunes.app.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bronytunes.app.R;
import com.bronytunes.app.data.Injector;

import java.util.HashMap;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import dagger.ObjectGraph;

public class MainActivity extends AppCompatActivity implements TrackListingFragment.TrackListeningCallbacks {

    private static final int    NUM_FRAGMENTS      = 3;
    private static final String BUNDLE_KEY_FRAG_ID = "fragId";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    @Bind(R.id.action_bar)
    Toolbar        toolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout   drawer;
    @Bind(R.id.nav_view)
    NavigationView nav;
    @Bind(R.id.main_content)
    View           content;

    @Inject
    AppContainer appContainer;

    private String[]              tabLabels;
    private SparseArray<Fragment> fragments;
    private ObjectGraph           activityGraph;
    private int                   fragId;

    @Override
    public Object getSystemService(@NonNull String name) {
        if (Injector.matchesService(name)) {
            return activityGraph;
        }
        return super.getSystemService(name);
    }

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        fragments = new SparseArray<>(NUM_FRAGMENTS);

        // Explicitly reference the application object since we don't want to match our own injector.
        activityGraph = Injector.obtain(getApplication());
        activityGraph.inject(this);

        ViewGroup container = appContainer.bind(this);
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.activity_main, container);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setupDrawerContent();

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        if (icicle != null) restoreState(icicle);
    }

    private void setupDrawerContent() {
        nav.setNavigationItemSelectedListener(menuItem -> {
            Integer id = menuItem.getItemId();

            switch (id) {
                default:
                    loadFragment(id, menuItem);
                    break;
                case R.id.offline_mode:
                    // TODO - toggle offline mode?
                    // Should look into action views
                    Snackbar.make(content, menuItem.getTitle(), Snackbar.LENGTH_LONG)
                            .show();
                    break;
                case R.id.settings:
                    startActivity(new Intent(this, SettingsActivity.class));
                    break;
            }

            drawer.closeDrawers();
            return false;
        });
    }

    private void restoreState(@NonNull Bundle icicle) {
        int fragId = icicle.getInt(BUNDLE_KEY_FRAG_ID, R.id.listen_now);
        MenuItem item = nav.getMenu().findItem(this.fragId);
        loadFragment(this.fragId, item);
    }

    private Fragment createFragment(Integer id) {
        Fragment frag;
        switch (id) {
            case R.id.listen_now:
                // TODO - Create a new instance of the appropriate fragment
                frag = ListenNowFragment.newInstance();
                break;
            case R.id.radio:
                // TODO - Create a new instance of the appropriate fragment
                frag = new Fragment();
                break;
            case R.id.library:
                // TODO - Create a new instance of the appropriate fragment
                frag = new Fragment();
                break;
            default:
                throw new RuntimeException("Unknown drawer fragment clicked");
        }
        fragments.put(id, frag);
        return frag;
    }

    private void loadFragment(Integer id, MenuItem item) {
        this.fragId = id;
        item.setChecked(true);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_host, fragments.get(id, createFragment(id)));
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_KEY_FRAG_ID, this.fragId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // TODO - Fragment callback fleshout
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private TrackListingFragment[] fragments;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

            fragments = new TrackListingFragment[]{
                    TrackListingFragment.newInstance(TrackListingFragment.ENDPOINT_FEATURED, MainActivity.this),
                    TrackListingFragment.newInstance(TrackListingFragment.ENDPOINT_TRENDING, MainActivity.this),
                    TrackListingFragment.newInstance(TrackListingFragment.ENDPOINT_NEW, MainActivity.this)
            };
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            return tabLabels[position].toUpperCase(l);
        }
    }
}
