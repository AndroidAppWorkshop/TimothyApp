package com.example.practice;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;

public class ServerRequest implements Response.ErrorListener
{
    RequestQueue RQueue ;
    String URL ;
    Response.Listener<JSONArray> listener;
    ServerRequest(RequestQueue RQueue, String URL , Response.Listener<JSONArray> listener)
    {
        this.RQueue = RQueue;
        this.URL = URL ;
        this.listener =listener;
    }
    void MgetRequest()
    {
        RQueue.add( new JsonArrayRequest( Request.Method.GET , URL , listener , this ));
    }
    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Log.e("Error Report : ", volleyError.toString() );
    }
}
//import android.content.Context;
//import android.os.AsyncTask;
//import android.util.Log;
//import com.android.volley.toolbox.Volley;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.util.EntityUtils;
//public class ServerRequest extends AsyncTask<String, Void, String[]>
//{
//    HttpClient httpClient =new DefaultHttpClient();
//
//    String[] result;
//    protected String[] doInBackground(String... params)
//    {	 result=new String[params.length];
//        try
//        {
//            for(int i=0;i<params.length;i++)
//            {
//                HttpGet get = new HttpGet(params[i]);
//
//                HttpResponse response = httpClient.execute(get);
//
//                HttpEntity resEntity=response.getEntity();
//
//                result[i] = EntityUtils.toString(resEntity, "utf-8");
//
//                Log.e("result", result[i]);
//            }
//        } catch (Exception e)
//        {
//            return null;
//        }
//        return result;
//    }
//}