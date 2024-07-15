package com.pb.criconet.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import com.google.android.material.snackbar.Snackbar;
import com.pb.criconet.AGApplication;
import com.pb.criconet.R;
import com.pb.criconet.model.AmbassadorModel;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;


public class Global {

    public static final String SOCKETURL="https://chat.criconetonline.com";
    public static final String CREATE_WATCH_PARTY="https://chat.criconetonline.com/createWatchParty";
    public static final String GET_EMOJI_LIST ="https://www.criconet.com/app_api.php?type=get_emoji_list";
    public static final String GET_LIVE_STREAMING_MATCH_DETAILS="get_live_streaming_match_details";


    public static final String UPCOMING_MATCH_IPL = "get_fantasy_match_list";
    public static final String JOIN_FANTASY_MATCH_CONTEST = "join_fatasy_match_contest";
    public static final String My_FANTASY_CONTEST = "my_fantasy_contest";
    public static final String My_CONTEST_PREVIEW = "contest_team_preview";


    public static final String FILE_UPLOAD_URL = "https://criconetonline.com/app_api.php?type=new_post";
    /*for debug*/
    //public static final String URL = "https://stage.criconetonline.com/app_api.php?type=";
    //public static final String URL_CHAT = "https://stage.criconetonline.com/";
    //public static String GameURL = "https://criconetonline.com/cricket_js/index.php?";

    public static String GET_CAMPUS_AMBASSADOAR = "https://www.criconet.com/campus-ambassador?rst=app";

    /*for live*/
    public static final String URL = "https://www.criconet.com/app_api.php?type=";
    public static final String URL_CHAT = "https://www.criconet.com";
    public static String GameURL = "https://www.criconet.com/cricket_js/index.php?";

    // for video upload testing..
    //public static final String URL = "https://www.criconet.com/app_api.php?type=";
    public static final String BOOKING_URL = "https://stage.criconetonline.com/app_api.php?type=create_booking_order";
    //public static String URL2 = "https://www.criconet.com/";
    public static final String VIDEO_URL = "https://stage.criconetonline.com";// for bebug
    public static final String URL2 = "https://criconet.com"; // for release live

    /*Use this URL when app goes to live*/
    //public static String URL = "https://www.criconet.com/app_api.php?type=";
    /*..........Live URL*/

    public static String websiteURL = "https://criconetonline.com/webviewhome?";
    public static String websiteURL_demo = "https://criconetonline.com/webviewhome?username=dfordharma&password=dharma@123";
    public static String NOTIFICATION_KEY = "key=AIzaSyCKqEUtgG3zQq1JRI3s5bS5H4uk0qXhJB4";
    public static final String SURVEY_FORM_LINK = "https://stage.criconetonline.com/online-survey-form";
    public static final String OTP_VERIFY = "otp_verification";////
    public static final String RESEND_OTP = "resend_otp";
    public static final String VERIFY_MOBILE = "mobile_update";
    public static final String PAYMENT_SUCCESS = "booking_payment_success";
    public static final String ACADEMY_BOOKING_SUCCESS = "academy_booking_success";
    public static final String PROFILE_COMPLETION = "get_profile_score";


