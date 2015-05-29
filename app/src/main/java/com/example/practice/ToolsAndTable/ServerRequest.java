package com.example.practice.ToolsAndTable;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;

public class ServerRequest implements Response.ErrorListener , Response.Listener<JSONArray>
{
    RequestQueue RQueue ;
    String URL ;
    Response.Listener<JSONArray> listener;
    public ServerRequest(RequestQueue RQueue, String URL, Response.Listener<JSONArray> listener)
    {
        this.RQueue = RQueue;
        this.URL = URL ;
        this.listener =listener;
    }
    public void MgetRequest()
    {
        RQueue.add( new JsonArrayRequest( Request.Method.GET , URL , listener , this ));
    }
    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Log.e("Error Report : ", volleyError.toString() );
    }

    @Override
    public void onResponse(JSONArray jsonArray) {

    }
}