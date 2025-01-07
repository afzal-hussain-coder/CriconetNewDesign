package com.pb.criconet.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.pb.criconet.R;
import com.pb.criconet.databinding.ActivityNoticeBoardDetailsBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.model.NoticeBoardModel;
import com.pb.criconet.util.Toaster;


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

        if (getIntent() != null) {
            Intent intent = getIntent();
            NoticeBoardModel noticeBoardModel = (NoticeBoardModel) intent.getSerializableExtra("NoticeBoard");

            if (noticeBoardModel != null) {
                setData(noticeBoardModel);
            }
        }

        inItView();
    }

    private void inItView() {
        ToolbarInnerpageBinding toolbarInnerpageBinding = activityNoticeBoardDetailsBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(getResources().getString(R.string.notice_boar));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

    }

    public void setData(NoticeBoardModel noticeBoardModel) {
        activityNoticeBoardDetailsBinding.tvNoticeBoardTitle.setText(noticeBoardModel.getTitle());
        activityNoticeBoardDetailsBinding.tvNoticeType.setText("Category : "+" "+noticeBoardModel.getNotic_type());
        activityNoticeBoardDetailsBinding.tvBlogDetails.setText(noticeBoardModel.getDetails());
        activityNoticeBoardDetailsBinding.tvBy.setText("By: "+" "+noticeBoardModel.getName());
        activityNoticeBoardDetailsBinding.tvViewsCount.setText("0 View");

    }
}