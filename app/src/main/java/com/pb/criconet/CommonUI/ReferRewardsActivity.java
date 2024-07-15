package com.pb.criconet.CommonUI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.pb.criconet.R;
import com.pb.criconet.adapter.AmbassadorProgramAdapter;

import com.pb.criconet.databinding.ActivityReferRewardsBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.Toaster;

public class ReferRewardsActivity extends AppCompatActivity {
    ActivityReferRewardsBinding activityReferRewardsBinding;
    Context mContext;
    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityReferRewardsBinding = ActivityReferRewardsBinding.inflate(getLayoutInflater());
        setContentView(activityReferRewardsBinding.getRoot());
        mContext = this;
        mActivity = this;

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityReferRewardsBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(R.string.refer_rewards);
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> {
            finish();
        });

        inItView();
    }

    private void inItView() {
        activityReferRewardsBinding.rvAmbassador.setHasFixedSize(true);
        activityReferRewardsBinding.rvAmbassador.setLayoutManager(new GridLayoutManager(mActivity,2));
        activityReferRewardsBinding.rvAmbassador.setAdapter(new AmbassadorProgramAdapter(mContext, Global.getAmbassadorList()));

        activityReferRewardsBinding.ivCopy.setOnClickListener(view -> {
            if(activityReferRewardsBinding.tvCopy.getText().toString().trim().isEmpty()){
                Toaster.customToast("No Content for copy");
            }else{
                copyToClipBoard();
            }
        });

    }

    private void copyToClipBoard()
    {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(mActivity.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", activityReferRewardsBinding.tvCopy.getText());
        if (clipboard == null || clip == null) return;
        clipboard.setPrimaryClip(clip);
        if(clipboard.hasPrimaryClip()){
            activityReferRewardsBinding.tvCopied.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    activityReferRewardsBinding.tvCopied.setVisibility(View.GONE);
                }
            },1000);

        }
    }
}