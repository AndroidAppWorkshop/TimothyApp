package com.timothy.Activitys;

import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseExpandableListAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.db.chart.view.XController;
import com.db.chart.view.YController;
import com.db.chart.view.animation.Animation;
import com.db.chart.view.animation.easing.BaseEasingMethod;
import com.timothy.Core.BaseApplication;
import com.timothy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.timothy.Resources.Name;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        DrawerLayout Dl = (DrawerLayout)findViewById(R.id.HistoryDrawer);
        Dl.setDrawerShadow(R.drawable.drashadow, Gravity.END);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        initBarChart();
        sharedPreferences = this.getSharedPreferences(Name.Key.Apikey, Context.MODE_PRIVATE);
        apiKey = sharedPreferences.getString(Name.Key.Apikey, null);

        EditText editTextStartDate = (EditText) findViewById(R.id.startDate);
        EditText editTextEndDate = (EditText) findViewById(R.id.endDate);

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
    }
    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance();
        final EditText edit = (EditText)v;
        new DatePickerDialog(HistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, monthOfYear);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String dateString = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
                edit.setText(R.string.util);
                edit.append(dateString);
                loadOrderhistory(dateString);
            }},
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void loadOrderhistory(String date)  {
        JSONObject dateBody = new JSONObject();
        try {
            dateBody.put(Name.Key.KeyDate,date);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(Name.Key.KeyDate,dateBody.toString());
        BaseApplication.getInstance().addToRequestQueue(
                new JsonArrayRequest(
                        Request.Method.POST,
                        UriResources.Server.orderhistory,dateBody,
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
                        headers.put(Name.Key.Apikey, apiKey);
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
    }
    private static ImageButton mOrderBtn;
    private final static int[] beginOrder = {0, 1, 2, 3, 4, 5, 6};
    private final static int[] middleOrder = {3, 2, 4, 1, 5, 0, 6};
    private final static int[] endOrder = {6, 5, 4, 3, 2, 1, 0};
    private static float mCurrOverlapFactor;
    private static int[] mCurrOverlapOrder;
    private static float mOldOverlapFactor;
    private static int[] mOldOverlapOrder;


    /**
     * Ease
     */
    private static ImageButton mEaseBtn;
    private static BaseEasingMethod mCurrEasing;
    private static BaseEasingMethod mOldEasing;


    /**
     * Enter
     */
    private static ImageButton mEnterBtn;
    private static float mCurrStartX;
    private static float mCurrStartY;
    private static float mOldStartX;
    private static float mOldStartY;


    /**
     * Alpha
     */
    private static ImageButton mAlphaBtn;
    private static int mCurrAlpha;
    private static int mOldAlpha;

    private final static int BAR_MAX = 10;
    private final static int BAR_MIN = 0;
    private final static String[] barLabels = {"YAK", "ANT", "GNU", "OWL", "APE", "JAY", "COD"};
    private final static float [][] barValues = { {6.5f, 7.5f, 3.5f, 3.5f, 10f, 4.5f, 5.5f},
            {9.5f, 3.5f, 5.5f, 4.5f, 8.5f, 6.5f, 5.5f} };
    private static BarChartView mBarChart;
    private Paint mBarGridPaint;
    private TextView mBarTooltip;

    private final TimeInterpolator enterInterpolator = new DecelerateInterpolator(1.5f);
    private final TimeInterpolator exitInterpolator = new AccelerateInterpolator();


    private Animation getAnimation(boolean newAnim){
        if(newAnim)
            return new Animation()
                    .setAlpha(mCurrAlpha)
                    .setEasing(mCurrEasing)
                    .setOverlap(mCurrOverlapFactor, mCurrOverlapOrder)
                    .setStartPoint(mCurrStartX, mCurrStartY);
        else
            return new Animation()
                    .setAlpha(mOldAlpha)
                    .setEasing(mOldEasing)
                    .setOverlap(mOldOverlapFactor, mOldOverlapOrder)
                    .setStartPoint(mOldStartX, mOldStartY);
    }
    private void initBarChart(){

        mBarChart = (BarChartView) findViewById(R.id.barchart);
//        mBarChart.setOnEntryClickListener(barEntryListener);
//        mBarChart.setOnClickListener(barClickListener);

        mBarGridPaint = new Paint();
        mBarGridPaint.setColor(this.getResources().getColor(R.color.bar_grid));
        mBarGridPaint.setStyle(Paint.Style.STROKE);
        mBarGridPaint.setAntiAlias(true);
        mBarGridPaint.setStrokeWidth(Tools.fromDpToPx(.75f));
        updateBarChart();
    }
    private void updateBarChart(){

        mBarChart.reset();

        BarSet barSet = new BarSet();
        Bar bar;
        for(int i = 0; i < barLabels.length; i++){
            bar = new Bar(barLabels[i], barValues[0][i]);
//                if(i == 4)
//                    bar.setColor(this.getResources().getColor(R.color.bar_highest));
//                else
            bar.setColor(this.getResources().getColor(R.color.bar_fill1));
            barSet.addBar(bar);
        }
        mBarChart.addData(barSet);

        barSet = new BarSet();
        addBars(barSet ,barLabels, barValues[1]);
        barSet.setColor(this.getResources().getColor(R.color.bar_fill2));
        mBarChart.addData(barSet);

        mBarChart.setSetSpacing(Tools.fromDpToPx(3));
        mBarChart.setBarSpacing(Tools.fromDpToPx(14));

        mBarChart.setBorderSpacing(0)
                .setAxisBorderValues(BAR_MIN, BAR_MAX, 2)
                .setGrid(BarChartView.GridType.FULL, mBarGridPaint)
                .setYAxis(false)
                .setXLabels(XController.LabelPosition.OUTSIDE)
                .setYLabels(YController.LabelPosition.NONE)
                .show()
        //.show()getAnimation(true).setEndAction(mEnterEndAction)
        ;
    }
    @SuppressLint("NewApi")
//    private void showBarTooltip(int setIndex, int entryIndex, Rect rect){
//
//        mBarTooltip = (TextView) getLayoutInflater().inflate(R.layout.bar_tooltip, null);
//        mBarTooltip.setText(Integer.toString((int) barValues[setIndex][entryIndex]));
//
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(rect.width(), rect.height());
//        layoutParams.leftMargin = rect.left;
//        layoutParams.topMargin = rect.top;
//        mBarTooltip.setLayoutParams(layoutParams);
//
//        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1){
//            mBarTooltip.setAlpha(0);
//            mBarTooltip.setScaleY(0);
//            mBarTooltip.animate()
//                    .setDuration(200)
//                    .alpha(1)
//                    .scaleY(1)
//                    .setInterpolator(enterInterpolator);
//        }
//        mBarChart.showTooltip(mBarTooltip,true);
//    }
    public void addBars(BarSet barSet ,String[] labels, float[] values){

        if(labels.length == values.length)

        for(int i = 0; i < labels.length ; i++)
            addBar(barSet ,labels[i], values[i]);
    }
    public void addBar(BarSet barSet ,String label, float value){
        barSet.addBar(new Bar(label, value));
    }

//    private void addBars(BarSet barSet ,String[] barLabels ,float [][] barValues)
//    {
//        barSet.addBar(new Bar());
//    }
}




