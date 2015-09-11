package com.bronytunes.app.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bronytunes.app.R;
import com.bronytunes.app.ui.adapters.ListenNowAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListenNowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListenNowFragment extends Fragment {

    @Bind(R.id.list)
    RecyclerView list;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ListenNowFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListenNowFragment newInstance() {
        ListenNowFragment fragment = new ListenNowFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ListenNowFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_listen_now, container, false);

        ButterKnife.bind(this, v);
        list.setAdapter(new ListenNowAdapter(v.getContext(), v));
        list.setHasFixedSize(false);

        return v;
    }


}
