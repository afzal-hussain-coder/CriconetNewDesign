package com.pb.criconetnewdesign.Activity.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.pb.criconetnewdesign.Activity.Coach.CoachFeedBackReviewActivity;
import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.adapter.EcoachingAdapter.UserBookingListAdapter;
import com.pb.criconetnewdesign.databinding.ActivityUserBookingHistoryBinding;
import com.pb.criconetnewdesign.databinding.ToolbarInnerpageBinding;
import com.pb.criconetnewdesign.util.BookingHistoryDropDown;
import com.pb.criconetnewdesign.util.DataModel;
import com.pb.criconetnewdesign.util.Global;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UserBookingHistory extends AppCompatActivity {
    ActivityUserBookingHistoryBinding activityUserBookingHistoryBinding;
    Context mContext;
    Activity mActivity;
    private ArrayList<DataModel> option = new ArrayList<>();
    private String filterType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserBookingHistoryBinding = ActivityUserBookingHistoryBinding.inflate(getLayoutInflater());
        setContentView(activityUserBookingHistoryBinding.getRoot());

        mContext = this;
        mActivity = this;

        inItView();
    }

    private void inItView() {
        ToolbarInnerpageBinding toolbarInnerpageBinding = activityUserBookingHistoryBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(getResources().getString(R.string.booking_history));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        activityUserBookingHistoryBinding.tvFromDate.setOnClickListener(v -> {
            openCalendar(activityUserBookingHistoryBinding.tvFromDate);
        });

        activityUserBookingHistoryBinding.tvToDate.setOnClickListener(v -> {
            openCalendar(activityUserBookingHistoryBinding.tvToDate);
        });


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
            public void onClickDone(String name) {

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
        activityUserBookingHistoryBinding.rvBooking.setAdapter(new UserBookingListAdapter(mContext, new UserBookingListAdapter.coachItemClickListener() {
            @Override
            public void viewDetails(int id) {
             startActivity(new Intent(mContext,UserBookingDetails.class));
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
        }));


    }

    public void openCalendar(TextView textView){
        // on below line we are getting
        // the instance of our calendar.
        final Calendar c = Calendar.getInstance();

        // on below line we are getting
        // our day, month and year.
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // on below line we are creating a variable for date picker dialog.
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                // on below line we are passing context.
                mContext,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    // on below line we are setting date to our text view.
                    textView.setText(Global.getDateGotCoachh(year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth));

                },
                // on below line we are passing year,
                // month and day for selected date in our date picker.
                year, month, day);
        // at last we are calling show to
        // display our date picker dialog.
        datePickerDialog.show();
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}