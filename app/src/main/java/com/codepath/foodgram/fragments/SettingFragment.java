package com.codepath.foodgram.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.foodgram.R;
import com.codepath.foodgram.details.DetailActivity_ChangePsw;
import com.codepath.foodgram.details.DetailActivity_PersonalInfo;


public class SettingFragment extends Fragment {

    public static final String TAG = "SettingFragment";
    private TextView tvPersonalInfo;
    private TextView tvChangePsw;


    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvPersonalInfo = view.findViewById(R.id.tvPersonalInfo);
        tvChangePsw = view.findViewById(R.id.tvChangePSW);

        tvPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), DetailActivity_PersonalInfo.class);
                startActivity(i);
            }
        });

        tvChangePsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), DetailActivity_ChangePsw.class);
                startActivity(i);
            }
        });
    }
}