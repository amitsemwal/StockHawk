package com.sam_chordas.android.stockhawk.ui;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;
import com.sam_chordas.android.stockhawk.Quote;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collections;

public class GraphActivity extends Activity  implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = GraphActivity.class.getSimpleName();
    public static final int CURSOR_LOADER_ID=0;
    private LineChartView mLineChart;
    private Intent mServiceIntent;
    private Context mContext;
    private Cursor mCursor;
    private boolean isConnected;
    private ArrayList<Quote> quoteArrayList;
    private String mSymbl;
    private Bundle args = new Bundle();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        setContentView(R.layout.activity_line_graph);
        mLineChart = (LineChartView) findViewById(R.id.linechart);

        Intent intent = getIntent();
        mSymbl = intent.getStringExtra("symbol");
        args.putString("symbol",mSymbl);
       /* quoteArrayList = intent.getParcelableArrayListExtra("graphdata");
        Collections.sort(quoteArrayList, new Comparator<Quote>() {
            @Override
            public int compare(Quote lhs, Quote rhs) {
                return lhs.getRecordDate().compareTo(rhs.getRecordDate());
            }
        });
        drawGraphofLastWeek();
*/
//        Log.e(LOG_TAG, args.getString("symbol"));
        getLoaderManager().initLoader(CURSOR_LOADER_ID, args, this);

    }

    private void drawGraphofLastWeek() {
        LineSet mLineSet = new LineSet();

        ArrayList<Float> range = new ArrayList();
        int i = 0;
        for (i = 6; i >= 0; i--) {
            Quote item = quoteArrayList.get(i);
            float closingVal = (float) item.getClose();
            range.add(closingVal);
            mLineSet.addPoint(Integer.toString(i++), closingVal);

        }

        int minRange = Math.round(Collections.min(range));
        int maxRange = Math.round(Collections.max(range));

        mLineSet.setDotsColor(Color.parseColor("#00BFFF"));
        mLineChart.setAxisBorderValues(minRange - 100, maxRange + 100).setLabelsColor(Color.parseColor("#FF8E9196"));

        mLineChart.addData(mLineSet);
        mLineChart.show();

    }

        @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, args, this);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, QuoteProvider.Quotes.CONTENT_URI,
                new String[]{},
                QuoteColumns.SYMBOL + " = ?",
                new String[]{args.getString("symbol")},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        int i = 1;
        DatabaseUtils.dumpCursor(mCursor);

        LineSet mLineSet = new LineSet();

        ArrayList<Float> range = new ArrayList<Float>();
        mCursor.moveToFirst();
        while (mCursor.moveToNext()) {
            float price = Float.parseFloat(mCursor.getString(mCursor.getColumnIndex(QuoteColumns.BIDPRICE)));
            range.add(price);
            mLineSet.addPoint(Integer.toString(i), price);
            i++;
        }

        int minRange = Math.round(Collections.min(range));
        int maxRange = Math.round(Collections.max(range));

        mLineSet.setDotsColor(Color.parseColor("#00BFFF"));
        mLineChart.setAxisBorderValues(minRange - 100, maxRange + 100).setLabelsColor(Color.parseColor("#FF8E9196"));

        mLineChart.addData(mLineSet);
        mLineChart.show();


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Subscribe
    private void drawGraphofHistoricalData() {
        LineSet mLineSet = new LineSet();

        ArrayList<Float> range = new ArrayList();
        int i = 0;
        for (Quote item :
                quoteArrayList) {
            float closingVal = (float) item.getClose();
            range.add(closingVal);
            mLineSet.addPoint(Integer.toString(i++), closingVal);
        }

        int minRange = Math.round(Collections.min(range));
        int maxRange = Math.round(Collections.max(range));

        mLineSet.setDotsColor(Color.parseColor("#00BFFF"));
        mLineChart.setAxisBorderValues(minRange - 100, maxRange + 100).setLabelsColor(Color.parseColor("#FF8E9196"));

        mLineChart.addData(mLineSet);
        mLineChart.show();

    }
}
