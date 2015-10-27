package com.timothy.GCM;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.timothy.Activitys.AlertActivity;
import com.timothy.Activitys.OrderActivity;
import com.timothy.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import library.timothy.Order.OrderDetail;
import library.timothy.Resources.StringResources;
/**
 * Receiver接收器
 * 接收Server端回傳
 **/
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    public static final int NOTIFICATION_ID = 0;
    private Class Class ;
    private String OrderId ="";
    private Map<String,String> data = new HashMap<>();
    StringBuilder sb=new StringBuilder();
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        String messageType = gcm.getMessageType(intent);
        Resources res = context.getResources();

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                Log.i(getClass() + res.getString(R.string.GcmError), extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                Log.i(getClass() + res.getString(R.string.GcmDelete), extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Log.i(getClass() + res.getString(R.string.GcmMessage), extras.toString());
        String content = extras.getString(StringResources.Gcm.Message);
                Intent it = new Intent(context , AlertActivity.class);
                try {
                    JSONArray ja = new JSONArray(content);
                    OrderId = ja.getJSONObject(0).getString(StringResources.Key.OrderID);
                    OrderDetail detail = new OrderDetail(ja);
                    data = detail.getChild();
                    for (Map.Entry<String , String > entry : data.entrySet())
                        sb.append(entry.getValue());
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                it.putExtra(StringResources.Gcm.Message, sb.toString());
                it.putExtra(StringResources.Gcm.SendId, OrderId);
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(it);
                Intent intentNextAction = new Intent(context, OrderActivity.class );
                intentNextAction.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                MagicLenGCM
                        .sendLocalNotification(context,
                                NOTIFICATION_ID, R.drawable.actionbar_menu,
                                res.getString(R.string.FromServer),
                                sb.toString(),
                                res.getString(R.string.app_name), true,
                                PendingIntent.getActivity(context,
                                        NOTIFICATION_ID, intentNextAction,
                                        PendingIntent.FLAG_UPDATE_CURRENT)
                        );
            }

        }
        setResultCode(Activity.RESULT_OK);
    }

}
