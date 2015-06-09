package com.timothy.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timothy.R;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

public class Artgine extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.artgine ,container ,false );
        AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(getActivity());

        MyAlertDialog.setTitle("I am ArtGine ");

        MyAlertDialog.setMessage(" 折線圖 ~");

        String[] titles = new String[] { "折線1", "折線2" };
        List<double[]> x = new ArrayList<double[]>();
        List<double[]> y = new ArrayList<double[]>();
        x.add(new double[] { 1, 3, 5, 7, 9, 11 });
        x.add(new double[] { 0, 2, 4, 6, 8, 10 });
        y.add(new double[] { 3, 14, 8, 22, 16, 18 });
        y.add(new double[] { 20, 18, 15, 12, 10, 8 });
        XYMultipleSeriesDataset dataset = buildDatset(titles, x, y);

        int[] colors = new int[] { Color.BLUE, Color.GREEN };

        PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND };

        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles, true);

        setChartSettings(renderer, "折線圖展示", "X軸名稱", "Y軸名稱", 0, 12, 0, 25, Color.BLACK);

        View chart = ChartFactory.getLineChartView(getActivity() , dataset, renderer);

        MyAlertDialog.setView(chart);

        MyAlertDialog.setNeutralButton(" Finsh ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onStop();
            }
        });
        MyAlertDialog.show();
        return v;
    }

    protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
                                    String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor) {
        renderer.setChartTitle(title);
        renderer.setChartTitleTextSize(24);
        renderer.setXTitle(xTitle);
        renderer.setYTitle(yTitle);
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setXLabelsColor(Color.BLACK);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setAxesColor(axesColor);
        renderer.setYLabelsColor(0, Color.BLACK);
        renderer.setLabelsColor(Color.BLACK);
        renderer.setMarginsColor(Color.WHITE);
        renderer.setShowGrid(true);
    }

    private XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles, boolean fill) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        int length = colors.length;
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer r = new XYSeriesRenderer();
            r.setColor(colors[i]);
            r.setPointStyle(styles[i]);
            r.setFillPoints(fill);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }

    private XYMultipleSeriesDataset buildDatset(String[] titles, List<double[]> xValues,
                                                List<double[]> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

        int length = titles.length;
        for (int i = 0; i < length; i++) {
            XYSeries series = new XYSeries(titles[i]);
            double[] xV = xValues.get(i);
            double[] yV = yValues.get(i);
            int seriesLength = xV.length;
            for (int k = 0; k < seriesLength; k++)
            {
                series.add(xV[k], yV[k]);
            }
            dataset.addSeries(series);
        }
        return dataset;
    }
}
