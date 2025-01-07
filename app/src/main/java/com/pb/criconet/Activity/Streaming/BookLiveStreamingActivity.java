package com.pb.criconet.Activity.Streaming;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.applandeo.materialcalendarview.CalendarView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.pb.criconet.Activity.MainActivity;
import com.pb.criconet.R;
import com.pb.criconet.databinding.ActivityBookLiveStreamingBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.model.StreamingModel.City;
import com.pb.criconet.model.StreamingModel.States;
import com.pb.criconet.util.BookStreamDropDown;
import com.pb.criconet.util.BookStreamDropDownDate;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.DataModel;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.SessionManager;
import com.pb.criconet.util.Toaster;


import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import timber.log.Timber;

public class BookLiveStreamingActivity extends AppCompatActivity {

    ActivityBookLiveStreamingBinding activityBookLiveStreamingBinding;
    Context mContext;
    Activity mActivity;

    private ArrayList<DataModel> option_select_type = new ArrayList<>();
    private ArrayList<DataModel> option_state = new ArrayList<>();
    private ArrayList<DataModel> option_city = new ArrayList<>();
    private String type = "";
    private String state = "";
    private String city = "";

    private City citymodelArrayList;
    private States statemodelArrayList;

    private SharedPreferences prefs;
    private RequestQueue queue;
    CustomLoaderView loaderView;
    String cityID = "", stateID = "";
    List<Calendar> selectedDatesOld = new ArrayList<>();
    StringBuilder langStringBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBookLiveStreamingBinding = ActivityBookLiveStreamingBinding.inflate(getLayoutInflater());
        setContentView(activityBookLiveStreamingBinding.getRoot());
        mContext = this;
        mActivity = this;

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityBookLiveStreamingBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.e_streaming));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        queue = Volley.newRequestQueue(mContext);
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        loaderView = CustomLoaderView.initialize(this);
        inItView();

        if (Global.isOnline(mContext)) {
            getState();
        } else {
            Global.showDialog(mActivity);
        }
    }

    private void inItView() {

        option_select_type.add(new DataModel("Club"));
        option_select_type.add(new DataModel("Tournament"));
        option_select_type.add(new DataModel("Organization"));
        activityBookLiveStreamingBinding.dropSelectType.setOptionList(option_select_type);
        activityBookLiveStreamingBinding.dropSelectType.setClickListener(new BookStreamDropDownDate.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name) {
                type = name;
                activityBookLiveStreamingBinding.tvTypeError.setVisibility(View.GONE);

            }


            @Override
            public void onDismiss() {
            }
        });


        activityBookLiveStreamingBinding.editTextUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityBookLiveStreamingBinding.filledTextFieldUsername.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityBookLiveStreamingBinding.filledTextFieldUsername.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityBookLiveStreamingBinding.filledTextFieldUsername.setErrorEnabled(false);
            }
        });
        activityBookLiveStreamingBinding.phoneInputLayoutEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityBookLiveStreamingBinding.phoneInputLayoutEmail.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityBookLiveStreamingBinding.phoneInputLayoutEmail.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityBookLiveStreamingBinding.phoneInputLayoutEmail.setErrorEnabled(false);
            }
        });
        activityBookLiveStreamingBinding.emailInputLayoutEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityBookLiveStreamingBinding.textInputLayoutEmail.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityBookLiveStreamingBinding.textInputLayoutEmail.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityBookLiveStreamingBinding.textInputLayoutEmail.setErrorEnabled(false);
            }
        });
        activityBookLiveStreamingBinding.groundLocationTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityBookLiveStreamingBinding.groundLocationTextInputLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityBookLiveStreamingBinding.groundLocationTextInputLayout.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityBookLiveStreamingBinding.groundLocationTextInputLayout.setErrorEnabled(false);
            }
        });
        activityBookLiveStreamingBinding.messageTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityBookLiveStreamingBinding.messageTextInputLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityBookLiveStreamingBinding.messageTextInputLayout.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityBookLiveStreamingBinding.messageTextInputLayout.setErrorEnabled(false);
            }
        });

        activityBookLiveStreamingBinding.tvSelectdate.setOnClickListener(v -> {
            dateDialog(selectedDatesOld);
        });

        activityBookLiveStreamingBinding.flSubmit.setOnClickListener(v -> {
            if (Objects.requireNonNull(activityBookLiveStreamingBinding.editTextUsername.getText()).toString().isEmpty()) {
                activityBookLiveStreamingBinding.filledTextFieldUsername.setErrorEnabled(true);
                activityBookLiveStreamingBinding.filledTextFieldUsername.setError("Name can't be empty!");
                activityBookLiveStreamingBinding.filledTextFieldUsername.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            } else if (type.isEmpty()) {
                activityBookLiveStreamingBinding.tvTypeError.setVisibility(View.VISIBLE);
            } else if (Objects.requireNonNull(activityBookLiveStreamingBinding.phoneInputLayoutEditText.getText()).toString().isEmpty()) {
                activityBookLiveStreamingBinding.phoneInputLayoutEmail.setErrorEnabled(true);
                activityBookLiveStreamingBinding.phoneInputLayoutEmail.setError("Phone number can't be empty!");
                activityBookLiveStreamingBinding.phoneInputLayoutEmail.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            } else if (Objects.requireNonNull(!Global.isValidEmailAddress(activityBookLiveStreamingBinding.emailInputLayoutEmail.getText().toString()))) {
                activityBookLiveStreamingBinding.textInputLayoutEmail.setErrorEnabled(true);
                activityBookLiveStreamingBinding.textInputLayoutEmail.setError("Enter valid email!");
                activityBookLiveStreamingBinding.textInputLayoutEmail.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            } else if (stateID.isEmpty()) {
                activityBookLiveStreamingBinding.tvStateError.setVisibility(View.VISIBLE);
            } else if (cityID.isEmpty()) {
                activityBookLiveStreamingBinding.tvCityError.setVisibility(View.VISIBLE);
            } else if (Objects.requireNonNull(activityBookLiveStreamingBinding.groundLocationTextInputEditText.getText()).toString().isEmpty()) {
                activityBookLiveStreamingBinding.groundLocationTextInputLayout.setErrorEnabled(true);
                activityBookLiveStreamingBinding.groundLocationTextInputLayout.setError("Ground location can't be empty!");
                activityBookLiveStreamingBinding.groundLocationTextInputLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            } else if(activityBookLiveStreamingBinding.tvSelectdate.getText().toString().isEmpty()){
                Toaster.customToast("Select Streaming Date");
            }
            else if (Objects.requireNonNull(activityBookLiveStreamingBinding.messageTextInputEditText.getText()).toString().isEmpty()) {
                activityBookLiveStreamingBinding.messageTextInputLayout.setErrorEnabled(true);
                activityBookLiveStreamingBinding.messageTextInputLayout.setError("Message can't be empty!");
                activityBookLiveStreamingBinding.messageTextInputLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            }else{
                if (Global.isOnline(mContext)) {
                    sendBookRequestLiveStreaming();
                } else {
                    Global.showDialog(mActivity);
                }
            }
        });
    }


    private void getState() {
        StringRequest postRequest = new StringRequest(Request.Method.GET, Global.URL + "get_states" + "&country_id=" + "101", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Timber.d(response);
                try {
                    if (!response.isEmpty()) {
                        Gson gson = new Gson();
                        statemodelArrayList = gson.fromJson(response, States.class);
                        if (statemodelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                            ArrayList<String> state = new ArrayList<>();
                            state.add("Select State/UT");

                            for (States.Datum data : statemodelArrayList.getData()) {
                                state.add(data.getName());

                                option_state.add(new DataModel(data.getName(), data.getId()));
                            }


                            activityBookLiveStreamingBinding.dropSelectStat.setOptionList(option_state);
                            activityBookLiveStreamingBinding.dropSelectStat.setClickListener(new BookStreamDropDown.onClickInterface() {
                                @Override
                                public void onClickAction() {
                                }

                                @Override
                                public void onClickDone(String id) {
                                    stateID = id;

                                    if (Global.isOnline(mContext)) {
                                        getCity(id);
                                    } else {
                                        Global.showDialog(mActivity);
                                    }
                                    activityBookLiveStreamingBinding.tvStateError.setVisibility(View.GONE);
                                }


                                @Override
                                public void onDismiss() {
                                }
                            });


                        }
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void getCity(String stateId) {
        StringRequest postRequest = new StringRequest(Request.Method.GET, Global.URL + "get_cities" + "&state_id=" + stateId, response -> {
            try {
                Log.d("ResponseCity", response);
                Gson gson = new Gson();
                citymodelArrayList = gson.fromJson(response, City.class);
                if (citymodelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                    ArrayList<String> city = new ArrayList<>();
                    city.add("Select City");
                    for (City.Datum data : citymodelArrayList.getData()) {
                        city.add(data.getName());
                        option_city.add(new DataModel(data.getName(), data.getId()));
                    }

                    activityBookLiveStreamingBinding.dropSelectCity.setOptionList(option_city);
                    activityBookLiveStreamingBinding.dropSelectCity.setClickListener(new BookStreamDropDown.onClickInterface() {
                        @Override
                        public void onClickAction() {
                        }

                        @Override
                        public void onClickDone(String id) {
                            cityID = id;
                            activityBookLiveStreamingBinding.tvCityError.setVisibility(View.GONE);
                        }


                        @Override
                        public void onDismiss() {
                        }
                    });

                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    public void sendBookRequestLiveStreaming() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.SEND_LIVE_STREAMING_BOOKING_REQUEST,
                new Response.Listener<String>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(String response) {
                        Timber.d(response);
                        loaderView.hideLoader();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                                msgDialogSuccess(mActivity, jsonObject.optString("message"));
                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(mActivity, jsonObject.optString("errors"));
                            } else {
                                Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        loaderView.hideLoader();
                        Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
//                Global.msgDialog(EditProfile.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> param = new HashMap<String, String>();
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("name", activityBookLiveStreamingBinding.editTextUsername.getText().toString().trim());
                param.put("contact_no", activityBookLiveStreamingBinding.phoneInputLayoutEditText.getText().toString().trim());
                param.put("email", activityBookLiveStreamingBinding.emailInputLayoutEmail.getText().toString().trim());
                param.put("booking_date", activityBookLiveStreamingBinding.tvSelectdate.getText().toString().trim());
                param.put("state_id", stateID);
                param.put("city_id", cityID);
                param.put("message", activityBookLiveStreamingBinding.messageTextInputEditText.getText().toString().trim());
                param.put("location", activityBookLiveStreamingBinding.groundLocationTextInputEditText.getText().toString().trim());
                param.put("event_type", type);

                System.out.println("data   :" + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    public void msgDialogSuccess(Activity ac, String msg) {
        try {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(ac);
            alertbox.setTitle(ac.getResources().getString(R.string.app_name));
            alertbox.setMessage(Html.fromHtml(msg));
            alertbox.setPositiveButton(ac.getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intent = new Intent(mContext, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            alertbox.show();
        } catch (Exception e) {
        }
    }

    private void setupCalendarView(CalendarView calendarView) {
        // Get current date
        Calendar currentDate = Calendar.getInstance();

        // Set the first day of the month
        currentDate.set(Calendar.DAY_OF_MONTH, 1);
        long startOfMonth = currentDate.getTimeInMillis();  // Not used directly now

        // Set the last day of the month
        Calendar endOfMonthCalendar = Calendar.getInstance();
        endOfMonthCalendar.set(Calendar.MONTH, currentDate.get(Calendar.MONTH));
        endOfMonthCalendar.set(Calendar.YEAR, currentDate.get(Calendar.YEAR));
        endOfMonthCalendar.set(Calendar.DAY_OF_MONTH, endOfMonthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        // Set minimum and maximum dates using Calendar objects
        calendarView.setMinimumDate(currentDate); // Set to first day of the current month
        calendarView.setMaximumDate(endOfMonthCalendar); // Set to last day of the current month
    }

    private void dateDialog(List<Calendar> selectedDatesOld) {
        Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_select_multiple_date);
        dialog.setCancelable(false);

        FrameLayout fl_cancell = dialog.findViewById(R.id.fl_cancell);
        fl_cancell.setOnClickListener(v -> {
            activityBookLiveStreamingBinding.tvSelectdate.setText("");
            dialog.dismiss();
        });

        FrameLayout fl_ok = dialog.findViewById(R.id.fl_ok);
        CalendarView calendarView = dialog.findViewById(R.id.calendarView);

        Calendar min = Calendar.getInstance();
        min.set(Calendar.DAY_OF_MONTH, 1);
        min.set(Calendar.HOUR_OF_DAY, 0);
        min.set(Calendar.MINUTE, 0);
        min.set(Calendar.SECOND, 0);
        min.set(Calendar.MILLISECOND, 0);
        // Set the minimum date to today
        calendarView.setMinimumDate(min);

        fl_ok.setOnClickListener(v -> {
            List<Calendar> selectedDates = calendarView.getSelectedDates();
            if (selectedDates.size() == 0) {
                Toaster.customToast("Select date");
            } else {
                selectedDatesOld.clear();
                if (selectedDates.size() > 5) {
                    Toaster.customToast("Select max five different dates");
                } else {
//                    for (Calendar date : selectedDates) {
//                        selectedDatesOld.add(date);
//                    }
                    dialog.dismiss();
                    StringBuilder langStringBuilder = new StringBuilder();
                    String prefix = "";
                    for (Calendar date : selectedDates) {
                        langStringBuilder.append(prefix);
                        prefix = ", ";
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        String formattedDate = sdf.format(date.getTime());
                       Log.d("date",formattedDate);
                        langStringBuilder.append(formattedDate);
                    }
                    activityBookLiveStreamingBinding.tvSelectdate.setText(langStringBuilder.toString());
                }
            }
        });

        calendarView.setSelectedDates(selectedDatesOld);
        dialog.show();
    }



}