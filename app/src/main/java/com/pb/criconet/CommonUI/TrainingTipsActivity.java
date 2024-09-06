package com.pb.criconet.CommonUI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.pb.criconet.R;
import com.pb.criconet.adapter.AcademyAdapter.AcademyTipsPreviewAdapter;
import com.pb.criconet.databinding.ActivityTrainingTipsBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.model.AcademyModel.AcademyTipsPreviewModel;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.SessionManager;
import com.pb.criconet.util.Toaster;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class TrainingTipsActivity extends AppCompatActivity {
    ActivityTrainingTipsBinding activityTrainingTipsBinding;
    Context mContext;
    Activity mActivity;

    CustomLoaderView loaderView;
    private RequestQueue queue;
    private SharedPreferences prefs;
    private ArrayList<AcademyTipsPreviewModel> academyTipsPreviewModels;
    String tipsTitleShare="";
    String tipsDetailsShare="";
    Uri shortLink;
    String deeplink="";
    private String academyId = "";
    String FROM = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTrainingTipsBinding = ActivityTrainingTipsBinding.inflate(getLayoutInflater());
        setContentView(activityTrainingTipsBinding.getRoot());

        mContext = this;
        mActivity = this;

        loaderView = CustomLoaderView.initialize(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        queue = Volley.newRequestQueue(this);

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityTrainingTipsBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(R.string.training_ti);
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> {
            finish();
        });

        if (getIntent() != null) {
            FROM = getIntent().getStringExtra("FROM");
           // academyId = getIntent().getStringExtra("ACADEMY_ID");
        }

        if (Global.isOnline(mContext)) {
            getTipsList();
        } else {
            Global.showDialog(mActivity);
        }

        inItView();

    }

    private void inItView() {
        activityTrainingTipsBinding.rcvTips.setHasFixedSize(true);
        activityTrainingTipsBinding.rcvTips.setLayoutManager(new LinearLayoutManager(mContext));


    }


    private void getTipsList() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.TRAINING_TIPS_LIST, response -> {
            Timber.d(response);

            try {
                loaderView.hideLoader();
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("api_status").equalsIgnoreCase("200")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    academyTipsPreviewModels = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        academyTipsPreviewModels.add(new AcademyTipsPreviewModel(jsonArray.getJSONObject(i)));
                    }

                    if (jsonArray.length() == 0) {
                        activityTrainingTipsBinding.tvDataNotFound.setVisibility(View.VISIBLE);
                        activityTrainingTipsBinding.rcvTips.setVisibility(View.GONE);
                    } else {
                        activityTrainingTipsBinding.tvDataNotFound.setVisibility(View.GONE);
                        activityTrainingTipsBinding.rcvTips.setVisibility(View.VISIBLE);
                        activityTrainingTipsBinding.rcvTips.setAdapter(new AcademyTipsPreviewAdapter(FROM, mContext, academyTipsPreviewModels, new AcademyTipsPreviewAdapter.ItemListener() {
                            @Override
                            public void onItemClickk(int size, String _tipsId) {
                                if (Global.isOnline(mContext)) {
                                    deleteGallery(_tipsId);
                                } else {
                                    Global.showDialog(mActivity);
                                }
                            }

                            @Override
                            public void onShareItem(String title, String details, String tipsId, String videoPath) {
                                //TODO here we have to share tips on Third party app..
                                tipsTitleShare = title;
                                tipsDetailsShare = details;

                                generateSharingLink();

                            }

                        }));
                    }


                } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                    Global.msgDialog(mActivity, jsonObject.getJSONObject("errors").getString("error_text"));
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
                param.put("academy_id", SessionManager.get_academyId(prefs));

//                if(academyId.isEmpty()){
//                    param.put("academy_id", SessionManager.get_academyId(prefs));
//                }else{
//                    param.put("academy_id", academyId);
//                }
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void deleteGallery(String _tipsId) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.DELETE_ACADEMY_TIPS, response -> {
            Log.d("ResponseDeleteGallery", response);

            try {
                loaderView.hideLoader();
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                    Toaster.customToast(jsonObject.getString("message"));
                    //new Handler().postDelayed((Runnable) this::finish, 100);
                } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                    Global.msgDialogTips(mActivity, jsonObject.getJSONObject("errors").getString("error_text"));
                } else {
                    Global.msgDialogTips(mActivity, getResources().getString(R.string.error_server));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
            Global.msgDialogTips(mActivity, getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("academy_id", SessionManager.get_academyId(prefs));
                param.put("tips_id", _tipsId);
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }


    /*Share deep link for android and browser*/
    public void generateSharingLink() {

        String deepLink ="";
        if(academyId.isEmpty()){
            deepLink = "https://www.criconet.com/"+"?type=training_tips";
        }else{
            deepLink = "https://www.criconet.com/"+"?type=training_tips"+"/"+academyId;
        }


        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(deepLink))
                .setDomainUriPrefix("https://criconet.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder()
                                .setTitle(tipsTitleShare)
                                .setDescription(tipsDetailsShare)
                                .setImageUrl(Uri.parse("https://www.criconet.com/upload/tips/traing-tips.jpg"))
                                .build())
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.pb.criconet")
                                .setFallbackUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.pb.criconet"))
                                .setMinimumVersion(1)
                                .build())
                .buildShortDynamicLink()
                .addOnCompleteListener(mActivity, task -> {
                    if (task.isSuccessful()) {
                        Uri shortLink = task.getResult().getShortLink();

                        deeplink = tipsTitleShare + "\n" + "\n" + "*" + tipsDetailsShare + "*" + "\n" + "\n"  + shortLink;

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.setPackage("com.whatsapp");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Criconet");
                        intent.putExtra(Intent.EXTRA_TEXT, deeplink);
                        startActivity(Intent.createChooser(intent, "send"));

                    } else {
                        Toaster.customToast("Failed to share event.");
                    }
                });


    }
}