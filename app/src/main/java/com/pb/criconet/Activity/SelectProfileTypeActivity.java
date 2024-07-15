package com.pb.criconet.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.pb.criconet.R;

public class SelectProfileTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_profile_type);

        findViewById(R.id.fl_player).setOnClickListener(v -> {
            startActivity(new Intent(SelectProfileTypeActivity.this,MainActivity.class));

        });
    }
}