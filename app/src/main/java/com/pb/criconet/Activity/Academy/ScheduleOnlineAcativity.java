package com.pb.criconet.Activity.Academy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pb.criconet.Fragment.CoachFragments.CoachPersonalInformationFragment;
import com.pb.criconet.R;
import com.pb.criconet.adapter.AcademyAdapter.ScheduleAcademyCoachesListAdapter;
import com.pb.criconet.adapter.AcademyAdapter.ScheduleAcademyStudentListAdapter;
import com.pb.criconet.databinding.ActivityScheduleOnlineAcativityBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.model.AcademyModel.AcademyStudenModel;
import com.pb.criconet.model.AcademyModel.ManageCoachesModel;
import com.pb.criconet.model.Language;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.SessionManager;
import com.pb.criconet.util.Toaster;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import timber.log.Timber;

public class ScheduleOnlineAcativity extends AppCompatActivity {
    ActivityScheduleOnlineAcativityBinding activityScheduleOnlineAcativityBinding;
    Context mContext;
    Activity mActivity;
    private SharedPreferences prefs;
    private RequestQueue queue;
    CustomLoaderView loaderView;

    private int year, month, day, hour, minute;
    String date = "";
    Date _selectedDate;
    Date _currentDate;
    String aTime = "",durationTime="",sendDurationTime="";
    String currentDate = "";
    Dialog dialogCoach,dialogStudents;
    String topic = "";


    private JSONArray jsonArrayStudents = new JSONArray();
    private JSONArray jsonArrayCoach = new JSONArray();
    StringBuilder coachStringBuilder,coachNameStringBuilder, studentStringBuilder,studentsNameStringBuilder;
    ArrayList<String> coachIdsArrayList = new ArrayList<>();
    ArrayList<String> coachNameArrayList = new ArrayList<>();
    ArrayList<String> studentsArrayList = new ArrayList<>();
    ArrayList<String> studentsNameArrayList = new ArrayList<>();

