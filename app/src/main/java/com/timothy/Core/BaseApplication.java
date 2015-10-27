package com.timothy.Core;

import android.app.Application;
import android.content.res.Configuration;
import android.support.v4.view.MenuItemCompat;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.timothy.Cache.LruBitmapCache;
/**
 * Application Core
 * 存放核心參數
 * ImageLoader , InternetQueue (網路執行佇列 )
 **/
public class BaseApplication extends Application {
    public static final String TAG = BaseApplication.class.getSimpleName();
    private static BaseApplication instance;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private LruBitmapCache LruBitmapCache;

    public static BaseApplication jason(){return instance;}

    public static synchronized BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this ;
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
            LruBitmapCache = new LruBitmapCache();
            this.imageLoader = new ImageLoader(getRequestQueue(),
                    LruBitmapCache);
        }

        return this.imageLoader;
    }
    public LruBitmapCache getLruBitmapCache() {
        if (this.LruBitmapCache == null) {
            getImageLoader();
        }
        return this.LruBitmapCache;
    }
}
