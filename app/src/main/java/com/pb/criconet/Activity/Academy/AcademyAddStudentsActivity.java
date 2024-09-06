package com.pb.criconet.Activity.Academy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pb.criconet.R;
import com.pb.criconet.databinding.ActivityAcademyAddStudentsBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.model.AcademyModel.AcademyStudenModel;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.DataModel;
import com.pb.criconet.util.DropDownBlue;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.SessionManager;
import com.pb.criconet.util.Toaster;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class AcademyAddStudentsActivity extends AppCompatActivity {
    ActivityAcademyAddStudentsBinding activityAcademyAddStudentsBinding;
    Context mContext;
    Activity mActivity;
    private RequestQueue queue;
    CustomLoaderView loaderView;
    private SharedPreferences prefs;


    private ArrayList<DataModel> option_player_type = new ArrayList<>();
    private ArrayList<DataModel> option_batting_hand = new ArrayList<>();
    private ArrayList<DataModel> option_bowling_arm = new ArrayList<>();
    private ArrayList<DataModel> option_ball_type = new ArrayList<>();
    private ArrayList<DataModel> option_age_group = new ArrayList<>();
    private ArrayList<DataModel> option_gender = new ArrayList<>();

    String filterTypeStatus="";
    String FROM="";
    AcademyStudenModel academyStudenModel;
    String userId_updated = "";
    ToolbarInnerpageBinding toolbarInnerpageBinding;
    String ageGroup ="";
    String gender="";
    String ballType="";
    String battingHand="";
    String ballingArm="";
    String playerType ="";
    String student_id ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAcademyAddStudentsBinding = ActivityAcademyAddStudentsBinding.inflate(getLayoutInflater());
        setContentView(activityAcademyAddStudentsBinding.getRoot());
        mContext = this;
        mActivity = this;

        toolbarInnerpageBinding = activityAcademyAddStudentsBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.add_students));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        loaderView = CustomLoaderView.initialize(mContext);
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        queue = Volley.newRequestQueue(mContext);

        inItView();
        getIntentValue();
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
            public void onClickDone(String name,String id) {
                playerType = name;

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
            public void onClickDone(String name,String id) {
                battingHand = name;

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
            public void onClickDone(String name,String id) {
                ballingArm = name;

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
            public void onClickDone(String name,String id) {
                ballType = name;

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
            public void onClickDone(String name,String id) {
                ageGroup = name;

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
            public void onClickDone(String name,String id) {
                gender = name;

            }


            @Override
            public void onDismiss() {
            }
        });


        activityAcademyAddStudentsBinding.editTextFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAcademyAddStudentsBinding.filledTextFieldFirstName.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAcademyAddStudentsBinding.filledTextFieldFirstName.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAcademyAddStudentsBinding.filledTextFieldFirstName.setErrorEnabled(false);
            }
        });

        activityAcademyAddStudentsBinding.editTextLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAcademyAddStudentsBinding.filledTextFieldLastName.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAcademyAddStudentsBinding.filledTextFieldLastName.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAcademyAddStudentsBinding.filledTextFieldLastName.setErrorEnabled(false);
            }
        });

        activityAcademyAddStudentsBinding.editTextUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAcademyAddStudentsBinding.filledTextFieldUserName.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAcademyAddStudentsBinding.filledTextFieldUserName.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAcademyAddStudentsBinding.filledTextFieldUserName.setErrorEnabled(false);
            }
        });

        activityAcademyAddStudentsBinding.emailInputLayoutEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAcademyAddStudentsBinding.textInputLayoutEmail.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAcademyAddStudentsBinding.textInputLayoutEmail.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAcademyAddStudentsBinding.textInputLayoutEmail.setErrorEnabled(false);
            }
        });

        activityAcademyAddStudentsBinding.phoneInputLayoutEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAcademyAddStudentsBinding.phoneInputLayou.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAcademyAddStudentsBinding.phoneInputLayou.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAcademyAddStudentsBinding.phoneInputLayou.setErrorEnabled(false);
            }
        });


        activityAcademyAddStudentsBinding.flSubmit.setOnClickListener(v -> {
            if (checkValidation()) {
                if (Global.isOnline(mContext)) {
                    addAcademyStudent();
                } else {
                    Global.showDialog(mActivity);
                }
            }
        });


    }

    private void getIntentValue() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            FROM = extras.getString("FROM");
            if ((getIntent().getBundleExtra("Certificate") == null)) {
            } else {
                Bundle args = getIntent().getBundleExtra("Certificate");
                academyStudenModel = (AcademyStudenModel) args.getSerializable("ARRAYLIST");
                userId_updated = academyStudenModel.getUser_id();
                if (userId_updated.equalsIgnoreCase("")) {
                    toolbarInnerpageBinding.toolbartext.setText(getResources().getString(R.string.add_new_students));
                } else {
                    toolbarInnerpageBinding.toolbartext.setText(getResources().getString(R.string.edit_new_students));
                }

                activityAcademyAddStudentsBinding.profileLinkInputLayoutEditText.setText(academyStudenModel.getCricheroes_profile_link());
                SessionManager.save_cricheroes_profile_link(prefs, academyStudenModel.getCricheroes_profile_link());

                activityAcademyAddStudentsBinding.editTextFirstName.setText(academyStudenModel.getFirst_name());
                activityAcademyAddStudentsBinding.editTextMiddleName.setText(academyStudenModel.getMid_name());
                activityAcademyAddStudentsBinding.editTextLastName.setText(academyStudenModel.getLast_name());

                activityAcademyAddStudentsBinding.phoneInputLayoutEditText.setText(academyStudenModel.getPhone_number());
                activityAcademyAddStudentsBinding.passwordInputLayou.setVisibility(View.GONE);
                activityAcademyAddStudentsBinding.editTextUserName.setText(academyStudenModel.getUsername());
                activityAcademyAddStudentsBinding.editTextUserName.setClickable(false);
                activityAcademyAddStudentsBinding.editTextUserName.setEnabled(false);


                activityAcademyAddStudentsBinding.emailInputLayoutEmail.setText(academyStudenModel.getEmail());
                activityAcademyAddStudentsBinding.emailInputLayoutEmail.setClickable(false);
                activityAcademyAddStudentsBinding.emailInputLayoutEmail.setEnabled(false);
                ageGroup = academyStudenModel.getAge_group();
                activityAcademyAddStudentsBinding.dropAgeGroup.setText(ageGroup);
                gender = academyStudenModel.getGender();
                activityAcademyAddStudentsBinding.dropSelectGender.setText(gender);
                ballType = academyStudenModel.getType_of_ball();
                activityAcademyAddStudentsBinding.dropBallType.setText(ballType);
                battingHand = academyStudenModel.getBatting_hand();
                activityAcademyAddStudentsBinding.dropBattingType.setText(battingHand);
                ballingArm = academyStudenModel.getBowling_arm();
                activityAcademyAddStudentsBinding.dropBowlingArm.setText(ballingArm);
                playerType = academyStudenModel.getPlayer_type();
                activityAcademyAddStudentsBinding.dropSelectPlayerType.setText(playerType);
                student_id = academyStudenModel.getStudent_id();
                //Toaster.customToast(student_id);
            }

        }
    }

    private boolean checkValidation() {

        if (!Global.validateLengthofCoachRegisterr(activityAcademyAddStudentsBinding.editTextFirstName.getText().toString().trim())) {
            activityAcademyAddStudentsBinding.filledTextFieldFirstName.setErrorEnabled(true);
            activityAcademyAddStudentsBinding.filledTextFieldFirstName.setError("Name can't be empty!");
            activityAcademyAddStudentsBinding.filledTextFieldFirstName.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            return false;
        }
        else if (!Global.validateLengthofCoachRegisterr(activityAcademyAddStudentsBinding.editTextLastName.getText().toString().trim())) {
            activityAcademyAddStudentsBinding.filledTextFieldLastName.setErrorEnabled(true);
            activityAcademyAddStudentsBinding.filledTextFieldLastName.setError(mContext.getResources().getString(R.string.Enter_Last_Name_at_least_3_character));
            activityAcademyAddStudentsBinding.filledTextFieldFirstName.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            return false;
        } else if (!Global.validateLengthofCoachRegisterr(activityAcademyAddStudentsBinding.editTextUserName.getText().toString().trim())) {
            activityAcademyAddStudentsBinding.filledTextFieldUserName.setErrorEnabled(true);
            activityAcademyAddStudentsBinding.filledTextFieldUserName.setError(mContext.getResources().getString(R.string.enter_userName));
            activityAcademyAddStudentsBinding.filledTextFieldUserName.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            return false;
        }
        else if (activityAcademyAddStudentsBinding.emailInputLayoutEmail.getText().toString().trim().isEmpty()) {
            activityAcademyAddStudentsBinding.textInputLayoutEmail.setErrorEnabled(true);
            activityAcademyAddStudentsBinding.textInputLayoutEmail.setError(mContext.getResources().getString(R.string.Invalid_email_formatt));
            activityAcademyAddStudentsBinding.textInputLayoutEmail.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            return false;
        } else if (!Global.isValidEmailAddress(activityAcademyAddStudentsBinding.emailInputLayoutEmail.getText().toString().trim())) {
            activityAcademyAddStudentsBinding.textInputLayoutEmail.setErrorEnabled(true);
            activityAcademyAddStudentsBinding.textInputLayoutEmail.setError(mContext.getResources().getString(R.string.Invalid_email_formatt));
            activityAcademyAddStudentsBinding.textInputLayoutEmail.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            return false;
        }
        else if (activityAcademyAddStudentsBinding.phoneInputLayoutEditText.getText().toString().equalsIgnoreCase("0000000000") || activityAcademyAddStudentsBinding.phoneInputLayoutEditText.getText().toString().startsWith("1111111111") ||
                activityAcademyAddStudentsBinding.phoneInputLayoutEditText.getText().toString().equalsIgnoreCase("2222222222")
                || activityAcademyAddStudentsBinding.phoneInputLayoutEditText.getText().toString().equalsIgnoreCase("3333333333") ||
                activityAcademyAddStudentsBinding.phoneInputLayoutEditText.getText().toString().equalsIgnoreCase("4444444444") ||
                activityAcademyAddStudentsBinding.phoneInputLayoutEditText.getText().toString().equalsIgnoreCase("5555555555") ||
                activityAcademyAddStudentsBinding.phoneInputLayoutEditText.getText().toString().equalsIgnoreCase("6666666666")
                || activityAcademyAddStudentsBinding.phoneInputLayoutEditText.getText().toString().equalsIgnoreCase("7777777777") ||
                activityAcademyAddStudentsBinding.phoneInputLayoutEditText.getText().toString().equalsIgnoreCase("8888888888") ||
                activityAcademyAddStudentsBinding.phoneInputLayoutEditText.getText().toString().equalsIgnoreCase("9999999999")) {

            activityAcademyAddStudentsBinding.phoneInputLayou.setErrorEnabled(true);
            activityAcademyAddStudentsBinding.phoneInputLayou.setError(mContext.getResources().getString(R.string.enter_phone_number));
            activityAcademyAddStudentsBinding.phoneInputLayou.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));

            return false;
        } else if (!Global.isValidMobile(activityAcademyAddStudentsBinding.phoneInputLayoutEditText.getText().toString())) {
            activityAcademyAddStudentsBinding.phoneInputLayou.setErrorEnabled(true);
            activityAcademyAddStudentsBinding.phoneInputLayou.setError(mContext.getResources().getString(R.string.enter_phone_number));
            activityAcademyAddStudentsBinding.phoneInputLayou.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));

            return false;
        }
