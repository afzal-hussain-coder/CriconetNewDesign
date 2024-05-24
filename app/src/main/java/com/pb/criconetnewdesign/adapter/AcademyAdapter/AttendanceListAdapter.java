package com.pb.criconetnewdesign.adapter.AcademyAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.criconetnewdesign.Activity.Academy.AcademyAttendanceDetailsActivity;
import com.pb.criconetnewdesign.databinding.AmbassadorListItemBinding;
import com.pb.criconetnewdesign.databinding.AttendanceChildBinding;
import com.pb.criconetnewdesign.model.AmbassadorModel;

import java.util.ArrayList;

public class AttendanceListAdapter extends RecyclerView.Adapter<AttendanceListAdapter.MyViewHolder>{
    private Context mContext;
    private ArrayList<AmbassadorModel>ambassadorModels;


    public AttendanceListAdapter(Context mContext){
        this.mContext=mContext;
        this.ambassadorModels = ambassadorModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(AttendanceChildBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//     holder.attendanceChildBinding.rIvAmbassador.setBackgroundResource(ambassadorModels.get(position).getImage());
//     holder.attendanceChildBinding.tvName.setText(ambassadorModels.get(position).getText());

        holder.attendanceChildBinding.tvViewDetails.setOnClickListener(v -> {
            mContext.startActivity(new Intent(mContext, AcademyAttendanceDetailsActivity.class));
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        AttendanceChildBinding attendanceChildBinding;

        public MyViewHolder(@NonNull AttendanceChildBinding binding) {
            super(binding.getRoot());
            attendanceChildBinding = binding;
            
        }
    }

}
