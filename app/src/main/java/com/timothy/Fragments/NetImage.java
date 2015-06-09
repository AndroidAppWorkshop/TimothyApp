package com.timothy.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.volley.toolbox.NetworkImageView;
import com.timothy.Core.BaseApplication;
import com.timothy.R;
import library.timothy.Resources.UriResources;

public class NetImage extends Fragment {
    NetworkImageView networkImageView;
    TextView tx1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.netimage, container, false);

        networkImageView = (NetworkImageView) v.findViewById(R.id.NetImage);

        tx1 = (TextView) v.findViewById(R.id.TextV01);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        networkImageView.setImageUrl( UriResources.Test.NetImageTest , BaseApplication.getInstance().getImageLoader() );
    }
}
