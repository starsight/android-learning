package com.wenjiehe.android_learning.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MyService extends Service {
    private int count =0;
    private MyBinder myBinder = new MyBinder();
    private boolean quit;


    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    class  MyBinder extends Binder{
        public int getCount()
        {
            return count;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread()
        {
            public void run()
            {
                while(!quit)
                {
                    try
                    {
                        Thread.sleep(1000);
                    }catch(InterruptedException e){e.printStackTrace();}
                    count++;
                }
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        quit =true;
    }
}
