package com.pb.criconet.Activity.Streaming;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.pb.criconet.R;
import com.pb.criconet.adapter.PavilionAdapter.SearchUserAdapter;
import com.pb.criconet.model.StreamingModel.LiveStreamingNewModel;
import com.pb.criconet.model.pavilionModel.SearchUser;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.Toaster;
import com.potyvideo.library.AndExoPlayerView;

import java.net.URL;
import java.util.ArrayList;

import timber.log.Timber;


public class Play_Live_Stream_Both extends AppCompatActivity {
    private Context mContext;
    private AndExoPlayerView videoview;
    private AndExoPlayerView videoview2;
    private String VideoURL = "", VideoURL2 = "";
    private int selected = 0;
    private TextView cam_1, cam_2, cam_both, desc;
    LinearLayout bottomPanel;
    RelativeLayout toolbar;
    String player_status = "";
    TextView tv_des;
    Animation anim = null;
    Boolean isFullScreen;
    //JSONObject object;
    //liveStreamingNewModel liveStreamingNewModel;
    LiveStreamingNewModel liveStreamingNewModel;
    String FROM = "";
    ExtendedFloatingActionButton extFab_book_live_streaming,extFab_watchParty;
    private ImageView ib_comment;


    RelativeLayout main;


    private RequestQueue queue;

    private Activity mActivity;

    InputMethodManager imm;

    /*..split landscape*/
    LinearLayout li_cam;
    LinearLayout li_split;
    private AndExoPlayerView videoView4_split;
    RelativeLayout topPanel;

    /*search user view initialize here*/
    private RecyclerView rv_searchUser;
    private SearchUserAdapter searchUserAdapter;
    private ArrayList<SearchUser> searchUserArrayList = new ArrayList<>();
    private String searchUsername = "";

    private RecyclerView rv_searchUser_landscape;
    ImageView ib_share;


    //Share Stream link
    Uri shortLink;
    URL url;
    Uri imageUri;
    String deeplink = "";
    CustomLoaderView loaderView;

    //SocketManager socketManager;
    TextView toolbarText;
    String matchId = "";
    ImageView img_back;
   // RelativeLayout rlVideo2;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stream_live_view4);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        mContext = this;
        mActivity = this;


        extFab_watchParty = findViewById(R.id.extFab_watchParty);
        if(extFab_watchParty.getVisibility() == View.VISIBLE){
            extFab_watchParty.setVisibility(View.GONE);
        }

        loaderView = CustomLoaderView.initialize(mContext);
        queue = Volley.newRequestQueue(mContext);
        topPanel = findViewById(R.id.topPanel);


        toolbar = findViewById(R.id.toolbar);
        img_back = toolbar.findViewById(R.id.img_back);
        img_back.setOnClickListener(v -> {
                   finish();
                }

        );
        ib_comment = findViewById(R.id.ib_comment);
        ib_comment.setVisibility(View.GONE);

        anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(3000); //You can manage the blinking time with this parameter
        anim.setStartOffset(30);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        toolbarText = (TextView) toolbar.findViewById(R.id.toolbartext);
        tv_des = toolbar.findViewById(R.id.tv_des);


        tv_des.startAnimation(anim);
        tv_des.setText("CAMERA ONE ACTIVE");
        tv_des.setTextColor(getResources().getColor(R.color.blue_intro_color));
        //TextView desc = (TextView) toolbar.findViewById(R.id.desc);

        //rlVideo2 = findViewById(R.id.rlVideo2);
        videoview = (AndExoPlayerView) findViewById(R.id.VideoView);
        videoview2 = (AndExoPlayerView) findViewById(R.id.VideoView2);


        //....Split landscape view initialize
        li_cam = findViewById(R.id.li_cam);


        cam_1 = (TextView) findViewById(R.id.cam_1);
        cam_2 = (TextView) findViewById(R.id.cam_2);
        cam_both = (TextView) findViewById(R.id.cam_both);
        desc = (TextView) findViewById(R.id.desc);

        bottomPanel = (LinearLayout) findViewById(R.id.bottomPanel);


//        if (Global.isOnline(mContext)) {
//            getMatchDetails();
//        } else {
//            Global.showDialog((Activity) mContext);
//        }


        extFab_book_live_streaming = findViewById(R.id.extFab_book_live_streaming);
        extFab_book_live_streaming.setOnClickListener(view -> {
            Intent intent = new Intent(Play_Live_Stream_Both.this, BookLiveStreamingActivity.class);
            intent.putExtra("FROM", "3");
            startActivity(intent);
            //finish();```````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````
        });

        ib_share = findViewById(R.id.ib_share);
        ib_share.setOnClickListener(v -> {
            generateSharingLink();

        });


        cam_1.setOnClickListener(v -> {
                    finish();
                }
        );
        cam_2.setOnClickListener(v -> {
                    finish();
                }
        );

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //try {
            // object = new JSONObject(bundle.getString("data"));
            liveStreamingNewModel = (LiveStreamingNewModel) bundle.getSerializable("data");
            FROM = bundle.getString("FROM");
            //Timber.e("Data : %s", object);
            Timber.e("Data : %s", liveStreamingNewModel);


