package com.pb.criconetnewdesign.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.pb.criconetnewdesign.R;

public class ForgetPasswordActvity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_actvity);

        findViewById(R.id.img_back).setOnClickListener(v -> {
           finish();
        });
    }
}