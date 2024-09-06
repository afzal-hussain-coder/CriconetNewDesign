package com.pb.criconet.adapter.AcademyAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pb.criconet.R;
import com.pb.criconet.databinding.ManageCoachChildBinding;
import com.pb.criconet.model.AcademyModel.AcademyListModel;
import com.pb.criconet.model.AcademyModel.ManageCoachesModel;
import com.pb.criconet.util.Toaster;

import java.util.ArrayList;

public class AcademyCoachAdapter extends RecyclerView.Adapter<AcademyCoachAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<AcademyListModel.AcademyCoaches> coachesModelArrayList;
    private onItemClick onItemClick;


    public AcademyCoachAdapter(Context mContext, ArrayList<AcademyListModel.AcademyCoaches> coachesModelArrayList, onItemClick onItemClick) {
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
        AcademyListModel.AcademyCoaches manageCoachesModel = coachesModelArrayList.get(position);
        holder.manageStudentsChildBinding.tvName.setText(manageCoachesModel.getName());
        holder.manageStudentsChildBinding.tvSkills.setText(manageCoachesModel.getCat_title());


        Glide.with(mContext).load(manageCoachesModel.getAvatar()).error(R.drawable.coach_avtar).into(holder.manageStudentsChildBinding.rIvAvtar);



        holder.manageStudentsChildBinding.flKnowMore.setOnClickListener(v -> {
            onItemClick.knowMore(coachesModelArrayList.get(position));
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
        void knowMore(AcademyListModel.AcademyCoaches manageCoachesModel);
    }

}
