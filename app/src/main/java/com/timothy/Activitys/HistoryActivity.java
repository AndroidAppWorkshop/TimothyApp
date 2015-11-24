package com.timothy.Activitys;

import android.animation.PropertyValuesHolder;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.db.chart.Tools;
import com.db.chart.listener.OnEntryClickListener;
import com.db.chart.model.Bar;
import com.db.chart.model.BarSet;
import com.db.chart.view.BarChartView;
import com.db.chart.view.Tooltip;
import com.timothy.Core.BaseApplication;
import com.timothy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.timothy.Resources.StringResources;
import library.timothy.Resources.UriResources;
import library.timothy.History.HistoryOrder;
import library.timothy.History.OrderRepository;
import library.timothy.History.Product;
/**
 * History歷史訂單畫面Activity
 **/
public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {


    private ExpandableListView expandableListView;
    private ProgressBar progressBar;
    private ExpandableAdapter expandableAdapter;
    private SharedPreferences sharedPreferences;
    private String apiKey;
    private EditText editTextStartDate;
    private static BarChartView BarChart;
    private Paint BarGridPaint;
    private Button quest;
    private DrawerLayout Drawer;
    private List<String> Barlist = new ArrayList<>();
    private TextView value;
    private OnEntryClickListener OnEntryClickListener;
    private List<HistoryOrder> orders ;
    private Map<String , Integer> counts ;
    //生命週期 於被呼叫時優先執行之一
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Drawer = (DrawerLayout)findViewById(R.id.HistoryDrawer);
        Drawer.setDrawerShadow(R.drawable.drashadow, GravityCompat.END);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        sharedPreferences = this.getSharedPreferences(StringResources.Key.ApiKey, Context.MODE_PRIVATE);
        apiKey = sharedPreferences.getString(StringResources.Key.ApiKey, null);

        editTextStartDate = (EditText) findViewById(R.id.startDate);

        editTextStartDate.setOnClickListener(this);
        quest = (Button)findViewById(R.id.quest);
        quest.setOnClickListener(this);
        Calendar c = Calendar.getInstance();

        String dateString = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        editTextStartDate.setText(R.string.from);
        editTextStartDate.append(dateString);
        String date= new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        loadOrderhistory(date);
        initBarChart();
    }
    @Override
    public void onClick(View v) {
        if (v == quest) {
            Drawer.openDrawer(Gravity.END);
            return ;
        }
        Calendar calendar = Calendar.getInstance();
        final EditText edit = (EditText)v;
        final int text = R.string.from ;
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
                BarChart.removeAllViews();
            }},
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }
    //以POST要求Server端回傳History資訊並在載入後完成設定
    private void loadOrderhistory(final String date)  {
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
                                if (jsonArray.isNull(0))
                                    Toast.makeText(HistoryActivity.this,"No Order In this Day "+date,Toast.LENGTH_SHORT).show();
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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(Drawer.isDrawerOpen(Gravity.END)) {
                Drawer.closeDrawer(Gravity.END);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    private void renderlistview() {
        expandableAdapter = new ExpandableAdapter();
        expandableListView.setAdapter(expandableAdapter);
    }

    class ExpandableAdapter extends BaseExpandableListAdapter {

        ExpandableAdapter() {
            orders = OrderRepository.getOrders();
            counts = OrderRepository.getCounts();
            updateBarChart();
            }

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
            HistoryOrder order = orders.get(groupPosition);

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

            BarChart.reset();
            if(counts.isEmpty())
                return;
            BarSet barSet = new BarSet();
            Bar bar;
            int Max = 10 ;

            for(Map.Entry<String , Integer> entry : counts.entrySet()){

                String Key = entry.getKey();

                int Value = entry.getValue();

                bar = new Bar("" , (float)Value);
                bar.setColor(getResources().getColor(R.color.bar_fill2));

                if(Max < Value)
                    Max = Value;

                if(!Key.equals(StringResources.Key.Null) || Key.isEmpty() )
                barSet.addBar(bar);
            }
            Barlist = new ArrayList<>();
            Barlist.addAll(counts.keySet());
            OnEntryClickListener = new OnEntryClickListener() {
                @Override
                public void onClick(int setIndex, int entryIndex, Rect rect){
                    value.clearAnimation();
                    AlphaAnimation animation = new AlphaAnimation(0.1f,1.0f);
                    animation.setDuration(1000);
                    value.setAnimation(animation);
                    String text = Barlist.get(entryIndex);
                    value.setText(text);
                }
            };
            int divid = Max / 5 ;
            BarChart.addData(barSet);
            BarChart.setSetSpacing(Tools.fromDpToPx(2));
            BarChart.setBarSpacing(Tools.fromDpToPx(9));
            BarChart.setOnEntryClickListener(OnEntryClickListener);
            BarChart.setBorderSpacing(0)
                    .setAxisBorderValues(0, Max, divid)
                    .setGrid(BarChartView.GridType.FULL , BarGridPaint)
                    .show();

        }
    }
    private void initBarChart(){

        BarChart = (BarChartView) findViewById(R.id.barchart);
        value = (TextView)findViewById(R.id.value);
        BarGridPaint = new Paint();
        BarGridPaint.setColor(this.getResources().getColor(R.color.white));
        BarGridPaint.setStyle(Paint.Style.STROKE);
        BarGridPaint.setStrokeWidth(Tools.fromDpToPx(0.70f));

    }
}




