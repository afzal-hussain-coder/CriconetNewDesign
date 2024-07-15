package com.pb.criconet.adapter.AcademyAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.criconet.databinding.ManageStudentsChildBinding;
import com.pb.criconet.model.AmbassadorModel;
import java.util.ArrayList;

public class ManageStudentsAdapter extends RecyclerView.Adapter<ManageStudentsAdapter.MyViewHolder>{
    private Context mContext;
    private ArrayList<AmbassadorModel>ambassadorModels;
    private onItemClick onItemClick;


    public ManageStudentsAdapter(Context mContext, ArrayList<AmbassadorModel>ambassadorModels,onItemClick onItemClick){
        this.mContext=mContext;
        this.ambassadorModels = ambassadorModels;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ManageStudentsChildBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//     holder.AmbassadorListItemBinding.rIvAmbassador.setBackgroundResource(ambassadorModels.get(position).getImage());
//     holder.AmbassadorListItemBinding.tvName.setText(ambassadorModels.get(position).getText());

        holder.manageStudentsChildBinding.flKnowMore.setOnClickListener(v -> {
            onItemClick.knowMore();
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        ManageStudentsChildBinding manageStudentsChildBinding;

        public MyViewHolder(@NonNull ManageStudentsChildBinding binding) {
            super(binding.getRoot());
            manageStudentsChildBinding = binding;
            
        }
    }

    public interface onItemClick{
        void knowMore();
    }

}
