package com.timothy.Activitys;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;
import com.timothy.R;


/**
 * Created by h94u04 on 2015/6/3.
 */
public class Another extends Activity{
    TextView tv ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.netimage);
        tv = (TextView)findViewById(R.id.TextV01);
        tv.setText("Another Page ");

    }
}
