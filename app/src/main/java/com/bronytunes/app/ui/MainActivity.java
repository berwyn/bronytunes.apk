package com.bronytunes.app.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.bronytunes.app.R;
import com.bronytunes.app.data.Injector;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import dagger.ObjectGraph;

public class MainActivity extends AppCompatActivity implements TrackListingFragment.TrackListeningCallbacks {

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

    @Inject
    AppContainer appContainer;

    private String[]    tabLabels;
    private ObjectGraph activityGraph;

    @Override
    public Object getSystemService(@NonNull String name) {
        if (Injector.matchesService(name)) {
            return activityGraph;
        }
        return super.getSystemService(name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            case R.id.action_settings:
                return true;
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

    private void setupDrawerContent() {
        nav.setNavigationItemSelectedListener(menuItem -> {
            menuItem.setChecked(true);
            drawer.closeDrawers();
            return true;
        });
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
