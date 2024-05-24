package com.pb.criconetnewdesign.CommonUI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;
import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.adapter.AmbassadorProgramAdapter;
import com.pb.criconetnewdesign.databinding.ActivityAmbassadorProgramBinding;
import com.pb.criconetnewdesign.databinding.ToolbarInnerpageBinding;
import com.pb.criconetnewdesign.util.Global;

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