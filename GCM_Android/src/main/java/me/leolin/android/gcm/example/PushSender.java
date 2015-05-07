package me.leolin.android.gcm.example;

import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class PushSender {//將regid,apikey 給server,傳送推播ID給伺服器
    public static final String API_KEY = "AIzaSyBZJ4et-5GtExXEFvVHAbDCZ0KP7cWGNxk";
    public static final String SERVER_URL = "http://jasonchi.ddns.net:8080/api/PushNotification";

    private static final String LOG_TAG = PushSender.class.getSimpleName();

    public static void pushMessage(final String regId, final String message) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("Message", message);
                map.put("RegistrationId", regId);

                //Map<String, Object> data = new HashMap<String, Object>();
                //data.put("Message", message);

                //map.put("Data", new JSONObject(data));//記得要轉json格式不然會傳回來會null

                JSONObject jsonObject = new JSONObject(map);

                String jsonBody = jsonObject.toString();
                //29~40將這些資訊組成一個json,42~47開始把這個json送給server,49~..srver送回來的訊息
                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpPost post = new HttpPost(SERVER_URL);
                    post.addHeader("content-type", "application/json");
                    post.setEntity(new StringEntity(jsonBody,"utf-8"));
                    HttpResponse response = client.execute(post);

                    if (response.getStatusLine().getStatusCode() == 200) {
                        String resBody = EntityUtils.toString(response.getEntity(), "UTF-8");
                        Log.d(LOG_TAG, resBody);
                    } else {
                        Log.d(LOG_TAG, "Fail:" + response.getStatusLine().getStatusCode());
                    }
                } catch (IOException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                }
                return null;
            }
        }.execute();
    }
}
