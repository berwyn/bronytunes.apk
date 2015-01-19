package com.bronytunes.app.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bronytunes.API;
import com.bronytunes.app.BronyTunesApp;
import com.bronytunes.app.R;
import com.bronytunes.app.db.TrackDatabaseHelper;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.schedulers.Schedulers;

public class TestActivity extends ActionBarActivity {

    @Inject
    API                 api;
    @Inject
    TrackDatabaseHelper trackDB;
    @InjectView(R.id.tv_test)
    TextView            textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.inject(this);

        BronyTunesApp app = BronyTunesApp.get(this);
        app.inject(this);

        api.getLibrary()
           .subscribeOn(Schedulers.io())
           .observeOn(Schedulers.computation())
           .subscribe(tracks -> {
               trackDB.saveTrack(tracks);
               textView.post(() -> textView.setText("DB complete"));
           });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
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
