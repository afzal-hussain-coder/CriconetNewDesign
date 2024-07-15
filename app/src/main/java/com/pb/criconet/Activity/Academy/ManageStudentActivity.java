package com.pb.criconet.Activity.Academy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.pb.criconet.R;
import com.pb.criconet.adapter.AcademyAdapter.ManageStudentsAdapter;
import com.pb.criconet.databinding.ActivityManageStudentBinding;
import com.pb.criconet.util.Global;

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