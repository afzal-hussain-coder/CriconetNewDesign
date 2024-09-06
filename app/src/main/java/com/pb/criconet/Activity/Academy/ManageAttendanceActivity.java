package com.pb.criconet.Activity.Academy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.pb.criconet.adapter.AcademyAdapter.AttendanceListAdapter;
import com.pb.criconet.databinding.ActivityManageAttendanceBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.model.AcademyModel.AttendanceReportViewChild;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.SessionManager;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class ManageAttendanceActivity extends AppCompatActivity {

    ActivityManageAttendanceBinding activityManageAttendanceBinding;
    Context mContext;
    Activity mActivity;
    private RequestQueue queue;
    CustomLoaderView loaderView;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityManageAttendanceBinding = ActivityManageAttendanceBinding.inflate(getLayoutInflater());
        setContentView(activityManageAttendanceBinding.getRoot());

        mContext = this;
        mActivity = this;

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityManageAttendanceBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.manage_atte));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        queue = Volley.newRequestQueue(mContext);
        loaderView = CustomLoaderView.initialize(mContext);
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);



        inItView();

        if (Global.isOnline(mContext)) {
            getAcademyStudentsList();
        } else {
            Global.showDialog(mActivity);
        }

    }




    private void inItView() {

        activityManageAttendanceBinding.rcvAttendance.setHasFixedSize(true);
        activityManageAttendanceBinding.rcvAttendance.setLayoutManager(new LinearLayoutManager(mContext));

        activityManageAttendanceBinding.tvTakeAttendance.setOnClickListener(v -> {
        startActivity(new Intent(mContext,AcademyTakeAttendanceActivity.class));
        });
    }

    private void getAcademyStudentsList() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.ACADEMY_ATTENDANCE_REPORT, response -> {
            Timber.d(response);
            loaderView.hideLoader();
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("status").equalsIgnoreCase("200")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    ArrayList<AttendanceReportViewChild> studentList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        AttendanceReportViewChild academyStudenModel = new AttendanceReportViewChild(jsonArray.getJSONObject(i));
                        studentList.add(academyStudenModel);
                    }
                    if(studentList.isEmpty()){

                        activityManageAttendanceBinding.rcvAttendance.setVisibility(View.GONE);
                        activityManageAttendanceBinding.tvnotfound.setVisibility(View.VISIBLE);
                    }else{
                        activityManageAttendanceBinding.rcvAttendance.setVisibility(View.VISIBLE);
                        activityManageAttendanceBinding.tvnotfound.setVisibility(View.GONE);

                        activityManageAttendanceBinding.rcvAttendance.setAdapter(new AttendanceListAdapter(mContext, studentList));
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