package com.pb.criconet.Activity.Academy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.pb.criconet.R;
import com.pb.criconet.adapter.AcademyAdapter.ManageCoachAdapter;
import com.pb.criconet.databinding.ActivityManageAcademyCoachBinding;
import com.pb.criconet.util.Global;

public class ManageAcademyCoachActivity extends AppCompatActivity {
    ActivityManageAcademyCoachBinding activityManageAcademyCoachBinding;
    Context mContext;
    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityManageAcademyCoachBinding = ActivityManageAcademyCoachBinding.inflate(getLayoutInflater());
        setContentView(activityManageAcademyCoachBinding.getRoot());

        mContext = this;
        mActivity = this;

        inItView();

    }

    private void inItView() {

        activityManageAcademyCoachBinding.imgBack.setOnClickListener(v -> {
            finish();
        });
        activityManageAcademyCoachBinding.toolbartext.setText(mContext.getResources().getString(R.string.manage_coac));

        activityManageAcademyCoachBinding.rvAcademyCoach.setHasFixedSize(true);
        activityManageAcademyCoachBinding.rvAcademyCoach.setLayoutManager(new GridLayoutManager(mActivity,2));
        activityManageAcademyCoachBinding.rvAcademyCoach.setAdapter(new ManageCoachAdapter(mContext, Global.getAmbassadorList(), () -> {
            startActivity(new Intent(mContext,ManageAcademyCoachDetailsActivity.class));

        }));

        activityManageAcademyCoachBinding.flAddCoach.setOnClickListener(v -> {
         startActivity(new Intent(mContext,AcademyAddCoachActivity.class));
        });


    }
}