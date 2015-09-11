package com.bronytunes.app.ui.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bronytunes.API;
import com.bronytunes.app.R;
import com.bronytunes.app.data.Injector;
import com.bronytunes.app.data.api.APIRoute;
import com.bronytunes.app.ui.widget.PaddingItemDecoration;
import com.bronytunes.model.Album;
import com.squareup.picasso.Picasso;

import org.lucasr.twowayview.widget.SpacingItemDecoration;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by berwyn on 10/09/2015.
 */
public class ListenNowFeaturedItem implements VariantRecyclerItem {

    private ViewGroup parent;

    @Override
    public int provideViewType() {
        return ListenNowAdapter.VIEW_TYPE_HEADER_FEATURED;
    }

    @Override
    public RecyclerView.ViewHolder createRecyclerView(LayoutInflater inflater, ViewGroup parent) {
        this.parent = parent;
        return new HeaderFeaturedViewHolder(inflater.inflate(R.layout.view_listennow_headerfeatured, parent, false));
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder vh) {
        final HeaderFeaturedViewHolder v =((HeaderFeaturedViewHolder) vh);
        v.rv.setAdapter(new HeaderListAdapter(parent.getContext(), parent));
        v.rv.addItemDecoration(new PaddingItemDecoration(parent.getContext()));
    }

    public static class HeaderFeaturedViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.listFeatured)
        RecyclerView rv;

        public HeaderFeaturedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class HeaderFeaturedItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.cover)
        ImageView cover;
        @Bind(R.id.title)
        TextView  title;
        @Bind(R.id.artist)
        TextView  artist;

        public HeaderFeaturedItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class HeaderListAdapter extends RecyclerView.Adapter<HeaderFeaturedItemViewHolder> {

        @Inject
        API api;

        private SparseArray<Album> data = new SparseArray<>(15);
        private Context      context;
        private Subscription subscription;

        public HeaderListAdapter(Context context, View parent) {
            this.context = context;
            Injector.obtain(context).inject(this);

            subscription = api.getFeaturedAlbums()
                              .subscribeOn(Schedulers.immediate())
                              .observeOn(AndroidSchedulers.mainThread())
                              .flatMap(Observable::from)
                              .limit(15)
                              .collect(() -> new ArrayList<Album>(), ArrayList::add)
                              .subscribe(
                                      albums -> {
                                          for (int i = 0, len = albums.size(); i < len; i++) {
                                              data.put(i, albums.get(i));
                                          }
                                          notifyDataSetChanged();
                                          subscription.unsubscribe();
                                      },
                                      err -> {
                                          Snackbar.make(parent, "Failed to download featured", Snackbar.LENGTH_INDEFINITE).show();
                                          Timber.e(err, "Featured API");
                                      });
        }

        @Override
        public HeaderFeaturedItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (context == null) context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new HeaderFeaturedItemViewHolder(inflater.inflate(R.layout.view_track_griditem, parent, false));
        }

        @Override
        public void onBindViewHolder(HeaderFeaturedItemViewHolder holder, int position) {
            final Album album = data.get(position);
            holder.title.setText(album.title);
            holder.artist.setText(album.artistName);
            Picasso.with(context)
                   .load(APIRoute.getInstance(context).albumArt(album.songId, "square", 400))
                   .placeholder(R.drawable.vinyl)
                   .error(R.drawable.vinyl)
                   .into(holder.cover);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
