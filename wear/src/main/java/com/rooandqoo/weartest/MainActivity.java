package com.rooandqoo.weartest;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.view.WindowInsets;

public class MainActivity extends Activity {

    private GridViewPager mGridViewPager;
    private DotsPageIndicator mDotsPageIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mGridViewPager = (GridViewPager) stub.findViewById(R.id.pager);
                mDotsPageIndicator = (DotsPageIndicator) stub.findViewById(R.id.page_indicator);
                setupViewPager();
            }
        });
    }

    private void setupViewPager() {
        final Resources res = getResources();
        mGridViewPager.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View view, WindowInsets insets) {
                final boolean round = insets.isRound();
                int rowMargin = res.getDimensionPixelOffset(R.dimen.page_row_margin);
                int colMargin = res.getDimensionPixelOffset(round ?
                        R.dimen.page_column_margin_round : R.dimen.page_column_margin);
                mGridViewPager.setPageMargins(rowMargin, colMargin);
                mGridViewPager.onApplyWindowInsets(insets);
                return insets;
            }
        });

        mGridViewPager.setAdapter(new MyGridViewPagerAdapter(this, getFragmentManager()));
        mDotsPageIndicator.setPager(mGridViewPager);
    }

}
