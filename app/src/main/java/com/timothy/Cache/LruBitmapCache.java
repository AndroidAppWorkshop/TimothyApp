package com.timothy.Cache;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
/**
 * 控制圖片載入時存取的快取Class
 **/
public class LruBitmapCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {
    ArrayList<String> urlKey = new ArrayList<>();
    public static int getDefaultLruCacheSize() {
        final int maxMemory =
                (int) (Runtime.getRuntime().maxMemory() );
        final int cacheSize = maxMemory / 8 ;
        return cacheSize;
    }

    public LruBitmapCache() {
        super(getDefaultLruCacheSize());
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {

        return value.getRowBytes() * value.getHeight();
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        urlKey.add(url);
        put(url, bitmap);
    }

    public ArrayList<String> getUrlKey() {
        return urlKey;
    }
}
