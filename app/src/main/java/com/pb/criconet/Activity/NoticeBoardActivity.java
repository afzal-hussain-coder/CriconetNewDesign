package com.pb.criconet.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.pb.criconet.R;
import com.pb.criconet.adapter.PavilionAdapter.NoticeBoardAdapter;
import com.pb.criconet.databinding.ActivityNoticeBoardBinding;
import com.pb.criconet.databinding.DialogAddNoticeBoardFormBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.model.DataModelSpecialization;
import com.pb.criconet.model.NoticeBoardModel;
import com.pb.criconet.util.BookingHistoryDropDown;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.DataModel;
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

public class NoticeBoardActivity extends AppCompatActivity {

    ActivityNoticeBoardBinding activityNoticeBoardBinding;
    Context mContext;
    Activity mActivity;
    ToolbarInnerpageBinding toolbarInnerpageBinding;

    ArrayList<DataModel> option_category = new ArrayList<>();
    CustomLoaderView loaderView;
    private RequestQueue queue;
    private SharedPreferences prefs;
    String selectedCategoryName = "";
    String title = "";
    String des = "";
    NoticeBoardAdapter noticeBoardAdapter;
    private ArrayList<NoticeBoardModel> noticeBoardModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityNoticeBoardBinding = ActivityNoticeBoardBinding.inflate(getLayoutInflater());
        setContentView(activityNoticeBoardBinding.getRoot());
        mContext = this;
        mActivity = this;

        loaderView = CustomLoaderView.initialize(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        queue = Volley.newRequestQueue(this);

        inItView();

        if (Global.isOnline(mContext)) {
            getNoticeBoardList();
        } else {
            Global.showDialog(mActivity);
        }

    }

    private void inItView() {
        toolbarInnerpageBinding = activityNoticeBoardBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(getResources().getString(R.string.notice_boar));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        activityNoticeBoardBinding.rcVNoticeBoard.setHasFixedSize(true);
        activityNoticeBoardBinding.rcVNoticeBoard.setLayoutManager(new LinearLayoutManager(mActivity));

        noticeBoardAdapter = new NoticeBoardAdapter(mContext, new ArrayList<>(), new NoticeBoardAdapter.noticeBoardItemClickListener() {
            @Override
            public void noticeItemClickEvent(NoticeBoardModel noticeBoardModel) {
                startActivity(new Intent(mContext, NoticeBoardDetailsActivity.class)
                        .putExtra("NoticeBoard", noticeBoardModel));
            }

            @Override
            public void deleteItem(int pos, String id) {
                if (Global.isOnline(mContext)) {
                    deleteNoticeBoard(pos,id);
                } else {
                    Global.showDialog(mActivity);
                }
            }
        });
        activityNoticeBoardBinding.rcVNoticeBoard.setAdapter(noticeBoardAdapter);

        toolbarInnerpageBinding.ivAddNotice.setVisibility(View.VISIBLE);
        toolbarInnerpageBinding.ivAddNotice.setOnClickListener(view -> {
            openCameraAndGalleryDialog();
        });
    }


