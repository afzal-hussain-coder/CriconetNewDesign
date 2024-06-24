package com.pb.criconetnewdesign.Activity.Coach;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.databinding.ActivityBlogBinding;
import com.pb.criconetnewdesign.databinding.ActivityCancelletionWebViewBinding;
import com.pb.criconetnewdesign.databinding.ToolbarInnerpageBinding;

public class CancelletionWebView extends AppCompatActivity {

    ActivityCancelletionWebViewBinding activityCancelletionWebViewBinding;
    TextView mTitle;
    WebView web;
    String url="";
    String title="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCancelletionWebViewBinding = ActivityCancelletionWebViewBinding.inflate(getLayoutInflater());
        setContentView(activityCancelletionWebViewBinding.getRoot());


        ToolbarInnerpageBinding toolbarInnerpageBinding = activityCancelletionWebViewBinding.toolbar;
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        web = findViewById(R.id.web);
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("URL");
            web.getSettings().setJavaScriptEnabled(true);
            web.loadUrl(url);
            title = bundle.getString("title");
        }
        Log.d("URL",url);
        toolbarInnerpageBinding.toolbartext.setText(title);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CancelletionWebView.this, CancellationFeedbackFormActivity.class);
        startActivity(intent);
        finish();
    }
}