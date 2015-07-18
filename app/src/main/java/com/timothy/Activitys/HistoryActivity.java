package com.timothy.Activitys;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
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

import library.timothy.Resources.StringResuorces;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        DrawerLayout Dl = (DrawerLayout)findViewById(R.id.HistoryDrawer);
        Dl.setDrawerShadow(R.drawable.drashadow, Gravity.END);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        sharedPreferences = this.getSharedPreferences(StringResuorces.Key.apiKey, Context.MODE_PRIVATE);
        apiKey = sharedPreferences.getString(StringResuorces.Key.apiKey, null);

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
                edit.setText(getResources().getString(text));
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
            dateBody.put(StringResuorces.Key.date,date);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(StringResuorces.Key.date,dateBody.toString());
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
                        headers.put(StringResuorces.Key.apiKey, apiKey);
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
}




