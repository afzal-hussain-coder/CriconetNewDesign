package com.pb.criconet.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pb.criconet.R;
import com.pb.criconet.databinding.ActivityVerifiedMobileNumberBinding;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.SessionManager;
import com.pb.criconet.util.Toaster;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VerifiedMobileNumberActivity extends AppCompatActivity {
    ActivityVerifiedMobileNumberBinding activityVerifiedMobileNumberBinding;
    Context mContext;
    Activity mActivity;
    CustomLoaderView loaderView;
    RequestQueue queue;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityVerifiedMobileNumberBinding = ActivityVerifiedMobileNumberBinding.inflate(getLayoutInflater());
        setContentView(activityVerifiedMobileNumberBinding.getRoot());
        mContext = this;
        mActivity = this;

        loaderView = CustomLoaderView.initialize(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        queue = Volley.newRequestQueue(mContext);

        activityVerifiedMobileNumberBinding.flSubmit.setOnClickListener(v -> {
            if (activityVerifiedMobileNumberBinding.editTextPhone.getText().toString().isEmpty()) {
                Toaster.customToast(getResources().getString(R.string.Please_enter_mobile_no));
            } else {
//                /String phoneCode = ccp.getSelectedCountryCode();/
                String phoneCode = "91";
                String phoneNumber = activityVerifiedMobileNumberBinding.editTextPhone.getText().toString();
                updateMobileNo(phoneNumber, phoneCode);
            }
        });


    }

    private void updateMobileNo(String mobile, String phone_code) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.VERIFY_MOBILE,
                response -> {
                    Log.d("MobileResend", response);
                    loaderView.hideLoader();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {

                            startActivity(new Intent(VerifiedMobileNumberActivity.this, VerifyOtpActivity.class));

                            if (jsonObject.has("data")) {
                                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                //phoneNumber = jsonObjectData.getString("temp_mobile_no");
                                SessionManager.save_name(prefs, jsonObjectData.getString("username"));
                                SessionManager.save_emailid(prefs, jsonObjectData.getString("email"));
                                SessionManager.savePhone(prefs, jsonObjectData.getString("phone_number"));
                                SessionManager.savePhoneCode(prefs, jsonObjectData.getString("phone_code"));
                                SessionManager.save_mobile_verified(prefs, jsonObjectData.getString("is_mobile_verified"));

                            }
                        } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                            Toaster.customToastUp(jsonObject.optJSONObject("errors").optString("error_text"));
                        } else {
                            Toaster.customToastUp(getResources().getString(R.string.socket_timeout));
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
}