package com.pb.criconet.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pb.criconet.R;
import com.pb.criconet.adapter.PavilionAdapter.LikedDislikedPostAdapter;
import com.pb.criconet.databinding.ActivityLikeDislikeBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.model.pavilionModel.NewUserModel;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.GridSpacingItemDecoration;
import com.pb.criconet.util.SessionManager;
import com.pb.criconet.util.Toaster;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class LikeDislikeActivity extends AppCompatActivity {

    ActivityLikeDislikeBinding activityLikeDislikeBinding;
    Context mContext;
    Activity mActivity;

    // For pagination.
    int page = 1;
    public static final String Type = "Type";
    public static final String PostId = "post_id";

    private RequestQueue queue;
    private SharedPreferences prefs;
    CustomLoaderView loaderView;
    ToolbarInnerpageBinding toolbarInnerpageBinding;

    String Post_id, post_type;
    ArrayList<NewUserModel> modelArrayList;
    LikedDislikedPostAdapter adapter;
    boolean isFollow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityLikeDislikeBinding = ActivityLikeDislikeBinding.inflate(getLayoutInflater());
        setContentView(activityLikeDislikeBinding.getRoot());

        mContext = this;
        mActivity = this;

        loaderView = CustomLoaderView.initialize(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        queue = Volley.newRequestQueue(this);

        toolbarInnerpageBinding = activityLikeDislikeBinding.toolbar;
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if(bundle.getString(Type).equalsIgnoreCase("Like")){
                toolbarInnerpageBinding.toolbartext.setText(getResources().getString(R.string.Liked));
            }else{
                toolbarInnerpageBinding.toolbartext.setText(bundle.getString(Type));
            }

            post_type = bundle.getString(Type);
            Post_id = bundle.getString(PostId);
        }

        RefreshList();

        inItView();


    }

    private void inItView() {
        activityLikeDislikeBinding.rvLikedDisliked.setHasFixedSize(true);
        activityLikeDislikeBinding.rvLikedDisliked.setLayoutManager(new GridLayoutManager(mContext,2));
        activityLikeDislikeBinding.rvLikedDisliked.addItemDecoration(new GridSpacingItemDecoration(2, 10, false));

    }

    @Override
    public void onResume() {
        super.onResume();
        page = 1;
    }

    public void RefreshList() {
        page = 1;

        modelArrayList = new ArrayList<>();
        adapter = new LikedDislikedPostAdapter(mContext, modelArrayList, (userId, pos) -> {

            isFollow = !isFollow;

            if (Global.isOnline(mContext)) {
                getFollowUser(userId,pos,isFollow);
            } else {
                Global.showDialog(mActivity);
            }

        });
        activityLikeDislikeBinding.rvLikedDisliked.setAdapter(adapter);
        if (Global.isOnline(mContext)) {
            getFriends();
        } else {
            Global.showDialog(mActivity);
        }
    }


    public void getFriends() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_post_data",
                response -> {
                    Timber.e("response:  %s", response);
                    loaderView.hideLoader();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                            ArrayList<NewUserModel> object;
                            if (post_type.equalsIgnoreCase(mContext.getString(R.string.likes)))
                                object = NewUserModel.fromJson(jsonObject.getJSONArray("post_likes"));
                            else
                                object = NewUserModel.fromJson(jsonObject.getJSONArray("post_wonders"));
                            modelArrayList.addAll(object);
                            adapter.notifyDataSetChanged();
                            if (modelArrayList.size() == 0) {
                                activityLikeDislikeBinding.notfound.setVisibility(View.VISIBLE);
                            } else {
                                activityLikeDislikeBinding.notfound.setVisibility(View.GONE);
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
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("post_id", Post_id);
                System.out.println("data   :" + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    public void getFollowUser(String likedUserId,int pos,boolean isFollow) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.FOLLOW_USER,
                response -> {
                    Timber.e("responseFollow:  %s", response);
                    loaderView.hideLoader();
                    String value ="";
                    if(isFollow==true){
                        value="1";
                    }else{
                        value ="0";
                    }
                    adapter.updateItem(pos,value);
                },
                error -> {
                    error.printStackTrace();
                    loaderView.hideLoader();
                    Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("recipient_id", likedUserId);
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