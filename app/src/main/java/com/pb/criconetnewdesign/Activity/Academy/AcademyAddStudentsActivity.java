package com.pb.criconetnewdesign.Activity.Academy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.databinding.ActivityAcademyAddStudentsBinding;
import com.pb.criconetnewdesign.databinding.ToolbarInnerpageBinding;
import com.pb.criconetnewdesign.util.BookingHistoryDropDown;
import com.pb.criconetnewdesign.util.DataModel;
import com.pb.criconetnewdesign.util.DropDownBlue;
import com.pb.criconetnewdesign.util.LanguageDropDown;

import java.util.ArrayList;

public class AcademyAddStudentsActivity extends AppCompatActivity {
    ActivityAcademyAddStudentsBinding activityAcademyAddStudentsBinding;
    Context mContext;
    Activity mActivity;
    private ArrayList<DataModel> option_player_type = new ArrayList<>();
    private ArrayList<DataModel> option_batting_hand = new ArrayList<>();
    private ArrayList<DataModel> option_bowling_arm = new ArrayList<>();
    private ArrayList<DataModel> option_ball_type = new ArrayList<>();
    private ArrayList<DataModel> option_age_group = new ArrayList<>();
    private ArrayList<DataModel> option_gender = new ArrayList<>();

    String filterTypeStatus="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAcademyAddStudentsBinding = ActivityAcademyAddStudentsBinding.inflate(getLayoutInflater());
        setContentView(activityAcademyAddStudentsBinding.getRoot());
        mContext = this;
        mActivity = this;

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityAcademyAddStudentsBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.add_students));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        inItView();

    }

    private void inItView() {

        //todo player type
        option_player_type.add(new DataModel("Batsman"));
        option_player_type.add(new DataModel("Bowler"));
        option_player_type.add(new DataModel("All Rounder"));
        option_player_type.add(new DataModel("Wicket Keeper"));
        activityAcademyAddStudentsBinding.dropSelectPlayerType.setOptionList(option_player_type);
        activityAcademyAddStudentsBinding.dropSelectPlayerType.setClickListener(new DropDownBlue.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name) {


            }


            @Override
            public void onDismiss() {
            }
        });

        //todo Batting Hand
        option_batting_hand.add(new DataModel("Right Hand"));
        option_batting_hand.add(new DataModel("Left Hand"));
        activityAcademyAddStudentsBinding.dropBattingType.setOptionList(option_batting_hand);
        activityAcademyAddStudentsBinding.dropBattingType.setClickListener(new DropDownBlue.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name) {


            }


            @Override
            public void onDismiss() {
            }
        });

        //todo Bowling Arm
        option_bowling_arm.add(new DataModel("Right Arm"));
        option_bowling_arm.add(new DataModel("Left Arm"));
        activityAcademyAddStudentsBinding.dropBowlingArm.setOptionList(option_bowling_arm);
        activityAcademyAddStudentsBinding.dropBowlingArm.setClickListener(new DropDownBlue.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name) {


            }


            @Override
            public void onDismiss() {
            }
        });

        //todo Ball Type
        option_ball_type.add(new DataModel("Leather"));
        option_ball_type.add(new DataModel("Tennis"));
        activityAcademyAddStudentsBinding.dropBallType.setOptionList(option_ball_type);
        activityAcademyAddStudentsBinding.dropBallType.setClickListener(new DropDownBlue.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name) {


            }


            @Override
            public void onDismiss() {
            }
        });


        //todo Age Group
        option_age_group.add(new DataModel("Under 14"));
        option_age_group.add(new DataModel("Under 16"));
        option_age_group.add(new DataModel("Under 19"));
        option_age_group.add(new DataModel("Under 23"));
        option_age_group.add(new DataModel("Above 23"));
        activityAcademyAddStudentsBinding.dropAgeGroup.setOptionList(option_age_group);
        activityAcademyAddStudentsBinding.dropAgeGroup.setClickListener(new DropDownBlue.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name) {


            }


            @Override
            public void onDismiss() {
            }
        });

        //todo Gender
        option_gender.add(new DataModel("Male"));
        option_gender.add(new DataModel("Female"));
        option_gender.add(new DataModel("Other"));
        activityAcademyAddStudentsBinding.dropSelectGender.setOptionList(option_player_type);
        activityAcademyAddStudentsBinding.dropSelectGender.setClickListener(new DropDownBlue.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name) {


            }


            @Override
            public void onDismiss() {
            }
        });
    }
}