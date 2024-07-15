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
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pb.criconet.R;
import com.pb.criconet.adapter.Streaming.LiveMatchAdapter;
import com.pb.criconet.databinding.ActivityLiveMatchesBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.model.StreamingModel.LiveStreamingNewModel;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LiveMatchesActivity extends AppCompatActivity {

    ActivityLiveMatchesBinding activityLiveMatchesBinding;
    private ArrayList<LiveStreamingNewModel> dataNew;
    CustomLoaderView loaderView;
    private RequestQueue queue;
    private SharedPreferences prefs;
    Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLiveMatchesBinding = ActivityLiveMatchesBinding.inflate(getLayoutInflater());
        setContentView(activityLiveMatchesBinding.getRoot());

        mContext = this;

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityLiveMatchesBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(getResources().getString(R.string.live_streaming));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());


        loaderView = CustomLoaderView.initialize(mContext);
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        queue = Volley.newRequestQueue(mContext);
        activityLiveMatchesBinding.weekList.setLayoutManager(new LinearLayoutManager(mContext));
        activityLiveMatchesBinding.weekList.setHasFixedSize(true);


        if (Global.isOnline(mContext)) {
            getLiveMatchesNew();
        } else {
            Global.showDialog(mContext);
        }


    }

    private void getLiveMatchesNew() {
        //progress.show();
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.GET_LIVE_STREAMING_MATCH,
                response -> {
                    loaderView.hideLoader();
                    //progress.dismiss();
                    try {
                        Log.d("Live Stream Response",response);
                        JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                        if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {
                            dataNew = new ArrayList<>();

                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for(int i = 0; i<jsonArray.length();i++){
                                dataNew.add(new LiveStreamingNewModel(jsonArray.getJSONObject(i)));
                            }

//                            if(jsonObject.has("emoji")){
//                                emojiModelsList = getEmojiLis(jsonObject.getJSONArray("emoji"));
//                            }

                            if (dataNew.size() == 0) {
                                activityLiveMatchesBinding.notfound.setVisibility(View.VISIBLE);
                            } else {
                                activityLiveMatchesBinding.notfound.setVisibility(View.GONE);
                                activityLiveMatchesBinding.weekList.setAdapter(new LiveMatchAdapter(dataNew, mContext));
                            }


                        } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                            Global.msgDialog(mContext, jsonObject.optJSONObject("errors").optString("error_text"));
                        } else {
                            Global.msgDialog(mContext, getResources().getString(R.string.error_server));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    //progress.dismiss();
                    loaderView.hideLoader();
                    Global.msgDialog(mContext, getResources().getString(R.string.error_server));
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));

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