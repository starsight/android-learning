package com.wenjiehe.android_learning;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

public class BaseActivity extends AppCompatActivity {
    private GestureDetector gestureDetector;
    private Scroller mScroller;
    private View mRootView;
    private int mWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_base);

        mRootView = getWindow().getDecorView();
        mRootView.setBackgroundColor(Color.WHITE);
        gestureDetector = new GestureDetector(this,new GestureListener());
        Point point = new Point();
        getWindow().getWindowManager().getDefaultDisplay().getSize(point);
        mWidth = point.y;
        mWidth =getWindow().getWindowManager().getDefaultDisplay().getWidth();
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(e1!=null){
                handle(e2);
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
        private void handle(MotionEvent e2){
            mRootView.setTranslationX(e2.getX());
            if(e2.getX()>mWidth-50){
                finish();
            }
        }
    }
}
