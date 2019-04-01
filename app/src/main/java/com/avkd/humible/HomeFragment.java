package com.avkd.humible;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.avkd.humible.util.ToastUtil;

import java.time.LocalDate;
import java.util.Calendar;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;

public class HomeFragment extends Fragment implements View.OnClickListener {

    BluetoothSPP bt;
    Switch sw_ble;
    LinearLayout ll_btn01;
    LinearLayout ll_btn02;
    LinearLayout ll_btn03;
    LinearLayout ll_btn04;
    LinearLayout ll_btn05;
    LinearLayout ll_btn06;

    public static Fragment newInstance() {
        Fragment frag = new HomeFragment();
        Bundle args = new Bundle();

        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        if(!AVKDConstents.IS_AMULATOR) {
            bt = new BluetoothSPP(getContext());
        }

        View customView = ((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView();
        sw_ble = customView.findViewById(R.id.sw_ble);

        ll_btn01 = view.findViewById(R.id.ll_btn01);
        ll_btn02 = view.findViewById(R.id.ll_btn02);
        ll_btn03 = view.findViewById(R.id.ll_btn03);
        ll_btn04 = view.findViewById(R.id.ll_btn04);
        ll_btn05 = view.findViewById(R.id.ll_btn05);
        ll_btn06 = view.findViewById(R.id.ll_btn06);

        ll_btn01.setOnClickListener(this);
        ll_btn02.setOnClickListener(this);
        ll_btn03.setOnClickListener(this);
        ll_btn04.setOnClickListener(this);
        ll_btn05.setOnClickListener(this);
        ll_btn06.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_btn01:

                if(bt != null) {
                    if(ll_btn01.getTag() == null || !((boolean) ll_btn01.getTag())) {
                        Log.d("ll_btn01", "ON");
                        ll_btn01.setTag(true);
                        bt.send("1", false);
                    } else {
                        Log.d("ll_btn01", "OFF");
                        ll_btn01.setTag(false);
                        bt.send("254", false);
                    }
                }
                break;
            case R.id.ll_btn02:
                if(bt != null) {
                    if(ll_btn02.getTag() == null || !((boolean) ll_btn02.getTag())) {
                        Log.d("ll_btn02", "ON");
                        ll_btn02.setTag(true);
                        bt.send("2", false);
                    } else {
                        Log.d("ll_btn02", "OFF");
                        ll_btn02.setTag(false);
                        bt.send("253", false);
                    }
                }
                break;
            case R.id.ll_btn03:
                if(bt != null) {
                    if(ll_btn03.getTag() == null || !((boolean) ll_btn03.getTag())) {
                        Log.d("ll_btn03", "ON");
                        ll_btn03.setTag(true);
                        bt.send("4", false);
                    } else {
                        Log.d("ll_btn03", "OFF");
                        ll_btn03.setTag(false);
                        bt.send("251", false);
                    }
                }
                break;
            case R.id.ll_btn04:
                if(bt != null) {
                    if(ll_btn04.getTag() == null || !((boolean) ll_btn04.getTag())) {
                        Log.d("ll_btn04", "ON");
                        ll_btn04.setTag(true);
                        bt.send("8", false);
                    } else {
                        Log.d("ll_btn04", "OFF");
                        ll_btn04.setTag(false);
                        bt.send("247", false);
                    }
                }
                break;
            case R.id.ll_btn05:
                if(bt != null) {
                    if(ll_btn05.getTag() == null || !((boolean) ll_btn05.getTag())) {
                        Log.d("ll_btn05", "ON");
                        ll_btn05.setTag(true);
                        bt.send("16", false);
                    } else {
                        Log.d("ll_btn05", "OFF");
                        ll_btn05.setTag(false);
                        bt.send("239", false);
                    }
                }
                break;
            case R.id.ll_btn06:
                if(bt != null) {
                    if(ll_btn06.getTag() == null || !((boolean) ll_btn06.getTag())) {
                        Log.d("ll_btn06", "ON");
                        ll_btn06.setTag(true);
                        bt.send("32", false);
                    } else {
                        Log.d("ll_btn06", "OFF");
                        ll_btn06.setTag(false);
                        bt.send("223", false);
                    }
                }
                break;
        }
    }

}
