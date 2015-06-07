package com.rooandqoo.weartest;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyGridViewPagerAdapter extends FragmentGridPagerAdapter {

    private final Context mContext;
    private List<Fragment> mColumns;

    public MyGridViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;

        mColumns = new ArrayList<>();
        mColumns.add(CustomFragment.createInstance(3));
        mColumns.add(CustomFragment.createInstance(4));
        mColumns.add(CustomFragment.createInstance(5));
    }

    private Fragment cardFragment(String title, String text) {
        CardFragment fragment = CardFragment.create(title, text);
        fragment.setCardMarginBottom(mContext.getResources().getDimensionPixelSize(R.dimen.card_margin_bottom));
        return fragment;
    }

    public Fragment getFragment(int i, int i1) {
        return mColumns.get(i1);
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount(int i) {
        return mColumns.size();
    }
}
