package com.mtjin.mapogreen.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mtjin.mapogreen.R;
import com.mtjin.mapogreen.adapter.LocationAdapter;
import com.mtjin.mapogreen.api.ApiClient;
import com.mtjin.mapogreen.api.ApiInterface;
import com.mtjin.mapogreen.model.category_search.Document;
import com.mtjin.mapogreen.model.category_search.CategoryResult;
import com.mtjin.mapogreen.utils.BusProvider;
import com.mtjin.mapogreen.utils.IntentKey;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;


import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapActivity extends AppCompatActivity implements MapView.MapViewEventListener, MapView.POIItemEventListener, MapView.OpenAPIKeyAuthenticationResultListener, View.OnClickListener, MapView.CurrentLocationEventListener {
    final static String TAG = "MapTAG";
    //xml
    MapView mMapView;
    ViewGroup mMapViewContainer;
    EditText mSearchEdit;
    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2, fab3, searchDetailFab, stopTrackingFab;
    RelativeLayout mLoaderLayout;
    RecyclerView recyclerView;

    //value
    MapPoint currentMapPoint;
    private double mCurrentLng; //Long = X, Lat = Yㅌ
    private double mCurrentLat;
    private double mSearchLng = -1;
    private double mSearchLat = -1;
    private String mSearchName;
    boolean isTrackingMode = false; //트래킹 모드인지 (3번째 버튼 현재위치 추적 눌렀을 경우 true되고 stop 버튼 누르면 false로 된다)
    Bus bus = BusProvider.getInstance();

    ArrayList<Document> bigMartList = new ArrayList<>(); //대형마트 MT1
    ArrayList<Document> gs24List = new ArrayList<>(); //편의점 CS2
    ArrayList<Document> schoolList = new ArrayList<>(); //학교 SC4
    ArrayList<Document> academyList = new ArrayList<>(); //학원 AC5
    ArrayList<Document> subwayList = new ArrayList<>(); //지하철 SW8
    ArrayList<Document> bankList = new ArrayList<>(); //은행 BK9
    ArrayList<Document> hospitalList = new ArrayList<>(); //병원 HP8
    ArrayList<Document> pharmacyList = new ArrayList<>(); //약국 PM9
    ArrayList<Document> cafeList = new ArrayList<>(); //카페

    ArrayList<Document> documentArrayList = new ArrayList<>(); //지역명 검색 결과 리스트

    MapPOIItem searchMarker = new MapPOIItem();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_map);
        bus.register(this); //정류소 등록
        initView();
    }


    private void initView() {
        //binding
        mSearchEdit = findViewById(R.id.map_et_search);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        fab3 = findViewById(R.id.fab3);
        searchDetailFab = findViewById(R.id.fab_detail);
        stopTrackingFab = findViewById(R.id.fab_stop_tracking);
        mLoaderLayout = findViewById(R.id.loaderLayout);
        mMapView = new MapView(this);
        mMapViewContainer = findViewById(R.id.map_mv_mapcontainer);
        mMapViewContainer.addView(mMapView);
        recyclerView = findViewById(R.id.map_recyclerview);
        LocationAdapter locationAdapter = new LocationAdapter(documentArrayList, getApplicationContext(), mSearchEdit, recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false); //레이아웃매니저 생성
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL)); //아래구분선 세팅
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(locationAdapter);

        //리스너 세팅
        mMapView.setMapViewEventListener(this); // this에 MapView.MapViewEventListener 구현.
        mMapView.setPOIItemEventListener(this);
        mMapView.setOpenAPIKeyAuthenticationResultListener(this);

        //버튼클릭
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
        searchDetailFab.setOnClickListener(this);
        stopTrackingFab.setOnClickListener(this);

        Toast.makeText(this, "맵을 로딩중입니다", Toast.LENGTH_SHORT).show();
        //현재위치 업데이트
        mMapView.setCurrentLocationEventListener(this);
        //setCurrentLocationTrackingMode (지도랑 현재위치 좌표 찍어주고 따라다닌다.)
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        mLoaderLayout.setVisibility(View.VISIBLE);

        // editText 검색 텍스처이벤트
        mSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // 입력하기 전에
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() >= 1) {
                    // if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {

                    documentArrayList.clear();
                    locationAdapter.clear();
                    locationAdapter.notifyDataSetChanged();
                    ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                    Call<CategoryResult> call = apiInterface.getSearchLocation(getString(R.string.restapi_key), charSequence.toString(), 15);
                    call.enqueue(new Callback<CategoryResult>() {
                        @Override
                        public void onResponse(@NotNull Call<CategoryResult> call, @NotNull Response<CategoryResult> response) {
                            if (response.isSuccessful()) {
                                assert response.body() != null;
                                for (Document document : response.body().getDocuments()) {
                                    locationAdapter.addItem(document);
                                }
                                locationAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<CategoryResult> call, @NotNull Throwable t) {

                        }
                    });
                    //}
                    //mLastClickTime = SystemClock.elapsedRealtime();
                } else {
                    if (charSequence.length() <= 0) {
                        recyclerView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 입력이 끝났을 때
            }
        });

        mSearchEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Toast.makeText(getApplicationContext(), "Got the focus", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Lost the focus", Toast.LENGTH_LONG).show();
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
        mSearchEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FancyToast.makeText(getApplicationContext(), "검색리스트에서 장소를 선택해주세요", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                FancyToast.makeText(this, "1번 버튼: 검색좌표 기준으로 1km 검색" +
                        "\n2번 버튼: 현재위치 기준으로 주변환경 검색" +
                        "\n3번 버튼: 현재위치 추적 및 업데이트", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();
                anim();
                break;
            case R.id.fab1: //아래버튼에서부터 1~3임
                FancyToast.makeText(this, "현재위치 추적 시작", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                searchDetailFab.setVisibility(View.GONE);
                mLoaderLayout.setVisibility(View.VISIBLE);
                isTrackingMode = true;
                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
                anim();
                stopTrackingFab.setVisibility(View.VISIBLE);
                mLoaderLayout.setVisibility(View.GONE);
                break;
            case R.id.fab2:
                FancyToast.makeText(this, "현재위치기준 1km 검색 시작", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                stopTrackingFab.setVisibility(View.GONE);
                mLoaderLayout.setVisibility(View.VISIBLE);
                anim();
                //현재 위치 기준으로 1km 검색
                mMapView.removeAllPOIItems();
                mMapView.removeAllCircles();
                requestSearchLocal(mCurrentLng, mCurrentLat);
                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                break;
            case R.id.fab3:
                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
                mLoaderLayout.setVisibility(View.VISIBLE);
                anim();
                if(mSearchLat != -1 && mSearchLng != -1){
                    mMapView.removeAllPOIItems();
                    mMapView.removeAllCircles();
                    mMapView.addPOIItem(searchMarker);
                    requestSearchLocal(mSearchLng, mSearchLat);
                }else{
                    FancyToast.makeText(this, "검색 먼저 해주세요", FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                }
                mLoaderLayout.setVisibility(View.GONE);
                break;
            case R.id.fab_detail:
                FancyToast.makeText(this, "검색결과 상세보기", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                Intent detailIntent = new Intent(MapActivity.this, MapSearchDetailActivity.class);
                detailIntent.putParcelableArrayListExtra(IntentKey.CATEGOTY_SEARCH_MODEL_EXTRA1, bigMartList);
                detailIntent.putParcelableArrayListExtra(IntentKey.CATEGOTY_SEARCH_MODEL_EXTRA2, gs24List);
                detailIntent.putParcelableArrayListExtra(IntentKey.CATEGOTY_SEARCH_MODEL_EXTRA3, schoolList);
                detailIntent.putParcelableArrayListExtra(IntentKey.CATEGOTY_SEARCH_MODEL_EXTRA4, academyList);
                detailIntent.putParcelableArrayListExtra(IntentKey.CATEGOTY_SEARCH_MODEL_EXTRA5, subwayList);
                detailIntent.putParcelableArrayListExtra(IntentKey.CATEGOTY_SEARCH_MODEL_EXTRA6, bankList);
                detailIntent.putParcelableArrayListExtra(IntentKey.CATEGOTY_SEARCH_MODEL_EXTRA7, hospitalList);
                detailIntent.putParcelableArrayListExtra(IntentKey.CATEGOTY_SEARCH_MODEL_EXTRA8, pharmacyList);
                detailIntent.putParcelableArrayListExtra(IntentKey.CATEGOTY_SEARCH_MODEL_EXTRA9, cafeList);
                startActivity(detailIntent);
                Log.d(TAG, "fab_detail");
                break;
            case R.id.fab_stop_tracking:
                isTrackingMode = false;
                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
                stopTrackingFab.setVisibility(View.GONE);
                FancyToast.makeText(this, "현재위치 추적 종료", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                break;
        }
    }

    private void requestSearchLocal(double x, double y) {

        bigMartList.clear();
        gs24List.clear();
        schoolList.clear();
        academyList.clear();
        subwayList.clear();
        bankList.clear();
        hospitalList.clear();
        pharmacyList.clear();
        cafeList.clear();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<CategoryResult> call = apiInterface.getSearchCategory(getString(R.string.restapi_key), "MT1", x + "", y + "", 1000);
        call.enqueue(new Callback<CategoryResult>() {
            @Override
            public void onResponse(@NotNull Call<CategoryResult> call, @NotNull Response<CategoryResult> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getDocuments() != null) {
                        Log.d(TAG, "bigMartList Success");
                        bigMartList.addAll(response.body().getDocuments());
                    }
                    call = apiInterface.getSearchCategory(getString(R.string.restapi_key), "CS2", x + "", y + "", 1000);
                    call.enqueue(new Callback<CategoryResult>() {
                        @Override
                        public void onResponse(@NotNull Call<CategoryResult> call, @NotNull Response<CategoryResult> response) {
                            if (response.isSuccessful()) {
                                assert response.body() != null;
                                Log.d(TAG, "gs24List Success");
                                gs24List.addAll(response.body().getDocuments());
                                call = apiInterface.getSearchCategory(getString(R.string.restapi_key), "SC4", x + "", y + "", 1000);
                                call.enqueue(new Callback<CategoryResult>() {
                                    @Override
                                    public void onResponse(@NotNull Call<CategoryResult> call, @NotNull Response<CategoryResult> response) {
                                        if (response.isSuccessful()) {
                                            assert response.body() != null;
                                            Log.d(TAG, "schoolList Success");
                                            schoolList.addAll(response.body().getDocuments());
                                            call = apiInterface.getSearchCategory(getString(R.string.restapi_key), "AC5", x + "", y + "", 1000);
                                            call.enqueue(new Callback<CategoryResult>() {
                                                @Override
                                                public void onResponse(@NotNull Call<CategoryResult> call, @NotNull Response<CategoryResult> response) {
                                                    if (response.isSuccessful()) {
                                                        assert response.body() != null;
                                                        Log.d(TAG, "academyList Success");
                                                        academyList.addAll(response.body().getDocuments());
                                                        call = apiInterface.getSearchCategory(getString(R.string.restapi_key), "SW8", x + "", y + "", 1000);
                                                        call.enqueue(new Callback<CategoryResult>() {
                                                            @Override
                                                            public void onResponse(@NotNull Call<CategoryResult> call, @NotNull Response<CategoryResult> response) {
                                                                if (response.isSuccessful()) {
                                                                    assert response.body() != null;
                                                                    Log.d(TAG, "subwayList Success");
                                                                    subwayList.addAll(response.body().getDocuments());
                                                                    call = apiInterface.getSearchCategory(getString(R.string.restapi_key), "BK9", x + "", y + "", 1000);
                                                                    call.enqueue(new Callback<CategoryResult>() {
                                                                        @Override
                                                                        public void onResponse(@NotNull Call<CategoryResult> call, @NotNull Response<CategoryResult> response) {
                                                                            if (response.isSuccessful()) {
                                                                                assert response.body() != null;
                                                                                Log.d(TAG, "bankList Success");
                                                                                bankList.addAll(response.body().getDocuments());
                                                                                call = apiInterface.getSearchCategory(getString(R.string.restapi_key), "HP8", x + "", y + "", 1000);
                                                                                call.enqueue(new Callback<CategoryResult>() {
                                                                                    @Override
                                                                                    public void onResponse(@NotNull Call<CategoryResult> call, @NotNull Response<CategoryResult> response) {
                                                                                        if (response.isSuccessful()) {
                                                                                            assert response.body() != null;
                                                                                            Log.d(TAG, "hospitalList Success");
                                                                                            hospitalList.addAll(response.body().getDocuments());
                                                                                            call = apiInterface.getSearchCategory(getString(R.string.restapi_key), "PM9", x + "", y + "", 1000);
                                                                                            call.enqueue(new Callback<CategoryResult>() {
                                                                                                @Override
                                                                                                public void onResponse(@NotNull Call<CategoryResult> call, @NotNull Response<CategoryResult> response) {
                                                                                                    if (response.isSuccessful()) {
                                                                                                        assert response.body() != null;
                                                                                                        Log.d(TAG, "pharmacyList Success");
                                                                                                        pharmacyList.addAll(response.body().getDocuments());
                                                                                                        call = apiInterface.getSearchCategory(getString(R.string.restapi_key), "CE7", x + "", y + "", 1000);
                                                                                                        call.enqueue(new Callback<CategoryResult>() {
                                                                                                            @Override
                                                                                                            public void onResponse(@NotNull Call<CategoryResult> call, @NotNull Response<CategoryResult> response) {
                                                                                                                if (response.isSuccessful()) {
                                                                                                                    assert response.body() != null;
                                                                                                                    Log.d(TAG, "cafeList Success");
                                                                                                                    cafeList.addAll(response.body().getDocuments());
                                                                                                                    //모두 통신 성공 시 circle 생성
                                                                                                                    MapCircle circle1 = new MapCircle(
                                                                                                                            MapPoint.mapPointWithGeoCoord(y, x), // center
                                                                                                                            1000, // radius
                                                                                                                            Color.argb(128, 255, 0, 0), // strokeColor
                                                                                                                            Color.argb(128, 0, 255, 0) // fillColor
                                                                                                                    );
                                                                                                                    circle1.setTag(5678);
                                                                                                                    mMapView.addCircle(circle1);
                                                                                                                    Log.d("SIZE1", bigMartList.size() + "");
                                                                                                                    Log.d("SIZE2", gs24List.size() + "");
                                                                                                                    Log.d("SIZE3", schoolList.size() + "");
                                                                                                                    Log.d("SIZE4", academyList.size() + "");
                                                                                                                    Log.d("SIZE5", subwayList.size() + "");
                                                                                                                    Log.d("SIZE6", bankList.size() + "");
                                                                                                                    //마커 생성
                                                                                                                    int tagNum = 10;
                                                                                                                    for (Document document : bigMartList) {
                                                                                                                        MapPOIItem marker = new MapPOIItem();
                                                                                                                        marker.setItemName(document.getPlaceName() + "(대형마트)");
                                                                                                                        marker.setTag(tagNum++);
                                                                                                                        double x = Double.parseDouble(document.getY());
                                                                                                                        double y = Double.parseDouble(document.getX());
                                                                                                                        //카카오맵은 참고로 new MapPoint()로  생성못함. 좌표기준이 여러개라 이렇게 메소드로 생성해야함
                                                                                                                        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(x, y);
                                                                                                                        marker.setMapPoint(mapPoint);
                                                                                                                        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
                                                                                                                        marker.setCustomImageResourceId(R.drawable.ic_big_mart_marker); // 마커 이미지.
                                                                                                                        marker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
                                                                                                                        marker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
                                                                                                                        mMapView.addPOIItem(marker);
                                                                                                                    }
                                                                                                                    for (Document document : gs24List) {
                                                                                                                        MapPOIItem marker = new MapPOIItem();
                                                                                                                        marker.setItemName(document.getPlaceName() + "(편의점)");
                                                                                                                        marker.setTag(tagNum++);
                                                                                                                        double x = Double.parseDouble(document.getY());
                                                                                                                        double y = Double.parseDouble(document.getX());
                                                                                                                        //카카오맵은 참고로 new MapPoint()로  생성못함. 좌표기준이 여러개라 이렇게 메소드로 생성해야함
                                                                                                                        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(x, y);
                                                                                                                        marker.setMapPoint(mapPoint);
                                                                                                                        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
                                                                                                                        marker.setCustomImageResourceId(R.drawable.ic_24_mart_marker); // 마커 이미지.
                                                                                                                        marker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
                                                                                                                        marker.setCustomImageAnchor(0.5f, 1.0f);
                                                                                                                        mMapView.addPOIItem(marker);
                                                                                                                    }
                                                                                                                    for (Document document : schoolList) {
                                                                                                                        MapPOIItem marker = new MapPOIItem();
                                                                                                                        marker.setItemName(document.getPlaceName() + "(학교)");
                                                                                                                        marker.setTag(tagNum++);
                                                                                                                        double x = Double.parseDouble(document.getY());
                                                                                                                        double y = Double.parseDouble(document.getX());
                                                                                                                        //카카오맵은 참고로 new MapPoint()로  생성못함. 좌표기준이 여러개라 이렇게 메소드로 생성해야함
                                                                                                                        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(x, y);
                                                                                                                        marker.setMapPoint(mapPoint);
                                                                                                                        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
                                                                                                                        marker.setCustomImageResourceId(R.drawable.ic_school_marker); // 마커 이미지.
                                                                                                                        marker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
                                                                                                                        marker.setCustomImageAnchor(0.5f, 1.0f);
                                                                                                                        mMapView.addPOIItem(marker);
                                                                                                                    }
                                                                                                                    for (Document document : academyList) {
                                                                                                                        MapPOIItem marker = new MapPOIItem();
                                                                                                                        marker.setItemName(document.getPlaceName() + "(학원)");
                                                                                                                        marker.setTag(tagNum++);
                                                                                                                        double x = Double.parseDouble(document.getY());
                                                                                                                        double y = Double.parseDouble(document.getX());
                                                                                                                        //카카오맵은 참고로 new MapPoint()로  생성못함. 좌표기준이 여러개라 이렇게 메소드로 생성해야함
                                                                                                                        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(x, y);
                                                                                                                        marker.setMapPoint(mapPoint);
                                                                                                                        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
                                                                                                                        marker.setCustomImageResourceId(R.drawable.ic_academy_marker); // 마커 이미지.
                                                                                                                        marker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
                                                                                                                        marker.setCustomImageAnchor(0.5f, 1.0f);
                                                                                                                        mMapView.addPOIItem(marker);
                                                                                                                    }
                                                                                                                    for (Document document : subwayList) {
                                                                                                                        MapPOIItem marker = new MapPOIItem();
                                                                                                                        marker.setItemName(document.getPlaceName() + "(지하철)");
                                                                                                                        marker.setTag(tagNum++);
                                                                                                                        double x = Double.parseDouble(document.getY());
                                                                                                                        double y = Double.parseDouble(document.getX());
                                                                                                                        //카카오맵은 참고로 new MapPoint()로  생성못함. 좌표기준이 여러개라 이렇게 메소드로 생성해야함
                                                                                                                        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(x, y);
                                                                                                                        marker.setMapPoint(mapPoint);
                                                                                                                        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
                                                                                                                        marker.setCustomImageResourceId(R.drawable.ic_subway_marker); // 마커 이미지.
                                                                                                                        marker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
                                                                                                                        marker.setCustomImageAnchor(0.5f, 1.0f);
                                                                                                                        mMapView.addPOIItem(marker);
                                                                                                                    }
                                                                                                                    for (Document document : bankList) {
                                                                                                                        MapPOIItem marker = new MapPOIItem();
                                                                                                                        marker.setItemName(document.getPlaceName() + "(은행)");
                                                                                                                        marker.setTag(tagNum++);
                                                                                                                        double x = Double.parseDouble(document.getY());
                                                                                                                        double y = Double.parseDouble(document.getX());
                                                                                                                        //카카오맵은 참고로 new MapPoint()로  생성못함. 좌표기준이 여러개라 이렇게 메소드로 생성해야함
                                                                                                                        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(x, y);
                                                                                                                        marker.setMapPoint(mapPoint);
                                                                                                                        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
                                                                                                                        marker.setCustomImageResourceId(R.drawable.ic_bank_marker); // 마커 이미지.
                                                                                                                        marker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
                                                                                                                        marker.setCustomImageAnchor(0.5f, 1.0f);
                                                                                                                        mMapView.addPOIItem(marker);
                                                                                                                    }
                                                                                                                    for (Document document : hospitalList) {
                                                                                                                        MapPOIItem marker = new MapPOIItem();
                                                                                                                        marker.setItemName(document.getPlaceName() + "(병원)");
                                                                                                                        marker.setTag(tagNum++);
                                                                                                                        double x = Double.parseDouble(document.getY());
                                                                                                                        double y = Double.parseDouble(document.getX());
                                                                                                                        //카카오맵은 참고로 new MapPoint()로  생성못함. 좌표기준이 여러개라 이렇게 메소드로 생성해야함
                                                                                                                        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(x, y);
                                                                                                                        marker.setMapPoint(mapPoint);
                                                                                                                        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
                                                                                                                        marker.setCustomImageResourceId(R.drawable.ic_hospital_marker); // 마커 이미지.
                                                                                                                        marker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
                                                                                                                        marker.setCustomImageAnchor(0.5f, 1.0f);
                                                                                                                        mMapView.addPOIItem(marker);
                                                                                                                    }
                                                                                                                    for (Document document : pharmacyList) {
                                                                                                                        MapPOIItem marker = new MapPOIItem();
                                                                                                                        marker.setItemName(document.getPlaceName() + "(약국)");
                                                                                                                        marker.setTag(tagNum++);
                                                                                                                        double x = Double.parseDouble(document.getY());
                                                                                                                        double y = Double.parseDouble(document.getX());
                                                                                                                        //카카오맵은 참고로 new MapPoint()로  생성못함. 좌표기준이 여러개라 이렇게 메소드로 생성해야함
                                                                                                                        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(x, y);
                                                                                                                        marker.setMapPoint(mapPoint);
                                                                                                                        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
                                                                                                                        marker.setCustomImageResourceId(R.drawable.ic_pharmacy_marker); // 마커 이미지.
                                                                                                                        marker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
                                                                                                                        marker.setCustomImageAnchor(0.5f, 1.0f);
                                                                                                                        mMapView.addPOIItem(marker);
                                                                                                                        //자세히보기 fab 버튼 보이게
                                                                                                                        mLoaderLayout.setVisibility(View.GONE);
                                                                                                                        searchDetailFab.setVisibility(View.VISIBLE);
                                                                                                                    }
                                                                                                                    for (Document document : cafeList) {
                                                                                                                        MapPOIItem marker = new MapPOIItem();
                                                                                                                        marker.setItemName(document.getPlaceName() + "(카페)");
                                                                                                                        marker.setTag(tagNum++);
                                                                                                                        double x = Double.parseDouble(document.getY());
                                                                                                                        double y = Double.parseDouble(document.getX());
                                                                                                                        //카카오맵은 참고로 new MapPoint()로  생성못함. 좌표기준이 여러개라 이렇게 메소드로 생성해야함
                                                                                                                        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(x, y);
                                                                                                                        marker.setMapPoint(mapPoint);
                                                                                                                        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
                                                                                                                        marker.setCustomImageResourceId(R.drawable.ic_cafe_marker); // 마커 이미지.
                                                                                                                        marker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
                                                                                                                        marker.setCustomImageAnchor(0.5f, 1.0f);
                                                                                                                        mMapView.addPOIItem(marker);
                                                                                                                        //자세히보기 fab 버튼 보이게
                                                                                                                        mLoaderLayout.setVisibility(View.GONE);
                                                                                                                        searchDetailFab.setVisibility(View.VISIBLE);
                                                                                                                    }
                                                                                                                }
                                                                                                            }

                                                                                                            @Override
                                                                                                            public void onFailure(@NotNull Call<CategoryResult> call, @NotNull Throwable t) {

                                                                                                            }
                                                                                                        });
                                                                                                    }
                                                                                                }

                                                                                                @Override
                                                                                                public void onFailure(@NotNull Call<CategoryResult> call, Throwable t) {

                                                                                                }
                                                                                            });
                                                                                        }
                                                                                    }

                                                                                    @Override
                                                                                    public void onFailure(@NotNull Call<CategoryResult> call, @NotNull Throwable t) {

                                                                                    }
                                                                                });
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onFailure(@NotNull Call<CategoryResult> call, @NotNull Throwable t) {

                                                                        }
                                                                    });
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(@NotNull Call<CategoryResult> call, @NotNull Throwable t) {

                                                            }
                                                        });
                                                    }
                                                }

                                                @Override
                                                public void onFailure(@NotNull Call<CategoryResult> call, @NotNull Throwable t) {

                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NotNull Call<CategoryResult> call, @NotNull Throwable t) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<CategoryResult> call, @NotNull Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(@NotNull Call<CategoryResult> call, @NotNull Throwable t) {
                Log.d(TAG, "FAIL");
            }
        });
    }

    public void anim() {

        if (isFabOpen) {
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            isFabOpen = false;
        } else {
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            isFabOpen = true;
        }
    }


    @Override
    public void onMapViewInitialized(MapView mapView) {
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {
      /*  Log.d(TAG, "X=> " + mapView.getMapCenterPoint().getMapPointGeoCoord().latitude);
        Log.d(TAG, "Y=> " + mapView.getMapCenterPoint().getMapPointGeoCoord().longitude);*/
    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        //검색창켜져있을때 맵클릭하면 검색창 사라지게함
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onDaumMapOpenAPIKeyAuthenticationResult(MapView mapView, int i, String s) {

    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }

    /*
     *  현재 위치 업데이트(setCurrentLocationEventListener)
     */
    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float accuracyInMeters) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        Log.i(TAG, String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, accuracyInMeters));
        currentMapPoint = MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude, mapPointGeo.longitude);
        //이 좌표로 지도 중심 이동
        mMapView.setMapCenterPoint(currentMapPoint, true);
        //전역변수로 현재 좌표 저장
        mCurrentLat = mapPointGeo.latitude;
        mCurrentLng = mapPointGeo.longitude;
        Log.d(TAG, "현재위치 => " + mCurrentLat + "  " + mCurrentLng);
        mLoaderLayout.setVisibility(View.GONE);
        if (!isTrackingMode) {
            mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        }
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {
        Log.i(TAG, "onCurrentLocationUpdateFailed");
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {
        Log.i(TAG, "onCurrentLocationUpdateCancelled");
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
    }

    @Subscribe //검색예시 클릭시 이벤트 오토버스
    public void search(Document document) {//public항상 붙여줘야함
        FancyToast.makeText(getApplicationContext(), document.getPlaceName() +" 검색", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
        mSearchName = document.getPlaceName();
        mSearchLng = Double.parseDouble(document.getX());
        mSearchLat = Double.parseDouble(document.getY());
        mMapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(mSearchLat, mSearchLng), true);
        mMapView.removePOIItem(searchMarker);
        searchMarker.setItemName(mSearchName);
        searchMarker.setTag(10000);
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(mSearchLat, mSearchLng);
        searchMarker.setMapPoint(mapPoint);
        searchMarker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        searchMarker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        mMapView.addPOIItem(searchMarker);
    }

    @Override
    public void finish() {
        super.finish();
        bus.unregister(this); //이액티비티 떠나면 정류소 해제해줌
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        mMapView.setShowCurrentLocationMarker(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
