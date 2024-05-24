package com.pb.criconetnewdesign.Activity.Academy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.adapter.AcademyAdapter.AttendanceDetailsListAdapter;
import com.pb.criconetnewdesign.adapter.AcademyAdapter.TakeAttendanceListAdapter;
import com.pb.criconetnewdesign.databinding.ActivityAcademyAttendanceDetailsBinding;
import com.pb.criconetnewdesign.databinding.ToolbarInnerpageBinding;
import com.pb.criconetnewdesign.util.Global;

public class AcademyAttendanceDetailsActivity extends AppCompatActivity {

    ActivityAcademyAttendanceDetailsBinding activityAcademyAttendanceDetailsBinding;

    Context mContext;
    Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAcademyAttendanceDetailsBinding = ActivityAcademyAttendanceDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityAcademyAttendanceDetailsBinding.getRoot());

        mContext = this;
        mActivity = this;

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityAcademyAttendanceDetailsBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.attendance));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        iniTView();
    }

    private void iniTView() {
        activityAcademyAttendanceDetailsBinding.rcvTakeAttendance.setHasFixedSize(true);
        activityAcademyAttendanceDetailsBinding.rcvTakeAttendance.setLayoutManager(new LinearLayoutManager(mContext));
        activityAcademyAttendanceDetailsBinding.rcvTakeAttendance.setAdapter(new AttendanceDetailsListAdapter(mContext, Global.getTakeAttendanceList()));

    }
}