package com.timothy.Fragments;

import org.json.JSONArray;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import library.timothy.Helpers.JsonHelper;

import com.timothy.Core.BaseApplication;
import com.timothy.R;

import library.timothy.Resources.UriResources;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class fragment_2 extends Fragment implements OnClickListener {
    private RelativeLayout course;
    private RelativeLayout found;
    private RelativeLayout set;
    private ImageView course_image;
    private ImageView found_image;
    private ImageView settings_image;
    private TextView course_text;
    private TextView settings_text;
    private TextView found_text;
    private TextView txv;
    private int gray = 0xFF7597B3;
    private int blue = 0xFF0AB2FB;
    private LinearLayout me;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment2, container, false);

        initView();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        BaseApplication.getInstance().addToRequestQueue(
                new JsonArrayRequest(
                        Request.Method.GET,
                        UriResources.Test.OpenData2,
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
        me = (LinearLayout) view.findViewById(R.id.me);
        course = (RelativeLayout) view.findViewById(R.id.course);
        found = (RelativeLayout) view.findViewById(R.id.found);
        set = (RelativeLayout) view.findViewById(R.id.setting);
        course_image = (ImageView) view.findViewById(R.id.course_image_a);
        found_image = (ImageView) view.findViewById(R.id.found_image_b);
        settings_image = (ImageView) view.findViewById(R.id.setting_image_c);
        course_text = (TextView) view.findViewById(R.id.course_text_a);
        found_text = (TextView) view.findViewById(R.id.found_text_b);
        settings_text = (TextView) view.findViewById(R.id.setting_text_c);
        course.setOnClickListener(this);
        found.setOnClickListener(this);
        set.setOnClickListener(this);
        me.getBackground().setAlpha(200);
        txv = (TextView) view.findViewById(R.id.txv);
    }

    @Override
    public void onClick(View v) {
        resetBtn();
        switch (v.getId()) {
            case R.id.course:
                course_image.setImageResource(R.drawable.buttonbar_image_click4);
                course_text.setTextColor(blue);
                break;
            case R.id.found:
                found_image.setImageResource(R.drawable.buttonbar_image_click5);
                found_text.setTextColor(blue);
                break;
            case R.id.setting:
                settings_image.setImageResource(R.drawable.buttonbar_image_click6);
                settings_text.setTextColor(blue);
                break;
            default:
                break;
        }
    }

    private void resetBtn() {
        course_image.setImageResource(R.drawable.buttonbar_image_create4);
        course_text.setTextColor(gray);
        found_image.setImageResource(R.drawable.buttonbar_image_create5);
        found_text.setTextColor(gray);
        settings_image.setImageResource(R.drawable.buttonbar_image_create6);
        settings_text.setTextColor(gray);
    }

}
