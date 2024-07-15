package com.pb.criconet.Activity.Coach;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.pb.criconet.CustomeCamera.CustomeCameraActivity;
import com.pb.criconet.R;
import com.pb.criconet.adapter.EcoachingAdapter.ViewPagerAdapter;
import com.pb.criconet.databinding.ActivityRegisterAsAcocahProfileBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;


public class RegisterAsACoachProfileActivity extends AppCompatActivity {
    ActivityRegisterAsAcocahProfileBinding activityRegisterAsAcocahProfileBinding;
    Context mContext;
    Activity mActivity;
    ViewPagerAdapter viewPagerAdapter;
    public static Uri image_uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRegisterAsAcocahProfileBinding = ActivityRegisterAsAcocahProfileBinding.inflate(getLayoutInflater());
        setContentView(activityRegisterAsAcocahProfileBinding.getRoot());

        mContext = this;
        mActivity = this;

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityRegisterAsAcocahProfileBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.my_profile));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        inItView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != image_uri) {
            activityRegisterAsAcocahProfileBinding.profilePic.setImageURI(image_uri);
        }
    }

    private void inItView() {

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        activityRegisterAsAcocahProfileBinding.viewPager.setAdapter(viewPagerAdapter);
        activityRegisterAsAcocahProfileBinding.tabLayout.setupWithViewPager(activityRegisterAsAcocahProfileBinding.viewPager);
        activityRegisterAsAcocahProfileBinding.tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.purple_700));


        activityRegisterAsAcocahProfileBinding.tvChangePhoto.setOnClickListener(v -> {
            startActivity(new Intent(mContext, CustomeCameraActivity.class)
                    .putExtra("FROM","RegisterAsACoachProfileActivity"));
        });
    }
}