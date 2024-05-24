package com.pb.criconetnewdesign.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.databinding.ActivitySignUpBinding;
import com.pb.criconetnewdesign.util.CustomLoaderView;
import com.pb.criconetnewdesign.util.DatePickerUtils;
import com.pb.criconetnewdesign.util.Global;
import com.pb.criconetnewdesign.util.SessionManager;
import com.pb.criconetnewdesign.util.Toaster;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding activitySignUpBinding;
    Context mContext;
    Activity mActivity;
    CustomLoaderView loaderView;
    RequestQueue queue;
    SharedPreferences prefs;
    String phoneNumber = "";
    String gender = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySignUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(activitySignUpBinding.getRoot());

        mContext = this;
        mActivity = this;

        loaderView = CustomLoaderView.initialize(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        queue = Volley.newRequestQueue(mContext);

        activitySignUpBinding.tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        });

        activitySignUpBinding.radioFemale.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked == true) {
                activitySignUpBinding.radioMale.setChecked(false);
            }
            gender = activitySignUpBinding.female.getText().toString();
        });

        activitySignUpBinding.radioMale.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked == true) {
                activitySignUpBinding.radioFemale.setChecked(false);
            }
            gender = activitySignUpBinding.male.getText().toString();
        });


        activitySignUpBinding.flRegister.setOnClickListener(v -> {

            if (activitySignUpBinding.editTextEmail.getText().toString().isEmpty()) {
                Toaster.customToast(getResources().getString(R.string.email_empty_validation));
            } else if (!Global.isValidEmail(activitySignUpBinding.editTextEmail.getText().toString())) {
                Toaster.customToast(getResources().getString(R.string.email_invalid_validation));
            } else if (activitySignUpBinding.editTextName.getText().toString().isEmpty()) {
                Toaster.customToast(getResources().getString(R.string.name_empty_validation));
            } else if (activitySignUpBinding.editTextName.length() < 3) {
                Toaster.customToast(getResources().getString(R.string.name_limit_validation));
            } else if (!activitySignUpBinding.editTextName.getText().toString().matches("[a-zA-Z0-9.? ]*")) {
                Toaster.customToast(getResources().getString(R.string.Special_character_not_allowed));
            } else if (activitySignUpBinding.editTextDob.getText().toString().isEmpty()) {
                Toaster.customToast(getResources().getString(R.string.date_of_birth));
            } else if (activitySignUpBinding.editTextPhone.getText().toString().isEmpty()) {
                Toaster.customToast(getResources().getString(R.string.phone_error));
            } else if (!activitySignUpBinding.editTextPhone.getText().toString().matches("[0-9.? ]*")) {
                Toaster.customToast(getResources().getString(R.string.Special_character_not_allowed));
            } else if (!Global.isValidPhoneNumber(activitySignUpBinding.editTextPhone.getText().toString())) {
                Toaster.customToast(getResources().getString(R.string.error_invalid_phone));
            } else if (gender.isEmpty()) {
                Toaster.customToast(getResources().getString(R.string.Select_Gender));
            } else if (activitySignUpBinding.editTextPassword.getText().toString().isEmpty()) {
                Toaster.customToast(getResources().getString(R.string.password_cannot_empty));
            } else if (activitySignUpBinding.editTextPassword.getText().toString().length() < 6) {
                Toaster.customToast(getResources().getString(R.string.password_too_short));
            } else if (activitySignUpBinding.editTextConpassword.getText().toString().isEmpty()) {
                Toaster.customToast(getResources().getString(R.string.confirm_password_cant_be_empty));
            } else if (!activitySignUpBinding.editTextPassword.getText().toString().equals(activitySignUpBinding.editTextConpassword.getText().toString())) {
                Toaster.customToast(getResources().getString(R.string.Password_does_not_match));
            } else if (activitySignUpBinding.cbTearms.isChecked() == false) {
                Toaster.customToast(getResources().getString(R.string.tearms_condition_validation));
            } else {
                if (Global.isOnline(mContext)) {
                    signup_task();
                } else {
                    Toaster.customToast(getResources().getString(R.string.no_internet));
                }
            }
        });

        activitySignUpBinding.editTextDob.setOnClickListener(v -> {
            showDatePickerDialog();
        });

    }

    private void showDatePickerDialog() {
        DatePickerUtils.showDatePickerDialog(this, (day, month, year) -> {
            // Set the selected date to the EditText
            activitySignUpBinding.editTextDob.setText(day + "/" + month + "/" + year);
        });
    }

    public void signup_task() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.REGISTER_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("SignUpResponse", response);
                        loaderView.hideLoader();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("api_text").equalsIgnoreCase("success")) {
                                SessionManager.save_user_id(prefs, jsonObject.getString("user_id"));
                                SessionManager.save_session_id(prefs, jsonObject.getString("session_id"));

                                if (jsonObject.has("data")) {
                                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                    phoneNumber = jsonObjectData.getString("temp_mobile_no");

                                    SessionManager.save_name(prefs, jsonObjectData.getString("username"));
                                    SessionManager.save_emailid(prefs, jsonObjectData.getString("email"));
                                    SessionManager.savePhone(prefs, jsonObjectData.getString("phone_number"));
                                    SessionManager.savePhoneCode(prefs, jsonObjectData.getString("phone_code"));
                                    SessionManager.save_sex(prefs, jsonObjectData.getString("gender"));
                                    SessionManager.save_image(prefs, jsonObjectData.getString("avatar"));
                                    SessionManager.save_cover(prefs, jsonObjectData.getString("cover"));
                                    //SessionManager.save_mobile_verified(prefs, jsonObjectData.getString("is_mobile_verified"));


                                    if (jsonObjectData.getString("profile_type") == null) {

                                        SessionManager.save_profiletype(prefs, jsonObjectData.getString("profile_type"));
                                    } else {
                                        SessionManager.save_profiletype(prefs, jsonObjectData.getString("profile_type"));
                                    }

                                    activitySignUpBinding.editTextEmail.setText("");
                                    activitySignUpBinding.editTextName.setText("");
                                    activitySignUpBinding.editTextDob.setText("");
                                    activitySignUpBinding.editTextPhone.setText("");
                                    activitySignUpBinding.editTextPassword.setText("");
                                    activitySignUpBinding.editTextConpassword.setText("");
                                    activitySignUpBinding.radioMale.setChecked(false);
                                    activitySignUpBinding.radioFemale.setChecked(false);
                                    activitySignUpBinding.cbTearms.setChecked(false);

                                }
                                startActivity(new Intent(SignUpActivity.this, VerifyOtpActivity.class));

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Toaster.customToast(jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Toaster.customToast(getResources().getString(R.string.socket_timeout));
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
                        Toaster.customToast(getResources().getString(R.string.socket_timeout));
//                Global.msgDialog(Signup.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("username", activitySignUpBinding.editTextName.getText().toString());
                param.put("email", activitySignUpBinding.editTextEmail.getText().toString());
                param.put("phone_number", activitySignUpBinding.editTextPhone.getText().toString());
//                param.put("phone_code", phoneCode);
                param.put("ref_code", activitySignUpBinding.editTextReferalCode.getText().toString());
                param.put("birthday",activitySignUpBinding.editTextDob.getText().toString());

//                if (profileType.equalsIgnoreCase("")) {
//                    param.put("profile_type", "");
//                } else {
//                    param.put("profile_type", profileType.toLowerCase());
//                }

                param.put("password", activitySignUpBinding.editTextPassword.getText().toString());
                param.put("confirm_password", activitySignUpBinding.editTextConpassword.getText().toString());
                param.put("gender", gender.toLowerCase());
                param.put("device", "android");
                param.put("device_id", SessionManager.get_devicetoken(prefs));
                param.put("s", "1");
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