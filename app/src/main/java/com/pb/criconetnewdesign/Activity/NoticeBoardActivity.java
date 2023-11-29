package com.pb.criconetnewdesign.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.adapter.PavilionAdapter.MyBlogsAdapter;
import com.pb.criconetnewdesign.adapter.PavilionAdapter.NoticeBoardAdapter;
import com.pb.criconetnewdesign.databinding.ActivityNoticeBoardBinding;
import com.pb.criconetnewdesign.databinding.ToolbarInnerpageBinding;

public class NoticeBoardActivity extends AppCompatActivity {

    ActivityNoticeBoardBinding activityNoticeBoardBinding;
    Context mContext;
    Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityNoticeBoardBinding = ActivityNoticeBoardBinding.inflate(getLayoutInflater());
        setContentView(activityNoticeBoardBinding.getRoot());
        mContext = this;
        mActivity = this;
        inItView();
    }
    private void inItView() {
        ToolbarInnerpageBinding toolbarInnerpageBinding = activityNoticeBoardBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(getResources().getString(R.string.notice_boar));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        activityNoticeBoardBinding.rcVNoticeBoard.setHasFixedSize(true);
        activityNoticeBoardBinding.rcVNoticeBoard.setLayoutManager(new LinearLayoutManager(mActivity));
        activityNoticeBoardBinding.rcVNoticeBoard.setAdapter(new NoticeBoardAdapter(mContext, () -> {
             startActivity(new Intent(mContext,NoticeBoardDetailsActivity.class));
        }));
    }
}