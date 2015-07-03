package com.timothy.Activitys;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.timothy.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import library.timothy.history.Order;
import library.timothy.history.OrderRepository;
import library.timothy.history.Product;


public class HistoryActivity extends AppCompatActivity {

    private ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListView.setAdapter(new ExpandableAdapter());

        final EditText editTextStartDate = (EditText) findViewById(R.id.startDate);
        final EditText editTextEndDate = (EditText) findViewById(R.id.endDate);

        editTextStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                new DatePickerDialog(HistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, monthOfYear);
                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String dateString = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());

                        editTextStartDate.setText(dateString);
                    }
                },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });
//        Calendar c = Calendar.getInstance();
//        String dateString = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
//        editTextStartDate.setText(dateString);

        editTextEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                new DatePickerDialog(HistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, monthOfYear);
                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String dateString = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());

                        editTextEndDate.setText(dateString);
                    }
                },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

//        editTextEndDate.setText(dateString);
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
            return groupPosition * 1000 + childPosition;
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

            TextView textView = (TextView) convertView.findViewById(R.id.textViewOrderId);
            textView.setText("      "+order.getId() + "(" + order.getProducts().size() + ")");
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.listitem_historyproduct, null);
            }
            Product product = orders.get(groupPosition).getProducts().get(childPosition);

            TextView textView = (TextView) convertView.findViewById(R.id.textViewProductName);
            textView.setText(product.getName());
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }



}


