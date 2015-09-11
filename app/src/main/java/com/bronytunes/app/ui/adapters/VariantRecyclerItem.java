package com.bronytunes.app.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by berwyn on 10/09/2015.
 */
interface VariantRecyclerItem {

    int provideViewType();
    RecyclerView.ViewHolder createRecyclerView(LayoutInflater li, ViewGroup parent);
    void bindViewHolder(RecyclerView.ViewHolder vh);

}
