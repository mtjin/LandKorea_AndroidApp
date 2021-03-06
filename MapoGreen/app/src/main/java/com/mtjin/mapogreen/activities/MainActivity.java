package com.mtjin.mapogreen.activities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.mtjin.mapogreen.R;
import com.ramotion.circlemenu.CircleMenuView;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainTAG";
    private CircleMenuView mCircleMenuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCircleMenuView = findViewById(R.id.main_circle_menu);
        mCircleMenuView.setEventListener(new CircleMenuView.EventListener() {
            @Override
            public void onMenuOpenAnimationStart(@NotNull CircleMenuView view) {

            }

            @Override
            public void onMenuOpenAnimationEnd(@NotNull CircleMenuView view) {
            }

            @Override
            public void onMenuCloseAnimationStart(@NotNull CircleMenuView view) {
            }

            @Override
            public void onMenuCloseAnimationEnd(@NotNull CircleMenuView view) {
            }

            @Override
            public void onButtonClickAnimationStart(@NotNull CircleMenuView view, int index) {
            }

            @Override
            public void onButtonClickAnimationEnd(@NotNull CircleMenuView view, int index) {
                Log.d(TAG, "onButtonClickAnimationEnd index => " +   index);
                if(index == 0){
                    Intent intent = new Intent(MainActivity.this, MapNavigationActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in_splash, R.anim.fade_out_splash);
                    FancyToast.makeText(MainActivity.this,"네비게이션",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                }
                else if (index == 1) {
                    Intent intent = new Intent(MainActivity.this, MapActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in_splash, R.anim.fade_out_splash);
                    FancyToast.makeText(MainActivity.this,"맵 검색",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                }
            }

            @Override
            public boolean onButtonLongClick(@NotNull CircleMenuView view, int index) {
                return  true;
            }

            @Override
            public void onButtonLongClickAnimationStart(@NotNull CircleMenuView view, int index) {

            }

            @Override
            public void onButtonLongClickAnimationEnd(@NotNull CircleMenuView view, int index) {
                Log.d(TAG, "onButtonLongClickAnimationEnd index => " +   index);
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                if (index == 1) {
                    startActivity(intent);
                    FancyToast.makeText(MainActivity.this,"맵 검색",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true);
                }
            }
        });


    }

}
