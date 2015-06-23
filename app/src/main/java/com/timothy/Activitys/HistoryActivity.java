package com.timothy.Activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.timothy.R;


public class HistoryActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listView = (ListView) findViewById(R.id.listView);

        listView.setAdapter(new ListViewAdapter());
    }

    class ListViewAdapter extends BaseAdapter {
        private String data[] = new String[]{"A", "B", "C"};

        @Override
        public int getCount() {
            return data.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.listitem_history, null);
                viewHolder = new ViewHolder();
                viewHolder.setButtonExpand((Button) convertView.findViewById(R.id.buttonExpand));
                viewHolder.setListitemBody((LinearLayout) convertView.findViewById(R.id.listitemBody));

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            final LinearLayout listitemBody = viewHolder.getListitemBody();
            viewHolder.getButtonExpand().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listitemBody.getVisibility() == View.VISIBLE) {
                        listitemBody.setVisibility(View.GONE);
                    } else {
                        listitemBody.setVisibility(View.VISIBLE);
                    }
                }
            });

            return convertView;
        }

        class ViewHolder {
            private Button buttonExpand;
            private LinearLayout listitemBody;

            public Button getButtonExpand() {
                return buttonExpand;
            }

            public void setButtonExpand(Button buttonExpand) {
                this.buttonExpand = buttonExpand;
            }

            public LinearLayout getListitemBody() {
                return listitemBody;
            }

            public void setListitemBody(LinearLayout listitemBody) {
                this.listitemBody = listitemBody;
            }
        }
    }

}
