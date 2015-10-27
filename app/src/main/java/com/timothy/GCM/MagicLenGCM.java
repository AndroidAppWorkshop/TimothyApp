package com.timothy.GCM;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.timothy.Activitys.AlertActivity;
import com.timothy.Core.BaseApplication;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import library.timothy.Resources.StringResources;
import library.timothy.Resources.UriResources;
/**
 * 處理接收到的回傳
 * 將回傳通知於使用者
 * 與產生手機RegistrationId並儲存( Phone Identification Code )
 **/
public class MagicLenGCM {

    private final static String SENDER_ID = StringResources.Gcm.SendId;
    private static final String PROPERTY_REG_ID = StringResources.Gcm.PropertyReg;
    private static final String PROPERTY_APP_VERSION = StringResources.Gcm.ProretyAppVersion;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private Activity activity;
    private MagicLenGCMListener listener;
    private String Empty = StringResources.Gcm.Empty;
    private String REGIDforSend = Empty;
    private String regidforlocal = Empty;

    public static enum PlayServicesState {SUPPROT, NEED_PLAY_SERVICE, UNSUPPORT;}

    public static enum GCMState {PLAY_SERVICES_NEED_PLAY_SERVICE, PLAY_SERVICES_UNSUPPORT, NEED_REGISTER, AVAILABLE;}

    public static interface MagicLenGCMListener {

        public void gcmRegistered(boolean successfull, String regID);

        public boolean gcmSendRegistrationIdToAppServer(String regID);
    }

    public MagicLenGCM(Activity activity) {
        this(activity, null);
    }

    public MagicLenGCM(Activity activity, MagicLenGCMListener listener) {
        this.activity = activity;
        GCMcheck();
        setMagicLenGCMListener(listener);
    }

    public Activity getActivity() {
        return activity;
    }

    public void setMagicLenGCMListener(MagicLenGCMListener listener) {
        this.listener = listener;
    }
    //獲得Gcm狀態
    private GCMState openGCM() {
        switch (checkPlayServices()) {
            case SUPPROT:
                regidforlocal = getRegistrationId();
                if (regidforlocal.isEmpty()) {
                    return GCMState.NEED_REGISTER;
                } else {
                    return GCMState.AVAILABLE;
                }
            case NEED_PLAY_SERVICE:
                return GCMState.PLAY_SERVICES_NEED_PLAY_SERVICE;
            default:
                return GCMState.PLAY_SERVICES_UNSUPPORT;
        }
    }
    //以Gcm目前狀態，採取相對行為
    public MagicLenGCM GCMcheck() {
        switch (openGCM()) {
            case NEED_REGISTER:
                registerInBackground();
                REGIDforSend = getRegistrationId();
                break;
            case AVAILABLE:
                REGIDforSend = regidforlocal;
                break;
            case PLAY_SERVICES_NEED_PLAY_SERVICE:
                break;
            case PLAY_SERVICES_UNSUPPORT:
                break;
            default:
                break;
        }
        return this;
    }
    //載入緩存中的識別碼
    private String getRegistrationId() {
        final SharedPreferences prefs = getGCMPreferences();
        String registrationId = prefs.getString(PROPERTY_REG_ID, Empty);
        if (registrationId.isEmpty()) {
            return Empty;
        }
        int registeredVersion = prefs.getInt(MagicLenGCM.PROPERTY_APP_VERSION,
                Integer.MIN_VALUE);
        int currentVersion = getAppVersion();
        if (registeredVersion != currentVersion) {
            return Empty;
        }
        return registrationId;
    }

    private int getAppVersion() {
        try {
            PackageInfo packageInfo = activity.getPackageManager()
                    .getPackageInfo(activity.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            throw new RuntimeException(PROPERTY_APP_VERSION + StringResources.Gcm.Error);
        }
    }

    private SharedPreferences getGCMPreferences() {
        return activity.getSharedPreferences(activity.getClass()
                .getSimpleName(), Context.MODE_PRIVATE);
    }

    private PlayServicesState checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity, PLAY_SERVICES_RESOLUTION_REQUEST).show();
                return PlayServicesState.NEED_PLAY_SERVICE;
            } else {
                return PlayServicesState.UNSUPPORT;
            }
        }
        return PlayServicesState.SUPPROT;
    }
    //儲存 Identification Code
    private void storeRegistrationId(String regId) {
        final SharedPreferences prefs = getGCMPreferences();
        int appVersion = getAppVersion();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    private void registerInBackground() {
        new AsyncTaskRegister().execute();
    }
    //向Google產生Identification Code
    private final class AsyncTaskRegister extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String regid = Empty;
            try {
                GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(activity);

                regid = gcm.register(SENDER_ID);

                if (regid == null || regid.isEmpty()) {
                    return Empty;
                }
                storeRegistrationId(regid);

                if (listener != null && !listener.gcmSendRegistrationIdToAppServer(regid)) {
                    storeRegistrationId(Empty);
                    return Empty;
                }
            } catch (IOException ex) {
                Log.e(PROPERTY_REG_ID + StringResources.Gcm.Error, ex.getStackTrace().toString());
            }
            return regid;
        }

        @Override
        protected void onPostExecute(String msg) {
            if (listener != null)
                listener.gcmRegistered(!msg.isEmpty(), msg.toString());
        }
    }
    //Server回傳以振動並通知顯示的載入
    public static void sendLocalNotification(Context context, int notifyID, int drawableSmallIcon,
                                             String title, String msg, String info,
                                             boolean autoCancel, PendingIntent pendingIntent) {

        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(drawableSmallIcon)
                .setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(autoCancel)
                .setContentInfo(info)
                .setDefaults(Notification.DEFAULT_ALL);

        if (msg.length() > 10)
            mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(msg));

        mBuilder.setContentIntent(pendingIntent);
        mNotificationManager.notify(notifyID, mBuilder.build());
    }
    public String getAPIkey() {
        return getActivity().getSharedPreferences(StringResources.Key.ApiKey, Context.MODE_PRIVATE).getString(StringResources.Key.ApiKey, null);
    }
    public String getSendRegID() {
        return REGIDforSend;
    }
}