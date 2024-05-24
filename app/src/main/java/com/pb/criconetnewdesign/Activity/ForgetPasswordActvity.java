package com.pb.criconetnewdesign.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.databinding.ActivityForgetPasswordActvityBinding;
import com.pb.criconetnewdesign.util.CustomLoaderView;
import com.pb.criconetnewdesign.util.Global;
import com.pb.criconetnewdesign.util.SessionManager;
import com.pb.criconetnewdesign.util.Toaster;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgetPasswordActvity extends AppCompatActivity {
    ActivityForgetPasswordActvityBinding activityForgetPasswordActvityBinding;
    Context mContext;
    Activity mActivity;
    RequestQueue queue;
    CustomLoaderView loaderView;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityForgetPasswordActvityBinding = ActivityForgetPasswordActvityBinding.inflate(getLayoutInflater());
        setContentView(activityForgetPasswordActvityBinding.getRoot());

        mContext = this;
        mActivity = this;
        loaderView = CustomLoaderView.initialize(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        queue = Volley.newRequestQueue(mContext);

        findViewById(R.id.img_back).setOnClickListener(v -> {
            finish();
        });

        activityForgetPasswordActvityBinding.flRecover.setOnClickListener(v -> {

            if (activityForgetPasswordActvityBinding.editTextEmail.getText().toString().isEmpty()) {
                Toaster.customToast(getResources().getString(R.string.Email_Cant_be_empty));
            } else if (!Global.ValidEmail(activityForgetPasswordActvityBinding.editTextEmail.getText().toString())) {
                Toaster.customToast(getResources().getString(R.string.Invalid_email_format));
            } else {
                if (Global.isOnline(mContext)) {
                    forgot_task(activityForgetPasswordActvityBinding.editTextEmail.getText().toString());
                } else {
                    Toaster.customToast(getResources().getString(R.string.no_internet));
                }
            }
        });

    }

    public void forgot_task(final String email_String) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "forget_password",
                response -> {
                    Log.d("ForgetPasswordResponse", response);
                    loaderView.hideLoader();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                            activityForgetPasswordActvityBinding.editTextEmail.setText("");
                            Toaster.customToast(jsonObject.optString("message"));
                        } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                            Toaster.customToast(jsonObject.optString("message"));
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
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("email", email_String);
//                param.put("device_id", SessionManager.get_devicetoken(prefs));
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