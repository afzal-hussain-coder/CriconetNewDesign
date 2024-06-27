package com.pb.criconetnewdesign.Activity.Academy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.chip.Chip;
import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.adapter.AcademyAdapter.MediaRecyclerAdapter;
import com.pb.criconetnewdesign.adapter.AcademyAdapter.SliderImageAdapter;
import com.pb.criconetnewdesign.adapter.PavilionAdapter.BlogCommentsAdapter;
import com.pb.criconetnewdesign.databinding.ActivityAcademyDetailsBinding;
import com.pb.criconetnewdesign.databinding.ActivityBlogDetailsBinding;
import com.pb.criconetnewdesign.databinding.ToolbarInnerpageBinding;
import com.pb.criconetnewdesign.model.AcademyModel.AcademyListModel;
import com.pb.criconetnewdesign.model.AcademyModel.SliderImageData;
import com.pb.criconetnewdesign.util.Global;
import com.pb.criconetnewdesign.util.HorizontalSpaceItemDecoration;
import com.pb.criconetnewdesign.util.Toaster;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class AcademyDetailsActivity extends AppCompatActivity {

    ActivityAcademyDetailsBinding activityAcademyDetailsBinding;
    Context mContext;
    Activity mActivity;
    ArrayList<String> sliderDataArrayList = new ArrayList<>();
    ArrayList<Integer> sliderDataArrayListt = new ArrayList<>();
    AcademyListModel academyListModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academy_details);
        activityAcademyDetailsBinding = ActivityAcademyDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityAcademyDetailsBinding.getRoot());
        mContext = this;
        mActivity = this;

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

        activityAcademyDetailsBinding.ivYouTube.setOnClickListener(v -> openSocialMedia("https://www.instagram.com/criconet/"));

        activityAcademyDetailsBinding.flContact.setOnClickListener(v -> {
            Global.callApp(academyListModel.getContact_person_phone(), mContext);
        });


    }

    private void getIntentValue() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            academyListModel = (AcademyListModel) bundle.getSerializable("AcademyDetails");

            if (academyListModel.getImageArryList().size() == 0) {
                sliderDataArrayListt.add(R.drawable.academy_bg_avatar);
                sliderDataArrayListt.add(R.drawable.myblogimage);
                sliderDataArrayListt.add(R.drawable.academy_bg_avatar);
                sliderDataArrayListt.add(R.drawable.myblogimage);
            } else {
                sliderDataArrayList = academyListModel.getImageArryList();
            }


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
}