    TextView TvDialogHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityScheduleOnlineAcativityBinding = ActivityScheduleOnlineAcativityBinding.inflate(getLayoutInflater());
        setContentView(activityScheduleOnlineAcativityBinding.getRoot());
        mContext = this;
        mActivity = this;

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityScheduleOnlineAcativityBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(R.string.schedule_on);
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> {
            finish();
        });

        loaderView = CustomLoaderView.initialize(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        queue = Volley.newRequestQueue(this);

        dialogCoach = new Dialog(mContext);
        dialogCoach.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialogCoach.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogCoach.setContentView(R.layout.dialog_select_language);
        TvDialogHeader = dialogCoach.findViewById(R.id.tvHeader);
        dialogCoach.setCancelable(false);

        dialogStudents = new Dialog(mContext);
        dialogStudents.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialogStudents.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogStudents.setContentView(R.layout.dialog_select_language);
        TvDialogHeader = dialogCoach.findViewById(R.id.tvHeader);
        dialogCoach.setCancelable(false);

        if (Global.isOnline(this)) {
            getAcademyCoachsList();
        } else {
            Global.showDialog(this);
        }
        if (Global.isOnline(this)) {
            getAcademyStudentsList();
        } else {
            Global.showDialog(this);
        }

        inItView();
    }

    private void inItView() {


        activityScheduleOnlineAcativityBinding.editTextTopic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityScheduleOnlineAcativityBinding.filledTextFieldTopic.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityScheduleOnlineAcativityBinding.filledTextFieldTopic.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityScheduleOnlineAcativityBinding.filledTextFieldTopic.setErrorEnabled(false);
            }
        });

        activityScheduleOnlineAcativityBinding.rlStartDate.setOnClickListener(v -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            final Calendar c = Calendar.getInstance();

            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            currentDate = year + "-" + (month + 1) + "-" + day;

            Calendar twoDaysLater = (Calendar) c.clone();
            twoDaysLater.add(Calendar.DATE, 90);

            DatePickerDialog picker = new DatePickerDialog(mActivity, (datePicker, year, month, day) -> {
                date = year + "-" + (month + 1) + "-" + day;

                try {
                    _currentDate = simpleDateFormat.parse(currentDate);
                    _selectedDate = simpleDateFormat.parse(date);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                activityScheduleOnlineAcativityBinding.tvStartDate.setText(Global.convertUTCDateToMM(date));
            }, year, month, day);
            picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            picker.getDatePicker().setMaxDate(twoDaysLater.getTimeInMillis());

            picker.show();
        });

        activityScheduleOnlineAcativityBinding.rlStartTime.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(mActivity, (timePicker, hour, min) -> {
                Calendar datetime = Calendar.getInstance();
                Calendar c1 = Calendar.getInstance();
                datetime.set(Calendar.HOUR_OF_DAY, hour);
                datetime.set(Calendar.MINUTE, min);

                updateTime(hour, min, activityScheduleOnlineAcativityBinding.tvStartTime);


            }, hour, minute, false);

            timePickerDialog.show();
        });

        activityScheduleOnlineAcativityBinding.rlSetDuration.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(mActivity, (timePicker, hour, min) -> {

                Calendar datetime = Calendar.getInstance();
                Calendar c1 = Calendar.getInstance();
                datetime.set(Calendar.HOUR_OF_DAY, hour);
                datetime.set(Calendar.MINUTE, min);


                updateTimeDuration(hour, min, activityScheduleOnlineAcativityBinding.tvSetDuratiomn);


            }, hour, minute, false);


            timePickerDialog.show();
        });

        activityScheduleOnlineAcativityBinding.typeTextInputLayoutSelectCoach.setOnClickListener(v -> {
            TvDialogHeader.setText("Select Coach for your schedule!");
            dialogCoach.show();
        });

        activityScheduleOnlineAcativityBinding.typeTextInputLayoutSelectStudent.setOnClickListener(v -> {
            TvDialogHeader.setText("Select Students for your schedule!");
            dialogStudents.show();
        });

        activityScheduleOnlineAcativityBinding.flAddCoach.setOnClickListener(v -> {
            startActivity(new Intent(mContext,AcademyAddCoachActivity.class));
        });

        activityScheduleOnlineAcativityBinding.flAddStudents.setOnClickListener(v -> {
            startActivity(new Intent(mContext,AcademyAddStudentsActivity.class));
        });

        activityScheduleOnlineAcativityBinding.flSubmit.setOnClickListener(v -> {
            if (checkValidation()) {
                if (Global.isOnline(mActivity)) {
                    createSchedule();
                } else {
                    Global.showDialog(mActivity);
                }
            }
        });


    }

    private boolean checkValidation() {
        topic = activityScheduleOnlineAcativityBinding.editTextTopic.getText().toString().trim();

        if (!Global.validateLengthofCoachRegisterr(activityScheduleOnlineAcativityBinding.editTextTopic.getText().toString().trim())) {
            activityScheduleOnlineAcativityBinding.filledTextFieldTopic.setErrorEnabled(true);
            activityScheduleOnlineAcativityBinding.filledTextFieldTopic.setError(mContext.getResources().getString(R.string.Enter_topic));
            activityScheduleOnlineAcativityBinding.filledTextFieldTopic.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.app_light_red)));
            return false;
        } else if (date.isEmpty()) {
            Toaster.customToast(mActivity.getResources().getString(R.string.Enter_date));
            return false;
        } else if (aTime.isEmpty() || activityScheduleOnlineAcativityBinding.tvStartTime.getText().toString().equalsIgnoreCase("Select Time")) {
            Toaster.customToast(mActivity.getResources().getString(R.string.Enter_time));
            return false;
        } else if (durationTime.isEmpty()) {
            Toaster.customToast(mActivity.getResources().getString(R.string.select_hour_and_minute_to_schedule_meeting));
            return false;
        } else if (coachStringBuilder == null) {
            Toaster.customToast(mActivity.getResources().getString(R.string.selcet_coach));
            return false;
        } else if (studentStringBuilder == null) {
            Toaster.customToast(mActivity.getResources().getString(R.string.select_student));
            return false;
        }


        return true;
    }

    private void updateTime(int hours, int mins, TextView tv_time) {
        String timeSet;
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";
        String minutes;
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);
        aTime = String.valueOf(hours) + ':' + minutes + " " + timeSet;
        tv_time.setText(aTime);

    }

    private void updateTimeDuration(int hours, int mins, TextView tv_time) {
        String timeSet;
        if (hours > 12) {
            hours -= 12;
            // timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            //timeSet = "AM";
        } else if (hours == 12) {

        }
        // timeSet = "PM";
        else {

        }
        // timeSet = "AM";
        String minutes;
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);
        durationTime = hours + " Hours" + " : " + minutes + " Minutes";
        sendDurationTime = hours +":" + minutes;
        tv_time.setText(durationTime);

    }

    private void coachSelectionDialog(List<ManageCoachesModel> data, ArrayList<String> integerArrayList) {
        TextView btnOk = dialogCoach.findViewById(R.id.btnOk);
        RecyclerView rv_language = dialogCoach.findViewById(R.id.rv_language);
        rv_language.setHasFixedSize(true);
        rv_language.setLayoutManager(new LinearLayoutManager(mContext));

        ScheduleAcademyCoachesListAdapter scheduleAcademyCoachesListAdapter = new ScheduleAcademyCoachesListAdapter(mContext, data, (integerArrayList1,coachNameArraylist1) -> {
            coachIdsArrayList = integerArrayList1;
            coachNameArrayList = coachNameArraylist1;
        });
        rv_language.setAdapter(scheduleAcademyCoachesListAdapter);

        btnOk.setOnClickListener(view -> {

            coachStringBuilder = new StringBuilder();
            coachNameStringBuilder = new StringBuilder();
            String prefix = "";
            String prefixName="";

            for(int i =0;i<coachIdsArrayList.size();i++){

                coachStringBuilder.append(prefix);
                coachNameStringBuilder.append(prefixName);
                prefix = ",";
                prefixName =" , ";
                coachNameStringBuilder.append(coachNameArrayList.get(i));
                coachStringBuilder.append(coachIdsArrayList.get(i));

            }

            activityScheduleOnlineAcativityBinding.typeTextInputLayoutSelectCoach.setText(coachNameStringBuilder.toString());
            dialogCoach.dismiss();
        });

    }

    private void getAcademyCoachsList() {
        // loaderView.showLoader();

        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.ACADEMY_COACH_LIST, response -> {
            Timber.d(response);
            //  loaderView.hideLoader();

            try {
                JSONObject jsonObject = new JSONObject(response);

                if (jsonObject.optString("status").equalsIgnoreCase("200")) {

                    jsonArrayCoach = jsonObject.getJSONArray("data");


                    if(jsonArrayCoach.length()>0 && jsonArrayStudents.length()>0){
                        activityScheduleOnlineAcativityBinding.typeTextInputLayoutSelectCoach.setVisibility(View.VISIBLE);
                        activityScheduleOnlineAcativityBinding.typeTextInputLayoutSelectStudent.setVisibility(View.VISIBLE);
                        activityScheduleOnlineAcativityBinding.flSubmit.setVisibility(View.VISIBLE);
                        activityScheduleOnlineAcativityBinding.flAddCoach.setVisibility(View.GONE);
                        activityScheduleOnlineAcativityBinding.flAddStudents.setVisibility(View.GONE);
                        activityScheduleOnlineAcativityBinding.tvMessage.setVisibility(View.GONE);
                        activityScheduleOnlineAcativityBinding.tvSelectCoachStudentText.setVisibility(View.VISIBLE);
                    }else{
                        if (jsonArrayCoach.length() == 0) {
                            activityScheduleOnlineAcativityBinding.typeTextInputLayoutSelectStudent.setVisibility(View.GONE);
                            activityScheduleOnlineAcativityBinding.flAddStudents.setVisibility(View.VISIBLE);
                            activityScheduleOnlineAcativityBinding.tvMessage.setVisibility(View.VISIBLE);
                            activityScheduleOnlineAcativityBinding.flSubmit.setVisibility(View.GONE);
                            activityScheduleOnlineAcativityBinding.tvSelectCoachStudentText.setVisibility(View.GONE);
                        } else {
                            activityScheduleOnlineAcativityBinding.typeTextInputLayoutSelectStudent.setVisibility(View.VISIBLE);
                            activityScheduleOnlineAcativityBinding.flAddStudents.setVisibility(View.GONE);
                            activityScheduleOnlineAcativityBinding.tvMessage.setVisibility(View.GONE);
                            activityScheduleOnlineAcativityBinding.tvSelectCoachStudentText.setVisibility(View.VISIBLE);

                        }
                        if (jsonArrayStudents.length() == 0) {
                            activityScheduleOnlineAcativityBinding.typeTextInputLayoutSelectCoach.setVisibility(View.GONE);
                            activityScheduleOnlineAcativityBinding.flAddCoach.setVisibility(View.VISIBLE);
                            activityScheduleOnlineAcativityBinding.tvMessage.setVisibility(View.VISIBLE);
                            activityScheduleOnlineAcativityBinding.flSubmit.setVisibility(View.GONE);
                            activityScheduleOnlineAcativityBinding.tvSelectCoachStudentText.setVisibility(View.GONE);
                        } else {
                            activityScheduleOnlineAcativityBinding.typeTextInputLayoutSelectCoach.setVisibility(View.VISIBLE);
                            activityScheduleOnlineAcativityBinding.flAddCoach.setVisibility(View.GONE);
                            activityScheduleOnlineAcativityBinding.tvMessage.setVisibility(View.GONE);
                            activityScheduleOnlineAcativityBinding.flSubmit.setVisibility(View.GONE);
                            activityScheduleOnlineAcativityBinding.tvSelectCoachStudentText.setVisibility(View.VISIBLE);
                        }
                    }

                    ArrayList<ManageCoachesModel> studentList = new ArrayList<>();

                    for (int i = 0; i < jsonArrayCoach.length(); i++) {
                        ManageCoachesModel academyStudenModel = new ManageCoachesModel(jsonArrayCoach.getJSONObject(i));
                        studentList.add(academyStudenModel);
                    }

                    coachSelectionDialog(studentList, new ArrayList<>());


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
            //loaderView.hideLoader();
            //Global.dismissDialog(progressDialog);
            Global.msgDialog((Activity) mActivity, getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("academy_id", SessionManager.get_academyId(prefs));
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void studentsSelectionDialog(List<AcademyStudenModel> data, ArrayList<String> integerArrayList) {
        TextView btnOk = dialogStudents.findViewById(R.id.btnOk);
        RecyclerView rv_language = dialogStudents.findViewById(R.id.rv_language);
        rv_language.setHasFixedSize(true);
        rv_language.setLayoutManager(new LinearLayoutManager(mContext));
        //Log.d("ReSize",coachLanguages.size()+"");
        ScheduleAcademyStudentListAdapter scheduleAcademyStudentListAdapter = new ScheduleAcademyStudentListAdapter(mActivity, data, (integerArrayList1,studentsNameArrayList1) -> {
            studentsArrayList = integerArrayList1;
            studentsNameArrayList = studentsNameArrayList1;
        });
        rv_language.setAdapter(scheduleAcademyStudentListAdapter);

        btnOk.setOnClickListener(view -> {
            studentStringBuilder = new StringBuilder();
            studentsNameStringBuilder = new StringBuilder();
            String prefix = "";
            String studentNamePrefix="";

            for(int i =0;i<studentsArrayList.size();i++){
                studentStringBuilder.append(prefix);
                studentsNameStringBuilder.append(studentNamePrefix);
                prefix=",";
                studentNamePrefix=" , ";
                studentStringBuilder.append(studentsArrayList.get(i));
                studentsNameStringBuilder.append(studentsNameArrayList.get(i));

            }

           // Log.d("size", studentStringBuilder.toString());

            activityScheduleOnlineAcativityBinding.typeTextInputLayoutSelectStudent.setText(studentsNameStringBuilder.toString());
            dialogStudents.dismiss();
        });

        //dialog.show();

    }

    private void getAcademyStudentsList() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.ACADEMY_STUDENT_LIST, response -> {
            Timber.d(response);
            loaderView.hideLoader();
            //Global.dismissDialog(progressDialog);

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("status").equalsIgnoreCase("200")) {

                    jsonArrayStudents = jsonObject.getJSONArray("data");

                    if(jsonArrayCoach.length()>0 && jsonArrayStudents.length()>0){
                        activityScheduleOnlineAcativityBinding.typeTextInputLayoutSelectCoach.setVisibility(View.VISIBLE);
                        activityScheduleOnlineAcativityBinding.typeTextInputLayoutSelectStudent.setVisibility(View.VISIBLE);
                        activityScheduleOnlineAcativityBinding.flSubmit.setVisibility(View.VISIBLE);
                        activityScheduleOnlineAcativityBinding.flAddCoach.setVisibility(View.GONE);
                        activityScheduleOnlineAcativityBinding.flAddStudents.setVisibility(View.GONE);
                        activityScheduleOnlineAcativityBinding.tvMessage.setVisibility(View.GONE);
                        activityScheduleOnlineAcativityBinding.tvSelectCoachStudentText.setVisibility(View.VISIBLE);
                    }else{
                        if (jsonArrayStudents.length() == 0) {
                            activityScheduleOnlineAcativityBinding.typeTextInputLayoutSelectStudent.setVisibility(View.GONE);
                            activityScheduleOnlineAcativityBinding.flAddStudents.setVisibility(View.VISIBLE);
                            activityScheduleOnlineAcativityBinding.tvMessage.setVisibility(View.VISIBLE);
                            activityScheduleOnlineAcativityBinding.flSubmit.setVisibility(View.GONE);
                            activityScheduleOnlineAcativityBinding.tvSelectCoachStudentText.setVisibility(View.GONE);
                        } else {
                            activityScheduleOnlineAcativityBinding.typeTextInputLayoutSelectStudent.setVisibility(View.VISIBLE);
                            activityScheduleOnlineAcativityBinding.flAddStudents.setVisibility(View.GONE);
                            activityScheduleOnlineAcativityBinding.tvMessage.setVisibility(View.GONE);
                            activityScheduleOnlineAcativityBinding.tvSelectCoachStudentText.setVisibility(View.VISIBLE);

                        }
                        if (jsonArrayCoach.length() == 0) {
                            activityScheduleOnlineAcativityBinding.typeTextInputLayoutSelectCoach.setVisibility(View.GONE);
                            activityScheduleOnlineAcativityBinding.flAddCoach.setVisibility(View.VISIBLE);
                            activityScheduleOnlineAcativityBinding.tvMessage.setVisibility(View.VISIBLE);
                            activityScheduleOnlineAcativityBinding.flSubmit.setVisibility(View.GONE);
                            activityScheduleOnlineAcativityBinding.tvSelectCoachStudentText.setVisibility(View.GONE);
                        } else {
                            activityScheduleOnlineAcativityBinding.typeTextInputLayoutSelectCoach.setVisibility(View.VISIBLE);
                            activityScheduleOnlineAcativityBinding.flAddCoach.setVisibility(View.GONE);
                            activityScheduleOnlineAcativityBinding.tvMessage.setVisibility(View.GONE);
                            activityScheduleOnlineAcativityBinding.flSubmit.setVisibility(View.GONE);
                            activityScheduleOnlineAcativityBinding.tvSelectCoachStudentText.setVisibility(View.VISIBLE);
                        }
                    }

                    ArrayList<AcademyStudenModel> studentList = new ArrayList<>();

                    for (int i = 0; i < jsonArrayStudents.length(); i++) {
                        AcademyStudenModel academyStudenModel = new AcademyStudenModel(jsonArrayStudents.getJSONObject(i));
                        studentList.add(academyStudenModel);
                    }

                    studentsSelectionDialog(studentList, new ArrayList<>());

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
            //Global.dismissDialog(progressDialog);
            Global.msgDialog((Activity) mActivity, getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("academy_id", SessionManager.get_academyId(prefs));
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void createSchedule() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.ACADEMY_CRETATE_SCHEDULE_MEETING, response -> {
            Timber.d(response);

            try {
                loaderView.hideLoader();
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("api_status").equalsIgnoreCase("200")) {
                    Toaster.customToast(jsonObject.getString("message"));

                    activityScheduleOnlineAcativityBinding.editTextTopic.setText("");
                    activityScheduleOnlineAcativityBinding.tvStartDate.setText("");
                    activityScheduleOnlineAcativityBinding.tvStartTime.setText("");
                    activityScheduleOnlineAcativityBinding.tvSetDuratiomn.setText("");
                    coachStringBuilder=null;
                    studentStringBuilder=null;
                    coachNameStringBuilder =null;
                    studentsNameStringBuilder = null;
                    new Handler().postDelayed(() -> startActivity(new Intent(mActivity, AcademyUpcomingSessionActivity.class)), 1000);

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
                param.put("title", topic);
                param.put("schedule_date", date);
                param.put("schedule_time", aTime);
                param.put("duration_hrs", sendDurationTime);
                param.put("group_coach_ids", coachStringBuilder.toString());
                param.put("group_student_ids", studentStringBuilder.toString());
                Timber.e(param.toString());
                return param;

            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}