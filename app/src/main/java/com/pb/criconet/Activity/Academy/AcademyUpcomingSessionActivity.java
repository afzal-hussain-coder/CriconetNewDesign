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
import com.pb.criconet.adapter.AcademyAdapter.AcademyUpComingAdapter;
import com.pb.criconet.databinding.ActivityAcademyUpcomingSessionBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.model.AcademyModel.ScheduledListModel;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class AcademyUpcomingSessionActivity extends AppCompatActivity {
    ActivityAcademyUpcomingSessionBinding activityAcademyUpcomingSessionBinding;
    Context mContext;
    Activity mActivity;
    private SharedPreferences prefs;
    private RequestQueue queue;
    CustomLoaderView loaderView;
    private AcademyUpComingAdapter adapter;
    private ArrayList<ScheduledListModel> myContestListModelslist;
    String FROM ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAcademyUpcomingSessionBinding = ActivityAcademyUpcomingSessionBinding.inflate(getLayoutInflater());
        setContentView(activityAcademyUpcomingSessionBinding.getRoot());
        mContext = this;
        mActivity = this;

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityAcademyUpcomingSessionBinding.toolbar;
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> {
            finish();
        });

        loaderView = CustomLoaderView.initialize(this);
        queue = Volley.newRequestQueue(mActivity);
        prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            FROM = extras.getString("FROM");
        }

        if(FROM.equalsIgnoreCase("Past")){
            toolbarInnerpageBinding.toolbartext.setText(R.string.past_sessio);
        }else{
            toolbarInnerpageBinding.toolbartext.setText(R.string.upcoming_se);
        }

        if (Global.isOnline(mActivity)) {
            getScheduledSession();
        } else {
            Global.showDialog(mActivity);
        }

        inItView();


    }

    private void inItView() {

        activityAcademyUpcomingSessionBinding.rcvUpcomingSession.setHasFixedSize(true);
        activityAcademyUpcomingSessionBinding.rcvUpcomingSession.setLayoutManager(new LinearLayoutManager(mContext));


    }

    private void getScheduledSession() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.ACADEMY_GET_SCHEDULE_MEETING_LIST,
                response -> {
                    Timber.d(response);
                    loaderView.hideLoader();
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {

                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            ScheduledListModel myContestListModel;
                            myContestListModelslist = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                myContestListModel = new ScheduledListModel(jsonArray.getJSONObject(i));
                                myContestListModelslist.add(myContestListModel);
                            }
                            //Toaster.customToast(myContestListModelslist.size()+"");

                            if (myContestListModelslist.isEmpty()) {
                                activityAcademyUpcomingSessionBinding.tvNotFound.setVisibility(View.VISIBLE);
                                activityAcademyUpcomingSessionBinding.rcvUpcomingSession.setVisibility(View.GONE);
                            } else {
                                activityAcademyUpcomingSessionBinding.tvNotFound.setVisibility(View.GONE);
                                activityAcademyUpcomingSessionBinding.rcvUpcomingSession.setVisibility(View.VISIBLE);
                                adapter = new AcademyUpComingAdapter(mActivity, myContestListModelslist, SessionManager.get_leaugeName(prefs),FROM);
                                activityAcademyUpcomingSessionBinding.rcvUpcomingSession.setAdapter(adapter);

                            }

                        } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {

                            Global.msgDialog(mActivity, jsonObject.optJSONObject("errors").optString("error_text"));
                        } else {
                            Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    loaderView.hideLoader();
                    Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                if(FROM.equalsIgnoreCase("Past")){
                    param.put("session_history", "old");
                }else{
                    param.put("session_history", "new");
                }
                param.put("academy_id", SessionManager.get_academyId(prefs));
                System.out.println("data   :" + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }
}