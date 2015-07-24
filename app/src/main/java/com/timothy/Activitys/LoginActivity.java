package com.timothy.Activitys;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.timothy.Core.BaseApplication;
import com.timothy.GCM.MagicLenGCM;
import com.timothy.R;
import library.timothy.Resources.StringResources;
import library.timothy.Resources.UriResources;
import library.timothy.Shopping.ComboRepository;
import library.timothy.Shopping.ProductRepository;
import library.timothy.history.OrderRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity
{
    private static final String LOG_TAG = LoginActivity.class.getSimpleName();

    private Button buttonLogin;
    private EditText editTextAccount;
    private EditText editTextPassword;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    private String Account;
    private String Password;
    private Resources res ;
    private String apiKey;
    private  static Map<String, Boolean>  resultApi = new HashMap<String, Boolean>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        res = getResources();

        sharedPreferences = getSharedPreferences( StringResources.Key.ApiKey, MODE_PRIVATE);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        editTextAccount = (EditText) findViewById(R.id.editTextAccount);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Login();
            }
        });
    }

    private void Login() {

        try {
            MagicLenGCM gcm = new MagicLenGCM(this);

            Password = editTextPassword.getText().toString();
            Account = editTextAccount.getText().toString();
            progressBar.setVisibility(View.VISIBLE);

            JSONObject loginBody = new JSONObject();
            loginBody.put(StringResources.Key.Account, Account);
            loginBody.put(StringResources.Key.Password, Password);
            //loginBody.put(StringResources.Key.RegId,gcm.GCMcheck().getSendRegID());

            BaseApplication.getInstance().addToRequestQueue(
                    new JsonObjectRequest(Request.Method.POST, UriResources.Server.LogIn, loginBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressBar.setVisibility(View.INVISIBLE);
                            if (!TextUtils.isEmpty(response.optString(StringResources.Key.Failure))) {
                                Toast.makeText(LoginActivity.this, res.getString(R.string.loginfail) , Toast.LENGTH_SHORT).show();
                                return;
                            }

                            apiKey = response.optString(StringResources.Key.ApiKey);
                            if (TextUtils.isEmpty(apiKey)) {
                                Toast.makeText(LoginActivity.this, res.getString(R.string.loginfail)+res.getString(R.string.apiKeyError), Toast.LENGTH_SHORT).show();
                                return;
                            }

                            sharedPreferences.edit().putString(StringResources.Key.ApiKey, apiKey).commit();
                            Calendar c = Calendar.getInstance();
                            String date= new SimpleDateFormat("yyyyMMdd").format(c.getTime());
                            loadProducts();
                            loadCombo();
                            loadOrderhistory(date);
                            loadOrder();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginActivity.this, res.getString(R.string.loginfail) + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
            ));
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            progressBar.setVisibility(View.INVISIBLE);
        }

    }
    private void loadProducts() {
        progressBar.setVisibility(View.VISIBLE);
        BaseApplication.getInstance().addToRequestQueue(
                new JsonArrayRequest(
                        Request.Method.GET,
                        UriResources.Server.Product,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray jsonArray) {
                                progressBar.setVisibility(View.GONE);
                        //      ProductRepository.refreshdata(jsonArray);
                                saveApiResult("Product",true);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                progressBar.setVisibility(View.GONE);
                                saveApiResult("Product",false);
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put(StringResources.Key.ApiKey, apiKey);
                        return headers;
                    }
                });
    }

    private void loadCombo() {
        progressBar.setVisibility(View.VISIBLE);
        BaseApplication.getInstance().addToRequestQueue(
                new JsonArrayRequest(
                        Request.Method.GET,
                        UriResources.Server.Combo,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray jsonArray) {
                                progressBar.setVisibility(View.GONE);
                               // ComboRepository.refreshData(jsonArray);
                                saveApiResult("Combo",true);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                progressBar.setVisibility(View.GONE);
                                saveApiResult("Combo",false);
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put(StringResources.Key.ApiKey, apiKey);
                        return headers;
                    }
                });
    }

    private void loadOrderhistory(String date)  {
        progressBar.setVisibility(View.VISIBLE);
        JSONObject dateBody = new JSONObject();
        try {
            dateBody.put(StringResources.Key.Date,date);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BaseApplication.getInstance().addToRequestQueue(
                new JsonArrayRequest(
                        Request.Method.POST,
                        UriResources.Server.Orderhistory,dateBody,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray jsonArray) {
                                progressBar.setVisibility(View.GONE);
                                OrderRepository.refreshData(jsonArray);
                                saveApiResult("History",true);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                progressBar.setVisibility(View.GONE);
                                saveApiResult("History",false);
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put(StringResources.Key.ApiKey, apiKey);
                        return headers;
                    }
                });
    }

    private void loadOrder() {
        progressBar.setVisibility(View.VISIBLE);
        BaseApplication.getInstance().addToRequestQueue(
                new JsonArrayRequest(
                        Request.Method.POST,
                        UriResources.Server.Order,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray jsonArray) {
                                progressBar.setVisibility(View.GONE);
                                saveApiResult("Order",true);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                progressBar.setVisibility(View.GONE);
                                saveApiResult("Order",false);
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put(StringResources.Key.ApiKey, apiKey);
                        return headers;
                    }
                });
    }

    private void saveApiResult(String apiId,Boolean use)
    {
        resultApi.put(apiId, use);

        if(resultApi.size()==4) {
            goToNextActivity();
        }
    }

    private void goToNextActivity() {
        Log.i("result", resultApi.toString());
        if(resultApi.get("Combo")&&resultApi.get("Product")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            Intent intent = new Intent(this, OrderActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public  static Map<String, Boolean> getResultApi() {
        return resultApi;
    }
}