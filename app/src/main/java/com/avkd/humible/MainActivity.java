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
import android.widget.TextView;
import android.widget.Toast;

import com.avkd.humible.util.ToastUtil;

import java.util.List;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_add_device;
    ImageButton btn_humi_on;
    ImageButton btn_roomi_on;
    BluetoothSPP bt;
    Switch sw_ble;

    TextView tv_ht;
    TextView tv_tp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        bt = new BluetoothSPP(this);
//        if(!bt.isBluetoothAvailable()) {
//            ToastUtil.showMessage(this, "블루투스를 지원하지 않는 기기입니다.");
//            finish();
//        }

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

        tv_ht = findViewById(R.id.tv_ht);
        tv_tp = findViewById(R.id.tv_tp);

        AVKDData.makeDumpData(getApplicationContext());
        List<Record> records = AVKDData.getGraph(getApplicationContext(), Date.TODAY, AVKDConstents.HUMIDT_DATA_CODE);
        int ht = (int) (records.size() > 0? records.get(records.size()-1).getValue() : 0.0f);
        records = AVKDData.getGraph(getApplicationContext(), Date.TODAY, AVKDConstents.TEMPR_DATA_CODE);
        int tp = (int) (records.size() > 0? records.get(records.size()-1).getValue() : 0.0f);

        tv_ht.setText(ht+"");
        tv_tp.setText(tp+"");

//        setupBleListener();

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

        if (bt != null) {
            bt.stopService(); //블루투스 중지
        }
    }

    @Override
    protected void onStart() {
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

    private void updateHumi() {
        List<Record> records = AVKDData.getGraph(getApplicationContext(), Date.TODAY, AVKDConstents.HUMIDT_DATA_CODE);
        float ht = records.size() > 0? records.get(records.size()-1).getValue() : 0.0f;
        records = AVKDData.getGraph(getApplicationContext(), Date.TODAY, AVKDConstents.HUMIDF_DATA_CODE);
        float hf = records.size() > 0? records.get(records.size()-1).getValue() : 0.0f;
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

            if (data.length == 2) {
                int temper = data[0];   // 온도
                int humi = data[1];     // 습도
                Log.d("BluetoothService", "OnDataReceived - 온도: " + temper + " | 습도: " + humi);
            }

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

    private String bytes2String(byte[] b, int count) {

        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < count; i++)
        {
            String myInt = Integer.toHexString((int) (b[i] & 0xFF));
            ret.append("0x" + myInt);
        }
        return ret.toString();
    }
}