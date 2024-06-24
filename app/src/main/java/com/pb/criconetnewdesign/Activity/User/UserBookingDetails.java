package com.pb.criconetnewdesign.Activity.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.pb.criconetnewdesign.Activity.Coach.CancellationFeedbackFormActivity;
import com.pb.criconetnewdesign.Activity.MainActivity;
import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.databinding.ActivityAcademyDetailsBinding;
import com.pb.criconetnewdesign.databinding.ActivityUserBookingDetailsBinding;
import com.pb.criconetnewdesign.databinding.ToolbarInnerpageBinding;
import com.pb.criconetnewdesign.model.Coaching.BookingPaymentsDetails;
import com.pb.criconetnewdesign.util.CustomLoaderView;
import com.pb.criconetnewdesign.util.Global;
import com.pb.criconetnewdesign.util.SessionManager;
import com.pb.criconetnewdesign.util.Toaster;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserBookingDetails extends AppCompatActivity {

    ActivityUserBookingDetailsBinding activityUserBookingDetailsBinding;
    Context mContext;
    Activity mActivity;
    private RequestQueue queue;
    private SharedPreferences prefs;
    CustomLoaderView loaderView;

    String bookingId, fromWhere = "";
    BookingPaymentsDetails bookingPaymentsDetails;
    /*Cancel booking attributes*/
    long currentTimeMilliSeconds;
    long startSessionTimeMilliSeconds;
    long differenceBetweenMillisecond;
    int hours = 0;
    float refundAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserBookingDetailsBinding = ActivityUserBookingDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityUserBookingDetailsBinding.getRoot());
        mContext = this;
        mActivity = this;
        loaderView = CustomLoaderView.initialize(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        queue = Volley.newRequestQueue(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            fromWhere = bundle.getString("FROM");

            if (fromWhere.equalsIgnoreCase("1")) {
                bookingPaymentsDetails = (BookingPaymentsDetails) getIntent().getSerializableExtra("Data");
                bookingDetails(bookingPaymentsDetails);
            } else {
                bookingId = bundle.getString("BookingID");


                if (Global.isOnline(this)) {
                    getBookingDetails();
                } else {
                    Global.showDialog(this);
                }
            }
        }

        inItView();
    }

    private void inItView() {
        ToolbarInnerpageBinding toolbarInnerpageBinding = activityUserBookingDetailsBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(getResources().getString(R.string.booking_details));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        activityUserBookingDetailsBinding.flCancelBooking.setOnClickListener(v -> {

            currentTimeMilliSeconds = System.currentTimeMillis();


            String myDate = bookingPaymentsDetails.getOnlineSessionStartTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = sdf.parse(myDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            startSessionTimeMilliSeconds = date.getTime();


            differenceBetweenMillisecond = startSessionTimeMilliSeconds - currentTimeMilliSeconds;

            hours = (int) ((differenceBetweenMillisecond / 1000) / 3600);

            Log.d("CurrentTimeMiles", currentTimeMilliSeconds + "?" + startSessionTimeMilliSeconds + "hou" + hours);


            cancelAlertDialog(hours, bookingPaymentsDetails.getPaymentAmount());
        });

        activityUserBookingDetailsBinding.flBookAnotherSession.setOnClickListener(v -> {
            startActivity(new Intent(mContext, MainActivity.class).putExtra("type", 1).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        });
    }

    @SuppressLint("SetTextI18n")
    private void bookingDetails(BookingPaymentsDetails bookingPaymentsDetails) {

        bookingId = bookingPaymentsDetails.getId();
        try {
            if (!bookingPaymentsDetails.getAvatar().isEmpty()) {

                Glide.with(mActivity).load(bookingPaymentsDetails.getAvatar())
                        .into(activityUserBookingDetailsBinding.ivRoundedProfile);
            } else {
                Glide.with(mActivity).load(R.drawable.coachpic)
                        .into(activityUserBookingDetailsBinding.ivRoundedProfile);
            }

//            if (bookingPaymentsDetails.getVerified().equalsIgnoreCase("1")) {
//                iv_verified.setVisibility(View.VISIBLE);
//                if(bookingPaymentsDetails.getVerified().equalsIgnoreCase("1") &&
//                        bookingPaymentsDetails.getCriconet_verified().equalsIgnoreCase("1")){
//                    iv_verified.setColorFilter(ContextCompat.getColor(mActivity, R.color.colorPrimary));
//                }else{
//                    iv_verified.setColorFilter(ContextCompat.getColor(mActivity, R.color.verified_user_color));
//                }
//            } else {
//                iv_verified.setVisibility(View.GONE);
//                iv_verified.setImageTintList(ContextCompat.getColorStateList(mActivity, R.color.bckground_light));
//            }

            activityUserBookingDetailsBinding.tvCoachName.setText(bookingPaymentsDetails.getCoachName());
            activityUserBookingDetailsBinding.tvBookingId.setText(getResources().getString(R.string.bookingId) + " " + bookingPaymentsDetails.getBooking_id());
            activityUserBookingDetailsBinding.tvSessionDate.setText(Global.convertUTCDateToLocalDate(bookingPaymentsDetails.getOnlineSessionStartTime()) + " , " + bookingPaymentsDetails.getBookingSlotTxt());

            activityUserBookingDetailsBinding.tvCoachPrice.setText(getResources().getString(R.string.price) + "\u20B9" +bookingPaymentsDetails.getPaymentAmount());

//            tvBookingDate.setText(Global.getDate(Long.parseLong(bookingPaymentsDetails.getBookingTime())));
//
//            if (paymentMethod.equalsIgnoreCase("PAYLATER")) {
//                li_paid.setVisibility(View.GONE);
//                li_paylater.setVisibility(View.VISIBLE);
//                tvSessionAmount_paylater.setText("\u20B9" + " " + bookingPaymentsDetails.getPaymentAmount());
//            } else if (paymentMethod.equalsIgnoreCase("PAID")) {
//                li_paid.setVisibility(View.VISIBLE);
//                li_paylater.setVisibility(View.GONE);
//                tvSessionAmount.setText("\u20B9" + " " + bookingPaymentsDetails.getPaymentAmount());
//            } else {
//                li_paid.setVisibility(View.VISIBLE);
//                li_paylater.setVisibility(View.GONE);
//                tvSessionAmount.setText("\u20B9" + " " + bookingPaymentsDetails.getPaymentAmount());
//            }


            if (bookingPaymentsDetails.getBookingStatus().equalsIgnoreCase("1")) {
                activityUserBookingDetailsBinding.tvBookingStatus.setText(getString(R.string.bookingConfirmed));
            } else {
                activityUserBookingDetailsBinding.tvBookingStatus.setText(getResources().getString(R.string.Booking_in_processing));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void cancelAlertDialog(int hour, String payableAmount) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_cancel_booking);
        dialog.setCancelable(false);

        TextView tvCancellationMessage = dialog.findViewById(R.id.tvCancellationMessage);
        TextView tvCancellationDeduction = dialog.findViewById(R.id.tvCancellationDeduction);

        int value = Integer.compare(hour, 48);
        int value1 = Integer.compare(hour, 12);


        if (value > 0) {

            if (SessionManager.get_profiletype(prefs).equalsIgnoreCase("coach")) {
                refundAmount = Float.parseFloat(payableAmount);
            } else {
                refundAmount = Float.parseFloat(payableAmount) - 200;
            }

            //tvCancellationDeduction.setText("** You are canceling the booking 48 hours in advance so you have to pay only cancellation fee \u20B9 200 **");

            tvCancellationMessage.setText("** You are going to cancel the booking 48 hours in advance, so \u20B9 200 will be deducted from your Payable Amount **");

        } else if (value1 > 0 || value == 0) {

            if (SessionManager.get_profiletype(prefs).equalsIgnoreCase("coach")) {
                refundAmount = Float.parseFloat(payableAmount);
            } else {
                refundAmount = Float.parseFloat(payableAmount) * 50 / 100;
            }

            tvCancellationMessage.setText("** You are going to cancel the booking before the scheduled time 48 hours and after the scheduled time 12 hours, so 50% of the fee is refunded to you **");

        } else if (value1 == 0 || value1 < 0) {

            if (SessionManager.get_profiletype(prefs).equalsIgnoreCase("coach")) {
                refundAmount = Float.parseFloat(payableAmount);
            } else {
                refundAmount = 0;
            }

            tvCancellationMessage.setText("** You are going to cancel the booking within 12 hours or less so no refund will be given to you **");
        }


        new Handler().postDelayed(() -> dialog.dismiss(), 45000);

        FrameLayout fl_no = dialog.findViewById(R.id.fl_no);
        fl_no.setOnClickListener(v -> dialog.dismiss());
        FrameLayout fl_yes = dialog.findViewById(R.id.fl_yes);
        fl_yes.setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(new Intent(mContext, CancellationFeedbackFormActivity.class)
                    .putExtra("BookingId", bookingId)
                    .putExtra("RefundAmount", refundAmount));

        });

        dialog.show();
    }

    private void getBookingDetails() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_booking_details", (Response.Listener<String>) response -> {
            Log.d("BookingDetails",response);
            loaderView.hideLoader();
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("api_text").equalsIgnoreCase("success")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    bookingPaymentsDetails = new BookingPaymentsDetails(jsonObject1);
                    bookingDetails(bookingPaymentsDetails);
                } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                    Toaster.customToast(jsonObject.optJSONObject("errors").optString("error_text"));
                } else {
                    Toaster.customToast(getResources().getString(R.string.socket_timeout));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }, (Response.ErrorListener) error -> {
            error.printStackTrace();
            loaderView.hideLoader();
            Global.msgDialog((Activity) mActivity, getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("booking_id", bookingId);
                Log.d("Param",param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}