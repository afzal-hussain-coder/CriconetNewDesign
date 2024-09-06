package com.pb.criconet.Activity.Academy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pb.criconet.R;
import com.pb.criconet.adapter.AcademyAdapter.TakeAttendanceListAdapter;
import com.pb.criconet.databinding.ActivityAcademyTakeAttendanceBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.model.AcademyModel.AcademyStudenModel;
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


public class AcademyTakeAttendanceActivity extends AppCompatActivity {
    ActivityAcademyTakeAttendanceBinding activityAcademyTakeAttendanceBinding;
    Context mContext;
    Activity mActivity;
    private RequestQueue queue;
    CustomLoaderView loaderView;
    private SharedPreferences prefs;
    JSONArray jsonArrayattendance_status=null;
    ArrayList<AttendanceReport> studentListreport = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAcademyTakeAttendanceBinding = ActivityAcademyTakeAttendanceBinding.inflate(getLayoutInflater());
        setContentView(activityAcademyTakeAttendanceBinding.getRoot());

        mContext = this;
        mActivity = this;

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityAcademyTakeAttendanceBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.attendance));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        queue = Volley.newRequestQueue(mContext);
        loaderView = CustomLoaderView.initialize(mContext);
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        inItView();

        if (Global.isOnline(mContext)) {
            getAcademyStudentsListReport();
        } else {
            Global.showDialog(mActivity);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Global.isOnline(mContext)) {
                    getAcademyStudentsList();
                } else {
                    Global.showDialog(mActivity);
                }
            }
        },500);

    }

    private void inItView() {

        activityAcademyTakeAttendanceBinding.tvCurrentDate.setText("Today is "+Global.getCurrentDateAcademyPanel());

        activityAcademyTakeAttendanceBinding.rcvTakeAttendance.setHasFixedSize(true);
        activityAcademyTakeAttendanceBinding.rcvTakeAttendance.setLayoutManager(new LinearLayoutManager(mContext));

        activityAcademyTakeAttendanceBinding.flSubmit.setOnClickListener(v -> {
            if (Global.isOnline(mContext)) {
                addAcademyStudent();
            } else {
                Global.showDialog(mActivity);
            }
        });


    }

    private void getAcademyStudentsListReport() {
        loaderView.showLoader();
//        progressDialog = Global.getProgressDialog(this, CCResource.getString(this, R.string.loading_dot), false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.ACADEMY_ATTENDANCE_LIST, response -> {
            Timber.d(response);
            loaderView.hideLoader();
            //Global.dismissDialog(progressDialog);

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("status").equalsIgnoreCase("200")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");


                    for (int i = 0; i < jsonArray.length(); i++) {
                        AttendanceReport academyStudenModel = new AttendanceReport(jsonArray.getJSONObject(i));
                        studentListreport.add(academyStudenModel);
                    }
                    //Toaster.customToast(matchLists.size()+"");
//                    if(studentList.isEmpty()){
//
//                        rvstudentList.setVisibility(View.GONE);
//                        tvnotfound.setVisibility(View.VISIBLE);
//                    }else{
//                        rvstudentList.setVisibility(View.VISIBLE);
//                        tvnotfound.setVisibility(View.GONE);
//
//                        rvstudentList.setAdapter(new AcademyAttendanceReportListAdapter(mContext, studentList));
//                    }


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
                param.put("section_group", "");
                param.put("attendance_date", Global.getCurrentDatee());
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

    private void getAcademyStudentsList() {
        loaderView.showLoader();
//        progressDialog = Global.getProgressDialog(this, CCResource.getString(this, R.string.loading_dot), false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.ACADEMY_STUDENT_LIST, response -> {
            Timber.d(response);
            loaderView.hideLoader();
            //Global.dismissDialog(progressDialog);

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("status").equalsIgnoreCase("200")) {



                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    ArrayList<AcademyStudenModel> studentList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        AcademyStudenModel academyStudenModel = new AcademyStudenModel(jsonArray.getJSONObject(i));
                        studentList.add(academyStudenModel);
                    }
                    //Toaster.customToast(matchLists.size()+"");
                    if(studentList.isEmpty()){

                        activityAcademyTakeAttendanceBinding.rcvTakeAttendance.setVisibility(View.GONE);
                        activityAcademyTakeAttendanceBinding.tvnotfound.setVisibility(View.VISIBLE);
                    }else{
                        activityAcademyTakeAttendanceBinding.rcvTakeAttendance.setVisibility(View.VISIBLE);
                        activityAcademyTakeAttendanceBinding.tvnotfound.setVisibility(View.GONE);

                        if(studentListreport.size()>0){
                            activityAcademyTakeAttendanceBinding.rcvTakeAttendance.setAdapter(new TakeAttendanceListAdapter(mContext, studentList,studentListreport, new TakeAttendanceListAdapter.clickCallback() {

                                @Override
                                public void parsent(JSONArray jsonArray) {
                                    jsonArrayattendance_status = jsonArray;
                                    //Toaster.customToast(jsonArrayattendance_status.length()+"");
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(jsonArrayattendance_status.length()>0){
                                                activityAcademyTakeAttendanceBinding.flSubmit.setVisibility(View.VISIBLE);
                                            }else{
                                                activityAcademyTakeAttendanceBinding.flSubmit.setVisibility(View.GONE);
                                            }
                                        }
                                    },100);


                                    //Log.d("Parsent",jsonArray.toString());
                                }

                                @Override
                                public void sendMessage(String coachId, String userId) {

                                }
                            }));
                        }else{
                            activityAcademyTakeAttendanceBinding.rcvTakeAttendance.setAdapter(new TakeAttendanceListAdapter(mContext, studentList,new ArrayList<>(), new TakeAttendanceListAdapter.clickCallback() {

                                @Override
                                public void parsent(JSONArray jsonArray) {
                                    jsonArrayattendance_status = jsonArray;
                                    if(jsonArrayattendance_status.length()>0){
                                        activityAcademyTakeAttendanceBinding.flSubmit.setVisibility(View.VISIBLE);
                                    }else{
                                        activityAcademyTakeAttendanceBinding.flSubmit.setVisibility(View.GONE);
                                    }
                                    //Log.d("Parsent",jsonArray.toString());
                                }

                                @Override
                                public void sendMessage(String coachId, String userId) {

                                }
                            }));
                        }



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

    private void addAcademyStudent() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.ACADEMY_TAKE_ATTENDANCE, response -> {
            Timber.d(response);

            try {
                loaderView.hideLoader();
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                    Toaster.customToast(jsonObject.getString("message"));
                    //new Handler().postDelayed((Runnable) this::finish, 100);
//                    startActivity(new Intent(mContext,ViewAttandenceReport.class));
//                    finish();
                } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                    Global.msgDialog(mActivity, jsonObject.getJSONObject("errors").getString("error_text"));
                } else {
                    Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }, error -> {
            error.printStackTrace();
            loaderView.hideLoader();
            Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();

                // attendance_status:[{"attendance_status":"A","user_id":"1"},{"attendance_status":"A","user_id":"2"},{"attendance_status":"A","user_id":"3"}]

                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("attendance_status", jsonArrayattendance_status.toString());
                param.put("section_group", "");
                param.put("attendance_date", Global.getCurrentDatee());
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