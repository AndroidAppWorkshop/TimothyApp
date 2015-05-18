package com.example.practice;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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

        ImageLoader Imlod = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String s) {
                return null;
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {

            }
        });
        ImageLoader.ImageListener Imlisten = Imlod.getImageListener(networkImageView, R.drawable.clear_dark, R.drawable.boot_camp_dark);

        networkImageView.setImageUrl(url, Imlod);

//        String[] url2 =
//                {
//                        "http://data.kaohsiung.gov.tw/Opendata/DownLoad.aspx?Type=2&CaseNo1=BA&CaseNo2=1&FileType=2&Lang=C&FolderType=O",
//                        "http://data.kaohsiung.gov.tw/Opendata/DownLoad.aspx?Type=2&CaseNo1=AV&CaseNo2=1&FileType=1&Lang=C&FolderType",
//                        "http://data.kaohsiung.gov.tw/Opendata/DownLoad.aspx?Type=2&CaseNo1=AF&CaseNo2=2&FileType=2&Lang=C&FolderType=O"
//                };
//        for (int URLSwitch = 0; URLSwitch < url2.length; URLSwitch++) {
//
//            JsonArrayRequest JRequest = new JsonArrayRequest
//                    (Request.Method.GET ,  url2[URLSwitch] ,
//                            new Response.Listener<JSONArray>() {
//                                @Override
//                                public void onResponse(JSONArray jsonArray) {
//
//                                    tx1.append(jsonArray.toString());
//
//                                }
//                            }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError volleyError) {
//
//                        }
//
//                    });
//            requestQueue.add(JRequest);
//        }
    }
}
//
//    class SRequest extends com.android.volley.toolbox.StringRequest {
//        String url;
//
//        public SRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
//            super(method, url, listener, errorListener);
//            this.url = url;
//
//        }
//
//        @Override
//        public Map<String, String> getHeaders() throws AuthFailureError {
//            Map<String, String> map = new HashMap<>();
//            map.put("Cookie", url);
//
//            return map;
//        }
//    }
//}
//class BitmapLruImageCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache{
//    private final String TAG = this.getClass().getSimpleName();
//
//    public BitmapLruImageCache(int maxSize) {
//        super(maxSize);
//    }
//
//    @Override
//    protected int sizeOf(String key, Bitmap value){
//        return value.getRowBytes() * value.getHeight();
//    }
//
//    @Override
//    public Bitmap getBitmap(String url){
//        Log.v(TAG, "Retrieved item from Mem Cache");
//
//        return get(url);
//    }
//
//    @Override
//    public void putBitmap(String url, Bitmap bitmap) {
//        Log.v(TAG, "Added item to Mem Cache");
//
//        put(url, bitmap);
//    }
//}
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
////    public static synchronized MySingleton getInstance(Context context) {
////        if (mInstance == null) {
////            mInstance = new MySingleton(context);
////        }
////        return mInstance;
////    }
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
