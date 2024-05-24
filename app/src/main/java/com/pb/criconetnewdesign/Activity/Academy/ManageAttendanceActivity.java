package com.pb.criconetnewdesign.Activity.Academy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.adapter.AcademyAdapter.AttendanceListAdapter;
import com.pb.criconetnewdesign.databinding.ActivityManageAttendanceBinding;
import com.pb.criconetnewdesign.databinding.ToolbarInnerpageBinding;

import java.util.ArrayList;

public class ManageAttendanceActivity extends AppCompatActivity {

    ActivityManageAttendanceBinding activityManageAttendanceBinding;
    Context mContext;
    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityManageAttendanceBinding = ActivityManageAttendanceBinding.inflate(getLayoutInflater());
        setContentView(activityManageAttendanceBinding.getRoot());

        mContext = this;
        mActivity = this;

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityManageAttendanceBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.manage_atte));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        inItView();

    }

    private void inItView() {

        activityManageAttendanceBinding.rcvAttendance.setHasFixedSize(true);
        activityManageAttendanceBinding.rcvAttendance.setLayoutManager(new LinearLayoutManager(mContext));
        activityManageAttendanceBinding.rcvAttendance.setAdapter(new AttendanceListAdapter(mContext));

        activityManageAttendanceBinding.tvTakeAttendance.setOnClickListener(v -> {
        startActivity(new Intent(mContext,AcademyTakeAttendanceActivity.class));
        });
    }
}