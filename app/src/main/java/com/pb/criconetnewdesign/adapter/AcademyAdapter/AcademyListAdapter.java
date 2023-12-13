package com.pb.criconetnewdesign.adapter.AcademyAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.criconetnewdesign.R;

public class AcademyListAdapter extends RecyclerView.Adapter<AcademyListAdapter.MyViewHolder>{

    Context mContext;
    academyItemClickListener itemClickListener;
    public AcademyListAdapter(Context mContext, academyItemClickListener itemClickListener){
        this.mContext = mContext;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.academy_list_child,null,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.itemView.setOnClickListener(v -> {
            itemClickListener.academyItemClickEvent();
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

    public interface academyItemClickListener {
        public void academyItemClickEvent();
    }
}
