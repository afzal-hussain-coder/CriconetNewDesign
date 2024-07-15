package com.pb.criconet.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.pb.criconet.R;
import com.pb.criconet.databinding.ActivityNoticeBoardDetailsBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;


public class NoticeBoardDetailsActivity extends AppCompatActivity {

    ActivityNoticeBoardDetailsBinding activityNoticeBoardDetailsBinding;
    Context mContext;
    Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityNoticeBoardDetailsBinding = ActivityNoticeBoardDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityNoticeBoardDetailsBinding.getRoot());

        mContext = this;
        mActivity = this;

        inItView();
    }
    private void inItView() {
        ToolbarInnerpageBinding toolbarInnerpageBinding = activityNoticeBoardDetailsBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(getResources().getString(R.string.notice_boar));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());


    }
}