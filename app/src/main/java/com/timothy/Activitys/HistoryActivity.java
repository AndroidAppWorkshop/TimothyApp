package com.timothy.Activitys;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.db.chart.Tools;
import com.db.chart.model.Bar;
import com.db.chart.model.BarSet;
import com.db.chart.view.BarChartView;
import com.timothy.Core.BaseApplication;
import com.timothy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import library.timothy.Resources.StringResources;
import library.timothy.Resources.UriResources;
import library.timothy.history.Order;
import library.timothy.history.OrderRepository;
import library.timothy.history.Product;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener{


    private ExpandableListView expandableListView;
    private ProgressBar progressBar;
    private  ExpandableAdapter expandableAdapter;
    private SharedPreferences sharedPreferences;
    private String apiKey;
    private EditText editTextEndDate;
    private EditText editTextStartDate;
    private static BarChartView mBarChart;
    private Paint mBarGridPaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_history);

        DrawerLayout Dl = (DrawerLayout)findViewById(R.id.HistoryDrawer);
        Dl.setDrawerShadow(R.drawable.drashadow, Gravity.END);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        sharedPreferences = this.getSharedPreferences(StringResources.Key.ApiKey, Context.MODE_PRIVATE);
        apiKey = sharedPreferences.getString(StringResources.Key.ApiKey, null);

        editTextStartDate = (EditText) findViewById(R.id.startDate);
        editTextEndDate = (EditText) findViewById(R.id.endDate);

        editTextStartDate.setOnClickListener(this);
        editTextEndDate.setOnClickListener(this);
        Calendar c = Calendar.getInstance();

        String dateString = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        editTextStartDate.setText(R.string.from);
        editTextEndDate.setText(R.string.util);
        editTextStartDate.append(dateString);
        editTextEndDate.append(dateString);
        String date= new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        loadOrderhistory(date);
        initBarChart();
    }
    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance();
        final EditText edit = (EditText)v;
        final int text = (v == editTextStartDate) ? R.string.from : R.string.util;
        new DatePickerDialog(HistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, monthOfYear);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String dateString = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
                String date= new SimpleDateFormat("yyyyMMdd").format(c.getTime());
                edit.setText(getResources().getString(text));
                edit.append(dateString);
                loadOrderhistory(date);
            }},
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void loadOrderhistory(String date)  {
        JSONObject dateBody = new JSONObject();
        try {
            dateBody.put(StringResources.Key.Date,date);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(StringResources.Key.Date,dateBody.toString());
        BaseApplication.getInstance().addToRequestQueue(
                new JsonArrayRequest(
                        Request.Method.POST,
                        UriResources.Server.Orderhistory, dateBody,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray jsonArray) {
                                progressBar.setVisibility(View.GONE);
                                OrderRepository.refreshData(jsonArray);
                                renderlistview();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(HistoryActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put(StringResources.Key.ApiKey, apiKey);
                        return headers;
                    }
                });
    }
    private void renderlistview() {
        expandableAdapter = new ExpandableAdapter();
        expandableListView.setAdapter(expandableAdapter);
    }

    class ExpandableAdapter extends BaseExpandableListAdapter {

        private List<Order> orders = OrderRepository.getOrders();
        ExpandableAdapter() {updateBarChart();}

        public int getGroupCount() {
            return orders.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return orders.get(groupPosition).getProducts().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return orders.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return orders.get(groupPosition).getProducts().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return  childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.listitem_historyorder, null);
            }
            Order order = orders.get(groupPosition);

            TextView OrderId = (TextView) convertView.findViewById(R.id.textViewOrderId);
            OrderId.setText(order.getId() + "(" + (order.getProducts().size()-1) + ")"+" "+order.getstatus());
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.listitem_historyproduct, null);
            }
            Product product = orders.get(groupPosition).getProducts().get(childPosition);
            TextView productname = (TextView) convertView.findViewById(R.id.textViewProductName);
            if (childPosition != 0)
                productname.setText(product.getName() +getResources().getString(R.string.numberOf)+product.getquantity());
            else
                productname.setText(product.getName());
            return convertView;
        }
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

        public void updateBarChart(){
            Map<String,Integer> map = new HashMap();

            for(Order order : orders) {
                int size = order.getProducts().size();
                for (int index = 1; index < size; index++) {
                    Product product = order.getProducts().get(index) ;
                    if(map.containsKey(product.getName()))
                        map.put(product.getName() , map.get(product.getName())+product.getquantity());
                    else
                        map.put(product.getName() , product.getquantity());
                }
            }

            mBarChart.reset();

            BarSet barSet = new BarSet();
            Bar bar;
            int Max =0 ;
            for(Map.Entry<String , Integer> entry : map.entrySet()){

                String Key = entry.getKey();

                int Value = entry.getValue();

                if(Max < Value) Max = Value;

                bar = new Bar(Key , (float)Value);

                barSet.addBar(bar);
            }
            mBarChart.addData(barSet);

            mBarChart.setSetSpacing(Tools.fromDpToPx(2));
            mBarChart.setBarSpacing(Tools.fromDpToPx(10));

            mBarChart.setBorderSpacing(0)
                    .setAxisBorderValues(0, Max, Max / 3 )

                    .show();
        }
    }
    private void initBarChart(){

        mBarChart = (BarChartView) findViewById(R.id.barchart);

        mBarGridPaint = new Paint();
        mBarGridPaint.setColor(this.getResources().getColor(R.color.white));
        mBarGridPaint.setStyle(Paint.Style.STROKE);
        mBarGridPaint.setAntiAlias(true);
        mBarGridPaint.setStrokeWidth(Tools.fromDpToPx(.75f));
    }

}




