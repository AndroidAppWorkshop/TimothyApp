package com.timothy.GCM;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.timothy.R;


public class PushNotificationFragment extends Fragment implements View.OnClickListener{

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
                magicLenGCM.SendMessage(    Message.getText().toString() );

            Toast.makeText(getActivity() ,"RegID  : "+magicLenGCM.getSendREGID() , Toast.LENGTH_SHORT ).show();
    }
}