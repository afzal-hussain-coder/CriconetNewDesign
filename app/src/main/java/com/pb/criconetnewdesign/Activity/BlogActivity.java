package com.pb.criconetnewdesign.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.adapter.PavilionAdapter.MyBlogsAdapter;
import com.pb.criconetnewdesign.databinding.ActivityBlogBinding;
import com.pb.criconetnewdesign.databinding.ToolbarInnerpageBinding;

public class BlogActivity extends AppCompatActivity {


    ActivityBlogBinding activityBlogBinding;
    Context mContext;
    Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBlogBinding = ActivityBlogBinding.inflate(getLayoutInflater());
        setContentView(activityBlogBinding.getRoot());
        mContext = this;
        mActivity = this;
        inItView();
    }
    private void inItView() {
        ToolbarInnerpageBinding toolbarInnerpageBinding = activityBlogBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(getResources().getString(R.string.blog));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        activityBlogBinding.rcvBlogs.setHasFixedSize(true);
        activityBlogBinding.rcvBlogs.setLayoutManager(new LinearLayoutManager(mActivity));
        activityBlogBinding.rcvBlogs.setAdapter(new MyBlogsAdapter(mContext, () -> {
            startActivity(new Intent(mContext,BlogDetailsActivity.class));
        }));
    }
}