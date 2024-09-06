package com.pb.criconet.Activity.Academy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

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
import com.pb.criconet.adapter.AcademyAdapter.ManageStudentsAdapter;
import com.pb.criconet.databinding.ActivityManageStudentBinding;
import com.pb.criconet.model.AcademyModel.AcademyStudenModel;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class ManageStudentActivity extends AppCompatActivity {
    ActivityManageStudentBinding activityManageStudentBinding;
    Context mContext;
    Activity mActivity;
    private RequestQueue queue;
    private SharedPreferences prefs;
    CustomLoaderView loaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityManageStudentBinding = ActivityManageStudentBinding.inflate(getLayoutInflater());
        setContentView(activityManageStudentBinding.getRoot());
        mContext = this;
        mActivity = this;

        loaderView = CustomLoaderView.initialize(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        queue = Volley.newRequestQueue(this);

        inItView();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Global.isOnline(this)) {
            getAcademyStudentsList();
        } else {
            Global.showDialog(this);
        }
    }

    private void inItView() {

        activityManageStudentBinding.imgBack.setOnClickListener(v -> {
            finish();
        });
        activityManageStudentBinding.toolbartext.setText(mContext.getResources().getString(R.string.manage_stud));

        activityManageStudentBinding.flAddStudents.setOnClickListener(v -> {
        startActivity(new Intent(mContext,AcademyAddStudentsActivity.class).putExtra("FROM","1"));
        });

        activityManageStudentBinding.rvAcademyStudents.setHasFixedSize(true);
        activityManageStudentBinding.rvAcademyStudents.setLayoutManager(new GridLayoutManager(mActivity,2));


        activityManageStudentBinding.tvAcademyName.setText(SessionManager.get_academyName(prefs));
        activityManageStudentBinding.tvAcademyAddress.setText(SessionManager.get_academyAddress(prefs));




    }

    private void getAcademyStudentsList() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.ACADEMY_STUDENT_LIST, response -> {
            Timber.d(response);
            loaderView.hideLoader();

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("status").equalsIgnoreCase("200")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    ArrayList<AcademyStudenModel> studentList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        AcademyStudenModel academyStudenModel = new AcademyStudenModel(jsonArray.getJSONObject(i));
                        studentList.add(academyStudenModel);
                    }

                    if(studentList.isEmpty()){
                        activityManageStudentBinding.tvTotalCount.setText(String.valueOf(0));
                        activityManageStudentBinding.rvAcademyStudents.setVisibility(View.GONE);
                        activityManageStudentBinding.tvnotfound.setVisibility(View.VISIBLE);
                    }else{
                        activityManageStudentBinding.tvTotalCount.setText(" -"+String.valueOf(studentList.size()));
                        activityManageStudentBinding.rvAcademyStudents.setVisibility(View.VISIBLE);
                        activityManageStudentBinding.tvnotfound.setVisibility(View.GONE);


                        activityManageStudentBinding.rvAcademyStudents.setAdapter(new ManageStudentsAdapter(mContext, studentList, academyStudenModel -> {
                            Intent intent =new Intent(mContext, AcademyAddStudentsActivity.class);
                            Bundle args = new Bundle();
                            try{
                                args.putSerializable("ARRAYLIST",(Serializable)academyStudenModel);
                                intent.putExtra("Certificate",args);
                                intent.putExtra("FROM","3");
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                            startActivity(intent);
                        }));


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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}