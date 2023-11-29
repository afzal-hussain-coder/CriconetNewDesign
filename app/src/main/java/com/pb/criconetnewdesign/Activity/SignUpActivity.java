package com.pb.criconetnewdesign.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.pb.criconetnewdesign.R;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        findViewById(R.id.tv_login).setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
        });
        findViewById(R.id.fl_register).setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this,VerifyOtpActivity.class));
        });




    }
}