package com.bronytunes.app.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.graphics.PaletteItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bronytunes.API;
import com.bronytunes.app.R;
import com.bronytunes.model.Song;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Subscription;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SongDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class SongDetailFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SONG_ID = "song_id";

    @InjectView(R.id.song_detail_root)
    View               contentRoot;
    TextView title;
    TextView artist;
    @InjectView(R.id.view_splash)
    ImageView splashArt;
    ImageView albumArt;

    private int                   songId;
    private API                   api;
    private Subscription          apiSubscription;
    private Song                  song;
    private Palette               palette;
    private Context               context;
    private FadingActionBarHelper fadingActionbarHelper;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param songId The id of the song to display.
     * @return A new instance of fragment SongDetailFragment.
     */
    public static SongDetailFragment newInstance(int songId) {
        SongDetailFragment fragment = new SongDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SONG_ID, songId);
        fragment.setArguments(args);
        return fragment;
    }

    public SongDetailFragment() {
        // Required empty public constructor
        api = new API.Builder().build();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            songId = getArguments().getInt(ARG_SONG_ID);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
        fadingActionbarHelper = new FadingActionBarHelper()
                .actionBarBackground(R.drawable.ab_solid_apptheme)
                .headerLayout(R.layout.view_artist_splash)
                .contentLayout(R.layout.fragment_song_detail);
        fadingActionbarHelper.initActionBar(activity);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View content = fadingActionbarHelper.createView(inflater);
        ButterKnife.inject(this, content);

//        apiSubscription = api
//                .getSong(songId)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(song -> {
//                    apiSubscription.unsubscribe();
//                    bindViews(song);
//                });

        return content;
    }

    public void bindViews(Song song) {
        this.song = song;

        if(title == null) return;
        title.setText(song.name);
        artist.setText("PsychGoth");
        Picasso.with(context)
                .load("http://bronytunes.com/retrieve_artwork.php?song_id=" + songId)
                .transform(new Transformation() {
                    @Override
                    public Bitmap transform(Bitmap source) {
                        Palette.generateAsync(source, 24, SongDetailFragment.this::colorise);
                        return source;
                    }

                    @Override
                    public String key() {
                        return "Palette";
                    }
                })
                .into(albumArt);
    }

    public void colorise(Palette palette) {
        if(title == null) return;
        PaletteItem background = palette.getDarkMutedColor();
        PaletteItem primaryText = palette.getLightVibrantColor();
        PaletteItem secondaryText = palette.getLightMutedColor();

        if(background != null) {
            contentRoot.setBackgroundColor(background.getRgb());
        }

        if(primaryText != null) {
            title.setTextColor(primaryText.getRgb());
        }

        if(secondaryText != null) {
            artist.setTextColor(secondaryText.getRgb());
        }
    }

}
