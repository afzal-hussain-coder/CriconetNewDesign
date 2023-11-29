package com.pb.criconetnewdesign.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.adapter.PavilionAdapter.MyBlogsAdapter;
import com.pb.criconetnewdesign.databinding.ActivityMyBlogsBinding;
import com.pb.criconetnewdesign.databinding.ToolbarInnerpageBinding;

public class MyBlogsActivity extends AppCompatActivity {

    ActivityMyBlogsBinding activityMyBlogsBinding;
    Context mContext;
    Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMyBlogsBinding = ActivityMyBlogsBinding.inflate(getLayoutInflater());
        setContentView(activityMyBlogsBinding.getRoot());
        mContext = this;
        mActivity = this;
        inItView();
    }

    private void inItView() {
        ToolbarInnerpageBinding toolbarInnerpageBinding = activityMyBlogsBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(getResources().getString(R.string.my_blogs));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        activityMyBlogsBinding.rcvMyBlogs.setHasFixedSize(true);
        activityMyBlogsBinding.rcvMyBlogs.setLayoutManager(new LinearLayoutManager(mActivity));
        activityMyBlogsBinding.rcvMyBlogs.setAdapter(new MyBlogsAdapter(mContext, () -> {
            startActivity(new Intent(mContext,BlogDetailsActivity.class));
        }));
    }
}