package com.pb.criconet.Activity.Academy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.pb.criconet.R;
import com.pb.criconet.databinding.ActivityManageAcademyCoachDetailsBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;

public class ManageAcademyCoachDetailsActivity extends AppCompatActivity {
    ActivityManageAcademyCoachDetailsBinding activityManageAcademyCoachDetailsBinding;
    Context mContext;
    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityManageAcademyCoachDetailsBinding = ActivityManageAcademyCoachDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityManageAcademyCoachDetailsBinding.getRoot());

        mContext = this;
        mActivity = this;

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityManageAcademyCoachDetailsBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.coach_details));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());


        inItView();
    }

    private void inItView() {

    }
}