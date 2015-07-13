package com.bronytunes.app.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.widget.TextView;

import com.bronytunes.app.R;
import com.bronytunes.app.ui.misc.Truss;
import com.bronytunes.app.util.Intents;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * With <3 from JakeWharton/u2020
 */
public class ExternalIntentActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    public static final String ACTION = "com.bronytunes.app.intent.EXTERNAL_INTENT";
    public static final String EXTRA_BASE_INTENT = "debug_base_intent";

    public static Intent createIntent(Intent baseIntent) {
        Intent intent = new Intent();
        intent.setAction(ACTION);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_BASE_INTENT, baseIntent);
        return intent;
    }

    @InjectView(R.id.toolbar)
    Toolbar  toolbarView;
    @InjectView(R.id.action)
    TextView actionView;
    @InjectView(R.id.data)
    TextView dataView;
    @InjectView(R.id.extras)
    TextView extrasView;

    private Intent baseIntent;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_external_intent);
        ButterKnife.inject(this);

        toolbarView.inflateMenu(R.menu.menu_external_intent);
        toolbarView.setOnMenuItemClickListener(this);

        baseIntent = getIntent().getParcelableExtra(EXTRA_BASE_INTENT);
        fillAction();
        fillData();
        fillExtras();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.debug_launch:
                if (Intents.maybeStartActivity(this, baseIntent)) {
                    finish();
                }
                return true;
            default:
                return false;
        }
    }

    public void fillAction() {
        String action = baseIntent.getAction();
        actionView.setText(action == null ? "None!" : action);
    }

    public void fillData() {
        Uri data = baseIntent.getData();
        dataView.setText(data == null ? "None!" : data.toString());
    }

    public void fillExtras() {
        Bundle extras = baseIntent.getExtras();
        if(extras == null) {
            extrasView.setText("None!");
        } else {
            Truss truss = new Truss();
            for(String key : extras.keySet()) {
                Object value = extras.get(key);

                String valueString;
                if(value.getClass().isArray()) {
                    valueString = Arrays.toString((Object[]) value);
                } else {
                    valueString = value.toString();
                }

                truss.pushSpan(new StyleSpan(Typeface.BOLD));
                truss.append(key).append(":\n");
                truss.popSpan();
                truss.append(valueString).append("\n\n");
            }

            extrasView.setText(truss.build());
        }
    }
}
