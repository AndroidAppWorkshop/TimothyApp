package com.timothy.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.timothy.GCM.MagicLenGCM;
import com.timothy.R;

import library.timothy.Shopping.Cart;


public class PushNotificationFragment extends Fragment implements View.OnClickListener{

    Button send;
    EditText Message;
    MagicLenGCM magicLenGCM;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pushnoitfication, container, false);

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

        Toast.makeText(getActivity() ,getResources().getString(R.string.regId)+magicLenGCM.getSendREGID() , Toast.LENGTH_SHORT ).show();
    }
    
}