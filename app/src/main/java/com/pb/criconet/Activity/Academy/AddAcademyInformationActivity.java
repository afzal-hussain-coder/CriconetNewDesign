package com.pb.criconet.Activity.Academy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.pb.criconet.R;
import com.pb.criconet.adapter.AcademyAdapter.AddAcademySpecilitiesButtonAdapter;
import com.pb.criconet.databinding.ActivityAddAcademyInformationBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.model.AcademyModel.AcademyGallery;
import com.pb.criconet.model.AcademyModel.CoachLanguage;
import com.pb.criconet.model.DataModel;
import com.pb.criconet.model.Datum;
import com.pb.criconet.model.Language;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.SessionManager;
import com.pb.criconet.util.Toaster;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import timber.log.Timber;

public class AddAcademyInformationActivity extends AppCompatActivity implements AddAcademySpecilitiesButtonAdapter.ItemListener {
    ActivityAddAcademyInformationBinding activityAddAcademyInformationBinding;
    Context mContext;
    Activity mActivity;
    private RequestQueue queue;
    CustomLoaderView loaderView;
    private SharedPreferences prefs;
    ToolbarInnerpageBinding toolbarInnerpageBinding;
    private DataModel modelArrayList;
    private StringBuilder categoryId;
    private Language languageModelArrayList;
    ArrayList<Language.Datum> language = null;
    Dialog dialog;
    ArrayList<String> languageArray_new = new ArrayList<>();
    private StringBuilder langStringBuilder;
    String training_type = "";
    String name = "", email = "", address = "", fee = "", about = "", achievement = "", short_desc = "", contact_person_phone = "",
            contact_person = "",  facebook = "", twitter = "", youtube = "", instagram = "", linkedIn = "", academy_id = "";
    String FROM = "";
    String banner_image = "", logo = "", video = "";
    ArrayList<AcademyGallery> galleryImageList;
    ArrayList<String> galleryVideoList;
    private ArrayList<Datum> editList_speclization = new ArrayList<>();
    ArrayList<String> coachLanguages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddAcademyInformationBinding = ActivityAddAcademyInformationBinding.inflate(getLayoutInflater());
        setContentView(activityAddAcademyInformationBinding.getRoot());

        mContext = this;
        mActivity = this;

