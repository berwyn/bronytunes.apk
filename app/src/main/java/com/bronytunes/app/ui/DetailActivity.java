package com.bronytunes.app.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.bronytunes.API;
import com.bronytunes.app.R;

import javax.inject.Inject;

public class DetailActivity extends ActionBarActivity {

    API api;

    private Fragment displayFragment;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        toolbar = ((Toolbar) findViewById(R.id.toolbar));
        setSupportActionBar(toolbar);

        api = new API.Builder().build();

        if (getIntent().getData() == null) {
            finish();
        }

        String resource;
        int id;
        Uri uri = getIntent().getData();
        if (uri.getScheme().equals("bronytunes")) {
            resource = uri.getHost();
            id = Integer.parseInt(uri.getPath().replaceFirst("/", ""), 10);
        } else {
            // The path leads with a '/', so we want to remove that and split
            // into its individual components
            String[] parts = uri.getPath().substring(1).split("/");
            resource = parts[0];
            // Our path slug is in the format id-name-of-song
            String[] slugParts = parts[1].split("-");
            id = Integer.parseInt(slugParts[0], 10);
        }

        switch(resource) {
            default:
            case "songs":
                displayFragment = SongDetailFragment.newInstance(id);
                break;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, displayFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