    public static final String REGISTER_API ="user_registration";
    public static final String GET_FEEDBACK_FORM = "get_cancel_feedback_form";
    public static final String CANCEL_BOOKING = "cancel_booking";
    public static final String GET_SPECIALITIES = "get_specialities_cat";
    public static final String GET_SESSION_FEEDBACKFORM = "get_session_feedback_form";
    public static final String SUBMIT_END_SESSION_FEEDBACK = "post_session_feedback_form";
    public static final String getOffet = "get_offer";
    public static final String coupan_applay = "coupon_price_cal";
    public static final String check_coupon_code = "check_coupon_code";
    public static final String GET_APP_SETTINGS = "get_app_setting";
    public static final String GET_CHAT_NOTIFICATION = "get_message_notifications";
    public static final String UPLOAD_VIDEO = "recording_upload";
    public static final String GET_RECORDED_VIDEO = "get_recording_video";
    public static final String GET_BOOKING_DETAILS = "get_booking_details";
    public static final String GET_REFERRAL_CODE = "generate_referal_code";
    public static final String GET_CAMPUS_AMBASSADOR = "reg_campus_ambassador";
    public static final String GET_LANGUAGE = "get_languages";
    public static final String UPDATE_COACH_PERSONAL_INFO = "update_personal_info";
    public static final String GET_SPECIALITIES_CATEGORY = "get_specialities_cat";
    public static final String ADD_COACH_QUALIFICATION = "update_coach_spec_exp";
    public static final String REMOVE_CERTIFICATE = "remove_certificate";
    public static final String SESSION_CLOSE_TIME = "session_close_time";
    public static final String ACADEMY_LIST = "academy_lists";
    public static final String ADD_ACADEMY = "add_academy";
    public static final String GET_ACADEMY_SLOT = "get_academy_slots";
    public static final String UPDATE_ACADEMY_SLOT = "update_academy_slots";
    public static final String SEND_LIVE_STREAMING_BOOKING_REQUEST = "live_streaming_booking_req";

    /*Academy Panel APi*/
    public static final String ACADEMY_ADD_STUDENTS = "my_academy_add_student";
    public static final String ACADEMY_STUDENT_LIST = "my_academy_student_list";
    public static final String ADD_ACADEMY_COACH = "my_academy_add_coach";
    public static final String ACADEMY_COACH_LIST = "my_academy_coach_list";
    public static final String ACADEMY_TAKE_ATTENDANCE = "my_academy_take_attendance";
    public static final String ACADEMY_ATTENDANCE_LIST = "my_academy_attendance";
    public static final String ACADEMY_ATTENDANCE_REPORT = "my_academy_attendance_report";
    public static final String ACADEMY_ATTENDANCE_REPORT_BY_STUDENT = "my_academy_attendace_report_by_user";


    public static final String ACADEMY_TRAINING_TIPS = "add_traning_tips";
    public static final String ACADEMY_ADD_GALLERY = "add_academy_gallery";
    public static final String TRAINING_TIPS_LIST = "traning_tips_list";
    public static final String GET_ACADEMY_DETAILS = "academy_details";
    public static final String ACADEMY_ENQUIRY = "academy_enquiry";
    public static final String ACADEMY_ROLE_LIST = "get_academy_list_role";
    public static final String ACADEMY_CRETATE_SCHEDULE_MEETING = "create_schedule_metting";
    public static final String ACADEMY_GET_SCHEDULE_MEETING_LIST = "get_schedule_metting";

    //........Cricket..Live..Scoring..Api..............//
    public static final String GET_CRICKET_LIVE_SCORING = "get_cricket_live_scoring";
    public static final String DELETE_ACADEMY_GALLERY ="delete_academy_gallery_files";
    public static final String DELETE_ACADEMY_TIPS ="delete_academy_tips";
    public static final String DELETE_RECORDING_VIDEO ="delete_recording_video";
    public static final String SCREEN_SHARED_NOTIFY="screen_shared_notify";

    //.......Live Streaming..........

    public static final String GET_LIVE_STREAMING_MATCH="get_live_streaming_matches";

    public static final String ADD_LIVE_STREAMING_COMMENT = "add_live_streaming_match_comments";
    public static final String GET_LIVE_STREAMING_COMMENT ="get_live_streaming_match_comments";

    //.......Cart Api..............
    public static final String ADD_CART="add_to_cart";
    public static final String CART_LIST="cart_list";
    public static final String REMOVE_CART="remove_from_cart";
    public static final String CART_CHECKOUT="cart_checkout";
    public static final String CART_PAYMENT_SUCCESS="cart_payment_success";

