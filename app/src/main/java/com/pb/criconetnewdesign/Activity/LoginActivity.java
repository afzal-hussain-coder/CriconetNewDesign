package com.pb.criconetnewdesign.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.pb.criconetnewdesign.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        findViewById(R.id.tv_forgot).setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this,ForgetPasswordActvity.class));
        });

        findViewById(R.id.register).setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
        });

    }
}