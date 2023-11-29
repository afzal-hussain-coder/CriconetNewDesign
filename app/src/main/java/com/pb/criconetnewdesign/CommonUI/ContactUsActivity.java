package com.pb.criconetnewdesign.CommonUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.databinding.ActivityContactUsBinding;
import com.pb.criconetnewdesign.databinding.ToolbarInnerpageBinding;

import java.util.Objects;

public class ContactUsActivity extends AppCompatActivity {

    ActivityContactUsBinding activityContactUsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityContactUsBinding = ActivityContactUsBinding.inflate(getLayoutInflater());
        setContentView(activityContactUsBinding.getRoot());

        initView();
    }

    private void initView() {
        ToolbarInnerpageBinding toolbarInnerpageBinding = activityContactUsBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(getResources().getString(R.string.contact_us));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        String[] feelings = getResources().getStringArray(R.array.typeOfBooking);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.dropdown_item, feelings);
        activityContactUsBinding.autoCompleteTextView.setAdapter(arrayAdapter);
        activityContactUsBinding.autoCompleteTextView.setDropDownBackgroundDrawable(getResources().getDrawable(R.drawable.spinner_bg));

        activityContactUsBinding.editTextUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityContactUsBinding.filledTextFieldUsername.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityContactUsBinding.filledTextFieldUsername.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityContactUsBinding.filledTextFieldUsername.setErrorEnabled(false);
            }
        });
        activityContactUsBinding.emailInputLayoutEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityContactUsBinding.textInputLayoutEmail.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityContactUsBinding.textInputLayoutEmail.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityContactUsBinding.textInputLayoutEmail.setErrorEnabled(false);
            }
        });
        activityContactUsBinding.phoneInputLayoutEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityContactUsBinding.phoneInputLayoutEmail.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityContactUsBinding.phoneInputLayoutEmail.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityContactUsBinding.phoneInputLayoutEmail.setErrorEnabled(false);
            }
        });
        activityContactUsBinding.subjectTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityContactUsBinding.subjectTextInputLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityContactUsBinding.subjectTextInputLayout.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityContactUsBinding.subjectTextInputLayout.setErrorEnabled(false);
            }
        });
        activityContactUsBinding.messageTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityContactUsBinding.messageTextInputLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityContactUsBinding.messageTextInputLayout.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityContactUsBinding.messageTextInputLayout.setErrorEnabled(false);
            }
        });

        activityContactUsBinding.flSubmit.setOnClickListener(v -> {
            if (Objects.requireNonNull(activityContactUsBinding.editTextUsername.getText()).toString().isEmpty()) {
                activityContactUsBinding.filledTextFieldUsername.setErrorEnabled(true);
                activityContactUsBinding.filledTextFieldUsername.setError("Username can't be empty!");
                activityContactUsBinding.filledTextFieldUsername.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            } else if (Objects.requireNonNull(activityContactUsBinding.emailInputLayoutEmail.getText()).toString().isEmpty()) {
                activityContactUsBinding.textInputLayoutEmail.setErrorEnabled(true);
                activityContactUsBinding.textInputLayoutEmail.setError("Email can't be empty!");
                activityContactUsBinding.textInputLayoutEmail.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            } else if (Objects.requireNonNull(activityContactUsBinding.phoneInputLayoutEditText.getText()).toString().isEmpty()) {
                activityContactUsBinding.phoneInputLayoutEmail.setErrorEnabled(true);
                activityContactUsBinding.phoneInputLayoutEmail.setError("Phone number can't be empty!");
                activityContactUsBinding.phoneInputLayoutEmail.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            }else if(activityContactUsBinding.autoCompleteTextView.getText().toString().isEmpty()){
                activityContactUsBinding.typeTextInputLayout.setErrorEnabled(true);
                activityContactUsBinding.typeTextInputLayout.setError("Type of booking can't be empty!");
                activityContactUsBinding.typeTextInputLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            }
            else if (Objects.requireNonNull(activityContactUsBinding.subjectTextInputEditText.getText()).toString().isEmpty()) {
                activityContactUsBinding.subjectTextInputLayout.setErrorEnabled(true);
                activityContactUsBinding.subjectTextInputLayout.setError("Subject can't be empty!");
                activityContactUsBinding.subjectTextInputLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            }else if (Objects.requireNonNull(activityContactUsBinding.messageTextInputEditText.getText()).toString().isEmpty()) {
                activityContactUsBinding.messageTextInputLayout.setErrorEnabled(true);
                activityContactUsBinding.messageTextInputLayout.setError("Message can't be empty!");
                activityContactUsBinding.messageTextInputLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            }
        });

    }
}