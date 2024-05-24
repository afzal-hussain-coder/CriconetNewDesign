package com.pb.criconetnewdesign.Activity.User;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.pb.criconetnewdesign.CustomeCamera.CustomeCameraActivity;
import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.databinding.ActivityUserProfileBinding;
import com.pb.criconetnewdesign.databinding.ToolbarInnerpageBinding;

public class UserProfileActivity extends AppCompatActivity {
    ActivityUserProfileBinding activityUserProfileBinding;
    Context mContext;
    Activity mActivity;
    public static Uri image_uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserProfileBinding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(activityUserProfileBinding.getRoot());
        mContext = this;
        mActivity = this;

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityUserProfileBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.my_profile));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());


        inItView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != image_uri) {
            activityUserProfileBinding.profilePic.setImageURI(image_uri);
        }
    }

    private void inItView() {
        activityUserProfileBinding.tvChangePhoto.setOnClickListener(v -> {
            startActivity(new Intent(mContext, CustomeCameraActivity.class)
                    .putExtra("FROM","UserProfileActivity"));
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

        });
    }
}