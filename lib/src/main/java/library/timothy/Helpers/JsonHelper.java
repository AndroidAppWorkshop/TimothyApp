package library.timothy.Helpers;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonHelper {

    private static final String NEWLINE = "\n";
    private static final String COLON = ":";

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
                    stringBuilder.append(str + COLON + jsonObject.get(str) + NEWLINE);
                }
                stringBuilder.append(NEWLINE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return stringBuilder;
    }
}