            //VideoURL = object.getString("first_player_link");
            VideoURL = liveStreamingNewModel.getFirst_player_link();


            //object.getString("first_player_link");
            //VideoURL2 = object.getString("second_player_link");
            VideoURL2 = liveStreamingNewModel.getSecond_player_link();
            // player_status = object.optString("second_player_status");
            player_status = liveStreamingNewModel.getSecond_player_status();


//                VideoURL = "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8";
//                VideoURL2 = "https://content.jwplatform.com/manifests/yp34SRmf.m3u8";
//                VideoURL2 = "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8";

            //toolbarText.setText(object.getString("title"));

            toolbarText.setText(liveStreamingNewModel.getTitle());

            if (liveStreamingNewModel.getIs_power().equalsIgnoreCase("1")) {
                desc.setVisibility(View.VISIBLE);
                desc.setText(liveStreamingNewModel.getPower_msg());
            } else {
                desc.setVisibility(View.VISIBLE);
                desc.setText(liveStreamingNewModel.getDesc());
            }

            SwitchSelected(3);

            /*Old Api Paarsing*/
//                if (object.getString("is_power").equalsIgnoreCase("1")) {
//                    desc.setVisibility(View.VISIBLE);
//                    desc.setText(object.getString("power_msg"));
//                } else {
//                    desc.setVisibility(View.VISIBLE);
//                    desc.setText(object.getString("desc"));
//                }

//                name1.setText(object.optString("first_player"));
//                name2.setText(object.optString("second_player"));
            //  } catch (JSONException e) {
            //     e.printStackTrace();
            // }

