package com.pb.criconet.Activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.allattentionhere.autoplayvideos.AAH_CustomRecyclerView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pb.criconet.R;
import com.pb.criconet.adapter.PavilionAdapter.SavedPostAdapter;
import com.pb.criconet.databinding.ActivitySavedPostBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.inteface.pavilionInterface.PostListeners;
import com.pb.criconet.model.pavilionModel.NewPostModel;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.RecycleViewPaginationScrollListener;
import com.pb.criconet.util.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SavedPostActivity extends AppCompatActivity implements PostListeners {

    ActivitySavedPostBinding activitySavedPostBinding;
    Context mContext;
    Activity mActivity;
    private RequestQueue queue;
    private SharedPreferences prefs;
    CustomLoaderView loaderView;
    SavedPostAdapter savedPostAdapter;
    private AAH_CustomRecyclerView savedPostRV;
    private String after_post_id = "0";
    private TextView tvNotFoundSavedPost;
    public ArrayList<NewPostModel> modelArrayList;
    private LinearLayoutManager mLayoutManager;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_post);
        activitySavedPostBinding = ActivitySavedPostBinding.inflate(getLayoutInflater());
        setContentView(activitySavedPostBinding.getRoot());
        mContext = this;
        mActivity = this;

        inItView();

        if (Global.isOnline(mContext)) {
            getFeed();
        } else {
            Global.showDialog(mActivity);
        }
    }

    private void inItView() {
        ToolbarInnerpageBinding toolbarInnerpageBinding = activitySavedPostBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(getResources().getString(R.string.saved_posts));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        loaderView = CustomLoaderView.initialize(mContext);
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        queue = Volley.newRequestQueue(mContext);

        savedPostRV = findViewById(R.id.savedPostRV);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(mContext, R.drawable.divider)));
        savedPostRV.addItemDecoration(itemDecorator);
        savedPostRV.setHasFixedSize(true);
        tvNotFoundSavedPost = findViewById(R.id.tvNotFoundSavedPost);
        modelArrayList = new ArrayList<>();
        savedPostAdapter = new SavedPostAdapter(mActivity, modelArrayList, this);
        mLayoutManager = new LinearLayoutManager(mContext);

        savedPostRV.setLayoutManager(mLayoutManager);
        savedPostRV.setItemAnimator(new DefaultItemAnimator());
        savedPostRV.setActivity(mActivity); //todo before setAdapter
        //optional - to play only first visible video
        savedPostRV.setPlayOnlyFirstVideo(true);
        savedPostRV.setVisiblePercent(90); // percentage of View that needs to be visible to start playing
        savedPostRV.setAdapter(savedPostAdapter);
        //call this functions when u want to start autoplay on loading async lists (eg firebase)
        savedPostRV.smoothScrollBy(0, 1);
        savedPostRV.smoothScrollBy(0, -1);
        savedPostRV.addOnScrollListener(new RecycleViewPaginationScrollListener(mLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                after_post_id = modelArrayList.get(modelArrayList.size() - 1).getId();
                getFeed();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

        });
    }

    private void getFeed() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "home_posts", response -> {
            Log.d("homeResponse", response);
            loaderView.hideLoader();
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                    JSONArray array = jsonObject.getJSONArray("posts");
                    if (array.length() < 1) {
                        isLastPage = true;
                    }
                    modelArrayList.addAll(NewPostModel.fromJson(array));

                    isLoading = false;
                    savedPostAdapter.notifyDataSetChanged();

//                        if(modelArrayList.size()>0){
//                            String pID="30811";
//
//                            for(int i=0;i<modelArrayList.size();i++){
//
//                                if(pID.equalsIgnoreCase(modelArrayList.get(i).getId())){
//                                    //Toaster.customToast(modelArrayList.get(i).getId()+"/"+i);
//                                    savedPostRV.getLayoutManager().scrollToPosition(i);
//                                    break;
//                                }
//                            }
//                        }

                    if (after_post_id.equals("0")) {
                        savedPostRV.smoothScrollBy(0, 1);
                        savedPostRV.smoothScrollBy(0, -1);

                        if (modelArrayList.size() == 0) {
                            tvNotFoundSavedPost.setVisibility(View.VISIBLE);
                        } else {
                            tvNotFoundSavedPost.setVisibility(View.GONE);
                        }
                    }

                } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                    Global.msgDialog(mActivity, Objects.requireNonNull(jsonObject.optJSONObject("errors")).optString("error_text"));
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
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("after_post_id", after_post_id);
                param.put("s", SessionManager.get_session_id(prefs));
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    public void DeleteFeed(final String id,int pos) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "delete_post", response -> {
            Log.e(" %s", response);
            loaderView.hideLoader();
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                  //  ResetFeed();
                } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                    Global.msgDialog(mActivity, Objects.requireNonNull(jsonObject.optJSONObject("errors")).optString("error_text"));
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
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("post_id", id);
                param.put("s", SessionManager.get_session_id(prefs));
                Log.e("Pavilion",param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    private void likeFeed(final String id) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "like_on_post", response -> {
            Log.e("Pavilion",String.valueOf(response));
            loaderView.hideLoader();
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                        ResetFeed();
                } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                    Global.msgDialog(mActivity, Objects.requireNonNull(jsonObject.optJSONObject("errors")).optString("error_text"));
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
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("post_id", id);
                param.put("s", SessionManager.get_session_id(prefs));
                Log.e("Pavilion",param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void dislikeFeed(final String id) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "unlike_on_post", response -> {
            Log.e("Pavilion",String.valueOf(response));
            loaderView.hideLoader();
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                        ResetFeed();
                } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                    Global.msgDialog(mActivity, Objects.requireNonNull(jsonObject.optJSONObject("errors")).optString("error_text"));
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
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("post_id", id);
                param.put("s", SessionManager.get_session_id(prefs));
                Log.e("Pavilion",param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    public void ReportFeed(final String id, final String message) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "report_post", response -> {
            Log.e("Pavilion",String.valueOf(response));
            loaderView.hideLoader();
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                    Global.msgDialog(mActivity, getResources().getString(R.string.Post_reported_Successfully));
                       ResetFeed();
                } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                    Global.msgDialog(mActivity, Objects.requireNonNull(jsonObject.optJSONObject("errors")).optString("error_text"));
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
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("post_id", id);
                param.put("report_text", message);
                Log.e("Pavilion",param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    private void ResetFeed() {
        after_post_id = "0";
        isLoading = false;
        isLastPage = false;

        modelArrayList = new ArrayList<>();
        savedPostAdapter = new SavedPostAdapter(mActivity, modelArrayList, this);
        mLayoutManager = new LinearLayoutManager(mContext);

        savedPostRV.setLayoutManager(mLayoutManager);
        savedPostRV.setItemAnimator(new DefaultItemAnimator());

        savedPostRV.setActivity(mActivity); //todo before setAdapter
        //optional - to play only first visible video
        savedPostRV.setPlayOnlyFirstVideo(true);
        savedPostRV.setVisiblePercent(90); // percentage of View that needs to be visible to start playing
        savedPostRV.setAdapter(savedPostAdapter);
        //call this functions when u want to start autoplay on loading async lists (eg firebase)
        savedPostRV.smoothScrollBy(0, 1);
        savedPostRV.smoothScrollBy(0, -1);

        if (Global.isOnline(mContext)) {
            getFeed();
        } else {
            Global.showDialog(mActivity);
        }
    }

    @Override
    public void onLikeClickListener(NewPostModel post) {
        likeFeed(post.getId());
    }

    @Override
    public void onDislikeClickListener(NewPostModel post) {
        dislikeFeed(post.getId());
    }

    @Override
    public void onCommentClickListener(NewPostModel post) {
        Log.e("Pavilion",post.toString());
//        Intent intent = new Intent(getActivity(), FeedDetails.class);
//        intent.putExtra("feed_id", post.getId());
//        startActivity(intent);
//        getActivity().finish();
    }

    @Override
    public void onShareClickListener(NewPostModel post) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, post.getDetails_url());
//        shareIntent.setType("text/html");
        shareIntent.setType("text/plain");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share"));
    }

    @Override
    public void onProfileClickListener(NewPostModel post) {

    }

    @Override
    public void onReportFeedListener(String id, String message) {
        ReportFeed(id, message);
    }

    @Override
    public void onDeleteFeedListener(String id) {
       // DeleteFeed(id);
    }

    @Override
    public void onPostDeleteFeedListener(String id, int pos) {
        modelArrayList.remove(pos);
        savedPostAdapter.notifyItemRemoved(pos);
        DeleteFeed(id, pos);
    }

    @Override
    public void onEditFeedListener(String id, String text) {
        //editPostDialog(id,text);
    }
}