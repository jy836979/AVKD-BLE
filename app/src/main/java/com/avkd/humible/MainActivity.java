package com.avkd.humible;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
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

import java.util.Calendar;
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

    BroadcastReceiver mReceiver;

    int rcvCnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!AVKDConstents.IS_AMULATOR) {
            bt = new BluetoothSPP(this);
            if(!bt.isBluetoothAvailable()) {
                ToastUtil.showMessage(this, "블루투스를 지원하지 않는 기기입니다.");
                finish();
            }
//            bt.autoConnect("IOIO");
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

        tv_ht = findViewById(R.id.tv_ht);
        tv_tp = findViewById(R.id.tv_tp);

        settingReceiver();

        AVKDData.makeDumpData(getApplicationContext());
        updateHtAndTpData();

        if(!AVKDConstents.IS_AMULATOR) {
            setupBleListener();
        } else {
            new BleReciveDemo(getApplicationContext()).sendEmptyMessage(BleReciveDemo.TIMER_1);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_add_device:
                if(!AVKDConstents.IS_AMULATOR) {
                    addDevice();
                }
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
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AVKDConstents.BLE_RECIVE_DATA);
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
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

            if (data.length == 2 && rcvCnt % 5 == 0) {

                rcvCnt++;

                Calendar cal = Calendar.getInstance();
                int time = cal.get(Calendar.HOUR_OF_DAY);

                Record temper = new Record(time, data[0]);    // 온도
                Record humi = new Record(time, data[1]);      // 습도

                AVKDData.setRecord(this, Date.TODAY, AVKDConstents.TEMPR_DATA_CODE, temper);
                AVKDData.setRecord(this, Date.TODAY, AVKDConstents.HUMIDT_DATA_CODE, humi);

                Intent intent = new Intent(AVKDConstents.BLE_RECIVE_DATA);
                sendBroadcast(intent);

                Log.d("BluetoothService", "OnDataReceived - 온도: " + temper.getValue() + " | 습도: " + humi.getValue());
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

    private void settingReceiver() {

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(AVKDConstents.BLE_RECIVE_DATA.equals(intent.getAction())) {
                    updateHtAndTpData();
                    sendNotification();
                }
            }
        };
    }

    private void openMenuActivity() {

        Intent intent = new Intent(this, MenuActivity.class);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out);
        startActivity(intent, options.toBundle());
    }

    private void sendNotification() {

        int NOTIFICATION_ID = 234;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {


            String CHANNEL_ID = "my_channel_01";
            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);

            builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        } else {

            builder = new NotificationCompat.Builder(this);
        }


        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Humi")
                .setContentText("현재 모든 방에 환기가 필요합니다. ‘강’모드로 작동해주세요. ");
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MenuActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MenuActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        builder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(0, builder.build());
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

    private void updateHtAndTpData() {
        List<Record> records = AVKDData.getGraph(getApplicationContext(), Date.TODAY, AVKDConstents.HUMIDT_DATA_CODE);
        int ht = (int) (records.size() > 0? records.get(records.size()-1).getValue() : 0.0f);
        records = AVKDData.getGraph(getApplicationContext(), Date.TODAY, AVKDConstents.TEMPR_DATA_CODE);
        int tp = (int) (records.size() > 0? records.get(records.size()-1).getValue() : 0.0f);

        tv_ht.setText(ht+"");
        tv_tp.setText(tp+"");
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