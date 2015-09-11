package com.bronytunes.app.ui.adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bronytunes.API;
import com.bronytunes.app.R;
import com.bronytunes.app.data.Injector;
import com.bronytunes.app.data.api.APIRoute;
import com.bronytunes.model.Album;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Endpoint;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * An adapter for the "Listen Now" fragment's {@link RecyclerView} that adds some header views
 * to the top of the list before adding the item views.
 */
public class ListenNowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    static final int VIEW_TYPE_HEADER_FEATURED = 1;
    static final int VIEW_TYPE_HEADER_NEW      = 2;
    static final int VIEW_TYPE_HEADER_TITLE    = 3;
    static final int VIEW_TYPE_LIST_ITEM       = 4;

    private static final int IDX_HEADER = 0;
    private static final int IDX_NEW    = 1;
    private static final int IDX_TITLE  = 2;

    private final VariantRecyclerItem[] headerViews = new VariantRecyclerItem[]{
            new ListenNowFeaturedItem(),
            new ListenNowNewItem(),
            new ListenNowTitleItem()
    };

    @Inject
    API api;

    private SparseArray<Album> data = new SparseArray<>();
    private Subscription apiSubscription;
    private Context      context;

    public ListenNowAdapter(Context context, View parent) {
        this.context = context;
        Injector.obtain(context).inject(this);

        apiSubscription = api.getTrendingAlbums()
                             .subscribeOn(Schedulers.io())
                             .observeOn(AndroidSchedulers.mainThread())
                             .flatMap(Observable::from)
                             .limit(15)
                             .collect(() -> new ArrayList<Album>(15), ArrayList::add)
                             .subscribe(
                                     list -> {
                                         for (int i = 0, len = list.size(); i < len; i++) {
                                             this.data.put(i, list.get(i));
                                         }

                                         this.notifyDataSetChanged();
                                         apiSubscription.unsubscribe();
                                     },
                                     err -> {
                                         Snackbar.make(parent,
                                                 "Failed to download albums",
                                                 Snackbar.LENGTH_INDEFINITE).show();
                                         Timber.e(err, "Listen Now API call");
                                     });
    }

    @Override
    public int getItemViewType(int position) {
        // TODO: Have items provide their own types
        switch (position) {
            default:
                return VIEW_TYPE_LIST_ITEM;
            case IDX_HEADER:
            case IDX_NEW:
            case IDX_TITLE:
                return headerViews[position].provideViewType();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // TODO: Build ViewHolder logic
        View itemView;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEW_TYPE_HEADER_FEATURED:
                return headerViews[IDX_HEADER].createRecyclerView(inflater, parent);
            case VIEW_TYPE_HEADER_NEW:
                return headerViews[IDX_NEW].createRecyclerView(inflater, parent);
            case VIEW_TYPE_HEADER_TITLE:
                return headerViews[IDX_TITLE].createRecyclerView(inflater, parent);
            default:
            case VIEW_TYPE_LIST_ITEM:
                return new TrendingItemViewHolder(inflater.inflate(R.layout.view_track_listitem, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (position) {
            case IDX_HEADER:
            case IDX_NEW:
            case IDX_TITLE:
                // TODO:
                break;
            default:
                TrendingItemViewHolder vh = ((TrendingItemViewHolder) holder);
                Album album = this.data.get(position - 3);
                vh.title.setText(album.title);
                vh.artist.setText(album.artistName);
                Picasso.with(context)
                        .load(APIRoute.getInstance(context).albumArt(album.songId, "square", 200))
                        .error(R.drawable.vinyl)
                        .placeholder(R.drawable.vinyl)
                        .into(vh.cover);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + 3;
    }

    public static class TrendingItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.albumArt)
        ImageView cover;
        @Bind(R.id.albumName)
        TextView  title;
        @Bind(R.id.artistName)
        TextView  artist;

        public TrendingItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
