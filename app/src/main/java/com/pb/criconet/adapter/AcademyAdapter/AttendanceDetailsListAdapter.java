package com.pb.criconet.adapter.AcademyAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.criconet.R;
import com.pb.criconet.databinding.AttendanceDetailsChildBinding;
import com.pb.criconet.model.AcademyModel.AttendanceReport;

import java.util.ArrayList;

public class AttendanceDetailsListAdapter extends RecyclerView.Adapter<AttendanceDetailsListAdapter.MyViewHolder>{
    private Context mContext;
    private ArrayList<AttendanceReport> academyTipsPreviewModels;


    public AttendanceDetailsListAdapter(Context mContext, ArrayList<AttendanceReport> academyTipsPreviewModels){
        this.mContext=mContext;
        this.academyTipsPreviewModels = academyTipsPreviewModels;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(AttendanceDetailsChildBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

     holder.attendanceDetailsChildBinding.tvStudentName.setText(academyTipsPreviewModels.get(position).getName());

     if(academyTipsPreviewModels.get(position).getAttendance_status().equalsIgnoreCase("A")){
         holder.attendanceDetailsChildBinding.tvStudentStatus.setText(mContext.getResources().getString(R.string.absent));
     }else{
         holder.attendanceDetailsChildBinding.tvStudentStatus.setText(mContext.getResources().getString(R.string.present));
     }


    }

    @Override
    public int getItemCount() {
        return academyTipsPreviewModels.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        AttendanceDetailsChildBinding attendanceDetailsChildBinding;

        public MyViewHolder(@NonNull AttendanceDetailsChildBinding binding) {
            super(binding.getRoot());
            attendanceDetailsChildBinding = binding;
            
        }
    }

}
