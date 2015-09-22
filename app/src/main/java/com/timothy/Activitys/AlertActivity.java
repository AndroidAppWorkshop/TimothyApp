package com.timothy.Activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.timothy.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import library.timothy.Order.OrderDetail;
import library.timothy.Resources.StringResources;

/**
 * Created by h94u04 on 2015/8/28.
 */
public class AlertActivity extends Activity{

    private TextView title , content ;
    private Button btnCancel, btnOk ;
    private Intent it;
    private PowerManager.WakeLock lock ;
    private PowerManager manager ;
    private String OrderId ;
    private Map<String,String> data = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.activity_alertnotification);
        String Message = getIntent().getStringExtra(StringResources.Gcm.Message);
        try {
            JSONArray ja = new JSONArray(Message);
            OrderId = ja.getJSONObject(0).getString(StringResources.Key.OrderID);
            OrderDetail detail = new OrderDetail(ja);
            data = detail.getChild();
        }
        catch (JSONException e) { e.printStackTrace();}
        manager = (PowerManager) getSystemService(this.POWER_SERVICE);
        btnCancel = (Button)findViewById(R.id.cancle);
        btnOk = (Button)findViewById(R.id.ok);
        title = (TextView)findViewById(R.id.title);
        title.setText(getResources().getString(R.string.NewOrder) + OrderId);
        content = (TextView)findViewById(R.id.content);
        for (Map.Entry<String , String > entry : data.entrySet())
            content.append( entry.getValue()+ "\n" );

        it = new Intent();
        it.setClass(this , OrderActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(it);
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