            // generateSharingLink();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        videoview.pausePlayer();
        videoview2.pausePlayer();
        super.onPause();

    }


    //... selected camera...switch method...
    void SwitchSelected(int i) {
        switch (i) {
            case 1:

                videoview.setSource(VideoURL);

                videoview2.pausePlayer();
                videoview2.setVisibility(View.GONE);
                //rlVideo2.setVisibility(View.GONE);

                videoview2.setShowFullScreen(false);
                videoview.setShowFullScreen(true);

                cam_1.setTextColor(getResources().getColor(R.color.light_text_color));
                cam_2.setTextColor(getResources().getColor(R.color.light_text_color));
                cam_both.setTextColor(getResources().getColor(R.color.light_text_color));
                cam_1.setBackground(getResources().getDrawable(R.drawable.round_corner_red_camera));
                cam_2.setBackground(getResources().getDrawable(R.drawable.round_corner_blue_camera));
                cam_both.setBackground(getResources().getDrawable(R.drawable.round_corner_blue_camera));
                tv_des.startAnimation(anim);
                tv_des.setText("CAMERA ONE ACTIVE");
                tv_des.setTextColor(getResources().getColor(R.color.blue_intro_color));
                selected = 1;
                break;
            case 2:
                videoview.setSource(VideoURL2);
                videoview2.setSource("");
                videoview2.pausePlayer();
                videoview2.setVisibility(View.GONE);
                //rlVideo2.setVisibility(View.GONE);

                videoview2.setShowFullScreen(false);
                videoview.setShowFullScreen(true);

                cam_1.setTextColor(getResources().getColor(R.color.light_text_color));
                cam_2.setTextColor(getResources().getColor(R.color.light_text_color));
                cam_both.setTextColor(getResources().getColor(R.color.light_text_color));
                cam_1.setBackground(getResources().getDrawable(R.drawable.round_corner_blue_camera));
                cam_2.setBackground(getResources().getDrawable(R.drawable.round_corner_red_camera));
                cam_both.setBackground(getResources().getDrawable(R.drawable.round_corner_blue_camera));
                tv_des.startAnimation(anim);
                tv_des.setText("CAMERA TWO ACTIVE");
                tv_des.setTextColor(getResources().getColor(R.color.blue_intro_color));
                selected = 2;
                break;
            case 3:

                videoview.setSource(VideoURL);
                videoview.setShowFullScreen(false);

                //rlVideo2.setVisibility(View.VISIBLE);
                videoview2.setVisibility(View.VISIBLE);
                videoview2.setSource(VideoURL2);
                videoview2.setPlayWhenReady(true);
                videoview2.setShowFullScreen(false);


                cam_1.setTextColor(getResources().getColor(R.color.light_text_color));
                cam_2.setTextColor(getResources().getColor(R.color.light_text_color));
                cam_both.setTextColor(getResources().getColor(R.color.light_text_color));
                cam_1.setBackground(getResources().getDrawable(R.drawable.round_corner_blue_camera));
                cam_2.setBackground(getResources().getDrawable(R.drawable.round_corner_blue_camera));
                cam_both.setBackground(getResources().getDrawable(R.drawable.round_corner_red_camera));
                tv_des.startAnimation(anim);
                tv_des.setText("BOTH CAMERA ACTIVE");
                tv_des.setTextColor(getResources().getColor(R.color.blue_intro_color));
                selected = 3;
                break;
            default:
                videoview.setSource(VideoURL);

                videoview2.pausePlayer();
                videoview2.setVisibility(View.GONE);
               // rlVideo2.setVisibility(View.GONE);

                tv_des.startAnimation(anim);
                tv_des.setText("CAMERA ONE ACTIVE");
                tv_des.setTextColor(getResources().getColor(R.color.blue_intro_color));
                selected = 1;
                break;
        }


    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

//            if (videoview2.getParent() != null) {
//                ViewGroup parent = (ViewGroup) videoview2.getParent();
//                parent.removeView(videoview2);
//            }


            videoview2.pausePlayer();
            videoview2.setVisibility(View.GONE);
           // rlVideo2.setVisibility(View.GONE);
            getSupportActionBar().hide();
            bottomPanel.setVisibility(View.GONE);
            desc.setVisibility(View.GONE);
            extFab_book_live_streaming.setVisibility(View.GONE);
        } else {
            main.setVisibility(View.VISIBLE);
            getSupportActionBar().show();
            bottomPanel.setVisibility(View.VISIBLE);
            desc.setVisibility(View.VISIBLE);
            extFab_book_live_streaming.setVisibility(View.VISIBLE);
        }
        super.onConfigurationChanged(newConfig);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }


   /* private void getMatchDetails() {
        //progress.show();
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.GET_LIVE_STREAMING_MATCH_DETAILS,
                response -> {
                    loaderView.hideLoader();
                    //progress.dismiss();
                    try {
                        Log.d("Match Details Response", response);
                        JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                        if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {

                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                            liveStreamingNewModel = new LiveStreamingNewModel(jsonObject1);

                            //VideoURL = object.getString("first_player_link");
                            VideoURL = liveStreamingNewModel.getFirst_player_link();


                            //object.getString("first_player_link");
                            //VideoURL2 = object.getString("second_player_link");
                            VideoURL2 = liveStreamingNewModel.getSecond_player_link();
                            // player_status = object.optString("second_player_status");
                            player_status = liveStreamingNewModel.getSecond_player_status();
                            if (player_status.equalsIgnoreCase("0")) {
                                cam_1.setVisibility(View.VISIBLE);
                                cam_2.setVisibility(View.GONE);
                                cam_both.setVisibility(View.GONE);
                            } else {
                                cam_1.setVisibility(View.VISIBLE);
                                cam_2.setVisibility(View.VISIBLE);
                                cam_both.setVisibility(View.VISIBLE);
                            }


//                VideoURL = "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8";
//                VideoURL2 = "https://content.jwplatform.com/manifests/yp34SRmf.m3u8";
//                VideoURL2 = "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8";

                            //toolbarText.setText(object.getString("title"));

                            toolbarText.setText(liveStreamingNewModel.getTitle());
                            if (liveStreamingNewModel.getIs_power().equalsIgnoreCase("1")) {
                                desc.setVisibility(View.VISIBLE);
                                desc.setText(liveStreamingNewModel.getPower_msg());
                            } else {
                                desc.setVisibility(View.VISIBLE);
                                desc.setText(liveStreamingNewModel.getDesc());
                            }

                            //videoview.setSource(VideoURL);

                            SwitchSelected(3);
//                            if (FROM.equalsIgnoreCase("1")) {
//                                SwitchSelected(1);
//                            } else if (FROM.equalsIgnoreCase("2")) {
//                                SwitchSelected(2);
//                            } else {
//                                SwitchSelected(3);
//                            }

                        } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                            Global.msgDialog((Activity) mContext, jsonObject.optJSONObject("errors").optString("error_text"));
                        } else {
                            Global.msgDialog((Activity) mContext, getResources().getString(R.string.error_server));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    //progress.dismiss();
                    loaderView.hideLoader();
                    Global.msgDialog((Activity) mContext, getResources().getString(R.string.error_server));
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("match_id", matchId);

                System.out.println("data   " + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }*/


    public void generateSharingLink() {

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.criconet.com/livematch/" + liveStreamingNewModel.getId() + "?type=live_streaming" + "/" + liveStreamingNewModel.getId()))
                .setDomainUriPrefix("https://criconet.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder()
                                .setTitle(liveStreamingNewModel.getTitle())
                                .setDescription(liveStreamingNewModel.getDesc())
                                .setImageUrl(Uri.parse("https://www.criconet.com/upload/notification/live-streaming-notification.jpg"))
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

                        deeplink = liveStreamingNewModel.getTitle() + "\n" + "\n" + "*" + liveStreamingNewModel.getDesc() + "*" + "\n" + "\n" + shortLink;
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Criconet");
                        intent.putExtra(Intent.EXTRA_TEXT, deeplink);
                        startActivity(Intent.createChooser(intent, "send"));
                    } else {
                        Toaster.customToast("Failed to share event.");
                    }
                });


    }


}








