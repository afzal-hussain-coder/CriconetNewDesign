package com.pb.criconetnewdesign.Activity.Coach;



import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CalendarView;

import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.adapter.EcoachingAdapter.SessionTimeListAdapter;
import com.pb.criconetnewdesign.databinding.ActivityCoachDetailsBinding;
import com.pb.criconetnewdesign.databinding.ToolbarInnerpageBinding;
import com.pb.criconetnewdesign.util.Toaster;

public class CoachDetailsActivity extends AppCompatActivity {

    ActivityCoachDetailsBinding activityCoachDetailsBinding;
    Context mContext;
    Activity mActivity;
    Long date;
    Animation slide_down,slide_up;
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



        activityCoachDetailsBinding.rvSessionTime.setHasFixedSize(true);
        activityCoachDetailsBinding.rvSessionTime.setLayoutManager(new GridLayoutManager(mContext,2));
        activityCoachDetailsBinding.rvSessionTime.setAdapter(new SessionTimeListAdapter(mContext));

        activityCoachDetailsBinding.flBook.setOnClickListener(v -> {
            slide_down = AnimationUtils.loadAnimation(mContext,
                    R.anim.slide_down);
            activityCoachDetailsBinding.flBook.setVisibility(View.GONE);
            activityCoachDetailsBinding.liBookLayout.setAnimation(slide_down);
            activityCoachDetailsBinding.liBookLayout.setVisibility(View.VISIBLE);
        });

        activityCoachDetailsBinding.tvCheckavailability.setOnClickListener(v -> {
            slide_down = AnimationUtils.loadAnimation(mContext,
                    R.anim.slide_down);
            activityCoachDetailsBinding.flBook.setVisibility(View.GONE);
            activityCoachDetailsBinding.liBookLayout.setAnimation(slide_down);
            activityCoachDetailsBinding.liBookLayout.setVisibility(View.VISIBLE);
        });
        activityCoachDetailsBinding.calendorView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {

            date = activityCoachDetailsBinding.calendorView.getDate();
            //Toaster.customToast("Year=" + year + " Month=" + month + " Day=" + dayOfMonth);
            activityCoachDetailsBinding.liSessionLayout.setVisibility(View.VISIBLE);
        });

        activityCoachDetailsBinding.ivClose.setOnClickListener(v -> {
            slide_up = AnimationUtils.loadAnimation(mContext,
                    R.anim.slide_up);
            activityCoachDetailsBinding.liBookLayout.setAnimation(slide_up);
            activityCoachDetailsBinding.flBook.setVisibility(View.VISIBLE);


            activityCoachDetailsBinding.liBookLayout.setVisibility(View.INVISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    activityCoachDetailsBinding.liBookLayout.setVisibility(View.GONE);
                    activityCoachDetailsBinding.liSessionLayout.setVisibility(View.GONE);
                }
            },600);


        });

        activityCoachDetailsBinding.flFinalBook.setOnClickListener(v -> {
          startActivity(new Intent(mContext,CheckoutDetails.class));
        });
        activityCoachDetailsBinding.flAddToCart.setOnClickListener(v -> {

        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}