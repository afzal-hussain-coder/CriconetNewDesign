package com.pb.criconetnewdesign.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.pb.criconetnewdesign.IntroSlider.IntroScreen;
import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.util.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

public class SplashScreen extends AppCompatActivity {
    public static final String TAG = SplashScreen.class.getSimpleName();
    SharedPreferences prefs;
    boolean permission = false;
    int PERMISSION_ALL = 1;
    Context mContext;
    String type = "", booking_id = "", post_id = "", academy_id = "", matchId = "";
    String party_grpup_txt = "", party_grpup_id = "";
    String create = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
//        Fresco.initialize(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(SplashScreen.this);
        //calculateHashKey("com.pb.criconet");
        Log.d("DeviceToken", SessionManager.get_devicetoken(prefs));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("type")) {
                type = bundle.getString("type");
            }
            if (bundle.containsKey("booking_id")) {
                booking_id = bundle.getString("booking_id");
            }
            if (bundle.containsKey("post_id")) {
                post_id = bundle.getString("post_id");
            }
            if (bundle.containsKey("academy_id")) {
                academy_id = bundle.getString("academy_id");
            }

            if (bundle.containsKey("match_id")) {
                matchId = bundle.getString("match_id");
            }
            Log.d("academy_idd", academy_id + "/" + type);


            Log.d("SpashNo", type + "/" + booking_id + "/" + matchId);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // only for gingerbread and newer versions
            if (!permission) {
                if (checkAndRequestPermissions()) {
                    // carry on the normal flow, as the case of  permissions  granted.
                    sendIntent();
                    permission = true;
                }
            }
        } else {
            sendIntent();
        }

        /*Deep linking*/
//        FirebaseDynamicLinks.getInstance()
//                .getDynamicLink(getIntent())
//                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
//                    @Override
//                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
//                        // Get deep link from result (may be null if no link is found)
//                        Uri deepLink = null;
//                        if (pendingDynamicLinkData != null) {
//                            deepLink = pendingDynamicLinkData.getLink();
//
//                            type = deepLink.toString().substring(deepLink.toString().lastIndexOf("=") + 1);
//
//
//                            // Split the input string based on '/'
//                            String[] parts = type.split("/");
//
//                            // Store the parts in separate variables
//                            create = parts[0];
//
//
//                            Log.d("Type", type + "//" + create + "/" + party_grpup_txt + "/" + party_grpup_id);
//
//                            if (type.contains("/")) {
//                                String id = type.substring(type.lastIndexOf("/") + 1);
//
//                                type = type.replaceAll("[0-9]", "");
//                                type = type.replace("/", "");
//
//                                Log.d(TAG, type + "academy_id=" + academy_id);
//
//                                if (type.equalsIgnoreCase("post")) {
//                                    post_id = id;
//                                } else if (type.equalsIgnoreCase("training_tips")) {
//                                    academy_id = id;
//                                } else if (type.equalsIgnoreCase("live_streaming")) {
//                                    matchId = id;
//                                } else if (create.equalsIgnoreCase("create")) {
//                                    matchId = parts[1];
//                                    party_grpup_txt = parts[2];
//                                    party_grpup_id = parts[3];
//                                }
//
//                            }
//
//                            Log.d("Type..", party_grpup_id + "/" + party_grpup_txt + "/" + matchId);
//                            //Log.d(TAG,type+"/"+deepLink+"academy_id="+academy_id+"post_id="+post_id+"/matchId="+matchId);
//
//                        }
//
//                    }
//                })
//                .addOnFailureListener(this, new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "getDynamicLink:onFailure", e);
//                    }
//                });


    }

    private boolean checkAndRequestPermissions() {
        int access_network_state = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        int readSDPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writeSDPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //int manageSDPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE);

        int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int audioPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int modify_audio_settings = ContextCompat.checkSelfPermission(this, Manifest.permission.MODIFY_AUDIO_SETTINGS);
        int vibrate = ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE);
        int phoneCallPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);


        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (writeSDPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

//        if (manageSDPermission != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.MANAGE_EXTERNAL_STORAGE);
//        }


        if (readSDPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }

        if (vibrate != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.VIBRATE);
        }
        if (phoneCallPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }


        if (audioPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }

        if (access_network_state != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }

        if (modify_audio_settings != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.MODIFY_AUDIO_SETTINGS);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), PERMISSION_ALL);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            sendIntent();
        } else {
            finish();
        }
    }

    void sendIntent() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                // Log.d("Status",SessionManager.get_check_login(prefs)+"");
