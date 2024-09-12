package com.pb.criconet.Activity.Academy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.chip.Chip;
import com.pb.criconet.R;
import com.pb.criconet.adapter.AcademyAdapter.MediaRecyclerAdapter;
import com.pb.criconet.adapter.AcademyAdapter.SliderImageAdapter;
import com.pb.criconet.adapter.PavilionAdapter.BlogCommentsAdapter;
import com.pb.criconet.databinding.ActivityAcademyDetailsBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.model.AcademyModel.AcademyListModel;
import com.pb.criconet.model.AcademyModel.SliderImageData;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.HorizontalSpaceItemDecoration;
import com.pb.criconet.util.SessionManager;
import com.pb.criconet.util.Toaster;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import timber.log.Timber;

public class AcademyDetailsActivity extends AppCompatActivity {

    ActivityAcademyDetailsBinding activityAcademyDetailsBinding;
    Context mContext;
    Activity mActivity;
    ArrayList<String> sliderDataArrayList = new ArrayList<>();
    ArrayList<Integer> sliderDataArrayListt = new ArrayList<>();
    AcademyListModel academyListModel;
    String academyId="";
    ArrayList<AcademyListModel.AcademyCoaches> academyCoachesArrayList = null;
    String FROM ="";

    private RequestQueue queue;
    CustomLoaderView loaderView;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academy_details);
        activityAcademyDetailsBinding = ActivityAcademyDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityAcademyDetailsBinding.getRoot());
        mContext = this;
        mActivity = this;

        loaderView = CustomLoaderView.initialize(mContext);
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        queue = Volley.newRequestQueue(mContext);

        inItView();
        getIntentValue();
    }

    private void inItView() {
        ToolbarInnerpageBinding toolbarInnerpageBinding = activityAcademyDetailsBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(getResources().getString(R.string.e_academy));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        activityAcademyDetailsBinding.slider.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        activityAcademyDetailsBinding.slider.setScrollTimeInSec(3);
        activityAcademyDetailsBinding.slider.setAutoCycle(true);
        activityAcademyDetailsBinding.slider.startAutoCycle();


        activityAcademyDetailsBinding.ivFacebook.setOnClickListener(v -> openSocialMedia("https://www.facebook.com/criconetonline"));

        activityAcademyDetailsBinding.ivInstagram.setOnClickListener(v -> openSocialMedia("https://x.com/i/flow/login?redirect_after_login=%2Fcriconetonline"));

        activityAcademyDetailsBinding.ivLinkend.setOnClickListener(v -> openSocialMedia("https://www.linkedin.com/uas/login?session_redirect=%2Fcompany%2F13448164"));

        activityAcademyDetailsBinding.ivYouTube.setOnClickListener(v -> openSocialMedia("https://www.youtube.com/@criconetonline4849"));

        activityAcademyDetailsBinding.flContact.setOnClickListener(v -> {
            startActivity(new Intent(mContext, AcademyContactUsActivity.class).putExtra("FROM", "2").putExtra("ACADEMY_ID", academyId));

        });

        activityAcademyDetailsBinding.flCoachList.setOnClickListener(v -> {

            startActivity(new Intent(mContext, AcademyCoachListActivity.class).putExtra("ACADEMY_LIST", academyCoachesArrayList));
        });


    }

    private void getIntentValue() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            FROM = bundle.getString("FROM");

            if(FROM.equalsIgnoreCase("1")){
                academyListModel = (AcademyListModel) bundle.getSerializable("AcademyDetails");
                academyId = academyListModel.getId();

                if (academyListModel.getImageArryList().size() == 0) {
                    sliderDataArrayListt.add(R.drawable.academy_bg_avatar);
                    sliderDataArrayListt.add(R.drawable.myblogimage);
                    sliderDataArrayListt.add(R.drawable.academy_bg_avatar);
                    sliderDataArrayListt.add(R.drawable.myblogimage);
                } else {
                    sliderDataArrayList = academyListModel.getImageArryList();
                }

                academyCoachesArrayList = academyListModel.getAcademyCoachesArrayList();


                activityAcademyDetailsBinding.slider.setSliderAdapter(new SliderImageAdapter(this, sliderDataArrayList, sliderDataArrayListt));

                activityAcademyDetailsBinding.tvName.setText(academyListModel.getName());
                activityAcademyDetailsBinding.tvAddress.setText(academyListModel.getAddress());
                activityAcademyDetailsBinding.tvPhone.setText(academyListModel.getContact_person_phone());

                String[] categoriesArray = academyListModel.getCat_title().split(",");


                ArrayList<String> categoriesList = new ArrayList<>(Arrays.asList(categoriesArray));

                for (String chipText : categoriesList) {
                    Chip chip = new Chip(mContext);
                    chip.setText(chipText);
                    chip.setChipBackgroundColorResource(R.color.white);
                    chip.setChipStrokeColorResource(R.color.purple_700);
                    chip.setChipStrokeWidth(2.0f);
                    chip.setTextAppearance(R.style.MyChipTextAppearance);
                    activityAcademyDetailsBinding.chipSpecializationGroupList.addView(chip);
                }

                activityAcademyDetailsBinding.tvFees.setText("Fees- " + academyListModel.getFee() + "/" + "Month");
                activityAcademyDetailsBinding.tvTrainingType.setText("Training Type- " + academyListModel.getTraining_type());
                activityAcademyDetailsBinding.tvAcademyDetails.setText(academyListModel.getAbout());

                activityAcademyDetailsBinding.tvlanguages.setText(academyListModel.getLang());


                activityAcademyDetailsBinding.exoPlayerRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1, LinearLayoutManager.HORIZONTAL, false));
                activityAcademyDetailsBinding.exoPlayerRecyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(5));

                if (academyListModel.getVideoList().isEmpty() || academyListModel.getVideoList() == null) {
                    activityAcademyDetailsBinding.exoPlayerRecyclerView.setVisibility(View.GONE);
                    activityAcademyDetailsBinding.exoPlayerRecyclerView.setMediaObjects(academyListModel.getVideoList());
                    activityAcademyDetailsBinding.exoPlayerRecyclerView.setAdapter(new MediaRecyclerAdapter(new ArrayList<>(), initGlide()));

                } else {
                    activityAcademyDetailsBinding.exoPlayerRecyclerView.setVisibility(View.VISIBLE);
                    activityAcademyDetailsBinding.exoPlayerRecyclerView.setMediaObjects(academyListModel.getVideoList());
                    activityAcademyDetailsBinding.exoPlayerRecyclerView.setAdapter(new MediaRecyclerAdapter(academyListModel.getVideoList(), initGlide()));
                    //rv_video.smoothScrollToPosition(0);

                }
            }else{
                academyId = bundle.getString("AcademyId");
                if (Global.isOnline(mActivity)) {
                    getUsersDetails();
                } else {
                    Global.showDialog(mActivity);
                }
            }


        }
    }

    private RequestManager initGlide() {
        RequestOptions options = new RequestOptions();
        return Glide.with(this)
                .setDefaultRequestOptions(options);
    }

    private void openSocialMedia(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (activityAcademyDetailsBinding.exoPlayerRecyclerView != null) {
            activityAcademyDetailsBinding.exoPlayerRecyclerView.onPausePlayer();
        }
    }

    @Override
    protected void onDestroy() {
        if (activityAcademyDetailsBinding.exoPlayerRecyclerView != null) {
            activityAcademyDetailsBinding.exoPlayerRecyclerView.releasePlayer();
        }
        super.onDestroy();
    }

    public void getUsersDetails() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.GET_ACADEMY_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("AcademyDetails",response);
                        Timber.d(response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {

                                if (jsonObject.has("data")) {
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                                    AcademyListModel academyListModel;
                                    academyListModel = new AcademyListModel(jsonObject1);
                                    setData(academyListModel);
                                }

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(mActivity, jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loaderView.hideLoader();
                                }
                            },8000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> {
                    error.printStackTrace();
                    loaderView.hideLoader();
                    Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
//                Global.msgDialog(EditProfile.this, "Internet connection is slow");
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("academy_id", academyId);
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

    public void setData(AcademyListModel academyListModel){
        academyId = academyListModel.getId();

        if (academyListModel.getImageArryList().size() == 0) {
            sliderDataArrayListt.add(R.drawable.academy_bg_avatar);
            sliderDataArrayListt.add(R.drawable.myblogimage);
            sliderDataArrayListt.add(R.drawable.academy_bg_avatar);
            sliderDataArrayListt.add(R.drawable.myblogimage);
        } else {
            sliderDataArrayList = academyListModel.getImageArryList();
        }

        academyCoachesArrayList = academyListModel.getAcademyCoachesArrayList();


        activityAcademyDetailsBinding.slider.setSliderAdapter(new SliderImageAdapter(this, sliderDataArrayList, sliderDataArrayListt));

        activityAcademyDetailsBinding.tvName.setText(academyListModel.getName());
        activityAcademyDetailsBinding.tvAddress.setText(academyListModel.getAddress());
        activityAcademyDetailsBinding.tvPhone.setText(academyListModel.getContact_person_phone());

        String[] categoriesArray = academyListModel.getCat_title().split(",");


        ArrayList<String> categoriesList = new ArrayList<>(Arrays.asList(categoriesArray));

        for (String chipText : categoriesList) {
            Chip chip = new Chip(mContext);
            chip.setText(chipText);
            chip.setChipBackgroundColorResource(R.color.white);
            chip.setChipStrokeColorResource(R.color.purple_700);
            chip.setChipStrokeWidth(2.0f);
            chip.setTextAppearance(R.style.MyChipTextAppearance);
            activityAcademyDetailsBinding.chipSpecializationGroupList.addView(chip);
        }

        activityAcademyDetailsBinding.tvFees.setText("Fees- " + academyListModel.getFee() + "/" + "Month");
        activityAcademyDetailsBinding.tvTrainingType.setText("Training Type- " + academyListModel.getTraining_type());
        activityAcademyDetailsBinding.tvAcademyDetails.setText(academyListModel.getAbout());

        activityAcademyDetailsBinding.tvlanguages.setText(academyListModel.getLang());


        activityAcademyDetailsBinding.exoPlayerRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1, LinearLayoutManager.HORIZONTAL, false));
        activityAcademyDetailsBinding.exoPlayerRecyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(5));

        if (academyListModel.getVideoList().isEmpty() || academyListModel.getVideoList() == null) {
            activityAcademyDetailsBinding.exoPlayerRecyclerView.setVisibility(View.GONE);
            activityAcademyDetailsBinding.exoPlayerRecyclerView.setMediaObjects(academyListModel.getVideoList());
            activityAcademyDetailsBinding.exoPlayerRecyclerView.setAdapter(new MediaRecyclerAdapter(new ArrayList<>(), initGlide()));

        } else {
            activityAcademyDetailsBinding.exoPlayerRecyclerView.setVisibility(View.VISIBLE);
            activityAcademyDetailsBinding.exoPlayerRecyclerView.setMediaObjects(academyListModel.getVideoList());
            activityAcademyDetailsBinding.exoPlayerRecyclerView.setAdapter(new MediaRecyclerAdapter(academyListModel.getVideoList(), initGlide()));
            //rv_video.smoothScrollToPosition(0);

        }
    }
}