//        else if (!Global.password(password) && edttxt_password.getVisibility() == View.VISIBLE) {
//            Toaster.customToast(mContext.getResources().getString(R.string.password_cannot_empty));
//            return false;
//        }
//        else if (!Global.password(password)) {
//            Toaster.customToast(mContext.getResources().getString(R.string.The_Password_Cant_be_emptyy));
//            return false;
//        }
//          else if (userId_updated.isEmpty()) {
//                Toaster.customToast(mContext.getResources().getString(R.string.The_Password_Cant_be_empty));
//                return false;
//            }
        else if (playerType.isEmpty()) {
            Toaster.customToast(mContext.getResources().getString(R.string.select_player_type));
            return false;
        } else if (battingHand.isEmpty()) {
            Toaster.customToast(mContext.getResources().getString(R.string.select_batting_type));
            return false;
        } else if (ballingArm.isEmpty()) {
            Toaster.customToast(mContext.getResources().getString(R.string.select_balling_arm));
            return false;
        } else if (ballType.isEmpty()) {
            Toaster.customToast(mContext.getResources().getString(R.string.select_ball_type));
            return false;
        } else if (ageGroup.isEmpty()) {
            Toaster.customToast(mContext.getResources().getString(R.string.select_age_group));
            return false;
        } else if (gender.isEmpty()) {
            Toaster.customToast(mContext.getResources().getString(R.string.select_gender));
            return false;
        }


        return true;
    }

    private void addAcademyStudent() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.ACADEMY_ADD_STUDENTS, response -> {
            Timber.d(response);

            try {
                loaderView.hideLoader();
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                    Toaster.customToast(jsonObject.getString("message"));

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (FROM.equalsIgnoreCase("2")) {
                                //startActivity(new Intent(activity, AddScheduleSessionActivity.class));
                                finish();
                            } else {
                                finish();
                            }
                        }
                    },100);



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
              /*  if(userId_updated.isEmpty()){
                    param.put("user_id", SessionManager.get_user_id(prefs));
                }else{
                    param.put("user_id", userId_updated);
                }*/

                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("username", activityAcademyAddStudentsBinding.editTextUserName.getText().toString().trim());
                param.put("email", activityAcademyAddStudentsBinding.emailInputLayoutEmail.getText().toString().trim());
                param.put("password", activityAcademyAddStudentsBinding.passwordInputLayoutEditText.getText().toString().trim());
                param.put("cricheroes_profile_link", activityAcademyAddStudentsBinding.profileLinkInputLayoutEditText.getText().toString().trim());
                param.put("gender", gender);
                param.put("first_name", activityAcademyAddStudentsBinding.editTextFirstName.getText().toString().trim());
                param.put("last_name", activityAcademyAddStudentsBinding.editTextLastName.getText().toString().trim());
                param.put("mid_name", activityAcademyAddStudentsBinding.editTextMiddleName.getText().toString().trim());
                param.put("phone_number", activityAcademyAddStudentsBinding.phoneInputLayoutEditText.getText().toString().trim());
                param.put("player_type", playerType);
                param.put("batting_hand", battingHand);
                param.put("bowling_arm", ballingArm);
                param.put("type_of_ball", ballType);
                param.put("age_group", ageGroup);
                param.put("academy_id", SessionManager.get_academyId(prefs));
                param.put("student_id", student_id);

                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }
}