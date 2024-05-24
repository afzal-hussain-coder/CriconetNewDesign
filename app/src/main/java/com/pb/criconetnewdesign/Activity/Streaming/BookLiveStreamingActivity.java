package com.pb.criconetnewdesign.Activity.Streaming;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;

import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.databinding.ActivityBookLiveStreamingBinding;
import com.pb.criconetnewdesign.databinding.ToolbarInnerpageBinding;
import com.pb.criconetnewdesign.util.BookStreamDropDown;
import com.pb.criconetnewdesign.util.BookingHistoryDropDown;
import com.pb.criconetnewdesign.util.DataModel;
import com.pb.criconetnewdesign.util.Global;

import java.util.ArrayList;
import java.util.Objects;

public class BookLiveStreamingActivity extends AppCompatActivity {

    ActivityBookLiveStreamingBinding activityBookLiveStreamingBinding;
    Context mContext;
    Activity mActivity;

    private ArrayList<DataModel> option_select_type = new ArrayList<>();
    private ArrayList<DataModel> option_state = new ArrayList<>();
    private ArrayList<DataModel> option_city = new ArrayList<>();
    private String type = "";
    private String state = "";
    private String city = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBookLiveStreamingBinding = ActivityBookLiveStreamingBinding.inflate(getLayoutInflater());
        setContentView(activityBookLiveStreamingBinding.getRoot());
        mContext = this;
        mActivity = this;

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityBookLiveStreamingBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.e_streaming));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());


        inItView();
    }

    private void inItView() {

        option_select_type.add(new DataModel("Name of the Club"));
        option_select_type.add(new DataModel("Name of Tournament"));
        option_select_type.add(new DataModel("Name of Organization"));
        activityBookLiveStreamingBinding.dropSelectType.setOptionList(option_select_type);
        activityBookLiveStreamingBinding.dropSelectType.setClickListener(new BookStreamDropDown.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name) {
                type = name;
                activityBookLiveStreamingBinding.tvTypeError.setVisibility(View.GONE);

            }


            @Override
            public void onDismiss() {
            }
        });

        option_state.add(new DataModel("Bihar"));
        option_state.add(new DataModel("UP"));
        option_state.add(new DataModel("MP"));
        activityBookLiveStreamingBinding.dropSelectStat.setOptionList(option_state);
        activityBookLiveStreamingBinding.dropSelectStat.setClickListener(new BookStreamDropDown.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name) {
                state = name;
                activityBookLiveStreamingBinding.tvStateError.setVisibility(View.GONE);
            }


            @Override
            public void onDismiss() {
            }
        });

        option_city.add(new DataModel("Patna"));
        option_city.add(new DataModel("Samastipur"));
        option_city.add(new DataModel("Gaya"));
        activityBookLiveStreamingBinding.dropSelectCity.setOptionList(option_city);
        activityBookLiveStreamingBinding.dropSelectCity.setClickListener(new BookStreamDropDown.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name) {
                city = name;
                activityBookLiveStreamingBinding.tvCityError.setVisibility(View.GONE);
            }


            @Override
            public void onDismiss() {
            }
        });

        activityBookLiveStreamingBinding.editTextUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityBookLiveStreamingBinding.filledTextFieldUsername.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityBookLiveStreamingBinding.filledTextFieldUsername.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityBookLiveStreamingBinding.filledTextFieldUsername.setErrorEnabled(false);
            }
        });
        activityBookLiveStreamingBinding.phoneInputLayoutEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityBookLiveStreamingBinding.phoneInputLayoutEmail.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityBookLiveStreamingBinding.phoneInputLayoutEmail.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityBookLiveStreamingBinding.phoneInputLayoutEmail.setErrorEnabled(false);
            }
        });
        activityBookLiveStreamingBinding.emailInputLayoutEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityBookLiveStreamingBinding.textInputLayoutEmail.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityBookLiveStreamingBinding.textInputLayoutEmail.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityBookLiveStreamingBinding.textInputLayoutEmail.setErrorEnabled(false);
            }
        });
        activityBookLiveStreamingBinding.groundLocationTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityBookLiveStreamingBinding.groundLocationTextInputLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityBookLiveStreamingBinding.groundLocationTextInputLayout.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityBookLiveStreamingBinding.groundLocationTextInputLayout.setErrorEnabled(false);
            }
        });
        activityBookLiveStreamingBinding.messageTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityBookLiveStreamingBinding.messageTextInputLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityBookLiveStreamingBinding.messageTextInputLayout.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityBookLiveStreamingBinding.messageTextInputLayout.setErrorEnabled(false);
            }
        });

        activityBookLiveStreamingBinding.flSubmit.setOnClickListener(v -> {
            if (Objects.requireNonNull(activityBookLiveStreamingBinding.editTextUsername.getText()).toString().isEmpty()) {
                activityBookLiveStreamingBinding.filledTextFieldUsername.setErrorEnabled(true);
                activityBookLiveStreamingBinding.filledTextFieldUsername.setError("Name can't be empty!");
                activityBookLiveStreamingBinding.filledTextFieldUsername.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            } else if (type.isEmpty()) {
                activityBookLiveStreamingBinding.tvTypeError.setVisibility(View.VISIBLE);
            }  else if (Objects.requireNonNull(activityBookLiveStreamingBinding.phoneInputLayoutEditText.getText()).toString().isEmpty()) {
                activityBookLiveStreamingBinding.phoneInputLayoutEmail.setErrorEnabled(true);
                activityBookLiveStreamingBinding.phoneInputLayoutEmail.setError("Phone number can't be empty!");
                activityBookLiveStreamingBinding.phoneInputLayoutEmail.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            }
            else if (Objects.requireNonNull(!Global.isValidEmailAddress(activityBookLiveStreamingBinding.emailInputLayoutEmail.getText().toString()))) {
                activityBookLiveStreamingBinding.textInputLayoutEmail.setErrorEnabled(true);
                activityBookLiveStreamingBinding.textInputLayoutEmail.setError("Enter valid email!");
                activityBookLiveStreamingBinding.textInputLayoutEmail.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            }else if(state.isEmpty()){
                activityBookLiveStreamingBinding.tvStateError.setVisibility(View.VISIBLE);
            } else if(city.isEmpty()){
                activityBookLiveStreamingBinding.tvCityError.setVisibility(View.VISIBLE);
            }
            else if (Objects.requireNonNull(activityBookLiveStreamingBinding.groundLocationTextInputEditText.getText()).toString().isEmpty()) {
                activityBookLiveStreamingBinding.groundLocationTextInputLayout.setErrorEnabled(true);
                activityBookLiveStreamingBinding.groundLocationTextInputLayout.setError("Ground location can't be empty!");
                activityBookLiveStreamingBinding.groundLocationTextInputLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            } else if (Objects.requireNonNull(activityBookLiveStreamingBinding.messageTextInputEditText.getText()).toString().isEmpty()) {
                activityBookLiveStreamingBinding.messageTextInputLayout.setErrorEnabled(true);
                activityBookLiveStreamingBinding.messageTextInputLayout.setError("Message can't be empty!");
                activityBookLiveStreamingBinding.messageTextInputLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            }
        });
    }
}