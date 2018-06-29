package com.wenjiehe.android_learning.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenjiehe.android_learning.Entry.GankItem;
import com.wenjiehe.android_learning.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class GankRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<GankItem> mData;


    public GankRecyclerViewAdapter(Context context,List<GankItem> data){
        mContext = context;
        mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        if(viewType == TYPE_IMAGE){
            View v = LayoutInflater.from(mContext).inflate(R.layout.recycler_image_item, parent, false);
             viewHolder = new GankImageViewHolder(v);
        }else{
            View v = LayoutInflater.from(mContext).inflate(R.layout.recycler_no_image_item, parent, false);
             viewHolder = new GankNoImageViewHolder(v);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        GankItem myEntity = mData.get(position);
        int type = getItemViewType(position);
        if(type == TYPE_NO_IMAGE){
            ((GankNoImageViewHolder)holder).setStr(myEntity.desc);
        }else{
            final GankImageViewHolder imageViewHolder = (GankImageViewHolder) holder;
            imageViewHolder.setStr(myEntity.desc);
            //先设置图片占位符
            imageViewHolder.gankImage.setImageDrawable(mContext.getDrawable(R.mipmap.ic_launcher));
            final String url = myEntity.getImage();
            //为imageView设置Tag,内容是该imageView等待加载的图片url
            imageViewHolder.gankImage.setTag(url);
            AsyncTask asyncTask = new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... params) {
                    try {
                        URL url = new URL(mData.get(position).getImage());
                        Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
                        return bitmap;
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    super.onPostExecute(bitmap);
                    //加载完毕后判断该imageView等待的图片url是不是加载完毕的这张
                    //如果是则为imageView设置图片,否则说明imageView已经被重用到其他item
                    String str=(String)imageViewHolder.gankImage.getTag();
                    if(url.equals(str)) {
                        imageViewHolder.gankImage.setImageBitmap(bitmap);
                    }else {
                        Log.d("AAA", "onPostExecute: " + url + "~~" + str);
                    }

                }
            }.execute();
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    private final static int TYPE_NO_IMAGE =1;
    private final static int TYPE_IMAGE =2;

    @Override
    public int getItemViewType(int position) {
        if(mData.get(position).getImage()==null){
            return TYPE_NO_IMAGE;
        }else {
            return TYPE_IMAGE;
        }
    }


    static class GankImageViewHolder extends RecyclerView.ViewHolder {
        private TextView strTv;
        private ImageView gankImage;

        public GankImageViewHolder(View itemView) {
            super(itemView);
            strTv =  itemView.findViewById(R.id.desc);
            gankImage =  itemView.findViewById(R.id.gank_image);
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

}

