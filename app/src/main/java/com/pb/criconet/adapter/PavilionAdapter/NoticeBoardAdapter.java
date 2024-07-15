package com.pb.criconet.adapter.PavilionAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.criconet.R;

public class NoticeBoardAdapter extends RecyclerView.Adapter<NoticeBoardAdapter.MyViewHolder>{

    Context mContext;
    noticeBoardItemClickListener noticeBoardItemClickListener;
    public NoticeBoardAdapter(Context mContext,noticeBoardItemClickListener noticeBoardItemClickListener){
        this.mContext = mContext;
        this.noticeBoardItemClickListener = noticeBoardItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.mynoticeboard_child,null,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
     holder.itemView.setOnClickListener(v -> {
         noticeBoardItemClickListener.noticeItemClickEvent();
     });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    public interface noticeBoardItemClickListener {
         void noticeItemClickEvent();
    }
}
