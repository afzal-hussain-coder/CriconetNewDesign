package com.pb.criconet.util;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerUtils{

    public static void showDatePickerDialog(Context context, final OnDateSelectedListener listener) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                (view, year1, monthOfYear, dayOfMonth1) -> {
                    if (listener != null) {
                        listener.onDateSelected(dayOfMonth1, monthOfYear + 1, year1);
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    public interface OnDateSelectedListener {
        void onDateSelected(int day, int month, int year);
    }
}
