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

import library.timothy.Resources.StringResources;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    public static final int NOTIFICATION_ID = 0;
    private Class Class ;
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        String messageType = gcm.getMessageType(intent);
        Resources res = context.getResources();

        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        boolean showingLocked = km.inKeyguardRestrictedInputMode();
        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                Log.i(getClass() + res.getString(R.string.GcmError), extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                Log.i(getClass() + res.getString(R.string.GcmDelete), extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Log.i(getClass() + res.getString(R.string.GcmMessage), extras.toString());
        String content = extras.getString(StringResources.Gcm.Message);
//                if (showingLocked)
                    Intent it = new Intent(context , AlertActivity.class);
                    it.putExtra(StringResources.Gcm.Message, content);
                    it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(it);



                Intent intentNextAction = new Intent(context, OrderActivity.class );
                intentNextAction.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                MagicLenGCM
                        .sendLocalNotification(context,
                                NOTIFICATION_ID, R.drawable.actionbar_menu,
                                res.getString(R.string.FromServer),
                                content,
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
