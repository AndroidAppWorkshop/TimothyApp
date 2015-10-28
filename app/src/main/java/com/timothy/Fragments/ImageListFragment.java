package com.timothy.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.timothy.Cache.LruBitmapCache;
import com.timothy.Core.BaseApplication;
import com.timothy.R;

import java.util.ArrayList;

/**
 * Product展示 ,主畫面之一
 */
public class ImageListFragment extends Fragment{
    private ListView list ;
    private View view;
    private LruBitmapCache cache ;
    private ImageView image ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmet_imagefrag , null);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        list = (ListView) view.findViewById(R.id.list);
        list.setVisibility(View.INVISIBLE);
        image = (ImageView) view.findViewById(R.id.thimothy);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cache = BaseApplication.getInstance().getLruBitmapCache();
                if(BaseApplication.getInstance() != null){
                    list.setVisibility(View.VISIBLE);
                    list.setAdapter(new ImageAdapter());
                }
            }
        });
        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem == 0 )
                    image.setVisibility(View.VISIBLE);
                else
                    image.setVisibility(View.GONE);
            }
        });

    }

    private class ImageAdapter extends BaseAdapter{
        private ArrayList<String> Keylist = cache.getUrlKey();
        @Override
        public int getCount() {
            return Keylist.size();
        }

        @Override
        public Object getItem(int position) {
            return cache.getBitmap(Keylist.get(position));
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getActivity().getLayoutInflater().inflate(R.layout.fragimage, null);

            ImageView imageView = (ImageView)convertView.findViewById(R.id.DraiImage);
            imageView.setImageBitmap(cache.getBitmap(Keylist.get(position)));
            imageView.setPadding(0,1,0,0);
            return convertView;
        }
    }
}

