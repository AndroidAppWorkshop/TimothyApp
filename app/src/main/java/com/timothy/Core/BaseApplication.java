package com.timothy.Core;

import android.app.Application;
import android.content.res.Configuration;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.timothy.Cache.LruBitmapCache;

/**
 * Created by Wilson on 2015/5/31.
 */
public class BaseApplication extends Application {
    public static final String TAG = BaseApplication.class.getSimpleName();
    private static BaseApplication instance;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    public static synchronized BaseApplication getInstance() {
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (this.requestQueue == null) {
            this.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return this.requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        addToRequestQueue(req, TAG);
    }

    public <T> void addToRequestQueue(Request<T> req, String tag){
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag){
        getRequestQueue().cancelAll(tag);
    }

    public ImageLoader getImageLoader() {
        if (this.imageLoader == null) {
            this.imageLoader = new ImageLoader(getRequestQueue(),
                    new LruBitmapCache());
        }

        return this.imageLoader;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
