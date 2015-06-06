package com.timothy.Fragments;

import org.json.JSONArray;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.timothy.Core.BaseApplication;

import library.timothy.Helpers.JsonHelper;

import com.timothy.R;

import library.timothy.Resources.UriResources;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class fragment_1 extends Fragment implements OnClickListener {
    private RelativeLayout course, found, set;
    private ImageView course_image, settings_image, found_image;
    private TextView course_text, settings_text, found_text, txv;
    private int gray = 0xFF7597B3;
    private int blue = 0xFF0AB2FB;
    private View view;
    private LinearLayout me1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment1, container, false);
        initView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        BaseApplication.getInstance().addToRequestQueue(
                new JsonArrayRequest(
                        Request.Method.GET,
                        UriResources.Test.OpenData1,
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
        me1 = (LinearLayout) view.findViewById(R.id.me1);
        course = (RelativeLayout) view.findViewById(R.id.course_layout);
        found = (RelativeLayout) view.findViewById(R.id.found_layout);
        set = (RelativeLayout) view.findViewById(R.id.setting_layout);
        course_image = (ImageView) view.findViewById(R.id.course_image);
        found_image = (ImageView) view.findViewById(R.id.found_image);
        settings_image = (ImageView) view.findViewById(R.id.setting_image);
        course_text = (TextView) view.findViewById(R.id.course_text);
        found_text = (TextView) view.findViewById(R.id.found_text);
        settings_text = (TextView) view.findViewById(R.id.setting_text);
        course.setOnClickListener(this);
        found.setOnClickListener(this);
        set.setOnClickListener(this);
        me1.getBackground().setAlpha(200);
        txv = (TextView) view.findViewById(R.id.txv);
    }

    @Override
    public void onClick(View v) {
        resetBtn();
        switch (v.getId()) {
            case R.id.course_layout:
                course_image.setImageResource(R.drawable.buttonbar_image_click);
                course_text.setTextColor(blue);
                break;
            case R.id.found_layout:
                found_image.setImageResource(R.drawable.buttonbar_image_click2);
                found_text.setTextColor(blue);
                break;
            case R.id.setting_layout:
                settings_image.setImageResource(R.drawable.buttonbar_image_click3);
                settings_text.setTextColor(blue);
                break;
            default:
                break;
        }

    }

    private void resetBtn() {
        course_image.setImageResource(R.drawable.buttonbar_image_create);
        course_text.setTextColor(gray);
        found_image.setImageResource(R.drawable.buttonbar_image_create2);
        found_text.setTextColor(gray);
        settings_image.setImageResource(R.drawable.buttonbar_image_create3);
        settings_text.setTextColor(gray);
    }
}
