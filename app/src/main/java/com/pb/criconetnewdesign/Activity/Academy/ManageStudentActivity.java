package com.pb.criconetnewdesign.Activity.Academy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.adapter.AcademyAdapter.ManageStudentsAdapter;
import com.pb.criconetnewdesign.adapter.AmbassadorProgramAdapter;
import com.pb.criconetnewdesign.databinding.ActivityManageStudentBinding;
import com.pb.criconetnewdesign.databinding.ToolbarInnerpageBinding;
import com.pb.criconetnewdesign.util.Global;
import com.pb.criconetnewdesign.util.Toaster;

public class ManageStudentActivity extends AppCompatActivity {
    ActivityManageStudentBinding activityManageStudentBinding;
    Context mContext;
    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityManageStudentBinding = ActivityManageStudentBinding.inflate(getLayoutInflater());
        setContentView(activityManageStudentBinding.getRoot());
        mContext = this;
        mActivity = this;

        inItView();
    }

    private void inItView() {

        activityManageStudentBinding.imgBack.setOnClickListener(v -> {
            finish();
        });
        activityManageStudentBinding.toolbartext.setText(mContext.getResources().getString(R.string.manage_stud));

        activityManageStudentBinding.flAddStudents.setOnClickListener(v -> {
        startActivity(new Intent(mContext,AcademyAddStudentsActivity.class));
        });

        activityManageStudentBinding.rvAcademyStudents.setHasFixedSize(true);
        activityManageStudentBinding.rvAcademyStudents.setLayoutManager(new GridLayoutManager(mActivity,2));
        activityManageStudentBinding.rvAcademyStudents.setAdapter(new ManageStudentsAdapter(mContext, Global.getAmbassadorList(), () -> {
            startActivity(new Intent(mContext,AcademyStudentsDetailsActivity.class));

        }));


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}