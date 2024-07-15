package com.pb.criconet.Activity.Streaming;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pb.criconet.R;
import com.pb.criconet.adapter.Streaming.RecordedMatchAdapter;
import com.pb.criconet.databinding.ActivityArchievMatchBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.model.StreamingModel.VideoModel;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.SessionManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ArchievMatchActivity extends AppCompatActivity {

    ActivityArchievMatchBinding activityArchievMatchBinding;
    CustomLoaderView loaderView;
    private RequestQueue queue;
    private SharedPreferences prefs;
    Activity mContext;
    private ArrayList<VideoModel> data;
    String search_value="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityArchievMatchBinding = ActivityArchievMatchBinding.inflate(getLayoutInflater());
        setContentView(activityArchievMatchBinding.getRoot());

        mContext = this;

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityArchievMatchBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(getResources().getString(R.string.view_archiv));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());


        loaderView = CustomLoaderView.initialize(mContext);
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        queue = Volley.newRequestQueue(mContext);

        activityArchievMatchBinding.weekList.setLayoutManager(new LinearLayoutManager(mContext));
        activityArchievMatchBinding.weekList.setHasFixedSize(true);


        activityArchievMatchBinding.flSubmit.setOnClickListener(v -> {
            search_value = activityArchievMatchBinding.editSearch.getText().toString().trim();

            if(search_value.isEmpty()){

            }else{
                if (Global.isOnline(mContext)) {
                    getArchieveatch();
                } else {
                    Global.showDialog(mContext);
                }
            }


        });

        if (Global.isOnline(mContext)) {
            getArchieveatch();
        } else {
            Global.showDialog(mContext);
        }

    }


    private void getArchieveatch() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_recorded_video_lists",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RecordeMatch", response);
                        loaderView.hideLoader();
                        try {
                            JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                data = new ArrayList<>();
                                activityArchievMatchBinding.editSearch.setText("");
                                data = VideoModel.fromJson(jsonObject.getJSONArray("data"));
//                                int size = data.size();
//                                size =0;
                                if (data.size() == 0) {
                                    activityArchievMatchBinding.notfound.setVisibility(View.VISIBLE);
                                } else {
                                    activityArchievMatchBinding.notfound.setVisibility(View.GONE);
                                    activityArchievMatchBinding.weekList.setAdapter(new RecordedMatchAdapter(data, mContext));
                                }

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(mContext, jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Global.msgDialog(mContext, getResources().getString(R.string.error_server));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        //progress.dismiss();
                        loaderView.hideLoader();
                        Global.msgDialog(mContext, getResources().getString(R.string.error_server));
//                Global.msgDialog(getActivity(), "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("country_id", "101");
                param.put("state_id", "");
                param.put("search_key", search_value);

                System.out.println("data   " + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }
}