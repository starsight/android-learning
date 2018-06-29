package com.wenjiehe.android_learning;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.wenjiehe.android_learning.Adapter.GankRecyclerViewAdapter;
import com.wenjiehe.android_learning.Entry.GankItem;
import com.wenjiehe.android_learning.Network.NetworkManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author wenjie
 */
public class MainActivity extends AppCompatActivity{
    private static final String TAG = "MainActivity";
    private NetworkManager networkManager;

    @BindView(R.id.button_network_request_1)
    Button buttonNetworkRequest1;

    @BindView(R.id.button_network_request_2)
    Button button_network_request_2;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    private ArrayList<GankItem> myData;
    private GankRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        myData = new ArrayList<>();

        networkManager = NetworkManager.getInstance();
        CustomLayoutManager customLayoutManager = new CustomLayoutManager();
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapter = new GankRecyclerViewAdapter(MainActivity.this,myData);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    @OnClick(R.id.button_network_request_1)
    void requestGankInfo(){
        Toast.makeText(this,"button click~",Toast.LENGTH_SHORT).show();
        networkManager.loadNew("2018/05/30")
                .delay(1500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNew);

    }

    @OnClick(R.id.button_network_request_2)
    void addGankInfo(){
        GankItem gankItem = new GankItem();
        gankItem.desc = "1";
        myData.add(gankItem);
        adapter.notifyItemInserted(myData.size()-1);
        recyclerView.scrollToPosition(myData.size()-1);
    }

    private Action1<List<GankItem>> onNew = new Action1<List<GankItem>>() {
        @Override
        public void call(List<GankItem> gankItemList) {
            int i=0;
            int index = myData.size();
            for (GankItem gankItem :gankItemList) {
                Log.d(TAG, "call: "+ gankItem.toString());
                myData.add(gankItem);
                //adapter.notifyItemInserted(i++);
            }
            adapter.notifyItemRangeInserted(index,myData.size());
            //recyclerView.notifyAll();
        }
    };


}
