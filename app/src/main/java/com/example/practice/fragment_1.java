package com.example.practice;

import org.json.JSONArray;
import org.json.JSONException;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.practice.R;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class fragment_1  extends Fragment implements OnClickListener{
	private RelativeLayout course , found , set;
	private ImageView course_image  , settings_image ,found_image;
    private TextView  course_text ,settings_text ,found_text ,txv;
    private int gray = 0xFF7597B3;
	private int blue =0xFF0AB2FB;
    private View view;
    private LinearLayout me1;
    RequestQueue RQueue;
    String URL = "http://data.kaohsiung.gov.tw/Opendata/DownLoad.aspx?Type=2&CaseNo1=BA&CaseNo2=1&FileType=2&Lang=C&FolderType=O";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.layout1, container, false);

		initView(); 

		return view;
	}
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.e("DATA Report",  this.toString() + "Fragmen Has be Calllllllllllllllllll ! ");
        RQueue = Volley.newRequestQueue(getActivity());

        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {

                txv.setText(parseJson.parseJsonData(jsonArray));

            }
        };
        new ServerRequest( RQueue , URL , listener).MgetRequest();
    }

    private void initView()
	{			
		me1=(LinearLayout)view.findViewById(R.id.me1);
		course=(RelativeLayout)view.findViewById(R.id.course_layout);
		found=(RelativeLayout)view.findViewById(R.id.found_layout);
		set=(RelativeLayout)view.findViewById(R.id.setting_layout);
		course_image = (ImageView)view.findViewById(R.id.course_image);  
        found_image = (ImageView)view.findViewById(R.id.found_image);  
        settings_image = (ImageView)view.findViewById(R.id.setting_image);  
        course_text = (TextView)view.findViewById(R.id.course_text);  
        found_text = (TextView)view.findViewById(R.id.found_text);  
        settings_text = (TextView)view.findViewById(R.id.setting_text);  
		course.setOnClickListener(this);
		found.setOnClickListener(this);
		set.setOnClickListener(this);
		me1.getBackground().setAlpha(200);
        txv=(TextView)view.findViewById(R.id.txv);
	}

	@Override
	public void onClick(View v) {
		 switch (v.getId()) 
		 {
		 	case R.id.course_layout:  
	            setTabSelection(0);
	           Toast.makeText(getActivity(),"�ڬO��{��", Toast.LENGTH_SHORT).show();
	            break;  
	        case R.id.found_layout:  
	            setTabSelection(1);
	            Toast.makeText(getActivity(),"�ڬO�o�{", Toast.LENGTH_SHORT).show();
	            break;  
	        case R.id.setting_layout:  
	            setTabSelection(2);
	            Toast.makeText(getActivity(),"�ڬO�]�m", Toast.LENGTH_SHORT).show();
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
           
        	course_image.setImageResource(R.drawable.ic_tabbar_course_pressed); 
        	//course.setBackgroundColor(R.drawable.ic_tabbar_bg_click);
        	course_text.setTextColor(blue);
            break;  
        case 1:  
          
        	found_image.setImageResource(R.drawable.ic_tabbar_found_pressed); 
        	//found.setBackgroundColor(R.drawable.ic_tabbar_found_pressed);
        	found_text.setTextColor(blue); 
            break;  
        case 2:  
           
        	settings_image.setImageResource(R.drawable.ic_tabbar_settings_pressed);  
        	//set.setBackgroundColor(R.drawable.ic_tabbar_settings_pressed);
        	settings_text.setTextColor(blue);  
            break; 
            
        default:  
            break; 
        }     
		
	}
	private void resetBtn() {
		course_image.setImageResource(R.drawable.ic_tabbar_course_normal);  
        //course.setBackgroundColor(R.drawable.bottom_bar);  
        course_text.setTextColor(gray);  
        found_image.setImageResource(R.drawable.ic_tabbar_found_normal);  
       // found.setBackgroundColor(R.drawable.bottom_bar);  
        found_text.setTextColor(gray);  
        settings_image.setImageResource(R.drawable.ic_tabbar_settings_normal);  
        //set.setBackgroundColor(R.drawable.bottom_bar);  
        settings_text.setTextColor(gray);  
	}

	
	
}
