package com.bronytunes.app.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.bronytunes.app.R;
import com.bronytunes.app.data.Injector;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import dagger.ObjectGraph;

public class SettingsActivity extends AppCompatActivity {

    @Bind(R.id.action_bar)
    Toolbar toolbar;

    @Inject
    AppContainer appContainer;

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

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
