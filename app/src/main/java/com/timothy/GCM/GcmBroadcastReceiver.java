package com.timothy.GCM;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.timothy.Activitys.OrderActivity;
import com.timothy.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    public static final int NOTIFICATION_ID = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                Log.i(getClass() + " GCM ERROR", extras.toString());
            }
            else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                Log.i(getClass() + " GCM DELETE", extras.toString());
            }
            else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Log.i(getClass() + " GCM MESSAGE", extras.toString());

                Intent intentNextAction = new Intent(context, OrderActivity.class);

                intentNextAction.putExtra("Content", "this is a message from Intent " + extras.getString("message"));

                MagicLenGCM
                        .sendLocalNotification(context,
                                               NOTIFICATION_ID, R.drawable.actionbar_menu,
                                               "From Jason Server ---",
                                               extras.getString("message"),
                                               " -Timothy- ", true,
                                               PendingIntent.getActivity(context,
                                                                          NOTIFICATION_ID, intentNextAction ,
                                                                          PendingIntent.FLAG_UPDATE_CURRENT )
                                               );
            }
        }
        setResultCode(Activity.RESULT_OK);
    }

}
