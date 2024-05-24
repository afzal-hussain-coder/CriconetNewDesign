package com.pb.criconetnewdesign.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.databinding.ActivityVerifyOtpBinding;
import com.pb.criconetnewdesign.util.CustomLoaderView;
import com.pb.criconetnewdesign.util.Global;
import com.pb.criconetnewdesign.util.SessionManager;
import com.pb.criconetnewdesign.util.Toaster;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class VerifyOtpActivity extends AppCompatActivity {
    ActivityVerifyOtpBinding activityVerifyOtpBinding;
    CustomLoaderView loaderView;
    RequestQueue queue;
    SharedPreferences prefs;
    String phoneNumber = "";
    Context mContext;
    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityVerifyOtpBinding = ActivityVerifyOtpBinding.inflate(getLayoutInflater());
        setContentView(activityVerifyOtpBinding.getRoot());
        mContext = this;
        mActivity = this;

        loaderView = CustomLoaderView.initialize(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        queue = Volley.newRequestQueue(mContext);

        activityVerifyOtpBinding.flSubmit.setOnClickListener(v -> {
            activityVerifyOtpBinding.layOtpExpire.setVisibility(View.VISIBLE);

            if (Objects.requireNonNull(activityVerifyOtpBinding.editTextOtp.getText()).toString().isEmpty()) {
                Toaster.customToast(getString(R.string.code_msg));
            } else if (activityVerifyOtpBinding.editTextOtp.getText().toString().length() != 4) {
                Toaster.customToast(getString(R.string.code_invalid));
            } else {
                sendVerifyOtp(activityVerifyOtpBinding.editTextOtp.getText().toString().trim());
            }

        });

        startTimer();

        activityVerifyOtpBinding.tvResendOtp.setOnClickListener(v -> {
            resendOTP();
            activityVerifyOtpBinding.layOtpExpire.setVisibility(View.VISIBLE);
            startTimer();
        });

    }

//    private void showWelcomeDialog() {
//        final Dialog dialog = new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.custom_dialog_welcome);
//
//        new Handler().postDelayed(() -> {
//            Intent intent = new Intent(mContext, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
//        }, 2000);
//
//        dialog.show();
//    }

    private void sendVerifyOtp(String otp) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.OTP_VERIFY,
                response -> {
                    Log.d("OTP Response", response);
                    loaderView.hideLoader();
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {

                            if (jsonObject.has("data")) {
                                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                phoneNumber = jsonObjectData.getString("temp_mobile_no");
                                SessionManager.save_name(prefs, jsonObjectData.getString("username"));
                                SessionManager.save_emailid(prefs, jsonObjectData.getString("email"));
                                SessionManager.save_mobile(prefs, jsonObjectData.getString("phone_number"));
                                SessionManager.savePhoneCode(prefs, jsonObjectData.getString("phone_code"));

                                // Toaster.customToast(jsonObjectData.getString("is_mobile_verified"));
                                SessionManager.save_mobile_verified(prefs, jsonObjectData.getString("is_mobile_verified"));
                                JSONObject ambassadorProfile = jsonObjectData.getJSONObject("ambassadorProfile");

                                if (ambassadorProfile.length() > 0) {
                                    SessionManager.save_is_ambassador(prefs, "1");
                                    SessionManager.save_is_amb_name(prefs, ambassadorProfile.getString("name"));
                                    SessionManager.save_is_amb_fullname(prefs, ambassadorProfile.getString("full_name"));
                                    SessionManager.save_is_amb_email(prefs, ambassadorProfile.getString("email"));
                                    SessionManager.save_mobile(prefs, ambassadorProfile.getString("phone_number"));
                                    SessionManager.save_is_amb_college(prefs, ambassadorProfile.getString("school_college_name"));
                                    SessionManager.save_is_amb_highestQ(prefs, ambassadorProfile.getString("height_qualification"));
                                    SessionManager.save_is_ambs_have_you_org_event_flag(prefs, ambassadorProfile.getString("have_you_org_event_flag"));
                                    SessionManager.save_is_ambs_have_you_org_event_txt(prefs, ambassadorProfile.getString("have_you_org_event_txt"));
                                    SessionManager.save_is_ambs_innovative_thing(prefs, ambassadorProfile.getString("innovative_thing"));
                                    SessionManager.save_is_ambs_how_many_hrs_per_week(prefs, ambassadorProfile.getString("how_many_hrs_per_week"));
                                    SessionManager.save_is_ambs_passionate_thing(prefs, ambassadorProfile.getString("passionate_thing"));
                                    SessionManager.save_is_ambs_do_you_want_campus_ambassdor(prefs, ambassadorProfile.getString("do_you_want_campus_ambassdor"));
                                    SessionManager.save_is_ambs_thing_you_are_know_criconet(prefs, ambassadorProfile.getString("thing_you_are_know_criconet"));
                                } else {
                                    SessionManager.save_is_ambassador(prefs, "0");
                                }


                                new Handler().postDelayed(() -> {
                                    Intent intent = new Intent(mContext, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }, 2000);

                                //showWelcomeDialog();
                                SessionManager.save_check_login(prefs, true);

                            }

                        } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                            Toaster.customToast(jsonObject.optJSONObject("errors").optString("error_text"));
                        } else {
                            Toaster.customToast(getResources().getString(R.string.socket_timeout));
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
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
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

    private void startTimer() {
        new CountDownTimer(180000, 1000) {

            public void onTick(long millisUntilFinished) {
                activityVerifyOtpBinding.tvOtpTime.setText("00 : " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                activityVerifyOtpBinding.layOtpExpire.setVisibility(View.GONE);
                activityVerifyOtpBinding.tvResendOtp.setVisibility(View.VISIBLE);

            }

        }.start();
    }

    private void resendOTP() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.RESEND_OTP,
                response -> {
                    Log.d("OTP Resend Response", response);
                    loaderView.hideLoader();
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {

                        } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
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
                param.put("phone_number", SessionManager.get_mobile(prefs));
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
}