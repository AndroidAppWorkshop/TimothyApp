package com.timothy.Activitys;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.timothy.R;

/**
 * Created by h94u04 on 2015/8/28.
 */
public class AlertActivity extends Activity{

    TextView title , content ;
    Button btnCancle , btnOk ;
    Intent it;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.activity_alertnotification);
        btnCancle = (Button)findViewById(R.id.cancle);
        btnOk = (Button)findViewById(R.id.ok);
        title = (TextView)findViewById(R.id.title);
        content = (TextView)findViewById(R.id.content);
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
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DevicePolicyManager policyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
                policyManager.lockNow();
                finish();
            }
        });
    }
}
