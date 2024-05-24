package com.pb.criconetnewdesign.Activity.Academy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.databinding.ActivityManageAcademyCoachDetailsBinding;
import com.pb.criconetnewdesign.databinding.ToolbarInnerpageBinding;

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