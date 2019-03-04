package com.avkd.humible;

import android.app.Activity;
import android.app.ActivityOptions;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_add_device;
    ImageButton btn_humi_on;
    ImageButton btn_roomi_on;
    BluetoothSPP bt;
    private Switch sw_ble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt = new BluetoothSPP(this);
        if(!bt.isBluetoothAvailable()) {
            showToastMessage("블루투스를 지원하지 않는 기기입니다.");
            finish();
        }

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View customView = getSupportActionBar().getCustomView();

        Toolbar parent =(Toolbar) customView.getParent();
        parent.setContentInsetsAbsolute(0,0);


        Button btn_add_device = findViewById(R.id.btn_add_device);
        ImageButton btn_humi_on = findViewById(R.id.btn_humi_on);
        ImageButton btn_roomi_on = findViewById(R.id.btn_roomi_on);
        sw_ble = customView.findViewById(R.id.sw_ble);

        btn_add_device.setOnClickListener(this);
        btn_humi_on.setOnClickListener(this);
        btn_roomi_on.setOnClickListener(this);


        setupBleListener();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_add_device:
                addDevice();
                break;
            case R.id.btn_humi_on:
                openMenuActivity();
                break;
            case R.id.btn_roomi_on:
                openMenuActivity();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        bt.stopService(); //블루투스 중지
    }

    @Override
    protected void onStart() {
        super.onStart();

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

        //데이터 수신
        bt.setOnDataReceivedListener((data, message) -> {
            Log.d("Bluetooth", "Listener Data - " + message);
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        });

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {

            public void onDeviceConnected(String name, String address) { //연결됐을 때
                Toast.makeText(getApplicationContext()
                        , "Connected to " + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() { //연결해제
                Toast.makeText(getApplicationContext()
                        , "Connection lost", Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() { //연결실패
                Toast.makeText(getApplicationContext()
                        , "Unable to connect", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openMenuActivity() {

        Intent intent = new Intent(this, MenuActivity.class);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out);
        startActivity(intent, options.toBundle());
    }

    private void addDevice() {

        if (!bt.isBluetoothEnabled() || !sw_ble.isChecked()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            Intent intent = new Intent(getApplicationContext(), DeviceList.class);
            startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
        }
    }

    private void showToastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}