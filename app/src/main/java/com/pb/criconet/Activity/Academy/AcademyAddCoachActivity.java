package com.pb.criconet.Activity.Academy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.pb.criconet.R;
import com.pb.criconet.adapter.SpecialitiesAdapter;
import com.pb.criconet.databinding.ActivityAcademyAddCoachBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.model.AcademyModel.AcademyRole;
import com.pb.criconet.model.AcademyModel.ManageCoachesModel;
import com.pb.criconet.model.Country;
import com.pb.criconet.model.DataModelSpecialities;
import com.pb.criconet.model.Datum;
import com.pb.criconet.model.StreamingModel.City;
import com.pb.criconet.model.StreamingModel.States;
import com.pb.criconet.util.BookStreamDropDown;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.DataModel;
import com.pb.criconet.util.DropDownBlue;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.SessionManager;
import com.pb.criconet.util.Toaster;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import timber.log.Timber;

public class AcademyAddCoachActivity extends AppCompatActivity {
    ActivityAcademyAddCoachBinding activityAcademyAddCoachBinding;
    Context mContext;
    Activity mActivity;
    private DataModelSpecialities modelArrayList;
    private RequestQueue queue;
    CustomLoaderView loaderView;
    private SharedPreferences prefs;
    ArrayList<DataModelSpecialities.Datum> updateList = new ArrayList<>();
    private final ArrayList<DataModel> option_role = new ArrayList<>();
    private ArrayList<DataModel> option_gender = new ArrayList<>();
    private ArrayList<DataModel> option_country = new ArrayList<>();
    private ArrayList<DataModel> option_state = new ArrayList<>();
    private ArrayList<DataModel> option_city = new ArrayList<>();
    private ArrayList<DataModel> optionYear = new ArrayList<>();
    private ArrayList<DataModel> optionMonth = new ArrayList<>();
    //private DataModel modelArrayList;
    private Country countryArrayList;
    private City citymodelArrayList;
    private States statemodelArrayList;

    ArrayList<AcademyRole> roleArrayList = null;
    AcademyRole academyRole;
    String countryID = "", stateID = "", cityID = "",userId_updated = "",selectedYear = "",profileTitle = "", selectedMonth = "";
    String FROM ="";
    private ManageCoachesModel manageCoachesModel;
    ToolbarInnerpageBinding toolbarInnerpageBinding;
    JSONObject roleJsonObject = null;
    String roleName = "", roleId = "",countryName="",state_Name="",city_Name="";
    private String what_you_teach = "", skills_you_learn = "", about_coach_profile = "",achievement="", username = "", password = "", gender = "", phoneNumber = "", pincode = "", address = "", landmark = "", email_String = "", mName = "", name_String, fname_String, lname_String, gender_String;
    private StringBuilder categoryId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAcademyAddCoachBinding = ActivityAcademyAddCoachBinding.inflate(getLayoutInflater());
        setContentView(activityAcademyAddCoachBinding.getRoot());

        mContext = this;
        mActivity = this;

