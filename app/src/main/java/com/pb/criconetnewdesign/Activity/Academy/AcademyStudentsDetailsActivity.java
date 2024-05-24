package com.pb.criconetnewdesign.Activity.Academy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.databinding.ActivityAcademyStudentsDetailsBinding;
import com.pb.criconetnewdesign.databinding.ToolbarInnerpageBinding;

public class AcademyStudentsDetailsActivity extends AppCompatActivity {
    ActivityAcademyStudentsDetailsBinding activityAcademyStudentsDetailsBinding;
    Context mContext;
    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAcademyStudentsDetailsBinding = ActivityAcademyStudentsDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityAcademyStudentsDetailsBinding.getRoot());
        mContext = this;
        mActivity = this;

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityAcademyStudentsDetailsBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.students_details));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        inItView();
    }

    private void inItView() {

    }
}