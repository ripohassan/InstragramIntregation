package com.mcc.instagramintegration.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mcc.instagramintegration.R;
import com.mcc.instagramintegration.model.Datum;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Datum> dataList;

    public PostAdapter(Context mContext, ArrayList<Datum> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Glide.with(mContext).load(dataList.get(position).getImages().getStandardResolution().getUrl()).into(holder.ivPostPicture);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivPostPicture;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPostPicture = itemView.findViewById(R.id.iv_post);

        }

    }

}
