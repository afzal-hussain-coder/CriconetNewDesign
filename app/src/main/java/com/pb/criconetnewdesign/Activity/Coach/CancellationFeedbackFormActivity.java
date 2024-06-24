package com.pb.criconetnewdesign.Activity.Coach;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pb.criconetnewdesign.Activity.MainActivity;
import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.adapter.EcoachingAdapter.FeedbackAdapter;
import com.pb.criconetnewdesign.databinding.ActivityCancellationFeedbackFormBinding;
import com.pb.criconetnewdesign.databinding.ActivityUserBookingDetailsBinding;
import com.pb.criconetnewdesign.databinding.ToolbarInnerpageBinding;
import com.pb.criconetnewdesign.databinding.ToolbarReviewBinding;
import com.pb.criconetnewdesign.model.Coaching.FeedbackModel;
import com.pb.criconetnewdesign.util.CustomLoaderView;
import com.pb.criconetnewdesign.util.Global;
import com.pb.criconetnewdesign.util.SessionManager;
import com.pb.criconetnewdesign.util.Toaster;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CancellationFeedbackFormActivity extends AppCompatActivity {

    ActivityCancellationFeedbackFormBinding activityCancellationFeedbackFormBinding;
    Context mContext;
    Activity mActivity;
    private boolean isTermsPolicyChecked;
    CustomLoaderView loaderView;
    private RequestQueue queue;
    private SharedPreferences prefs;
    private String bookingId;
    private FeedbackModel feedbackModel;
    private final ArrayList<FeedbackModel> feedbackModelList = new ArrayList<>();
    private FeedbackAdapter feedbackAdapter;
    private String feedbackOption;
    private String feedbackText;
    private int lastCheckedRB = -1;
    private float refund_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCancellationFeedbackFormBinding = ActivityCancellationFeedbackFormBinding.inflate(getLayoutInflater());
        setContentView(activityCancellationFeedbackFormBinding.getRoot());
        mContext = this;
        mActivity = this;

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityCancellationFeedbackFormBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(getResources().getString(R.string.Cancellation_Feedback));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        loaderView = CustomLoaderView.initialize(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        queue = Volley.newRequestQueue(this);

        activityCancellationFeedbackFormBinding.recyclerFeedback.setHasFixedSize(true);
        activityCancellationFeedbackFormBinding.recyclerFeedback.setLayoutManager(new LinearLayoutManager(mContext));

        if (getIntent().getExtras() != null) {
            bookingId = getIntent().getExtras().getString("BookingId");
            refund_amount = getIntent().getExtras().getFloat("RefundAmount");
            //Toaster.customToast(refund_amount+"");

        }

        if (Global.isOnline(this)) {
            getCancelBookingFeedbackForm();
        } else {
            Global.showDialog(this);
        }


        activityCancellationFeedbackFormBinding.chTerams.setOnCheckedChangeListener((buttonView, isChecked) -> isTermsPolicyChecked = isChecked);
        activityCancellationFeedbackFormBinding.tvTerms.setOnClickListener(v -> {
            startActivity(new Intent(mContext, CancelletionWebView.class).putExtra("URL","https://www.criconet.com/terms/terms?rst=app").putExtra("title", "Terms"));
            finish();
        });
        activityCancellationFeedbackFormBinding.tvPolicy.setOnClickListener(v -> {

            startActivity(new Intent(mContext, CancelletionWebView.class).putExtra("URL","https://www.criconet.com/terms/terms?rst=app").putExtra("title", "Terms"));
            finish();
        });

        activityCancellationFeedbackFormBinding.flSubmitFeedback.setOnClickListener(v -> {
            feedbackText = activityCancellationFeedbackFormBinding.editTypeFeedback.getText().toString().trim();
            if (isValidate()) {
                if (Global.isOnline(this)) {
                    getBookingCancelled();
                } else {
                    Global.showDialog(this);
                }
            }
        });

    }


    private void getCancelBookingFeedbackForm() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.GET_FEEDBACK_FORM, response -> {
            loaderView.hideLoader();
            Log.d("CancelFeedbackResponse", response);
            loaderView.hideLoader();
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("api_text").equalsIgnoreCase("success")) {
                    String message = jsonObject.getString("message");

                    activityCancellationFeedbackFormBinding.tvCancelFormQuets.setText(message);

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        feedbackModel = new FeedbackModel(jsonArray.getJSONObject(i));
                        feedbackModelList.add(feedbackModel);
                    }

                    feedbackAdapter = new FeedbackAdapter(feedbackModelList, mContext, (message1, pos) -> {
                        feedbackOption = message1;
                        lastCheckedRB = pos;
                        if (pos == feedbackModelList.size() - 1) {
                            activityCancellationFeedbackFormBinding.liEdit.setVisibility(View.VISIBLE);
                            Global.showKeyboard(mActivity);
                        } else {
                            activityCancellationFeedbackFormBinding.liEdit.setVisibility(View.GONE);
                        }
                    });

                    activityCancellationFeedbackFormBinding.recyclerFeedback.setAdapter(feedbackAdapter);

                } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                    Toaster.customToast(jsonObject.optJSONObject("errors").optString("error_text"));
                } else {
                    Toaster.customToast(getResources().getString(R.string.socket_timeout));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }, error -> {
            error.printStackTrace();
            loaderView.hideLoader();
            Toaster.customToast(getString(R.string.socket_timeout));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                Log.e("Param", param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void getBookingCancelled() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.CANCEL_BOOKING, response -> {
            loaderView.hideLoader();
            Log.d("cancelBooking", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("api_text").equalsIgnoreCase("success")) {
                    cancelAlertDialog(jsonObject.getString("message"));
                } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                    Toaster.customToast(jsonObject.optJSONObject("errors").optString("error_text"));
                } else {
                    Toaster.customToast(getResources().getString(R.string.socket_timeout));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }, error -> {
            error.printStackTrace();
            loaderView.hideLoader();
            Toaster.customToast(getString(R.string.socket_timeout));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("question_id", feedbackOption);
                param.put("feedbabk_teext", feedbackText);
                param.put("booking_id", bookingId);
                /*..if cocah is cancelled the booking the main amount got to...in refund amount..*/
                param.put("refund_amount", refund_amount + "");
                Log.e("Param", param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private boolean isValidate() {
        boolean isOk = true;
        if (lastCheckedRB == -1) {
            Toaster.customToastDown(getResources().getString(R.string.Please_choose_option));
            isOk = false;
        } else if (lastCheckedRB == feedbackModelList.size() - 1) {
            if (activityCancellationFeedbackFormBinding.editTypeFeedback.getText().toString().trim().isEmpty()) {
                Toaster.customToastDown(getResources().getString(R.string.Please_type_your_feedback));
                isOk = false;
            } else if (!isTermsPolicyChecked) {
                Toaster.customToastDown(getResources().getString(R.string.Please_check_to_accept_the_Cancellation_Policy));
                isOk = false;
            }
        } else if (!isTermsPolicyChecked) {
            Toaster.customToastDown(getResources().getString(R.string.Please_check_to_accept_the_Cancellation_Policy));
            isOk = false;
        }
        return isOk;
    }

    private void cancelAlertDialog(String message) {
        final Dialog dialog = new Dialog(CancellationFeedbackFormActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_cancel_confirmation);
        dialog.setCancelable(false);
        TextView tv_cancel_confirmed_msg = dialog.findViewById(R.id.tv_cancel_confirmed_msg);
        tv_cancel_confirmed_msg.setText(message);
        FrameLayout fl_no = dialog.findViewById(R.id.fl_done);
        fl_no.setOnClickListener(v -> {
            dialog.dismiss();

            Intent intent = new Intent(CancellationFeedbackFormActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        dialog.show();
    }
}