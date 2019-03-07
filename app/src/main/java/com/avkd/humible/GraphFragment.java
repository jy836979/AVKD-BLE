package com.avkd.humible;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GraphFragment extends Fragment implements View.OnClickListener {

    private static String TAG = "GraphFragment";

    LineChart lineChart;
    LinearLayout ll_btn01;
    LinearLayout ll_btn02;
    LinearLayout ll_btn03;
    LinearLayout ll_btn04;
    View[] views;

    TextView tv_avg;
    TextView tv_prev;
    TextView tv_today;
    TextView tv_next;
    TextView tv_date;

    Date dataDate = Date.TODAY;
    String dataCode = AVKDConstents.HUMIDT_DATA_CODE;

    public static Fragment newInstance() {
        Fragment frag = new GraphFragment();
        Bundle args = new Bundle();

        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        lineChart = view.findViewById(R.id.line_chart);

        ll_btn01 = view.findViewById(R.id.ll_btn01);
        ll_btn02 = view.findViewById(R.id.ll_btn02);
        ll_btn03 = view.findViewById(R.id.ll_btn03);
        ll_btn04 = view.findViewById(R.id.ll_btn04);
        views = new View[]{ ll_btn01, ll_btn02, ll_btn03, ll_btn04 };

        ll_btn01.setOnClickListener(this);
        ll_btn02.setOnClickListener(this);
        ll_btn03.setOnClickListener(this);
        ll_btn04.setOnClickListener(this);

        tv_avg = view.findViewById(R.id.tv_avg);
        tv_prev = view.findViewById(R.id.tv_prev);
        tv_today = view.findViewById(R.id.tv_today);
        tv_next = view.findViewById(R.id.tv_next);
        tv_date = view.findViewById(R.id.tv_date);

        tv_prev.setOnClickListener(this);
        tv_today.setOnClickListener(this);
        tv_next.setOnClickListener(this);

        updateData(AVKDConstents.HUMIDT_DATA_CODE);
        return view;
    }

    private void setChart(List<Record> records) {

        lineChart.invalidate(); //차트 초기화 작업
        lineChart.clear();

        ArrayList<Entry> values = new ArrayList<>();//차트 데이터 셋에 담겨질 데이터

        for (int i=0; i < records.size(); i++) {//values에 데이터를 담는 과정
            Record record = records.get(i);
            float weight = record.getValue();
            values.add(new Entry(i+1, weight));
        }

        LineDataSet lineDataSet = new LineDataSet(values, "시간 대 별 습도"); //LineDataSet 선언
        lineDataSet.setColor(ContextCompat.getColor(getContext(), R.color.dark_gray)); //LineChart에서 Line Color 설정
        lineDataSet.setCircleColor(ContextCompat.getColor(getContext(), R.color.light_blue)); // LineChart에서 Line Circle Color 설정
        lineDataSet.setCircleHoleColor(ContextCompat.getColor(getContext(), R.color.white)); // LineChart에서 Line Hole Circle Color 설정
        lineDataSet.setCircleHoleRadius(3);
        lineDataSet.setCircleRadius(5);

        LineData lineData = new LineData(); //LineDataSet을 담는 그릇 여러개의 라인 데이터가 들어갈 수 있습니다.
        lineData.addDataSet(lineDataSet);

        lineData.setValueTextColor(ContextCompat.getColor(getContext(), R.color.textColor)); //라인 데이터의 텍스트 컬러 설정
        lineData.setValueTextSize(9);

        XAxis xAxis = lineChart.getXAxis(); // x 축 설정
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x 축 표시에 대한 위치 설정
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setEnabled(false);
//        xAxis.setValueFormatter(new ChartXValueFormatter()); //X축의 데이터를 제 가공함. new ChartXValueFormatter은 Custom한 소스
        xAxis.setLabelCount(8, true); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정 5개 force가 true 이면 반드시 보여줌
        xAxis.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor)); // X축 텍스트컬러설정
        xAxis.setGridColor(ContextCompat.getColor(getContext(), R.color.textColor)); // X축 줄의 컬러 설정

        YAxis yAxisLeft = lineChart.getAxisLeft(); //Y축의 왼쪽면 설정
        yAxisLeft.setDrawLabels(false);
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setAxisMaximum(100);
        yAxisLeft.setAxisMinimum(0);
        yAxisLeft.setLabelCount(5, true);
        yAxisLeft.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor)); //Y축 텍스트 컬러 설정
        yAxisLeft.setGridColor(ContextCompat.getColor(getContext(), R.color.textColor)); // Y축 줄의 컬러 설정

        YAxis yAxisRight = lineChart.getAxisRight(); //Y축의 오른쪽면 설정
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);
        //y축의 활성화를 제거함

