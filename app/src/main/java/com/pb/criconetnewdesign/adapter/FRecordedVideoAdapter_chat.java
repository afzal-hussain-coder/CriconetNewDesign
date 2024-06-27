package com.pb.criconetnewdesign.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.model.RecodedVideo;

import java.util.ArrayList;

public class FRecordedVideoAdapter_chat extends RecyclerView.Adapter<FRecordedVideoAdapter_chat.MyViewHolder> {
    Context context;
    LayoutInflater inflater;
    ArrayList<RecodedVideo> arrayList_image;
    itemClick itemClick;
    public FRecordedVideoAdapter_chat(ArrayList<RecodedVideo> chatname_list1, Context context, itemClick itemClick) {
        this.context = context;
        this.arrayList_image=chatname_list1;
        this.itemClick=itemClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_video_recorded_chat, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        Glide.with(context).load(arrayList_image.get(position).getPostVideo()).dontAnimate()
                .into(holder.ivMediaCoverImage);
        //holder.tv_recoded_at.setText(context.getResources().getString(R.string.Video_recorded_at)+arrayList_image.get(position).getCreated());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.zoomOn(arrayList_image.get(position).getPostVideo());
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList_image.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMediaCoverImage,ivPlay;
        TextView tv_recoded_at;
        MaterialCardView relative_dash;

        MyViewHolder(View itemView) {
            super(itemView);
            //relative_dash = itemView.findViewById(R.id.relative_dash);
            ivMediaCoverImage =  itemView.findViewById(R.id.ivMediaCoverImage);
            ivPlay = itemView.findViewById(R.id.ivPlay);


        }
    }


    public interface itemClick{
        void zoomOn(String url);
    }
}