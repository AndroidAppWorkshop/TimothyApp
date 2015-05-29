package com.example.practice.ActivityClass;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.practice.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends Activity
{
    private static final String LOG_TAG = LoginActivity.class.getSimpleName();//??

    private Button buttonLogin;
    private EditText editTextAccount;
    private EditText editTextPassword;
    private ProgressBar progressBar;
    RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        editTextAccount = (EditText) findViewById(R.id.editTextAccount);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mQueue = Volley.newRequestQueue(this);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Login();
            }
        });
    }


    private void Login() {

        Map<String,String> params = new HashMap<String, String>();
        params.put("Account", editTextAccount.getText().toString());
        params.put("Password", editTextPassword.getText().toString());


        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,"http://jasonchi.ddns.net:8080/api/Authenticate",
               new JSONObject(params),new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                    Boolean result =false;
                    Log.e("通知", response.toString());
                try {
                    if (response.optBoolean("success"))
                        result = response.getBoolean("success");
                    else
                    {
                        //Log.d(LOG_TAG, "Login fail with status code=" + statusCode);
                    }
                }
                catch (JSONException e)
                {
                    Log.e(LOG_TAG, e.getMessage(), e);
                }
                if(result)
                {
                    goToNextActivity();
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Login fail.", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "No Response ....... ", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                Log.e("eror",error.toString());
            }
        }
        )

        {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        mQueue.add(jsonObjectRequest);
    }
    private void goToNextActivity() {
        Intent intent = new Intent(this, MenuListActivity.class);
        startActivity(intent);
        finish();
    }
}