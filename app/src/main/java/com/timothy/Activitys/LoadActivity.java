package com.timothy.Activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.timothy.R;

import java.util.Set;

import library.timothy.Order.Order;
import library.timothy.Resources.StringResources;

import static android.net.NetworkInfo.State.CONNECTED;

/**
 * Created by h94u04 on 2015/6/12.
 * 於APP 最先啟動的Activty 並向Server端驗證是否曾經登入
 */
public class LoadActivity extends Activity {
    private Intent intent;
    private ProgressBar progressBar;
    private Context context;
    private NetworkInfo mNetworkInfo;
    private AlphaAnimation animation;
    //生命週期 於被呼叫時優先執行之一
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_load);
        context = getApplicationContext();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ImageView loadingImage = (ImageView) findViewById(R.id.LoadImage);
        loadingImage.setImageResource(R.drawable.timothycafe);

        animation = new AlphaAnimation(0.1f, 1.0f);
        animation.setDuration(3000);
        loadingImage.setAnimation(animation);
        ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        intent = new Intent( context , LoginActivity.class);
        animation.setAnimationListener(new Animation.AnimationListener() {
            //設定讀取的浮現動畫的生命週期
            @Override
            public void onAnimationStart(Animation animation) {
                progressBar.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd(Animation animationA) {
                progressBar.setVisibility(View.INVISIBLE);
                least();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }
    private void least() {
        Set<String> Login = getSharedPreferences(StringResources.Key.Login , MODE_PRIVATE)
                .getStringSet(StringResources.Key.Login, null) ;
        if (getSharedPreferences(StringResources.Key.ApiKey, MODE_PRIVATE)
                .getString(StringResources.Key.ApiKey, null) != null && Login != null ){
            if(Login.size() != 4)
                intent.setClass(context ,OrderActivity.class);
            else
                intent.setClass(context , MainActivity.class);
        }
        startActivity(intent);
        finish();
    }
}