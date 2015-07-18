package com.timothy.Activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.timothy.Adapter.ExpandableListAdapter;
import com.timothy.R;

public class OrderActivity extends Activity implements View.OnClickListener {

    ExpandableListView orderList;
    ExpandableListAdapter expandableListAdapter;
    Button testBtn, reQuestBtn;
    Context context;
    Intent it;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        orderList = (ExpandableListView) findViewById(R.id.Order_List);
        context = getApplicationContext();
        expandableListAdapter = new ExpandableListAdapter(this,orderList);
        testBtn = (Button) findViewById(R.id.testBtn);
        reQuestBtn = (Button) findViewById(R.id.reQuestBtn);
        reQuestBtn.setOnClickListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void onClick(View v) {
        it = new Intent();
        it.setClass(this, OrderActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(it);
    }
}