        toolbarInnerpageBinding = activityAddAcademyInformationBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.add_academy));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        loaderView = CustomLoaderView.initialize(mContext);
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        queue = Volley.newRequestQueue(mContext);

        initView();

        if (getIntent() != null) {
            FROM = getIntent().getStringExtra("FROM");

        }

        if (Global.isOnline(mActivity)) {
            getSpecialities();
        } else {
            Global.showDialog(mActivity);
        }

        if (Global.isOnline(mActivity)) {
            getLanguage();
        } else {
            Global.showDialog(mActivity);
        }

        if (FROM.equalsIgnoreCase("2")) {

            if (Global.isOnline(mActivity)) {
                getUsersDetails();
            } else {
                Global.showDialog(mActivity);
            }
        }




    }

    private void initView() {
        activityAddAcademyInformationBinding.rcvSpecialities.setHasFixedSize(true);
        activityAddAcademyInformationBinding.rcvSpecialities.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));

        activityAddAcademyInformationBinding.editTextAcademyName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAddAcademyInformationBinding.filledTextFieldAcademyName.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAddAcademyInformationBinding.filledTextFieldAcademyName.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAddAcademyInformationBinding.filledTextFieldAcademyName.setErrorEnabled(false);
            }
        });

        activityAddAcademyInformationBinding.editTextAcademyEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAddAcademyInformationBinding.filledTextFieldAcademyEmail.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAddAcademyInformationBinding.filledTextFieldAcademyEmail.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAddAcademyInformationBinding.filledTextFieldAcademyEmail.setErrorEnabled(false);
            }
        });

        activityAddAcademyInformationBinding.editLocationOfTheAcademy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAddAcademyInformationBinding.filledTextFieldLocationOfTheAcademy.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAddAcademyInformationBinding.filledTextFieldLocationOfTheAcademy.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAddAcademyInformationBinding.filledTextFieldLocationOfTheAcademy.setErrorEnabled(false);
            }
        });

        activityAddAcademyInformationBinding.editAcademyFees.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAddAcademyInformationBinding.filledTextFieldAcademyFees.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAddAcademyInformationBinding.filledTextFieldAcademyFees.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAddAcademyInformationBinding.filledTextFieldAcademyFees.setErrorEnabled(false);
            }
        });

        activityAddAcademyInformationBinding.editDetailsAcademy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAddAcademyInformationBinding.filledTextFieldDetailsAcademy.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAddAcademyInformationBinding.filledTextFieldDetailsAcademy.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAddAcademyInformationBinding.filledTextFieldDetailsAcademy.setErrorEnabled(false);
            }
        });

        activityAddAcademyInformationBinding.editAchivementAcademy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAddAcademyInformationBinding.filledTextFieldAchivementAcademy.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAddAcademyInformationBinding.filledTextFieldAchivementAcademy.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAddAcademyInformationBinding.filledTextFieldAchivementAcademy.setErrorEnabled(false);
            }
        });

        activityAddAcademyInformationBinding.editContactPersonName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAddAcademyInformationBinding.filledTextFieldContactPersonName.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAddAcademyInformationBinding.filledTextFieldContactPersonName.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAddAcademyInformationBinding.filledTextFieldContactPersonName.setErrorEnabled(false);
            }
        });

        activityAddAcademyInformationBinding.editContactPersonPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAddAcademyInformationBinding.filledTextFieldContactPersonPhoneNumber.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAddAcademyInformationBinding.filledTextFieldContactPersonPhoneNumber.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAddAcademyInformationBinding.filledTextFieldContactPersonPhoneNumber.setErrorEnabled(false);
            }
        });

        activityAddAcademyInformationBinding.editFacebook.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAddAcademyInformationBinding.filledTextFieldFacebook.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAddAcademyInformationBinding.filledTextFieldFacebook.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAddAcademyInformationBinding.filledTextFieldFacebook.setErrorEnabled(false);
            }
        });

        activityAddAcademyInformationBinding.editTwitter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAddAcademyInformationBinding.filledTextFieldTwitter.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAddAcademyInformationBinding.filledTextFieldTwitter.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAddAcademyInformationBinding.filledTextFieldTwitter.setErrorEnabled(false);
            }
        });

        activityAddAcademyInformationBinding.editLinkend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAddAcademyInformationBinding.filledTextFieldLinkend.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAddAcademyInformationBinding.filledTextFieldLinkend.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAddAcademyInformationBinding.filledTextFieldLinkend.setErrorEnabled(false);
            }
        });

        activityAddAcademyInformationBinding.editInstagram.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAddAcademyInformationBinding.filledTextFieldInstagram.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAddAcademyInformationBinding.filledTextFieldInstagram.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAddAcademyInformationBinding.filledTextFieldInstagram.setErrorEnabled(false);
            }
        });

        activityAddAcademyInformationBinding.editYoutube.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAddAcademyInformationBinding.filledTextFieldYoutube.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAddAcademyInformationBinding.filledTextFieldYoutube.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAddAcademyInformationBinding.filledTextFieldYoutube.setErrorEnabled(false);
            }
        });

        activityAddAcademyInformationBinding.cbPersonal.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                activityAddAcademyInformationBinding.cbPersonal.setChecked(true);
                activityAddAcademyInformationBinding.cbPersonal.setTextColor(mContext.getResources().getColor(R.color.app_green));
                if (activityAddAcademyInformationBinding.cbGroup.isChecked()) {
                    activityAddAcademyInformationBinding.cbGroup.setChecked(false);
                    activityAddAcademyInformationBinding.cbGroup.setTextColor(mContext.getResources().getColor(R.color.bckground_light));
                }
                training_type = "";
                training_type = activityAddAcademyInformationBinding.cbPersonal.getText().toString().trim();
                Toaster.customToast(training_type);
            }


        });

        activityAddAcademyInformationBinding.cbGroup.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                activityAddAcademyInformationBinding.cbGroup.setChecked(true);
                activityAddAcademyInformationBinding.cbGroup.setTextColor(mContext.getResources().getColor(R.color.app_green));
                if (activityAddAcademyInformationBinding.cbPersonal.isChecked()) {
                    activityAddAcademyInformationBinding.cbPersonal.setChecked(false);
                    activityAddAcademyInformationBinding.cbPersonal.setTextColor(mContext.getResources().getColor(R.color.bckground_light));
                }
                training_type = "";
                training_type = activityAddAcademyInformationBinding.cbGroup.getText().toString().trim();

                Toaster.customToast(training_type);

            }

        });


        dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_select_language);
        dialog.setCancelable(false);

        activityAddAcademyInformationBinding.textViewLanguage.setOnClickListener(v -> {
            dialog.show();
        });


        activityAddAcademyInformationBinding.flSubmit.setOnClickListener(view1 -> {
            if (checkValidation()) {
                if (Global.isOnline(mContext)) {
                    addAcademyRequest();
                } else {
                    Global.showDialog(mActivity);
                }
            }
        });


    }

    private void getSpecialities() {
        StringRequest postRequest = new StringRequest(Request.Method.GET, Global.URL + Global.GET_SPECIALITIES_CATEGORY, response -> {
            Gson gson = new Gson();
            modelArrayList = gson.fromJson(response, DataModel.class);
            if (modelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                activityAddAcademyInformationBinding.rcvSpecialities.setAdapter(new AddAcademySpecilitiesButtonAdapter(mContext, new ArrayList<>(), modelArrayList.getData(), AddAcademyInformationActivity.this));
            }

        }, error -> {
            error.printStackTrace();
            Global.msgDialog(mActivity, "Error from server");
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void getLanguage() {
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.GET_LANGUAGE, response -> {
            Timber.d(response);
            Gson gson = new Gson();
            languageModelArrayList = gson.fromJson(response, Language.class);
            if (languageModelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                language = new ArrayList<>();
                language.addAll(languageModelArrayList.getData());
                languageSelectionDialog(language, new ArrayList<>());

            }
        }, error -> {
            error.printStackTrace();
            Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void languageSelectionDialog(ArrayList<Language.Datum> languageArray, ArrayList<String> coachLanguages) {
        TextView btnOk = dialog.findViewById(R.id.btnOk);
        RecyclerView rv_language = dialog.findViewById(R.id.rv_language);
        rv_language.setHasFixedSize(true);
        rv_language.setLayoutManager(new LinearLayoutManager(mActivity));
        //Log.d("ReSize",coachLanguages.size()+"");
        selectCoachLanguageAdapter selectCoachLanguageAdapter = new selectCoachLanguageAdapter(languageArray, coachLanguages, mActivity, languageArrayy -> {
            Log.d("SizeSelected", languageArrayy.size() + "");
            languageArray_new = languageArrayy;
        });
        rv_language.setAdapter(selectCoachLanguageAdapter);
        //TextView btnCancel = dialog.findViewById(R.id.btnCancel);
//        btnCancel.setOnClickListener(view -> {
//            dialog.dismiss();
//        });
        btnOk.setOnClickListener(view -> {

            langStringBuilder = new StringBuilder();
            String prefix = "";

            for (int i = 0; i < languageArray_new.size(); i++) {
                langStringBuilder.append(prefix);
                prefix = ",";
                langStringBuilder.append(languageArray_new.get(i));
            }

            //Log.d("size",langStringBuilder.toString());

            activityAddAcademyInformationBinding.textViewLanguage.setText(langStringBuilder.toString());
            dialog.dismiss();
        });

        //dialog.show();

    }

    public static class selectCoachLanguageAdapter extends RecyclerView.Adapter<selectCoachLanguageAdapter.MyViewHolder> {
        Context context;
        ArrayList<Language.Datum> languageArray;
        ArrayList<String> checkedArray;
        ArrayList<String> coachLanguages;
        selectCoachLanguageAdapter.languageSelectionListner languageSelectionListner;

        public selectCoachLanguageAdapter(ArrayList<Language.Datum> languageArray, ArrayList<String> coachLanguages, Context context, selectCoachLanguageAdapter.languageSelectionListner languageSelectionListner) {
            this.context = context;
            this.languageArray = languageArray;
            this.languageSelectionListner = languageSelectionListner;
            this.coachLanguages = coachLanguages;
            checkedArray = new ArrayList<>();
        }

        @Override
        public selectCoachLanguageAdapter.@NotNull MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // inflate the item Layout
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_coach_spinner_item, parent, false);
            return new selectCoachLanguageAdapter.MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(selectCoachLanguageAdapter.MyViewHolder holder, final int position) {
            Language.Datum coachLanguage = languageArray.get(position);

            holder.textView.setText(coachLanguage.getName_eng());
            holder.checkBox.setChecked(coachLanguage.isSelected());

            Log.d("size", coachLanguages.size() + "");


            for (int j = 0; j < coachLanguages.size(); j++) {
                if (coachLanguages.get(j).equalsIgnoreCase(coachLanguage.getName_eng())) {
                    holder.checkBox.setChecked(true);
                    checkedArray.add(coachLanguages.get(j));
                    languageSelectionListner.itemChcked(checkedArray);
                    break;
                }
            }


            holder.checkBox.setOnClickListener(view -> {
                CheckBox myCheckBox = (CheckBox) view;
                Language.Datum coachLanguage1 = languageArray.get(position);

                if (myCheckBox.isChecked()) {
                    coachLanguage1.setSelected(true);
                    checkedArray.add(coachLanguage1.getName_eng());
                } else if (!myCheckBox.isChecked()) {
                    coachLanguage1.setSelected(false);
                    checkedArray.remove(coachLanguage1.getName_eng());
                }
                languageSelectionListner.itemChcked(checkedArray);
            });


//            holder.liLanguage.setOnClickListener(v -> {
//                // CheckBox myCheckBox = (CheckBox) v;
//                Language.Datum coachLanguage1 = languageArray.get(position);
//
//                if (holder.checkBox.isChecked()) {
//                    holder.checkBox.setChecked(false);
//                    coachLanguage1.setSelected(true);
//                    checkedArray.add(coachLanguage1.getName_eng());
//                } else if (!holder.checkBox.isChecked()) {
//                    holder.checkBox.setChecked(true);
//                    coachLanguage1.setSelected(false);
//                    checkedArray.remove(coachLanguage1.getName_eng());
//                }
//                languageSelectionListner.itemChcked(checkedArray);
//            });


        }

        @Override
        public int getItemCount() {
            return languageArray.size();
        }

        static class MyViewHolder extends RecyclerView.ViewHolder {
            CheckBox checkBox;
            TextView textView;
            LinearLayout liLanguage;

            MyViewHolder(View itemView) {
                super(itemView);
                checkBox = itemView.findViewById(R.id.radio);
                textView = itemView.findViewById(R.id.textView);
                liLanguage = itemView.findViewById(R.id.liLanguage);

            }
        }

        interface languageSelectionListner {
            void itemChcked(ArrayList<String> languageArray);
        }
    }

    @Override
    public void onItemClick(List<DataModel.Datum> items) {
        categoryId = new StringBuilder();
        String prefix = "";
        for (DataModel.Datum item : modelArrayList.getData()) {
            if (item.isCheck()) {
                categoryId.append(prefix);
                prefix = ",";
                categoryId.append(item.getId());
            }
        }
    }


    private boolean checkValidation() {
        name = activityAddAcademyInformationBinding.editTextAcademyName.getText().toString().trim();
        email = activityAddAcademyInformationBinding.editTextAcademyEmail.getText().toString().trim();
        address = activityAddAcademyInformationBinding.editLocationOfTheAcademy.getText().toString().trim();
        fee = activityAddAcademyInformationBinding.editAcademyFees.getText().toString().trim();
        about = activityAddAcademyInformationBinding.editDetailsAcademy.getText().toString().trim();
        achievement = activityAddAcademyInformationBinding.editAchivementAcademy.getText().toString().trim();
        //short_desc = edit_short_des.getText().toString().trim();
        contact_person = activityAddAcademyInformationBinding.editContactPersonName.getText().toString().trim();
        contact_person_phone = activityAddAcademyInformationBinding.editContactPersonPhoneNumber.getText().toString().trim();
        facebook = activityAddAcademyInformationBinding.editFacebook.getText().toString().trim();
        twitter = activityAddAcademyInformationBinding.editTwitter.getText().toString().trim();
        linkedIn = activityAddAcademyInformationBinding.editLinkend.getText().toString().trim();
        instagram = activityAddAcademyInformationBinding.editInstagram.getText().toString().trim();
        youtube = activityAddAcademyInformationBinding.editYoutube.getText().toString().trim();


        if (!Global.validateLengthofAcademy(name)) {
            activityAddAcademyInformationBinding.filledTextFieldAcademyName.setErrorEnabled(true);
            activityAddAcademyInformationBinding.filledTextFieldAcademyName.setError(mContext.getResources().getString(R.string.Please_enter_a_valid_email_address_Invalid_email_address));
            activityAddAcademyInformationBinding.filledTextFieldAcademyName.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            return false;
        } else if (!Global.ValidEmail(email)) {
            activityAddAcademyInformationBinding.filledTextFieldAcademyEmail.setErrorEnabled(true);
            activityAddAcademyInformationBinding.filledTextFieldAcademyEmail.setError(mContext.getResources().getString(R.string.Please_Enter_name_of_the_academy));
            activityAddAcademyInformationBinding.filledTextFieldAcademyEmail.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            return false;
        } else if (address.isEmpty()) {
            activityAddAcademyInformationBinding.filledTextFieldLocationOfTheAcademy.setErrorEnabled(true);
            activityAddAcademyInformationBinding.filledTextFieldLocationOfTheAcademy.setError(mContext.getResources().getString(R.string.Please_enter_address_for_verification));
            activityAddAcademyInformationBinding.filledTextFieldLocationOfTheAcademy.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            return false;
        } else if (!Global.isValidAddress(address)) {
            activityAddAcademyInformationBinding.filledTextFieldLocationOfTheAcademy.setErrorEnabled(true);
            activityAddAcademyInformationBinding.filledTextFieldLocationOfTheAcademy.setError(mContext.getResources().getString(R.string.Please_enter_address_for_verification));
            activityAddAcademyInformationBinding.filledTextFieldLocationOfTheAcademy.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            return false;
        } else if (fee.isEmpty()) {
            activityAddAcademyInformationBinding.filledTextFieldAcademyFees.setErrorEnabled(true);
            activityAddAcademyInformationBinding.filledTextFieldAcademyFees.setError(mContext.getResources().getString(R.string.Enter_academy_fee_month));
            activityAddAcademyInformationBinding.filledTextFieldAcademyFees.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            return false;
        } else if (fee.equalsIgnoreCase("0") || Float.parseFloat(fee) < 1.0000) {
            activityAddAcademyInformationBinding.filledTextFieldAcademyFees.setErrorEnabled(true);
            activityAddAcademyInformationBinding.filledTextFieldAcademyFees.setError(mContext.getResources().getString(R.string.Enter_academy_fee_month));
            activityAddAcademyInformationBinding.filledTextFieldAcademyFees.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            return false;
        } else if (!Global.isAboutDetails(about)) {

            activityAddAcademyInformationBinding.filledTextFieldDetailsAcademy.setErrorEnabled(true);
            activityAddAcademyInformationBinding.filledTextFieldDetailsAcademy.setError(mContext.getResources().getString(R.string.Enter_details_about_your_academy));
            activityAddAcademyInformationBinding.filledTextFieldDetailsAcademy.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            return false;
        }
//        else if (!Global.isShortDes(short_desc)) {
//            Toaster.customToast(mContext.getResources().getString(R.string.Enter_short_des_your_academy));
//            return false;
//        }
//        else if (selectedTimeSlott.isEmpty()) {
//            Toaster.customToast(mContext.getResources().getString(R.string.Select_session_time_for_online_training));
//            return false;
//        }
        else if (activityAddAcademyInformationBinding.editContactPersonName.getText().toString().isEmpty()) {
            activityAddAcademyInformationBinding.filledTextFieldContactPersonName.setErrorEnabled(true);
            activityAddAcademyInformationBinding.filledTextFieldContactPersonName.setError(mContext.getResources().getString(R.string.personName));
            activityAddAcademyInformationBinding.filledTextFieldContactPersonName.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            return false;
        } else if (!Global.isValidMobile(contact_person_phone)) {
            activityAddAcademyInformationBinding.filledTextFieldContactPersonPhoneNumber.setErrorEnabled(true);
            activityAddAcademyInformationBinding.filledTextFieldContactPersonPhoneNumber.setError(mContext.getResources().getString(R.string.error_invalid_phone));
            activityAddAcademyInformationBinding.filledTextFieldContactPersonPhoneNumber.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            return false;
        }
        else if (training_type.isEmpty()) {
            Toaster.customToast(mContext.getResources().getString(R.string.Select_training_type));
            return false;
        } else if (categoryId == null) {
            Toaster.customToast(mContext.getResources().getString(R.string.choose_your_specialities));
            return false;
        } else if (activityAddAcademyInformationBinding.textViewLanguage.getText().toString().equalsIgnoreCase("Language for training") || activityAddAcademyInformationBinding.textViewLanguage.getText().toString().isEmpty()) {
            Toaster.customToast(mContext.getResources().getString(R.string.Select_mode_of_language_of_the_academy));
            return false;
        }


        return true;
    }

    private void addAcademyRequest() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.ADD_ACADEMY, response -> {
            Timber.d(response);

            try {
                loaderView.hideLoader();
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                    Toaster.customToast(jsonObject.getString("msg"));
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    SessionManager.save_academyId(prefs, String.valueOf(jsonObject1.getInt("academy")));


                    if (FROM.equalsIgnoreCase("2")) {
                        new Handler().postDelayed((Runnable) () -> {
                            int size;
                            if(galleryVideoList.isEmpty()){
                                startActivity(new Intent(mActivity, AddAcademyGalleryActivity.class)
                                        .putExtra("Banner", banner_image)
                                        .putExtra("Logo", logo)
                                        .putExtra("Image", galleryImageList)
                                        .putExtra("Video", "")
                                        .putExtra("FROM","2"));
                                finish();
                            }else{
                                startActivity(new Intent(mActivity, AddAcademyGalleryActivity.class)
                                        .putExtra("Banner", banner_image)
                                        .putExtra("Logo", logo)
                                        .putExtra("Image", galleryImageList)
                                        .putExtra("Video", galleryVideoList.get(galleryVideoList.size() - 1))
                                        .putExtra("FROM","2"));
                                finish();
                            }


                        }, 1000);
                    } else {
                        new Handler().postDelayed((Runnable) () -> {
                            startActivity(new Intent(mActivity, AddAcademyGalleryActivity.class)
                                    .putExtra("FROM","1"));
                            finish();
                        }, 1000);
                    }

                } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                    Global.msgDialog(mActivity, jsonObject.getJSONObject("errors").getString("error_text"));
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
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("name", name);
                param.put("email", email);
                param.put("address", address);
                param.put("fee", fee);
                param.put("about", about);
                param.put("achievement", achievement);
                param.put("short_desc", short_desc);
                param.put("contact_person_phone", contact_person_phone);
                param.put("contact_person", contact_person);
                param.put("training_type", training_type);
                param.put("training_session", "");
                param.put("Facebook", facebook);
                param.put("Twitter", twitter);
                param.put("Youtube", youtube);
                param.put("Instagram", instagram);
                param.put("LinkedIn", linkedIn);
                param.put("language_id", langStringBuilder.toString());
                param.put("role_id", "");
                param.put("category_id", categoryId.toString());
                param.put("academy_id", SessionManager.get_academyId(prefs));

                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void setData(JSONObject data) {

//        if (data.has("academy_slots")) {
//            try {
//                JSONArray jsonArray = data.getJSONArray("academy_slots");
//                ArrayList<String> newList = new ArrayList<>();
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    newList.add(jsonArray.getJSONObject(i).getString("slot_id"));
//                }
//
//                recycler_view_session.setAdapter(new AddAcademyTimeSessionAdapter(newList, addAcademySlots, (slotId) -> {
//                    String[] namesArr = slotId.toArray(new String[0]);
//                    selectedTimeSlott = toCSV(namesArr);
//                }));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }

        if (data.has("specialization_category")) {
            try {
                JSONArray jsonArray = data.getJSONArray("specialization_category");
                JSONObject jsonObject = null;
                Datum coachLanguage = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    coachLanguage = new Datum(jsonObject);
                    editList_speclization.add(coachLanguage);
                }
                activityAddAcademyInformationBinding.rcvSpecialities.setAdapter(new AddAcademySpecilitiesButtonAdapter(mContext, editList_speclization, modelArrayList.getData(), AddAcademyInformationActivity.this));


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        if (data.has("academy_languages")) {

            try {
                JSONArray jsonArray = data.getJSONArray("academy_languages");

                JSONObject jsonObject;
                CoachLanguage coachLanguage;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    coachLanguage = new CoachLanguage(jsonObject);
                    coachLanguages.add(coachLanguage.getName_eng());
                }

                languageSelectionDialog(language, coachLanguages);

                langStringBuilder = new StringBuilder();
                String prefix = "";
                for (String item : coachLanguages) {
                    langStringBuilder.append(prefix);
                    prefix = ",";
                    langStringBuilder.append(item);
                }

                activityAddAcademyInformationBinding.textViewLanguage.setText(langStringBuilder.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (data.has("name")) {
            activityAddAcademyInformationBinding.editTextAcademyName.setText(data.optString("name"));
        }
        if (data.has("email")) {
            activityAddAcademyInformationBinding.editTextAcademyEmail.setText(data.optString("email"));
            activityAddAcademyInformationBinding.editTextAcademyEmail.setEnabled(false);
            activityAddAcademyInformationBinding.editTextAcademyEmail.setClickable(false);
        }
        if (data.has("address")) {
            try {
                activityAddAcademyInformationBinding.editLocationOfTheAcademy.setText(data.getString("address"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (data.has("fee")) {
            activityAddAcademyInformationBinding.editAcademyFees.setText(data.optString("fee"));

        }
        if (data.has("about")) {
            activityAddAcademyInformationBinding.editDetailsAcademy.setText(data.optString("about"));
        }
        if (data.has("achievement")) {
            activityAddAcademyInformationBinding.editAchivementAcademy.setText(data.optString("achievement"));
        }
        if (data.has("contact_person_name")) {
            activityAddAcademyInformationBinding.editContactPersonName.setText(data.optString("contact_person_name"));
        }
        if (data.has("contact_person_phone")) {
            activityAddAcademyInformationBinding.editContactPersonPhoneNumber.setText(data.optString("contact_person_phone"));
        }
        if (data.has("training_type")) {
            training_type = data.optString("training_type");

            if (training_type.equalsIgnoreCase("Personal")) {
                activityAddAcademyInformationBinding.cbPersonal.setChecked(true);
            }
            if (training_type.equalsIgnoreCase("Group")) {
                activityAddAcademyInformationBinding.cbGroup.setChecked(true);
            }
        }

        if (data.has("academy_image")) {
            try {
                JSONArray jsonArray = data.getJSONArray("academy_image");
                galleryImageList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    galleryImageList.add(new AcademyGallery(jsonArray.getJSONObject(i)));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (data.has("academy_video")) {
            try {
                JSONArray jsonArray = data.getJSONArray("academy_video");
                galleryVideoList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    galleryVideoList.add(jsonArray.getJSONObject(i).getString("files"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        if (data.has("banner_image")) {
            banner_image = data.optString("banner_image");
        }

        if (data.has("logo")) {
            logo = data.optString("logo");
        }

        if (data.has("video")) {
            video = data.optString("video");
        }

        if (data.has("Facebook")) {
            activityAddAcademyInformationBinding.editFacebook.setText(data.optString("Facebook"));
        }
        if (data.has("Twitter")) {
            activityAddAcademyInformationBinding.editTwitter.setText(data.optString("Twitter"));
        }

        if (data.has("LinkedIn")) {
            activityAddAcademyInformationBinding.editLinkend.setText(data.optString("LinkedIn"));
        }
        if (data.has("Instagram")) {
            activityAddAcademyInformationBinding.editInstagram.setText(data.optString("Instagram"));
        }
        if (data.has("Youtube")) {
            activityAddAcademyInformationBinding.editYoutube.setText(data.optString("Youtube"));
        }
        //  try {

//            if(data.has("role")){
//                String value= data.getJSONObject("role").toString();
//                JSONObject  roleJsonObject = new JSONObject(value);
//                if(roleJsonObject.has("role_id")){
//                    try {
//                        roleId = roleJsonObject.getString("role_id");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                if(roleJsonObject.has("name")){
//                    try {
//                        roleName = roleJsonObject.getString("name");
//                        //textView_role.setText(roleName);
//                        spn_role.setText(roleName);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }


//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }

    public void getUsersDetails() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.GET_ACADEMY_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("AcademyDetails",response);
                        Timber.d(response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {

                                if (jsonObject.has("data")) {
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                    setData(jsonObject1);
                                }

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(mActivity, jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loaderView.hideLoader();
                                }
                            },8000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("academy_id", SessionManager.get_academyId(prefs));
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
}