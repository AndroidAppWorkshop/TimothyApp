package com.timothy.Activitys;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;
import com.timothy.R;

public class OrderActivity extends Activity{
    TextView tv ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        tv = (TextView)findViewById(R.id.Text);

        tv.append( this.getIntent().getStringExtra("Content")+ " a test Text ! !"  );
    }
}
