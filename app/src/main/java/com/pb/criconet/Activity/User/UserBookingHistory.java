package com.pb.criconet.Activity.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.pb.criconet.Activity.Coach.CallActivity;
import com.pb.criconet.Activity.Coach.CoachFeedBackReviewActivity;
import com.pb.criconet.R;
import com.pb.criconet.adapter.EcoachingAdapter.UserBookingListAdapter;
import com.pb.criconet.databinding.ActivityUserBookingHistoryBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.model.Coaching.BookingHistory;
import com.pb.criconet.model.Coaching.CoachAccept;
import com.pb.criconet.model.ConstantApp;
import com.pb.criconet.util.BookingHistoryDropDown;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.DataModel;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.SessionManager;
import com.pb.criconet.util.Toaster;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class UserBookingHistory extends AppCompatActivity {
    ActivityUserBookingHistoryBinding activityUserBookingHistoryBinding;
    Context mContext;
    Activity mActivity;
    private final ArrayList<DataModel> option = new ArrayList<>();
    private String filterType = "";
    private RequestQueue queue;
    private SharedPreferences prefs;
    CustomLoaderView loaderView;
    private String from_date = "";
    private String to_date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserBookingHistoryBinding = ActivityUserBookingHistoryBinding.inflate(getLayoutInflater());
        setContentView(activityUserBookingHistoryBinding.getRoot());

        mContext = this;
        mActivity = this;

        loaderView = CustomLoaderView.initialize(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        queue = Volley.newRequestQueue(this);

        inItView();

        if (Global.isOnline(this)) {
            getBookingHistory();
        } else {
            Global.showDialog(this);
        }
    }

    private void inItView() {
        ToolbarInnerpageBinding toolbarInnerpageBinding = activityUserBookingHistoryBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(getResources().getString(R.string.booking_history));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        activityUserBookingHistoryBinding.tvFromDate.setOnClickListener(v -> openCalendarFromDate(activityUserBookingHistoryBinding.tvFromDate));

        activityUserBookingHistoryBinding.tvToDate.setOnClickListener(v -> openCalendarToDate(activityUserBookingHistoryBinding.tvToDate));


        option.add(new DataModel("All Booking"));
        option.add(new DataModel("Cancelled booking"));
        option.add(new DataModel("Confirmed booking"));
        activityUserBookingHistoryBinding.dropBooking.setOptionList(option);
        filterType = option.get(0).getName();
        activityUserBookingHistoryBinding.dropBooking.setText(filterType);

        activityUserBookingHistoryBinding.dropBooking.setClickListener(new BookingHistoryDropDown.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name, String id) {

                if (name.equalsIgnoreCase("Cancelled booking")) {
                    filterType = "cancelled";
                } else if (name.equalsIgnoreCase("Confirmed booking")) {
                    filterType = "confirmed";
                } else {
                    filterType = name;
                }
            }


            @Override
            public void onDismiss() {
            }
        });


        activityUserBookingHistoryBinding.rvBooking.setHasFixedSize(true);
        activityUserBookingHistoryBinding.rvBooking.setLayoutManager(new LinearLayoutManager(mContext));


        activityUserBookingHistoryBinding.flApply.setOnClickListener(view -> {
            if (Global.isOnline(this)) {
                getBookingHistory();
            } else {
                Global.showDialog(this);
            }
        });

        activityUserBookingHistoryBinding.tvClear.setOnClickListener(view -> {
            activityUserBookingHistoryBinding.liApply.setVisibility(View.GONE);
            from_date = "";
            to_date = "";
            activityUserBookingHistoryBinding.tvFromDate.setText(mContext.getResources().getString(R.string.select_e_session_from_date));
            activityUserBookingHistoryBinding.tvToDate.setText(mContext.getResources().getString(R.string.Select_e_Session_to_Date));
            filterType = "";

            if (Global.isOnline(this)) {
                getBookingHistory();
            } else {
                Global.showDialog(this);
            }

        });


    }

    public void openCalendarFromDate(TextView textView) {

        final Calendar c = Calendar.getInstance();


        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(

                mContext,
                (view, year1, monthOfYear, dayOfMonth) -> {


                    from_date = year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                    //Toaster.customToast(from_date);

                    textView.setText(Global.getDateGotCoachh(year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth));

                },

                year, month, day);
        datePickerDialog.show();
    }

    public void openCalendarToDate(TextView textView) {
        final Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                mContext,
                (view, year1, monthOfYear, dayOfMonth) -> {

                    to_date = year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    //Toaster.customToast(to_date);

                    textView.setText(Global.getDateGotCoachh(year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth));

                    if (!from_date.isEmpty() && !to_date.isEmpty()) {
                        activityUserBookingHistoryBinding.liApply.setVisibility(View.VISIBLE);
                    }

                },

                year, month, day);
        datePickerDialog.show();
    }


    private void getBookingHistory() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_booking_history", response -> {
            Log.d("BookingResponse", response);
            loaderView.hideLoader();

            try {
                Gson gson = new Gson();
                BookingHistory modelArrayList = gson.fromJson(response, BookingHistory.class);

                if (modelArrayList.getData().isEmpty()) {
                    activityUserBookingHistoryBinding.notfound.setVisibility(View.VISIBLE);
                    activityUserBookingHistoryBinding.rvBooking.setVisibility(View.GONE);
                } else {
                    activityUserBookingHistoryBinding.notfound.setVisibility(View.GONE);
                    activityUserBookingHistoryBinding.rvBooking.setVisibility(View.VISIBLE);

                    activityUserBookingHistoryBinding.rvBooking.setAdapter(new UserBookingListAdapter(mContext, modelArrayList.getData(), new UserBookingListAdapter.coachItemClickListener() {
                        @Override
                        public void viewDetails(String bookingId) {
                            startActivity(new Intent(mContext, UserBookingDetails.class)
                                    .putExtra("BookingID", bookingId)
                                    .putExtra("FROM", "2"));
                        }

                        @Override
                        public void bookCoach(int id) {

                        }

                        @Override
                        public void shareCoach() {

                        }

                        @Override
                        public void sendFeedback(int id) {
                            startActivity(new Intent(mContext, CoachFeedBackReviewActivity.class));

                        }

                        @Override
                        public void buttonJoin(String id, long timeDuration, String action, String channel_id, String booking_id, String userid, String coachid, String coachName) {
                            if (action.equalsIgnoreCase("join")) {
                                Intent intent = new Intent(mContext, CallActivity.class);
                                intent.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME, channel_id);
                                intent.putExtra("UserId", userid);
                                intent.putExtra("CoachId", coachid);
                                intent.putExtra("booking_id", booking_id);
                                intent.putExtra("id", id);
                                intent.putExtra("timeDuration", timeDuration);
                                intent.putExtra("Name", coachName);
                                startActivity(intent);
                            } else {
                                validateAction(booking_id, action);
                            }
                        }
                    }));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }, error -> {
            error.printStackTrace();
            loaderView.hideLoader();
            Global.msgDialog((Activity) mContext, getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("bstatus", filterType);
                param.put("from_date", from_date);
                param.put("to_date", to_date);
                Log.e("Param", param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void validateAction(String booking_id, String action) {
        loaderView.showLoader();
//        progressDialog = Global.getProgressDialog(this, CCResource.getString(this, R.string.loading_dot), false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "booking_action", response -> {
            loaderView.hideLoader();
            //Global.dismissDialog(progressDialog);
            Gson gson = new Gson();
            CoachAccept modelArrayList = gson.fromJson(response, CoachAccept.class);
            Toaster.customToast(modelArrayList.getData().getMessage());

        }, error -> {
            error.printStackTrace();
            loaderView.hideLoader();
            //Global.dismissDialog(progressDialog);
            Global.msgDialog((Activity) mContext, getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("action", action);
                param.put("booking_id", booking_id);
                param.put("s", SessionManager.get_session_id(prefs));
                Timber.e(param.toString());
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