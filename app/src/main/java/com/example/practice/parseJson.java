package com.example.practice;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class parseJson {
	
	public parseJson() 
	{
		
	}
	
	//��@����json object
	public static  StringBuilder parseJsonData(JSONArray data)
	{	
		StringBuilder  SchoolData=new StringBuilder();
		try 
		{	
			Iterator<String>  iterator;
			
						
			for(int i= 0;i<data.length();i++)
			{	JSONObject  school =data.getJSONObject(i);
				
				iterator=school.keys();//JSONObject�����okey���|�ǤJiterator
				while(iterator.hasNext())
				{
					String str=iterator.next().toString();//��key ID
					SchoolData.append(str+":"+school.get(str) +"\n");
				
				}
				 SchoolData.append("\n\n");
			} 	
			
		}
		catch (JSONException e) {
			
			e.printStackTrace();
		}
		return SchoolData;	
	}
	
}