//                if (SessionManager.get_check_login(prefs)) {
////                    if (SessionManager.get_check_agreement(prefs)) {
////                        Intent intent = new Intent(Splash.this, Verification.class);
////                        startActivity(intent);
////                        finish();
////                    } else {
//                    if (type.equalsIgnoreCase("Coach_List")) {
//                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
//                        intent.putExtra("type", "Coach_List");
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        finish();
//                    } else if (type.equalsIgnoreCase("live_streaming")) {
//                        Intent intent = new Intent(SplashScreen.this, Play_Live_Stream_Single.class);
//                        intent.putExtra("FROM", "4");
//                        intent.putExtra("matchId", matchId);
//                        intent.putExtra("type", type);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        finish();
//                    } else if (create.equalsIgnoreCase("create")) {
//                        Intent intent = new Intent(SplashScreen.this, Play_Live_Stream_Single.class);
//                        intent.putExtra("FROM", "4");
//                        intent.putExtra("matchId", matchId);
//                        intent.putExtra("partyGroupId", party_grpup_id);
//                        intent.putExtra("partyGroupTxt", party_grpup_txt);
//                        intent.putExtra("type", create);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        finish();
//                    } else if (type.equalsIgnoreCase("Booking")) {
//                        Intent intent = new Intent(SplashScreen.this, BookingActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        finish();
//                    } else if (type.equalsIgnoreCase("post")) {
//                        Intent intent = new Intent(SplashScreen.this, FeedDetails.class);
//                        intent.putExtra("feed_id", post_id);
//                        //intent.putExtra("type", type);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        finish();
//                        /*if(type.equalsIgnoreCase("Booking")){
//                            Intent intent = new Intent(Splash.this, BookingActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                            if (booking_id.isEmpty()) {
////                                Intent intent = new Intent(Splash.this, BookingActivity.class);
////                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                            } else {
////                                Intent intent = new Intent(Splash.this, BookingDetailsActivity.class);
////                                intent.putExtra("FROM", "2");
////                                intent.putExtra("BookingID", booking_id);
////                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                            }
//                        }*/
//
//                    } else if (type.equalsIgnoreCase("training_tips")) {
//                        Intent intent = new Intent(SplashScreen.this, AcademyTipsPreviewActivity.class);
//
//                        if (academy_id.isEmpty()) {
//                            intent.putExtra("ACADEMY_ID", "");
//                        } else {
//                            intent.putExtra("ACADEMY_ID", academy_id);
//                        }
//                        intent.putExtra("FROM", "2");
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        finish();
//                    } else if (type.equalsIgnoreCase("tips")) {
//                        Intent intent = new Intent(SplashScreen.this, AcademyTipsPreviewActivity.class);
//                        intent.putExtra("ACADEMY_ID", academy_id);
//                        intent.putExtra("FROM", "3");
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                } else {
//
//                    if (!SessionManager.get_check_login(prefs)) {
//                        Intent intent = new Intent(SplashScreen.this, IntroScreen.class);
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//
////                    Intent intent = new Intent(Splash.this, Welcome.class);
//                }

                if (SessionManager.get_check_login(prefs)) {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (!SessionManager.get_check_login(prefs)) {
                        Intent intent = new Intent(SplashScreen.this, IntroScreen.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }


            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 1000);
    }
}