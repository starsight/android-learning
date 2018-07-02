package com.wenjiehe.android_learning;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wenjiehe.android_learning.Adapter.GankRecyclerViewAdapter;
import com.wenjiehe.android_learning.Entry.GankItem;
import com.wenjiehe.android_learning.Network.NetworkManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private NetworkManager networkManager;

    @BindView(R.id.button_network_request_1)
    Button buttonNetworkRequest1;

    @BindView(R.id.button_scroll_to_top)
    Button button_scroll_to_top;


    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    CustomLayoutManager customLayoutManager;
    private ArrayList<GankItem> myData;
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        myData = new ArrayList<>();

        networkManager = NetworkManager.getInstance();
        customLayoutManager = new CustomLayoutManager(this);
        //recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setLayoutManager(customLayoutManager);
        //adapter = new GankRecyclerViewAdapter(MainActivity.this, myData);
        //recyclerView.setAdapter(adapter);
        adapter = new CustomAdapter(myData,MainActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(null);
        MySnap snap = new MySnap();
        snap.attachToRecyclerView(recyclerView);


        calendar = new GregorianCalendar();
        calendar.setTime(new Date());
    }

    Calendar calendar;

    @OnClick(R.id.button_scroll_to_top)
    void scrollToTop(){
        if(customLayoutManager.getmPosition()>3){
            recyclerView.scrollToPosition(3);
        }
        recyclerView.smoothScrollToPosition(0);
    }

    @OnClick(R.id.button_network_request_1)
    void requestGankInfo() {
        Toast.makeText(this, "button click~", Toast.LENGTH_SHORT).show();


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String str = simpleDateFormat.format(calendar.getTime());


        Log.d(TAG, "requestGankInfo: str:" + str);
        networkManager.loadNew(str)
                .delay(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNew);

        calendar.add(Calendar.DAY_OF_MONTH, -1);

    }

    private Action1<List<GankItem>> onNew = new Action1<List<GankItem>>() {
        @Override
        public void call(List<GankItem> gankItemList) {
            int i = 0;
            int index = myData.size() - 1;
            for (GankItem gankItem : gankItemList) {
                //Log.d(TAG, "call: "+ gankItem.toString());
                myData.add(gankItem);
            }
            adapter.notifyItemRangeInserted(index + 1, myData.size() - index + 1);
            //recyclerView.scrollToPosition(index);
        }
    };


}

class XRecyclerView extends RecyclerView {
    public XRecyclerView(Context context) {
        this(context, null);
    }

    public XRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        addOnScrollListener(new ImageAutoLoadScrollListener());
    }

    //监听滚动来对图片加载进行判断处理
    public class ImageAutoLoadScrollListener extends OnScrollListener {

        boolean isResume = false;
        private static final String TAG = "ImageAutoLoadScrollList";

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            Log.d(TAG, "onScrolled: " + dy);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            switch (newState) {
                case SCROLL_STATE_IDLE: // The RecyclerView is not currently scrolling.
                    //当屏幕停止滚动，加载图片
                    try {
                        if (getContext() != null) Glide.with(getContext()).resumeRequests();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case SCROLL_STATE_DRAGGING: // The RecyclerView is currently being dragged by outside input such as user touch input.
                    //当屏幕滚动且用户使用的触碰或手指还在屏幕上，停止加载图片
                    try {
                        if (getContext() != null) {
                            Log.d(TAG, "onScrollStateChanged: SCROLL_STATE_DRAGGING");
                            Glide.with(getContext()).pauseRequests();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case SCROLL_STATE_SETTLING: // The RecyclerView is currently animating to a final position while not under outside control.
                    //由于用户的操作，屏幕产生惯性滑动，停止加载图片
                    try {
                        if (getContext() != null) {
                            Log.d(TAG, "onScrollStateChanged: SCROLL_STATE_SETTLING");
                            Glide.with(getContext()).pauseRequests();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    break;
            }
        }
    }
}

