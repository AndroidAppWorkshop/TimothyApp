package com.example.practice;

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

        // Retrieves an image specified by the URL, displays it in the UI.

        // Access the RequestQueue through your singleton class.
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getActivity();

        requestQueue = Volley.newRequestQueue(context);

        String url = "http://i.imgur.com/7spzG.png";

        ImageLruCache cache = new ImageLruCache();

        ImageLoader Imlod = new ImageLoader(requestQueue, cache );
        ImageLoader.ImageListener Imlisten = Imlod.getImageListener(networkImageView, R.drawable.clear_dark, R.drawable.boot_camp_dark);

        //Imlod.get(url, Imlisten);
        networkImageView.setDefaultImageResId(R.drawable.clear_dark);
        networkImageView.setImageUrl(url ,Imlod );
    }

}

//    private static MySingleton mInstance;
//    private RequestQueue mRequestQueue;
//    private ImageLoader mImageLoader;
//    private static Context mCtx;
//
//    private MySingleton(Context context) {
//        mCtx = context;
//        mRequestQueue = getRequestQueue();
//
//        mImageLoader = new ImageLoader(mRequestQueue,
//                new ImageLoader.ImageCache() {
//                    private final LruCache<String, Bitmap>
//                            cache = new LruCache<String, Bitmap>(20);
//
//                    @Override
//                    public Bitmap getBitmap(String url) {
//                        return cache.get(url);
//                    }
//
//                    @Override
//                    public void putBitmap(String url, Bitmap bitmap) {
//                        cache.put(url, bitmap);
//                    }
//                });
//    }
//
//    public RequestQueue getRequestQueue() {
//        if (mRequestQueue == null) {
//            // getApplicationContext() is key, it keeps you from leaking the
//            // Activity or BroadcastReceiver if someone passes one in.
//            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
//        }
//        return mRequestQueue;
//    }
//
//    public <T> void addToRequestQueue(Request<T> req) {
//        getRequestQueue().add(req);
//    }
//
//    public ImageLoader getImageLoader() {
//        return mImageLoader;
//    }
//}
//}
