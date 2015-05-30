package com.timothy.Fragments;

import org.json.JSONArray;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.timothy.Tools.JSONHelper;
import com.timothy.R;
import com.timothy.Tools.ServerRequest;
import com.timothy.Tools.UriResources;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class fragment_3  extends Fragment implements OnClickListener {
	private RelativeLayout course , found, set;
	private ImageView course_image , found_image,settings_image;
    private TextView found_text, txv,settings_text,course_text;
    private RequestQueue RQueue;
    private int gray = 0xFF7597B3;  
	private int blue =0xFF0AB2FB; 
	int green=Color.GREEN;
	View view;	
	private LinearLayout me3;
    String URL = UriResources.Test.OpenData3;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment3, container, false);

		initView();

		return view;
	}
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RQueue = Volley.newRequestQueue(getActivity());

        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {

                txv.setText(JSONHelper.readLine(jsonArray));

            }
        };
        ServerRequest serverRequest = new ServerRequest( RQueue , URL , listener);
        serverRequest.MgetRequest();
    }
    private void initView() {
		me3=(LinearLayout)view.findViewById(R.id.me3);
		course=(RelativeLayout)view.findViewById(R.id.cou);
		found=(RelativeLayout)view.findViewById(R.id.fou);
		set=(RelativeLayout)view.findViewById(R.id.set);
		course_image = (ImageView)view.findViewById(R.id.course_image1);  
        found_image = (ImageView)view.findViewById(R.id.found_image2);  
        settings_image = (ImageView)view.findViewById(R.id.setting_image3);  
        course_text = (TextView)view.findViewById(R.id.course_text1);  
        found_text = (TextView)view.findViewById(R.id.found_text2);  
        settings_text = (TextView)view.findViewById(R.id.setting_text3);  
		course.setOnClickListener(this);
		found.setOnClickListener(this);
		set.setOnClickListener(this);
		me3.getBackground().setAlpha(200);
        txv=(TextView)view.findViewById(R.id.txv);
	}
	@Override
	public void onClick(View v) {
		 switch (v.getId()) 
		 {
		 	case R.id.cou:  
	            setTabSelection(0);
	           Toast.makeText(getActivity(),"�Ƥl", Toast.LENGTH_SHORT).show();
	            break;  
	        case R.id.fou:  
	            setTabSelection(1);
	            Toast.makeText(getActivity(),"�_��", Toast.LENGTH_SHORT).show();
	            break;  
	        case R.id.set:  
	            setTabSelection(2);
	            Toast.makeText(getActivity(),"���g", Toast.LENGTH_SHORT).show();
	            break;
	        default:  
	            break;  
		 }
		
	}
	
	private void setTabSelection(int i) {
        resetBtn();       
        switch (i)  
        {  
        case 0:  
           
        	course_image.setImageResource(R.drawable.buttonbar_image_click7);
        	//course.setBackgroundColor(R.drawable.ic_tabbar_bg_click);
        	course_text.setTextColor(green);
            break;  
        case 1:  
          
        	found_image.setImageResource(R.drawable.buttonbar_image_click8);
        	//found.setBackgroundColor(R.drawable.ic_tabbar_bg_click);
        	found_text.setTextColor(green); 
            break;  
        case 2:  
           
        	settings_image.setImageResource(R.drawable.buttonbar_image_click9);
        	//set.setBackgroundColor(R.drawable.ic_tabbar_bg_click);
        	settings_text.setTextColor(green);  
            break;
        default:  
            break; 
        }
	}
	private void resetBtn()
    {
		course_image.setImageResource(R.drawable.buttonbar_image_create7);
        //course.setBackgroundColor(R.drawable.bottom_bar);  
        course_text.setTextColor(gray);  
        found_image.setImageResource(R.drawable.buttonbar_image_create8);
        //found.setBackgroundColor(R.drawable.bottom_bar);  
        found_text.setTextColor(gray);  
        settings_image.setImageResource(R.drawable.buttonbar_image_create9);
        //set.setBackgroundColor(R.drawable.bottom_bar);  
        settings_text.setTextColor(gray);  
	}
	
	
}

	



	

