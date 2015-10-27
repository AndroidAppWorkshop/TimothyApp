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

import com.timothy.Core.BaseApplication;
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
 *
 * ->當點餐完成時，Server端回傳時呼叫
 */
public class AlertActivity extends Activity {

    private TextView title , content ;
    private Button btnCancel, btnOk ;
    private Intent it;
    private PowerManager.WakeLock lock ;
    private PowerManager manager ;
    private String OrderId ;
    private Map<String,String> data = new HashMap<>();
    //生命週期 於被呼叫時優先執行之一
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.activity_alertnotification);
        String Message = getIntent().getStringExtra(StringResources.Gcm.Message);
        OrderId = getIntent().getStringExtra(StringResources.Gcm.SendId);

        manager = (PowerManager) getSystemService(this.POWER_SERVICE);
        btnCancel = (Button)findViewById(R.id.cancle);
        btnOk = (Button)findViewById(R.id.ok);
        title = (TextView)findViewById(R.id.title);
        title.setText(getResources().getString(R.string.NewOrder) + OrderId);
        content = (TextView)findViewById(R.id.content);
        content.setText(Message);

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
