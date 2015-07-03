package com.timothy.Activitys;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.timothy.Adapter.ExpandableListAdapter;
import com.timothy.R;

import library.timothy.Resources.NameResources;

public class OrderActivity extends Activity{

    ExpandableListView orderList;
    ExpandableListAdapter expandableListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        orderList = (ExpandableListView)findViewById(R.id.Order_List);

        expandableListAdapter = new ExpandableListAdapter(this , ExpandableListAdapter.getListFakerData()
                                                              ,  ExpandableListAdapter.getMapFakerData() );
        orderList.setAdapter(expandableListAdapter);
        registerReceiver(receiver, new IntentFilter(NameResources.Key.OrderActionlKey));
    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(!NameResources.Key.OrderActionlKey.equals(intent.getAction()))
                return ;
            String tickit = intent.getStringExtra(NameResources.Key.OrderPrefenslKey);
            expandableListAdapter.addNewItem(tickit);
        }
    };

}
