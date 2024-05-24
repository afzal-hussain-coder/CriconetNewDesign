package com.pb.criconetnewdesign.CommonUI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.databinding.ActivityAmbassadorScoreBoardBinding;
import com.pb.criconetnewdesign.databinding.ToolbarInnerpageBinding;

public class AmbassadorScoreBoardActivity extends AppCompatActivity {
    ActivityAmbassadorScoreBoardBinding activityAmbassadorScoreBoardBinding;
    Context mContext;
    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAmbassadorScoreBoardBinding = ActivityAmbassadorScoreBoardBinding.inflate(getLayoutInflater());
        setContentView(activityAmbassadorScoreBoardBinding.getRoot());
        mContext = this;
        mActivity = this;

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityAmbassadorScoreBoardBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(R.string.ambassador_scoreboard);
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> {
            finish();
        });

        inItView();
    }

    private void inItView() {

    }
}