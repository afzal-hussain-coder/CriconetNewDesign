package com.pb.criconet.Activity;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.RelativeLayout.*;

import static com.pb.criconet.util.Global.TYPE_IMAGE;
import static com.pb.criconet.util.Global.TYPE_LINK;
import static com.pb.criconet.util.Global.TYPE_TEXT;
import static com.pb.criconet.util.Global.TYPE_VIDEO;
import static com.pb.criconet.util.Global.TYPE_YOUTUBE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.luseen.autolinklibrary.AutoLinkMode;
import com.luseen.autolinklibrary.AutoLinkOnClickListener;
import com.pb.criconet.AGApplication;
import com.pb.criconet.CustomeCamera.CustomeCameraActivity;
import com.pb.criconet.R;
import com.pb.criconet.adapter.PavilionAdapter.CommentAdapter;
import com.pb.criconet.adapter.PavilionAdapter.MyCustomPagerAdapter;
import com.pb.criconet.adapter.PavilionAdapter.SearchUserAdapter;
import com.pb.criconet.databinding.ActivityFeedDetailsBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.model.pavilionModel.CommentModel;
import com.pb.criconet.model.pavilionModel.ImageModel;
import com.pb.criconet.model.pavilionModel.NewPostModel;
import com.pb.criconet.model.pavilionModel.SearchUser;
import com.pb.criconet.model.pavilionModel.TagModel;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.SessionManager;
import com.pb.criconet.util.Toaster;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;

public class FeedDetailsActivity extends AppCompatActivity {

    ActivityFeedDetailsBinding activityFeedDetailsBinding;
    Context mContext;
    Activity mActivity;
    boolean isKeyBoardShow = false;
    String singleImagePic = "";
    private RequestQueue queue;
    private ArrayList<SearchUser> searchUserArrayList = new ArrayList<>();
    private String searchUsername = "";
    public static Uri image_uri = null;
    String feed_id;
    CustomLoaderView loaderView;
    NewPostModel data;
    ArrayList<CommentModel> modelArrayList;
    private SharedPreferences prefs;

    TextView like_link, dislike_link, tv_comment_count;
    LinearLayout like, dislike, share, comment;
    CommentAdapter commentAdapter;
    String CommentType = "", commentId = "", replyId = "", commentReplayedOnUserName = "",type="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFeedDetailsBinding = ActivityFeedDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityFeedDetailsBinding.getRoot());

        mActivity = this;
        mContext = this;
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        queue = Volley.newRequestQueue(mActivity);
        loaderView = CustomLoaderView.initialize(mActivity);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            feed_id = bundle.getString("feed_id");
            type = bundle.getString("type");

