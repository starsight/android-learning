package com.wenjiehe.android_learning.ryg.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.squareup.leakcanary.RefWatcher;
import com.wenjiehe.android_learning.BaseActivity;
import com.wenjiehe.android_learning.MyApplication;
import com.wenjiehe.android_learning.R;
import com.wenjiehe.android_learning.fragment.AFragment;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FragmentActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        AFragment aFragment =new AFragment();

        //ft.add(R.id.framelayout,aFragment);
        //ft.commit();
        //init();
        LeakThread leakThread = new LeakThread();
        //leakThread.start();
    }

    class LeakThread extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(6 * 60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = MyApplication.getRefWatcher(this);//1
        refWatcher.watch(this);
    }

    public void init(){
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.i("接收数据", String.valueOf(aLong));
                    }
                });
    }
}
