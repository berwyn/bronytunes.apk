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
public class ListenNowNewItem implements VariantRecyclerItem {
    @Override
    public int provideViewType() {
        return ListenNowAdapter.VIEW_TYPE_HEADER_NEW;
    }

    @Override
    public RecyclerView.ViewHolder createRecyclerView(LayoutInflater inflater, ViewGroup parent) {
        return new HeaderNewViewHolder(inflater.inflate(R.layout.view_listennow_headernew, parent, false));
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder vh) {

    }

    public static class HeaderNewViewHolder extends RecyclerView.ViewHolder {

        public HeaderNewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
