package com.bronytunes.app.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bronytunes.API;
import com.bronytunes.app.BronyTunesApp;
import com.bronytunes.app.R;
import com.bronytunes.model.Album;
import com.squareup.picasso.Picasso;

import org.lucasr.twowayview.TwoWayLayoutManager;
import org.lucasr.twowayview.widget.ListLayoutManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.bronytunes.app.ui.TrackListingFragment.TrackListeningCallbacks} interface
 * to handle interaction events.
 * Use the {@link TrackListingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackListingFragment extends Fragment {

    public static final int ENDPOINT_FEATURED = 0;
    public static final int ENDPOINT_TRENDING = 1;
    public static final int ENDPOINT_NEW      = 2;

    private static final String ARG_ENDPOINT = "endpoint";

    @Inject
    API          api;
    @InjectView(R.id.list)
    RecyclerView listView;

    private int                        endpoint;
    private RecyclerView.LayoutManager layoutManager;
    private TrackAdapter               adapter;

    private TrackListeningCallbacks mListener;

    /**
     * Creates a new fragment instance for the given endpoint
     *
     * @param endpoint {@link #ENDPOINT_FEATURED} | {@link #ENDPOINT_TRENDING} | {@link #ENDPOINT_NEW}
     * @return A new instance of the fragment for the specified endpoint
     */
    public static TrackListingFragment newInstance(int endpoint) {
        TrackListingFragment fragment = new TrackListingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ENDPOINT, endpoint);
        fragment.setArguments(args);
        return fragment;
    }

    public TrackListingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            endpoint = getArguments().getInt(ARG_ENDPOINT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_track_listing, container, false);
        ButterKnife.inject(this, layout);

        adapter = new TrackAdapter();

        switch (endpoint) {
            default:
            case ENDPOINT_FEATURED:
                layoutManager = new ListLayoutManager(getActivity(), TwoWayLayoutManager.Orientation.VERTICAL);
                api.getFeaturedAlbums()
                   .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .map(albums -> Arrays.copyOfRange(albums, 0, 10))
                   .subscribe(adapter::setData);
                break;

            case ENDPOINT_TRENDING:
                layoutManager = new ListLayoutManager(getActivity(), TwoWayLayoutManager.Orientation.VERTICAL);
                api.getTrendingAlbums()
                   .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .map(albums -> Arrays.copyOfRange(albums, 0, 10))
                   .subscribe(adapter::setData);
                break;

            case ENDPOINT_NEW:
                int width = layout.getMeasuredWidth();
                DisplayMetrics metrics = getResources().getDisplayMetrics();
                float dpWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, metrics);

                int colCount = dpWidth < 360 ? 2 : 3;
                layoutManager = new GridLayoutManager(getActivity(), colCount);

                api.getNewAlbums()
                   .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe(adapter::setData);
                break;
        }

        listView.setLayoutManager(layoutManager);
        listView.setAdapter(adapter);
        listView.setHasFixedSize(true);

        return layout;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (TrackListeningCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TrackListingCallbacks");
        }

        BronyTunesApp app = BronyTunesApp.get(activity);
        app.inject(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * A callback interface so that fragments can communicate user interaction
     * with the Activity hosting them
     */
    public interface TrackListeningCallbacks {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    class TrackAdapter extends RecyclerView.Adapter<TrackViewHolder> {

        private Album[] data;

        public TrackAdapter() {
            data = new Album[0];
        }

        public void setData(Album... albums) {
            this.data = albums;
            notifyDataSetChanged();
        }

        @Override
        public TrackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v;
            switch (endpoint) {
                default:
                case ENDPOINT_NEW:
                case ENDPOINT_FEATURED:
                    v = LayoutInflater.from(parent.getContext())
                                      .inflate(R.layout.view_track_listitem, parent, false);
                    break;
                case ENDPOINT_TRENDING:
                    v = LayoutInflater.from(parent.getContext())
                                      .inflate(R.layout.view_trending_item, parent, false);
                    break;
            }
            return new TrackViewHolder(v);
        }

        @Override
        public void onBindViewHolder(TrackViewHolder holder, int position) {
            Album album = data[position];

            holder.albumName.setText(album.title);
            holder.artistName.setText(album.artistName);

            File art = new File(getActivity().getExternalCacheDir(), String.valueOf(album.songId));
            if (art.exists()) {
                // If we've already cached the art, load it into the image view
                Picasso.with(getActivity())
                       .load(art)
                       .resize(100, 100)
                       .centerCrop()
                       .into(holder.albumArt);
            } else {
                // We don't have a cached copy, load load a default
                Picasso.with(getActivity())
                       .load(R.drawable.ic_launcher)
                       .resize(100, 100)
                       .centerCrop()
                       .into(holder.albumArt);

                // Now, request the image and load it into the cache
                api.getImage(album.songId)
                   .subscribeOn(Schedulers.io())
                   .observeOn(Schedulers.io())
                   .doOnError(err -> Timber.e(err, "Failed to request image"))
                   .subscribe(res -> {
                       InputStream is = null;
                       OutputStream os = null;
                       try {
                           is = res.getBody().in();
                           os = new FileOutputStream(art);

                           byte[] buffer = new byte[(int) res.getBody().length()];

                           is.read(buffer);
                           os.write(buffer);

                           // Now we have the image in the cache, so we should use
                           // Picasso to load it. Post to the image view so it
                           // runs on the main thread
                           holder.albumArt.post(() -> Picasso.with(getActivity())
                                                             .load(art)
                                                             .resize(100, 100)
                                                             .centerCrop()
                                                             .into(holder.albumArt));
                       } catch (IOException e) {
                           // We've already got a placeholder, so just
                           // use Timber to log the error
                           Timber.e(e, "Failed to save image");
                       } finally {
                           try {
                               if (is != null) {
                                   is.close();
                               }
                               if (os != null) {
                                   os.close();
                               }
                           } catch (IOException stupid) {
                               // Swallowing this exception
                           }
                       }
                   });
            }


            if (holder.trendNumber != null) {
                holder.trendNumber.setText(String.format("%1s.", position + 1));
            }
        }

        @Override
        public int getItemCount() {
            return data.length;
        }
    }

    class TrackViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.albumName)
        TextView  albumName;
        @InjectView(R.id.artistName)
        TextView  artistName;
        @InjectView(R.id.albumArt)
        ImageView albumArt;
        @Optional
        @InjectView(R.id.trendText)
        TextView  trendNumber;

        public TrackViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
