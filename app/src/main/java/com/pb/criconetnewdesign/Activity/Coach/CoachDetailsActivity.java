package com.pb.criconetnewdesign.Activity.Coach;



import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.databinding.ActivityCoachDetailsBinding;
import com.pb.criconetnewdesign.databinding.ToolbarInnerpageBinding;

public class CoachDetailsActivity extends AppCompatActivity {

    ActivityCoachDetailsBinding activityCoachDetailsBinding;
    Context mContext;
    Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCoachDetailsBinding = ActivityCoachDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityCoachDetailsBinding.getRoot());
        mContext = this;
        mActivity = this;

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityCoachDetailsBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.coach_details));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        initView();


    }

    private void initView() {
        activityCoachDetailsBinding.ratingBar.setRating(5f);
        activityCoachDetailsBinding.tvCheckavailability.setPaintFlags(activityCoachDetailsBinding.tvCheckavailability.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}