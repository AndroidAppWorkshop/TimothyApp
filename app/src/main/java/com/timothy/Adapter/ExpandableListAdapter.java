package com.timothy.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.timothy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader;
    private static List<String> listChild;
    private static List<String> listHeader;
    private static HashMap<String, List<String>> map;
    private HashMap<String, List<String>> listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;

    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild
                .get(this.listDataHeader.get(groupPosition))
                .get(childPosititon);
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
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
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

    public static HashMap<String, List<String>> getMapFakerData() {
        if(listChild == null ){
            listChild = new ArrayList<>();
            listChild.add("test Expand");
        }

        if(map == null ){
            map = new HashMap<>();
            map.put(getListFakerData().get(0), listChild);
            map.put(getListFakerData().get(1), listChild);
        }
        return map;
    }

    public static List<String> getListFakerData() {
        if(listHeader == null ){
            listHeader = new ArrayList<>();
            listHeader.add("TEST");
            listHeader.add("Menu");
        }
        return listHeader;
    }
    private void removeOrder(String name)
    {
        listDataChild.remove(name);
        listDataHeader.remove(name);
    }
    public void addNewItem(String Item )
    {
        listHeader.add(Item);
        notifyDataSetChanged();
    }
    public void addNewItem(String Item ,List<String> ChildItem)
    {
        listDataChild.put(Item , ChildItem);
        notifyDataSetChanged();
    }
}
