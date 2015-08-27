package com.bronytunes.app.ui.adapters;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bronytunes.app.R;
import com.bronytunes.model.Album;

import butterknife.ButterKnife;

/**
 * An adapter for the "Listen Now" fragment's {@link RecyclerView} that adds some header views
 * to the top of the list before adding the item views.
 */
public class ListenNowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER_FEATURED = 1;
    private static final int VIEW_TYPE_HEADER_NEW      = 2;
    private static final int VIEW_TYPE_HEADER_TITLE    = 3;
    private static final int VIEW_TYPE_LIST_ITEM       = 4;

    private Album[] data;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // TODO: Build ViewHolder logic
        View itemView;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEW_TYPE_HEADER_FEATURED:
                itemView = inflater.inflate(R.layout.view_listennow_headerfeatured, parent, false);
                return new HeaderFeaturedViewHolder(itemView);
            case VIEW_TYPE_HEADER_NEW:
                itemView = inflater.inflate(R.layout.view_listennow_headernew, parent, false);
                return new HeaderNewViewHolder(itemView);
            case VIEW_TYPE_HEADER_TITLE:
                itemView = inflater.inflate(R.layout.view_listennow_headertrending, parent, false);
                return new RecyclerView.ViewHolder(itemView) {};
            case VIEW_TYPE_LIST_ITEM:
                return null;
            default:
                throw new RuntimeException("Unknown view type passed to onCreateViewHolder");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return data.length + 3;
    }

    public static class HeaderFeaturedViewHolder extends RecyclerView.ViewHolder {

        public HeaderFeaturedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class HeaderNewViewHolder extends RecyclerView.ViewHolder {

        public HeaderNewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
