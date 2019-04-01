package com.avkd.humible;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.avkd.humible.util.ToastUtil;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String SELECTED_ITEM = "arg_selected_item";

    private BottomNavigationView mBottomNav;
    private int mSelectedItem;

    BluetoothSPP bt;

    private TextView tv_btn_menu;
    private TextView tv_btn_back;
    private TextView tv_title;
    private Switch sw_ble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if(!AVKDConstents.IS_AMULATOR) {
            bt = new BluetoothSPP(this);
        }

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View customView = getSupportActionBar().getCustomView();

        Toolbar parent =(Toolbar) customView.getParent();
        parent.setContentInsetsAbsolute(0,0);

        tv_btn_menu = customView.findViewById(R.id.tv_btn_menu);
        tv_btn_back = customView.findViewById(R.id.tv_btn_back);
        tv_title = customView.findViewById(R.id.tv_title);
        sw_ble = customView.findViewById(R.id.sw_ble);

        tv_btn_menu.setVisibility(View.GONE);
        tv_btn_back.setVisibility(View.VISIBLE);

        tv_btn_back.setOnClickListener(this);

        mBottomNav = findViewById(R.id.navigation);
        mBottomNav.setOnNavigationItemSelectedListener(item -> {
            selectFragment(item);
            return true;
        });

        MenuItem selectedItem;
        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            selectedItem = mBottomNav.getMenu().findItem(mSelectedItem);
        } else {
            selectedItem = mBottomNav.getMenu().getItem(0);
        }
        selectFragment(selectedItem);

        if(!AVKDConstents.IS_AMULATOR) {
            setupBleListener();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        MenuItem homeItem = mBottomNav.getMenu().getItem(0);
        if (mSelectedItem != homeItem.getItemId()) {
            // select home item
            selectFragment(homeItem);
        } else {
            super.onBackPressed();
        }
    }

    private void selectFragment(MenuItem item) {
        Fragment frag = null;
        // init corresponding fragment
        switch (item.getItemId()) {
            case R.id.menu_home:
                frag = HomeFragment.newInstance();
                break;
            case R.id.menu_notifications:
                frag = NotificationsFragment.newInstance();
                break;
            case R.id.menu_graph:
                frag = GraphFragment.newInstance();
                break;
            case R.id.menu_settings:
                frag = SettingsFragment.newInstance();
                break;
        }

        // update selected item
        mSelectedItem = item.getItemId();

        // uncheck the other items.
        for (int i = 0; i< mBottomNav.getMenu().size(); i++) {
            MenuItem menuItem = mBottomNav.getMenu().getItem(i);
            if (menuItem.getItemId() == item.getItemId()) {
                menuItem.setChecked(true);
            }
        }

        updateToolbar(item);

        if (frag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.container, frag, frag.getTag());
            ft.commit();
        }
    }

    private void updateToolbar(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
            case R.id.menu_notifications:
            case R.id.menu_graph:
                tv_title.setText("공부방 1");
                sw_ble.setVisibility(View.VISIBLE);
                break;
            case R.id.menu_settings:
                tv_title.setText("설정");
                sw_ble.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_btn_back:
                finish();
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
