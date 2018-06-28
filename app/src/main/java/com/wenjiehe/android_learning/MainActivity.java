package com.wenjiehe.android_learning;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wenjiehe.android_learning.Entry.GankItem;
import com.wenjiehe.android_learning.Network.NetworkManager;

import java.util.ArrayList;
import java.util.Iterator;
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

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    private ArrayList<GankItem> myData;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        myData = new ArrayList<>();

        networkManager = NetworkManager.getInstance();
        CustomLayoutManager customLayoutManager = new CustomLayoutManager();
        recyclerView.setLayoutManager(customLayoutManager);
        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);

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

    private Action1<List<GankItem>> onNew = new Action1<List<GankItem>>() {
        @Override
        public void call(List<GankItem> gankItemList) {
            for (GankItem gankItem :gankItemList) {
                Log.d(TAG, "call: "+ gankItem.toString());
                myData.add(gankItem);
            }
            adapter.notifyDataSetChanged();
            //recyclerView.notifyAll();
        }
    };

    //自定义Adapter
    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.recycler_view_item, parent, false);

            MyViewHolder viewHolder = new MyViewHolder(v);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            GankItem myEntity = myData.get(position);

            holder.setStr(myEntity.desc);
        }

        @Override
        public int getItemCount() {
            return myData.size();
        }
    }

    //自定义Holder
    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView strTv;


        public MyViewHolder(View itemView) {
            super(itemView);
            strTv = (TextView) itemView.findViewById(R.id.desc);
        }

        public void setStr(String str) {
            strTv.setText(str);
        }

    }
}
