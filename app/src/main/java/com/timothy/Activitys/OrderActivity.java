package com.timothy.Activitys;

import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.db.chart.Tools;
import com.db.chart.listener.OnEntryClickListener;
import com.db.chart.model.Bar;
import com.db.chart.model.BarSet;
import com.db.chart.model.LineSet;
import com.db.chart.view.BarChartView;
import com.db.chart.view.LineChartView;
import com.db.chart.view.XController;
import com.db.chart.view.YController;
import com.db.chart.view.animation.Animation;
import com.db.chart.view.animation.easing.BaseEasingMethod;
import com.db.chart.view.animation.style.DashAnimation;
import com.timothy.Adapter.ExpandableListAdapter;
import com.timothy.Core.BaseApplication;
import com.timothy.GCM.MagicLenGCM;
import com.timothy.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import library.timothy.Resources.Name;
import library.timothy.Resources.UriResources;

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
        testBtn = (Button) findViewById(R.id.testBtn);
        reQuestBtn = (Button) findViewById(R.id.reQuestBtn);
        reQuestBtn.setOnClickListener(this);
        OrderRequest();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        OrderRequest();
    }

    @Override
    public void onClick(View v) {
        it = new Intent();
        it.setClass(this, OrderActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(it);
    }

    public void OrderRequest() {
        Map<String, String> bodymap = new HashMap<>();
        bodymap.put(Name.Key.KeyAccount, getSharedPreferences(Name.Key.Apikey, Context.MODE_PRIVATE).getString(Name.Key.KeyAccount, null));
        BaseApplication.getInstance().addToRequestQueue(new JsonArrayRequest(Request.Method.POST,
                UriResources.Server.Order,
                new JSONObject(bodymap),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        orderList.setVisibility(View.VISIBLE);
                        expandableListAdapter = new ExpandableListAdapter(context);
                        expandableListAdapter.mapNewData(jsonArray);
                        orderList.setAdapter(expandableListAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        orderList.setVisibility(View.INVISIBLE);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put(Name.Key.KeyContentType, Name.Key.KeyHeaderformat);
                headers.put(Name.Key.Apikey,
                        getSharedPreferences(Name.Key.Apikey, Context.MODE_PRIVATE).getString(Name.Key.Apikey, null));
                return headers;
            }
        });
    }
}