//        lineChart.setVisibleXRangeMinimum(60 * 60 * 24 * 1000 * 5); //라인차트에서 최대로 보여질 X축의 데이터 설정
        lineChart.setDescription(null); //차트에서 Description 설정 저는 따로 안했습니다.
//        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setScaleEnabled(false);

        Legend legend = lineChart.getLegend(); //레전드 설정 (차트 밑에 색과 라벨을 나타내는 설정)
        legend.setEnabled(false);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);//하단 왼쪽에 설정
        legend.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor)); // 레전드 컬러 설정

        lineChart.setData(lineData);
        float padding = 0.2f;
        xAxis.setAxisMaximum(lineChart.getLineData().getXMax() + padding);
        xAxis.setAxisMinimum(lineChart.getLineData().getXMin() - padding);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_btn01:
                circleActive(v);
                updateData(AVKDConstents.HUMIDT_DATA_CODE);
                break;
            case R.id.ll_btn02:
                circleActive(v);
                updateData(AVKDConstents.AIR_DATA_CODE);
                break;
            case R.id.ll_btn03:
                circleActive(v);
                updateData(AVKDConstents.TEMPR_DATA_CODE);
                break;
            case R.id.ll_btn04:
                circleActive(v);
                updateData(AVKDConstents.HUMIDF_DATA_CODE);
                break;
            case R.id.tv_prev:
                dataDate = Date.YESTERDAY;
                tv_prev.setVisibility(View.GONE);
                tv_today.setVisibility(View.VISIBLE);
                tv_next.setVisibility(View.GONE);
                tv_date.setText("어제");
                circleActive(ll_btn01);
                updateData(AVKDConstents.HUMIDT_DATA_CODE);
                break;
            case R.id.tv_today:
                dataDate = Date.TODAY;
                tv_prev.setVisibility(View.VISIBLE);
                tv_today.setVisibility(View.GONE);
                tv_next.setVisibility(View.VISIBLE);
                tv_date.setText("오늘");
                circleActive(ll_btn01);
                updateData(AVKDConstents.HUMIDT_DATA_CODE);
                break;
            case R.id.tv_next:
                break;

        }

    }

    private void circleActive(View v) {
        ImageView iv;
        for(View ll : views) {
            iv = ll.findViewWithTag("active");
            iv.setImageResource(R.drawable.gray_circle);
        }
        iv = v.findViewWithTag("active");
        iv.setImageResource(R.drawable.blue_circle);

        iv = v.findViewWithTag("active");
    }

    private void updateData(String dataCode) {

        Log.d(TAG, "updateData:: dataCode - " + dataCode);
        this.dataCode = dataCode;

        int XAXIS_CNT = 8;
        List<Record> records = AVKDData.getGraph(getContext(), dataDate, dataCode);

        if (records.size() < XAXIS_CNT) {
            int max = XAXIS_CNT-records.size();
            for(int i=0; i < max; i++) {
                records.add(new Record(0, 0.0f));
            }
        }

        List<Record> graphArr = records.subList(records.size()-XAXIS_CNT, records.size());
        Double sum = graphArr.stream().collect(Collectors.summingDouble(o -> o.getValue()));
        Double average = sum/XAXIS_CNT;


        setChart(graphArr);
//        tv_avg.setText(average.intValue()+"%");
    }
}
