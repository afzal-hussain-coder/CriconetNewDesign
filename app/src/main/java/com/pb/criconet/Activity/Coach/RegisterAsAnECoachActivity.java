package com.pb.criconet.Activity.Coach;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pb.criconet.Fragment.CoachFragments.CoachAvailableDateAndSessionFragment;
import com.pb.criconet.Fragment.CoachFragments.CoachProfesionalInQualifocationFragment;
import com.pb.criconet.R;
import com.pb.criconet.adapter.EcoachingAdapter.ViewPagerAdapter;
import com.pb.criconet.adapter.EcoachingAdapter.ViewPagerAdapterForCoachFragment;
import com.pb.criconet.databinding.ActivityRegisterAsAnEcoachBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.SessionManager;
import com.pb.criconet.util.Toaster;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RegisterAsAnECoachActivity extends AppCompatActivity {

    ActivityRegisterAsAnEcoachBinding activityRegisterAsAnEcoachBinding;
    Context mContext;
    Activity mActivity;
    ViewPagerAdapterForCoachFragment viewPagerAdapter;
    String from_where="";
    private SharedPreferences prefs;
    private RequestQueue queue;
    CustomLoaderView loaderView;
    JSONObject jsonObjectPersonalInfo;
    JSONObject JsonObjectQulification;
    JSONArray jsonObjectAvailableSession;
    Bundle dataBundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRegisterAsAnEcoachBinding = ActivityRegisterAsAnEcoachBinding.inflate(getLayoutInflater());
        setContentView(activityRegisterAsAnEcoachBinding.getRoot());
        mContext = this;
        mActivity = this;

        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        loaderView = CustomLoaderView.initialize(mContext);
        queue = Volley.newRequestQueue(mContext);

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityRegisterAsAnEcoachBinding.toolbar;
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            from_where = bundle.getString("FROM");
        }

        Toaster.customToast("Register...");

        if(from_where.equalsIgnoreCase("1")){
            toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.upadate_as_a_coach));
        }else{
            toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.regsiter_as_a_coach));
        }



//        if(from_where.equalsIgnoreCase("1")){
//            if (Global.isOnline(mActivity)) {
//                getUsersDetails();
//            } else {
//                Global.showDialog(mActivity);
//            }
//        }

        initView();
    }


    public void switchToNextFragment(int pos) {
        activityRegisterAsAnEcoachBinding.viewPager.setCurrentItem(pos); // Switch to the second fragment


        // Set the visibility of the save button based on the position
        if (pos == 1) { // If switching to SecondFragment
            CoachProfesionalInQualifocationFragment fragment = (CoachProfesionalInQualifocationFragment) viewPagerAdapter.instantiateItem(activityRegisterAsAnEcoachBinding.viewPager, pos);
            fragment.setSaveButtonVisibility(true);
        } else if(pos==2){
            CoachAvailableDateAndSessionFragment fragment = (CoachAvailableDateAndSessionFragment) viewPagerAdapter.instantiateItem(activityRegisterAsAnEcoachBinding.viewPager, pos);
            fragment.setSaveButtonVisibility(true);
        }
        else {
            // Hide the save button when switching back or to other fragments
            if (viewPagerAdapter.getItem(pos) instanceof CoachProfesionalInQualifocationFragment) {
                CoachProfesionalInQualifocationFragment fragment = (CoachProfesionalInQualifocationFragment) viewPagerAdapter.instantiateItem(activityRegisterAsAnEcoachBinding.viewPager, pos);
                fragment.setSaveButtonVisibility(false);
            }
        }
    }

    private void initView() {


        viewPagerAdapter = new ViewPagerAdapterForCoachFragment(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,from_where);
        activityRegisterAsAnEcoachBinding.viewPager.setAdapter(viewPagerAdapter);
        activityRegisterAsAnEcoachBinding.tabLayout.setupWithViewPager(activityRegisterAsAnEcoachBinding.viewPager);
        activityRegisterAsAnEcoachBinding.tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.purple_700));

    }

    public void getUsersDetails() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_user_data",
                response -> {
                    Log.d("getUserDetails", response);
                    loaderView.hideLoader();
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                            JSONObject object = jsonObject.getJSONObject("coach_data");

                            if (object.has("coach_available_date")) {
                                jsonObjectAvailableSession = object.getJSONArray("coach_available_date");
                            }

                            if (object.has("personal_info")) {
                                jsonObjectPersonalInfo = object.getJSONObject("personal_info");

                            }


                            if(object.has("coach_info")){
                                JsonObjectQulification= object.getJSONObject("coach_info");
                            }

                            dataBundle = new Bundle();

                            if (from_where.equalsIgnoreCase("1")) {
                                dataBundle.putString("FROM", from_where);

                                // Safely add JSON objects to the bundle
                                if (jsonObjectPersonalInfo != null) {
                                    dataBundle.putString("Personal", jsonObjectPersonalInfo.toString());

                                } else {
                                    Log.e("RegisterAsAnECoach", "jsonObjectPersonalInfo is null");
                                }

                                if (JsonObjectQulification != null) {
                                    dataBundle.putString("Qualification", JsonObjectQulification.toString());
                                } else {
                                    Log.e("RegisterAsAnECoach", "JsonObjectQulification is null");
                                }

                                if (jsonObjectAvailableSession != null) {
                                    dataBundle.putString("AvailableSession", jsonObjectAvailableSession.toString());
                                } else {
                                    Log.e("RegisterAsAnECoach", "jsonObjectAvailableSession is null");
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
                },
                error -> {
                    error.printStackTrace();
                    Global.msgDialog(mActivity, getResources().getString(R.string.error_server));

                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("user_profile_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
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