package com.pb.criconet.CommonUI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.pb.criconet.R;
import com.pb.criconet.adapter.AmbassadorProgramAdapter;
import com.pb.criconet.databinding.ActivityAmbassadorProgramBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.util.Global;


public class AmbassadorProgramActivity extends AppCompatActivity {
    ActivityAmbassadorProgramBinding activityAmbassadorProgramBinding;
    Context mContext;
    Activity mActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAmbassadorProgramBinding = ActivityAmbassadorProgramBinding.inflate(getLayoutInflater());
        setContentView(activityAmbassadorProgramBinding.getRoot());
        mContext = this;
        mActivity = this;

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityAmbassadorProgramBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(R.string.ambassador_program);
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> {
            finish();
        });

        inItView();


    }

    private void inItView() {
        activityAmbassadorProgramBinding.rvAmbassador.setHasFixedSize(true);
        activityAmbassadorProgramBinding.rvAmbassador.setLayoutManager(new GridLayoutManager(mActivity,2));
        activityAmbassadorProgramBinding.rvAmbassador.setAdapter(new AmbassadorProgramAdapter(mContext, Global.getAmbassadorList()));

        activityAmbassadorProgramBinding.flScoreboard.setOnClickListener(v -> {
        startActivity(new Intent(mContext,AmbassadorScoreBoardActivity.class));
        });

        activityAmbassadorProgramBinding.flViewForm.setOnClickListener(v -> {
            startActivity(new Intent(mContext,AmbassadorFormActivity.class));
        });
    }
}