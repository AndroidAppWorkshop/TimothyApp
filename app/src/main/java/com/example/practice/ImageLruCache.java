package com.example.practice;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

public class ImageLruCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {
    private final String TAG = this.getClass().getSimpleName();

    public ImageLruCache() {
        super(getDefaultLruCacheSize());

    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }

    @Override
    public Bitmap getBitmap(String url) {
        Log.v(TAG, "Retrieved item from Mem Cache");

        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        Log.v(TAG, "Added item to Mem Cache");

        put(url, bitmap);
    }

    public static int getDefaultLruCacheSize() {
        final int maxMemory =
                (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        return cacheSize;
    }
//    public static ImageLruCache getImageLruCache()
//    {
//
//        return this;
//    }

}
