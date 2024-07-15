package com.pb.criconet.Activity.Academy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.pb.criconet.R;
import com.pb.criconet.adapter.AcademyAdapter.TakeAttendanceListAdapter;
import com.pb.criconet.databinding.ActivityAcademyTakeAttendanceBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.util.Global;


public class AcademyTakeAttendanceActivity extends AppCompatActivity {
    ActivityAcademyTakeAttendanceBinding activityAcademyTakeAttendanceBinding;
    Context mContext;
    Activity mActivity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAcademyTakeAttendanceBinding = ActivityAcademyTakeAttendanceBinding.inflate(getLayoutInflater());
        setContentView(activityAcademyTakeAttendanceBinding.getRoot());

        mContext = this;
        mActivity = this;

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityAcademyTakeAttendanceBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.attendance));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        inItView();

    }

    private void inItView() {

        activityAcademyTakeAttendanceBinding.rcvTakeAttendance.setHasFixedSize(true);
        activityAcademyTakeAttendanceBinding.rcvTakeAttendance.setLayoutManager(new LinearLayoutManager(mContext));
        activityAcademyTakeAttendanceBinding.rcvTakeAttendance.setAdapter(new TakeAttendanceListAdapter(mContext, Global.getTakeAttendanceList()));

        activityAcademyTakeAttendanceBinding.flSubmit.setOnClickListener(v -> {});


    }
}