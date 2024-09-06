package com.pb.criconet.Activity.Academy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pb.criconet.R;
import com.pb.criconet.databinding.ActivityAcademyContactUsBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.SessionManager;
import com.pb.criconet.util.Toaster;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class AcademyContactUsActivity extends AppCompatActivity {

    ActivityAcademyContactUsBinding activityAcademyContactUsBinding;
    Context mContext;
    Activity mAcativity;
    private String name = "", phone = "", email = "", des = "";
    CustomLoaderView loaderView;
    private RequestQueue queue;
    private SharedPreferences prefs;
    String FROM = "", academyId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAcademyContactUsBinding = ActivityAcademyContactUsBinding.inflate(getLayoutInflater());
        setContentView(activityAcademyContactUsBinding.getRoot());

        mContext = this;
        mAcativity = this;

        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        loaderView = CustomLoaderView.initialize(mContext);
        queue = Volley.newRequestQueue(mContext);

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityAcademyContactUsBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(R.string.academy_support);
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> {
            finish();
        });

        if (getIntent() != null) {
            FROM = getIntent().getStringExtra("FROM");
            academyId = getIntent().getStringExtra("ACADEMY_ID");
            //_phoneNumber = getIntent().getStringExtra("PHONE");
        }

        initView();


    }

    private void initView() {


        activityAcademyContactUsBinding.editTextFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAcademyContactUsBinding.filledTextFieldName.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAcademyContactUsBinding.filledTextFieldName.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAcademyContactUsBinding.filledTextFieldName.setErrorEnabled(false);
            }
        });

        activityAcademyContactUsBinding.emailInputLayoutEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAcademyContactUsBinding.textInputLayoutEmail.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAcademyContactUsBinding.textInputLayoutEmail.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAcademyContactUsBinding.textInputLayoutEmail.setErrorEnabled(false);
            }
        });

        activityAcademyContactUsBinding.phoneInputLayoutEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAcademyContactUsBinding.phoneInputLayou.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAcademyContactUsBinding.phoneInputLayou.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAcademyContactUsBinding.phoneInputLayou.setErrorEnabled(false);
            }
        });

        activityAcademyContactUsBinding.typemessageTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAcademyContactUsBinding.typemessageTextInputLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAcademyContactUsBinding.typemessageTextInputLayout.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAcademyContactUsBinding.typemessageTextInputLayout.setErrorEnabled(false);
            }
        });


        activityAcademyContactUsBinding.flSubmit.setOnClickListener(v -> {
            checkValidation();
        });

    }

    public boolean checkValidation() {

        name = activityAcademyContactUsBinding.editTextFirstName.getText().toString().trim();
        email = activityAcademyContactUsBinding.emailInputLayoutEmail.getText().toString().trim();
        phone = activityAcademyContactUsBinding.phoneInputLayoutEditText.getText().toString().trim();
        des = activityAcademyContactUsBinding.typemessageTextInputEditText.getText().toString().trim();
        if (name.equalsIgnoreCase("")) {
            activityAcademyContactUsBinding.filledTextFieldName.setErrorEnabled(true);
            activityAcademyContactUsBinding.filledTextFieldName.setError("Enter Name");
            activityAcademyContactUsBinding.filledTextFieldName.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(com.potyvideo.library.R.color.red)));
            return false;
        } else if (!Global.validateLengthOfNameOfBookingRequest(name)) {
            activityAcademyContactUsBinding.filledTextFieldName.setErrorEnabled(true);
            activityAcademyContactUsBinding.filledTextFieldName.setError("Enter name (should be 3 to 30 characters");
            activityAcademyContactUsBinding.filledTextFieldName.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(com.potyvideo.library.R.color.red)));
            return false;
        } else if (email.equalsIgnoreCase("")) {
            activityAcademyContactUsBinding.textInputLayoutEmail.setErrorEnabled(true);
            activityAcademyContactUsBinding.textInputLayoutEmail.setError("Enter Email Id");
            activityAcademyContactUsBinding.textInputLayoutEmail.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(com.potyvideo.library.R.color.red)));
            return false;
        } else if (!Global.isValidEmail(email)) {
            activityAcademyContactUsBinding.textInputLayoutEmail.setErrorEnabled(true);
            activityAcademyContactUsBinding.textInputLayoutEmail.setError("Enter Valid Email");
            activityAcademyContactUsBinding.textInputLayoutEmail.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(com.potyvideo.library.R.color.red)));
            return false;
        } else if (phone.equalsIgnoreCase("")) {
            activityAcademyContactUsBinding.phoneInputLayou.setErrorEnabled(true);
            activityAcademyContactUsBinding.phoneInputLayou.setError("Enter Phone");
            activityAcademyContactUsBinding.phoneInputLayou.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(com.potyvideo.library.R.color.red)));
            return false;
        } else if (!Global.isValidMobile(phone)) {
            activityAcademyContactUsBinding.phoneInputLayou.setErrorEnabled(true);
            activityAcademyContactUsBinding.phoneInputLayou.setError("Enter valid phone number");
            activityAcademyContactUsBinding.phoneInputLayou.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(com.potyvideo.library.R.color.red)));
            return false;
        } else if (des.equalsIgnoreCase("")) {
            activityAcademyContactUsBinding.typemessageTextInputLayout.setErrorEnabled(true);
            activityAcademyContactUsBinding.typemessageTextInputLayout.setError("Enter Message");
            activityAcademyContactUsBinding.typemessageTextInputLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(com.potyvideo.library.R.color.red)));
            return false;
        } else {
            if (Global.isOnline(mContext)) {
                addContactUs();
            } else {
                Global.showDialog(mAcativity);
            }
        }
        return true;

    }

    private void addContactUs() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.ACADEMY_ENQUIRY, response -> {
            Timber.d(response);

            try {
                loaderView.hideLoader();
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                    Toaster.customToast(jsonObject.getString("message"));
                    activityAcademyContactUsBinding.editTextFirstName.setText("");
                    activityAcademyContactUsBinding.emailInputLayoutEmail.setText("");
                    activityAcademyContactUsBinding.phoneInputLayoutEditText.setText("");
                    activityAcademyContactUsBinding.typemessageTextInputEditText.setText("");
//                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
//                    SessionManager.save_academyId(prefs, String.valueOf(jsonObject1.getInt("academy")));
//
                    // startActivity(new Intent(mActivity, AddAcademyGalleryActivity.class));
                    new Handler().postDelayed((Runnable) this::finish, 1000);


                } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                    Global.msgDialog(mAcativity, jsonObject.getJSONObject("errors").getString("error_text"));
                } else {
                    Global.msgDialog(mAcativity, getResources().getString(R.string.error_server));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }, error -> {
            error.printStackTrace();
            loaderView.hideLoader();
            Global.msgDialog(mAcativity, getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("academy_id", academyId);

//                if(FROM.equalsIgnoreCase("2")){
//                    param.put("academy_id", academyId);
//                }else{
//                    param.put("academy_id", SessionManager.get_academyId(prefs));
//                }


                param.put("name", name);
                param.put("email", email);
                param.put("phone_number", phone);
                param.put("message", des);
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