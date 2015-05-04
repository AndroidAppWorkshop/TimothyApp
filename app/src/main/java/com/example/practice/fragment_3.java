package com.example.practice;

import org.json.JSONArray;
import org.json.JSONException;

import com.example.practice.R;

import android.support.v4.app.Fragment;
import android.R.integer;
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
	JSONArray jsonArray;
	private RelativeLayout course;  
	private RelativeLayout found;  
	private RelativeLayout set; 
	private ImageView course_image;  
    private ImageView found_image;  
    private ImageView settings_image;  
    private TextView course_text;  
    private TextView settings_text;  
    private TextView found_text;  
    private int gray = 0xFF7597B3;  
	private int blue =0xFF0AB2FB; 
	int green=Color.GREEN;
	View view;	
	private LinearLayout me3;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.layout3, container, false);
		initView(); 
		String msg= (String) getArguments().get("json");
		
		try 
		{
			jsonArray=new JSONArray(msg);
		} 
		catch (JSONException e)
		{	
			e.printStackTrace();
		}
		TextView txv=(TextView)view.findViewById(R.id.txv);
		txv.setText(parseJson.parseJsonData(jsonArray));
	
		
		return view;
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
           
        	course_image.setImageResource(R.drawable.one_1); 
        	//course.setBackgroundColor(R.drawable.ic_tabbar_bg_click);
        	course_text.setTextColor(green);
            break;  
        case 1:  
          
        	found_image.setImageResource(R.drawable.two_2); 
        	//found.setBackgroundColor(R.drawable.ic_tabbar_bg_click);
        	found_text.setTextColor(green); 
            break;  
        case 2:  
           
        	settings_image.setImageResource(R.drawable.thr3);  
        	//set.setBackgroundColor(R.drawable.ic_tabbar_bg_click);
        	settings_text.setTextColor(green);  
            break; 
            
        default:  
            break; 
        }     
		
	}
	private void resetBtn() {
		course_image.setImageResource(R.drawable.one);  
        //course.setBackgroundColor(R.drawable.bottom_bar);  
        course_text.setTextColor(gray);  
        found_image.setImageResource(R.drawable.two);  
        //found.setBackgroundColor(R.drawable.bottom_bar);  
        found_text.setTextColor(gray);  
        settings_image.setImageResource(R.drawable.thr);  
        //set.setBackgroundColor(R.drawable.bottom_bar);  
        settings_text.setTextColor(gray);  
	}
	
	
}

	



	

