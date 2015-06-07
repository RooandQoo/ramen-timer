package com.rooandqoo.weartest;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CustomFragment extends Fragment {

    private static final String PARAM_MINUTES = "minutes";

    public static CustomFragment createInstance(int minutes) {
        CustomFragment fragment = new CustomFragment();
        Bundle args = new Bundle();
        args.putInt(PARAM_MINUTES, minutes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.custom_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int minutes = getArguments().getInt(PARAM_MINUTES);

        TextView min = (TextView) view.findViewById(R.id.text_minutes);
        min.setText(String.valueOf(minutes));
    }
}
