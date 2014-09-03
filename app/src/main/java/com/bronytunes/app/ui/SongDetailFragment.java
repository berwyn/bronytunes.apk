package com.bronytunes.app.ui;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bronytunes.app.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SongDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class SongDetailFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SONG_ID = "song_id";

    private int songId;

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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            songId = getArguments().getInt(ARG_SONG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_song_detail, container, false);
    }


}
