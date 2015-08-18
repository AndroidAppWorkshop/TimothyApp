package com.timothy.Activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.timothy.R;
import library.timothy.Resources.StringResources;

/**
 * Created by h94u04 on 2015/6/12.
 */
public class LoadActivity extends Activity {

    private Intent intent;
    private ProgressBar progressBar;
    private Context context;
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
        loadingImage.setImageResource(R.drawable.actionbar_menu);

        AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
        animation.setDuration(3000);
        loadingImage.setAnimation(animation);

        intent = new Intent( context , LoginActivity.class);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                progressBar.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd(Animation animation) {

             if (getSharedPreferences(StringResources.Key.ApiKey, MODE_PRIVATE)
                                .getString(StringResources.Key.ApiKey, null) != null &&
                getSharedPreferences(StringResources.Key.Login , MODE_PRIVATE)
                                .getStringSet(StringResources.Key.Login, null ) != null ) {
                 intent.setClass(context , MainActivity.class);
                 startActivity(intent);
             }
             else
                 startActivity(intent);

             progressBar.setVisibility(View.INVISIBLE);
             finish();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}