package com.pb.criconetnewdesign.CommonUI;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;

import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.databinding.ActivityAmbassadorFormBinding;
import com.pb.criconetnewdesign.databinding.ToolbarInnerpageBinding;
import com.pb.criconetnewdesign.util.Global;

import java.util.Objects;

public class AmbassadorFormActivity extends AppCompatActivity {
    ActivityAmbassadorFormBinding activityAmbassadorFormBinding;
    Context mContext;
    Activity mActivity;
    String organized_status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAmbassadorFormBinding = ActivityAmbassadorFormBinding.inflate(getLayoutInflater());
        setContentView(activityAmbassadorFormBinding.getRoot());
        mContext = this;
        mActivity = this;

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityAmbassadorFormBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(R.string.join_as_a_Ambassador);
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> {
            finish();
        });

        inItView();
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private void inItView() {

        activityAmbassadorFormBinding.rbYes.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b == true) {
                organized_status = "yes";
                activityAmbassadorFormBinding.liOrganized.setVisibility(View.VISIBLE);
                if (activityAmbassadorFormBinding.rbNo.isChecked()) {
                    activityAmbassadorFormBinding.rbNo.setChecked(false);
                }

            } else {

            }
        });

        activityAmbassadorFormBinding.rbNo.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b == true) {
                organized_status = "no";
                activityAmbassadorFormBinding.liOrganized.setVisibility(View.GONE);
                if (activityAmbassadorFormBinding.rbYes.isChecked()) {
                    activityAmbassadorFormBinding.rbYes.setChecked(false);
                }
            } else {

            }
        });

        String[] feelings = getResources().getStringArray(R.array.hours);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.dropdown_item, feelings);
        activityAmbassadorFormBinding.autoCompleteTextViewSelectHours.setAdapter(arrayAdapter);
        activityAmbassadorFormBinding.autoCompleteTextViewSelectHours.setDropDownBackgroundDrawable(getResources().getDrawable(R.drawable.spinner_bg));

        activityAmbassadorFormBinding.editTextUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAmbassadorFormBinding.filledTextFieldUsername.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAmbassadorFormBinding.filledTextFieldUsername.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAmbassadorFormBinding.filledTextFieldUsername.setErrorEnabled(false);
            }
        });

        activityAmbassadorFormBinding.emailInputLayoutEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAmbassadorFormBinding.textInputLayoutEmail.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAmbassadorFormBinding.textInputLayoutEmail.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAmbassadorFormBinding.textInputLayoutEmail.setErrorEnabled(false);
            }
        });

        activityAmbassadorFormBinding.phoneInputLayoutEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAmbassadorFormBinding.phoneInputLayoutEmail.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAmbassadorFormBinding.phoneInputLayoutEmail.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAmbassadorFormBinding.phoneInputLayoutEmail.setErrorEnabled(false);
            }
        });

        activityAmbassadorFormBinding.schoolInputLayoutEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAmbassadorFormBinding.inputLayoutSchool.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAmbassadorFormBinding.inputLayoutSchool.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAmbassadorFormBinding.inputLayoutSchool.setErrorEnabled(false);
            }
        });

        activityAmbassadorFormBinding.qualificatioInputLayoutEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAmbassadorFormBinding.inputLayoutQualificatio.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAmbassadorFormBinding.inputLayoutQualificatio.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAmbassadorFormBinding.inputLayoutQualificatio.setErrorEnabled(false);
            }
        });

        activityAmbassadorFormBinding.becomeTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAmbassadorFormBinding.becomeTextInputLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAmbassadorFormBinding.becomeTextInputLayout.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAmbassadorFormBinding.becomeTextInputLayout.setErrorEnabled(false);
            }
        });

        activityAmbassadorFormBinding.knowAboutTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAmbassadorFormBinding.knowAboutTextInputLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAmbassadorFormBinding.knowAboutTextInputLayout.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAmbassadorFormBinding.knowAboutTextInputLayout.setErrorEnabled(false);
            }
        });

        activityAmbassadorFormBinding.eventBeforeTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAmbassadorFormBinding.eventBeforeTextInputLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAmbassadorFormBinding.eventBeforeTextInputLayout.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAmbassadorFormBinding.eventBeforeTextInputLayout.setErrorEnabled(false);
            }
        });

        activityAmbassadorFormBinding.passionateTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAmbassadorFormBinding.passionateTextInputLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAmbassadorFormBinding.passionateTextInputLayout.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAmbassadorFormBinding.passionateTextInputLayout.setErrorEnabled(false);
            }
        });

        activityAmbassadorFormBinding.yesEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAmbassadorFormBinding.yesInput.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAmbassadorFormBinding.yesInput.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAmbassadorFormBinding.yesInput.setErrorEnabled(false);
            }
        });


        activityAmbassadorFormBinding.flSubmit.setOnClickListener(v -> {

            startActivity(new Intent(mContext,ReferRewardsActivity.class));

//            if (Objects.requireNonNull(activityAmbassadorFormBinding.editTextUsername.getText()).toString().isEmpty()) {
//                activityAmbassadorFormBinding.filledTextFieldUsername.setErrorEnabled(true);
//                activityAmbassadorFormBinding.filledTextFieldUsername.setError("Username can't be empty!");
//                activityAmbassadorFormBinding.filledTextFieldUsername.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.purple_700)));
//            } else if (!Global.isValidEmailAddress(Objects.requireNonNull(activityAmbassadorFormBinding.emailInputLayoutEmail.getText()).toString())) {
//                activityAmbassadorFormBinding.textInputLayoutEmail.setErrorEnabled(true);
//                activityAmbassadorFormBinding.textInputLayoutEmail.setError("Enter valid email!");
//                activityAmbassadorFormBinding.textInputLayoutEmail.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.purple_700)));
//            } else if (Objects.requireNonNull(activityAmbassadorFormBinding.phoneInputLayoutEditText.getText()).toString().isEmpty()) {
//                activityAmbassadorFormBinding.phoneInputLayoutEmail.setErrorEnabled(true);
//                activityAmbassadorFormBinding.phoneInputLayoutEmail.setError("Phone number can't be empty!");
//                activityAmbassadorFormBinding.phoneInputLayoutEmail.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.purple_700)));
//            } else if (Objects.requireNonNull(activityAmbassadorFormBinding.schoolInputLayoutEditText.getText()).toString().isEmpty()) {
//                activityAmbassadorFormBinding.inputLayoutSchool.setErrorEnabled(true);
//                activityAmbassadorFormBinding.inputLayoutSchool.setError(mContext.getResources().getString(R.string.min_character_length));
//                activityAmbassadorFormBinding.inputLayoutSchool.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.purple_700)));
//            } else if (Objects.requireNonNull(!activityAmbassadorFormBinding.schoolInputLayoutEditText.getText().toString().matches("[a-zA-Z0-9.? ]*"))) {
//                activityAmbassadorFormBinding.inputLayoutSchool.setErrorEnabled(true);
//                activityAmbassadorFormBinding.inputLayoutSchool.setError(mContext.getResources().getString(R.string.min_character_length));
//                activityAmbassadorFormBinding.inputLayoutSchool.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.purple_700)));
//            } else if (Objects.requireNonNull(activityAmbassadorFormBinding.qualificatioInputLayoutEditText.getText()).toString().isEmpty()) {
//                activityAmbassadorFormBinding.inputLayoutQualificatio.setErrorEnabled(true);
//                activityAmbassadorFormBinding.inputLayoutQualificatio.setError(mContext.getResources().getString(R.string.min_character_length_qualification));
//                activityAmbassadorFormBinding.inputLayoutQualificatio.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.purple_700)));
//            } else if (Objects.requireNonNull(activityAmbassadorFormBinding.becomeTextInputEditText.getText()).toString().isEmpty()) {
//                activityAmbassadorFormBinding.becomeTextInputLayout.setErrorEnabled(true);
//                activityAmbassadorFormBinding.becomeTextInputLayout.setError(mContext.getResources().getString(R.string.region_to_become_criconet_ambassador));
//                activityAmbassadorFormBinding.becomeTextInputLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.purple_700)));
//            } else if (activityAmbassadorFormBinding.rbYes.isChecked() && Objects.requireNonNull(activityAmbassadorFormBinding.yesEditText.getText()).toString().isEmpty()) {
//                activityAmbassadorFormBinding.yesInput.setErrorEnabled(true);
//                activityAmbassadorFormBinding.yesInput.setError(mContext.getResources().getString(R.string.Please_enter_description_yes));
//                activityAmbassadorFormBinding.yesInput.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.purple_700)));
//
//            } else if (activityAmbassadorFormBinding.autoCompleteTextViewSelectHours.getText().toString().isEmpty()) {
//                activityAmbassadorFormBinding.typeTextInputLayoutSelectHours.setErrorEnabled(true);
//                activityAmbassadorFormBinding.typeTextInputLayoutSelectHours.setError(mContext.getResources().getString(R.string.select_hours));
//                activityAmbassadorFormBinding.typeTextInputLayoutSelectHours.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.purple_700)));
//            } else {
//
//            }


        });

    }
}