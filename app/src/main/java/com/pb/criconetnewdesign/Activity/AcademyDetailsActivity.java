package com.pb.criconetnewdesign.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.adapter.AcademyAdapter.SliderImageAdapter;
import com.pb.criconetnewdesign.adapter.PavilionAdapter.BlogCommentsAdapter;
import com.pb.criconetnewdesign.databinding.ActivityAcademyDetailsBinding;
import com.pb.criconetnewdesign.databinding.ActivityBlogDetailsBinding;
import com.pb.criconetnewdesign.databinding.ToolbarInnerpageBinding;
import com.pb.criconetnewdesign.model.AcademyModel.SliderImageData;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Objects;

public class AcademyDetailsActivity extends AppCompatActivity {

    ActivityAcademyDetailsBinding activityAcademyDetailsBinding;
    Context mContext;
    Activity mActivity;
    ArrayList<Integer> sliderDataArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academy_details);
        activityAcademyDetailsBinding = ActivityAcademyDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityAcademyDetailsBinding.getRoot());
        mContext = this;
        mActivity = this;

        inItView();
    }

    private void inItView() {
        ToolbarInnerpageBinding toolbarInnerpageBinding = activityAcademyDetailsBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(getResources().getString(R.string.e_academy));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        // we are creating array list for storing our image urls.



        // adding the urls inside array list
        sliderDataArrayList.add(R.drawable.academy_bg_avatar);
        sliderDataArrayList.add(R.drawable.myblogimage);
        sliderDataArrayList.add(R.drawable.academy_bg_avatar);
        sliderDataArrayList.add(R.drawable.myblogimage);


        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.
        activityAcademyDetailsBinding.slider.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        // below method is used to
        // setadapter to sliderview.
        activityAcademyDetailsBinding.slider.setSliderAdapter(new SliderImageAdapter(this, sliderDataArrayList));

        // below method is use to set
        // scroll time in seconds.
        activityAcademyDetailsBinding.slider.setScrollTimeInSec(3);

        // to set it scrollable automatically
        // we use below method.
        activityAcademyDetailsBinding.slider.setAutoCycle(true);

        // to start autocycle below method is used.
        activityAcademyDetailsBinding.slider.startAutoCycle();



    }
}