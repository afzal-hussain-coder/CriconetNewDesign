package com.pb.criconet.adapter.AcademyAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.criconet.Activity.Academy.AcademyAttendanceDetailsActivity;
import com.pb.criconet.databinding.AttendanceChildBinding;
import com.pb.criconet.model.AcademyModel.AttendanceReportViewChild;
import com.pb.criconet.model.AmbassadorModel;
import com.pb.criconet.util.Global;

import java.util.ArrayList;

public class AttendanceListAdapter extends RecyclerView.Adapter<AttendanceListAdapter.MyViewHolder>{
    private Context mContext;
    private ArrayList<AttendanceReportViewChild> academyAttendanceReportList;


    public AttendanceListAdapter(Context mContext, ArrayList<AttendanceReportViewChild> academyAttendanceReportList){
        this.mContext=mContext;
        this.academyAttendanceReportList = academyAttendanceReportList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(AttendanceChildBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        AttendanceReportViewChild  attendanceReportViewChild = academyAttendanceReportList.get(position);

        holder.attendanceChildBinding.tvAbsent.setText(attendanceReportViewChild.getAbsent());
        holder.attendanceChildBinding.tvPresent.setText(attendanceReportViewChild.getPresent());
        holder.attendanceChildBinding.date.setText(Global.convertUTCDateToLocall(attendanceReportViewChild.getAttendance_date()));

        holder.attendanceChildBinding.tvViewDetails.setOnClickListener(v -> {

            mContext.startActivity(new Intent(mContext, AcademyAttendanceDetailsActivity.class).putExtra("Date",attendanceReportViewChild.getAttendance_date())
                    .putExtra("type","all"));

        });
    }

    @Override
    public int getItemCount() {
        return academyAttendanceReportList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        AttendanceChildBinding attendanceChildBinding;

        public MyViewHolder(@NonNull AttendanceChildBinding binding) {
            super(binding.getRoot());
            attendanceChildBinding = binding;
            
        }
    }

}
