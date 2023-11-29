package com.pb.criconetnewdesign.adapter.PavilionAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.criconetnewdesign.R;

public class MyBlogsAdapter extends RecyclerView.Adapter<MyBlogsAdapter.MyViewHolder>{

    Context mContext;
    blogItemClickListener itemClickListener;
    public MyBlogsAdapter(Context mContext,blogItemClickListener itemClickListener){
        this.mContext = mContext;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.myblog_child,null,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.itemView.setOnClickListener(v -> {
            itemClickListener.blogItemClickEvent();
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

    public interface blogItemClickListener {
        public void blogItemClickEvent();
    }
}