    // ... Notificatio Api..
    public static final String GET_NOTIFICATION="get_notifications";

    //.... search user by tag @
    public static final String SEARCH_USER_LIST_API="search_public_users";


    //Blog Api

    public static final String GET_BLOG ="get_blogs";


    public static final int TYPE_VIDEO = 0,
            TYPE_IMAGE = 1, TYPE_MULTI_IMAGE = 2,
            TYPE_TEXT = 3, TYPE_LINK = 4,
            TYPE_YOUTUBE = 5;
    public static final String POST_TYPE_VIDEO = "video",
            POST_TYPE_IMAGE = "image", POST_TYPE_MULTI_IMAGE = "photo_multi",
            POST_TYPE_TEXT = "text", POST_TYPE_LINK = "link",
            POST_TYPE_YOUTUBE = "youtube";

    public static final int POST_PRIVACY_PUBLIC = 0, POST_PRIVACY_PRIVATE = 3;
    public static final int PRIVACY_EVERYONE = 0, PRIVACY_PEOPLE_I_FOLLOW = 1, PRIVACY_MY_FOLLOWERS = 2,
            PRIVACY_ONLY_ME = 3;

    public static final String NO_INTERNET_CONNECTION = "Please check your internet connection!";
    public static Map Notification_data;
    Context context;

    public static boolean isSaved;

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
            return true;
        else
            return false;
    }

    public static boolean isValidEmailAddress(String email) {

        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();

    }

    public static boolean validateName(String firstrname) {
        return firstrname.length() > 0;
    }

    public static boolean validateLength(String name, int len) {
        return name.length() >= len;
    }

    public static boolean validateLengthofCoachRegister(String name) {
        if (name.length() < 3 || name.length() > 32) {
            name.length();
            return false;
        }
        return true;
    }

    public static boolean validateLengthofCoachRegisterProfileTitle(String name) {
        if (name.length() < 8 || name.length() > 40) {
            name.length();
            return false;
        }
        return true;
    }

    public static boolean validateLengthofCoachRegisterr(String name) {
        if (name.length() < 3 || name.length() > 32) {
            return false;
        }
        return true;
    }

    public static boolean validateLengthofAcademy(String name) {
        if (name.length() < 4) {
            return false;
        }
        return true;
    }

    public static boolean validateLengthOfNameOfBookingRequest(String name) {
        if (name.length() < 3 || name.length() > 30) {
            return false;
        }
        return true;
    }

    public static boolean password(String firstrname) {
        return firstrname.length() >= 6;
    }

    public static boolean ValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean ValidEmailBook(String target) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(target).matches();
    }

    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return email.matches(EMAIL_PATTERN);
    }

    public static boolean isValidPhoneNumber(String testString) {
        return (testString.length() >= 8 && testString.length() <= 10 && android.util.Patterns.PHONE.matcher(testString).matches());
    }

    public static boolean isValidPhoneNumberAcademy(String testString) {

        if (testString.isEmpty()) {
            return true;
        } else {
            return (testString.length() >= 8 && testString.length() <= 10 && android.util.Patterns.PHONE.matcher(testString).matches());
        }


    }

    public static boolean isValidPincode(String testString) {
        return (testString.length() == 6);
    }

    public static boolean isValidAddress(String testString) {
        return (testString.length() > 3);
    }

    public static boolean isAboutDetails(String testString) {
        return (testString.length() >= 3);
    }

    public static boolean isValidMobile(String phone) {
        if (!Pattern.matches(" \"^\\\\s*(?:\\\\+?(\\\\d{1,3}))?[-. (]*(\\\\d{3})[-. )]*(\\\\d{3})[-. ]*(\\\\d{4})(?: *x(\\\\d+))?\\\\s*$\"+", phone)) {
            return phone.length() == 10;
            // phone.length() >=8 &&
        }
        return false;
    }

    public static boolean isValidAbout(String testString) {
        return (testString.length() >= 8);
    }

    public static boolean isShortDes(String testString) {
        /* max limit 80 removed due to chennai academy visit*/
        return (testString.length() > 3);
    }

    public static boolean isValidShortDes(String testString) {
        return (testString.length() >= 18);
    }

    public static boolean isValidFees(Float testString) {
        return (testString >= 0);
    }

    public static String capitalizeFirstLatterOfString(String name) {
        if (name == null) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder(name);
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            return sb.toString();
        }

    }

    public static void msgDialog(Activity ac, String msg) {
        try {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(ac);
            alertbox.setTitle(ac.getResources().getString(R.string.app_name));
            alertbox.setMessage(Html.fromHtml(msg));
            alertbox.setPositiveButton(ac.getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            arg0.dismiss();
                          // ac.finish();

                        }
                    });
            alertbox.show();
        } catch (Exception e) {
        }
    }

    /*edit profile dialog*/
    public static void msgDialogEdit(Activity ac, String msg) {
        try {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(ac);
            alertbox.setTitle(ac.getResources().getString(R.string.app_name));
            alertbox.setMessage(Html.fromHtml(msg));
            alertbox.setPositiveButton(ac.getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            // ac.finish();

                        }
                    });
            alertbox.show();
        } catch (Exception e) {
        }
    }

    public static void msgDialogTips(Activity ac, String msg) {
        try {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(ac);
            alertbox.setTitle(ac.getResources().getString(R.string.app_name));
            alertbox.setMessage(Html.fromHtml(msg));
            alertbox.setPositiveButton(ac.getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                           // ac.finish();

                        }
                    });
            alertbox.show();
        } catch (Exception e) {
        }
    }

