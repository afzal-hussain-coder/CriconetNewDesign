package com.pb.criconetnewdesign.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.pb.criconetnewdesign.R;

public class VerifyOtpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        findViewById(R.id.fl_submit).setOnClickListener(v -> {
            startActivity(new Intent(VerifyOtpActivity.this,SelectProfileTypeActivity.class));
        });


    }
}