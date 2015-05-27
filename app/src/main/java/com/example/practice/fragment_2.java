package com.example.practice;

import org.json.JSONArray;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class fragment_2  extends Fragment implements OnClickListener{
	private RelativeLayout course;
	private RelativeLayout found;  
	private RelativeLayout set; 
	private ImageView course_image;  
    private ImageView found_image;  
    private ImageView settings_image;  
    private TextView course_text;  
    private TextView settings_text;  
    private TextView found_text;
    private TextView txv;
    private int gray = 0xFF7597B3;  
	private int blue =0xFF0AB2FB;  
	private LinearLayout me;
    RequestQueue RQueue;
	View view;
    String URL = Url_Value.fragUrl[1];
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.layout2, container, false);

		initView();

		return view;
	}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("DATA Report", this.toString() + "Fragmen Has be Calllllllllllllllllll ! ");

        RQueue = Volley.newRequestQueue(getActivity());

        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {

                txv.setText(ParseforJsonArray.parseJsonData(jsonArray));

            }
        };
        ServerRequest serverRequest = new ServerRequest( RQueue , URL , listener);
        serverRequest.MgetRequest();
    }
    private void initView()
	{			
		me=(LinearLayout)view.findViewById(R.id.me);
		course=(RelativeLayout)view.findViewById(R.id.course);
		found=(RelativeLayout)view.findViewById(R.id.found);
		set=(RelativeLayout)view.findViewById(R.id.setting);
		course_image = (ImageView)view.findViewById(R.id.course_image_a);  
        found_image = (ImageView)view.findViewById(R.id.found_image_b);  
        settings_image = (ImageView)view.findViewById(R.id.setting_image_c);  
        course_text = (TextView)view.findViewById(R.id.course_text_a);  
        found_text = (TextView)view.findViewById(R.id.found_text_b);  
        settings_text = (TextView)view.findViewById(R.id.setting_text_c);  
		course.setOnClickListener(this);
		found.setOnClickListener(this);
		set.setOnClickListener(this);	
		me.getBackground().setAlpha(200);
        txv=(TextView)view.findViewById(R.id.txv);
	}
	@Override
	public void onClick(View v) {
		 switch (v.getId()) 
		 {
		 	case R.id.course:  
	            setTabSelection(0);
	           Toast.makeText(getActivity(),"�ڬO001��", Toast.LENGTH_SHORT).show();
	            break;  
	        case R.id.found:  
	            setTabSelection(1);
	            Toast.makeText(getActivity(),"�ڬO002��", Toast.LENGTH_SHORT).show();
	            break;  
	        case R.id.setting:  
	            setTabSelection(2);
	            Toast.makeText(getActivity(),"�ڬO003��", Toast.LENGTH_SHORT).show();
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
           
        	course_image.setImageResource(R.drawable.buttonbar_image_click4);
        	//course.setBackgroundColor(R.drawable.ic_tabbar_bg_click);
        	course_text.setTextColor(blue);
            break;  
        case 1:  
          
        	found_image.setImageResource(R.drawable.buttonbar_image_click5);
        	//found.setBackgroundColor(R.drawable.ic_tabbar_bg_click);
        	found_text.setTextColor(blue); 
            break;  
        case 2:  
           
        	settings_image.setImageResource(R.drawable.buttonbar_image_click6);
        	//set.setBackgroundColor(R.drawable.ic_tabbar_bg_click);
        	settings_text.setTextColor(blue);  
            break; 
            
        default:  
            break; 
        }     
		
	}
	private void resetBtn() {
		course_image.setImageResource(R.drawable.buttonbar_image_create4);
        //course.setBackgroundColor(R.drawable.bottom_bar);  
        course_text.setTextColor(gray);  
        found_image.setImageResource(R.drawable.buttonbar_image_create5);
        //found.setBackgroundColor(R.drawable.bottom_bar);  
        found_text.setTextColor(gray);  
        settings_image.setImageResource(R.drawable.buttonbar_image_create6);
        //set.setBackgroundColor(R.drawable.bottom_bar);  
        settings_text.setTextColor(gray);  
	}
	

}
