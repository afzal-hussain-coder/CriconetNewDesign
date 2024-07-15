package com.pb.criconet.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pb.criconet.R;
import com.pb.criconet.adapter.PavilionAdapter.MyBlogsAdapter;
import com.pb.criconet.databinding.ActivityMyBlogsBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.model.Blog.BlogListData;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.SessionManager;
import com.pb.criconet.util.Toaster;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyBlogsActivity extends AppCompatActivity {

    ActivityMyBlogsBinding activityMyBlogsBinding;
    Context mContext;
    Activity mActivity;
    private RequestQueue queue;
    private SharedPreferences prefs;
    CustomLoaderView loaderView;
    ArrayList<BlogListData> blogListDataArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMyBlogsBinding = ActivityMyBlogsBinding.inflate(getLayoutInflater());
        setContentView(activityMyBlogsBinding.getRoot());
        mContext = this;
        mActivity = this;

        loaderView = CustomLoaderView.initialize(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        queue = Volley.newRequestQueue(this);

        inItView();

        if (Global.isOnline(mContext)) {
            getBlogList();
        } else {
            Global.showDialog(mActivity);
        }
    }

    private void inItView() {
        ToolbarInnerpageBinding toolbarInnerpageBinding = activityMyBlogsBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(getResources().getString(R.string.my_blogs));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        activityMyBlogsBinding.rcvMyBlogs.setHasFixedSize(true);
        activityMyBlogsBinding.rcvMyBlogs.setLayoutManager(new LinearLayoutManager(mActivity));

    }

    private void getBlogList() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.GET_BLOG, response -> {
            Log.d("BlogResponse", response);
            loaderView.hideLoader();

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("api_text").equalsIgnoreCase("success")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("blogs");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        blogListDataArrayList.add(new BlogListData(jsonArray.getJSONObject(i)));
                    }

                    activityMyBlogsBinding.rcvMyBlogs.setAdapter(new MyBlogsAdapter(mContext, blogListDataArrayList, blogListData -> {
                        startActivity(new Intent(mContext, BlogDetailsActivity.class).putExtra("key", (Serializable) blogListData));
                    }));


                } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {

                    Toaster.customToast(jsonObject.optJSONObject("errors").optString("error_text"));
                } else {
                    Toaster.customToast(getResources().getString(R.string.socket_timeout));

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
                Log.e("Param", param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }
}