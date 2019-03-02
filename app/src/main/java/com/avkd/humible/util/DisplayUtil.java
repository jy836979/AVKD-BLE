package com.avkd.humible.util;

import android.content.Context;

public class DisplayUtil {

    /**
     * 크기가 동일하게 유지되도록 px 값을 dip 또는 dp 값으로 변환합니다.
     */
    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * dip 또는 dp 값을 px 값으로 변환하여 동일한 크기를 확보하십시오.
     */
    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 텍스트 크기가 변하지 않도록 px 값을 sp 값으로 변환하십시오.
     */
    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 텍스트 크기가 변하지 않도록 px 값을 sp 값으로 변환하십시오.
     */
    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
