package com.pb.criconet.Activity.Coach;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.pb.criconet.Activity.User.UserBookingDetails;
import com.pb.criconet.Activity.User.UserBookingHistory;
import com.pb.criconet.R;
import com.pb.criconet.databinding.ActivityCheckoutDetailsBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.model.Coaching.BookingPaymentsDetails;
import com.pb.criconet.model.Coaching.CoachDetails;
import com.pb.criconet.model.Coaching.OrderCreate;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.SessionManager;
import com.pb.criconet.util.Toaster;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CheckoutDetails extends AppCompatActivity implements PaymentResultListener {

    ActivityCheckoutDetailsBinding activityCheckoutDetailsBinding;
    Context mContext;
    Activity mActivity;

    long MILI_SECONDS;
    private CoachDetails modelArrayList;
    private OrderCreate ordercreate;
    String dateGott = "", mslotId = "";
    CustomLoaderView loaderView;
    String coupon_text = "", coupon_status = "", coupon_id = "", tax = "", subtotal = "", payAmount = "";
    private SharedPreferences prefs;
    private RequestQueue queue;
    int payableAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCheckoutDetailsBinding = ActivityCheckoutDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityCheckoutDetailsBinding.getRoot());

        mContext = this;
        mActivity = this;
        loaderView = CustomLoaderView.initialize(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        queue = Volley.newRequestQueue(mContext);

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityCheckoutDetailsBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.checkoutdetails));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        modelArrayList = (CoachDetails) getIntent().getSerializableExtra("key");
        ordercreate = (OrderCreate) getIntent().getSerializableExtra("key1");
        dateGott = getIntent().getStringExtra("booking_slot_date");
        mslotId = getIntent().getStringExtra("booking_slot_id");
        payableAmount = ordercreate.getPaymentOption().getAmount();

        initView();


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {

        Glide.with(this).load(modelArrayList.getData().getAvatar()).into(activityCheckoutDetailsBinding.ivRoundedProfile);
        activityCheckoutDetailsBinding.tvCoachName.setText(modelArrayList.getData().getName());
        activityCheckoutDetailsBinding.tvCoachExp.setText(modelArrayList.getData().getExps());
        activityCheckoutDetailsBinding.tvCoachPrice.setText(modelArrayList.getData().getPrice().getCoachPrice());
        activityCheckoutDetailsBinding.tvSessiondate.setText(Global.convertUTCDateToLocall(ordercreate.getPaymentOption().getSessionDate()));

        if (modelArrayList.getData().getPrice().getOfferId().equalsIgnoreCase("0")) {
            activityCheckoutDetailsBinding.tvOfferl.setVisibility(View.GONE);
        } else {
            activityCheckoutDetailsBinding.tvOfferP.setVisibility(View.VISIBLE);
            activityCheckoutDetailsBinding.tvCoachPrice.setPaintFlags(activityCheckoutDetailsBinding.tvCoachPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            activityCheckoutDetailsBinding.tvOfferP.setText(mContext.getString(R.string.offer_price) + " " + "\u20B9" + modelArrayList.getData().getPrice().getPaymentPrice() + " " + "/" + mContext.getString(R.string.session));
        }


        activityCheckoutDetailsBinding.tvSessionTime.setText(ordercreate.getPaymentOption().getSessionTime());


        activityCheckoutDetailsBinding.tvSessionprice.setText(getResources().getString(R.string.price) + "\u20B9" + modelArrayList.getData().getPrice().getCoachPrice());
        activityCheckoutDetailsBinding.tvTaxes.setText(getResources().getString(R.string.texex) + "\u20B9" + modelArrayList.getData().getPrice().getTaxesAmount());
        activityCheckoutDetailsBinding.tvTotalPayableAmount.setText(getResources().getString(R.string.total_payable_amount) + "\u20B9" + modelArrayList.getData().getPrice().getWithTaxesAmount());
        activityCheckoutDetailsBinding.tvSubTotal.setText(getResources().getString(R.string.subtotal) + "\u20B9" + modelArrayList.getData().getPrice().getPaymentPrice());


        activityCheckoutDetailsBinding.tvSessionprice.setPaintFlags(activityCheckoutDetailsBinding.tvSessionprice.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        activityCheckoutDetailsBinding.tvSubTotal.setPaintFlags(activityCheckoutDetailsBinding.tvSubTotal.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        activityCheckoutDetailsBinding.tvTaxes.setPaintFlags(activityCheckoutDetailsBinding.tvTaxes.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        activityCheckoutDetailsBinding.tvTotalPayableAmount.setPaintFlags(activityCheckoutDetailsBinding.tvTotalPayableAmount.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        activityCheckoutDetailsBinding.flProceedToPay.setOnClickListener(v -> {

            try {
                if (payableAmount == 0) {
                    Intent intent = new Intent(mContext, UserBookingHistory.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (coupon_status.equalsIgnoreCase("apply")) {
                        if (Global.isOnline(mActivity)) {
                            BookCoach();
                        } else {
                            Global.showDialog(mActivity);
                        }
                    } else {
                        if (Global.isOnline(mActivity)) {
                            startPayment();
                        } else {
                            Global.showDialog(mActivity);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        });

        activityCheckoutDetailsBinding.bookingConLayout.ivClose.setOnClickListener(v -> {
            activityCheckoutDetailsBinding.bookingConLayout.getRoot().setVisibility(View.GONE);
        });

        activityCheckoutDetailsBinding.bookingFailedLayout.ivClose.setOnClickListener(v -> {
            activityCheckoutDetailsBinding.bookingFailedLayout.getRoot().setVisibility(View.GONE);
        });


        activityCheckoutDetailsBinding.editApplyCoupon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityCheckoutDetailsBinding.flCouponSubmit.setVisibility(View.VISIBLE);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityCheckoutDetailsBinding.flCouponSubmit.setVisibility(View.VISIBLE);

                } else if (s.length() == 0) {
                    // Toaster.customToast(s.length()+"");
                    activityCheckoutDetailsBinding.flCouponSubmit.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityCheckoutDetailsBinding.flCouponSubmit.setVisibility(View.VISIBLE);

            }
        });

        activityCheckoutDetailsBinding.flCouponSubmit.setOnClickListener(v -> {

            coupon_text = activityCheckoutDetailsBinding.editApplyCoupon.getText().toString().trim();
            if (coupon_text.equalsIgnoreCase("")) {
                Toaster.customToast(getResources().getString(R.string.enter_coupon_code));
            } else {

                if (Global.isOnline(mActivity)) {
                    checkCouponCode();
                } else {
                    Global.showDialog(mActivity);
                }
            }

            // startActivity(new Intent(mContext, CoachFeedBackReviewActivity.class));
        });

        activityCheckoutDetailsBinding.flCouponRemove.setOnClickListener(v -> {
            coupon_status = "remove";
            if (Global.isOnline(mActivity)) {
                applyCoupon();
            } else {
                Global.showDialog(mActivity);
            }
        });


    }

    private void applyCoupon() {

        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.coupan_applay, response -> {
            Log.d("Payment Response", response);
            loaderView.hideLoader();
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("api_text").equalsIgnoreCase("success")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                    if (jsonObject1.has("coupon_status")) {
                        coupon_status = jsonObject1.getString("coupon_status");
                    }

                    if (jsonObject1.has("taxes_amount")) {
                        tax = jsonObject1.getString("taxes_amount");
                        activityCheckoutDetailsBinding.tvTaxes.setText(getResources().getString(R.string.texex) + "\u20B9" + jsonObject1.getString("taxes_amount"));
                    }
                    if (jsonObject1.has("payment_amount")) {
                        payAmount = jsonObject1.getString("payment_amount");
                        //activityCheckoutDetailsBinding.tvSubTotal.setText(getResources().getString(R.string.total_payable_amount) + "\u20B9" + jsonObject1.getString("payment_amount"));
                    }
                    if (jsonObject1.has("payment_price")) {
                        subtotal = jsonObject1.getString("payment_price");
                        activityCheckoutDetailsBinding.tvSubTotal.setText(getResources().getString(R.string.subtotal) + "\u20B9" + jsonObject1.getString("payment_price"));
                    }

                    if (jsonObject1.has("coupon_discount")) {

                        if (jsonObject1.getString("coupon_discount").equalsIgnoreCase("")) {
                            activityCheckoutDetailsBinding.tvCouponDis.setVisibility(View.GONE);
                        } else {
                            activityCheckoutDetailsBinding.tvCouponDis.setVisibility(View.VISIBLE);
                            activityCheckoutDetailsBinding.tvCouponDis.setText(getResources().getString(R.string.coupon_discount) + "\u20B9" + jsonObject1.getString("coupon_discount"));
                        }
                    }

                    if (coupon_status.equalsIgnoreCase("apply")) {
                        activityCheckoutDetailsBinding.rlApplycoupon.setVisibility(View.GONE);
                        activityCheckoutDetailsBinding.flCouponRemove.setVisibility(View.VISIBLE);
                    } else {
                        activityCheckoutDetailsBinding.rlApplycoupon.setVisibility(View.VISIBLE);
                        activityCheckoutDetailsBinding.flCouponRemove.setVisibility(View.GONE);
                    }
                    // payableAmount =jsonObject1.getString("payment_amount");
                    payableAmount = Integer.parseInt(jsonObject1.getString("total_payable_amount"));

                    activityCheckoutDetailsBinding.tvTotalPayableAmount.setText(getResources().getString(R.string.total_payable_amount) + "\u20B9" + payableAmount);

                } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                    coupon_status = "";
                    Toaster.customToast(jsonObject.optJSONObject("errors").optString("error_text"));
                } else {
                    Toaster.customToast(getResources().getString(R.string.socket_timeout));
                    coupon_status = "";
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
                param.put("coach_id", modelArrayList.getData().getCoachId());
                param.put("coupon_code", coupon_text);
                param.put("coupon_id", coupon_id);
                param.put("order_id", ordercreate.getPaymentOption().getOrderId());
                param.put("coupon_status", coupon_status);
                Log.d("Param", param.toString());
                return param;
            }
        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void checkCouponCode() {

        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.check_coupon_code, response -> {
            Log.d("check coupon Response", response);
            loaderView.hideLoader();
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("api_text").equalsIgnoreCase("success")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    if (jsonObject1.has("id")) {
                        coupon_id = jsonObject1.getString("id");
                        if (Global.isOnline(mActivity)) {
                            activityCheckoutDetailsBinding.editApplyCoupon.setText("");
                            coupon_status = "apply";
                            applyCoupon();
                        } else {
                            Global.showDialog(mActivity);
                        }

                    }


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
            Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("coupon_code", coupon_text);
                Log.d("Param", param.toString());
                return param;
            }


        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void BookCoach() {
        //loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "create_booking_order", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Booking Response", response);
                //loaderView.hideLoader();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Gson gson = new Gson();
                    ordercreate = gson.fromJson(response, OrderCreate.class);
                    if (ordercreate != null && jsonObject.getString("status").equalsIgnoreCase("200")) {

                        if (ordercreate.getPayment() == 0) {
                            loaderView.showLoader();
                            new Handler().postDelayed(() -> {
                                loaderView.hideLoader();
                                Intent intent = new Intent(mContext, UserBookingHistory.class);
                                startActivity(intent);
                                finish();
                            }, 2000);

                        } else {
                            if (Global.isOnline(mActivity)) {
                                startPayment();
                            } else {
                                Global.showDialog(mActivity);
                            }
                        }

                    } else {
                        if (ordercreate == null) {
                            Toaster.customToast(ordercreate.getErrors().getErrorText());
                        } else {

                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, error -> {
            error.printStackTrace();
            //loaderView.hideLoader();
            Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return (headers != null || headers.isEmpty()) ? headers : super.getHeaders();
            }

            @Override
            protected Map<String, String> getParams() {
//                param.put("booking_date", getDateTime());
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("coach_id", modelArrayList.getData().getCoachId());
                param.put("booking_slot_date", dateGott);
                param.put("booking_slot_id", mslotId);
                param.put("booking_amount", subtotal);
                param.put("payment_amount", payAmount);
                param.put("taxes_amount", tax);
                param.put("offer_id", modelArrayList.getData().getPrice().getOfferId());
                param.put("coupon_code", coupon_text);
                param.put("coupon_id", coupon_id);
                param.put("old_booking_id", String.valueOf(ordercreate.getBookingId()));
                param.put("cuurency_code", "INR");
                Log.d("Param", param.toString());
                return param;
            }

        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }


    public void startPayment() {
        coupon_status = "";
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", ordercreate.getPaymentOption().getName());
            options.put("description", ordercreate.getPaymentOption().getDescription());
            options.put("send_sms_hash", true);
            options.put("allow_rotation", false);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", modelArrayList.getData().getAvatar());
            options.put("currency", ordercreate.getPaymentOption().getCurrency());
            options.put("theme.color", ordercreate.getPaymentOption().getTheme().getColor());
            Log.d("Payableamoou nt", String.valueOf(payableAmount));
            if (coupon_status.equalsIgnoreCase("apply")) {
                options.put("amount", payableAmount);
            } else {
                options.put("amount", payableAmount);
            }

            JSONObject preFill = new JSONObject();
            preFill.put("email", ordercreate.getPaymentOption().getPrefill().getEmail());
            preFill.put("contact", ordercreate.getPaymentOption().getPrefill().getContact());
            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, getResources().getString(R.string.error_in_payment) + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Log.d("RzpId", razorpayPaymentID);
            if (Global.isOnline(mActivity)) {
                sendPaymentSuccess(razorpayPaymentID);
            } else {
                Global.showDialog(mActivity);
            }

        } catch (Exception e) {
            Log.e("RzpError", "Exception in onPaymentSuccess");
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            //Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

    private void sendPaymentSuccess(String razorpayPaymentID) {

        loaderView.showLoader();

        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.PAYMENT_SUCCESS, response -> {
            Log.d("Payment Response", response);
            loaderView.hideLoader();
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("api_text").equalsIgnoreCase("success")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    BookingPaymentsDetails bookingPaymentsDetails = new BookingPaymentsDetails(jsonObject1);


                    MILI_SECONDS = 2000;
                    activityCheckoutDetailsBinding.bookingConLayout.getRoot().setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            activityCheckoutDetailsBinding.bookingConLayout.getRoot().setVisibility(View.GONE);
                            Intent intent = new Intent(mContext, UserBookingDetails.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("Data", (Serializable) bookingPaymentsDetails);
                            intent.putExtra("FROM", "1");
                            //intent.putExtra("PAYLATER", "PAID");
                            startActivity(intent);
                            finish();
                        }
                    }, MILI_SECONDS);


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
            Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("coach_id", modelArrayList.getData().getCoachId());
                param.put("booking_id", String.valueOf(ordercreate.getBookingId()));
                param.put("order_id", ordercreate.getPaymentOption().getOrderId());
                param.put("razorpay_payment_id", razorpayPaymentID);
                param.put("razorpay_signature", "kjqdka");
                Log.d("Param", param.toString());
                return param;
            }

        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}