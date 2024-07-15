package com.pb.criconet.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.pb.criconet.R;
import com.pb.criconet.databinding.ActivityLoginBinding;
import com.pb.criconet.model.UserData;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.SessionManager;
import com.pb.criconet.util.Toaster;
import org.json.JSONObject;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    ActivityLoginBinding activityLoginBinding;
    Context mContext;
    Activity mActivity;
    CustomLoaderView loaderView;
    RequestQueue queue;
    SharedPreferences prefs;

    /*Gmail Login*/
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(activityLoginBinding.getRoot());
        mContext = this;
        mActivity = this;

        loaderView = CustomLoaderView.initialize(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        queue = Volley.newRequestQueue(mContext);

        /*Gmail Login*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1072621342122-3e5u6pb066sr6orefostgdg1teih9dcp.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.tv_forgot).setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgetPasswordActvity.class));
        });

        findViewById(R.id.register).setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });

        activityLoginBinding.tvLogin.setOnClickListener(v -> {
            Global.printKeyHash(mActivity);

            if (!Global.validateName(activityLoginBinding.editTextUsername.getText().toString())) {
                Toaster.customToast(getResources().getString(R.string.Username_and_email_id_cant_be_empty));
            } else if (!Global.validateName(activityLoginBinding.editTextPassword.getText().toString())) {
                Toaster.customToast(getResources().getString(R.string.The_Password_Cant_be_empty));
            } else {
                if (Global.isOnline(mContext)) {
                    login_task(activityLoginBinding.editTextUsername.getText().toString(), activityLoginBinding.editTextPassword.getText().toString());
                } else {
                    Toast.makeText(mContext, R.string.no_internet, Toast.LENGTH_LONG).show();
                }
            }
        });

        activityLoginBinding.liGmailLogin.setOnClickListener(v -> {
            googleSignIn();
        });

    }

    public void login_task(final String email_String, final String password_String) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "user_login",
                response -> {
                    Log.v("login reponse", "" + response);
                    loaderView.hideLoader();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                            activityLoginBinding.editTextUsername.setText("");
                            activityLoginBinding.editTextPassword.setText("");


                            SessionManager.save_user_id(prefs, jsonObject.optString("user_id"));
                            SessionManager.save_session_id(prefs, jsonObject.optString("session_id"));
                            SessionManager.save_name(prefs, email_String);
                            SessionManager.save_emailid(prefs, email_String);
                            SessionManager.save_password(prefs, password_String);
//                                SessionManager.save_session_id(prefs, jsonObject.optString("session_id"));
//                                SessionManager.save_image(prefs, jsonObject.optString("i"));
                            // SessionManager.save_check_login(prefs, true);

                            JSONObject jsonData = jsonObject.optJSONObject("data");
                            UserData userData = UserData.fromJson(jsonObject.optJSONObject("data"));
                            SessionManager.save_profile_persantage(prefs, userData.getTotal_completed_profile());
                            SessionManager.save_user_id(prefs, userData.getUser_id());
                            SessionManager.save_name(prefs, userData.getUsername());
                            SessionManager.save_verified(prefs, userData.getVerified());
                            SessionManager.save_criconet_verified(prefs, userData.getCriconet_verified());
                            SessionManager.save_emailid(prefs, userData.getEmail());
                            SessionManager.save_sex(prefs, userData.getGender());
                            SessionManager.save_address(prefs, userData.getAddress());
                            SessionManager.save_image(prefs, userData.getAvatar());
                            SessionManager.save_cover(prefs, userData.getCover());
                            SessionManager.save_fname(prefs, userData.getFirst_name());
                            SessionManager.save_lname(prefs, userData.getLast_name());
                            SessionManager.save_profiletype(prefs, userData.getProfile_type());
                            SessionManager.save_mobile_verified(prefs, userData.getIs_mobile_verified());
                            //Toaster.customToast(SessionManager.get_mobile_verified(prefs));

                            if (jsonData.has("academy")) {
                                JSONObject jsonObject1 = jsonData.getJSONObject("academy");

                                if (jsonObject1.length() > 0) {
                                    SessionManager.save_academyId(prefs, jsonObject1.getString("id"));
                                    SessionManager.save_academyNumber(prefs, jsonObject1.getString("contact_person_phone"));


                                    if (jsonObject1.has("role")) {
                                        JSONObject jsonObject2 = jsonObject1.getJSONObject("role");
                                        if (jsonObject2.has("role_id")) {
                                            SessionManager.save_roleId(prefs, jsonObject2.getString("role_id"));
                                        }
                                    }

                                } else {
                                    SessionManager.save_academyId(prefs, "");
                                }

                            }

                            String is_mobile_verified = userData.getIs_mobile_verified();
                            String is_email_verified = userData.getIs_email_verified();

                            if (jsonData.has("ambassadorProfile")) {
                                JSONObject ambassadorProfile = jsonData.getJSONObject("ambassadorProfile");

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
                                //Toaster.customToast(ambassadorProfile.length()+"");
                            }
                            if (userData.getProfile_type().equalsIgnoreCase("coach")) {

                                if (jsonData.has("coachProfile")) {
                                    JSONObject jsonObject1 = jsonData.getJSONObject("coachProfile");
                                    SessionManager.save_coach_id(prefs, jsonObject1.getString("coach_id"));
                                }


                            } else {
                                SessionManager.save_coach_id(prefs, "");
                            }


                            /*......check..*/
                            if (is_mobile_verified.equalsIgnoreCase("0") && is_email_verified.equalsIgnoreCase("0")) {

                                startActivity(new Intent(LoginActivity.this, VerifiedMobileNumberActivity.class));

                            } else if (userData.getActive().equalsIgnoreCase("2")) {
                                Toaster.customToast("Please contact criconet to activate your account");
                            } else if (is_mobile_verified.equalsIgnoreCase("1") || is_email_verified.equalsIgnoreCase("1")) {
                                SessionManager.save_check_login(prefs, true);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
 /*Date...[9:57 PM, 7/6/2022] 🙃🙂: Reason not changed as web is not having mobile number
[9:57 PM, 7/6/2022] 🙃🙂: and removed this as we need to check the status of email and mobile*/

//                            Intent intent = new Intent(Login.this, MainActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//                            finish();

                        } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {

                            Toaster.customToast(jsonObject.optJSONObject("errors").optString("error_text"));
                            //Global.msgDialog(Login.this, jsonObject.optJSONObject("errors").optString("error_text"));
                        } else {
                            Toaster.customToast(getResources().getString(R.string.socket_timeout));
                            //Global.msgDialog(Login.this, "Error in server");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    loaderView.hideLoader();
                    Toaster.customToast(getResources().getString(R.string.socket_timeout));
//                Global.msgDialog(Login.this, "Internet connection is slow");
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("username", email_String);
                param.put("password", password_String);
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

    private void googleSignIn() {
//        Auth.GoogleSignInApi.signOut(mGoogleSignInClient);
        mGoogleSignInClient.signOut();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // Signed in successfully, show authenticated UI.
                updateUI(account);
            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Toaster.customToast(e.getStatusCode()+"");
                Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                updateUI(null);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateUI(@Nullable GoogleSignInAccount account) {

        if (account != null) {
            String name = account.getDisplayName();
            String imageurl = "";
            try {
                Uri uri = account.getPhotoUrl();
                if (uri != null)
                    imageurl = new URL(account.getPhotoUrl().toString()).toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            String email = account.getEmail();
            String id = account.getId();
            String logintype = "Google";

            Log.e(TAG, "updateUI name: " + name);
            Log.e(TAG, "updateUI imageurl: " + imageurl);
            Log.e(TAG, "updateUI email: " + email);
            Log.e(TAG, "updateUI id: " + id);

            signup_tasksocial(Global.URL + "social_login", name, email, logintype, id, imageurl);

        } else {
            Toast.makeText(this, getResources().getString(R.string.SignIn_failed), Toast.LENGTH_SHORT).show();
        }
    }

    public void signup_tasksocial(String url, final String name_string, final String email_String, final String logintype, final String personid, final String image) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    loaderView.hideLoader();
                    Log.d("socialLogin", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {
                            SessionManager.save_user_id(prefs, jsonObject.optString("user_id"));
                            SessionManager.save_session_id(prefs, jsonObject.optString("session_id"));
                            SessionManager.save_check_login(prefs, true);
                            JSONObject jsonData = jsonObject.optJSONObject("data");
                            UserData userData = UserData.fromJson(jsonObject.optJSONObject("data"));
                            SessionManager.save_profile_persantage(prefs,userData.getTotal_completed_profile());
                            SessionManager.save_user_id(prefs, userData.getUser_id());
                            SessionManager.save_name(prefs, userData.getUsername());
                            SessionManager.save_verified(prefs,userData.getVerified());
                            SessionManager.save_criconet_verified(prefs,userData.getCriconet_verified());
                            SessionManager.save_emailid(prefs, userData.getEmail());
                            SessionManager.save_sex(prefs, userData.getGender());
                            SessionManager.save_address(prefs, userData.getAddress());
                            SessionManager.save_image(prefs, userData.getAvatar());
                            SessionManager.save_cover(prefs, userData.getCover());
                            SessionManager.save_profiletype(prefs, userData.getProfile_type());
                            SessionManager.save_mobile_verified(prefs, userData.getIs_mobile_verified());

                            if (jsonData.has("academy")) {
                                JSONObject jsonObject1 = jsonData.getJSONObject("academy");

                                if (jsonObject1.length() > 0) {
                                    SessionManager.save_academyId(prefs, jsonObject1.getString("id"));
                                    SessionManager.save_academyNumber(prefs,jsonObject1.getString("contact_person_phone"));
                                    if(jsonObject1.has("role")){
                                        JSONObject jsonObject2= jsonObject1.getJSONObject("role");
                                        if(jsonObject2.has("role_id")){
                                            SessionManager.save_roleId(prefs,jsonObject2.getString("role_id"));
                                        }
                                    }

                                } else {
                                    SessionManager.save_academyId(prefs, "");
                                }

                            }
                            String is_mobile_verified = userData.getIs_mobile_verified();
                            String is_email_verified = userData.getIs_email_verified();

                            if (jsonData.has("ambassadorProfile")) {
                                JSONObject ambassadorProfile = jsonData.optJSONObject("ambassadorProfile");

                                if (ambassadorProfile.length() > 0) {
                                    SessionManager.save_is_ambassador(prefs, "1");
                                    SessionManager.save_is_amb_name(prefs, ambassadorProfile.optString("name"));
                                    SessionManager.save_is_amb_fullname(prefs, ambassadorProfile.optString("full_name"));
                                    SessionManager.save_is_amb_email(prefs, ambassadorProfile.optString("email"));
                                    SessionManager.save_mobile(prefs, ambassadorProfile.optString("phone_number"));
                                    SessionManager.save_is_amb_college(prefs, ambassadorProfile.optString("school_college_name"));
                                    SessionManager.save_is_amb_highestQ(prefs, ambassadorProfile.optString("height_qualification"));
                                    SessionManager.save_is_ambs_have_you_org_event_flag(prefs, ambassadorProfile.optString("have_you_org_event_flag"));
                                    SessionManager.save_is_ambs_have_you_org_event_txt(prefs, ambassadorProfile.optString("have_you_org_event_txt"));
                                    SessionManager.save_is_ambs_innovative_thing(prefs, ambassadorProfile.optString("innovative_thing"));
                                    SessionManager.save_is_ambs_how_many_hrs_per_week(prefs, ambassadorProfile.optString("how_many_hrs_per_week"));
                                    SessionManager.save_is_ambs_passionate_thing(prefs, ambassadorProfile.optString("passionate_thing"));
                                    SessionManager.save_is_ambs_do_you_want_campus_ambassdor(prefs, ambassadorProfile.optString("do_you_want_campus_ambassdor"));
                                    SessionManager.save_is_ambs_thing_you_are_know_criconet(prefs, ambassadorProfile.optString("thing_you_are_know_criconet"));
                                } else {
                                    SessionManager.save_is_ambassador(prefs, "0");
                                }

                            }

                            if (userData.getProfile_type().equalsIgnoreCase("coach")) {
                                JSONObject jsonObject1 = jsonData.optJSONObject("coachProfile");
                                SessionManager.save_coach_id(prefs, jsonObject1.optString("coach_id"));
                            } else {
                                SessionManager.save_coach_id(prefs, "");
                            }


                            if (is_mobile_verified.equalsIgnoreCase("0") && is_email_verified.equalsIgnoreCase("0")) {
                                startActivity(new Intent(LoginActivity.this, VerifiedMobileNumberActivity.class));
                            } else if (is_mobile_verified.equalsIgnoreCase("1") || is_email_verified.equalsIgnoreCase("1")) {
                                SessionManager.save_check_login(prefs, true);
                                Intent intent = new Intent(mContext, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                            Toaster.customToast(getResources().getString(R.string.message_internal_inconsistency));
                            //Global.msgDialog(Login.this, jsonObject.optJSONObject("errors").optString("error_text"));
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
                param.put("provider", logintype);
                param.put("identifier", personid);
                param.put("first_name", name_string);
                param.put("email", email_String);
                param.put("device", "android");

                param.put("device_id", SessionManager.get_devicetoken(prefs));
                param.put("deviceToken", SessionManager.get_devicetoken(prefs));
                param.put("firebaseDeviceTocken", SessionManager.get_devicetoken(prefs));
                param.put("firebaseId", SessionManager.get_firebaseId(prefs));
                param.put("photoURL", image);

//                param.put("socialMediaProfile", location_string);
//                param.put("phone", phone_string);
//                param.put("address", location_string);

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