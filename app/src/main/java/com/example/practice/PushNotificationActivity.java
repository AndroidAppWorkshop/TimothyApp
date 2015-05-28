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
import android.widget.Toast;

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
import java.util.Objects;


public class PushNotificationActivity extends Fragment implements View.OnClickListener{

    Button send;
    EditText Message;
    RequestQueue mQueue;
    String regId , MessageText ;
    MagicLenGCM magicLenGCM;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pushnoitfication , container , false);
        send = (Button) view.findViewById(R.id.Send);
        Message = (EditText) view.findViewById(R.id.RegistrationId);

        send.setOnClickListener(this);

        magicLenGCM = new MagicLenGCM(getActivity());

        return view;
    }

    @Override
    public void onClick(final View view) {

            if(!Message.getText().toString().trim().isEmpty())
                magicLenGCM.SendMessage(Message.getText().toString() );

            Toast.makeText(getActivity() ,"RegID  : "+magicLenGCM.getSendREGID() , Toast.LENGTH_SHORT ).show();
    }
}