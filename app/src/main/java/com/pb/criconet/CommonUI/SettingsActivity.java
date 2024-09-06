package com.pb.criconet.CommonUI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.pb.criconet.Activity.Coach.RegisterAsAnECoachActivity;
import com.pb.criconet.Activity.User.UserProfileActivity;
import com.pb.criconet.R;
import com.pb.criconet.databinding.ActivitySettingsBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.util.SessionManager;

public class SettingsActivity extends AppCompatActivity {
    ActivitySettingsBinding activitySettingsBinding;
    Context mContext;
    Activity mActivity;
    private SharedPreferences prefs;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySettingsBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(activitySettingsBinding.getRoot());

        mContext = this;
        mActivity = this;

        queue = Volley.newRequestQueue(mContext);
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        ToolbarInnerpageBinding toolbarInnerpageBinding = activitySettingsBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(R.string.setting);
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> {
            finish();
        });

        inItView();
    }

    private void inItView() {

        activitySettingsBinding.rlEditProfile.setOnClickListener(v -> {

            if (SessionManager.get_profiletype(prefs).equalsIgnoreCase("coach")) {
                startActivity(new Intent(mActivity, RegisterAsAnECoachActivity.class));
                //finish();
            }else{
                startActivity(new Intent(mActivity, UserProfileActivity.class).putExtra("FROM","2"));
                finish();
            }
        });
        activitySettingsBinding.rlChangePass.setOnClickListener(v -> {
            startActivity(new Intent(mContext, ChangePasswordActivity.class));
        });
        activitySettingsBinding.rlBlockedUsers.setOnClickListener(v -> {
            startActivity(new Intent(mContext,BlockUserActivity.class));
        });
        activitySettingsBinding.rlRateThisApp.setOnClickListener(v -> {
            Uri uri = Uri.parse("market://details?id=" + SettingsActivity.this.getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + SettingsActivity.this.getPackageName())));
            }

        });
        activitySettingsBinding.rlRecommendThisApp.setOnClickListener(v -> {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
                String sAux = getResources().getString(R.string.I_recommend_this_great_App_download_now_and_we_can_connect);
                sAux = sAux + "Android : https://play.google.com/store/apps/details?id=" + SettingsActivity.this.getPackageName();
//                    sAux = sAux + " \n\n IOS : https://itunes.apple.com/us/app/criconet/id1447634287?ls=1&mt=8";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "Select one"));
            } catch (Exception e) {
                //e.toString();
            }
        });
        activitySettingsBinding.rlUserAgreement.setOnClickListener(v -> {
            String url = "https:\\/\\/www.criconet.com\\/user-agreement?rst=app";
            startActivity(new Intent(mActivity, WebViewActivity.class).putExtra("URL", url).putExtra("title", "User Agreement"));
        });
        activitySettingsBinding.rlPrivacyPolicy.setOnClickListener(v -> {
            String url ="https:\\/\\/www.criconet.com\\/terms\\/privacy-policy?rst=app";
            startActivity(new Intent(mActivity, WebViewActivity.class).putExtra("URL", url).putExtra("title","Privacy Policy"));
        });
        activitySettingsBinding.rlTermsOfUse.setOnClickListener(v -> {
            String url ="https:\\/\\/www.criconet.com\\/terms\\/terms?rst=app";
            startActivity(new Intent(mActivity, WebViewActivity.class).putExtra("URL", url).putExtra("title","Terms"));
        });
        activitySettingsBinding.rlAboutCriconet.setOnClickListener(v -> {
            String url = "https://www.criconet.com/terms/about-us?rst=app";
            startActivity(new Intent(mActivity, WebViewActivity.class).putExtra("URL", url).putExtra("title", "About Us"));
        });

    }
}