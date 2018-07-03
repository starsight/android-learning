package com.wenjiehe.android_learning;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wenjiehe.android_learning.Entry.GankItem;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "CustomAdapter";
    private List<GankItem> mData;
    private Context mContext;

    CustomAdapter(List<GankItem> data, Context context) {
        mData = data;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View v = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_custom, parent, false);
        viewHolder = new ImageViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GankItem myEntity = mData.get(position);
        ImageViewHolder h = (ImageViewHolder)holder;

        Log.d("hewenjie", "onBindViewHolder: "+position);
        h.textView.setText(myEntity.desc);
        if (myEntity.getImage() != null) {
            final String url = myEntity.getImage()+"?imageView2/1/format/jpg";
            Glide.with(mContext).load(url).into(h.imageView);

        }else {
            String url ="http://pic88.huitu.com/res/20161011/133455_20161011161144150200_1.jpg";
            Glide.with(mContext).load(url).asBitmap().into(h.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.custom_imageview);
            textView = itemView.findViewById(R.id.custom_text);
            itemView.setOnClickListener(listener);
        }
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = 0;//((ImageViewHolder)v.getParent()).getAdapterPosition();
            Toast.makeText(mContext,"click"+pos,Toast.LENGTH_SHORT).show();
        }
    };
}
