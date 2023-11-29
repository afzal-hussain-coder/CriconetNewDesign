package com.pb.criconetnewdesign.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;

import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.adapter.PavilionAdapter.BlogCommentsAdapter;
import com.pb.criconetnewdesign.databinding.ActivityNoticeBoardDetailsBinding;
import com.pb.criconetnewdesign.databinding.ToolbarInnerpageBinding;

import java.util.Objects;

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