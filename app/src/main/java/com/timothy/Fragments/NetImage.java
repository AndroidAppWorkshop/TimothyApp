package com.timothy.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.timothy.Menu.LruBitmapCache;
import com.timothy.R;
import com.timothy.Tools.UriResources;

/**
 * Created by h94u04 on 2015/5/6.
 */
public class NetImage extends Fragment {
    RequestQueue requestQueue;
    Context context;
    NetworkImageView networkImageView;
    TextView tx1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.netimage, container, false);

        networkImageView = (NetworkImageView) v.findViewById(R.id.NetImage);

        tx1 = (TextView) v.findViewById(R.id.TextV01);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getActivity();

        requestQueue = Volley.newRequestQueue(context);

        String url = UriResources.Test.NetImageTest;

        LruBitmapCache cache = new LruBitmapCache();

        ImageLoader Imlod = new ImageLoader(requestQueue , cache );

        ImageLoader.ImageListener Imlisten = Imlod.getImageListener(networkImageView, R.drawable.clear_dark, R.drawable.boot_camp_dark);

        ImageLoader.ImageContainer container = Imlod.get(url , Imlisten , 200 ,200);
        networkImageView.setImageBitmap( container.getBitmap() );
//        networkImageView.setDefaultImageResId(R.drawable.clear_dark);
//        networkImageView.setImageUrl(url ,Imlod );
    }
}
