package com.timothy.Fragments;

import org.json.JSONArray;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import library.timothy.Helpers.JsonHelper;

import com.timothy.Core.BaseApplication;
import com.timothy.R;

import library.timothy.Resources.UriResources;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class fragment_3 extends Fragment implements OnClickListener {
    private RelativeLayout course, found, set;
    private ImageView course_image, found_image, settings_image;
    private TextView found_text, txv, settings_text, course_text;
    private int gray = 0xFF7597B3;
    int green = Color.GREEN;
    View view;
    private LinearLayout me3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment3, container, false);

        initView();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        BaseApplication.getInstance().addToRequestQueue(
                new JsonArrayRequest(
                        Request.Method.GET,
                        UriResources.Test.OpenData3,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray jsonArray) {
                                txv.setText(JsonHelper.readLine(jsonArray));
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                            }
                        }
                )
        );
    }

    private void initView() {
        me3 = (LinearLayout) view.findViewById(R.id.me3);
        course = (RelativeLayout) view.findViewById(R.id.cou);
        found = (RelativeLayout) view.findViewById(R.id.fou);
        set = (RelativeLayout) view.findViewById(R.id.set);
        course_image = (ImageView) view.findViewById(R.id.course_image1);
        found_image = (ImageView) view.findViewById(R.id.found_image2);
        settings_image = (ImageView) view.findViewById(R.id.setting_image3);
        course_text = (TextView) view.findViewById(R.id.course_text1);
        found_text = (TextView) view.findViewById(R.id.found_text2);
        settings_text = (TextView) view.findViewById(R.id.setting_text3);
        course.setOnClickListener(this);
        found.setOnClickListener(this);
        set.setOnClickListener(this);
        me3.getBackground().setAlpha(200);
        txv = (TextView) view.findViewById(R.id.txv);
    }

    @Override
    public void onClick(View v) {
        resetBtn();
        switch (v.getId()) {
            case R.id.cou:
                course_image.setImageResource(R.drawable.buttonbar_image_click7);
                course_text.setTextColor(green);
                break;
            case R.id.fou:
                found_image.setImageResource(R.drawable.buttonbar_image_click8);
                found_text.setTextColor(green);
                break;
            case R.id.set:
                settings_image.setImageResource(R.drawable.buttonbar_image_click9);
                settings_text.setTextColor(green);
                break;
            default:
                break;
        }
    }

    private void resetBtn() {
        course_image.setImageResource(R.drawable.buttonbar_image_create7);
        course_text.setTextColor(gray);
        found_image.setImageResource(R.drawable.buttonbar_image_create8);
        found_text.setTextColor(gray);
        settings_image.setImageResource(R.drawable.buttonbar_image_create9);
        settings_text.setTextColor(gray);
    }
}

	



	

