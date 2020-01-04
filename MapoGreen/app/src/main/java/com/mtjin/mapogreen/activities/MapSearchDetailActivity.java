package com.mtjin.mapogreen.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.mtjin.mapogreen.R;
import com.mtjin.mapogreen.model.Document;
import com.mtjin.mapogreen.utils.IntentKey;

import java.util.ArrayList;

public class MapSearchDetailActivity extends AppCompatActivity {
    final static String TAG = "MapSearchDetailTAG";

    //xml
    RadarChart radarChart;

    //value
    ArrayList<Document> bigMartList = new ArrayList<>(); //대형마트 MT1
    ArrayList<Document> gs24List = new ArrayList<>(); //편의점 CS2
    ArrayList<Document> schoolList = new ArrayList<>(); //학교 SC4
    ArrayList<Document> academyList = new ArrayList<>(); //학원 AC5
    ArrayList<Document> subwayList = new ArrayList<>(); //지하철 SW8
    ArrayList<Document> bankList = new ArrayList<>(); //은행 BK9
    ArrayList<Document> hospitalList = new ArrayList<>(); //병원 HP8
    ArrayList<Document> pharmacyList = new ArrayList<>(); //약국 PM9
    ArrayList<Document> cafeList = new ArrayList<>(); //카페

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search_detail);
        radarChart = findViewById(R.id.mapsearchdetail_radar_chart);
        processIntent();
        makeChart();
    }

    //인텐트처리
    private void processIntent(){
        Intent getIntent = getIntent();
        bigMartList = getIntent.getParcelableArrayListExtra(IntentKey.CATEGOTY_SEARCH_MODEL_EXTRA1);
        gs24List = getIntent.getParcelableArrayListExtra(IntentKey.CATEGOTY_SEARCH_MODEL_EXTRA2);
        schoolList = getIntent.getParcelableArrayListExtra(IntentKey.CATEGOTY_SEARCH_MODEL_EXTRA3);
        academyList = getIntent.getParcelableArrayListExtra(IntentKey.CATEGOTY_SEARCH_MODEL_EXTRA4);
        subwayList = getIntent.getParcelableArrayListExtra(IntentKey.CATEGOTY_SEARCH_MODEL_EXTRA5);
        bankList = getIntent.getParcelableArrayListExtra(IntentKey.CATEGOTY_SEARCH_MODEL_EXTRA6);
        hospitalList = getIntent.getParcelableArrayListExtra(IntentKey.CATEGOTY_SEARCH_MODEL_EXTRA7);
        pharmacyList = getIntent.getParcelableArrayListExtra(IntentKey.CATEGOTY_SEARCH_MODEL_EXTRA8);
        cafeList = getIntent.getParcelableArrayListExtra(IntentKey.CATEGOTY_SEARCH_MODEL_EXTRA9);
    }

    //차트생성
    private void makeChart(){
        RadarDataSet dataSet = new RadarDataSet(dataValue(), "DATA");
        dataSet.setColor(Color.BLUE);

        RadarData data = new RadarData();
        data.addDataSet(dataSet);
        String[] labels =  {"대형마트", "편의점", "학교", "학원", "지하철", "은행", "병원", "약국", "카페"};
        XAxis xAxis = radarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        radarChart.setData(data);
    }

    //차트 데이터 생성
    private ArrayList<RadarEntry> dataValue(){
        ArrayList<RadarEntry> dataVals = new ArrayList<>();
        dataVals.add(new RadarEntry(bigMartList.size()));
        dataVals.add(new RadarEntry(gs24List.size()));
        dataVals.add(new RadarEntry(schoolList.size()));
        dataVals.add(new RadarEntry(academyList.size()));
        dataVals.add(new RadarEntry(subwayList.size()));
        dataVals.add(new RadarEntry(bankList.size()));
        dataVals.add(new RadarEntry(hospitalList.size()));
        dataVals.add(new RadarEntry(pharmacyList.size()));
        dataVals.add(new RadarEntry(cafeList.size()));
        return  dataVals;
    }

}
