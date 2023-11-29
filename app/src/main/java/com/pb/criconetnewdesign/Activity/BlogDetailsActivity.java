package com.pb.criconetnewdesign.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Scroller;

import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.adapter.PavilionAdapter.BlogCommentsAdapter;
import com.pb.criconetnewdesign.databinding.ActivityBlogDetailsBinding;
import com.pb.criconetnewdesign.databinding.ToolbarInnerpageBinding;

import java.util.Objects;

public class BlogDetailsActivity extends AppCompatActivity {

    ActivityBlogDetailsBinding activityBlogDetailsBinding;
    Context mContext;
    Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBlogDetailsBinding = ActivityBlogDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityBlogDetailsBinding.getRoot());
        mContext = this;
        mActivity = this;

        inItView();
    }

    private void inItView() {
        ToolbarInnerpageBinding toolbarInnerpageBinding = activityBlogDetailsBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(getResources().getString(R.string.blog));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        activityBlogDetailsBinding.rcvBlogComments.setHasFixedSize(true);
        activityBlogDetailsBinding.rcvBlogComments.setLayoutManager(new LinearLayoutManager(mContext));
        activityBlogDetailsBinding.rcvBlogComments.setAdapter(new BlogCommentsAdapter(mContext));

        activityBlogDetailsBinding.tvCommentsCount.setOnClickListener(v -> {
            if(activityBlogDetailsBinding.rcvBlogComments.getVisibility()== View.VISIBLE){
                activityBlogDetailsBinding.rcvBlogComments.setVisibility(View.GONE);
                activityBlogDetailsBinding.tvCommentsCount.setCompoundDrawablesWithIntrinsicBounds(
                        null,null,getDrawable(R.drawable.arrow_circle_down_black_24dp),null);
            }else{
                activityBlogDetailsBinding.tvCommentsCount.setCompoundDrawablesWithIntrinsicBounds(
                        null,null,getDrawable(R.drawable.arrow_circle_up_black_24dp),null);
                activityBlogDetailsBinding.rcvBlogComments.setVisibility(View.VISIBLE);
            }
        });


        activityBlogDetailsBinding.messageTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityBlogDetailsBinding.messageTextInputLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityBlogDetailsBinding.messageTextInputLayout.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityBlogDetailsBinding.messageTextInputLayout.setErrorEnabled(false);
            }
        });
        activityBlogDetailsBinding.nameTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityBlogDetailsBinding.nameTextInputLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityBlogDetailsBinding.nameTextInputLayout.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityBlogDetailsBinding.nameTextInputLayout.setErrorEnabled(false);
            }
        });
        activityBlogDetailsBinding.emailTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                activityBlogDetailsBinding.emailTextInputLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activityBlogDetailsBinding.emailTextInputLayout.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityBlogDetailsBinding.emailTextInputLayout.setErrorEnabled(false);
            }
        });

        activityBlogDetailsBinding.flSubmit.setOnClickListener(v -> {
            if (Objects.requireNonNull(activityBlogDetailsBinding.messageTextInputEditText.getText()).toString().isEmpty()) {
                activityBlogDetailsBinding.messageTextInputLayout.setErrorEnabled(true);
                activityBlogDetailsBinding.messageTextInputLayout.setError("Message can't be empty!");
                activityBlogDetailsBinding.messageTextInputLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.purple_700)));
            }else if (Objects.requireNonNull(activityBlogDetailsBinding.nameTextInputEditText.getText()).toString().isEmpty()) {
                activityBlogDetailsBinding.nameTextInputLayout.setErrorEnabled(true);
                activityBlogDetailsBinding.nameTextInputLayout.setError("Name can't be empty!");
                activityBlogDetailsBinding.nameTextInputLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.purple_700)));
            } else if (Objects.requireNonNull(activityBlogDetailsBinding.emailTextInputEditText.getText()).toString().isEmpty()) {
                activityBlogDetailsBinding.emailTextInputLayout.setErrorEnabled(true);
                activityBlogDetailsBinding.emailTextInputLayout.setError("Email can't be empty!");
                activityBlogDetailsBinding.emailTextInputLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.purple_700)));
            }
        });

    }
}