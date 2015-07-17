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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.timothy.Core.BaseApplication;
import com.timothy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.timothy.Resources.Name;
import library.timothy.Resources.UriResources;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> orderIdlist = new ArrayList<>();
    private List<String> listSerialnum = new ArrayList<>();
    private HashMap<String, Map<String,String>> HeaderMap = new HashMap<>();;
    private ExpandableListAdapter expandableListAdapter;
    private ExpandableListView expandableListView;
    public ExpandableListAdapter(Context context , ExpandableListView expandableListView) {
        this.context = context;
        expandableListAdapter = this;
        this.expandableListView = expandableListView;
        OrderRequest();
    }
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return HeaderMap.get(orderIdlist.get(groupPosition)).get(childPosititon+"");
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

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
    public int getChildrenCount(int groupPosition) {
        return this.HeaderMap.get(orderIdlist.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return orderIdlist.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.orderIdlist.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
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
                removeOrder(GroupText.getText().toString() , position);
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

    private void removeOrder(String name ,int position) {
        orderIdlist.remove(name);
        OrderRequest(listSerialnum.get(position));
    }

    public void mapNewData(JSONArray jsonArray) {
        JSONObject jsonObject ;
        JSONArray DetailArray ;
        String orderId;
        orderIdlist = new ArrayList<>();
        listSerialnum = new ArrayList<>();
        HeaderMap = new HashMap<>();
        try {
            for (int index = 0; index < jsonArray.length(); index++) {
                jsonObject = jsonArray.getJSONObject(index);
                DetailArray = jsonObject.getJSONArray(Name.Order.orderDetail);
                Map map= getChildMap(DetailArray);
                String Id = jsonObject.getString(Name.Order.orderID);
                orderId = context.getResources().getString(R.string.serialNumber) + Id + "(" + map.size() + ")";
                listSerialnum.add(Id);
                orderIdlist.add(orderId);
                HeaderMap.put(orderId, map);
            }
        }
        catch (JSONException e) { e.printStackTrace();}
        noticeChanged();
    }
    private Map getChildMap(JSONArray DetailArray) {
        Map<String , String> ChildMap = new HashMap<>();
        JSONObject jo;
        for (int index = 0; index < DetailArray.length(); index++) {
            try {
                jo = DetailArray.getJSONObject(index);
                jo.getString(Name.Order.productName);
                jo.getString(Name.Order.quantity);
                ChildMap.put(index+"" , jo.getString(Name.Order.productName) + "x" + jo.getString(Name.Order.quantity));
            }
            catch (JSONException e) {e.printStackTrace();}
        }
        return ChildMap;
    }
    public void OrderRequest() {
        BaseApplication.getInstance().addToRequestQueue(new JsonArrayRequest(Request.Method.POST,
                UriResources.Server.Order,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        expandableListAdapter.mapNewData(jsonArray);
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
        });
    }
    public void OrderRequest(String SerialNumber) {
        Map<String, String> bodymap = new HashMap<>();

        bodymap.put(Name.Key.KeyAccept, SerialNumber);

        BaseApplication.getInstance().addToRequestQueue(new JsonArrayRequest(Request.Method.POST,
                UriResources.Server.Order,
                new JSONObject(bodymap),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        expandableListAdapter.mapNewData(jsonArray);
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
//            @Override
//            public Map<String, String> getHeaders() {
//                HashMap<String, String> headers = new HashMap<>();
////                headers.put(Name.Key.KeyContentType, Name.Key.KeyHeaderformat);
////                headers.put(Name.Key.Apikey,
////                        context.getSharedPreferences(Name.Key.Apikey, Context.MODE_PRIVATE).getString(Name.Key.Apikey, null));
//                return headers;
//            }
        });

    }
}
