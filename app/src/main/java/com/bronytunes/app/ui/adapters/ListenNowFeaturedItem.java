package com.bronytunes.app.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bronytunes.app.R;

import butterknife.ButterKnife;

/**
 * Created by berwyn on 10/09/2015.
 */
class ListenNowFeaturedItem implements VariantRecyclerItem {
    @Override
    public int provideViewType() {
        return ListenNowAdapter.VIEW_TYPE_HEADER_FEATURED;
    }

    @Override
    public RecyclerView.ViewHolder createRecyclerView(LayoutInflater inflater, ViewGroup parent) {
        return new HeaderFeaturedViewHolder(inflater.inflate(R.layout.view_listennow_headerfeatured, parent, false));
    }

    public static class HeaderFeaturedViewHolder extends RecyclerView.ViewHolder {

        public HeaderFeaturedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
