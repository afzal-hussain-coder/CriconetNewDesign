package com.pb.criconet.Activity;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.RelativeLayout.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pb.criconet.AGApplication;
import com.pb.criconet.CustomeCamera.CustomeCameraActivity;
import com.pb.criconet.R;
import com.pb.criconet.adapter.PavilionAdapter.CommentAdapter;
import com.pb.criconet.adapter.PavilionAdapter.SearchUserAdapter;
import com.pb.criconet.databinding.ActivityFeedDetailsBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.model.pavilionModel.SearchUser;
import com.pb.criconet.util.Global;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeedDetailsActivity extends AppCompatActivity{

    ActivityFeedDetailsBinding activityFeedDetailsBinding;
    Context mContext;
    Activity mActivity;
    boolean isKeyBoardShow = false;
    String singleImagePic = "";
    private RequestQueue queue;
    private ArrayList<SearchUser> searchUserArrayList = new ArrayList<>();
    private String searchUsername = "";
    public static Uri image_uri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFeedDetailsBinding = ActivityFeedDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityFeedDetailsBinding.getRoot());

        mActivity = this;
        mContext = this;
        queue = Volley.newRequestQueue(mActivity);
        inItView();
    }

    private void inItView() {
        ToolbarInnerpageBinding toolbarInnerpageBinding = activityFeedDetailsBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.comments));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> {
            if (activityFeedDetailsBinding.rvSearchUser.getVisibility() == View.VISIBLE) {
                activityFeedDetailsBinding.rvSearchUser.setVisibility(GONE);
                activityFeedDetailsBinding.rlMain.setVisibility(VISIBLE);
            } else {
                finish();
            }
        });

        activityFeedDetailsBinding.rvComment.setHasFixedSize(true);
        activityFeedDetailsBinding.rvComment.setLayoutManager(new LinearLayoutManager(mActivity));
        activityFeedDetailsBinding.rvComment.setAdapter(new CommentAdapter(mContext));

        activityFeedDetailsBinding.rvSearchUser.setHasFixedSize(true);
        activityFeedDetailsBinding.rvSearchUser.setLayoutManager(new LinearLayoutManager(mActivity));
        activityFeedDetailsBinding.rvSearchUser.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));


        //Comment Post Listener

        activityFeedDetailsBinding.editComment.setOnClickListener(v -> {
            activityFeedDetailsBinding.rlSubmit.setVisibility(View.VISIBLE);
            activityFeedDetailsBinding.editComment.setCursorVisible(true);
        });

        activityFeedDetailsBinding.editComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().isEmpty()) {

                    searchUsername = "";
                    activityFeedDetailsBinding.tvPostGray.setVisibility(View.VISIBLE);
                    activityFeedDetailsBinding.rlSubmit.setVisibility(View.GONE);
                } else {
                    activityFeedDetailsBinding.tvPostGray.setVisibility(View.GONE);
                    activityFeedDetailsBinding.rlSubmit.setVisibility(View.VISIBLE);
                    String search = s.toString();
                    String[] parts = null;
                    String lastWord = "";


                    if (search.contains("\n")) {
                        parts = search.split("\n");
                    } else {
                        parts = search.split(" ");
                    }

                    if (search.startsWith(" ")) {
                        lastWord = search;
                    } else {
                        lastWord = parts[parts.length - 1];
                    }

                    String finalWord = lastWord.substring(lastWord.lastIndexOf(" ") + 1);


                    if (finalWord.contains("@") && finalWord.length() > 3) {

                        Matcher matcher = Pattern.compile("@\\s*(\\w+)").matcher(finalWord);

                        while (matcher.find()) {
                            if (searchUsername.isEmpty()) {
                                if (Global.isOnline(mContext)) {
                                    getUserSearchList(matcher.group(1));
                                } else {
                                    Global.showDialog(mActivity);
                                }
                            }
                        }
                    } else {
                        activityFeedDetailsBinding.rvSearchUser.setVisibility(View.GONE);
                        searchUsername = "";
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        activityFeedDetailsBinding.editComment.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (Global.keyboardShown(activityFeedDetailsBinding.editComment.getRootView())) {
                isKeyBoardShow = true;
                activityFeedDetailsBinding.editComment.setCursorVisible(true);
                activityFeedDetailsBinding.tvPostGray.setVisibility(View.GONE);
                activityFeedDetailsBinding.rlSubmit.setVisibility(View.VISIBLE);
            } else {
                isKeyBoardShow = false;
                if (activityFeedDetailsBinding.editComment.getText().toString().isEmpty()) {
                    activityFeedDetailsBinding.tvPostGray.setVisibility(View.VISIBLE);
                    activityFeedDetailsBinding.editComment.setCursorVisible(false);
                    activityFeedDetailsBinding.rlSubmit.setVisibility(View.GONE);
                } else {
                    activityFeedDetailsBinding.tvPostGray.setVisibility(View.GONE);
                    activityFeedDetailsBinding.editComment.setCursorVisible(true);
                    activityFeedDetailsBinding.rlSubmit.setVisibility(View.VISIBLE);
                }

            }
        });

        activityFeedDetailsBinding.imgKeyboard.setOnClickListener(v -> {
            if (!isKeyBoardShow) {
                Global.showKeyBoard();
            } else {
                Global.hideKeyBoard(mActivity);
            }
        });

        activityFeedDetailsBinding.imgCamera.setOnClickListener(v -> {
            startActivity(new Intent(mContext, CustomeCameraActivity.class)
                    .putExtra("FROM","FeedDetailsActivity"));
        });
        activityFeedDetailsBinding.imgClose.setOnClickListener(v -> {
            activityFeedDetailsBinding.flAttachedImage.setVisibility(GONE);
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            activityFeedDetailsBinding.rlScroll.setLayoutParams(layoutParams);
            activityFeedDetailsBinding.rlScroll.requestLayout();
        });

        activityFeedDetailsBinding.tvPostBlue.setOnClickListener(v -> {
            activityFeedDetailsBinding.flAttachedImage.setVisibility(GONE);
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            activityFeedDetailsBinding.rlScroll.setLayoutParams(layoutParams);
            activityFeedDetailsBinding.rlScroll.requestLayout();
            image_uri=null;
            activityFeedDetailsBinding.editComment.setText("");
        });

        activityFeedDetailsBinding.imgAtTheRate.setOnClickListener(v -> {
            activityFeedDetailsBinding.rlMain.setVisibility(GONE);
            activityFeedDetailsBinding.rvSearchUser.setVisibility(VISIBLE);
            if (Global.isOnline(mContext)) {
                getUserSearchList("");
            } else {
                Global.showDialog(mActivity);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != image_uri) {
            final float scale = AGApplication.getContext().getResources().getDisplayMetrics().density;
            int pixels = (int) (150 * scale + 0.5f);
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(MATCH_PARENT, pixels);
            activityFeedDetailsBinding.rlScroll.setLayoutParams(layoutParams);
            activityFeedDetailsBinding.rlScroll.requestLayout();
            activityFeedDetailsBinding.flAttachedImage.setVisibility(View.VISIBLE);
            activityFeedDetailsBinding.rIVAttached.setImageURI(image_uri);
        }
    }

    private void getUserSearchList(String searchKey) {
        //loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.SEARCH_USER_LIST_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SearchUser Response", response);
                //loaderView.hideLoader();
                searchUserArrayList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("users");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            searchUserArrayList.add(new SearchUser(jsonArray.getJSONObject(i)));
                        }

                        if (!searchUserArrayList.isEmpty()) {
                            //tv_notFound.setVisibility(View.GONE);
                            activityFeedDetailsBinding.rvSearchUser.setVisibility(VISIBLE);
                            activityFeedDetailsBinding.rlMain.setVisibility(GONE);
                            activityFeedDetailsBinding.rvSearchUser.setAdapter(new SearchUserAdapter(mContext, searchUserArrayList, username -> {
                                searchUsername = username;
                                activityFeedDetailsBinding.rvSearchUser.setVisibility(GONE);
                                activityFeedDetailsBinding.rlMain.setVisibility(VISIBLE);
                                if (activityFeedDetailsBinding.editComment.getText().toString().trim().length() > 0) {


                                    Matcher matcher = Pattern.compile("@\\s*(\\w+)").matcher(activityFeedDetailsBinding.editComment.getText().toString().trim());
                                    String find = "";
                                    while (matcher.find()) {
                                        find = matcher.group(1);
                                    }

                                    StringBuffer stringBuffer = new StringBuffer();

                                    String newText = "";
                                    newText = remove(activityFeedDetailsBinding.editComment.getText().toString().trim(), "@" + find);
                                    stringBuffer.append(newText);
                                    stringBuffer.append(searchUsername);

                                    activityFeedDetailsBinding.editComment.setText(stringBuffer);

                                    activityFeedDetailsBinding.editComment.setSelection(activityFeedDetailsBinding.editComment.getText().toString().length());

                                } else {
                                    activityFeedDetailsBinding.rvSearchUser.setVisibility(GONE);
                                    activityFeedDetailsBinding.rlMain.setVisibility(VISIBLE);
                                    activityFeedDetailsBinding.editComment.setText(searchUsername);
                                }
                            }));
                        } else {
                            //tv_notFound.setVisibility(View.VISIBLE);
                            activityFeedDetailsBinding.rvSearchUser.setVisibility(View.GONE);
                        }
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(mActivity, jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, error -> {
            error.printStackTrace();
            //loaderView.hideLoader();
            Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", "1387");
                param.put("s", "d46e3041de65228dfe11f66de56f5f02c22e6cf639f96877e600810a3075ec1ddd53728122917009d19a006fd6d25d23c93d3bf4e48eb25f");
                param.put("search_key", searchKey);

                Log.e("Pavilion", param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    public String remove(String str, String word) {
        String strNew = str.substring(0, str.length() - word.length());

        System.out.print(strNew);
        return strNew;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}