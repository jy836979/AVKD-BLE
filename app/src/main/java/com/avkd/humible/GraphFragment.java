package com.avkd.humible;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class GraphFragment extends Fragment {

    LineChart lineChart;

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

        setChart(getDumpData());
        return view;
    }

    private List<Record> getDumpData() {

        List<Record> records = new ArrayList<>();
        records.add(new Record(1, 48));
        records.add(new Record(2, 48));
        records.add(new Record(3, 40));
        records.add(new Record(4, 40));
        records.add(new Record(5, 34));
        records.add(new Record(6, 38));
        records.add(new Record(7, 38));
        records.add(new Record(8, 45));

        return records;
    }

    private void setChart(List<Record> records) {

        lineChart.invalidate(); //차트 초기화 작업
        lineChart.clear();

        ArrayList<Entry> values = new ArrayList<>();//차트 데이터 셋에 담겨질 데이터

        for (Record record : records) { //values에 데이터를 담는 과정
            long time = record.getTime();
            float weight = record.getValue();
            values.add(new Entry(time, weight));
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
}
