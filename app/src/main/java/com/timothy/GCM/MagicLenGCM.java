package com.timothy.GCM;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
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
import com.timothy.Core.BaseApplication;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import library.timothy.Resources.StringResuorces;
import library.timothy.Resources.UriResources;

public class MagicLenGCM {

    private final static String SENDER_ID = StringResuorces.Gcm.sendId;
    private static final String PROPERTY_REG_ID = StringResuorces.Gcm.propertyReg;
    private static final String PROPERTY_APP_VERSION = StringResuorces.Gcm.proretyAppVersion;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private Activity activity;
    private MagicLenGCMListener listener;
    private String Empty = StringResuorces.Gcm.empty;
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
        GCMPPush(openGCM());
        setMagicLenGCMListener(listener);
    }

    public Activity getActivity() {
        return activity;
    }

    public void setMagicLenGCMListener(MagicLenGCMListener listener) {
        this.listener = listener;
    }

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

    public void GCMPPush(GCMState GCMState) {
        switch (GCMState) {
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
    }

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
            throw new RuntimeException(PROPERTY_APP_VERSION + StringResuorces.Gcm.error);
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

    private void storeRegistrationId(String regId) {
        final SharedPreferences prefs = getGCMPreferences();
        int appVersion = getAppVersion();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    public String getSendREGID() {
        return REGIDforSend;
    }

    private void registerInBackground() {
        new AsyncTaskRegister().execute();
    }

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
                Log.e(PROPERTY_REG_ID + StringResuorces.Gcm.error, ex.getStackTrace().toString());
            }
            return regid;
        }

        @Override
        protected void onPostExecute(String msg) {
            if (listener != null)
                listener.gcmRegistered(!msg.isEmpty(), msg.toString());
        }
    }

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

    public void SendMessage(String message) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(StringResuorces.Key.registrationId, REGIDforSend);
        params.put(StringResuorces.Key.message, message);

        BaseApplication.getInstance()
                .addToRequestQueue(new JsonObjectRequest(Request.Method.POST,
                        UriResources.Server.PushNotification,
                        new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(StringResuorces.Gcm.notice, response.toString());
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(StringResuorces.Gcm.error, error.getMessage(), error);
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<>();
                        headers.put(StringResuorces.Key.accept, StringResuorces.Key.jsonFormat);
                        headers.put(StringResuorces.Key.ContentType, StringResuorces.Key.headerFormat);
                        headers.put(StringResuorces.Key.apiKey, getAPIkey());
                        return headers;
                    }
                });
    }

    public String getAPIkey() {
        return getActivity().getSharedPreferences(StringResuorces.Key.apiKey, Context.MODE_PRIVATE).getString(StringResuorces.Key.apiKey, null);
    }

}