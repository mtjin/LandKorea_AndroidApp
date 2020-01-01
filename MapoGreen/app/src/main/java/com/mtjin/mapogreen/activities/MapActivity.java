package com.mtjin.mapogreen.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
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
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mtjin.mapogreen.R;
import com.mtjin.mapogreen.api.ApiClient;
import com.mtjin.mapogreen.api.ApiInterface;
import com.shashank.sony.fancytoastlib.FancyToast;


import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;


public class MapActivity extends AppCompatActivity implements MapView.MapViewEventListener, MapView.POIItemEventListener, MapView.OpenAPIKeyAuthenticationResultListener, View.OnClickListener, MapView.CurrentLocationEventListener {
    final static String TAG = "MapActivityTAG";
    //xml
    MapView mMapView;
    ViewGroup mMapViewContainer;
    EditText mSearchEdit;
    Button mOkButton;
    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2, fab3;
    RelativeLayout mLoaderLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //binding
        mSearchEdit = findViewById(R.id.map_et_search);
        mOkButton = findViewById(R.id.map_btn_ok);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        fab3 = findViewById(R.id.fab3);
        mLoaderLayout = findViewById(R.id.loaderLayout);

        mMapView = new MapView(this);
        mMapViewContainer = findViewById(R.id.map_mv_mapcontainer);
        mMapViewContainer.addView(mMapView);

        //리스너 세팅
        mMapView.setMapViewEventListener(this); // this에 MapView.MapViewEventListener 구현.
        mMapView.setPOIItemEventListener(this);
        mMapView.setOpenAPIKeyAuthenticationResultListener(this);
        //버튼클릭
        mOkButton.setOnClickListener(this);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);

        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633);
        mMapView.setMapCenterPoint(mapPoint, true);
        //서울시 마포구 공덕동
        //MapCoord mapCoord = new MapCoord(491171, 1125184);

        //현재위치 업데이트
        mMapView.setCurrentLocationEventListener(this);
        //setCurrentLocationTrackingMode
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);

    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                anim();
                FancyToast.makeText(this, "1번 버튼: 해당 좌표 중심으로 1km 검색" +
                        "\n2번 버튼: 현재위치 검색" +
                        "\n3번 버튼: 1km 검색한 결과 자세히보기", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();
                break;
            case R.id.fab1:
                mLoaderLayout.setVisibility(View.VISIBLE);
                anim();
                mLoaderLayout.setVisibility(View.GONE);
                break;
            case R.id.fab2:
                mLoaderLayout.setVisibility(View.VISIBLE);
                anim();
                mLoaderLayout.setVisibility(View.GONE);
                break;
            case R.id.fab3:
                mLoaderLayout.setVisibility(View.VISIBLE);
                anim();
                mLoaderLayout.setVisibility(View.GONE);
                break;
            case R.id.map_btn_ok:
                mLoaderLayout.setVisibility(View.VISIBLE);
                if (mSearchEdit.getText().toString().trim().length() >= 3) {
                    requestSearchLocal(mSearchEdit.getText().toString().trim());
                } else {
                    FancyToast.makeText(this, "세글자 이상 입력해주세요", FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                }
                mLoaderLayout.setVisibility(View.GONE);
                break;
        }
    }

    private void requestSearchLocal(String searchName) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        // Call<SearchResult> call =apiInterface.getResearchLocal(searchName);
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
        float x = mapView.getX();
        float y = mapView.getY();
        Log.d(TAG, "X=> " + x);
        Log.d(TAG, "Y=> " + y);
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
        float x = mapView.getX();
        float y = mapView.getY();
        Log.d(TAG, "X=> " + x);
        Log.d(TAG, "Y=> " + y);
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {
        float x = mapView.getX();

        float y = mapView.getY();
        Log.d(TAG, "X=> " + mapView.getMapCenterPoint().getMapPointGeoCoord().latitude);
        Log.d(TAG, "Y=> " + mapView.getMapCenterPoint().getMapPointGeoCoord().longitude);
    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

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
        MapPoint currentMapPoint = MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude, mapPointGeo.longitude);
        mMapView.setMapCenterPoint(currentMapPoint, true);
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }
}
