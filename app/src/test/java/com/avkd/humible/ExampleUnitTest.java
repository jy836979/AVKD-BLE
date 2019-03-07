package com.avkd.humible;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {


    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void make_json() throws JSONException {

        // 습도
        JSONArray arrHT = new JSONArray();
        arrHT.put(new Record(AVKDConstents.XAXIS_01, 48));
        arrHT.put(new Record(AVKDConstents.XAXIS_01, 47));
        arrHT.put(new Record(AVKDConstents.XAXIS_01, 49));
        arrHT.put(new Record(AVKDConstents.XAXIS_01, 48));

        arrHT.put(new Record(AVKDConstents.XAXIS_02, 48));
        arrHT.put(new Record(AVKDConstents.XAXIS_02, 47));
        arrHT.put(new Record(AVKDConstents.XAXIS_02, 49));
        arrHT.put(new Record(AVKDConstents.XAXIS_02, 48));

        arrHT.put(new Record(AVKDConstents.XAXIS_03, 40));
        arrHT.put(new Record(AVKDConstents.XAXIS_03, 41));
        arrHT.put(new Record(AVKDConstents.XAXIS_03, 42));
        arrHT.put(new Record(AVKDConstents.XAXIS_03, 40));

        arrHT.put(new Record(AVKDConstents.XAXIS_04, 40));
        arrHT.put(new Record(AVKDConstents.XAXIS_04, 41));
        arrHT.put(new Record(AVKDConstents.XAXIS_04, 42));
        arrHT.put(new Record(AVKDConstents.XAXIS_04, 40));

        arrHT.put(new Record(AVKDConstents.XAXIS_05, 40));
        arrHT.put(new Record(AVKDConstents.XAXIS_05, 41));
        arrHT.put(new Record(AVKDConstents.XAXIS_05, 42));
        arrHT.put(new Record(AVKDConstents.XAXIS_05, 34));

        arrHT.put(new Record(AVKDConstents.XAXIS_06, 40));
        arrHT.put(new Record(AVKDConstents.XAXIS_06, 41));
        arrHT.put(new Record(AVKDConstents.XAXIS_06, 42));
        arrHT.put(new Record(AVKDConstents.XAXIS_06, 38));

        arrHT.put(new Record(AVKDConstents.XAXIS_07, 40));
        arrHT.put(new Record(AVKDConstents.XAXIS_07, 41));
        arrHT.put(new Record(AVKDConstents.XAXIS_07, 42));
        arrHT.put(new Record(AVKDConstents.XAXIS_07, 38));

        arrHT.put(new Record(AVKDConstents.XAXIS_08, 40));
        arrHT.put(new Record(AVKDConstents.XAXIS_08, 41));
        arrHT.put(new Record(AVKDConstents.XAXIS_08, 42));
        arrHT.put(new Record(AVKDConstents.XAXIS_08, 45));

        // 공기질
        JSONArray arrAR = new JSONArray();
        arrAR.put(new Record(AVKDConstents.XAXIS_01, 48));
        arrAR.put(new Record(AVKDConstents.XAXIS_01, 47));
        arrAR.put(new Record(AVKDConstents.XAXIS_01, 49));
        arrAR.put(new Record(AVKDConstents.XAXIS_01, 48));

        arrAR.put(new Record(AVKDConstents.XAXIS_02, 48));
        arrAR.put(new Record(AVKDConstents.XAXIS_02, 47));
        arrAR.put(new Record(AVKDConstents.XAXIS_02, 49));
        arrAR.put(new Record(AVKDConstents.XAXIS_02, 48));

        arrAR.put(new Record(AVKDConstents.XAXIS_03, 40));
        arrAR.put(new Record(AVKDConstents.XAXIS_03, 41));
        arrAR.put(new Record(AVKDConstents.XAXIS_03, 42));
        arrAR.put(new Record(AVKDConstents.XAXIS_03, 40));

        arrAR.put(new Record(AVKDConstents.XAXIS_04, 40));
        arrAR.put(new Record(AVKDConstents.XAXIS_04, 41));
        arrAR.put(new Record(AVKDConstents.XAXIS_04, 42));
        arrAR.put(new Record(AVKDConstents.XAXIS_04, 40));

        arrAR.put(new Record(AVKDConstents.XAXIS_05, 40));
        arrAR.put(new Record(AVKDConstents.XAXIS_05, 41));
        arrAR.put(new Record(AVKDConstents.XAXIS_05, 42));
        arrAR.put(new Record(AVKDConstents.XAXIS_05, 34));

        arrAR.put(new Record(AVKDConstents.XAXIS_06, 40));
        arrAR.put(new Record(AVKDConstents.XAXIS_06, 41));
        arrAR.put(new Record(AVKDConstents.XAXIS_06, 42));
        arrAR.put(new Record(AVKDConstents.XAXIS_06, 38));

        arrAR.put(new Record(AVKDConstents.XAXIS_07, 40));
        arrAR.put(new Record(AVKDConstents.XAXIS_07, 41));
        arrAR.put(new Record(AVKDConstents.XAXIS_07, 42));
        arrAR.put(new Record(AVKDConstents.XAXIS_07, 38));

        arrAR.put(new Record(AVKDConstents.XAXIS_08, 40));
        arrAR.put(new Record(AVKDConstents.XAXIS_08, 41));
        arrAR.put(new Record(AVKDConstents.XAXIS_08, 42));
        arrAR.put(new Record(AVKDConstents.XAXIS_08, 45));

        // 온도
        JSONArray arrTP = new JSONArray();
        arrTP.put(new Record(AVKDConstents.XAXIS_01, 48));
        arrTP.put(new Record(AVKDConstents.XAXIS_01, 47));
        arrTP.put(new Record(AVKDConstents.XAXIS_01, 49));
        arrTP.put(new Record(AVKDConstents.XAXIS_01, 48));

        arrTP.put(new Record(AVKDConstents.XAXIS_02, 48));
        arrTP.put(new Record(AVKDConstents.XAXIS_02, 47));
        arrTP.put(new Record(AVKDConstents.XAXIS_02, 49));
        arrTP.put(new Record(AVKDConstents.XAXIS_02, 48));

        arrTP.put(new Record(AVKDConstents.XAXIS_03, 40));
        arrTP.put(new Record(AVKDConstents.XAXIS_03, 41));
        arrTP.put(new Record(AVKDConstents.XAXIS_03, 42));
        arrTP.put(new Record(AVKDConstents.XAXIS_03, 40));

        arrTP.put(new Record(AVKDConstents.XAXIS_04, 40));
        arrTP.put(new Record(AVKDConstents.XAXIS_04, 41));
        arrTP.put(new Record(AVKDConstents.XAXIS_04, 42));
        arrTP.put(new Record(AVKDConstents.XAXIS_04, 40));

        arrTP.put(new Record(AVKDConstents.XAXIS_05, 40));
        arrTP.put(new Record(AVKDConstents.XAXIS_05, 41));
        arrTP.put(new Record(AVKDConstents.XAXIS_05, 42));
        arrTP.put(new Record(AVKDConstents.XAXIS_05, 34));

        arrTP.put(new Record(AVKDConstents.XAXIS_06, 40));
        arrTP.put(new Record(AVKDConstents.XAXIS_06, 41));
        arrTP.put(new Record(AVKDConstents.XAXIS_06, 42));
        arrTP.put(new Record(AVKDConstents.XAXIS_06, 38));

        arrTP.put(new Record(AVKDConstents.XAXIS_07, 40));
        arrTP.put(new Record(AVKDConstents.XAXIS_07, 41));
        arrTP.put(new Record(AVKDConstents.XAXIS_07, 42));
        arrTP.put(new Record(AVKDConstents.XAXIS_07, 38));

        arrTP.put(new Record(AVKDConstents.XAXIS_08, 40));
        arrTP.put(new Record(AVKDConstents.XAXIS_08, 41));
        arrTP.put(new Record(AVKDConstents.XAXIS_08, 42));
        arrTP.put(new Record(AVKDConstents.XAXIS_08, 45));

        // 가습량
        JSONArray arrHF = new JSONArray();
        arrHF.put(new Record(AVKDConstents.XAXIS_01, 48));
        arrHF.put(new Record(AVKDConstents.XAXIS_01, 47));
        arrHF.put(new Record(AVKDConstents.XAXIS_01, 49));
        arrHF.put(new Record(AVKDConstents.XAXIS_01, 48));

        arrHF.put(new Record(AVKDConstents.XAXIS_02, 48));
        arrHF.put(new Record(AVKDConstents.XAXIS_02, 47));
        arrHF.put(new Record(AVKDConstents.XAXIS_02, 49));
        arrHF.put(new Record(AVKDConstents.XAXIS_02, 48));

        arrHF.put(new Record(AVKDConstents.XAXIS_03, 40));
        arrHF.put(new Record(AVKDConstents.XAXIS_03, 41));
        arrHF.put(new Record(AVKDConstents.XAXIS_03, 42));
        arrHF.put(new Record(AVKDConstents.XAXIS_03, 40));

        arrHF.put(new Record(AVKDConstents.XAXIS_04, 40));
        arrHF.put(new Record(AVKDConstents.XAXIS_04, 41));
        arrHF.put(new Record(AVKDConstents.XAXIS_04, 42));
        arrHF.put(new Record(AVKDConstents.XAXIS_04, 40));

        arrHF.put(new Record(AVKDConstents.XAXIS_05, 40));
        arrHF.put(new Record(AVKDConstents.XAXIS_05, 41));
        arrHF.put(new Record(AVKDConstents.XAXIS_05, 42));
        arrHF.put(new Record(AVKDConstents.XAXIS_05, 34));

        arrHF.put(new Record(AVKDConstents.XAXIS_06, 40));
        arrHF.put(new Record(AVKDConstents.XAXIS_06, 41));
        arrHF.put(new Record(AVKDConstents.XAXIS_06, 42));
        arrHF.put(new Record(AVKDConstents.XAXIS_06, 38));

        arrHF.put(new Record(AVKDConstents.XAXIS_07, 40));
        arrHF.put(new Record(AVKDConstents.XAXIS_07, 41));
        arrHF.put(new Record(AVKDConstents.XAXIS_07, 42));
        arrHF.put(new Record(AVKDConstents.XAXIS_07, 38));

        arrHF.put(new Record(AVKDConstents.XAXIS_08, 40));
        arrHF.put(new Record(AVKDConstents.XAXIS_08, 41));
        arrHF.put(new Record(AVKDConstents.XAXIS_08, 42));
        arrHF.put(new Record(AVKDConstents.XAXIS_08, 45));

//        Record record = (Record) arrHT.get(0);
//        System.out.println("time: "+record.getTime()+", value: "+record.getValue() );


        ArrayList<Record> recordArr = new ArrayList<>();

        for(int i=0; i < arrHF.length(); i++) {
            try {
                Record record = (Record) arrHF.get(i);
                recordArr.add(record);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        List<Record> list =
                recordArr.stream().collect(
                        groupingBy(
                                Record::getTime,
                                Collectors.collectingAndThen(
                                        Collectors.toList(),
                                        values -> values.get(values.size()-1)
                                ))).values().stream().collect(Collectors.toList());


        System.out.println(list.size());




        System.out.println(DateType.TOMORROW.key());

        String jsonStrData = arrHF.toString();

        JSONArray records = new JSONArray();
        if (!jsonStrData.isEmpty()) {
            try {
                records = new JSONArray(jsonStrData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        List<Record> list1 = jsonArrToRecordArr(records);

        System.out.println(list1.toString());



    }

    @TargetApi(Build.VERSION_CODES.N)
    private static List<Record> jsonArrToRecordArr(JSONArray arr) {

        ArrayList<Record> recordArr = new ArrayList<>();

        for(int i=0; i < arr.length(); i++) {
            try {
                Gson gson = new Gson();
                Record record = gson.fromJson((String) arr.get(i), Record.class);
                recordArr.add(record);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

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

    public enum DateType {

        YESTERDAY(-1), TODAY(0), TOMORROW(1);

        private int amount;

        DateType(int amount) { this.amount = amount; }

        public String key() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, this.amount);
            return sdf.format(c.getTime());
        }
    }
}