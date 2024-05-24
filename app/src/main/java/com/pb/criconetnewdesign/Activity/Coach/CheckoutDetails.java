package com.pb.criconetnewdesign.Activity.Coach;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.pb.criconetnewdesign.Activity.User.UserBookingDetails;
import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.databinding.ActivityCheckoutDetailsBinding;
import com.pb.criconetnewdesign.databinding.ToolbarInnerpageBinding;
import com.pb.criconetnewdesign.util.Toaster;

public class CheckoutDetails extends AppCompatActivity {

    ActivityCheckoutDetailsBinding activityCheckoutDetailsBinding;
    Context mContext;
    Activity mActivity;

    long MILI_SECONDS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCheckoutDetailsBinding = ActivityCheckoutDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityCheckoutDetailsBinding.getRoot());

        mContext = this;
        mActivity = this;

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityCheckoutDetailsBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.checkoutdetails));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        initView();
    }

    private void initView() {
        activityCheckoutDetailsBinding.tvSessionprice.setPaintFlags(activityCheckoutDetailsBinding.tvSessionprice.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        activityCheckoutDetailsBinding.tvSubTotal.setPaintFlags(activityCheckoutDetailsBinding.tvSubTotal.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        activityCheckoutDetailsBinding.tvTaxes.setPaintFlags(activityCheckoutDetailsBinding.tvTaxes.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        activityCheckoutDetailsBinding.tvTotalPayableAmount.setPaintFlags(activityCheckoutDetailsBinding.tvTotalPayableAmount.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        activityCheckoutDetailsBinding.flProceedToPay.setOnClickListener(v -> {
            MILI_SECONDS = 2000;
            activityCheckoutDetailsBinding.bookingConLayout.getRoot().setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    activityCheckoutDetailsBinding.bookingConLayout.getRoot().setVisibility(View.GONE);
                    startActivity(new Intent(mContext, UserBookingDetails.class));
                }
            },MILI_SECONDS);
            //activityCheckoutDetailsBinding.bookingFailedLayout.getRoot().setVisibility(View.VISIBLE);
        });

        activityCheckoutDetailsBinding.bookingConLayout.ivClose.setOnClickListener(v -> {
            activityCheckoutDetailsBinding.bookingConLayout.getRoot().setVisibility(View.GONE);
        });

        activityCheckoutDetailsBinding.bookingFailedLayout.ivClose.setOnClickListener(v -> {
            activityCheckoutDetailsBinding.bookingFailedLayout.getRoot().setVisibility(View.GONE);
        });


        activityCheckoutDetailsBinding.editApplyCoupon.setOnClickListener(v -> {

        });


        activityCheckoutDetailsBinding.editApplyCoupon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityCheckoutDetailsBinding.flCouponSubmit.setVisibility(View.VISIBLE);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityCheckoutDetailsBinding.flCouponSubmit.setVisibility(View.VISIBLE);

                }else if(s.length()==0){
                   // Toaster.customToast(s.length()+"");
                    activityCheckoutDetailsBinding.flCouponSubmit.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityCheckoutDetailsBinding.flCouponSubmit.setVisibility(View.VISIBLE);

            }
        });

        activityCheckoutDetailsBinding.flCouponSubmit.setOnClickListener(v -> {
         startActivity(new Intent(mContext,CoachFeedBackReviewActivity.class));
        });



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}