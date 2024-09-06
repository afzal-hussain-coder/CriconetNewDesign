package com.pb.criconet.CommonUI;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pb.criconet.Activity.LoginActivity;
import com.pb.criconet.R;
import com.pb.criconet.databinding.ActivityChangePasswordBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.SessionManager;
import com.pb.criconet.util.Toaster;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class ChangePasswordActivity extends AppCompatActivity {
    ActivityChangePasswordBinding activityChangePasswordBinding;
    Context mContext;
    Activity mActivity;
    private SharedPreferences prefs;
    private RequestQueue queue;
    CustomLoaderView loaderView;
    private String current_password;
    private String new_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChangePasswordBinding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(activityChangePasswordBinding.getRoot());

        mContext = this;
        mActivity = this;

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityChangePasswordBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(R.string.change_pass);
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> {
            finish();
        });

        loaderView = CustomLoaderView.initialize(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        queue = Volley.newRequestQueue(this);

        iniTView();


    }

    private void iniTView() {

        activityChangePasswordBinding.editTextCurrentPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityChangePasswordBinding.filledTextFieldCurrentPassword.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityChangePasswordBinding.filledTextFieldCurrentPassword.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityChangePasswordBinding.filledTextFieldCurrentPassword.setErrorEnabled(false);
            }
        });
        activityChangePasswordBinding.editTextNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityChangePasswordBinding.filledTextFieldNewPassword.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityChangePasswordBinding.filledTextFieldNewPassword.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityChangePasswordBinding.filledTextFieldNewPassword.setErrorEnabled(false);
            }
        });
        activityChangePasswordBinding.editTextRepeatPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityChangePasswordBinding.filledTextFieldRepeatPassword.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityChangePasswordBinding.filledTextFieldRepeatPassword.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityChangePasswordBinding.filledTextFieldRepeatPassword.setErrorEnabled(false);
            }
        });

        activityChangePasswordBinding.flRecover.setOnClickListener(v -> {
            checkValidation();
        });





    }

    public void checkValidation() {
        current_password = activityChangePasswordBinding.editTextCurrentPassword.getText().toString().trim();
        new_password = activityChangePasswordBinding.editTextNewPassword.getText().toString().trim();
        String repeat_password = activityChangePasswordBinding.editTextRepeatPassword.getText().toString().trim();
        if (current_password.length() < 1) {
            activityChangePasswordBinding.filledTextFieldCurrentPassword.setErrorEnabled(true);
            activityChangePasswordBinding.filledTextFieldCurrentPassword.setError(getResources().getString(R.string.Enter_Current_Password));
            activityChangePasswordBinding.filledTextFieldCurrentPassword.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(com.potyvideo.library.R.color.red)));
        } else if (current_password.length() < 6) {
            activityChangePasswordBinding.filledTextFieldCurrentPassword.setErrorEnabled(true);
            activityChangePasswordBinding.filledTextFieldCurrentPassword.setError(getResources().getString(R.string.password_too_short));
            activityChangePasswordBinding.filledTextFieldCurrentPassword.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(com.potyvideo.library.R.color.red)));
        } else if (new_password.length() < 1) {
            activityChangePasswordBinding.filledTextFieldNewPassword.setErrorEnabled(true);
            activityChangePasswordBinding.filledTextFieldNewPassword.setError(getResources().getString(R.string.Enter_New_Password));
            activityChangePasswordBinding.filledTextFieldNewPassword.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(com.potyvideo.library.R.color.red)));
        } else if (new_password.length() < 6) {
            activityChangePasswordBinding.filledTextFieldNewPassword.setErrorEnabled(true);
            activityChangePasswordBinding.filledTextFieldNewPassword.setError(getResources().getString(R.string.password_too_short));
            activityChangePasswordBinding.filledTextFieldNewPassword.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(com.potyvideo.library.R.color.red)));
        } else if (repeat_password.length() < 1) {
            activityChangePasswordBinding.filledTextFieldRepeatPassword.setErrorEnabled(true);
            activityChangePasswordBinding.filledTextFieldRepeatPassword.setError(getResources().getString(R.string.Enter_Repeat_Password));
            activityChangePasswordBinding.filledTextFieldRepeatPassword.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(com.potyvideo.library.R.color.red)));
        } else if (repeat_password.length() < 6) {
            activityChangePasswordBinding.filledTextFieldRepeatPassword.setErrorEnabled(true);
            activityChangePasswordBinding.filledTextFieldRepeatPassword.setError(getResources().getString(R.string.password_too_short));
            activityChangePasswordBinding.filledTextFieldRepeatPassword.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(com.potyvideo.library.R.color.red)));
        } else if (!new_password.equals(repeat_password)) {
            activityChangePasswordBinding.filledTextFieldRepeatPassword.setErrorEnabled(true);
            activityChangePasswordBinding.filledTextFieldRepeatPassword.setError(getResources().getString(R.string.Repeat_password_must_be_same_as_new_password));
            activityChangePasswordBinding.filledTextFieldRepeatPassword.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(com.potyvideo.library.R.color.red)));
        } else {
            if (Global.isOnline(mContext)) {
                changePassword();
            } else {
                Global.showDialog(mActivity);
            }
        }


    }

    private void changePassword() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "update_user_data",
                response -> {
                    Timber.e(response);
                    loaderView.hideLoader();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                            SessionManager.save_password(prefs, activityChangePasswordBinding.editTextCurrentPassword.getText().toString().trim());
                            msgDialog(mActivity, "Password Changed Successfully");
                            activityChangePasswordBinding.editTextCurrentPassword.setText("");
                            activityChangePasswordBinding.editTextNewPassword.setText("");
                            activityChangePasswordBinding.editTextRepeatPassword.setText("");
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
//                Global.msgDialog(ChangePassword.this, "Internet connection is slow");
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
//                type:  change_password
//                current_password:
//                new_password:"

                param.put("type", "change_password");
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("current_password", current_password);
                param.put("new_password", new_password);
                param.put("s", SessionManager.get_session_id(prefs));

                Timber.e("%s", param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);


    }

    public void msgDialog(Activity ac, String msg) {
        try {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(ac);
            alertbox.setTitle(ac.getResources().getString(R.string.app_name));
            alertbox.setMessage(Html.fromHtml(msg));
            alertbox.setPositiveButton(ac.getResources().getString(R.string.ok),
                    (arg0, arg1) -> {
                        if (Global.isOnline(mContext)) {
                            logout();
                        } else {
                            Toaster.customToast(getResources().getString(R.string.no_internet));
                        }
                    });
            alertbox.show();
        } catch (Exception e) {
        }
    }

    public void logout() {
        RequestQueue queue = Volley.newRequestQueue(this);
        final JSONObject json = new JSONObject();
        try {
            json.put("user_id", SessionManager.get_user_id(prefs));
            json.put("s", SessionManager.get_session_id(prefs));
            //Log.e(" data  : ", "" + json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        loaderView.showLoader();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, Global.URL + "logout", json,
                response -> {
                    Log.v("logout reponse", "" + response);
//                        {"status":"Success"}
                    loaderView.hideLoader();
                    try {
                        JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                        if (jsonObject.getString("api_status").equals("200")) {
                            SessionManager.dataclear(prefs);
                            SessionManager.save_check_agreement(prefs, true);
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (jsonObject.getString("api_status").equals("400")) {
                            Toaster.customToast(jsonObject.optJSONObject("errors").optString("error_text"));
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
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        queue.add(jsonObjectRequest);
    }
}