            Log.d("FeedId", feed_id);
        }
        inItView();

        if (Global.isOnline(mContext)) {
            getFeedDetails();
        } else {
            Global.showDialog(mActivity);
        }
    }

    private void inItView() {

        like_link = findViewById(R.id.like_link);
        dislike_link = findViewById(R.id.dislike_link);
        tv_comment_count = findViewById(R.id.tv_comment_count);
        like = findViewById(R.id.like);
        like.setOnClickListener(v -> {
            if (Global.isOnline(mContext)) {
                likeFeed(data.getId());
            } else {
                Global.showDialog(mActivity);
            }
        });
        dislike = findViewById(R.id.dislike);
        dislike.setOnClickListener(v -> {
            if (Global.isOnline(mContext)) {
                dislikeFeed(data.getId());
            } else {
                Global.showDialog(mActivity);
            }
        });
        share = findViewById(R.id.share);
        share.setOnClickListener(v -> {
            if (data.getPost_type().equalsIgnoreCase("text") && !data.getPostFile().isEmpty()) {
                String imageUrl = data.getPostFile();
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());

            } else if (data.getPost_type().equalsIgnoreCase("link") && !data.getPostLinkImage().isEmpty()) {
                String imageUrl = data.getPostLinkImage();
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            } else if (data.getPost_type().equalsIgnoreCase("image")) {
                String imageUrl = data.getPostFile();
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            } else if (data.getPost_type().equalsIgnoreCase("video")) {
                String imageUrl = data.getThumb();
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            } else if (data.getPost_type().equalsIgnoreCase("profile_cover_picture")) {
                String imageUrl = data.getPostFile();
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            } else if (data.getPost_type().equalsIgnoreCase("photo_multi")) {
                String imageUrl = data.getPhoto_multi().get(0).getImage();
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            } else if (data.getPost_type().equalsIgnoreCase("youtube")) {
                String imageUrl = data.getPostLinkImage();
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            } else if (data.getPost_type().equalsIgnoreCase("profile_picture")) {
                String imageUrl = "";
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            } else {
                String imageUrl = "";
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            }
        });

        comment = findViewById(R.id.comment);
        comment.setOnClickListener(v -> {
            activityFeedDetailsBinding.editComment.setEnabled(true);
            activityFeedDetailsBinding.editComment.setCursorVisible(true);
            activityFeedDetailsBinding.editComment.setPressed(true);

        });

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityFeedDetailsBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.comments));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> {
            if (activityFeedDetailsBinding.rvSearchUser.getVisibility() == View.VISIBLE) {
                activityFeedDetailsBinding.rvSearchUser.setVisibility(GONE);
                activityFeedDetailsBinding.rlMain.setVisibility(VISIBLE);
            } else {
                if(type.equalsIgnoreCase("t")){
                    finish();
                }else{
                    startActivity(new Intent(mContext,MainActivity.class));
                }

            }
        });


        activityFeedDetailsBinding.rvComment.setHasFixedSize(true);
        activityFeedDetailsBinding.rvComment.setLayoutManager(new LinearLayoutManager(mActivity));


        activityFeedDetailsBinding.rvSearchUser.setHasFixedSize(true);
        activityFeedDetailsBinding.rvSearchUser.setLayoutManager(new LinearLayoutManager(mActivity));
        activityFeedDetailsBinding.rvSearchUser.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));


        //Comment Post Listener

        activityFeedDetailsBinding.editComment.setOnClickListener(v -> {
            activityFeedDetailsBinding.rlSubmit.setVisibility(View.VISIBLE);
            activityFeedDetailsBinding.editComment.setCursorVisible(true);
        });

        activityFeedDetailsBinding.editComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().isEmpty()) {

                    searchUsername = "";
                    activityFeedDetailsBinding.tvPostGray.setVisibility(View.VISIBLE);
                    activityFeedDetailsBinding.rlSubmit.setVisibility(View.GONE);
                } else {
                    activityFeedDetailsBinding.tvPostGray.setVisibility(View.GONE);
                    activityFeedDetailsBinding.rlSubmit.setVisibility(View.VISIBLE);
                    String search = s.toString();
                    String[] parts = null;
                    String lastWord = "";


                    if (search.contains("\n")) {
                        parts = search.split("\n");
                    } else {
                        parts = search.split(" ");
                    }

                    if (search.startsWith(" ")) {
                        lastWord = search;
                    } else {
                        lastWord = parts[parts.length - 1];
                    }

                    String finalWord = lastWord.substring(lastWord.lastIndexOf(" ") + 1);


                    if (finalWord.contains("@") && finalWord.length() > 3) {

                        Matcher matcher = Pattern.compile("@\\s*(\\w+)").matcher(finalWord);

                        while (matcher.find()) {
                            if (searchUsername.isEmpty()) {
                                if (Global.isOnline(mContext)) {
                                    getUserSearchList(matcher.group(1));
                                } else {
                                    Global.showDialog(mActivity);
                                }
                            }
                        }
                    } else {
                        activityFeedDetailsBinding.rvSearchUser.setVisibility(View.GONE);
                        searchUsername = "";
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        activityFeedDetailsBinding.editComment.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (Global.keyboardShown(activityFeedDetailsBinding.editComment.getRootView())) {
                isKeyBoardShow = true;
                activityFeedDetailsBinding.editComment.setCursorVisible(true);
                activityFeedDetailsBinding.tvPostGray.setVisibility(View.GONE);
                activityFeedDetailsBinding.rlSubmit.setVisibility(View.VISIBLE);
            } else {
                isKeyBoardShow = false;
                if (activityFeedDetailsBinding.editComment.getText().toString().isEmpty()) {
                    activityFeedDetailsBinding.tvPostGray.setVisibility(View.VISIBLE);
                    activityFeedDetailsBinding.editComment.setCursorVisible(false);
                    activityFeedDetailsBinding.rlSubmit.setVisibility(View.GONE);
                } else {
                    activityFeedDetailsBinding.tvPostGray.setVisibility(View.GONE);
                    activityFeedDetailsBinding.editComment.setCursorVisible(true);
                    activityFeedDetailsBinding.rlSubmit.setVisibility(View.VISIBLE);
                }

            }
        });

        /*activityFeedDetailsBinding.imgKeyboard.setOnClickListener(v -> {
            if (!isKeyBoardShow) {
                Global.showKeyBoard();
            } else {
                Global.hideKeyBoard(mActivity);
            }
        });*/

        activityFeedDetailsBinding.imgCamera.setOnClickListener(v -> {
            startActivity(new Intent(mContext, CustomeCameraActivity.class)
                    .putExtra("FROM", "FeedDetailsActivity"));
        });
        activityFeedDetailsBinding.imgClose.setOnClickListener(v -> {
            activityFeedDetailsBinding.flAttachedImage.setVisibility(GONE);
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            activityFeedDetailsBinding.rlScroll.setLayoutParams(layoutParams);
            activityFeedDetailsBinding.rlScroll.requestLayout();
        });

        activityFeedDetailsBinding.tvPostBlue.setOnClickListener(v -> {
            activityFeedDetailsBinding.flAttachedImage.setVisibility(GONE);
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            activityFeedDetailsBinding.rlScroll.setLayoutParams(layoutParams);
            activityFeedDetailsBinding.rlScroll.requestLayout();
            image_uri = null;
            activityFeedDetailsBinding.editComment.setText("");
        });

        activityFeedDetailsBinding.imgAtTheRate.setOnClickListener(v -> {
            activityFeedDetailsBinding.rlMain.setVisibility(GONE);
            activityFeedDetailsBinding.rvSearchUser.setVisibility(VISIBLE);
            if (Global.isOnline(mContext)) {
                getUserSearchList("");
            } else {
                Global.showDialog(mActivity);
            }
        });

        activityFeedDetailsBinding.tvPostBlue.setOnClickListener(v -> {

            if (CommentType.equalsIgnoreCase("Reply")) {
                replyId = "";
                if (activityFeedDetailsBinding.editComment.getText().toString().trim().length() <= commentReplayedOnUserName.length()) {
                    Toaster.customToast("Please replay on comments");
                } else {
                    if (Global.isOnline(mContext)) {
                        postReplayComment(activityFeedDetailsBinding.editComment.getText().toString(), commentId);
                    } else {
                        Global.showDialog(mActivity);
                    }
                }

            } else if (CommentType.equalsIgnoreCase("Edit")) {
                if (Global.isOnline(mContext)) {
                    postReplayComment(activityFeedDetailsBinding.editComment.getText().toString(), commentId);
                } else {
                    Global.showDialog(mActivity);
                }
            } else {
                activityFeedDetailsBinding.imgAtTheRate.setVisibility(VISIBLE);
                if (activityFeedDetailsBinding.editComment.getText().toString().isEmpty()) {
                    Toaster.customToast("Please add comments!");
                } else {

                    if (CommentType.equalsIgnoreCase("EditComment")) {
                        if (Global.isOnline(mContext)) {
                            postComment(activityFeedDetailsBinding.editComment.getText().toString(), commentId);
                        } else {
                            Global.showDialog(mActivity);
                        }
                    } else {
                        CommentType = "";
                        commentId = "";
                        if (Global.isOnline(mContext)) {
                            postComment(activityFeedDetailsBinding.editComment.getText().toString(), commentId);
                        } else {
                            Global.showDialog(mActivity);
                        }
                    }

                }
            }

        });

    }

    public void getFeedDetails() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_post_data", response -> {
            Timber.e(String.valueOf(response));
            loaderView.hideLoader();
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                    JSONObject array = jsonObject.getJSONObject("post_data");
                    data = NewPostModel.fromJson(array);
                    setPostData(data);

                    if (SessionManager.get_image(prefs).isEmpty()) {
                        Glide.with(mContext).load(mContext.getResources().getDrawable(R.drawable.user_default)).into(activityFeedDetailsBinding.civSenderImage);
                    } else {
                        Glide.with(mContext).load(SessionManager.get_image(prefs)).into(activityFeedDetailsBinding.civSenderImage);
                    }

                    modelArrayList = new ArrayList<>();
                    modelArrayList.addAll(data.getGet_post_comments());

                    commentAdapter = new CommentAdapter(mContext, data.getPublisher_type(), data.getPage_id(), modelArrayList, new CommentAdapter.itemDeleteInterface() {
                        @Override
                        public void commentDelete(String userId,String commentId, String commentText) {
                            BottomSheetDialogCooment(userId,commentId, commentText);
                        }

                        @Override
                        public void addReplayComment(String id, String username, String reply) {
                            activityFeedDetailsBinding.imgAtTheRate.setVisibility(GONE);
                            CommentType = reply;
                            commentId = id;
                            commentReplayedOnUserName = username;
                            activityFeedDetailsBinding.editComment.setText(username+" ");
                            activityFeedDetailsBinding.editComment.setTextColor(mContext.getResources().getColor(R.color.blue));
                            activityFeedDetailsBinding.editComment.setSelection(activityFeedDetailsBinding.editComment.getText().length());
                            activityFeedDetailsBinding.editComment.setCursorVisible(true);


                        }

                        @Override
                        public void deleteReply(String userId, String replyId, String commentText, String userNme, String CommentId) {
                            BottomSheetDialogReplyCooment(userId, replyId, commentText, userNme, CommentId);
                        }

                        @Override
                        public void likeComments(String commentId,boolean status) {
                          //  Toaster.customToast(status+"");
                            if (Global.isOnline(mContext)) {
                                likeCommentss(commentId,status);
                            } else {
                                Global.showDialog(mActivity);
                            }
                        }

                        @Override
                        public void likeReply(String replyId, boolean status) {
                            if(Global.isOnline(mContext)){
                                likeReplies(replyId,status);
                            }else{
                                Global.showDialog(mActivity);
                            }
                        }
                    });
                    activityFeedDetailsBinding.rvComment.setAdapter(commentAdapter);

                    like_link.setText(data.getCount_post_likes());
                    dislike_link.setText(data.getCount_post_wonders());
                    tv_comment_count.setText(data.getCount_post_comments() + " " + "Comments");

                } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                    Global.msgDialog(mActivity, jsonObject.optJSONObject("errors").optString("error_text"));
                } else {
                    Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
            loaderView.hideLoader();
            Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("post_id", feed_id);
                param.put("s", SessionManager.get_session_id(prefs));
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    public int getItemViewType(NewPostModel data) {

        if (data.getPost_type().equalsIgnoreCase("image")) {
            return TYPE_IMAGE;
        } else if (data.getPost_type().equalsIgnoreCase("video")) {
            return TYPE_VIDEO;
        } else if (data.getPost_type().equalsIgnoreCase("text")) {
            return TYPE_TEXT;
        } else if (data.getPost_type().equalsIgnoreCase("link")) {
            return TYPE_LINK;
        } else if (data.getPost_type().equalsIgnoreCase("youtube")) {
            return TYPE_YOUTUBE;
        } else if (data.getPost_type().equalsIgnoreCase("photo_multi")) {
            return TYPE_IMAGE;
        } else {
            return TYPE_IMAGE;
        }
    }

    private void setPostData(final NewPostModel data) {
        switch (getItemViewType(data)) {
            case TYPE_IMAGE:
                /*-----------------------------------------------------------------------------------
                ------------------------------------ FOR IMAGE/MULTI IMAGE --------------------------
                -----------------------------------------------------------------------------------*/
            {
                if (data.getPublisher().getUser().getAvatar().isEmpty()) {
                    Glide.with(mContext).load(mContext.getResources().getDrawable(R.drawable.user_default)).into(activityFeedDetailsBinding.cIVAvtarSingleImageMultiImage);
                } else {
                    Glide.with(mContext).load(data.getPublisher().getUser().getAvatar()).into(activityFeedDetailsBinding.cIVAvtarSingleImageMultiImage);
                }
                activityFeedDetailsBinding.tvUserNameSingleImageMultiImage.setText(data.getPublisher().getUser().getName());
                //activityFeedDetailsBinding.tvPostStatusSingleImageMultiImage.setText(data.getPublisher().getUser().getName());

                activityFeedDetailsBinding.ivOptionSingleImageMultiImage.setOnClickListener(v -> {
                    BottomSheetDialog(data.getId());
                });


                if (data.getPost_type().equalsIgnoreCase("photo_multi")) {
                    if (getKeyList(data.getPhoto_multi()).size() > 0) {
                        activityFeedDetailsBinding.liSingleImageMultiImage.setVisibility(VISIBLE);
                        activityFeedDetailsBinding.rlMultiImageSingleImageMultiImage.setVisibility(View.VISIBLE);
                        activityFeedDetailsBinding.ivPostimage.setVisibility(View.GONE);

                        ArrayList<String> temp = getKeyList(data.getPhoto_multi());

                        MyCustomPagerAdapter myCustomPagerAdapter = new MyCustomPagerAdapter(mContext, temp);
                        activityFeedDetailsBinding.viewPager.setAdapter(myCustomPagerAdapter);
                        // Disable clip to padding
                        activityFeedDetailsBinding.viewPager.setClipToPadding(false);
                        // set padding manually, the more you set the padding the more you see of prev & next page
                        activityFeedDetailsBinding.viewPager.setPadding(50, 0, 50, 0);
                        // sets a margin b/w individual pages to ensure that there is a gap b/w them
                        activityFeedDetailsBinding.viewPager.setPageMargin(20);
                    }
                } else {
                    activityFeedDetailsBinding.liSingleImageMultiImage.setVisibility(VISIBLE);
                    activityFeedDetailsBinding.rlMultiImageSingleImageMultiImage.setVisibility(View.GONE);
                    activityFeedDetailsBinding.ivPostimage.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(data.getPostFile()).into(activityFeedDetailsBinding.ivPostimage);
                }

                activityFeedDetailsBinding.rlMultiImageSingleImageMultiImage.setOnClickListener(v -> {
                    ArrayList<String> photos = new ArrayList<>();
//                    photos.addAll(getKeyList(data.getPhoto_multi()));
//                    Fragment fragment = new FeedsPhotos();
//                    Bundle bundle = new Bundle();
//                    bundle.putStringArrayList("photos", photos);
//                    fragment.setArguments(bundle);

                });

//                post_time.setText(data.getTime_text());
                if (data.getPostText().equalsIgnoreCase("")) {
                    activityFeedDetailsBinding.postTextAutolinkSingleImageMultiImage.setVisibility(View.GONE);
                } else {
                    activityFeedDetailsBinding.postTextAutolinkSingleImageMultiImage.setVisibility(View.VISIBLE);
                    ///new work today

//                  String postData=data.getPos;
//                    temp = postData.split("@");
//
//                    for(int i=0;i<data.getGet_post_comments().size();i++){
//                        for(int i=0;i<){
//
//                        }
//                    }


                    String text = data.getPostText().replace("\n", "<br>");
                    // post_text.setText(Html.fromHtml(text));
                    activityFeedDetailsBinding.postTextAutolinkSingleImageMultiImage.addAutoLinkMode(
                            AutoLinkMode.MODE_HASHTAG,
                            AutoLinkMode.MODE_PHONE,
                            AutoLinkMode.MODE_URL,
                            AutoLinkMode.MODE_MENTION,
                            AutoLinkMode.MODE_CUSTOM);
                    activityFeedDetailsBinding.postTextAutolinkSingleImageMultiImage.setAutoLinkText(Global.capitalizeFirstLatterOfString(data.getPostText_API()));

                    ArrayList<TagModel> tagModelArrayList = data.getTagsusers();
                    //Toaster.customToast(tagModelArrayList.size()+"");
                    activityFeedDetailsBinding.postTextAutolinkSingleImageMultiImage.setSelectedStateColor(ContextCompat.getColor(this, R.color.yourColor));
                    activityFeedDetailsBinding.postTextAutolinkSingleImageMultiImage.setHashtagModeColor(ContextCompat.getColor(mContext, R.color.app_green));
                    activityFeedDetailsBinding.postTextAutolinkSingleImageMultiImage.setPhoneModeColor(ContextCompat.getColor(mContext, R.color.app_green));
                    activityFeedDetailsBinding.postTextAutolinkSingleImageMultiImage.setCustomModeColor(ContextCompat.getColor(mContext, R.color.app_green));
                    activityFeedDetailsBinding.postTextAutolinkSingleImageMultiImage.setUrlModeColor(ContextCompat.getColor(mContext, R.color.app_green));
                    activityFeedDetailsBinding.postTextAutolinkSingleImageMultiImage.setMentionModeColor(ContextCompat.getColor(mContext, R.color.yourColor));
                    activityFeedDetailsBinding.postTextAutolinkSingleImageMultiImage.setEmailModeColor(ContextCompat.getColor(mContext, R.color.app_green));

                    activityFeedDetailsBinding.postTextAutolinkSingleImageMultiImage.setAutoLinkOnClickListener(new AutoLinkOnClickListener() {
                        @Override
                        public void onAutoLinkTextClick(AutoLinkMode autoLinkMode, String matchedText) {
                            String matched = matchedText.trim();
                            for (int i = 0; i < tagModelArrayList.size(); i++) {
                                //Toaster.customToast(matched+"/"+tagModelArrayList.get(i).getMatch_name().trim());
                                if (matched.trim().equalsIgnoreCase(tagModelArrayList.get(i).getMatch_name().trim())) {


                                    Intent intent = new Intent(mContext, UserDetails.class);
                                    intent.putExtra("user_id", tagModelArrayList.get(i).getUser_id());
                                    intent.putExtra("FROM", "1");
                                    intent.putExtra("feed_id", feed_id);
                                    startActivity(intent);

                                }
                            }

                        }
                    });
                }

                if (data.getPost_type().equalsIgnoreCase("Image")) {
                    Glide.with(mContext).load(data.getPostFile()).into(activityFeedDetailsBinding.ivPostimage);
                } else {
                    Glide.with(mContext).load(data.getPostFile()).into(activityFeedDetailsBinding.ivPostimage);
                }

                break;
            }
            case TYPE_VIDEO:
                /*-----------------------------------------------------------------------------------
                ------------------------------------ FOR VIDEO --------------------------------------
                -----------------------------------------------------------------------------------*/
            {

                activityFeedDetailsBinding.liVideo.setVisibility(VISIBLE);
                if (data.getPublisher().getUser().getAvatar().isEmpty()) {
                    Glide.with(mContext).load(mContext.getResources().getDrawable(R.drawable.user_default)).into(activityFeedDetailsBinding.civUserVideo);
                } else {
                    Glide.with(mContext).load(data.getPublisher().getUser().getAvatar()).into(activityFeedDetailsBinding.civUserVideo);
                }
                activityFeedDetailsBinding.tvUserNameVideo.setText(data.getPublisher().getUser().getName());
                //activityFeedDetailsBinding.tvPostStatusVideo.setText(data.getPublisher().getUser().getName());

                activityFeedDetailsBinding.ivPostOptionVideo.setOnClickListener(v -> {
                    BottomSheetDialog(data.getId());
                });

                if (data.getPostText().equalsIgnoreCase("")) {
                    activityFeedDetailsBinding.postTextAutolinkVideo.setVisibility(View.GONE);
                } else {
                    activityFeedDetailsBinding.postTextAutolinkVideo.setVisibility(View.VISIBLE);
                    String text = data.getPostText().replace("\n", "<br>");
                    // post_text.setText(Html.fromHtml(text));


                    activityFeedDetailsBinding.postTextAutolinkVideo.addAutoLinkMode(
                            AutoLinkMode.MODE_HASHTAG,
                            AutoLinkMode.MODE_PHONE,
                            AutoLinkMode.MODE_URL,
                            AutoLinkMode.MODE_EMAIL,
                            AutoLinkMode.MODE_MENTION);
                    activityFeedDetailsBinding.postTextAutolinkVideo.setAutoLinkText(Global.capitalizeFirstLatterOfString(data.getPostText_API()));
                    ArrayList<TagModel> tagModelArrayList = data.getTagsusers();
                    //Toaster.customToast(tagModelArrayList.size()+"");
                    activityFeedDetailsBinding.postTextAutolinkVideo.setSelectedStateColor(ContextCompat.getColor(mContext, R.color.yourColor));
                    activityFeedDetailsBinding.postTextAutolinkVideo.setHashtagModeColor(ContextCompat.getColor(mContext, R.color.app_green));
                    activityFeedDetailsBinding.postTextAutolinkVideo.setPhoneModeColor(ContextCompat.getColor(mContext, R.color.app_green));
                    activityFeedDetailsBinding.postTextAutolinkVideo.setCustomModeColor(ContextCompat.getColor(mContext, R.color.app_green));
                    activityFeedDetailsBinding.postTextAutolinkVideo.setUrlModeColor(ContextCompat.getColor(mContext, R.color.app_green));
                    activityFeedDetailsBinding.postTextAutolinkVideo.setMentionModeColor(ContextCompat.getColor(mContext, R.color.yourColor));
                    activityFeedDetailsBinding.postTextAutolinkVideo.setEmailModeColor(ContextCompat.getColor(mContext, R.color.app_green));
                    activityFeedDetailsBinding.postTextAutolinkVideo.setAutoLinkOnClickListener((autoLinkMode, matchedText) -> {
                        String matched = matchedText.trim();
                        for (int i = 0; i < tagModelArrayList.size(); i++) {
                            // Toaster.customToast(matched+"/"+tagModelArrayList.get(i).getMatch_name().trim());
                            if (matched.trim().equalsIgnoreCase(tagModelArrayList.get(i).getMatch_name().trim())) {


                                Intent intent = new Intent(mContext, UserDetails.class);
                                intent.putExtra("user_id", tagModelArrayList.get(i).getUser_id());
                                intent.putExtra("FROM", "1");
                                intent.putExtra("feed_id", feed_id);
                                startActivity(intent);

                            }
                        }

                    });
                }
                if (data.getThumb() != null && !data.getThumb().isEmpty()) {
                    activityFeedDetailsBinding.flVideo.setVisibility(VISIBLE);
                    Uri uri = Uri.parse(data.getPostFile());
                    MediaController mediaController = new MediaController(this);
                    mediaController.setAnchorView(activityFeedDetailsBinding.videoview);
                    activityFeedDetailsBinding.videoview.setMediaController(mediaController);
                    activityFeedDetailsBinding.videoview.setVideoURI(uri);
                    activityFeedDetailsBinding.videoview.start();
                }

//
//                //to play pause videos manually (optional)
//                img_playback.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        try {
//                            if (videoview.isPlaying()) {
//                                videoview.stopPlayback();
//                                //setPaused(true);
//                            } else {
//                                videoview.start();
//                                //playVideo();
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//
//                //to mute/un-mute video (optional)
//                img_vol.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        if (isMuted) {
////                            unmuteVideo();
////                            img_vol.setImageResource(R.drawable.ic_unmute);
////                        } else {
////                            muteVideo();
////                            img_vol.setImageResource(R.drawable.ic_mute);
////                        }
////                        isMuted = !isMuted;
//                    }
//                });

//                if (data.getPostFile() == null) {
//                    img_vol.setVisibility(View.GONE);
//                    img_playback.setVisibility(View.GONE);
//                } else {
//                    img_vol.setVisibility(View.VISIBLE);
//                    img_playback.setVisibility(View.VISIBLE);
//                }

                break;
            }
            default:
                /*-----------------------------------------------------------------------------------
                ------------------------------------ FOR TEXT/LINK/YOUTUBE --------------------------
                -----------------------------------------------------------------------------------*/
            {
                activityFeedDetailsBinding.liLink.setVisibility(VISIBLE);

                if (data.getPublisher().getUser().getAvatar().isEmpty()) {
                    Glide.with(mContext).load(mContext.getResources().getDrawable(R.drawable.user_default)).into(activityFeedDetailsBinding.civUserProfileLink);
                } else {
                    Glide.with(mContext).load(data.getPublisher().getUser().getAvatar()).into(activityFeedDetailsBinding.civUserProfileLink);
                }
                activityFeedDetailsBinding.tvUserNameLink.setText(data.getPublisher().getUser().getName());
                //activityFeedDetailsBinding.tvPostStatusLink.setText(data.getPublisher().getUser().getName());


                activityFeedDetailsBinding.postOptionsLink.setOnClickListener(v -> {
                    BottomSheetDialog(data.getId());
                });


                if (getItemViewType(data) == TYPE_LINK) {
                    activityFeedDetailsBinding.tvLinkPostLink.setVisibility(View.VISIBLE);
                    activityFeedDetailsBinding.postLinkImageLink.setVisibility(View.VISIBLE);
                    activityFeedDetailsBinding.tvPostTextLink.setVisibility(View.VISIBLE);
                    activityFeedDetailsBinding.tvPostContentLink.setVisibility(View.VISIBLE);

                    activityFeedDetailsBinding.tvLinkPostLink.setText(Html.fromHtml(data.getPostLink().replace("\n", "<br>")));
                    if (!data.getPostLinkImage().isEmpty()) {
                        Glide.with(mContext).load(data.getPostLinkImage())
                                .into(activityFeedDetailsBinding.postLinkImageLink);
                    } else {
                        activityFeedDetailsBinding.postLinkImageLink.setVisibility(View.GONE);
                    }
                    activityFeedDetailsBinding.tvPostContentLink.setText(Html.fromHtml(data.getPostLinkContent().replace("\n", "<br>")));

                } else if (getItemViewType(data) == TYPE_YOUTUBE) {
                    activityFeedDetailsBinding.youtubeView.setVisibility(View.VISIBLE);
                    /*new code add on 21-08-23*/
                    activityFeedDetailsBinding.youtubeView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                            String videoId = getVideoKey(data.getPostLink());
                            youTubePlayer.loadVideo(videoId, 0);
                        }
                    });

                }

                activityFeedDetailsBinding.postTextAutolinkLink.setOnClickListener(v -> {
                    Intent i = new Intent(mContext, WebView_Activity.class);
                    i.putExtra(WebView_Activity.WebURL, data.getPostLink());
                    mContext.startActivity(i);
                });

                if (data.getPostText().equalsIgnoreCase("")) {
                    activityFeedDetailsBinding.postTextAutolinkLink.setVisibility(View.GONE);
                } else {
                    activityFeedDetailsBinding.postTextAutolinkLink.setVisibility(View.VISIBLE);
                    String text = data.getPostText().replace("\n", "<br>");
                    //post_text.setText(Html.fromHtml(text));

                    activityFeedDetailsBinding.postTextAutolinkLink.addAutoLinkMode(
                            AutoLinkMode.MODE_HASHTAG,
                            AutoLinkMode.MODE_PHONE,
                            AutoLinkMode.MODE_URL,
                            AutoLinkMode.MODE_EMAIL,
                            AutoLinkMode.MODE_MENTION);
                    activityFeedDetailsBinding.postTextAutolinkLink.setAutoLinkText(Global.capitalizeFirstLatterOfString(data.getPostText_API()));
                    ArrayList<TagModel> tagModelArrayList = data.getTagsusers();
                    //Toaster.customToast(tagModelArrayList.size()+"");
                    activityFeedDetailsBinding.postTextAutolinkLink.setSelectedStateColor(ContextCompat.getColor(mContext, R.color.app_light_red));
                    activityFeedDetailsBinding.postTextAutolinkLink.setHashtagModeColor(ContextCompat.getColor(mContext, R.color.app_light_red));
                    activityFeedDetailsBinding.postTextAutolinkLink.setPhoneModeColor(ContextCompat.getColor(mContext, R.color.app_light_red));
                    activityFeedDetailsBinding.postTextAutolinkLink.setCustomModeColor(ContextCompat.getColor(mContext, R.color.app_light_red));
                    activityFeedDetailsBinding.postTextAutolinkLink.setUrlModeColor(ContextCompat.getColor(mContext, R.color.app_light_red));
                    activityFeedDetailsBinding.postTextAutolinkLink.setMentionModeColor(ContextCompat.getColor(mContext, R.color.app_light_red));
                    activityFeedDetailsBinding.postTextAutolinkLink.setEmailModeColor(ContextCompat.getColor(mContext, R.color.app_light_red));
                    activityFeedDetailsBinding.postTextAutolinkLink.setAutoLinkOnClickListener((autoLinkMode, matchedText) -> {
                        String matched = matchedText.trim();
                        for (int i = 0; i < tagModelArrayList.size(); i++) {
                            //Toaster.customToast(matched+"/"+tagModelArrayList.get(i).getMatch_name().trim());
                            if (matched.trim().equalsIgnoreCase(tagModelArrayList.get(i).getMatch_name().trim())) {


                                Intent intent = new Intent(mContext, UserDetails.class);
                                intent.putExtra("user_id", tagModelArrayList.get(i).getUser_id());
                                intent.putExtra("FROM", "1");
                                intent.putExtra("feed_id", feed_id);
                                startActivity(intent);

                            }
                        }

                    });
                }

                break;
            }
        }
    }

    private ArrayList<String> getKeyList(ArrayList<ImageModel> arrayList) {
        ArrayList<String> list = new ArrayList<>();
        for (ImageModel image : arrayList) {
            list.add(image.getImage());
        }
        return list;
    }

    private String getVideoKey(String url) {
//        String key;
//        if (url.contains("v=")) {
//            key = url.substring(url.lastIndexOf("v=") + 2);
//        } else {
//            key = url.substring(url.lastIndexOf("/") + 1);
//        }
//        Timber.tag("Youtube Key :- ").e(key);
//        return key;

        String pattern = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";

        Pattern compiledPattern = Pattern.compile(pattern,
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = compiledPattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }/*from w  w  w.  j a  va  2 s .c om*/
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != image_uri) {
            final float scale = AGApplication.getContext().getResources().getDisplayMetrics().density;
            int pixels = (int) (150 * scale + 0.5f);
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(MATCH_PARENT, pixels);
            activityFeedDetailsBinding.rlScroll.setLayoutParams(layoutParams);
            activityFeedDetailsBinding.rlScroll.requestLayout();
            activityFeedDetailsBinding.flAttachedImage.setVisibility(View.VISIBLE);
            activityFeedDetailsBinding.rIVAttached.setImageURI(image_uri);
        }
    }

    private void getUserSearchList(String searchKey) {
        //loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.SEARCH_USER_LIST_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SearchUser Response", response);
                //loaderView.hideLoader();
                searchUserArrayList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("users");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            searchUserArrayList.add(new SearchUser(jsonArray.getJSONObject(i)));
                        }

                        if (!searchUserArrayList.isEmpty()) {
                            //tv_notFound.setVisibility(View.GONE);
                            activityFeedDetailsBinding.rvSearchUser.setVisibility(VISIBLE);
                            activityFeedDetailsBinding.rlMain.setVisibility(GONE);
                            activityFeedDetailsBinding.rvSearchUser.setAdapter(new SearchUserAdapter(mContext, searchUserArrayList, username -> {


                                searchUsername = "@" + username;
                                activityFeedDetailsBinding.rvSearchUser.setVisibility(GONE);
                                activityFeedDetailsBinding.rlMain.setVisibility(VISIBLE);
                                if (activityFeedDetailsBinding.editComment.getText().toString().trim().length() > 0) {


                                    Matcher matcher = Pattern.compile("@\\s*(\\w+)").matcher(activityFeedDetailsBinding.editComment.getText().toString().trim());
                                    String find = "";
                                    while (matcher.find()) {
                                        find = matcher.group(1);
                                    }

                                    StringBuffer stringBuffer = new StringBuffer();

                                    String newText = "";
                                    newText = remove(activityFeedDetailsBinding.editComment.getText().toString(), "@" + find);

                                    stringBuffer.append(newText);
                                    stringBuffer.append(" ");
                                    stringBuffer.append(searchUsername);

                                    activityFeedDetailsBinding.editComment.setText(stringBuffer);

                                   activityFeedDetailsBinding.editComment.setSelection(activityFeedDetailsBinding.editComment.getText().toString().length());

                                } else {
                                    activityFeedDetailsBinding.rvSearchUser.setVisibility(GONE);
                                    activityFeedDetailsBinding.rlMain.setVisibility(VISIBLE);
                                    activityFeedDetailsBinding.editComment.setText(searchUsername);
                                }
                            }));
                        } else {
                            //tv_notFound.setVisibility(View.VISIBLE);
                            activityFeedDetailsBinding.rvSearchUser.setVisibility(View.GONE);
                        }
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(mActivity, jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, error -> {
            error.printStackTrace();
            //loaderView.hideLoader();
            Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("search_key", searchKey);

                Log.e("Pavilion", param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    public String remove(String str, String word) {
        String strNew = str.substring(0, str.length() - word.length());

        System.out.print(strNew);
        return strNew;
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (getCurrentFocus() != null) {
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    private void BottomSheetDialog(String id) {
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext, R.style.BottomSheetDialogTheme);
        dialog.setContentView(R.layout.bottom_setting_layout);
        TextView tv_share = dialog.findViewById(R.id.tv_share);
        TextView tv_report = dialog.findViewById(R.id.tv_report);
        TextView tv_editReply = dialog.findViewById(R.id.tv_editReply);
        TextView tv_deleteReply = dialog.findViewById(R.id.tv_deleteReply);

        if (data.getUser_id().equalsIgnoreCase(SessionManager.get_user_id(prefs))) {
            tv_editReply.setVisibility(VISIBLE);
            tv_deleteReply.setVisibility(VISIBLE);
        } else {
            tv_editReply.setVisibility(GONE);
            tv_deleteReply.setVisibility(GONE);
        }
        tv_share.setOnClickListener(v -> {

            if (data.getPost_type().equalsIgnoreCase("text") && !data.getPostFile().isEmpty()) {
                String imageUrl = data.getPostFile();
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());

            } else if (data.getPost_type().equalsIgnoreCase("link") && !data.getPostLinkImage().isEmpty()) {
                String imageUrl = data.getPostLinkImage();
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            } else if (data.getPost_type().equalsIgnoreCase("image")) {
                String imageUrl = data.getPostFile();
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            } else if (data.getPost_type().equalsIgnoreCase("video")) {
                String imageUrl = data.getThumb();
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            } else if (data.getPost_type().equalsIgnoreCase("profile_cover_picture")) {
                String imageUrl = data.getPostFile();
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            } else if (data.getPost_type().equalsIgnoreCase("photo_multi")) {
                String imageUrl = data.getPhoto_multi().get(0).getImage();
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            } else if (data.getPost_type().equalsIgnoreCase("youtube")) {
                String imageUrl = data.getPostLinkImage();
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            } else if (data.getPost_type().equalsIgnoreCase("profile_picture")) {
                String imageUrl = "";
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            } else {
                String imageUrl = "";
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            }


        });

        tv_report.setOnClickListener(v -> {
            dialog.dismiss();
            ReportDialog(id);
        });

        dialog.show();
    }

    public void ReportDialog(final String id) {
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext, R.style.BottomSheetDialogTheme);
        dialog.setContentView(R.layout.alert_dialog_with_edittext);
        final EditText input = (EditText) dialog.findViewById(R.id.editxt);
        FrameLayout ok = dialog.findViewById(R.id.fl_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Global.validateLength(input.getText().toString(), 5)) {
                    input.setError(mContext.getResources().getString(R.string.Enter_your_reason_to_report_this_post));
                } else {
                    input.setError(null);
                    ReportFeed(id, input.getText().toString());
                    dialog.dismiss();
                }
            }
        });
        dialog.show();

    }

    public void ReportFeed(final String id, final String message) {
        // progress.show();
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "report_post", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Pavilion", String.valueOf(response));
                loaderView.hideLoader();
                // progress.dismiss();
                try {
                    JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                        Global.msgDialog(mActivity, getResources().getString(R.string.Post_reported_Successfully));
//                        Toast.makeText(mContext, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
//                        ResetFeed();
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(mActivity, jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loaderView.hideLoader();
                //progress.dismiss();
                Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
//                param.put("s", "1");
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("post_id", id);
                param.put("report_text", message);
                Log.e("Pavilion", param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    /*Share deep link for android and browser*/
    public void generateSharingLink(int postId, String postedBy, String postLink, String postType) {

        Log.d("ImageUrl", postLink + "///" + postType + "?" + postId);

        String deepLink = "https://www.criconet.com/" + "?type=post" + "/" + postId;


        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(deepLink))
                .setDomainUriPrefix("https://criconet.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder()
                                .setTitle(postedBy + " " + "posted on criconet")
                                .setDescription(postedBy + " " + "posted on criconet")
                                .setImageUrl(Uri.parse(postLink))
                                .build())
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.pb.criconet")
                                .setFallbackUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.pb.criconet"))
                                .setMinimumVersion(1)
                                .build())
                .buildShortDynamicLink()

                .addOnCompleteListener(mActivity, task -> {
                    if (task.isSuccessful()) {
                        Uri shortLink = task.getResult().getShortLink();

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Criconet");
                        intent.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
                        startActivity(Intent.createChooser(intent, "send"));

                    } else {
                        Toaster.customToast("Failed to share event.");
                    }
                });


    }


    public void postComment(final String comment, String commentId) {
        //loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "comment_on_post", response -> {
            Log.e("CommentAdd",response);
            Timber.e(String.valueOf(response));
            // loaderView.hideLoader();
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                    activityFeedDetailsBinding.editComment.setText("");

                    if (Global.isOnline(mContext)) {
                        getFeedDetailsAgain();
                    } else {
                        Global.showDialog(mActivity);
                    }

                    //ResetFeed();
                } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                    Global.msgDialog(mActivity, jsonObject.optJSONObject("errors").optString("error_text"));
                } else {
                    Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
            // loaderView.hideLoader();
            Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("post_id", feed_id);
                param.put("comment_id", commentId);
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("text", comment);
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    public void DeleteComment(final String id) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "delete_comment", response -> {
            Timber.e(String.valueOf(response));
            loaderView.hideLoader();
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                    activityFeedDetailsBinding.editComment.setText("");
                    if (Global.isOnline(mContext)) {
                        getFeedDetailsAgain();
                    } else {
                        Global.showDialog(mActivity);
                    }
                } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                    Global.msgDialog(mActivity, jsonObject.optJSONObject("errors").optString("error_text"));
                } else {
                    Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
            loaderView.hideLoader();
            Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("comment_id", id);
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    public void postReplayComment(final String comment, String commentId) {
        //loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.REPLAY_ON_COMMENT, response -> {
            Timber.e(String.valueOf(response));
            // loaderView.hideLoader();
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                    activityFeedDetailsBinding.editComment.setText("");
                    activityFeedDetailsBinding.editComment.setTextColor(mContext.getResources().getColor(R.color.blue_intro_color));
                    commentReplayedOnUserName = "";
                    CommentType = "";


                    if (Global.isOnline(mContext)) {
                        getFeedDetailsAgain();
                    } else {
                        Global.showDialog(mActivity);
                    }

                    //ResetFeed();
                } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                    Global.msgDialog(mActivity, jsonObject.optJSONObject("errors").optString("error_text"));
                } else {
                    Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
            // loaderView.hideLoader();
            Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("comment_id", commentId);
                param.put("reply_id", replyId);
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("text", comment);
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }


    public void getFeedDetailsAgain() {
        // loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_post_data", response -> {
            Timber.e(String.valueOf(response));
            // loaderView.hideLoader();
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                    JSONObject array = jsonObject.getJSONObject("post_data");
                    data = NewPostModel.fromJson(array);
                    setPostData(data);

                    Glide.with(mContext).load(SessionManager.get_image(prefs)).into(activityFeedDetailsBinding.civSenderImage);


                    modelArrayList = new ArrayList<>();
                    modelArrayList.addAll(data.getGet_post_comments());

                    commentAdapter = new CommentAdapter(mContext, data.getPublisher_type(), data.getPage_id(), modelArrayList, new CommentAdapter.itemDeleteInterface() {
                        @Override
                        public void commentDelete(String userId,String commentId, String commentText) {
                            BottomSheetDialogCooment(userId,commentId, commentText);
                        }

                        @Override
                        public void addReplayComment(String Id, String username, String reply) {
                            CommentType = reply;
                            commentId = Id;
                            commentReplayedOnUserName = username;
                            activityFeedDetailsBinding.editComment.setText(username);
                            activityFeedDetailsBinding.editComment.setTextColor(mContext.getResources().getColor(R.color.blue));
                        }

                        @Override
                        public void deleteReply(String userId, String replyId, String commentText, String userNme, String CommentId) {
                            BottomSheetDialogReplyCooment(userId, replyId, commentText, userNme, CommentId);
                        }

                        @Override
                        public void likeComments(String commentId,boolean status) {
                            Toaster.customToast(status+"");
                            if (Global.isOnline(mContext)) {
                                likeCommentss(commentId,status);
                            } else {
                                Global.showDialog(mActivity);
                            }
                        }

                        @Override
                        public void likeReply(String replyId, boolean status) {
                            if(Global.isOnline(mContext)){
                                likeReplies(replyId,status);
                            }else{
                                Global.showDialog(mActivity);
                            }
                        }
                    });
                    activityFeedDetailsBinding.rvComment.setAdapter(commentAdapter);
                    tv_comment_count.setText(data.getCount_post_comments() + " " + "Comments");

                    like_link.setText(data.getCount_post_likes());
                    dislike_link.setText(data.getCount_post_wonders());

                } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                    Global.msgDialog(mActivity, jsonObject.optJSONObject("errors").optString("error_text"));
                } else {
                    Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
            // loaderView.hideLoader();
            Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("post_id", feed_id);
                param.put("s", SessionManager.get_session_id(prefs));
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void BottomSheetDialogCooment(String userId,String id, String commentText) {
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext, R.style.BottomSheetDialogTheme);
        dialog.setContentView(R.layout.bottom_setting_layout);
        TextView tv_share = dialog.findViewById(R.id.tv_share);
        TextView tv_report = dialog.findViewById(R.id.tv_report);
        TextView tv_editReply = dialog.findViewById(R.id.tv_editReply);
        TextView tv_deleteReply = dialog.findViewById(R.id.tv_deleteReply);

        Toaster.customToast(userId+"/"+SessionManager.get_user_id(prefs));
        if (userId.equalsIgnoreCase(SessionManager.get_user_id(prefs))) {
            tv_editReply.setVisibility(VISIBLE);
            tv_deleteReply.setVisibility(VISIBLE);
            tv_report.setVisibility(GONE);
        } else {
            tv_editReply.setVisibility(GONE);
            tv_deleteReply.setVisibility(GONE);
            tv_report.setVisibility(GONE);
        }

        tv_editReply.setOnClickListener(v -> {
            dialog.dismiss();
            CommentType = "EditComment";
            commentId = id;
            activityFeedDetailsBinding.editComment.setText(commentText);
        });

        tv_deleteReply.setOnClickListener(v -> {
            dialog.dismiss();
            if (Global.isOnline(mContext)) {
                DeleteComment(id);
            } else {
                Global.showDialog(mActivity);
            }
        });
        tv_share.setOnClickListener(v -> {

            if (data.getPost_type().equalsIgnoreCase("text") && !data.getPostFile().isEmpty()) {
                String imageUrl = data.getPostFile();
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());

            } else if (data.getPost_type().equalsIgnoreCase("link") && !data.getPostLinkImage().isEmpty()) {
                String imageUrl = data.getPostLinkImage();
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            } else if (data.getPost_type().equalsIgnoreCase("image")) {
                String imageUrl = data.getPostFile();
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            } else if (data.getPost_type().equalsIgnoreCase("video")) {
                String imageUrl = data.getThumb();
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            } else if (data.getPost_type().equalsIgnoreCase("profile_cover_picture")) {
                String imageUrl = data.getPostFile();
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            } else if (data.getPost_type().equalsIgnoreCase("photo_multi")) {
                String imageUrl = data.getPhoto_multi().get(0).getImage();
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            } else if (data.getPost_type().equalsIgnoreCase("youtube")) {
                String imageUrl = data.getPostLinkImage();
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            } else if (data.getPost_type().equalsIgnoreCase("profile_picture")) {
                String imageUrl = "";
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            } else {
                String imageUrl = "";
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            }


        });

        tv_report.setOnClickListener(v -> {
            dialog.dismiss();
            ReportDialog(id);
        });

        dialog.show();
    }

    private void likeFeed(final String id) {
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "like_on_post", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Pavilion", String.valueOf(response));
                try {
                    JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
//                        ResetFeed();
                        if (Global.isOnline(mContext)) {
                            getFeedDetailsAgain();
                        } else {
                            Global.showDialog(mActivity);
                        }

                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(mActivity, jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("post_id", id);
                param.put("s", SessionManager.get_session_id(prefs));
                Log.e("Pavilion", param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void dislikeFeed(final String id) {
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "unlike_on_post", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Pavilion", String.valueOf(response));
                try {
                    JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                        if (Global.isOnline(mContext)) {
                            getFeedDetailsAgain();
                        } else {
                            Global.showDialog(mActivity);
                        }
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(mActivity, jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
//                Global.msgDialog(Login.this, "Internet connection is slow");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("post_id", id);
                param.put("s", SessionManager.get_session_id(prefs));
                Log.e("Pavilion", param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    public void DeleteReply(final String id) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.DELETE_REPLY, response -> {
            Timber.e(String.valueOf(response));
            loaderView.hideLoader();
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                    activityFeedDetailsBinding.editComment.setText("");
                    if (Global.isOnline(mContext)) {
                        getFeedDetailsAgain();
                    } else {
                        Global.showDialog(mActivity);
                    }
                } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                    Global.msgDialog(mActivity, jsonObject.optJSONObject("errors").optString("error_text"));
                } else {
                    Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
            loaderView.hideLoader();
            Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("reply_id", id);
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void BottomSheetDialogReplyCooment(String userId, String reply_id, String commentext, String userneme, String comment_Id) {
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext, R.style.BottomSheetDialogTheme);
        dialog.setContentView(R.layout.bottom_setting_layout);
        TextView tv_share = dialog.findViewById(R.id.tv_share);
        TextView tv_report = dialog.findViewById(R.id.tv_report);
        TextView tv_editReply = dialog.findViewById(R.id.tv_editReply);
        TextView tv_deleteReply = dialog.findViewById(R.id.tv_deleteReply);

        if (userId.equalsIgnoreCase(SessionManager.get_user_id(prefs))) {
            tv_editReply.setVisibility(VISIBLE);
            tv_deleteReply.setVisibility(VISIBLE);
            tv_report.setVisibility(GONE);
        } else {
            tv_editReply.setVisibility(GONE);
            tv_deleteReply.setVisibility(GONE);
            tv_report.setVisibility(GONE);
        }

        tv_editReply.setOnClickListener(v -> {
            dialog.dismiss();
            CommentType = "edit";
            commentId = comment_Id;
            replyId = reply_id;
            activityFeedDetailsBinding.editComment.setText(" " + commentext);
            activityFeedDetailsBinding.editComment.setTextColor(mContext.getResources().getColor(R.color.blue));


        });

        tv_deleteReply.setOnClickListener(v -> {
            dialog.dismiss();
            if (Global.isOnline(mContext)) {
                DeleteReply(reply_id);
            } else {
                Global.showDialog(mActivity);
            }
        });
        tv_share.setOnClickListener(v -> {

            if (data.getPost_type().equalsIgnoreCase("text") && !data.getPostFile().isEmpty()) {
                String imageUrl = data.getPostFile();
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());

            } else if (data.getPost_type().equalsIgnoreCase("link") && !data.getPostLinkImage().isEmpty()) {
                String imageUrl = data.getPostLinkImage();
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            } else if (data.getPost_type().equalsIgnoreCase("image")) {
                String imageUrl = data.getPostFile();
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            } else if (data.getPost_type().equalsIgnoreCase("video")) {
                String imageUrl = data.getThumb();
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            } else if (data.getPost_type().equalsIgnoreCase("profile_cover_picture")) {
                String imageUrl = data.getPostFile();
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            } else if (data.getPost_type().equalsIgnoreCase("photo_multi")) {
                String imageUrl = data.getPhoto_multi().get(0).getImage();
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            } else if (data.getPost_type().equalsIgnoreCase("youtube")) {
                String imageUrl = data.getPostLinkImage();
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            } else if (data.getPost_type().equalsIgnoreCase("profile_picture")) {
                String imageUrl = "";
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            } else {
                String imageUrl = "";
                generateSharingLink(Integer.parseInt(data.getId()), data.getPublisherName(), imageUrl, data.getPost_type());
            }


        });

        tv_report.setOnClickListener(v -> {
            dialog.dismiss();
            ReportDialog(comment_Id);
        });

        dialog.show();
    }


    private void likeCommentss(final String id,boolean status) {

        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.COMMENT_LIKE, response -> {
            Log.e("LikeComments", String.valueOf(response));
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {

                    if (Global.isOnline(mContext)) {
                        getFeedDetailsAgain();
                    } else {
                        Global.showDialog(mActivity);
                    }

                } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                    Global.msgDialog(mActivity, jsonObject.optJSONObject("errors").optString("error_text"));
                } else {
                    Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
            Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("comment_id", id);
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("status",String.valueOf(status));

                Log.e("Pavilion", param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void likeReplies(final String id,boolean status) {
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.COMMENT_REPLY_LIKE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Pavilion", String.valueOf(response));
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                        if (Global.isOnline(mContext)) {
                            getFeedDetailsAgain();
                        } else {
                            Global.showDialog(mActivity);
                        }

                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(mActivity, jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("reply_id", id);
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("status",String.valueOf(status));
                Log.e("Pavilion", param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

}