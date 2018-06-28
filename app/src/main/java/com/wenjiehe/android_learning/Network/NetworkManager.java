package com.wenjiehe.android_learning.Network;

import android.util.Log;

import com.wenjiehe.android_learning.Entry.GankApi;
import com.wenjiehe.android_learning.Entry.GankInfo;
import com.wenjiehe.android_learning.Entry.GankItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Func1;

public class NetworkManager {
    private static final String TAG = "NetworkManager";
    private GankApi gankApi = null;

    public static volatile NetworkManager networkManager = null;
    private NetworkManager(){

    }

    public static NetworkManager getInstance(){
        if(networkManager == null){
            synchronized (NetworkManager.class){
                if(networkManager == null){
                    networkManager = new NetworkManager();
                }
            }
        }
        return networkManager;
    }

    public  Observable<List<GankItem>> loadNew(String dayStr){
        return loadFromGank(dayStr);
    }

    private Observable<List<GankItem>> loadFromGank(final String dayStr) {
        Log.d(TAG, "loadFromGank: ");
        String[] parts = dayStr.split("/");
        int year = Integer.valueOf(parts[0]);
        int month = Integer.valueOf(parts[1]);
        int day = Integer.valueOf(parts[2]);

        if(gankApi == null){
            gankApi = new Retrofit.Builder().baseUrl(GankApi.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build().create(GankApi.class);

        }
        return gankApi.loadData(year, month, day)
                .map(new Func1<GankInfo, List<GankItem>>() {
                    @Override
                    public List<GankItem> call(GankInfo gankInfo) {
                        if (gankInfo.error) {
                            throw new RuntimeException("gank error");
                        }
                        List<GankItem> gankItemList = new ArrayList<>();
                        if (gankInfo.hasData()) {
                            for (Map.Entry<String, List<GankItem>> entry: gankInfo.results.entrySet()) {
                                gankItemList.addAll(entry.getValue());
                            }
                        }
                        for (GankItem gankItem: gankItemList) {
                            gankItem.day = dayStr;
                        }
                        return gankItemList;
                    }
                });

    }

    private static String getToday() {
        Calendar calendar = Calendar.getInstance();
        return dayInt2Str(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    private static String dayInt2Str(int year, int month, int day) {
        return year + "/" + month + "/" + day;
    }


}
