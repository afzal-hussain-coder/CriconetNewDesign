package com.pb.criconet.Activity.Academy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pb.criconet.R;
import com.pb.criconet.adapter.AcademyAdapter.AttendanceDetailsListAdapter;
import com.pb.criconet.databinding.ActivityAcademyAttendanceDetailsBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.model.AcademyModel.AttendanceReport;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.SessionManager;
import com.pb.criconet.util.Toaster;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class AcademyAttendanceDetailsActivity extends AppCompatActivity {

    ActivityAcademyAttendanceDetailsBinding activityAcademyAttendanceDetailsBinding;

    Context mContext;
    Activity mActivity;
    String date = "";
    String type = "";

    private RequestQueue queue;
    CustomLoaderView loaderView;
    private SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAcademyAttendanceDetailsBinding = ActivityAcademyAttendanceDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityAcademyAttendanceDetailsBinding.getRoot());

        mContext = this;
        mActivity = this;

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityAcademyAttendanceDetailsBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.attendance));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        queue = Volley.newRequestQueue(mContext);
        loaderView = CustomLoaderView.initialize(mContext);
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        if (getIntent() != null) {
            date = getIntent().getStringExtra("Date");
            type = getIntent().getStringExtra("type");

        }

        iniTView();

        if (Global.isOnline(mContext)) {
            getAcademyStudentsList();
        } else {
            Global.showDialog(mActivity);
        }
    }

    private void iniTView() {
        activityAcademyAttendanceDetailsBinding.rcvTakeAttendance.setHasFixedSize(true);
        activityAcademyAttendanceDetailsBinding.rcvTakeAttendance.setLayoutManager(new LinearLayoutManager(mContext));

    }

    private void getAcademyStudentsList() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.ACADEMY_ATTENDANCE_LIST, response -> {
            Timber.d(response);
            loaderView.hideLoader();
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("status").equalsIgnoreCase("200")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    ArrayList<AttendanceReport> studentList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {

                        if (type.equalsIgnoreCase("all")) {
                            AttendanceReport academyStudenModel = new AttendanceReport(jsonArray.getJSONObject(i));
                            studentList.add(academyStudenModel);
                        } else if (type.equalsIgnoreCase("A")) {

                            if (jsonArray.getJSONObject(i).getString("attendance_status").equalsIgnoreCase("A")) {
                                AttendanceReport academyStudenModel = new AttendanceReport(jsonArray.getJSONObject(i));
                                studentList.add(academyStudenModel);
                            }


                        } else if (type.equalsIgnoreCase("P")) {
                            if (jsonArray.getJSONObject(i).getString("attendance_status").equalsIgnoreCase("P")) {
                                AttendanceReport academyStudenModel = new AttendanceReport(jsonArray.getJSONObject(i));
                                studentList.add(academyStudenModel);
                            }
                        }

                    }
                    if (studentList.isEmpty()) {

                        activityAcademyAttendanceDetailsBinding.rcvTakeAttendance.setVisibility(View.GONE);
                        activityAcademyAttendanceDetailsBinding.tvnotfound.setVisibility(View.VISIBLE);
                    } else {
                        activityAcademyAttendanceDetailsBinding.rcvTakeAttendance.setVisibility(View.VISIBLE);
                        activityAcademyAttendanceDetailsBinding.tvnotfound.setVisibility(View.GONE);

                        activityAcademyAttendanceDetailsBinding.rcvTakeAttendance.setAdapter(new AttendanceDetailsListAdapter(mContext, studentList));
                    }


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
            Global.msgDialog((Activity) mContext, getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("section_group", "");
                param.put("attendance_date", date);
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