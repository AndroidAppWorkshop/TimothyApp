package com.timothy.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.timothy.R;

import java.util.List;

public class DrawerAdapter  extends ArrayAdapter<DrawerAdapter.DrawerItem> {
    Context context;
    List<DrawerItem> drawerItemList;
    int layoutResID;

    public DrawerAdapter(Context context, int layoutResourceID,List<DrawerItem> listItems) {
        super(context, layoutResourceID, listItems);
        this.context = context;
        this.drawerItemList = listItems;
        this.layoutResID = layoutResourceID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DrawerItemHolder drawerHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            drawerHolder = new DrawerItemHolder();

            view = inflater.inflate(layoutResID, parent, false);
            drawerHolder.ItemName = (TextView) view.findViewById(R.id.DraText);
            drawerHolder.icon = (ImageView) view.findViewById(R.id.DraiImage);

            view.setTag(drawerHolder);

        } else {
            drawerHolder = (DrawerItemHolder) view.getTag();
        }

        DrawerItem dItem = (DrawerItem) this.drawerItemList.get(position);

        drawerHolder.icon.setImageDrawable(view.getResources().getDrawable(dItem.getImgResID()));
        drawerHolder.ItemName.setText(dItem.getItemName());

        return view;
    }
    private static class DrawerItemHolder {
        TextView ItemName;
        ImageView icon;
    }

    public static class DrawerItem {
        String ItemName;
        int imgResID;

        public DrawerItem(String itemName, int imgResID) {
            super();
            ItemName = itemName;
            this.imgResID = imgResID;
        }

        public String getItemName() {
            return ItemName;
        }
        public int getImgResID() {
            return imgResID;
        }
    }
}
