package com.timothy.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.timothy.Core.BaseApplication;
import com.timothy.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.timothy.Order.Order;
import library.timothy.Order.OrderList;
import library.timothy.Resources.StringResources;
import library.timothy.Resources.UriResources;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private Map<String, Order> DataMap = new HashMap<>();
    private List<String> KeyList;
    private library.timothy.Order.OrderList OrderList ;
    private ExpandableListAdapter expandableListAdapter;
    private ExpandableListView expandableListView;
    public ExpandableListAdapter(Context context, ExpandableListView expandableListView) {
        this.context = context;
        expandableListAdapter = this;
        this.expandableListView = expandableListView;
        OrderRequest();
    }
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return DataMap.get(KeyList.get(groupPosition)).getChild().get(childPosititon+"");
    }

    @Override
    public long getGroupId(int groupPosition) {return 0;}

    @Override
    public long getChildId(int groupPosition, int childPosition) {return 0;}

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_expand, null);
        }

        TextView leftText = (TextView) convertView.findViewById(R.id.leftText);

        leftText.setText(childText);

        return convertView;
    }

    @Override
    public int getGroupCount() {return DataMap.size();}

    @Override
    public int getChildrenCount(int groupPosition) {
        return DataMap.get(KeyList.get(groupPosition)).getChild().size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return DataMap.get(KeyList.get(groupPosition)).getOrderID();
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = getGroup(groupPosition) ;
        if (convertView == null) {
            LayoutInflater infalInflater =
                    (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        final TextView GroupText = (TextView) convertView.findViewById(R.id.groupText);
        Button Deltebtn = (Button) convertView.findViewById(R.id.DeleteBtn);
        Deltebtn.setFocusable(false);
        final int position = groupPosition;
        Deltebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeOrder(position);
                noticeChanged();
            }
        });

        GroupText.setTypeface(null, Typeface.BOLD);
        GroupText.setText(headerTitle);

        return convertView;
    }
    private void noticeChanged(){ this.notifyDataSetChanged();}

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void removeOrder(int position) {
        OrderRequest(KeyList.get(position));
    }
    private Map<String, Order> setData(Map<String, Order> DataMap) {
        return this.DataMap = DataMap;
    }

    public void OrderRequest() {
        BaseApplication.getInstance().addToRequestQueue(new JsonArrayRequest(Request.Method.POST,
                UriResources.Server.Order,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        OrderList = new OrderList().mapNewData(jsonArray);
                        KeyList = OrderList.getKeyList();
                        expandableListAdapter.setData(OrderList.getOrderList());
                        expandableListView.setAdapter(expandableListAdapter);
                        expandableListAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e(volleyError.getClass().toString(), volleyError.getStackTrace().toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
            HashMap<String, String> headers = new HashMap<>();
            headers.put(StringResources.Key.ApiKey, context.getSharedPreferences(StringResources.Key.ApiKey, Context.MODE_PRIVATE).getString(StringResources.Key.ApiKey, null));
            return headers;
        }});
    }

    public void OrderRequest(final String SerialNumber) {
        Map<String, String> bodymap = new HashMap<>();

        bodymap.put(StringResources.Key.Accept, SerialNumber);

        BaseApplication.getInstance().addToRequestQueue(new JsonArrayRequest(Request.Method.POST,
                UriResources.Server.Order,
                new JSONObject(bodymap),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        OrderList = new OrderList().mapNewData(jsonArray);
                        KeyList = OrderList.getKeyList();
                        DataMap.remove(SerialNumber);
                        expandableListAdapter.setData(OrderList.getOrderList());
                        expandableListView.setAdapter(expandableListAdapter);
                        expandableListAdapter.notifyDataSetChanged();
                        Toast.makeText(context , R.string.Delete ,Toast.LENGTH_SHORT ).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e(volleyError.getClass().toString(), volleyError.getStackTrace().toString());
                    }
                }){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put(StringResources.Key.ApiKey, context.getSharedPreferences(StringResources.Key.ApiKey, Context.MODE_PRIVATE).getString(StringResources.Key.ApiKey, null));
                return headers;
            }
        });

    }
}
