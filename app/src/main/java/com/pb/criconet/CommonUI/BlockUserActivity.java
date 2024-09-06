package com.pb.criconet.CommonUI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pb.criconet.R;
import com.pb.criconet.adapter.BlockUserAdapter;
import com.pb.criconet.databinding.ActivityBlockUserBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.model.pavilionModel.NewUserModel;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.GridSpacingItemDecoration;
import com.pb.criconet.util.SessionManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class BlockUserActivity extends AppCompatActivity {
    ActivityBlockUserBinding activityBlockUserBinding;
    Context mContext;
    Activity mActivity;
    private SharedPreferences prefs;
    private RequestQueue queue;
    CustomLoaderView loaderView;
    ArrayList<NewUserModel> modelArrayList;
    BlockUserAdapter blockUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBlockUserBinding = ActivityBlockUserBinding.inflate(getLayoutInflater());
        setContentView(activityBlockUserBinding.getRoot());

        mContext = this;
        mActivity = this;

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityBlockUserBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(R.string.blocked_users);
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> {
            finish();
        });

        loaderView = CustomLoaderView.initialize(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        queue = Volley.newRequestQueue(this);

        iniTView();

        if (Global.isOnline(mContext)) {
            getBlockUsers();
        } else {
            Global.showDialog(mActivity);
        }
    }

    private void iniTView() {

        activityBlockUserBinding.rvBlockUser.setHasFixedSize(true);
        activityBlockUserBinding.rvBlockUser.setLayoutManager(new GridLayoutManager(mContext,2));
        activityBlockUserBinding.rvBlockUser.addItemDecoration(new GridSpacingItemDecoration(2, 10, false));

    }


    public void getBlockUsers() {

        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_blocked_users",
                response -> {
                    Timber.e("response:  %s", response);
                    loaderView.hideLoader();
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                            ArrayList<NewUserModel> object = NewUserModel.fromJson(jsonObject.getJSONArray("blocked_users"));
                            modelArrayList = new ArrayList<>();
                            modelArrayList.addAll(object);


                            blockUserAdapter = new BlockUserAdapter(mContext, modelArrayList, (userId,pos) -> {
                                if (Global.isOnline(mContext)) {
                                    UnBlockUser(userId,pos);
                                } else {
                                    Global.showDialog(mActivity);
                                }
                            });
                            activityBlockUserBinding.rvBlockUser.setAdapter(blockUserAdapter);

                            if (modelArrayList.size() == 0) {
                                activityBlockUserBinding.rvBlockUser.setVisibility(View.GONE);
                                activityBlockUserBinding.notfound.setVisibility(View.VISIBLE);
                            } else {
                                activityBlockUserBinding.rvBlockUser.setVisibility(View.VISIBLE);
                                activityBlockUserBinding.notfound.setVisibility(View.GONE);
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
                System.out.println("data   :" + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    public void UnBlockUser(final String user_id,int pos) {
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "block_user",
                response -> {
                    Timber.e("response:  %s", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {
                            blockUserAdapter.removeItem(pos);
                        } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                            Global.msgDialog(mActivity, jsonObject.optJSONObject("errors").optString("error_text"));
                        } else {
                            Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
//                Global.msgDialog(EditProfile.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
//        "user_id:1735
//        s:1
//        recipient_id:1703
//        block_type:un - block "
                param.put("user_id", SessionManager.get_user_id(prefs));
//                param.put("s", "1");
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("recipient_id", user_id);
                param.put("block_type", "un-block");

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