        toolbarInnerpageBinding = activityAcademyAddCoachBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.add_coach));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        loaderView = CustomLoaderView.initialize(mContext);
        queue = Volley.newRequestQueue(mContext);



        if (Global.isOnline(mContext)) {
            getSpecialities();
        } else {
            Global.showDialog(mActivity);
        }
        if (Global.isOnline(mContext)) {
            getCountry();
        } else {
            Global.showDialog(mActivity);
        }
        if (Global.isOnline(mActivity)) {
            getRole();
        } else {
            Global.showDialog(mActivity);
        }

        inItView();
        getIntentValue();
    }


    private void inItView() {

        activityAcademyAddCoachBinding.rcvSpecialities.setHasFixedSize(true);
        activityAcademyAddCoachBinding.rcvSpecialities.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));

        activityAcademyAddCoachBinding.dropSelectRole.setClickListener(new DropDownBlue.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name, String id) {
                roleId = id;
            }

            @Override
            public void onDismiss() {
            }
        });


        option_gender.add(new DataModel("Male"));
        option_gender.add(new DataModel("Female"));
        activityAcademyAddCoachBinding.dropSelectGender.setOptionList(option_gender);
        activityAcademyAddCoachBinding.dropSelectGender.setClickListener(new DropDownBlue.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name, String id) {
                gender = name;
            }

            @Override
            public void onDismiss() {
            }
        });

        for(int i =0;i<12 ;i++){

            if(i==0 || i ==1){
                optionMonth.add(new DataModel(i+" month"));
            }else{
                optionMonth.add(new DataModel(i+" months"));
            }

        }
        activityAcademyAddCoachBinding.dropMonth.setOptionList(optionMonth);

        for(int i=0;i<30;i++){
            if(i==0 || i ==1){
                optionYear.add(new DataModel(i+" year"));
            }else{
                optionYear.add(new DataModel(i+" years"));
            }
        }
        activityAcademyAddCoachBinding.dropYear.setOptionList(optionYear);


        activityAcademyAddCoachBinding.editTextFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAcademyAddCoachBinding.filledTextFieldFirstName.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAcademyAddCoachBinding.filledTextFieldFirstName.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAcademyAddCoachBinding.filledTextFieldFirstName.setErrorEnabled(false);
            }
        });

        activityAcademyAddCoachBinding.editTextLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAcademyAddCoachBinding.filledTextFieldLastName.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAcademyAddCoachBinding.filledTextFieldLastName.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAcademyAddCoachBinding.filledTextFieldLastName.setErrorEnabled(false);
            }
        });

        activityAcademyAddCoachBinding.editTextUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAcademyAddCoachBinding.filledTextFieldUserName.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAcademyAddCoachBinding.filledTextFieldUserName.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAcademyAddCoachBinding.filledTextFieldUserName.setErrorEnabled(false);
            }
        });

        activityAcademyAddCoachBinding.emailInputLayoutEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAcademyAddCoachBinding.textInputLayoutEmail.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAcademyAddCoachBinding.textInputLayoutEmail.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAcademyAddCoachBinding.textInputLayoutEmail.setErrorEnabled(false);
            }
        });

        activityAcademyAddCoachBinding.phoneInputLayoutEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAcademyAddCoachBinding.phoneInputLayou.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAcademyAddCoachBinding.phoneInputLayou.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAcademyAddCoachBinding.phoneInputLayou.setErrorEnabled(false);
            }
        });

        activityAcademyAddCoachBinding.editTextAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityAcademyAddCoachBinding.filledTextFieldAge.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityAcademyAddCoachBinding.filledTextFieldAge.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityAcademyAddCoachBinding.filledTextFieldAge.setErrorEnabled(false);
            }
        });


        activityAcademyAddCoachBinding.flSubmit.setOnClickListener(view -> {
            if (checkValidation()) {
                if (Global.isOnline(mContext)) {
                    addAcademyCoach();
                } else {
                    Global.showDialog(mActivity);
                }
            }

        });

    }

    private void getIntentValue() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            FROM = extras.getString("FROM");
            if ((getIntent().getBundleExtra("Certificate") == null)) {
            } else {
                Bundle args = getIntent().getBundleExtra("Certificate");
                manageCoachesModel = (ManageCoachesModel) args.getSerializable("ARRAYLIST");
                userId_updated = manageCoachesModel.getUser_id();
                if (userId_updated.equalsIgnoreCase("")) {
                    toolbarInnerpageBinding.toolbartext.setText(getResources().getString(R.string.add_coach));
                } else {
                    toolbarInnerpageBinding.toolbartext.setText(getResources().getString(R.string.update_coach));
                }
                try {
                    roleJsonObject = new JSONObject(manageCoachesModel.getRoleJsonObject());
                    //Log.d("json",roleJsonObject.toString());

                    if (roleJsonObject.has("role_id")) {
                        try {
                            roleId = roleJsonObject.getString("role_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (roleJsonObject.has("name")) {
                        try {
                            roleName = roleJsonObject.getString("name");
                            //textView_role.setText(roleName);
                            activityAcademyAddCoachBinding.dropSelectRole.setText(roleName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                int is_owner = manageCoachesModel.getIs_owner();
                if (is_owner == 1) {
                    activityAcademyAddCoachBinding.dropSelectRole.setClickable(false);
                    activityAcademyAddCoachBinding.dropSelectRole.setEnabled(false);
                } else {
                    activityAcademyAddCoachBinding.dropSelectRole.setClickable(true);
                    activityAcademyAddCoachBinding.dropSelectRole.setEnabled(true);
                }

                activityAcademyAddCoachBinding.editTextFirstName.setText(manageCoachesModel.getFirst_name());
                activityAcademyAddCoachBinding.editTextMiddleName.setText(manageCoachesModel.getMid_name());
                activityAcademyAddCoachBinding.editTextLastName.setText(manageCoachesModel.getLast_name());
                activityAcademyAddCoachBinding.phoneInputLayoutEditText.setText(manageCoachesModel.getPhone());
                activityAcademyAddCoachBinding.passwordInputLayoutEditText.setVisibility(View.GONE);
                activityAcademyAddCoachBinding.editTextUserName.setText(manageCoachesModel.getUsername());
                activityAcademyAddCoachBinding.editTextUserName.setClickable(false);
                activityAcademyAddCoachBinding.editTextUserName.setEnabled(false);
                activityAcademyAddCoachBinding.emailInputLayoutEmail.setText(manageCoachesModel.getEmail());
                activityAcademyAddCoachBinding.emailInputLayoutEmail.setClickable(false);
                activityAcademyAddCoachBinding.emailInputLayoutEmail.setEnabled(false);
                activityAcademyAddCoachBinding.editTextaddress1.setText(manageCoachesModel.getAddress());
                activityAcademyAddCoachBinding.editTextaddress2.setText(manageCoachesModel.getAddress2());
                gender = manageCoachesModel.getGender();
                activityAcademyAddCoachBinding.dropSelectGender.setText(gender);
                activityAcademyAddCoachBinding.editTextPincode.setText(manageCoachesModel.getPincode());

                String monthName = "";
                if (manageCoachesModel.getExp_months().equalsIgnoreCase("0") || manageCoachesModel.getExp_months().equalsIgnoreCase("1")) {
                    monthName = manageCoachesModel.getExp_months() + " " + "month";
                } else if (manageCoachesModel.getExp_months().equalsIgnoreCase("")) {
                    monthName = "0" + " " + "month";
                } else {
                    monthName = manageCoachesModel.getExp_months() + " " + "months";
                }


                selectedMonth = monthName;
                if (monthName.contains("month")) {
                    selectedMonth = monthName.replace("month", "").trim();
                }
                if (monthName.contains("months")) {
                    selectedMonth = monthName.replace("months", "").trim();
                }
                activityAcademyAddCoachBinding.dropMonth.setText(monthName);

                String yearName = "";
                if (manageCoachesModel.getExp_years().equalsIgnoreCase("0")) {
                    yearName = manageCoachesModel.getExp_years() + " " + "year";
                } else if (manageCoachesModel.getExp_years().equalsIgnoreCase("1")) {
                    yearName = manageCoachesModel.getExp_years() + " " + "year";
                } else if (manageCoachesModel.getExp_years().equalsIgnoreCase("")) {
                    yearName = "0" + " " + "year";
                } else {
                    yearName = manageCoachesModel.getExp_years() + " " + "years";
                }
                selectedYear = yearName;
                if (yearName.contains("year")) {
                    selectedYear = yearName.replace("year", "").trim();
                }
                if (yearName.contains("years")) {
                    selectedYear = yearName.replace("years", "").trim();
                }
                activityAcademyAddCoachBinding.dropYear.setText(yearName);


                activityAcademyAddCoachBinding.acheivementInputLayoutEditText.setText(manageCoachesModel.getAchievement());
                activityAcademyAddCoachBinding.skillStuLearnTextInputEditText.setText(manageCoachesModel.getSkills_you_learn());
                activityAcademyAddCoachBinding.whatDoYouTeachTextInputEditText.setText(manageCoachesModel.getWhat_you_teach());
               // activityAcademyAddCoachBinding..setText(manageCoachesModel.getProfile_title());


                countryName = manageCoachesModel.getCountry_name();
                countryID = manageCoachesModel.getCountry_id();
                getState(countryID);
                activityAcademyAddCoachBinding.dropSelectCountry.setText(countryName);


                state_Name = manageCoachesModel.getState_name();
                stateID = manageCoachesModel.getState_id();
                getCity(stateID);


                city_Name = manageCoachesModel.getCity_name();


                try {
                    ArrayList<ManageCoachesModel.Specializationcategory> editList_speclization = manageCoachesModel.getTeamsArrayList_info();
                    if (editList_speclization != null) {

                        JSONArray jsonArray = new JSONArray();

                        for (int i = 0; i < editList_speclization.size(); i++) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("id", editList_speclization.get(i).getId());
                            jsonObject.put("title", editList_speclization.get(i).getTitle());
                            jsonArray.put(jsonObject);
                        }


                        JSONObject jsonObjectt = null;
                        DataModelSpecialities.Datum coachLanguage = null;
                        ArrayList<DataModelSpecialities.Datum> editList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObjectt = jsonArray.getJSONObject(i);
                            coachLanguage = new DataModelSpecialities.Datum(jsonObjectt);
                            // Log.d("speclization", jsonObjectt.getString("title") + "/" + jsonObjectt.getString("id"));
                            editList.add(coachLanguage);
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                activityAcademyAddCoachBinding.rcvSpecialities.setAdapter(new SpecialitiesAdapter(mContext, editList, updateList, new SpecialitiesAdapter.ItemListenerr() {
                                    @Override
                                    public void onItemClick(List<String> item) {
                                        categoryId = new StringBuilder();
                                        String prefix = "";

                                        for (String itemm : item) {
                                            categoryId.append(prefix);
                                            prefix = ",";
                                            categoryId.append(itemm);
                                        }
                                    }
                                }));

                            }
                        }, 1000);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }
    }

    private void getRole() {
        StringRequest postRequest = new StringRequest(Request.Method.GET, Global.URL + Global.ACADEMY_ROLE_LIST, response -> {
            Timber.d(response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("api_status") == 200) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    roleArrayList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        academyRole = new AcademyRole(jsonArray.getJSONObject(i));
                        //roleArrayList.add(new AcademyRole(jsonArray.getJSONObject(i)));
                        option_role.add(new DataModel(academyRole.getName(), academyRole.getId()));
                        activityAcademyAddCoachBinding.dropSelectRole.setOptionList(option_role);
                        //roleSelectionDialog(roleArrayList,roleId);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
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

    private void getSpecialities() {
        StringRequest postRequest = new StringRequest(Request.Method.GET, Global.URL + Global.GET_SPECIALITIES_CATEGORY, response -> {
            Gson gson = new Gson();
            modelArrayList = gson.fromJson(response, DataModelSpecialities.class);

            if (modelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                //Toaster.customToast(modelArrayList.getData().size()+"");
                updateList = (ArrayList<DataModelSpecialities.Datum>) modelArrayList.getData();
                activityAcademyAddCoachBinding.rcvSpecialities.setAdapter(new SpecialitiesAdapter(mContext, new ArrayList<>(), modelArrayList.getData(), new SpecialitiesAdapter.ItemListenerr() {
                    @Override
                    public void onItemClick(List<String> item) {
                        categoryId = new StringBuilder();
                        String prefix = "";

                        for (String itemm : item) {
                            categoryId.append(prefix);
                            prefix = ",";
                            categoryId.append(itemm);
                        }
                    }
                }));
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

    private void getCountry() {
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_countries", response -> {
            Log.d("ResponseCountry", response);
            Gson gson = new Gson();
            countryArrayList = gson.fromJson(response, Country.class);
            if (countryArrayList.getApiStatus().equalsIgnoreCase("200")) {
                ArrayList<String> country = new ArrayList<>();
                country.add("Country");
                for (Country.Datum data : countryArrayList.getData()) {
                    country.add(data.getName());
                    option_country.add(new DataModel(data.getName(), data.getId()));
                }



                activityAcademyAddCoachBinding.dropSelectCountry.setOptionList(option_country);
                activityAcademyAddCoachBinding.dropSelectCountry.setClickListener(new DropDownBlue.onClickInterface() {
                    @Override
                    public void onClickAction() {
                    }

                    @Override
                    public void onClickDone(String name,String id) {
                        countryID = id;

                        if (Global.isOnline(mContext)) {
                            getState(id);
                        } else {
                            Global.showDialog(mActivity);
                        }
                    }


                    @Override
                    public void onDismiss() {
                    }
                });
            }

        }, error -> {
            error.printStackTrace();
            Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
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

    private void getState(String countryId) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.GET, Global.URL + "get_states" + "&country_id=" + countryId, response -> {
            Log.d("ResponseState", response);
            if (!response.isEmpty()) {
                loaderView.hideLoader();
                Gson gson = new Gson();
                statemodelArrayList = gson.fromJson(response, States.class);
                if (statemodelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                    ArrayList<String> state = new ArrayList<>();
                    state.add("States/UT");
                    for (States.Datum data : statemodelArrayList.getData()) {
                        state.add(data.getName());
                        option_state.add(new DataModel(data.getName(), data.getId()));
                    }


                    activityAcademyAddCoachBinding.dropSelectState.setOptionList(option_state);
                    activityAcademyAddCoachBinding.dropSelectState.setClickListener(new DropDownBlue.onClickInterface() {
                        @Override
                        public void onClickAction() {
                        }

                        @Override
                        public void onClickDone(String name,String id) {
                            stateID = id;

                            if (Global.isOnline(mContext)) {
                                getCity(id);
                            } else {
                                Global.showDialog(mActivity);
                            }
                        }


                        @Override
                        public void onDismiss() {
                        }
                    });
                }
            }


        }, error -> {
            loaderView.hideLoader();
            error.printStackTrace();
            Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void getCity(String stateId) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.GET, Global.URL + "get_cities" + "&state_id=" + stateId, response -> {
            Log.d("ResponseCity", response);
            loaderView.hideLoader();
            Gson gson = new Gson();
            citymodelArrayList = gson.fromJson(response, City.class);
            if (citymodelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                ArrayList<String> city = new ArrayList<>();
                city.add("City");
                for (City.Datum data : citymodelArrayList.getData()) {
                    city.add(data.getName());
                    option_city.add(new DataModel(data.getName(), data.getId()));
                }

                activityAcademyAddCoachBinding.dropSelectCity.setOptionList(option_city);
                activityAcademyAddCoachBinding.dropSelectCity.setClickListener(new DropDownBlue.onClickInterface() {
                    @Override
                    public void onClickAction() {
                    }

                    @Override
                    public void onClickDone(String name,String id) {
                        cityID = id;
                    }


                    @Override
                    public void onDismiss() {
                    }
                });
            }

        }, error -> {
            loaderView.hideLoader();
            error.printStackTrace();
            Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private boolean checkValidation() {
        fname_String = activityAcademyAddCoachBinding.editTextFirstName.getText().toString().trim();
        lname_String = activityAcademyAddCoachBinding.editTextLastName.getText().toString().trim();
        mName = activityAcademyAddCoachBinding.editTextMiddleName.getText().toString().trim();
        username = activityAcademyAddCoachBinding.editTextUserName.getText().toString().trim();
        email_String = activityAcademyAddCoachBinding.emailInputLayoutEmail.getText().toString().trim();
        password = activityAcademyAddCoachBinding.passwordInputLayoutEditText.getText().toString().trim();
        what_you_teach = activityAcademyAddCoachBinding.whatDoYouTeachTextInputEditText.getText().toString().trim();
        skills_you_learn = activityAcademyAddCoachBinding.skillStuLearnTextInputEditText.getText().toString().trim();
        address = activityAcademyAddCoachBinding.editTextaddress1.getText().toString().trim();
        landmark = activityAcademyAddCoachBinding.editTextaddress2.getText().toString().trim();
        pincode = activityAcademyAddCoachBinding.editTextPincode.getText().toString().trim();
        phoneNumber = activityAcademyAddCoachBinding.phoneInputLayoutEditText.getText().toString().trim();
        achievement = activityAcademyAddCoachBinding.acheivementInputLayoutEditText.getText().toString().trim();
        profileTitle = activityAcademyAddCoachBinding.profileTitleInputLayoutEditText.getText().toString().trim();
        //amount = etAmount.getText().toString().trim();

        if (!Global.validateLengthofCoachRegisterr(fname_String)) {
            activityAcademyAddCoachBinding.filledTextFieldFirstName.setErrorEnabled(true);
            activityAcademyAddCoachBinding.filledTextFieldFirstName.setError("Name can't be empty!");
            activityAcademyAddCoachBinding.filledTextFieldFirstName.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            return false;
        }
//        else if (!Global.validateLengthofCoachRegisterr(mName)) {
//            Toaster.customToast(mContext.getResources().getString(R.string.Enter_Mid_Name_at_least_3_character));
//            return false;
//        }
        else if (!Global.validateLengthofCoachRegisterr(lname_String)) {
            activityAcademyAddCoachBinding.filledTextFieldLastName.setErrorEnabled(true);
            activityAcademyAddCoachBinding.filledTextFieldLastName.setError(mContext.getResources().getString(R.string.Enter_Last_Name_at_least_3_character));
            activityAcademyAddCoachBinding.filledTextFieldLastName.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            return false;
        } else if (!Global.validateLengthofCoachRegisterr(username)) {
            activityAcademyAddCoachBinding.filledTextFieldUserName.setErrorEnabled(true);
            activityAcademyAddCoachBinding.filledTextFieldUserName.setError(mContext.getResources().getString(R.string.enter_userName));
            activityAcademyAddCoachBinding.filledTextFieldUserName.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            return false;
        } else if (email_String.isEmpty()) {
            activityAcademyAddCoachBinding.textInputLayoutEmail.setErrorEnabled(true);
            activityAcademyAddCoachBinding.textInputLayoutEmail.setError(mContext.getResources().getString(R.string.Invalid_email_formatt));
            activityAcademyAddCoachBinding.textInputLayoutEmail.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            return false;
        } else if (!Global.isValidEmailAddress(email_String)) {
            activityAcademyAddCoachBinding.textInputLayoutEmail.setErrorEnabled(true);
            activityAcademyAddCoachBinding.textInputLayoutEmail.setError(mContext.getResources().getString(R.string.Invalid_email_formatt));
            activityAcademyAddCoachBinding.textInputLayoutEmail.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            return false;
        } else if (!Global.password(password) && activityAcademyAddCoachBinding.passwordInputLayoutEditText.getVisibility() == View.VISIBLE) {
            activityAcademyAddCoachBinding.passwordInputLayou.setErrorEnabled(true);
            activityAcademyAddCoachBinding.passwordInputLayou.setError(mContext.getResources().getString(R.string.password_cannot_empty));
            activityAcademyAddCoachBinding.passwordInputLayou.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            return false;
        } else if (countryID.isEmpty()) {
            Toaster.customToast(getResources().getString(R.string.Select_Country));
            return false;
        } else if (stateID.isEmpty()) {
            Toaster.customToast(getResources().getString(R.string.select_stat));
            return false;
        } else if (cityID.isEmpty()) {
            Toaster.customToast(getResources().getString(R.string.Select_state_to_select_city));
            return false;
        } else if (!Global.isValidAddress(address)) {
            Toaster.customToast(getResources().getString(R.string.enter_address));
            return false;
        } else if (!Global.isValidAddress(landmark)) {
            Toaster.customToast(getResources().getString(R.string.enter_land_mark));
            return false;
        } else if (pincode.isEmpty()) {
            Toaster.customToast(getResources().getString(R.string.enter_pincode));
            return false;
        } else if (pincode.equalsIgnoreCase("111111") || pincode.equalsIgnoreCase("222222") ||
                pincode.equalsIgnoreCase("333333") || pincode.equalsIgnoreCase("444444") ||
                pincode.equalsIgnoreCase("555555") || pincode.equalsIgnoreCase("666666") ||
                pincode.equalsIgnoreCase("777777") || pincode.equalsIgnoreCase("888888") ||
                pincode.equalsIgnoreCase("999999") || pincode.equalsIgnoreCase("000000")) {
            Toaster.customToast(getResources().getString(R.string.enter_pincodee));
            return false;
        } else if (!Global.isValidPincode(pincode)) {
            Toaster.customToast(getResources().getString(R.string.enter_pincodee));
        } else if (phoneNumber.equalsIgnoreCase("0000000000") || phoneNumber.startsWith("1111111111") ||
                phoneNumber.equalsIgnoreCase("2222222222") || phoneNumber.equalsIgnoreCase("3333333333") ||
                phoneNumber.equalsIgnoreCase("4444444444") || phoneNumber.equalsIgnoreCase("5555555555") ||
                phoneNumber.equalsIgnoreCase("6666666666") || phoneNumber.equalsIgnoreCase("7777777777") ||
                phoneNumber.equalsIgnoreCase("8888888888") || phoneNumber.equalsIgnoreCase("9999999999")) {
            Toaster.customToast(getResources().getString(R.string.enter_valid_phone_number));
            return false;
        } else if (!Global.isValidMobile(phoneNumber)) {
            Toaster.customToast(getResources().getString(R.string.enter_phone_number));
            return false;
        } else if (categoryId == null) {
            Toaster.customToast(mContext.getResources().getString(R.string.Please_choose_your_specialities));
            return false;
        } else if (!Global.validateLengthofCoachRegisterProfileTitle(profileTitle)) {
            Toaster.customToast(mContext.getResources().getString(R.string.Fill_your_profile_title_long));
            return false;
        } else if (!Global.isValidAddress(achievement)) {
            Toaster.customToast(mContext.getResources().getString(R.string.enter_achievement));
            return false;
        } else if (selectedYear.equalsIgnoreCase("Select Year") || selectedMonth.equalsIgnoreCase("Select Month")) {
            Toaster.customToast(mContext.getResources().getString(R.string.total_experience_coach));
            return false;
        } else if (selectedYear.equalsIgnoreCase("0") && selectedMonth.equalsIgnoreCase("0")) {
            Toaster.customToast(mContext.getResources().getString(R.string.Select_month));
            return false;
        } else if (gender.isEmpty()) {
            Toaster.customToast(mContext.getResources().getString(R.string.select_gender));
            return false;
        }

        return true;
    }

    private void addAcademyCoach() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.ADD_ACADEMY_COACH, response -> {
            Timber.d(response);

            try {
                loaderView.hideLoader();
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                    Toaster.customToast(jsonObject.getString("message"));

//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (FROM.equalsIgnoreCase("2")) {
//                                startActivity(new Intent(mContext, AddScheduleSessionActivity.class));
//                                finish();
//                            } else {
//                                finish();
//                            }
//                        }
//                    }, 100);
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

                if (userId_updated.isEmpty()) {
                    param.put("coach_id", "");
                } else {
                    param.put("coach_id", userId_updated);
                }
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("first_name", fname_String);
                param.put("mid_name", mName);
                param.put("last_name", lname_String);
                param.put("email", email_String);
                param.put("username", username);
                param.put("achievement", achievement);
                param.put("password", password);
                param.put("phone_number", phoneNumber);
                param.put("gender", gender);
                param.put("country_id", countryID);
                param.put("state_id", stateID);
                param.put("city_id", cityID);
                param.put("address", address);
                param.put("address2", landmark);
                param.put("pincode", pincode);
                param.put("profile_title", profileTitle);
                param.put("about_coach_profile", about_coach_profile);
                param.put("coach_category_id", categoryId.toString());
                param.put("exp_years", selectedYear);
                param.put("exp_months", selectedMonth);
                param.put("academy_id", SessionManager.get_academyId(prefs));
                param.put("role_id", roleId);
                param.put("what_you_teach", what_you_teach);
                param.put("skills_you_learn", skills_you_learn);

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