package com.wenjiehe.android_learning.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wenjiehe.android_learning.Entry.GankItem;
import com.wenjiehe.android_learning.R;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;

public class GankRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "GankRecyclerViewAdapter";
    private Context mContext;
    private List<GankItem> mData;
    private LruCache<String, BitmapDrawable> mLruCache;

    public GankRecyclerViewAdapter(Context context, List<GankItem> data) {
        mContext = context;
        mData = data;
        int maxSize = (int) Runtime.getRuntime().maxMemory() >> 3;
        mLruCache = new LruCache<String, BitmapDrawable>(maxSize) {
            @Override
            protected int sizeOf(String key, BitmapDrawable value) {
                return value.getBitmap().getByteCount();
            }
        };
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        if (viewType == TYPE_IMAGE) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.recycler_image_item, parent, false);
            viewHolder = new GankImageViewHolder(v);
        } else {
            View v = LayoutInflater.from(mContext).inflate(R.layout.recycler_no_image_item, parent, false);
            viewHolder = new GankNoImageViewHolder(v);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        GankItem myEntity = mData.get(position);
        int type = getItemViewType(position);
        if (type == TYPE_NO_IMAGE) {
            ((GankNoImageViewHolder) holder).setStr(myEntity.desc);
        } else {
            final GankImageViewHolder imageViewHolder = (GankImageViewHolder) holder;
            imageViewHolder.setStr(myEntity.desc);
            //先设置图片占位符
            //imageViewHolder.gankImage.setImageDrawable(mContext.getDrawable(R.mipmap.ic_launcher));
            final String url = myEntity.getImage()+"?imageView2/1/w/150/format/jpg";
            //为imageView设置Tag,内容是该imageView等待加载的图片url
            //imageViewHolder.gankImage.setTag(url);
            Glide.with(mContext).load(url).asBitmap().into(imageViewHolder.gankImage);
//            if (mLruCache.get(url) != null) {
//                imageViewHolder.gankImage.setImageDrawable(mLruCache.get(url));
//            } else {
//                loadBitmap(url, imageViewHolder.gankImage);
//            }
        }
    }

    public void loadBitmap(String url, ImageView imageView) {
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
        if (cancelBeforeTask(url, imageView)) {
            BitmapDownloadTask task = new BitmapDownloadTask(imageView, mLruCache, mContext);
            AsyncDrawable asyncDrawable = new AsyncDrawable(mContext.getResources(), bitmap, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(url);
        }
    }

    public boolean cancelBeforeTask(String url, ImageView imageView) {
        //Log.d(TAG, "cancelBeforeTask: 1");
        BitmapDownloadTask task = getBitmapWorkerTask(imageView);
        //Log.d(TAG, "cancelBeforeTask: 2");
        if (task != null) {
            String imgUrl = task.url;
            if (!imgUrl.equals(url) || imgUrl.equals("")) {
                //取消上次task，执行此次task
                Log.d(TAG, "cancelBeforeTask: cancel previous task");
                task.cancel(true);
            } else {
                Log.d(TAG, "cancelBeforeTask:  has two same task");
                // 已经存在相同task，此次task不执行
                return false;
            }
        }
        return true;
    }

    private BitmapDownloadTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapTask();
            }
        } else {
            //Log.d(TAG, "getBitmapWorkerTask:  null");
        }
        return null;
    }

    static class AsyncDrawable extends BitmapDrawable {
        private WeakReference<BitmapDownloadTask> taskWeakReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, BitmapDownloadTask task) {
            super(res, bitmap);
            taskWeakReference = new WeakReference<>(task);
        }

        public BitmapDownloadTask getBitmapTask() {
            return taskWeakReference.get();
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    private final static int TYPE_NO_IMAGE = 1;
    private final static int TYPE_IMAGE = 2;

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position).getImage() == null) {
            return TYPE_NO_IMAGE;
        } else {
            return TYPE_IMAGE;
        }
    }

    static class GankImageViewHolder extends RecyclerView.ViewHolder {
        private TextView strTv;
        private ImageView gankImage;

        public GankImageViewHolder(View itemView) {
            super(itemView);
            strTv = itemView.findViewById(R.id.desc);
            gankImage = itemView.findViewById(R.id.gank_image);
        }

        public void setStr(String str) {
            strTv.setText(str);
        }
    }

    static class GankNoImageViewHolder extends RecyclerView.ViewHolder {
        private TextView strTv;

        public GankNoImageViewHolder(View itemView) {
            super(itemView);
            strTv = itemView.findViewById(R.id.desc);
        }

        public void setStr(String str) {
            strTv.setText(str);
        }
    }

    class BitmapDownloadTask extends AsyncTask<String, Void, BitmapDrawable> {
        //private ImageView imageview;
        private WeakReference<ImageView> imageViewWeakReference;
        String url = "";
        private LruCache mLruCache;
        private Context mContext;

        BitmapDownloadTask(ImageView imageView, LruCache lruCache, Context context) {
            //imageview = imageView;
            imageViewWeakReference = new WeakReference<>(imageView);
            mLruCache = lruCache;
            mContext = context;
        }

        @Override
        protected BitmapDrawable doInBackground(String... params) {
            url = params[0];
            try {
                URL urll = new URL(url);
                Bitmap bitmap = BitmapFactory.decodeStream(urll.openStream());
                BitmapDrawable bitmapDrawable = new BitmapDrawable(mContext.getResources(), bitmap);
                mLruCache.put(url, bitmapDrawable);
                return bitmapDrawable;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(BitmapDrawable bitmapDrawable) {
            super.onPostExecute(bitmapDrawable);
            if (isCancelled()) {
                bitmapDrawable = null;
            }
            //加载完毕后判断该imageView等待的图片url是不是加载完毕的这张
            //如果是则为imageView设置图片,否则说明imageView已经被重用到其他item
//        String str = (String) imageview.getTag();
//        if (url.equals(str)) {
//            imageview.setImageBitmap(bitmap);
//        } else {
//            Log.d("AAA", "onPostExecute: " + url + "~~" + str);
//        }


            if (imageViewWeakReference != null && bitmapDrawable != null) {
                ImageView imageView = imageViewWeakReference.get();
                //Log.d(TAG, "onPostExecute: 1");
                BitmapDownloadTask task = getBitmapWorkerTask(imageView);
                //Log.d(TAG, "onPostExecute: 2");
                // 经测试，此次task == null 检测的目的在于WeakReference带来的释放。没有发现前面的cancel没取消旧的task
                if (this == task && imageView != null) {
                    imageView.setImageDrawable(bitmapDrawable);
                } else {
                    if (task != null) {
                        Log.d(TAG, "onPostExecute: ");
                    }
                }
            }

        }
    }


}
