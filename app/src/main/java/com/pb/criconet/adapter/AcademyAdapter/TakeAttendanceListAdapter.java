package com.pb.criconet.adapter.AcademyAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.criconet.R;
import com.pb.criconet.databinding.TakeAttendanceChildBinding;
import com.pb.criconet.util.DataModel;
import com.pb.criconet.util.DropDownBlue;
import java.util.ArrayList;

public class TakeAttendanceListAdapter extends RecyclerView.Adapter<TakeAttendanceListAdapter.MyViewHolder>{
    private Context mContext;
    private ArrayList<String>ambassadorModels;
    private ArrayList<DataModel> option_attendance_status = new ArrayList<>();


    public TakeAttendanceListAdapter(Context mContext,ArrayList<String>ambassadorModels){
        this.mContext=mContext;
        this.ambassadorModels = ambassadorModels;

        option_attendance_status.add(new DataModel(mContext.getResources().getString(R.string.present)));
        option_attendance_status.add(new DataModel(mContext.getResources().getString(R.string.absent)));

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(TakeAttendanceChildBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

     holder.takeAttendanceChildBinding.tvStudentName.setText(ambassadorModels.get(position));

        holder.takeAttendanceChildBinding.dropAttendanceStatus.setOptionList(option_attendance_status);
        holder.takeAttendanceChildBinding.dropAttendanceStatus.setClickListener(new DropDownBlue.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name) {

                if(name.equalsIgnoreCase(mContext.getResources().getString(R.string.present))){
                    holder.takeAttendanceChildBinding.dropAttendanceStatus.setTextColor(mContext.getResources().getColor(R.color.indicator_selector));
                }else{
                    holder.takeAttendanceChildBinding.dropAttendanceStatus.setTextColor(mContext.getResources().getColor(R.color.purple_700));
                }

            }


            @Override
            public void onDismiss() {
            }
        });
    }

    @Override
    public int getItemCount() {
        return ambassadorModels.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        TakeAttendanceChildBinding takeAttendanceChildBinding;

        public MyViewHolder(@NonNull TakeAttendanceChildBinding binding) {
            super(binding.getRoot());
            takeAttendanceChildBinding = binding;
            
        }
    }

}
