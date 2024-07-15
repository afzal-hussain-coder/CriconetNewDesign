package com.pb.criconet.Activity.Coach;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.pb.criconet.R;
import com.pb.criconet.adapter.EcoachingAdapter.EndSessionFeedbackAdapter;
import com.pb.criconet.databinding.ActivityCoachFeedBackReviewBinding;
import com.pb.criconet.databinding.ToolbarReviewBinding;
import com.pb.criconet.model.Coaching.FeedBackFormChildData;

import java.util.ArrayList;

public class CoachFeedBackReviewActivity extends AppCompatActivity {

    ActivityCoachFeedBackReviewBinding activityCoachFeedBackReviewBinding;
    Context mContext;
    Activity mActivity;
    private ArrayList<FeedBackFormChildData> feedbackModelList = new ArrayList<>();
    int a1,a2,a3,a4,a5,a6,a7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCoachFeedBackReviewBinding = ActivityCoachFeedBackReviewBinding.inflate(getLayoutInflater());
        setContentView(activityCoachFeedBackReviewBinding.getRoot());

        mContext = this;
        mActivity = this;

        ToolbarReviewBinding toolbarReviewBinding = activityCoachFeedBackReviewBinding.toolbar;
        toolbarReviewBinding.toolbartext.setText(mContext.getResources().getString(R.string.coach_review));
        toolbarReviewBinding.imgBack.setOnClickListener(v -> finish());

        initView();
    }

    private void initView() {
        activityCoachFeedBackReviewBinding.recyclerFeedback.setLayoutManager(new LinearLayoutManager(this));
        activityCoachFeedBackReviewBinding.recyclerFeedback.setHasFixedSize(true);
        activityCoachFeedBackReviewBinding.recyclerFeedback.setAdapter(new EndSessionFeedbackAdapter(getQuestionList(), mContext, (q1, v1) -> {
            for (int i = 1; i <= feedbackModelList.size(); i++) {

               // SessionManager.getProfileType(pref)
                String profileType="user";
                if (profileType.equalsIgnoreCase("coach")) {
                    if (q1 == 1) {
                        a5 = v1;
                    } else if (q1 == 2) {
                        a6 = v1;
                    } else if (q1 == 3) {
                        a7 = v1;
                    }
                } else {
                    if (q1 == 1) {
                        a1 = v1;
                    } else if (q1 == 2) {
                        a2 = v1;
                    } else if (q1 == 3) {
                        a3 = v1;
                    } else if (q1 == 4) {
                        a4 = v1;
                    }
                }


            }
        }));

        if (getQuestionList().size() > 0) {
            activityCoachFeedBackReviewBinding.tvLastPosition.setText(getQuestionList().size() + 1 + "." + getResources().getString(R.string.Do_you_have_any_suggestions));
        }

        activityCoachFeedBackReviewBinding.flSubmit.setOnClickListener(v -> {
         startActivity(new Intent(mContext,RegisterAsAnECoachActivity.class));
        });

    }

    public ArrayList<FeedBackFormChildData> getQuestionList(){
        ArrayList<FeedBackFormChildData>feedBackFormChildDataList = new ArrayList<>();
        feedBackFormChildDataList.add(new FeedBackFormChildData("How well was the coach prepared for the session?",
                "Not prepared at all","Very well prepared"));
        feedBackFormChildDataList.add(new FeedBackFormChildData("How beneficial was the session for you?",
                "Not beneficial at all","Very beneficial"));
        feedBackFormChildDataList.add(new FeedBackFormChildData("How easy was it to learn from the session?",
                "Not smooth at all","Very smooth"));
        feedBackFormChildDataList.add(new FeedBackFormChildData("How well was the coach prepared for the session?",
                "Not prepared at all","Very well prepared"));
        return feedBackFormChildDataList;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}