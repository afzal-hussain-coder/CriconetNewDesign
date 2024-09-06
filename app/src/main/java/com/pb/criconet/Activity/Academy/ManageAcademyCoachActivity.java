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
import com.pb.criconet.adapter.AcademyAdapter.ManageCoachAdapter;
import com.pb.criconet.databinding.ActivityManageAcademyCoachBinding;
import com.pb.criconet.model.AcademyModel.ManageCoachesModel;
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

public class ManageAcademyCoachActivity extends AppCompatActivity {
    ActivityManageAcademyCoachBinding activityManageAcademyCoachBinding;
    Context mContext;
    Activity mActivity;
    CustomLoaderView loaderView;
    private RequestQueue queue;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityManageAcademyCoachBinding = ActivityManageAcademyCoachBinding.inflate(getLayoutInflater());
        setContentView(activityManageAcademyCoachBinding.getRoot());

        mContext = this;
        mActivity = this;

        loaderView = CustomLoaderView.initialize(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        queue = Volley.newRequestQueue(this);

        inItView();

    }

    @Override
    protected void onResume() {
        if (Global.isOnline(this)) {
            getAcademyCoachsList();
        } else {
            Global.showDialog(this);
        }
        super.onResume();
    }

    private void inItView() {

        activityManageAcademyCoachBinding.imgBack.setOnClickListener(v -> {
            finish();
        });
        activityManageAcademyCoachBinding.toolbartext.setText(mContext.getResources().getString(R.string.manage_coac));

        activityManageAcademyCoachBinding.rvAcademyCoach.setHasFixedSize(true);
        activityManageAcademyCoachBinding.rvAcademyCoach.setLayoutManager(new GridLayoutManager(mActivity,2));


        activityManageAcademyCoachBinding.flAddCoach.setOnClickListener(v -> {
         startActivity(new Intent(mContext,AcademyAddCoachActivity.class));
        });


    }

    private void getAcademyCoachsList() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.ACADEMY_COACH_LIST, response -> {
            Timber.d(response);
            loaderView.hideLoader();
            try {
                JSONObject jsonObject = new JSONObject(response);
                String total = jsonObject.getString("total");
                if(total.equalsIgnoreCase("0")){
                    activityManageAcademyCoachBinding.tvTotalCount.setText("0");
                }else{
                    activityManageAcademyCoachBinding.tvTotalCount.setText(" - "+total);
                }
                if (jsonObject.optString("status").equalsIgnoreCase("200")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    ArrayList<ManageCoachesModel> studentList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        ManageCoachesModel academyStudenModel = new ManageCoachesModel(jsonArray.getJSONObject(i));
                        studentList.add(academyStudenModel);
                    }


                    if(total.equalsIgnoreCase("0")){
                        activityManageAcademyCoachBinding.rvAcademyCoach.setVisibility(View.GONE);
                        activityManageAcademyCoachBinding.tvnotfound.setVisibility(View.VISIBLE);
                    }else{
                        activityManageAcademyCoachBinding.rvAcademyCoach.setVisibility(View.VISIBLE);
                        activityManageAcademyCoachBinding.tvnotfound.setVisibility(View.GONE);


                        activityManageAcademyCoachBinding.rvAcademyCoach.setAdapter(new ManageCoachAdapter(mContext, studentList, (manageCoachesModel) -> {

                            Intent intent =new Intent(mContext, ManageAcademyCoachDetailsActivity.class);
                            Bundle args = new Bundle();
                            try{
                                args.putSerializable("ARRAYLIST",(Serializable)manageCoachesModel);
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
}