package com.pb.criconetnewdesign.Activity.User;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.databinding.ActivityAcademyDetailsBinding;
import com.pb.criconetnewdesign.databinding.ActivityUserBookingDetailsBinding;
import com.pb.criconetnewdesign.databinding.ToolbarInnerpageBinding;

public class UserBookingDetails extends AppCompatActivity {

    ActivityUserBookingDetailsBinding activityUserBookingDetailsBinding;
    Context mContext;
    Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserBookingDetailsBinding = ActivityUserBookingDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityUserBookingDetailsBinding.getRoot());
        mContext = this;
        mActivity = this;

        inItView();
    }

    private void inItView() {
        ToolbarInnerpageBinding toolbarInnerpageBinding = activityUserBookingDetailsBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(getResources().getString(R.string.booking_details));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}