package com.avkd.humible;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_add_device;
    ImageButton btn_humi_on;
    ImageButton btn_roomi_on;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        btn_add_device.setOnClickListener(this);
        btn_humi_on.setOnClickListener(this);
        btn_roomi_on.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_device:

                break;
            case R.id.btn_humi_on:
                openMenuActivity();
                break;
            case R.id.btn_roomi_on:
                openMenuActivity();
                break;
        }
    }

    private void openMenuActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out);
        startActivity(intent, options.toBundle());
    }
}