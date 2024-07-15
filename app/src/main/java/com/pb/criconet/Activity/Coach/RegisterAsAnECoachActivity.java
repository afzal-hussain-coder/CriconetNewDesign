package com.pb.criconet.Activity.Coach;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.pb.criconet.R;
import com.pb.criconet.adapter.EcoachingAdapter.ViewPagerAdapter;
import com.pb.criconet.databinding.ActivityRegisterAsAnEcoachBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;


public class RegisterAsAnECoachActivity extends AppCompatActivity {

    ActivityRegisterAsAnEcoachBinding activityRegisterAsAnEcoachBinding;
    Context mContext;
    Activity mActivity;
    ViewPagerAdapter viewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRegisterAsAnEcoachBinding = ActivityRegisterAsAnEcoachBinding.inflate(getLayoutInflater());
        setContentView(activityRegisterAsAnEcoachBinding.getRoot());
        mContext = this;
        mActivity = this;

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityRegisterAsAnEcoachBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.regsiter_as_a_coach));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        initView();
    }

    private void initView() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        activityRegisterAsAnEcoachBinding.viewPager.setAdapter(viewPagerAdapter);
        activityRegisterAsAnEcoachBinding.tabLayout.setupWithViewPager(activityRegisterAsAnEcoachBinding.viewPager);
        activityRegisterAsAnEcoachBinding.tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.purple_700));

    }
}