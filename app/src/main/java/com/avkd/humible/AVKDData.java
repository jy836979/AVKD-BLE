package com.avkd.humible;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

enum Date {

    YESTERDAY(-1), TODAY(0), TOMORROW(1);

    private int amount;

    Date(int amount) { this.amount = amount; }

    public String key(String prefix) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, this.amount);
        return prefix+sdf.format(c.getTime());
    }
}


public class AVKDData {

    private static final String TAG = "AVKDData";
    public static final String PREFERENCE_NAME = "AVKD_DATA";


    public static void makeDumpData(Context context) {

        // 습도
        JSONArray arrHT = new JSONArray();
        arrHT.put(new Record(AVKDConstents.XAXIS_01, 48));
        arrHT.put(new Record(AVKDConstents.XAXIS_02, 48));
        arrHT.put(new Record(AVKDConstents.XAXIS_03, 40));
        arrHT.put(new Record(AVKDConstents.XAXIS_04, 40));
        arrHT.put(new Record(AVKDConstents.XAXIS_05, 34));
        arrHT.put(new Record(AVKDConstents.XAXIS_06, 38));
        arrHT.put(new Record(AVKDConstents.XAXIS_07, 38));
        arrHT.put(new Record(AVKDConstents.XAXIS_08, 45));

        // 공기질
        JSONArray arrAR = new JSONArray();
        arrAR.put(new Record(AVKDConstents.XAXIS_01, 48));
        arrAR.put(new Record(AVKDConstents.XAXIS_02, 48));
        arrAR.put(new Record(AVKDConstents.XAXIS_03, 40));
        arrAR.put(new Record(AVKDConstents.XAXIS_04, 40));
        arrAR.put(new Record(AVKDConstents.XAXIS_05, 34));
        arrAR.put(new Record(AVKDConstents.XAXIS_06, 38));
        arrAR.put(new Record(AVKDConstents.XAXIS_07, 38));
        arrAR.put(new Record(AVKDConstents.XAXIS_08, 45));

        // 온도
        JSONArray arrTP = new JSONArray();
        arrTP.put(new Record(AVKDConstents.XAXIS_01, 48));
        arrTP.put(new Record(AVKDConstents.XAXIS_02, 48));
        arrTP.put(new Record(AVKDConstents.XAXIS_03, 40));
        arrTP.put(new Record(AVKDConstents.XAXIS_04, 40));
        arrTP.put(new Record(AVKDConstents.XAXIS_05, 34));
        arrTP.put(new Record(AVKDConstents.XAXIS_06, 38));
        arrTP.put(new Record(AVKDConstents.XAXIS_07, 38));
        arrTP.put(new Record(AVKDConstents.XAXIS_08, 45));

        // 가습량
        JSONArray arrHF = new JSONArray();
        arrHF.put(new Record(AVKDConstents.XAXIS_01, 48));
        arrHF.put(new Record(AVKDConstents.XAXIS_02, 48));
        arrHF.put(new Record(AVKDConstents.XAXIS_03, 40));
        arrHF.put(new Record(AVKDConstents.XAXIS_04, 40));
        arrHF.put(new Record(AVKDConstents.XAXIS_05, 34));
        arrHF.put(new Record(AVKDConstents.XAXIS_06, 38));
        arrHF.put(new Record(AVKDConstents.XAXIS_07, 38));
        arrHF.put(new Record(AVKDConstents.XAXIS_08, 45));

//        setGraph(context, Date.TODAY, AVKDConstents.HUMIDT_DATA_CODE, arrHT);
        setGraph(context, Date.TODAY, AVKDConstents.AIR_DATA_CODE, arrAR);
//        setGraph(context, Date.TODAY, AVKDConstents.TEMPR_DATA_CODE, arrTP);
        setGraph(context, Date.TODAY, AVKDConstents.HUMIDF_DATA_CODE, arrHF);

        //        setGraph(context, Date.YESTERDAY, AVKDConstents.HUMIDT_DATA_CODE, arrHT);
        setGraph(context, Date.YESTERDAY, AVKDConstents.AIR_DATA_CODE, arrAR);
//        setGraph(context, Date.YESTERDAY, AVKDConstents.TEMPR_DATA_CODE, arrTP);
        setGraph(context, Date.YESTERDAY, AVKDConstents.HUMIDF_DATA_CODE, arrHF);
    }

    public static List<Record> getGraph(Context context, Date date, String type) {

        SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);

        String jsonStrData = pref.getString(date.key(type), "");

        JSONArray records = new JSONArray();
        if (!jsonStrData.isEmpty()) {
            try {
                records = new JSONArray(jsonStrData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return jsonArrToRecordGroupArr(records);
    }

    public static List<Record> getGraphOrg(Context context, Date date, String type) {

        SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);

        String jsonStrData = pref.getString(date.key(type), "");

        JSONArray records = new JSONArray();
        if (!jsonStrData.isEmpty()) {
            try {
                records = new JSONArray(jsonStrData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return jsonArrToRecordArr(records);
    }

    public static void setRecord(Context context, Date date, String type, Record record) {

        SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        List<Record> arr = getGraphOrg(context, date, type);
        arr.add(record);
        editor.putString(date.key(type), arr.toString());
        editor.commit();
    }

    public static void setGraph(Context context, Date date, String type, JSONArray arr) {

        SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(date.key(type), arr.toString());
        editor.commit();
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static List<Record> jsonArrToRecordGroupArr(JSONArray arr) {

        List<Record> recordArr = jsonArrToRecordArr(arr);

        List<Record> lastRecordArr =
                recordArr.stream().collect(
                        groupingBy(
                                Record::getTime,
                                Collectors.collectingAndThen(
                                        Collectors.toList(),
                                        values -> values.get(values.size()-1)
                                ))).values().stream().collect(Collectors.toList());

        return lastRecordArr;
    }

    private static List<Record> jsonArrToRecordArr(JSONArray arr) {

        List<Record> recordArr = new ArrayList<>();

        for(int i=0; i < arr.length(); i++) {
            try {
                Gson gson = new Gson();
                Record record = gson.fromJson((String) arr.get(i), Record.class);
                recordArr.add(record);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return recordArr;
    }


}
