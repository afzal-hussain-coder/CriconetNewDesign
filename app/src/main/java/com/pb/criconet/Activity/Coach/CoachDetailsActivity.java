package com.pb.criconet.Activity.Coach;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;
import com.mukesh.OtpView;
import com.pb.criconet.Activity.User.UserBookingDetails;
import com.pb.criconet.R;
import com.pb.criconet.adapter.EcoachingAdapter.EcoachingListAdapter;
import com.pb.criconet.adapter.EcoachingAdapter.SessionTimeListAdapter;
import com.pb.criconet.databinding.ActivityCoachDetailsBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.model.Coaching.BookingPaymentsDetails;
import com.pb.criconet.model.Coaching.CoachDetails;
import com.pb.criconet.model.Coaching.CoachList;
import com.pb.criconet.model.Coaching.DateSlotes;
import com.pb.criconet.model.Coaching.OrderCreate;
import com.pb.criconet.model.Coaching.TimeSlot;
import com.pb.criconet.model.Coaching.updatedTimeSlot;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.SessionManager;
import com.pb.criconet.util.Toaster;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CoachDetailsActivity extends AppCompatActivity {

    ActivityCoachDetailsBinding activityCoachDetailsBinding;
    Context mContext;
    Activity mActivity;
    Long date;
    Animation slide_down, slide_up;
    private SharedPreferences prefs;
    private RequestQueue queue;
    CustomLoaderView loaderView;
    String CoachId = "";
    private String year = "";
    private String month = "";
    private String dateGott = "";
    DateSlotes modelArrayListdate;
    private Calendar[] days = new Calendar[0];
    List<EventDay> events = new ArrayList<>();
    Date previousDate;
    private String mslotId = "";
    OrderCreate ordercreate;
    private CoachDetails modelArrayList;
    private LinearLayout layout_enter_mobile_no, layout_otp;
    private OtpView otpView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCoachDetailsBinding = ActivityCoachDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityCoachDetailsBinding.getRoot());
        mContext = this;
        mActivity = this;

        if (getIntent() != null) {
            CoachId = getIntent().getStringExtra("CoachId");
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        queue = Volley.newRequestQueue(mContext);
        loaderView = CustomLoaderView.initialize(mContext);

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityCoachDetailsBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.coach_details));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());


        initView();

        if (Global.isOnline(mContext)) {
            getCoachDetails();
            //getCoachDete();
        } else {
            Global.showDialog((Activity) mContext);
        }


    }

    private void initView() {


        activityCoachDetailsBinding.ratingBar.setRating(5f);
        activityCoachDetailsBinding.tvCheckavailability.setPaintFlags(activityCoachDetailsBinding.tvCheckavailability.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        activityCoachDetailsBinding.rvSessionTime.setHasFixedSize(true);
        activityCoachDetailsBinding.rvSessionTime.setLayoutManager(new GridLayoutManager(mContext, 2));


        activityCoachDetailsBinding.flBook.setOnClickListener(v -> {


            slide_down = AnimationUtils.loadAnimation(mContext,
                    R.anim.slide_down);
            activityCoachDetailsBinding.flBook.setVisibility(View.GONE);
            activityCoachDetailsBinding.liBookLayout.setAnimation(slide_down);
            activityCoachDetailsBinding.liBookLayout.setVisibility(View.VISIBLE);


        });

        activityCoachDetailsBinding.tvCheckavailability.setOnClickListener(v -> {
            activityCoachDetailsBinding.liBookLayout.setVisibility(View.VISIBLE);
            slide_down = AnimationUtils.loadAnimation(mContext,
                    R.anim.slide_down);
            activityCoachDetailsBinding.flBook.setVisibility(View.GONE);
            activityCoachDetailsBinding.liBookLayout.setAnimation(slide_down);

        });


        year = Global.getYear(activityCoachDetailsBinding.calendarView.getCurrentPageDate().getTime().toString());
        month = Global.getMonth(activityCoachDetailsBinding.calendarView.getCurrentPageDate().getTime().toString());

        //disable dates before today
        Calendar min = Calendar.getInstance();
        previousDate = min.getTime();
        min.add(Calendar.DAY_OF_MONTH, -1);
        activityCoachDetailsBinding.calendarView.setMinimumDate(min);


        activityCoachDetailsBinding.calendarView.setOnDayClickListener(eventDay -> {
            activityCoachDetailsBinding.liSessionLayout.setVisibility(View.VISIBLE);
            mslotId = "";
            dateGott = Global.getDateGot(eventDay.getCalendar().getTime().toString());

            if (previousDate.compareTo(eventDay.getCalendar().getTime()) == -1) {
                if (Global.isOnline(mActivity)) {
                    getDateSlote(dateGott, CoachId);
                } else {
                    Global.showDialog(mActivity);
                }
            } else if (Global.getDateGot(previousDate.toString()).equalsIgnoreCase(Global.getDateGot(eventDay.getCalendar().getTime().toString()))) {
                if (Global.isOnline(mActivity)) {
                    getDateSlote(dateGott, CoachId);
                } else {
                    Global.showDialog(mActivity);
                }
            } else {
                //dateGot="";
            }

        });

        activityCoachDetailsBinding.calendarView.setOnForwardPageChangeListener(() -> {
            year = Global.getYear(activityCoachDetailsBinding.calendarView.getCurrentPageDate().getTime().toString());
            month = Global.getMonth(activityCoachDetailsBinding.calendarView.getCurrentPageDate().getTime().toString());

            if (year != null && month != null) {
                getCoachDete();
            }


        });

        activityCoachDetailsBinding.calendarView.setOnPreviousPageChangeListener(() -> {
            year = Global.getYear(activityCoachDetailsBinding.calendarView.getCurrentPageDate().getTime().toString());
            month = Global.getMonth(activityCoachDetailsBinding.calendarView.getCurrentPageDate().getTime().toString());

            if (year != null && month != null) {
                getCoachDete();
            }
        });

        activityCoachDetailsBinding.ivClose.setOnClickListener(v -> {
            slide_up = AnimationUtils.loadAnimation(mContext,
                    R.anim.slide_up);
            activityCoachDetailsBinding.liBookLayout.setAnimation(slide_up);
            activityCoachDetailsBinding.flBook.setVisibility(View.VISIBLE);


            activityCoachDetailsBinding.liBookLayout.setVisibility(View.INVISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    activityCoachDetailsBinding.liBookLayout.setVisibility(View.GONE);
                    activityCoachDetailsBinding.liSessionLayout.setVisibility(View.GONE);
                }
            }, 600);


        });

        activityCoachDetailsBinding.flFinalBook.setOnClickListener(v -> {

            if (dateGott.equalsIgnoreCase("")) {
                Toaster.customToast(getResources().getString(R.string.Select_Slot_Available_Date));
            } else if (mslotId.equalsIgnoreCase("")) {
                Toaster.customToast(getResources().getString(R.string.Select_Time_Slot));
            } else {

                String is_contact_verify = SessionManager.get_mobile_verified(prefs);
                //Toaster.customToast(SessionManager.get_mobile_verified(prefs));
                //is_contact_verify="0";

                if (is_contact_verify.equalsIgnoreCase("0")) {
                    EmailOtpDialog(modelArrayList, ordercreate);
                } else {
                    if (Global.isOnline(mActivity)) {
                        BookCoach();
                    } else {
                        Global.showDialog(mActivity);
                    }
                }
            }

        });

        activityCoachDetailsBinding.flAddToCart.setOnClickListener(v -> {

        });


    }

    private void getCoachDetails() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_coach_details", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ResponseDetails", response);
                loaderView.hideLoader();

                try {
                    Gson gson = new Gson();
                    modelArrayList = gson.fromJson(response, CoachDetails.class);
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("api_status").equalsIgnoreCase("200")) {

                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");

                        // all details and set to the view

                        Glide.with(mContext).load(jsonObjectData.getString("avatar")).placeholder(mContext.getResources().getDrawable(R.drawable.placeholder_user)).into(activityCoachDetailsBinding.ivRoundedProfile);
                        activityCoachDetailsBinding.tvCoachName.setText(jsonObjectData.getString("name"));
                        activityCoachDetailsBinding.tvCoachExp.setText(jsonObjectData.getString("exps"));
                        activityCoachDetailsBinding.tvCoachPrice.setText("Price: " + jsonObjectData.getString("charge_amount") + " INR/Session");


                        if (jsonObjectData.getInt("rating") == 0) {
                            activityCoachDetailsBinding.ratingBar.setVisibility(View.GONE);
                        } else {
                            activityCoachDetailsBinding.ratingBar.setVisibility(View.VISIBLE);
                            activityCoachDetailsBinding.ratingBar.setRating((float) jsonObjectData.getInt("rating"));
                        }

                        JSONArray jsonArray = jsonObjectData.getJSONArray("Specialization_category");

                        ArrayList<String> specializationArray = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            specializationArray.add(jsonArray.getJSONObject(i).getString("title"));
                        }


                        for (String chipText : specializationArray) {
                            Chip chip = new Chip(mContext);
                            chip.setText(chipText);
                            chip.setChipBackgroundColorResource(R.color.white);
                            chip.setChipStrokeColorResource(R.color.purple_700);
                            chip.setChipStrokeWidth(2.0f);
                            chip.setTextAppearance(R.style.MyChipTextAppearance);
                            activityCoachDetailsBinding.chipSpecializationGroup.addView(chip);
                        }

                        if (jsonObjectData.getString("about_coach_profile").isEmpty()) {
                            activityCoachDetailsBinding.liBio.setVisibility(View.GONE);
                        } else {
                            activityCoachDetailsBinding.liBio.setVisibility(View.VISIBLE);
                            activityCoachDetailsBinding.tvBioDetails.setText(jsonObjectData.getString("about_coach_profile"));
                        }

                        activityCoachDetailsBinding.tvLanguage.setText(jsonObjectData.getString("lang"));

                        if (jsonObjectData.getString("achievement").isEmpty()) {
                            activityCoachDetailsBinding.liAchievement.setVisibility(View.GONE);
                        } else {
                            activityCoachDetailsBinding.liAchievement.setVisibility(View.VISIBLE);
                            activityCoachDetailsBinding.tvTvAchievementDetails.setText(jsonObjectData.getString("achievement"));
                        }

                        if (jsonObjectData.getString("skills_you_learn").isEmpty()) {
                            activityCoachDetailsBinding.liWhatYouLean.setVisibility(View.GONE);
                        } else {
                            activityCoachDetailsBinding.liWhatYouLean.setVisibility(View.VISIBLE);
                            activityCoachDetailsBinding.tvWhatYourLearnDetails.setText(jsonObjectData.getString("skills_you_learn"));
                        }

                        if (jsonObjectData.getString("what_you_teach").isEmpty()) {
                            activityCoachDetailsBinding.liCourseOutline.setVisibility(View.GONE);
                        } else {
                            activityCoachDetailsBinding.liCourseOutline.setVisibility(View.VISIBLE);
                            activityCoachDetailsBinding.tvCourseOutlineDetails.setText(jsonObjectData.getString("what_you_teach"));
                        }


                        if (jsonObjectData.getJSONArray("certificate").length() == 0) {
                            activityCoachDetailsBinding.liCertificate.setVisibility(View.GONE);
                        } else {
                            activityCoachDetailsBinding.liCertificate.setVisibility(View.VISIBLE);
                            Glide.with(mContext).load(jsonObjectData.getJSONArray("certificate").getJSONObject(0).getString("certificate_url"))
                                    .into(activityCoachDetailsBinding.roundedIvCertificate);
                            activityCoachDetailsBinding.roundedIvCertificate.setOnClickListener(v -> {
                                try {
                                    imageViewDialog(jsonObjectData.getJSONArray("certificate").getJSONObject(0).getString("certificate_url"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            });
                        }

                        activityCoachDetailsBinding.ivZoom.setOnClickListener(v -> {
                            try {
                                imageViewDialog(jsonObjectData.getJSONArray("certificate").getJSONObject(0).getString("certificate_url"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });


                        if(jsonObjectData.getJSONObject("price").getString("offer_id").equalsIgnoreCase("0")){
                            activityCoachDetailsBinding.tvOfferl.setVisibility(View.GONE);
                        }else{
                            activityCoachDetailsBinding.tvOfferP.setVisibility(View.VISIBLE);
                            activityCoachDetailsBinding.tvCoachPrice.setPaintFlags(activityCoachDetailsBinding.tvCoachPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            activityCoachDetailsBinding.tvOfferP.setText(mContext.getString(R.string.offer_price)+" "+"\u20B9" +jsonObjectData.getJSONObject("price").getString("payment_price") +" "+"/"+mContext.getString(R.string.session));
                        }

                    } else {
                        Toaster.customToast(getString(R.string.socket_timeout));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, error -> {
            error.printStackTrace();
            loaderView.hideLoader();
            Global.msgDialog((Activity) mContext, getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("coach_id", CoachId);
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("user_id", SessionManager.get_user_id(prefs));
                Log.d("Param", param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void getDateSlote(String dateGot, String coachId) {
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_coach_date_slot", response -> {
            Log.d("Time respose", response);
            try {
                Gson gson = new Gson();
                TimeSlot modelArrayList = gson.fromJson(response, TimeSlot.class);
                if (modelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                    activityCoachDetailsBinding.rvSessionTime.setAdapter(new SessionTimeListAdapter(mContext, modelArrayList.getData(), new SessionTimeListAdapter.getSlotIdInterface() {
                        @Override
                        public void getSlotId(String slotId) {
                            mslotId = slotId;
                            // hare all slot id is passed on api for booking the coach

                        }
                    }));
                } else {
                    dateGott = "";
                    Toaster.customToast(modelArrayList.getErrors().getErrorText());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("coach_id", coachId);
                param.put("date", dateGot);
                Log.d("param", param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void getCoachDete() {
        //progressDialog = Global.getProgressDialog(mActivity, CCResource.getString(mActivity, R.string.loading_dot), false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_coach_available_date", response -> {
            Log.d("dateAvailable", response);
            // Global.dismissDialog(progressDialog);
            Gson gson = new Gson();

            if (!response.contains("false")) {
                modelArrayListdate = gson.fromJson(response, DateSlotes.class);

                if (modelArrayListdate.getApiStatus().equalsIgnoreCase("200")) {

                    days = new Calendar[modelArrayListdate.getData().size()];
                    for (int i = 0; i < modelArrayListdate.getData().size(); i++) {

                        try {
                            days[i] = Global.convertStringToCalendar(modelArrayListdate.getData().get(i));
                            events.add(new EventDay(Global.convertStringToCalendar(modelArrayListdate.getData().get(i)), Global.getThreeDots(mActivity)));
                            activityCoachDetailsBinding.calendarView.setEvents(events);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    // Toaster.customToast(modelArrayListdate.getData().size()+"");

//                    if (modelArrayListdate.getData().size() == 0) {
//                        btnCalender.setVisibility(View.GONE);
//                        fl_addCartfirst.setVisibility(View.GONE);
//                    } else if (jsonArrayCart.length() > 0 && modelArrayListdate.getData().size() > 0) {
//                        fl_addCartfirst.setVisibility(View.GONE);
//                        btnCalender.setVisibility(View.VISIBLE);
//                        /*Visible whe 05-010-23 for doing cricket scoring layout live*/
//                        li_addCart_viewCart.setVisibility(View.GONE);
//                    } else if (jsonArrayCart.length() > 0 && modelArrayListdate.getData().size() == 0) {
//                        btnCalender.setVisibility(View.GONE);
//                        fl_addCartfirst.setVisibility(View.GONE);
//                        li_addCart_viewCart.setVisibility(View.GONE);
//                        /*Visible whe 05-010-23 for doing cricket scoring layout live*/
//                        fl_viewCartSecond.setVisibility(View.GONE);
//                    } else {
//                        btnCalender.setVisibility(View.VISIBLE);
//                        /*Visible whe 05-010-23 for doing cricket scoring layout live*/
//                        fl_addCartfirst.setVisibility(View.GONE);
//                        li_addCart_viewCart.setVisibility(View.GONE);
//                    }

                } else {
                    Toaster.customToast(modelArrayListdate.getErrors().getErrorText());
                }
            }

        }, error -> {
            error.printStackTrace();
            //    Global.dismissDialog(progressDialog);
            Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("coach_id", CoachId);
                param.put("month", month);
                param.put("year", year);
                Log.d("param", param.toString());

                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void BookCoach() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "create_booking_order", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CreateBookingResponse", response);
                loaderView.hideLoader();
                try {
                    Gson gson = new Gson();
                    ordercreate = gson.fromJson(response, OrderCreate.class);

                    if (ordercreate != null && ordercreate.getStatus() == 200) {
                        //JSONObject jsonObject1 = ordercreate.getJSONObject("data");

                        SessionManager.save_mobile_verified(prefs, ordercreate.getPaymentOption().getPrefill().getIs_conatct_verified());


                        if (ordercreate.getPayment() == 0) {
                            //BookingPaymentsDetails bookingPaymentsDetails = new BookingPaymentsDetails(jsonObject1);

                            if (Global.isOnline(mActivity)) {
                                sendPaymentSuccess();
                            } else {
                                Global.showDialog(mActivity);
                            }

                        } else {

                            Intent intent = new Intent(mActivity, CheckoutDetails.class);
                            intent.putExtra("key", (Serializable) modelArrayList);
                            intent.putExtra("key1", (Serializable) ordercreate);
                            intent.putExtra("booking_slot_date", dateGott);
                            intent.putExtra("booking_slot_id", mslotId);
                            startActivity(intent);

                            dateGott = "";
                            mslotId = "";


                        }


                        //finish();


                    } else {
                        Toaster.customToast(ordercreate.getErrors().getErrorText());
//                        if (ordercreate == null){
//                            Toaster.customToast(ordercreate.getErrors().getErrorText());
//                        }else{
//
//                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loaderView.hideLoader();
                Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return (headers != null || headers.isEmpty()) ? headers : super.getHeaders();
            }

            @Override
            protected Map<String, String> getParams() {
//                param.put("booking_date", getDateTime());
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("coach_id", CoachId);
                param.put("booking_slot_date", dateGott);
                param.put("booking_slot_id", mslotId);
                param.put("booking_amount", modelArrayList.getData().getPrice().getPaymentPrice());
                param.put("payment_amount", modelArrayList.getData().getPrice().getPayment_amount());
                param.put("taxes_amount", modelArrayList.getData().getPrice().getTaxesAmount());
                param.put("offer_id", modelArrayList.getData().getPrice().getOfferId());
                param.put("cuurency_code", "INR");
                Log.d("Param", param.toString());
                return param;
            }

        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void sendPaymentSuccess() {

        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.GET_BOOKING_DETAILS, (Response.Listener<String>) response -> {
            Log.d("PaymentResponse", response);
            loaderView.hideLoader();
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("api_text").equalsIgnoreCase("success")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    BookingPaymentsDetails bookingPaymentsDetails = new BookingPaymentsDetails(jsonObject1);

                    Intent intent = new Intent(CoachDetailsActivity.this, UserBookingDetails.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("Data", (Serializable) bookingPaymentsDetails);
                    intent.putExtra("FROM", "1");
                    intent.putExtra("PAYLATER", "PAID");
                    startActivity(intent);
                    finish();
                } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                    Toaster.customToast(jsonObject.optJSONObject("errors").optString("error_text"));
                } else {
                    Toaster.customToast(getResources().getString(R.string.socket_timeout));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, (Response.ErrorListener) error -> {
            error.printStackTrace();
            loaderView.hideLoader();
            Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("booking_id", String.valueOf(ordercreate.getBookingId()));
                Log.d("param", param.toString());
                return param;
            }

        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    void imageViewDialog(String url) {

        final Dialog dialog = new Dialog(mContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.profiledialog);
        dialog.setCancelable(true);
        PhotoView img = (PhotoView) dialog.findViewById(R.id.image_view);
        ImageView del = (ImageView) dialog.findViewById(R.id.del);
//        img.setScale(3);
        Glide.with(mContext).load(url)
                .into(img);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void EmailOtpDialog(CoachDetails modelArrayList, OrderCreate ordercreate) {
        final Dialog dialog = new Dialog(CoachDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_email_verification_checkout);
        dialog.setCancelable(false);

        TextView tvOTPTime = dialog.findViewById(R.id.tv_otp_time);
        TextView btResend = dialog.findViewById(R.id.btn_resend);
        Button btContinue = dialog.findViewById(R.id.btn_continue);
        ImageView img_close = dialog.findViewById(R.id.img_close);
        layout_enter_mobile_no = dialog.findViewById(R.id.layout_enter_mobile_no);
        layout_otp = dialog.findViewById(R.id.layout_otp);
        LinearLayout lay_otp_expire = dialog.findViewById(R.id.lay_otp_expire);
        CountryCodePicker ccp = dialog.findViewById(R.id.ccp);
        EditText edit_phone = dialog.findViewById(R.id.edit_phone);
        Button btn_send = dialog.findViewById(R.id.btn_send);

        btn_send.setOnClickListener(v -> {
            if (edit_phone.getText().toString().trim().isEmpty()) {
                Toaster.customToast(getResources().getString(R.string.Please_enter_mobile_no));
            } else {
                /*String phoneCode = ccp.getSelectedCountryCode();*/
                String phoneCode = "91";
                String phoneNumber = edit_phone.getText().toString().trim();
                updateMobileNo(phoneNumber, phoneCode, layout_otp, layout_enter_mobile_no);
            }

        });

//        layout_enter_mobile_no.setVisibility(View.GONE);
//        layout_otp.setVisibility(View.VISIBLE);
        startTimer(tvOTPTime, btResend, lay_otp_expire);

        btResend.setOnClickListener(view -> {
            String phoneCode = ccp.getSelectedCountryCode();
            String phoneNumber = edit_phone.getText().toString().trim();
            lay_otp_expire.setVisibility(View.VISIBLE);
            startTimer(tvOTPTime, btResend, lay_otp_expire);
            resendOTP(phoneNumber);
        });
        otpView = dialog.findViewById(R.id.otp_view);
        /*otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String s) {

            }
        });
        otpView.setOtpCompletionListener(s -> {
        });*/

        btContinue.setOnClickListener(v -> {
            lay_otp_expire.setVisibility(View.VISIBLE);
            if (Objects.requireNonNull(otpView.getText()).toString().isEmpty()) {
                Toaster.customToast(getString(R.string.code_msg));
            } else if (otpView.getText().toString().length() != 4) {
                Toaster.customToast(getString(R.string.code_invalid));
            } else {
                sendVerifyOtp(otpView.getText().toString().trim(), dialog, modelArrayList, ordercreate);
            }
        });

        img_close.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void resendOTP(String mobile) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.RESEND_OTP,
                response ->
                        Log.d("resendOtp", response),
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        loaderView.hideLoader();
                        Toaster.customToast(getResources().getString(R.string.socket_timeout));
//                Global.msgDialog(Signup.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("phone_number", mobile);
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                System.out.println("data   " + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void sendVerifyOtp(String otp, Dialog dialog, CoachDetails modelArrayList, OrderCreate ordercreate) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.OTP_VERIFY,
                response -> {
                    Log.d("SewndVerifyOtp", response);
                    loaderView.hideLoader();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {
                            dialog.dismiss();

                            if (Global.isOnline(mActivity)) {
                                BookCoach();
                            } else {
                                Global.showDialog(mActivity);
                            }

                            //finish();
                        } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                            otpView.setText("");
                            Toaster.customToast(jsonObject.optJSONObject("errors").optString("error_text"));
                        } else {
                            Toaster.customToast(getResources().getString(R.string.socket_timeout));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    loaderView.hideLoader();
                    Toaster.customToast(getResources().getString(R.string.socket_timeout));
//                Global.msgDialog(Signup.this, "Internet connection is slow");
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("otp", otp);
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                System.out.println("data   " + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void updateMobileNo(String mobile, String phone_code, LinearLayout lin_opt, LinearLayout lin_mobile) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.VERIFY_MOBILE,
                response -> {
                    Log.d("UpdateMobileNumber", response);
                    loaderView.hideLoader();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {
                            lin_mobile.setVisibility(View.GONE);
                            lin_opt.setVisibility(View.VISIBLE);
                            if (jsonObject.has("data")) {
                                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                //phoneNumber = jsonObjectData.getString("temp_mobile_no");
                                SessionManager.save_name(prefs, jsonObjectData.getString("username"));
                                SessionManager.save_emailid(prefs, jsonObjectData.getString("email"));
                                SessionManager.savePhone(prefs, jsonObjectData.getString("phone_number"));
                                SessionManager.savePhoneCode(prefs, jsonObjectData.getString("phone_code"));
                                SessionManager.save_mobile_verified(prefs, jsonObjectData.getString("is_mobile_verified"));
                                //congratsDialog();

                            }
                        } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                            lin_mobile.setVisibility(View.VISIBLE);
                            lin_opt.setVisibility(View.GONE);
                            Toaster.customToastUp(jsonObject.optJSONObject("errors").optString("error_text"));
                        } else {
                            Toaster.customToastUp(getResources().getString(R.string.socket_timeout));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        loaderView.hideLoader();
                        Toaster.customToast(getResources().getString(R.string.socket_timeout));
//                Global.msgDialog(Signup.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("phone_code", phone_code);
                param.put("phone_number", mobile);
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                System.out.println("data   " + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void startTimer(TextView tvOTPTime, TextView btResend, LinearLayout lay_otp_expire) {
        new CountDownTimer(180000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvOTPTime.setText("00 : " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                lay_otp_expire.setVisibility(View.GONE);
                btResend.setVisibility(View.VISIBLE);
            }

        }.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}