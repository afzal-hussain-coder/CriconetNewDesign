package com.pb.criconet.Activity.Academy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.applandeo.materialcalendarview.EventDay;
import com.pb.criconet.R;
import com.pb.criconet.databinding.ActivityViewAttendanceBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.model.AcademyModel.AttendanceReport;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.EventDecorator;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.SessionManager;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import timber.log.Timber;

public class ViewAttendanceActivity extends AppCompatActivity {

    ActivityViewAttendanceBinding activityViewAttendanceBinding;
    Context mContext;
    Activity mActivity;
    ToolbarInnerpageBinding toolbarInnerpageBinding;
    private RequestQueue queue;
    CustomLoaderView loaderView;
    private SharedPreferences prefs;

    private Calendar[] days = new Calendar[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityViewAttendanceBinding = ActivityViewAttendanceBinding.inflate(getLayoutInflater());
        setContentView(activityViewAttendanceBinding.getRoot());
        mContext = this;
        mActivity = this;

        toolbarInnerpageBinding = activityViewAttendanceBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.view_attandence));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        loaderView = CustomLoaderView.initialize(mContext);
        queue = Volley.newRequestQueue(mContext);


        if (Global.isOnline(mContext)) {
            getAcademyStudentsList();
        } else {
            Global.showDialog(mActivity);
        }
    }



    private void getAcademyStudentsList() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.ACADEMY_ATTENDANCE_REPORT_BY_STUDENT, response -> {
            Timber.d(response);
            loaderView.hideLoader();

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("status").equalsIgnoreCase("200")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    ArrayList<AttendanceReport> studentList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        AttendanceReport academyStudenModel = new AttendanceReport(jsonArray.getJSONObject(i));
                        studentList.add(academyStudenModel);
                    }


                    HashSet<CalendarDay> presentDays = new HashSet<>();
                    HashSet<CalendarDay> absentDays = new HashSet<>();

                    for (int i = 0; i < studentList.size(); i++) {
                        try {
                            CalendarDay day = CalendarDay.from(Global.convertStringToCalendar(studentList.get(i).getAttendance_date()));

                            String attendanceStatus = studentList.get(i).getAttendance_status();
                            if ("P".equals(attendanceStatus)) {
                                presentDays.add(day);
                            } else if ("A".equals(attendanceStatus)) {
                                absentDays.add(day);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    // Create and apply decorators
                    activityViewAttendanceBinding.calendarView.addDecorator(new EventDecorator(getResources().getColor(R.color.indicator_selector), presentDays,"P",mContext)); // Present days
                    activityViewAttendanceBinding.calendarView.addDecorator(new EventDecorator(getResources().getColor(R.color.app_light_red), absentDays,"A",mContext));
                    //activityViewAttendanceBinding.calendarView.addDecorator(absentDecorator);

                } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                    Global.msgDialog(mActivity, jsonObject.optJSONObject("errors").optString("error_text"));
                } else {
                    Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

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
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("academy_id", SessionManager.get_academyId(prefs));
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }
}