//    public static void logoutt(Activity ac) {
//        RequestQueue queue = Volley.newRequestQueue(ac);
//        CustomLoaderView  loaderView = CustomLoaderView.initialize(ac);
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ac);
//        final JSONObject json = new JSONObject();
////        try {
////            json.put("user_id", SessionManager.get_user_id(prefs));
////            json.put("s", SessionManager.get_session_id(prefs));
////            //Log.e(" data  : ", "" + json);
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
//        loaderView.showLoader();
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.POST, Global.URL + "logout", json,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.v("logout reponse", "" + response);
////                        {"status":"Success"}
//                        loaderView.hideLoader();
//                        try {
//                            JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
//                            if (jsonObject.getString("api_status").equals("200")) {
//                                SessionManager.dataclear(prefs);
//                                SessionManager.save_check_agreement(prefs, true);
//                                Intent intent = new Intent(ac, Login.class);
//                                ac.startActivity(intent);
//                                ac.finish();
//                            } else if (jsonObject.getString("api_status").equals("400")) {
//                                Toaster.customToast(jsonObject.optJSONObject("errors").optString("error_text"));
//                            } else {
//                                Global.msgDialog(ac, ac.getResources().getString(R.string.error_server));
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//                loaderView.hideLoader();
//                Global.msgDialog(ac, ac.getResources().getString(R.string.error_server));
//            }
//        });
//        int socketTimeout = 30000;
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        jsonObjectRequest.setRetryPolicy(policy);
//        queue.add(jsonObjectRequest);
//    }

    public static void msgDialogg(Context ac, String msg) {
        try {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(ac);
            alertbox.setTitle(ac.getResources().getString(R.string.app_name));
            alertbox.setMessage(Html.fromHtml(msg));
            alertbox.setPositiveButton(ac.getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });
            alertbox.show();
        } catch (Exception e) {
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

    public static void showDialog(final Activity ac) {
        android.app.AlertDialog.Builder alertbox = new android.app.AlertDialog.Builder(ac);
        alertbox.setTitle(ac.getResources().getString(R.string.app_name));
        alertbox.setMessage(R.string.no_internet);
        alertbox.setPositiveButton(ac.getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        //arg0.cancel();
                        ac.finish();
                    }
                });
        if (!ac.isFinishing()) {
            //show dialog
            alertbox.show();
        }


    }

    public static void showAlertDialog(Context context, String title, String body, DialogInterface.OnClickListener okListener) {

        if (okListener == null) {
            okListener = (dialog, which) -> dialog.cancel();
        }

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context)
                .setMessage(body).setPositiveButton("OK", okListener);

        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }

        builder.show();
    }

    public static void showDialog(final Activity ac, String msg) {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(ac);
        alertbox.setTitle(ac.getResources().getString(R.string.app_name));
        alertbox.setMessage(msg);
        alertbox.setPositiveButton(ac.getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
        alertbox.show();
    }

    public static void showSettingsDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings(activity);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    public static void Sleep(int sec) {
        try {
            //set time in mili
            Thread.sleep(sec * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // navigating user to app settings
    private static void openSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, 101);
    }

    public static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public static Bitmap CheckRotationImage(String selectedImage) {

        String picturePath = selectedImage;
        Bitmap loadedBitmap = BitmapFactory.decodeFile(picturePath);

        ExifInterface exif = null;
        try {
            File pictureFile = new File(picturePath);
            exif = new ExifInterface(pictureFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        int orientation = ExifInterface.ORIENTATION_NORMAL;

        if (exif != null)
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                loadedBitmap = rotateBitmap(loadedBitmap, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                loadedBitmap = rotateBitmap(loadedBitmap, 180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                loadedBitmap = rotateBitmap(loadedBitmap, 270);
                break;
        }
        return loadedBitmap;
    }

    public static Bitmap rotateImage(Bitmap bmp, String imageUrl) {
        if (bmp != null) {
            ExifInterface ei;
            int orientation = 0;
            try {
                ei = new ExifInterface(imageUrl);
                orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                // e.printStackTrace();
            }

            int bmpWidth = bmp.getWidth();
            int bmpHeight = bmp.getHeight();
            Matrix matrix = new Matrix();

            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.postRotate(270);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.postRotate(90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.postRotate(180);
                    break;

                default:
                    break;
            }

            Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight, matrix, true);

            return resizedBitmap;

        } else {
            return bmp;
        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }


        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    public static String returnDate(String dateToConvert) {
        Date date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy MM dd, hh:mm a");
        //2022 05 10, 01:53 PM
        String temp = dateToConvert;
        try {
            date = formatter.parse(temp);
            Log.e("formated date ", date + "");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formateDateMonthName = new SimpleDateFormat("dd MMM yyyy").format(date);
        Log.v("output_date ", formateDateMonthName);
        String formateTime = new SimpleDateFormat("hh:mm a").format(date);
        Log.v("output_time ", formateDateMonthName + "--" + formateTime);
        String finalDateTime = formateDateMonthName;

        return formateDateMonthName + " , " + formateTime;
    }

    public static String validateDateFormat(String dateToValdate) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyyy, HH:mm");
        //To make strict date format validation
        formatter.setLenient(false);
        Date parsedDate = null;
        try {
            parsedDate = formatter.parse(dateToValdate);

        } catch (ParseException e) {
            //Handle exception
        }
        System.out.println("++validated DATE TIME ++" + formatter.format(parsedDate));
        String formateDateMonthName = new SimpleDateFormat("EEEE , dd MMM yyyy").format(parsedDate);
        // Log.v("output_date ", formateDateMonthName);
        String formateTime = new SimpleDateFormat("hh:mm a").format(parsedDate);
        // Log.v("output_time ", formateDateMonthName + "," + formateTime);
        String finalDateTime = formateDateMonthName + " , " + formateTime;
        return finalDateTime;
    }

    public static String validateDateFormatt(String dateToValdate) {
        //2022-04-16
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate = null;
        try {
            parsedDate = formatter.parse(dateToValdate);

        } catch (ParseException e) {
            //Handle exception
        }
        //System.out.println("++validated DATE TIME ++" + formatter.format(parsedDate));
        String formateDateMonthName = new SimpleDateFormat("EEEE , dd MMM yyyy").format(parsedDate);
//        // Log.v("output_date ", formateDateMonthName);
//        String formateTime = new SimpleDateFormat("hh:mm a").format(parsedDate);
//        // Log.v("output_time ", formateDateMonthName + "," + formateTime);
//        String finalDateTime = formateDateMonthName + " , " + formateTime;
        return formateDateMonthName;
    }


    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month - 0];
    }


//    public static ProgressDialog getProgressDialog(Context context, String message, Boolean canceleable) {
//        ProgressDialog dialog = new ProgressDialog(context);
//        dialog.setMessage(message);
//        dialog.setCancelable(canceleable);
//        dialog.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.loading_placeholder));
//        dialog.show();
//        return dialog;
//    }

    public static void dismissDialog(ProgressDialog dialog) {
        if (dialog != null && dialog.isShowing())
            dialog.cancel();
    }

    public static Calendar convertStringToCalendar(String time) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateformat.parse(time);
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            return calendar;
        }
    }

    public static String getDateGot(String dateTime) {
        Date date = null;
        String realdate = null;
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
        try {
            date = formatter.parse(dateTime);
            realdate = new SimpleDateFormat("yyyy-MM-dd").format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return realdate;
    }

    public static String getDateGotCoach(String dateTime) {
        Date date = null;
        String realdate = null;
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
        try {
            date = formatter.parse(dateTime);
            realdate = new SimpleDateFormat("dd-MM-yyyy").format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return realdate;
    }

    public static String getDateGotCoachh(String dateTime) {
        Date date = null;
        String realdate = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = formatter.parse(dateTime);
            realdate = new SimpleDateFormat("dd-MMM-yyyy").format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return realdate;
    }

    public static String getDateGott(String dateTime) {
        Date date = null;
        String realdate = null;
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
        try {
            date = formatter.parse(dateTime);
            realdate = new SimpleDateFormat("yyyy-MM-dd").format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return realdate;
    }

    public static String getMonth(String dateTime) {
        Date date = null;
        String realdate = null;
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
        try {
            date = formatter.parse(dateTime);
            realdate = new SimpleDateFormat("MM").format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return realdate;
    }

    public static String getYear(String dateTime) {
        Date date = null;
        String realdate = null;
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
        try {
            date = formatter.parse(dateTime);
            realdate = new SimpleDateFormat("yyyy").format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return realdate;
    }

    public static String getDay(String dateTime) {
        Date date = null;
        String realdate = null;
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
        try {
            date = formatter.parse(dateTime);
            realdate = new SimpleDateFormat("dd").format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return realdate;
    }

    public static Drawable getThreeDots(Context context) {
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.sample_three_icons);
        //Add padding to too large icon
        return new InsetDrawable(drawable, 100, 0, 100, 0);
    }

    public static Drawable getThreeDotss(Context context) {
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.sample_three_iconss);
        //Add padding to too large icon
        return new InsetDrawable(drawable, 100, 0, 100, 0);
    }

    public static int getIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public static void showSnackbar(View view, String msg) {
        Snackbar snackbar = Snackbar
                .make(view, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static String convertUTCDateToLocalDate(String date_string) {
        if (date_string.isEmpty()) {
            return "";
        }

        SimpleDateFormat oldFormatter = new SimpleDateFormat("yyyy-MM-dd");
        //oldFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value = null;
        String dueDateAsNormal = "";
        try {
            value = oldFormatter.parse(date_string);
            SimpleDateFormat newFormatter = new SimpleDateFormat("dd MMMM yyyy");

           /* Date date = new SimpleDateFormat("yyyy-M-d").parse(date_string);
            String dayOfWeek = new SimpleDateFormat("EEE", Locale.ENGLISH).format(newFormatter);

            Log.d("day",dayOfWeek);*/
            newFormatter.setTimeZone(TimeZone.getDefault());
            dueDateAsNormal = newFormatter.format(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("date", dueDateAsNormal);

        return dueDateAsNormal;
    }

    public static String convertUTCDateToLocalDatee(String date_string) {
        if (date_string.isEmpty()) {
            return "";
        }
        SimpleDateFormat oldFormatter = new SimpleDateFormat("dd MMM yyyy,hh:mm:ss");
        Date value = null;
        String dueDateAsNormal = "";
        try {
            value = oldFormatter.parse(date_string);
            SimpleDateFormat newFormatter = new SimpleDateFormat("dd MMM yyyy, h:mm a");
            newFormatter.setTimeZone(TimeZone.getDefault());
            dueDateAsNormal = newFormatter.format(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dueDateAsNormal;
    }

    public static String convertUTCDateToLocal(String date_string) {
        if (date_string.isEmpty()) {
            return "";
        }
        //02 Jun 2021,01:20:00
        SimpleDateFormat oldFormatter = new SimpleDateFormat("yyyy-mm-dd");
        //oldFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value = null;
        String dueDateAsNormal = "";
        try {
            value = oldFormatter.parse(date_string);
            SimpleDateFormat newFormatter = new SimpleDateFormat("dd MMM yyyy");

           /* Date date = new SimpleDateFormat("yyyy-M-d").parse(date_string);
            String dayOfWeek = new SimpleDateFormat("EEE", Locale.ENGLISH).format(newFormatter);

            Log.d("day",dayOfWeek);*/
            newFormatter.setTimeZone(TimeZone.getDefault());
            dueDateAsNormal = newFormatter.format(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("date", dueDateAsNormal);

        return dueDateAsNormal;
    }

    public static String convertUTCDateToLocall(String date_string) {
        if (date_string.isEmpty()) {
            return "";
        }
        //02 Jun 2021,01:20:00
        //oldFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dueDateAsNormal = "";
        try {
            SimpleDateFormat dateFormatprev = new SimpleDateFormat("yyyy-MM-dd");
            Date d = dateFormatprev.parse(date_string);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
            dueDateAsNormal = dateFormat.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("date", dueDateAsNormal);

        return dueDateAsNormal;
    }

    public static void printKeyHash(Activity context) {
        // Add code to print out the key hash
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo("com.pb.criconet", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("KeyHash:", e.toString());
        }
    }

    public static void showKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);
        }
    }

    public static String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd MM yyyy", Locale.getDefault());
        return df.format(c);
    }

    public static String getCurrentDatee() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return df.format(c);
    }

    public static String getCurrentDateAcademyPanel() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return df.format(c);
    }

    public static String capitizeString(String name) {
        String captilizedString = "";
        try {

            if (!name.trim().equals("")) {
                captilizedString = name.substring(0, 1).toUpperCase() + name.substring(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return captilizedString;
    }

    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("dd MMM yyyy", cal).toString();
        return date;
    }

    public static String convertSecondsToHMmSs(long seconds) {

        long s = seconds % 60;

        long m = (seconds / 60) % 60;

        long h = (seconds / (60 * 60)) % 24;

        return String.format("%d:%02d:%02d", h, m, s);

    }

    @SuppressLint("DefaultLocale")
    public static String convertSecondsTomSs(long seconds) {

        long s = seconds % 60;

        long m = (seconds / 60) % 60;

        //long h = (seconds / (60 * 60)) % 24;

        return String.format("%02d:%02d", m, s);

    }

    //2021-06-28 16:00:00
    public static String getCurrentDateAndTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    public static String getURLForResource(int resourceId) {
        //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
        return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + resourceId).toString();
    }


    /*new design code method*/
    public static void addReadMore(final String text, final TextView textView) {
        Typeface typeface = ResourcesCompat.getFont(AGApplication.getContext(), R.font.opensans_bold);
        SpannableString ss = new SpannableString(text.substring(0, 70) + "... read more");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                addReadLess(text, textView);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setTypeface(typeface);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ds.setColor(AGApplication.getContext().getResources().getColor(R.color.purple_700, AGApplication.getContext().getTheme()));
                } else {
                    ds.setColor(AGApplication.getContext().getResources().getColor(R.color.purple_700));
                }
            }
        };
        ss.setSpan(clickableSpan, ss.length() - 13, ss.length() , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static void addReadLess(final String text, final TextView textView) {
        Typeface typeface = ResourcesCompat.getFont(AGApplication.getContext(), R.font.opensans_bold);
        SpannableString ss = new SpannableString(text+"...read less");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                addReadMore(text, textView);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setTypeface(typeface);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ds.setColor(AGApplication.getContext().getResources().getColor(R.color.purple_700, AGApplication.getContext().getTheme()));
                } else {
                    ds.setColor(AGApplication.getContext().getResources().getColor(R.color.purple_700));
                }
            }
        };
        ss.setSpan(clickableSpan, ss.length()-12, ss.length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static void hideKeyBoard(Activity mContext){
        View view = mContext.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)AGApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showKeyBoard(){
        InputMethodManager imm = (InputMethodManager) AGApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    public static boolean keyboardShown(View rootView) {

        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }


    public static String convertUTCDateToMM(String date_string) {
        if (date_string.isEmpty()) {
            return "";
        }
        //02 Jun 2021,01:20:00
        //oldFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dueDateAsNormal = "";
        try {
            SimpleDateFormat dateFormatprev = new SimpleDateFormat("yyyy-MM-dd");
            Date d = dateFormatprev.parse(date_string);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
            dueDateAsNormal = dateFormat.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("date", dueDateAsNormal);

        return dueDateAsNormal;
    }

    public static ArrayList<AmbassadorModel> getAmbassadorList(){
        ArrayList<AmbassadorModel> ambassadorModels1 = new ArrayList<>();
        ambassadorModels1.add(new AmbassadorModel("One Free\n" +
                "e-Coaching",R.drawable.online_free_e_coaching));
        ambassadorModels1.add(new AmbassadorModel("T-shirt",R.drawable.tshirt));
        ambassadorModels1.add(new AmbassadorModel("Water Bottle",R.drawable.bottale));
        ambassadorModels1.add(new AmbassadorModel("Cricket Ball",R.drawable.cricket_ball));
        ambassadorModels1.add(new AmbassadorModel("Cricket Pads",R.drawable.cricket_pad));
        ambassadorModels1.add(new AmbassadorModel("Cricket Bat",R.drawable.cricket_bat));
        ambassadorModels1.add(new AmbassadorModel("Amazon Gift\n" +
                "Voucher",R.drawable.amazon_gift));
        return ambassadorModels1;
    }

    public static ArrayList<String> getTakeAttendanceList(){
        ArrayList<String> ambassadorModels1 = new ArrayList<>();
        ambassadorModels1.add("Neil Seth");
        ambassadorModels1.add("Akshay Mittal");
        ambassadorModels1.add("Kumar");
        ambassadorModels1.add("Manik Singh");
        ambassadorModels1.add("Bedi Nayak");
        ambassadorModels1.add("Sumit Singh");
        ambassadorModels1.add("Neil Seth");
        ambassadorModels1.add("Akshay Mittal");
        ambassadorModels1.add("Kumar");
        ambassadorModels1.add("Manik Singh");
        ambassadorModels1.add("Bedi Nayak");
        ambassadorModels1.add("Sumit Singh");

        return ambassadorModels1;
    }

    public static void callApp(String number,Context mContext){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+number));
        mContext.startActivity(callIntent);
    }



}
