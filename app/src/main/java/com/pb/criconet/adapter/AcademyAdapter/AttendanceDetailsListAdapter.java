package com.pb.criconet.adapter.AcademyAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.criconet.databinding.AttendanceDetailsChildBinding;

import java.util.ArrayList;

public class AttendanceDetailsListAdapter extends RecyclerView.Adapter<AttendanceDetailsListAdapter.MyViewHolder>{
    private Context mContext;
    private ArrayList<String>ambassadorModels;


    public AttendanceDetailsListAdapter(Context mContext, ArrayList<String>ambassadorModels){
        this.mContext=mContext;
        this.ambassadorModels = ambassadorModels;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(AttendanceDetailsChildBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

     holder.attendanceDetailsChildBinding.tvStudentName.setText(ambassadorModels.get(position));

    }

    @Override
    public int getItemCount() {
        return ambassadorModels.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        AttendanceDetailsChildBinding attendanceDetailsChildBinding;

        public MyViewHolder(@NonNull AttendanceDetailsChildBinding binding) {
            super(binding.getRoot());
            attendanceDetailsChildBinding = binding;
            
        }
    }

}
