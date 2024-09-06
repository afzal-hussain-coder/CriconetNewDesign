package com.pb.criconet.Activity.User;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.pb.criconet.CommonUI.ChangePasswordActivity;
import com.pb.criconet.CustomeCamera.CustomeCameraActivity;
import com.pb.criconet.R;
import com.pb.criconet.databinding.ActivityUserProfileBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.model.UserData;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.MultipartRequest;
import com.pb.criconet.util.SessionManager;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class UserProfileActivity extends AppCompatActivity {
    ActivityUserProfileBinding activityUserProfileBinding;
    Context mContext;
    Activity mActivity;
    public static Uri image_uri = null;
    private SharedPreferences prefs;
    private RequestQueue queue;
    CustomLoaderView loaderView;
    private UserData userData;

    String email_String = "", name_String = "", phone = "", address = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserProfileBinding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(activityUserProfileBinding.getRoot());
        mContext = this;
        mActivity = this;

        loaderView = CustomLoaderView.initialize(mContext);
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        queue = Volley.newRequestQueue(mContext);

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityUserProfileBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.my_profile));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());


        if (Global.isOnline(mContext)) {
            getUsersDetails(SessionManager.get_user_id(prefs));
        } else {
            Global.showDialog(mActivity);
        }

        inItView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != image_uri) {
            activityUserProfileBinding.profilePic.setImageURI(image_uri);
            updateImageTask(image_uri.getPath());
        }
    }

    private void inItView() {
        activityUserProfileBinding.tvChangePhoto.setOnClickListener(v -> {
            startActivity(new Intent(mContext, CustomeCameraActivity.class)
                    .putExtra("FROM", "UserProfileActivity"));
        });

        activityUserProfileBinding.tvChangePass.setOnClickListener(v -> {
            startActivity(new Intent(mContext, ChangePasswordActivity.class));
        });

        activityUserProfileBinding.editTextUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityUserProfileBinding.filledTextFieldUsername.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityUserProfileBinding.filledTextFieldUsername.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityUserProfileBinding.filledTextFieldUsername.setErrorEnabled(false);
            }
        });
        activityUserProfileBinding.editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityUserProfileBinding.filledTextFieldEmail.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityUserProfileBinding.filledTextFieldEmail.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityUserProfileBinding.filledTextFieldEmail.setErrorEnabled(false);
            }
        });
        activityUserProfileBinding.phoneInputLayoutEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityUserProfileBinding.phoneInputLayoutEmail.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityUserProfileBinding.phoneInputLayoutEmail.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityUserProfileBinding.phoneInputLayoutEmail.setErrorEnabled(false);
            }
        });
        activityUserProfileBinding.addressInputLayoutEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityUserProfileBinding.addressInputLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityUserProfileBinding.addressInputLayout.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityUserProfileBinding.addressInputLayout.setErrorEnabled(false);
            }
        });
        activityUserProfileBinding.passwordTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityUserProfileBinding.passwordTextInputLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityUserProfileBinding.passwordTextInputLayout.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityUserProfileBinding.passwordTextInputLayout.setErrorEnabled(false);
            }
        });
        activityUserProfileBinding.conPassTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityUserProfileBinding.conPassTextInputLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityUserProfileBinding.conPassTextInputLayout.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityUserProfileBinding.conPassTextInputLayout.setErrorEnabled(false);
            }
        });

        activityUserProfileBinding.flSubmit.setOnClickListener(v -> {
            checkMethod();

        });
    }

    private boolean checkMethod() {
        email_String = activityUserProfileBinding.editTextEmail.getText().toString().trim();
        name_String = activityUserProfileBinding.editTextUsername.getText().toString().trim();
        phone = activityUserProfileBinding.phoneInputLayoutEditText.getText().toString().trim();
        address = activityUserProfileBinding.addressInputLayoutEditText.getText().toString().trim();

        if (activityUserProfileBinding.editTextEmail.getText().toString().trim().isEmpty()) {
            activityUserProfileBinding.filledTextFieldEmail.setErrorEnabled(true);
            activityUserProfileBinding.filledTextFieldEmail.setError(mContext.getResources().getString(R.string.Invalid_email_formatt));
            activityUserProfileBinding.filledTextFieldEmail.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            return false;
        } else if (!Global.isValidEmailAddress(activityUserProfileBinding.editTextEmail.getText().toString().trim())) {
            activityUserProfileBinding.filledTextFieldEmail.setErrorEnabled(true);
            activityUserProfileBinding.filledTextFieldEmail.setError(mContext.getResources().getString(R.string.Invalid_email_formatt));
            activityUserProfileBinding.filledTextFieldEmail.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            return false;
        } else if (!Global.validateLength(name_String, 3)) {

            activityUserProfileBinding.filledTextFieldUsername.setErrorEnabled(true);
            activityUserProfileBinding.filledTextFieldUsername.setError(getResources().getString(R.string.Enter_Name_at_least_3_character));
            activityUserProfileBinding.filledTextFieldUsername.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            return false;
        } else if (activityUserProfileBinding.phoneInputLayoutEditText.getText().toString().equalsIgnoreCase("0000000000") || activityUserProfileBinding.phoneInputLayoutEditText.getText().toString().startsWith("1111111111") ||
                activityUserProfileBinding.phoneInputLayoutEditText.getText().toString().equalsIgnoreCase("2222222222")
                || activityUserProfileBinding.phoneInputLayoutEditText.getText().toString().equalsIgnoreCase("3333333333") ||
                activityUserProfileBinding.phoneInputLayoutEditText.getText().toString().equalsIgnoreCase("4444444444") ||
                activityUserProfileBinding.phoneInputLayoutEditText.getText().toString().equalsIgnoreCase("5555555555") ||
                activityUserProfileBinding.phoneInputLayoutEditText.getText().toString().equalsIgnoreCase("6666666666")
                || activityUserProfileBinding.phoneInputLayoutEditText.getText().toString().equalsIgnoreCase("7777777777") ||
                activityUserProfileBinding.phoneInputLayoutEditText.getText().toString().equalsIgnoreCase("8888888888") ||
                activityUserProfileBinding.phoneInputLayoutEditText.getText().toString().equalsIgnoreCase("9999999999")) {

            activityUserProfileBinding.phoneInputLayoutEmail.setError(mContext.getResources().getString(R.string.enter_phone_number));
            activityUserProfileBinding.phoneInputLayoutEmail.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));

            return false;
        } else if (!Global.isValidMobile(activityUserProfileBinding.phoneInputLayoutEditText.getText().toString())) {
            activityUserProfileBinding.phoneInputLayoutEmail.setError(mContext.getResources().getString(R.string.enter_phone_number));
            activityUserProfileBinding.phoneInputLayoutEmail.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));

            return false;
        } else if (!Global.validateLength(address, 3)) {
            activityUserProfileBinding.addressInputLayout.setError(mContext.getResources().getString(R.string.Enter_address_at_least_3_character));
            activityUserProfileBinding.addressInputLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            return false;
        } else {
            if (Global.isOnline(mActivity)) {
                editprofile_task();
            } else {
                Toast.makeText(mActivity, R.string.no_internet, Toast.LENGTH_LONG).show();
            }
        }

        return true;
    }


    public void getUsersDetails(final String user_id) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_user_data",
                response -> {
                    Timber.d(response);
                    loaderView.hideLoader();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                            JSONObject object = jsonObject.getJSONObject("user_data");

                            userData = UserData.fromJson(object);
                            SessionManager.save_user_id(prefs, userData.getUser_id());
                            SessionManager.save_name(prefs, userData.getUsername());
                            SessionManager.save_fname(prefs, userData.getFirst_name());
                            SessionManager.save_lname(prefs, userData.getLast_name());
                            SessionManager.save_emailid(prefs, userData.getEmail());
                            SessionManager.save_sex(prefs, userData.getGender());
                            SessionManager.save_address(prefs, userData.getAddress());
                            SessionManager.save_image(prefs, userData.getAvatar());
                            SessionManager.save_cover(prefs, userData.getCover());
                            SessionManager.save_dob(prefs, userData.getBirthday());
                            SessionManager.save_mobile(prefs, userData.getPhone_number());

                            SessionManager.savepinCode(prefs, userData.getPincode());
                            SessionManager.saveCountry(prefs, userData.getCountry_name());
                            SessionManager.saveStates(prefs, userData.getState_name());
                            SessionManager.saveCity(prefs, userData.getCity_name());
                            SessionManager.saveCityId(prefs, userData.getCity_id());
                            SessionManager.saveStateId(prefs, userData.getState_id());
                            SessionManager.save_cricheroes_profile_link(prefs, userData.getCricheroes_profile_link());

                            setData();


                        } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                            Global.msgDialog(mActivity, jsonObject.optJSONObject("errors").optString("error_text"));
                        } else {
                            Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    loaderView.hideLoader();
                    Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
//                Global.msgDialog(EditProfile.this, "Internet connection is slow");
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", user_id);
                param.put("user_profile_id", user_id);
//                param.put("s", "1");
                param.put("s", SessionManager.get_session_id(prefs));
                System.out.println("data   :" + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    private void setData() {
        activityUserProfileBinding.editTextUsername.setText(Global.capitizeString(SessionManager.get_name(prefs)));
        activityUserProfileBinding.editTextEmail.setText(SessionManager.get_emailid(prefs));
        activityUserProfileBinding.addressInputLayoutEditText.setText(SessionManager.get_address(prefs));
        // edttxt_add_profile_score_link.setText(SessionManager.get_cricheroes_profile_link(prefs));

//        if (SessionManager.get_dob(prefs).equalsIgnoreCase("0000-00-00")) {
//
//        } else {
//            edttxt_birthday.setText(SessionManager.get_dob(prefs));
//        }


        activityUserProfileBinding.phoneInputLayoutEditText.setText(SessionManager.get_mobile(prefs));

//        if (userData.getPincode().equalsIgnoreCase("null")) {
//            etPincode.setText("");
//        } else {
//            etPincode.setText(SessionManager.getpinCode(prefs));
//        }
//
//        spn_gender.setSelection(Global.getIndex(spn_gender, SessionManager.get_sex(prefs)));

        Glide.with(mActivity).load(SessionManager.get_image(prefs)).into(activityUserProfileBinding.profilePic);
        //Glide.with(mActivity).load(SessionManager.get_cover(prefs)).into(cover_img);
    }

    private void editprofile_task() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "update_user_data",
                response -> {
                    Timber.d(response);
                    loaderView.hideLoader();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                            Global.msgDialogEdit(mActivity, "Profile Saved Successfully");
                            getUsersDetails(SessionManager.get_user_id(prefs));

                        } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                            Global.msgDialogEdit(mActivity, jsonObject.optString("errors"));
                        } else {
                            Global.msgDialogEdit(mActivity, getResources().getString(R.string.error_server));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    loaderView.hideLoader();
                    Global.msgDialogEdit(mActivity, getResources().getString(R.string.error_server));
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("type", "profile_update");
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("username", name_String);
                param.put("gender", "gender_String");
                param.put("first_name", "edttxt_fname.getText().toString()");
                param.put("last_name", "edttxt_lname.getText().toString()");
                param.put("cricheroes_profile_link", "edttxt_add_profile_score_link.getText().toString().trim()");
                //param.put("mid_name", "");
                param.put("country_id", "countryID");
                param.put("state_id", "stateID");
                param.put("city_id", "cityID");
                param.put("address", address);
                //param.put("address2", "");
                param.put("pincode", "etPincode.getText().toString().trim()");
                //param.put("phone_code", "");
                param.put("phone_number", phone);
                param.put("birthday", "edttxt_birthday.getText().toString()");
                //System.out.println("data   :" + param);
                Log.e("param", param.toString());

                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    public void updateImageTask(String path) {
        loaderView.showLoader();
        try {
            MultipartEntity entity = new MultipartEntity();
            entity.addPart("user_id", new StringBody(SessionManager.get_user_id(prefs)));
            entity.addPart("s", new StringBody(SessionManager.get_session_id(prefs)));
            if (!(path.equals("") || path == null)) {
                File file = new File(path);
                FileBody fileBody = new FileBody(file);
                entity.addPart("image_type", new StringBody("avatar"));
                entity.addPart("image", fileBody);


//                if (img_type.equals("profile")) {
//                    entity.addPart("image_type", new StringBody("avatar"));
//                    entity.addPart("image", fileBody);
//                } else {
//                    entity.addPart("image_type", new StringBody("cover"));
//                    entity.addPart("image", fileBody);
//                }
            }
            MultipartRequest req = new MultipartRequest(Global.URL + "update_profile_picture", response -> {
                try {
                    loaderView.hideLoader();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                        getUsersDetails(SessionManager.get_user_id(prefs));

                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(mActivity, jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, error -> {
                loaderView.hideLoader();
                error.printStackTrace();
            }, entity);

            int socketTimeout = 30000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            req.setRetryPolicy(policy);
            queue.add(req);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}