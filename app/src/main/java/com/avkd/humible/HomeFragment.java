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
        ll_btn06 = view.findViewById(R.id.ll_btn05);

        ll_btn01.setOnClickListener(this);
        ll_btn02.setOnClickListener(this);
        ll_btn03.setOnClickListener(this);
        ll_btn04.setOnClickListener(this);
        ll_btn05.setOnClickListener(this);
        ll_btn06.setOnClickListener(this);

        if(!AVKDConstents.IS_AMULATOR) {
            setupBleListener();
        }

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_btn01:
                if(bt != null) {
                    bt.send("0", false);
                }
                break;
            case R.id.ll_btn02:
                if(bt != null) {
                    bt.send("1", false);
                }
                break;
            case R.id.ll_btn03:
                if(bt != null) {
                    bt.send("2", false);
                }
                break;
            case R.id.ll_btn04:
                if(bt != null) {
                    bt.send("3", false);
                }
                break;
            case R.id.ll_btn05:
                if(bt != null) {
                    bt.send("4", false);
                }
                break;
            case R.id.ll_btn06:
                if(bt != null) {
                    bt.send("5", false);
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (bt != null) {
            bt.stopService(); //블루투스 중지
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (bt != null) {
            if (!bt.isBluetoothEnabled()) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
            } else {
                sw_ble.setChecked(bt.isBluetoothEnabled());
                if(!bt.isServiceAvailable()) {
                    bt.setupService();
                    bt.startService(BluetoothState.DEVICE_OTHER);
//                setup();
                }
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                sw_ble.setChecked(true);
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
//                setup();
            } else {
                sw_ble.setChecked(false);
            }
        }
    }

    private void setupBleListener() {

        //블루투스 스위치
        sw_ble.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                if (!bt.isBluetoothEnabled()){
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
                }
            } else {
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
                }
            }
        });

    }
}
