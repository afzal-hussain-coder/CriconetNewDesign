package com.pb.criconet.adapter.AcademyAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.criconet.databinding.ManageCoachChildBinding;
import com.pb.criconet.model.AmbassadorModel;
import java.util.ArrayList;

public class ManageCoachAdapter extends RecyclerView.Adapter<ManageCoachAdapter.MyViewHolder>{
    private Context mContext;
    private ArrayList<AmbassadorModel>ambassadorModels;
    private onItemClick onItemClick;


    public ManageCoachAdapter(Context mContext, ArrayList<AmbassadorModel>ambassadorModels, onItemClick onItemClick){
        this.mContext=mContext;
        this.ambassadorModels = ambassadorModels;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ManageCoachChildBinding.inflate(LayoutInflater.from(mContext), parent, false));
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

        ManageCoachChildBinding manageStudentsChildBinding;

        public MyViewHolder(@NonNull ManageCoachChildBinding binding) {
            super(binding.getRoot());
            manageStudentsChildBinding = binding;
            
        }
    }

    public interface onItemClick{
        void knowMore();
    }

}
