package com.sam_chordas.android.stockhawk.ui;

import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;
import com.db.chart.view.Tooltip;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.model.Quote;

import java.util.ArrayList;
import java.util.Collections;

public class GraphActivity extends AppCompatActivity {
    private static final String LOG_TAG = GraphActivity.class.getSimpleName();
    private final String[] mMonths = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private LineChartView mLineChart;
    private ArrayList<Quote> quoteArrayList;
    private String mSymbl;
    private Tooltip mTip;
    private Runnable mBaseAction;
    private Typeface robotoLight;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //       mContext = this;

        setContentView(R.layout.activity_line_graph);
        mLineChart = (LineChartView) findViewById(R.id.linechart);

        Intent intent = getIntent();
        mSymbl = intent.getStringExtra("symbol");
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.graph_activity_title, mSymbl));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        //if savedInstanceState is null
        if (savedInstanceState == null) {

            quoteArrayList = intent.getParcelableArrayListExtra("graphdata");
        } else quoteArrayList = savedInstanceState.getParcelableArrayList("graphdata");
        drawGraphofLastWeek();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("graphdata", quoteArrayList);
    }

    private void drawGraphofLastWeek() {
        LineSet mLineSet = new LineSet();
        // Tooltip
        mTip = new Tooltip(getApplicationContext(), R.layout.linechart_three_tooltip, R.id.value);
        robotoLight = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Roboto-Thin.ttf");

        ((TextView) mTip.findViewById(R.id.value))
                .setTypeface(robotoLight);

        mTip.setVerticalAlignment(Tooltip.Alignment.BOTTOM_TOP);
        mTip.setDimensions((int) Tools.fromDpToPx(65), (int) Tools.fromDpToPx(25));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {

            mTip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f),
                    PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)).setDuration(200);

            mTip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 0),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f),
                    PropertyValuesHolder.ofFloat(View.SCALE_X, 0f)).setDuration(200);

            mTip.setPivotX(Tools.fromDpToPx(65) / 2);
            mTip.setPivotY(Tools.fromDpToPx(25));
        }

        mLineChart.setTooltips(mTip);

        final ArrayList<Float> range = new ArrayList();
        int i = 0;
        int month = 0;
        for (i = quoteArrayList.size() - 1; i >= 0; i--) {
            Quote item = quoteArrayList.get(i);
            float closingVal = (float) item.getClose();
            range.add(closingVal);
            int temp = Integer.parseInt(item.getStrDate().split("-")[1]);
            if (temp != month) {
                mLineSet.addPoint(mMonths[temp], closingVal);
                month = temp;
            } else
                mLineSet.addPoint("", closingVal);
        }

        int minRange = Math.round(Collections.min(range));
        int maxRange = Math.round(Collections.max(range));
        maxRange = ((maxRange + 9) / 10) * 10;


        mLineSet.setColor(Color.parseColor("#758cbb"))
                .setFill(Color.parseColor("#2d374c"))
                .setDotsColor(Color.parseColor("#758cbb"))
                .setThickness(1)
                .setDashed(new float[]{10f, 10f});

        minRange = ((minRange - 9) / 10) * 10;

        mLineChart.setAxisBorderValues(minRange, maxRange, 10).setLabelsColor(Color.parseColor("#FF8E9196"));

        mLineChart.addData(mLineSet);
        mLineChart.show();

    }
}