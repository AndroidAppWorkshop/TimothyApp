package com.timothy.Tools;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONHelper {

    public static StringBuilder readLine(JSONArray jsonArray) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Iterator<String> iterator;
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                iterator = jsonObject.keys();
                while (iterator.hasNext()) {
                    String str = iterator.next().toString();
                    stringBuilder.append(str + ":" + jsonObject.get(str) + "\n");
                }
                stringBuilder.append("\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return stringBuilder;
    }
}
