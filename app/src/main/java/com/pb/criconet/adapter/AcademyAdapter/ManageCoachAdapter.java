package com.pb.criconet.adapter.AcademyAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pb.criconet.R;
import com.pb.criconet.databinding.ManageCoachChildBinding;
import com.pb.criconet.model.AcademyModel.ManageCoachesModel;
import com.pb.criconet.model.AmbassadorModel;

import java.util.ArrayList;

public class ManageCoachAdapter extends RecyclerView.Adapter<ManageCoachAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<ManageCoachesModel> coachesModelArrayList;
    private onItemClick onItemClick;


    public ManageCoachAdapter(Context mContext, ArrayList<ManageCoachesModel> coachesModelArrayList, onItemClick onItemClick) {
        this.mContext = mContext;
        this.coachesModelArrayList = coachesModelArrayList;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ManageCoachChildBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ManageCoachesModel manageCoachesModel = coachesModelArrayList.get(position);
        holder.manageStudentsChildBinding.tvName.setText(manageCoachesModel.getName());
        holder.manageStudentsChildBinding.tvSkills.setText(manageCoachesModel.getCat_title());


        Glide.with(mContext).load(manageCoachesModel.getAvatar()).error(R.drawable.coach_avtar).into(holder.manageStudentsChildBinding.rIvAvtar);


        holder.manageStudentsChildBinding.flKnowMore.setOnClickListener(v -> {
            onItemClick.knowMore(manageCoachesModel);
        });
    }

    @Override
    public int getItemCount() {
        return coachesModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ManageCoachChildBinding manageStudentsChildBinding;

        public MyViewHolder(@NonNull ManageCoachChildBinding binding) {
            super(binding.getRoot());
            manageStudentsChildBinding = binding;

        }
    }

    public interface onItemClick {
        void knowMore(ManageCoachesModel manageCoachesModel);
    }

}
