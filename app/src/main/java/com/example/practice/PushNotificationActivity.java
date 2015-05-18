package com.example.practice;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class PushNotificationActivity extends Activity implements View.OnClickListener{

    Button send;
    EditText registrationId, message;
    RequestQueue mQueue;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pushnoitfication);
        send = (Button) findViewById(R.id.Send);
        registrationId = (EditText) findViewById(R.id.RegistrationId);
        message = (EditText) findViewById(R.id.Message);
        mQueue = Volley.newRequestQueue(this);
        send.setOnClickListener(this);

        MagicLenGCM magicLenGCM = new MagicLenGCM(this);
        magicLenGCM.openGCM();
        registrationId.setText(magicLenGCM.getRegistrationId());
    }
    @Override
    public void onClick(final View view) {
        Map<String,String> params = new HashMap<String, String>();
        params.put("RegistrationId", registrationId.getText().toString());
        params.put("Message", message.getText().toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,"http://jasonchi.ddns.net:8080/api/PushNotification", new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("通知", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("錯誤", error.getMessage(), error);
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=utf-8");

                return headers;
            }
        };

        mQueue.add(jsonObjectRequest);
    }
}
//檢查手機有沒有regid
//註冊後儲存
//送出訊息出去(pushsender)