package com.avkd.humible;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BleReciveDemo extends Handler {

    public static final int TIMER_1 = 0;
    public static final int TIMER_2 = 1;
    public int count = 0;

    public Context mContext;

    public BleReciveDemo(Context context) {
        mContext = context;
    }

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what)
        {
            case TIMER_1:
                count++;
                int time = (int)Math.ceil((double)count/5);
                if (time <= 24) {
                    Record temper = AVKDData.demoData(time);    // 온도
                    Record humi = AVKDData.demoData(time);      // 습도

                    AVKDData.setRecord(mContext, Date.TODAY, AVKDConstents.TEMPR_DATA_CODE, temper);
                    AVKDData.setRecord(mContext, Date.TODAY, AVKDConstents.HUMIDT_DATA_CODE, humi);

                    Intent intent = new Intent(AVKDConstents.BLE_RECIVE_DATA);
                    mContext.sendBroadcast(intent);

                    sendEmptyMessageDelayed(TIMER_1, 5000);
                } else {
                    removeMessages(TIMER_1);
                }
                break;
            case TIMER_2:

                removeMessages(TIMER_1);
                break;
        }
    }
}
