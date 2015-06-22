package com.timothy.Activitys;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.timothy.Adapter.ExpandableListAdapter;
import com.timothy.R;

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

    }

}
