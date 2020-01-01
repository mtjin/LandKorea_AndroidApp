package com.mtjin.mapogreen.activities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.mtjin.mapogreen.R;
import com.ramotion.circlemenu.CircleMenuView;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivityTAG";
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
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                if (index == 1) {
                    startActivity(intent);
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
                }
            }
        });


    }

}
