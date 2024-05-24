package com.pb.criconetnewdesign.Activity.Academy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.databinding.ActivityScheduleOnlineAcativityBinding;
import com.pb.criconetnewdesign.databinding.ToolbarInnerpageBinding;
import com.pb.criconetnewdesign.util.Global;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ScheduleOnlineAcativity extends AppCompatActivity {
    ActivityScheduleOnlineAcativityBinding activityScheduleOnlineAcativityBinding;
    Context mContext;
    Activity mActivity;

    private int year, month, day, hour, minute;
    String date = "";
    Date _selectedDate;
    Date _currentDate;
    String aTime = "";
    String currentDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityScheduleOnlineAcativityBinding = ActivityScheduleOnlineAcativityBinding.inflate(getLayoutInflater());
        setContentView(activityScheduleOnlineAcativityBinding.getRoot());
        mContext = this;
        mActivity = this;

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityScheduleOnlineAcativityBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(R.string.schedule_on);
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> {
            finish();
        });

        inItView();
    }

    private void inItView() {
        activityScheduleOnlineAcativityBinding.rlStartDate.setOnClickListener(v -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            final Calendar c = Calendar.getInstance();

            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            currentDate = year + "-" + (month + 1) + "-" + day;

            Calendar twoDaysLater = (Calendar) c.clone();
            twoDaysLater.add(Calendar.DATE, 90);

            DatePickerDialog picker = new DatePickerDialog(mActivity, (datePicker, year, month, day) -> {
                date = year + "-" + (month + 1) + "-" + day;

                try {
                    _currentDate = simpleDateFormat.parse(currentDate);
                    _selectedDate = simpleDateFormat.parse(date);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                activityScheduleOnlineAcativityBinding.tvStartDate.setText(Global.convertUTCDateToMM(date));
            }, year, month, day);
            picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            picker.getDatePicker().setMaxDate(twoDaysLater.getTimeInMillis());

            picker.show();
        });

        activityScheduleOnlineAcativityBinding.rlStartTime.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(mActivity, (timePicker, hour, min) -> {
                Calendar datetime = Calendar.getInstance();
                Calendar c1 = Calendar.getInstance();
                datetime.set(Calendar.HOUR_OF_DAY, hour);
                datetime.set(Calendar.MINUTE, min);

                updateTime(hour, min,activityScheduleOnlineAcativityBinding.tvStartTime);


            }, hour, minute, false);

            timePickerDialog.show();
        });


        String[] hours = getResources().getStringArray(R.array.hours);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.dropdown_item, hours);
        activityScheduleOnlineAcativityBinding.autoCompleteTextViewSelectHours.setAdapter(arrayAdapter);
        activityScheduleOnlineAcativityBinding.autoCompleteTextViewSelectHours.setDropDownBackgroundDrawable(getResources().getDrawable(R.drawable.spinner_bg));


        String[] minutes = getResources().getStringArray(R.array.minute);
        ArrayAdapter arrayAdapterr = new ArrayAdapter(this, R.layout.dropdown_item, minutes);
        activityScheduleOnlineAcativityBinding.autoCompleteTextViewSelectMinute.setAdapter(arrayAdapterr);
        activityScheduleOnlineAcativityBinding.autoCompleteTextViewSelectMinute.setDropDownBackgroundDrawable(getResources().getDrawable(R.drawable.spinner_bg));
    }

    private void updateTime(int hours, int mins,TextView tv_time) {
        String timeSet;
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";
        String minutes;
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);
        aTime = String.valueOf(hours) + ':' + minutes + " " + timeSet;
        tv_time.setText(aTime);

    }
}