    private void getNoticeCategories(DialogAddNoticeBoardFormBinding binding) {
        StringRequest postRequest = new StringRequest(Request.Method.GET, Global.URL + Global.NOTICE_BOARD_CATEGORIES, response -> {

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("api_status").equalsIgnoreCase("200")) {

                    ArrayList<String> dataList = new ArrayList<>();
                    if (jsonObject.has("data")) {
                        JSONObject jsonObjectDate = jsonObject.getJSONObject("data");
                        dataList.add(jsonObjectDate.getString("LiveStreaming"));
                        dataList.add(jsonObjectDate.getString("Cricket"));
                        dataList.add(jsonObjectDate.getString("Ground"));
                        dataList.add(jsonObjectDate.getString("Team"));
                        dataList.add(jsonObjectDate.getString("Player"));
                        dataList.add(jsonObjectDate.getString("Match"));
                    }

                    for (int i = 0; i < dataList.size(); i++) {
                        option_category.add(new DataModel(dataList.get(i)));
                    }

                    // Ensure bookingHistoryDropDown is not null before setting options
                    if (binding != null) {
                        binding.dropTipsCategory.setOptionList(option_category);
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

    private void createNoticeBoard() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.CREATE_NOTICE_BOARD,
                response -> {
                    Log.d("ForgetPasswordResponse", response);
                    loaderView.hideLoader();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {

                            Toaster.customToast(jsonObject.optString("message"));

                            if(jsonObject.has("data")){
                                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                NoticeBoardModel noticeBoardModel = new NoticeBoardModel(jsonObjectData);
                                addNewNoticeBoardItem(noticeBoardModel);

                            }

                            if (Global.isOnline(mContext)) {
                                getNoticeBoardList();
                            } else {
                                Global.showDialog(mActivity);
                            }

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
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("notic_type", selectedCategoryName);
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("title", title);
                param.put("details", des);

                System.out.println("data   " + param);

                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    public void addNewNoticeBoardItem(NoticeBoardModel newItem) {
        noticeBoardAdapter.addItem(newItem); // Call the adapter's add method
    }

    private void getNoticeBoardList() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.NOTICE_BOARD_LIST,
                response -> {
                    Log.d("NoticeBoardList", response);
                    loaderView.hideLoader();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {
                            ArrayList<NoticeBoardModel> noticeBoardModels = new ArrayList<>();

                            if (jsonObject.has("data")) {
                                JSONArray jsonObjectArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonObjectArray.length(); i++) {
                                    noticeBoardModels.add(new NoticeBoardModel(jsonObjectArray.getJSONObject(i)));
                                }
                            }

                            noticeBoardAdapter.updateData(noticeBoardModels);

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
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));

                System.out.println("data   " + param);

                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }


    private void deleteNoticeBoard(int pos,String id) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.DELETE_NOTICE_BOARD,
                response -> {
                    Log.d("ForgetPasswordResponse", response);
                    loaderView.hideLoader();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {

                            Toaster.customToast(jsonObject.optString("message"));

                            noticeBoardAdapter.deleteItem(pos);

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
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("id", id);
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));

                System.out.println("data   " + param);

                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    /**
     * open camera and gallery using this bottomSliderDialog
     */
    public void openCameraAndGalleryDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(mActivity, R.style.BottomSheetDialogTheme);

        // Inflate the layout using ViewBinding
        DialogAddNoticeBoardFormBinding binding = DialogAddNoticeBoardFormBinding.inflate(LayoutInflater.from(mActivity));
        dialog.setContentView(binding.getRoot());

        binding.dropTipsCategory.setClickListener(new BookingHistoryDropDown.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name, String id) {
                selectedCategoryName = name;
            }


            @Override
            public void onDismiss() {
            }
        });

        binding.editTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.filledTextFieldTitle.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    binding.filledTextFieldTitle.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.filledTextFieldTitle.setErrorEnabled(false);
            }
        });

        binding.messageTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.messageTextInputLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    binding.messageTextInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.messageTextInputLayout.setErrorEnabled(false);
            }
        });


        binding.flSubmit.setOnClickListener(view -> {
            title = binding.editTextTitle.getText().toString().trim();
            des = binding.messageTextInputEditText.getText().toString().trim();
            if (binding.editTextTitle.getText().toString().length() < 8) {
                binding.filledTextFieldTitle.setErrorEnabled(true);
                binding.filledTextFieldTitle.setError("Title should not be less than 8 characters");
                binding.filledTextFieldTitle.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.app_light_red)));

            } else if (binding.messageTextInputEditText.getText().toString().length() < 8) {
                binding.messageTextInputLayout.setErrorEnabled(true);
                binding.messageTextInputLayout.setError("Message should not be less than 8 characters");
                binding.messageTextInputLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.app_light_red)));

            } else if (selectedCategoryName.isEmpty()) {
                Toaster.customToast("Select Categories");

            } else {
                if (Global.isOnline(mContext)) {
                    createNoticeBoard();
                } else {
                    Global.showDialog(mActivity);
                }
                dialog.dismiss();


            }
        });


        if (Global.isOnline(mContext)) {
            getNoticeCategories(binding);
        } else {
            Global.showDialog(mActivity);
        }

        dialog.show();

    }
}