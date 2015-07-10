package com.timothy.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.timothy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.timothy.Resources.Name;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> orderIdlist ;
    private HashMap<String, Map<String,String>> HeaderMap;


    public ExpandableListAdapter(Context context) {
        this.context = context;
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
    public View getGroupView(int groupPosition, boolean isExpanded,
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
        Deltebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeOrder(GroupText.getText().toString());
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

    private void removeOrder(String name)
    {
        orderIdlist.remove(name);
    }
    public void mapNewData(JSONArray jsonArray) {
        JSONObject jsonObject ;
        JSONArray DetailArray ;
        String orderId;
        orderIdlist = new ArrayList<>();
        HeaderMap = new HashMap<>();
        try {
            for (int index = 0; index < jsonArray.length(); index++) {
                jsonObject = jsonArray.getJSONObject(index);
                DetailArray = jsonObject.getJSONArray(Name.Order.orderDetail);
                Map map= getChildMap(DetailArray);
                orderId = "³æ¸¹"+jsonObject.getString(Name.Order.orderID)+"("+map.size()+")";
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
}
