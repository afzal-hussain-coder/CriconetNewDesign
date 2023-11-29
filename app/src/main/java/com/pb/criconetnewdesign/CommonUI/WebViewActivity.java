package com.pb.criconetnewdesign.CommonUI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import com.pb.criconetnewdesign.databinding.ActivityPartnerWithUsBinding;
import com.pb.criconetnewdesign.databinding.ToolbarInnerpageBinding;
import com.pb.criconetnewdesign.util.Global;


public class WebViewActivity extends AppCompatActivity {

    ActivityPartnerWithUsBinding activityPartnerWithUsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityPartnerWithUsBinding = ActivityPartnerWithUsBinding.inflate(getLayoutInflater());
        setContentView(activityPartnerWithUsBinding.getRoot());

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityPartnerWithUsBinding.toolbar;
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());
        WebSettings webSettings= activityPartnerWithUsBinding.wvPartnerWithUs.getSettings();
        webSettings.setJavaScriptEnabled(true);
        activityPartnerWithUsBinding.wvPartnerWithUs.setWebViewClient(new WebViewClient());
        activityPartnerWithUsBinding.wvPartnerWithUs.setWebChromeClient(new WebChromeClient());
        if (getIntent() != null) {
            if (getIntent().getExtras().getString("URL").equalsIgnoreCase("Game")) {
                activityPartnerWithUsBinding.wvPartnerWithUs.loadUrl(Global.GameURL + "user_id=" + "1387" + "");
                toolbarInnerpageBinding.toolbartext.setText("Super Six");
            } else {
                activityPartnerWithUsBinding.wvPartnerWithUs.loadUrl(getIntent().getStringExtra("URL"));
                toolbarInnerpageBinding.toolbartext.setText(getIntent().getStringExtra("title"));
            }
        }

    }
}