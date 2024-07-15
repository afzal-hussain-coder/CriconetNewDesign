package com.pb.criconet.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.criconet.databinding.AmbassadorListItemBinding;
import com.pb.criconet.model.AmbassadorModel;

import java.util.ArrayList;

public class AmbassadorProgramAdapter extends RecyclerView.Adapter<AmbassadorProgramAdapter.MyViewHolder>{
    private Context mContext;
    private ArrayList<AmbassadorModel>ambassadorModels;


    public AmbassadorProgramAdapter(Context mContext,ArrayList<AmbassadorModel>ambassadorModels){
        this.mContext=mContext;
        this.ambassadorModels = ambassadorModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(AmbassadorListItemBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
     holder.AmbassadorListItemBinding.rIvAmbassador.setBackgroundResource(ambassadorModels.get(position).getImage());
     holder.AmbassadorListItemBinding.tvName.setText(ambassadorModels.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return ambassadorModels.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        AmbassadorListItemBinding AmbassadorListItemBinding;

        public MyViewHolder(@NonNull AmbassadorListItemBinding binding) {
            super(binding.getRoot());
            AmbassadorListItemBinding = binding;
            
        }
    }

}
