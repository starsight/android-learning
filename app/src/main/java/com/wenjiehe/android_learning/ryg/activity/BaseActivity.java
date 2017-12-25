package com.wenjiehe.android_learning.ryg.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("wenjie",this.toString()+"---create");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("wenjie",this.toString()+"---start");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("wenjie",this.toString()+"---resume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("wenjie",this.toString()+"---pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("wenjie",this.toString()+"---stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("wenjie",this.toString()+"---destory");
    }
}
