package com.pb.criconet.Activity.Streaming;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.pb.criconet.Activity.MainActivity;
import com.pb.criconet.R;
import com.pb.criconet.Socket.SocketManager;
import com.pb.criconet.adapter.PavilionAdapter.SearchUserAdapter;
import com.pb.criconet.adapter.Streaming.LiveMatchCommentAdapter;
import com.pb.criconet.adapter.Streaming.LiveMatchEmojiAdapter;
import com.pb.criconet.model.EmojiModel;
import com.pb.criconet.model.StreamingModel.LiveMatchCommentModel;
import com.pb.criconet.model.StreamingModel.LiveStreamingNewModel;
import com.pb.criconet.model.pavilionModel.SearchUser;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.KeyboardHeightHelper;
import com.pb.criconet.util.SessionManager;
import com.pb.criconet.util.Toaster;
import com.potyvideo.library.AndExoPlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;


public class Play_Live_Stream_Single extends AppCompatActivity {
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
    FrameLayout btnShare;
    ExtendedFloatingActionButton extFab_book_live_streaming, extFab_watchParty, extFab_start_recording, extFab_stop_recording;
    ExtendedFloatingActionButton share;

//    LinearLayout li_toggle;
//    String name = "";
//
//    //.....recording...12-07-22
//
//    private static final String TAG = "DATARECORDER";
//    private static final int PERMISSION_CODE = 1;
//    private MediaProjectionManager mProjectionManager;
//    private ToggleButton mToggleButton;
//    boolean isRecording;

    /*Initialize Comment code variable here..*/

    private RelativeLayout slideView;
    private ImageView ib_comment;
    private TextView tv_watching;
    private ImageView iv_close;
    private ImageView iv_emoji;
    private ImageView iv_send;
    private EditText edit_chat;
    private RecyclerView rv_comment;
    private Context mContext;
    private ArrayList<LiveMatchCommentModel> liveMatchCommentModels = new ArrayList<>();
    private SharedPreferences prefs;
    private LiveMatchCommentAdapter liveMatchCommentAdapter;
    private RelativeLayout rl_emoji;
    private RelativeLayout middle;
    private RecyclerView rv_emoji;
    private LiveMatchEmojiAdapter liveMatchEmojiAdapter;

    private RelativeLayout rl_emoji_zoom;
    private ImageView iv_emoji_zoom_close;
    private ImageView iv_send_emoji;
    private ImageView iv_emoji_zoom;
    private String zoom_emoji_image = "";
    private ImageView iv_comment_landscape;

    //......Landscap mode initialize variable
    private RelativeLayout rl_landscap;
    private ImageView iv_close_landscap;
    private TextView tv_watching_landscap;
    private RecyclerView rv_comment_landscap;
    private ImageView iv_emoji_landscap;
    private EditText edit_chat_landscap;
    private ImageView iv_send_landscap;
    private RelativeLayout rl_emoji_landscap;
    private RecyclerView rv_emoji_landscap;
    private RelativeLayout rl_emoji_zoom_landscap;
    private ImageView iv_emoji_zoom_close_landscap;
    private ImageView iv_send_emoji_landscap;
    private ImageView iv_emoji_zoom_landscap;

    private RelativeLayout rl_landscape_chat;
    private RelativeLayout rl_landscape_emoji_reclerview;

    private int height;
    private int width;
    private int simpleHeight;
    RelativeLayout main;
    RelativeLayout li_emo;

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

    SocketManager socketManager;
    TextView toolbarText;
    String matchId = "";
    ShapeableImageView imageViewAttached;

    LinearLayout liCreateWatchParty;
    TextView tVWatchPartyTerms;
    ImageView iv_close_watchParty;
    MaterialButton mtbCreateWatchParty;

    LinearLayout liJoinWatchParty;
    TextView textViewJoinTerms;
    ImageView iv_close_JoinParty;
    MaterialButton mtbJoinWatchParty;
    String type = "";

    //WatchParty ChatBox

    RelativeLayout rlwatchPartyChat;
    SwitchCompat switchControllerPlayer;
    ImageView iv_close_watchPartyChat;
    RelativeLayout middlewatchparty;
    RecyclerView rv_comment_watchparty;
    RelativeLayout li_emo_watchparty;
    RecyclerView rv_emoji_watchparty;
    RelativeLayout bottom_watchparty;
    ImageView iv_emoji_watchparty;
    ShapeableImageView imageViewAttached_watchparty;
    EditText edit_chat_watchparty;
    ImageView iv_send_watchparty;
    TextView username_watchparty;

    String party_group_txt = "";
    String party_group_id = "";
    String created_by = "";
    String createdMatchId = "";
    String CAMERA_ONE_STATUS = "";
    String CAMERA_TWO_STATUS = "";
    String CAMERA_TEXT = "";
    String created_by_name = "";

    ImageView ib_share_watchPartyLink;
    TextView tv_watchPartyOwnerName;
    TextView tv_community;

    boolean isWatchPartyClose = false;

    public static ArrayList<EmojiModel> emojiModelsList;

    ImageView img_back;
    //RelativeLayout rlVideo2;


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

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //try {
            // object = new JSONObject(bundle.getString("data"));
            matchId = bundle.getString("matchId");
            FROM = bundle.getString("FROM");
            type = bundle.getString("type");
            party_group_id = bundle.getString("partyGroupId");
            party_group_txt = bundle.getString("partyGroupTxt");


            // Toaster.customToast(type);
            //Timber.e("Data : %s", object);
            Timber.e("Data : %s", matchId + "/" + party_group_txt + "?" + party_group_id);


            //VideoURL = object.getString("first_player_link");
//            VideoURL = liveStreamingNewModel.getFirst_player_link();
//
//
//            //object.getString("first_player_link");
//            //VideoURL2 = object.getString("second_player_link");
//            VideoURL2 = liveStreamingNewModel.getSecond_player_link();
//            // player_status = object.optString("second_player_status");
//            player_status = liveStreamingNewModel.getSecond_player_status();
//            if (player_status.equalsIgnoreCase("0")) {
//                cam_1.setVisibility(View.VISIBLE);
//                cam_2.setVisibility(View.GONE);
//                cam_both.setVisibility(View.GONE);
//            } else {
//                cam_1.setVisibility(View.VISIBLE);
//                cam_2.setVisibility(View.VISIBLE);
//                cam_both.setVisibility(View.VISIBLE);
//            }
//
//
////                VideoURL = "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8";
////                VideoURL2 = "https://content.jwplatform.com/manifests/yp34SRmf.m3u8";
////                VideoURL2 = "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8";
//
//            //toolbarText.setText(object.getString("title"));
//
//            toolbarText.setText(liveStreamingNewModel.getTitle());
//            if (liveStreamingNewModel.getIs_power().equalsIgnoreCase("1")) {
//                desc.setVisibility(View.VISIBLE);
//                desc.setText(liveStreamingNewModel.getPower_msg());
//            } else {
//                desc.setVisibility(View.VISIBLE);
//                desc.setText(liveStreamingNewModel.getDesc());
//            }


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


        /*JOIN ROOM FOR CHAT*/
        socketManager = new SocketManager();
        socketManager.connect();

        socketManager.onPing(new SocketManager.Listener() {
            @Override
            public void onReceived(JSONObject message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = message;
                        String ping;
                        try {
                            ping = data.getString("ping");
                        } catch (JSONException e) {
                            return;
                        }
                        if (ping.equals("1")) {
                            socketManager.getSocket().emit("pong", "pong");
                        }
                        Log.e("SOCKETPING", "RECEIVED PING! ");
                    }
                });
            }
        });

        socketManager.JoinRoom(matchId);

        loaderView = CustomLoaderView.initialize(mContext);
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        queue = Volley.newRequestQueue(mContext);
        topPanel = findViewById(R.id.topPanel);
        slideView = findViewById(R.id.slideView);

        new KeyboardHeightHelper(
                (Activity) mContext,
                slideView,
                keyboardHeight -> slideView.setPadding(0, 0, 0, keyboardHeight));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        simpleHeight = height / 3;
        //simpleHeight = (simpleHeight * 2) - 300;
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        /*ScreenRecording...*/
//        mProjectionManager = (MediaProjectionManager) getSystemService (Context.MEDIA_PROJECTION_SERVICE);
//        mToggleButton = (ToggleButton) findViewById(R.id.toggle);
//
//        isRecording = isServiceRunning(RecordService.class);
//        if(isRecording){
//            mToggleButton.setChecked(true);
//        }
//
//        mToggleButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onToggleScreenShare(v);
//            }
//        });
        isFullScreen = false;
        toolbar = findViewById(R.id.toolbar);
        img_back = toolbar.findViewById(R.id.img_back);
        img_back.setOnClickListener(v -> {
                    if (FROM.equalsIgnoreCase("4")) {
                        startActivity(new Intent(mContext, MainActivity.class).putExtra("type", "live_streaming"));
                        finish();
                    } else {
                        finish();
                    }
                }

        );
        anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(3000); //You can manage the blinking time with this parameter
        anim.setStartOffset(30);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        toolbarText = toolbar.findViewById(R.id.toolbartext);
        tv_des = toolbar.findViewById(R.id.tv_des);


        tv_des.startAnimation(anim);
        tv_des.setText("CAMERA ONE ACTIVE");
        tv_des.setTextColor(getResources().getColor(R.color.blue_intro_color));
        //TextView desc = (TextView) toolbar.findViewById(R.id.desc);

       // rlVideo2 = findViewById(R.id.rlVideo2);
        videoview = (AndExoPlayerView) findViewById(R.id.VideoView);
        videoview2 = (AndExoPlayerView) findViewById(R.id.VideoView2);


        //....Split landscape view initialize
        li_cam = findViewById(R.id.li_cam);
//        li_split= findViewById(R.id.li_split);
//        videoView4_split = (AndExoPlayerView) findViewById(R.id.videoView4_split);


//        height_videoView = videoview.getHeight();
//        height_videoView2 = videoview2.getHeight();


        cam_1 = (TextView) findViewById(R.id.cam_1);
        cam_2 = (TextView) findViewById(R.id.cam_2);
        cam_both = (TextView) findViewById(R.id.cam_both);
        desc = (TextView) findViewById(R.id.desc);

        bottomPanel = (LinearLayout) findViewById(R.id.bottomPanel);
        //btnShare = findViewById(R.id.btnShare);
        //li_toggle = findViewById(R.id.li_toggle);


        if (Global.isOnline(mContext)) {
            getMatchDetails();
        } else {
            Global.showDialog((Activity) mContext);
        }

        if (Global.isOnline(mContext)) {
            getEmojiList();
        } else {
            Global.showDialog((Activity) mContext);
        }

        if (Global.isOnline(mContext)) {
            getCommentList(0);
        } else {
            Global.showDialog((Activity) mContext);
        }


        socketManager.onMessageReceived(message -> {
            Log.d("onReceived", message.toString());

            runOnUiThread(() -> {
                liveMatchCommentModels.add(0, new LiveMatchCommentModel(message));

                // liveMatchCommentModels.remove(liveMatchCommentModels.size()-1);
                //Log.d("Size",liveMatchCommentModels.size()+"");

                liveMatchCommentAdapter.notifyDataSetChanged();

            });

        });

        socketManager.getPlayerControl(message -> {
            Log.d("onReceivedControl", message.toString());

            runOnUiThread(() -> {


                try {
                    if (!SessionManager.getCreatedBy(prefs).equalsIgnoreCase(message.getString("UserId"))) {
                        if (message.getString("Camera1").equalsIgnoreCase("ON")) {
                            cam_1.setVisibility(View.VISIBLE);
                            cam_2.setVisibility(View.GONE);
                            SwitchSelected(1);

                        } else {
                            cam_1.setVisibility(View.GONE);
                            cam_2.setVisibility(View.VISIBLE);
                            SwitchSelected(2);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // here we check condition for operate camera one and camera two..

            });
        });


        // Set up event listeners for ExoPlayer
        //Player control of ExoPlayer...
//      videoview.getPlayer().addListener(new Player.EventListener() {
//          @Override
//          public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//              Player.EventListener.super.onPlayerStateChanged(playWhenReady, playbackState);
//
//
//              switch (playbackState) {
//                  case Player.STATE_BUFFERING:
//                      // Player is buffering
//                      break;
//                  case Player.STATE_READY:
//                      if (playWhenReady) {
//
//                          Toaster.customToast("Play");
//                          // Player is playing
//                          // Handle play event here
//                      } else {
//                          Toaster.customToast("Pause");
//                          // Player is paused
//                          // Handle pause event here
//                      }
//                      break;
//                  case Player.STATE_ENDED:
//                      // Player has ended playback
//                      break;
//                  case Player.STATE_IDLE:
//                      // Player is idle
//                      break;
//              }
//          }
//      });

//        socketManager.onMessageReceiveParty(new SocketManager.Listener() {
//            @Override
//            public void onReceived(JSONObject message) {
//                Log.d("onReceivedPartyJoin", message.toString());
//            }
//        });


//        videoview.requestFocus();
//        videoview.start();


        extFab_book_live_streaming = findViewById(R.id.extFab_book_live_streaming);
        extFab_book_live_streaming.setOnClickListener(view -> {
            Intent intent = new Intent(Play_Live_Stream_Single.this, BookLiveStreamingActivity.class);
            intent.putExtra("FROM", "3");
            startActivity(intent);
            //finish();```````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````
        });

        ib_share = findViewById(R.id.ib_share);
        ib_share.setOnClickListener(v -> {
            generateSharingLink();
        });



        /*Share the link of watch party on criconet...*/

        tv_watchPartyOwnerName = findViewById(R.id.tv_watchPartyOwnerName);
        ib_share_watchPartyLink = findViewById(R.id.ib_share_watchPartyLink);
        ib_share_watchPartyLink.setOnClickListener(v -> {
            createWatchPartySharingLink();
        });

        ib_comment = findViewById(R.id.ib_comment);

        /*Create watch party*/

        rlwatchPartyChat = findViewById(R.id.rlwatchPartyChat);

        rlwatchPartyChat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Consume touch events on the foreground layout
                return true; // Return true to indicate that the touch event is handled
            }
        });
        //switchControllerPlayer = findViewById(R.id.switchControllerPlayer);
        iv_close_watchPartyChat = findViewById(R.id.iv_close_watchPartyChat);
        middlewatchparty = findViewById(R.id.middlewatchparty);
        rv_comment_watchparty = findViewById(R.id.rv_comment_watchparty);
        li_emo_watchparty = findViewById(R.id.li_emo_watchparty);
        rv_emoji_watchparty = findViewById(R.id.rv_emoji_watchparty);
        bottom_watchparty = findViewById(R.id.bottom_watchparty);
        iv_emoji_watchparty = findViewById(R.id.iv_emoji_watchparty);
        imageViewAttached_watchparty = findViewById(R.id.imageViewAttached_watchparty);
        edit_chat_watchparty = findViewById(R.id.edit_chat_watchparty);
        iv_send_watchparty = findViewById(R.id.iv_send_watchparty);


        mtbCreateWatchParty = findViewById(R.id.mtbCreateWatchParty);
        iv_close_watchParty = findViewById(R.id.iv_close_watchParty);
        tVWatchPartyTerms = findViewById(R.id.textView);
        liCreateWatchParty = findViewById(R.id.liCreateWatchParty);

        /*Join Watch party*/
        liJoinWatchParty = findViewById(R.id.liJoinWatchParty);
        textViewJoinTerms = findViewById(R.id.textViewJoinTerms);
        iv_close_JoinParty = findViewById(R.id.iv_close_JoinParty);
        mtbJoinWatchParty = findViewById(R.id.mtbJoinWatchParty);

        SpannableString spannableString = new SpannableString("By viewing, you agree to our Watch Party Community Guidelines.");

        // Apply different span styles

        //spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 5, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //spannableString.setSpan(new RelativeSizeSpan(1.5f), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        ClickableSpan clickableSpanTerms = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Handle click action here, for example, open a browser with a URL
                // You can also handle the click event differently, like launching another activity, etc.
                String url = "https://www.google.com";
                // Example: open URL in a browser
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        };


        ClickableSpan clickableSpanGuidelIne = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //startActivity(new Intent(mContext,CommunityGudelineActivity.class));
            }
        };

        spannableString.setSpan(clickableSpanGuidelIne, 29, 62, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.verified_user_color)), 29, 62, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Set the spannable text to TextView
        tVWatchPartyTerms.setText(spannableString);
        tVWatchPartyTerms.setMovementMethod(LinkMovementMethod.getInstance());

        textViewJoinTerms.setText(spannableString);
        textViewJoinTerms.setMovementMethod(LinkMovementMethod.getInstance());


        extFab_watchParty = findViewById(R.id.extFab_watchParty);
        extFab_watchParty.setOnClickListener(v -> {

            //Open create watch party layout..
            liCreateWatchParty.setVisibility(View.VISIBLE);

        });

        iv_close_watchParty.setOnClickListener(v -> {
            liCreateWatchParty.setVisibility(View.GONE);
        });

        mtbCreateWatchParty.setOnClickListener(v -> {

            if (Global.isOnline(mContext)) {
                getCreateWatchParty();
            } else {
                Global.showDialog((Activity) mContext);
            }

        });


        /*Join watch party*/


        if (FROM.equalsIgnoreCase("4") && type.equals("create")) {
            ib_comment.setVisibility(View.GONE);
            ib_share.setVisibility(View.VISIBLE);
            ib_share_watchPartyLink.setVisibility(View.GONE);
            liJoinWatchParty.setVisibility(View.VISIBLE);

            //Toaster.customToast("c");
        } else if (SessionManager.getCreatedBy(prefs).equalsIgnoreCase(SessionManager.get_user_id(prefs))
                && matchId.equalsIgnoreCase(SessionManager.getCreatedMatchId(prefs)) && SessionManager.getPartyGroupId(prefs).equalsIgnoreCase(party_group_id)) {
            ib_comment.setVisibility(View.GONE);
            ib_share.setVisibility(View.VISIBLE);
            liJoinWatchParty.setVisibility(View.VISIBLE);
            //Toaster.customToast("d");
        } else if (matchId.equalsIgnoreCase(SessionManager.getCreatedMatchId(prefs)) && SessionManager.getPartyGroupId(prefs).equalsIgnoreCase(party_group_id)) {
            ib_comment.setVisibility(View.GONE);
            ib_share.setVisibility(View.VISIBLE);
            liJoinWatchParty.setVisibility(View.VISIBLE);
            //Toaster.customToast("e");
        } else {
            ib_share.setVisibility(View.VISIBLE);
            ib_comment.setVisibility(View.VISIBLE);
            liJoinWatchParty.setVisibility(View.GONE);
            //Toaster.customToast("f");
        }

        iv_close_JoinParty.setOnClickListener(v -> {
            isWatchPartyClose = true;
            cam_both.setVisibility(View.VISIBLE);
            cam_2.setVisibility(View.VISIBLE);
            cam_1.setVisibility(View.VISIBLE);
            ib_comment.setVisibility(View.VISIBLE);
            liJoinWatchParty.setVisibility(View.GONE);

        });


        //join

        mtbJoinWatchParty.setOnClickListener(v -> {

            if (SessionManager.getCreatedBy(prefs).equalsIgnoreCase(SessionManager.get_user_id(prefs)) && SessionManager.getPartyGroupId(prefs).equalsIgnoreCase(party_group_id)) {


                JSONObject payloadJson = new JSONObject();

                try {
                    payloadJson.put("party_group_txt", SessionManager.getPartyGroupTxt(prefs));
                    payloadJson.put("party_group_id", SessionManager.getPartyGroupId(prefs));
                    payloadJson.put("user_id", SessionManager.get_user_id(prefs));

                    socketManager.createWatchParty(payloadJson);

                    if (Global.isOnline(mContext)) {
                        getCommentList(Integer.parseInt(SessionManager.getPartyGroupId(prefs)));
                    } else {
                        Global.showDialog((Activity) mContext);
                    }
                    ib_comment.setVisibility(View.GONE);
                    ib_share.setVisibility(View.VISIBLE);
                    ib_share_watchPartyLink.setVisibility(View.VISIBLE);
                    liJoinWatchParty.setVisibility(View.GONE);
                    rlwatchPartyChat.setVisibility(View.VISIBLE);
                    cam_both.setVisibility(View.GONE);


                    socketManager.onPing(message -> runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject data = message;
                            String ping;
                            try {
                                ping = data.getString("ping");
                            } catch (JSONException e) {
                                return;
                            }
                            if (ping.equals("1")) {
                                socketManager.getSocket().emit("pong", "pong");
                            }
                            Log.e("SOCKETPING", "RECEIVED PING! ");
                        }
                    }));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

                //Toaster.customToast("jpinAgain");

                JSONObject payloadJson = new JSONObject();
                try {
                    payloadJson.put("party_group_txt", party_group_txt);
                    payloadJson.put("party_group_id", party_group_id);
                    payloadJson.put("user_id", SessionManager.get_user_id(prefs));

                    socketManager.createWatchParty(payloadJson);

                    if (Global.isOnline(mContext)) {
                        getCommentList(Integer.parseInt(party_group_id));
                    } else {
                        Global.showDialog((Activity) mContext);
                    }
                    ib_comment.setVisibility(View.GONE);
                    ib_share.setVisibility(View.VISIBLE);
                    liJoinWatchParty.setVisibility(View.GONE);
                    rlwatchPartyChat.setVisibility(View.VISIBLE);


                    cam_1.setVisibility(View.VISIBLE);
                    cam_2.setVisibility(View.GONE);
                    cam_both.setVisibility(View.GONE);

                    socketManager.onPing(new SocketManager.Listener() {
                        @Override
                        public void onReceived(JSONObject message) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    JSONObject data = message;
                                    String ping;
                                    try {
                                        ping = data.getString("ping");
                                    } catch (JSONException e) {
                                        return;
                                    }
                                    if (ping.equals("1")) {
                                        socketManager.getSocket().emit("pong", "pong");
                                    }
                                    Log.e("SOCKETPING", "RECEIVED PING! ");
                                }
                            });
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        });


        // Chat variable initialize here
        main = findViewById(R.id.main);
        //nested = findViewById(R.id.nested);
        li_emo = findViewById(R.id.li_emo);


        middle = findViewById(R.id.middle);

        simpleHeight = simpleHeight * 2;
        slideView.getLayoutParams().height = simpleHeight;
        //middle.getLayoutParams().height = simpleHeight - 300;


        cam_1.setOnClickListener(v -> {
                    simpleHeight = 0;
                    SwitchSelected(1);
                    simpleHeight = height / 3;
                    simpleHeight = simpleHeight * 2;
                    slideView.getLayoutParams().height = simpleHeight;
                    //middle.getLayoutParams().height = simpleHeight - 300;

                    if (!isWatchPartyClose == true) {
                        CAMERA_ONE_STATUS = "ON";
                        CAMERA_TWO_STATUS = "OFF";
                        CAMERA_TEXT = "Camera one Enable";
                        JSONObject payloadJson = new JSONObject();
                        try {
                            payloadJson.put("Camera1", CAMERA_ONE_STATUS);
                            payloadJson.put("Camera2", CAMERA_TWO_STATUS);
                            payloadJson.put("UserId", SessionManager.get_user_id(prefs));

                            if (FROM.equalsIgnoreCase("4") && type.equalsIgnoreCase("create")) {
                                payloadJson.put("party_group_txt", party_group_txt);
                                payloadJson.put("party_group_id", party_group_id);
                                payloadJson.put("comment_text", CAMERA_TEXT);
                                payloadJson.put("avatar", SessionManager.get_image(prefs));
                                payloadJson.put("username", SessionManager.get_user_name(prefs));
                                payloadJson.put("name", SessionManager.get_name(prefs));
                            } else {
                                payloadJson.put("party_group_txt", SessionManager.getPartyGroupTxt(prefs));
                                payloadJson.put("party_group_id", SessionManager.getPartyGroupId(prefs));
                                payloadJson.put("comment_text", CAMERA_TEXT);
                                payloadJson.put("avatar", SessionManager.get_image(prefs));
                                payloadJson.put("username", SessionManager.get_user_name(prefs));
                                payloadJson.put("name", SessionManager.get_name(prefs));
                            }

                            socketManager.onPing(message -> runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    JSONObject data = message;
                                    String ping;
                                    try {
                                        ping = data.getString("ping");
                                    } catch (JSONException e) {
                                        return;
                                    }
                                    if (ping.equals("1")) {
                                        socketManager.getSocket().emit("pong", "pong");
                                    }
                                    Log.e("SOCKETPING", "RECEIVED PING! ");
                                }
                            }));

                            socketManager.playerControl(payloadJson);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }
        );
        cam_2.setOnClickListener(v -> {
                    simpleHeight = 0;
                    SwitchSelected(2);
                    simpleHeight = height / 3;
                    simpleHeight = simpleHeight * 2;
                    slideView.getLayoutParams().height = simpleHeight;
                    //middle.getLayoutParams().height = simpleHeight - 300;

                    if (!isWatchPartyClose == true) {
                        CAMERA_ONE_STATUS = "OFF";
                        CAMERA_TWO_STATUS = "ON";
                        CAMERA_TEXT = "Camera two Enable";
                        JSONObject payloadJson = new JSONObject();
                        try {
                            payloadJson.put("Camera1", CAMERA_ONE_STATUS);
                            payloadJson.put("Camera2", CAMERA_TWO_STATUS);
                            payloadJson.put("UserId", SessionManager.get_user_id(prefs));
                            if (FROM.equalsIgnoreCase("4") && type.equalsIgnoreCase("create")) {
                                payloadJson.put("party_group_txt", party_group_txt);
                                payloadJson.put("party_group_id", party_group_id);
                                payloadJson.put("comment_text", CAMERA_TEXT);
                                payloadJson.put("avatar", SessionManager.get_image(prefs));
                                payloadJson.put("username", SessionManager.get_user_name(prefs));
                                payloadJson.put("name", SessionManager.get_name(prefs));
                            } else {
                                payloadJson.put("party_group_txt", SessionManager.getPartyGroupTxt(prefs));
                                payloadJson.put("party_group_id", SessionManager.getPartyGroupId(prefs));
                                payloadJson.put("comment_text", CAMERA_TEXT);
                                payloadJson.put("avatar", SessionManager.get_image(prefs));
                                payloadJson.put("username", SessionManager.get_user_name(prefs));
                                payloadJson.put("name", SessionManager.get_name(prefs));
                            }

                            socketManager.onPing(message -> runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    JSONObject data = message;
                                    String ping;
                                    try {
                                        ping = data.getString("ping");
                                    } catch (JSONException e) {
                                        return;
                                    }
                                    if (ping.equals("1")) {
                                        socketManager.getSocket().emit("pong", "pong");
                                    }
                                    Log.e("SOCKETPING", "RECEIVED PING! ");
                                }
                            }));
                            socketManager.playerControl(payloadJson);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }
        );
        cam_both.setOnClickListener(v -> {
                    //selected = 3;
                    //SwitchSelected(3);
                    startActivity(new Intent(mContext, Play_Live_Stream_Both.class).putExtra("data", liveStreamingNewModel));
                }
        );

        rl_landscape_chat = findViewById(R.id.rl_landscape_chat);

        tv_watching = findViewById(R.id.tv_watching);
        iv_close = findViewById(R.id.iv_close);
        iv_close.setOnClickListener(v -> {

            if (selected == 3) {
                //rlVideo2.setVisibility(View.VISIBLE);
                videoview2.setVisibility(View.VISIBLE);
            }
            main.setVisibility(View.VISIBLE);
            slideView.setVisibility(View.GONE);
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

        });

        //........landscap mode chat.....
//        iv_comment_landscape = findViewById(R.id.iv_comment_landscape);
//        iv_comment_landscape.setOnClickListener(v -> {
//
//            //.....Split
////            li_cam.setVisibility(View.GONE);
////            li_split.setVisibility(View.VISIBLE);
////            topPanel.setBackgroundColor(getResources().getColor(R.color.black));
////
////            if(selected == 1){
////                videoView4_split.setSource(VideoURL);
////                videoview2.pausePlayer();
////                videoview2.setVisibility(View.GONE);
////                videoView4_split.setShowFullScreen(true);
////            }else{
////                videoView4_split.setSource(VideoURL2);
////                videoview.setShowFullScreen(true);
////                videoview2.setShowFullScreen(true);
////                videoview2.pausePlayer();
////            }
//
//            //....without split
//            if (rl_landscap.getVisibility() == View.GONE) {
//                rl_landscap.setVisibility(View.VISIBLE);
//            } else {
//                rl_landscap.setVisibility(View.VISIBLE);
//            }
//        });

        //.... this recyclerview is showing search user list

        rv_searchUser = findViewById(R.id.rv_searchUser);
        rv_searchUser.setLayoutManager(new LinearLayoutManager(this));
        rv_searchUser.setHasFixedSize(true);

        rv_searchUser_landscape = findViewById(R.id.rv_searchUser_landscape);
        rv_searchUser_landscape.setLayoutManager(new LinearLayoutManager(this));
        rv_searchUser_landscape.setHasFixedSize(true);


        edit_chat = findViewById(R.id.edit_chat);
        edit_chat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Toaster.customToast(s+"");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    iv_send.setVisibility(View.GONE);
                } else {
                    iv_send.setVisibility(View.VISIBLE);
                }

                Timber.e("%s", s);


//                if (s.length() == 0) {
//                    rv_searchUser.setVisibility(View.GONE);
//                    searchUsername = "";
//                } else {
//                    String search = s.toString();
//                    String[] parts = search.split(" ");
//                    String lastWord = parts[parts.length - 1];
//
//                    if (search.contains("\n")) {
//                        parts = search.split("\n");
//                    } else {
//                        parts = search.split(" ");
//                    }
//
//                    if (search.startsWith(" ")) {
//                        lastWord = search;
//                    } else {
//                        lastWord = parts[parts.length - 1];
//                    }
//                    //Toaster.customToast(lastWord);
//                    if (lastWord.contains("@") && lastWord.length() > 3) {
//
//                        Matcher matcher = Pattern.compile("@\\s*(\\w+)").matcher(lastWord);
//
//                        while (matcher.find()) {
//                            if (searchUsername.isEmpty()) {
//                                if (Global.isOnline(mContext)) {
//                                    getUserSearchList(matcher.group(1));
//                                } else {
//                                    Global.showDialog(mActivity);
//                                }
//                            }
//                        }
//
//                    } else {
//                        rv_searchUser.setVisibility(View.GONE);
//                        searchUsername = "";
//                    }
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Toaster.customToast(s+"");

            }
        });

        rv_comment = findViewById(R.id.rv_comment);
        rv_comment.setHasFixedSize(true);
        rv_comment.setLayoutManager(new LinearLayoutManager(this));


        ib_comment.setOnClickListener(v -> {

//            if (selected == 3) {
//                videoview2.setVisibility(View.GONE);
//            } else {
//                // videoview2.setVisibility(View.VISIBLE);
//            }

            if (selected == 3) {
                if (videoview2.getVisibility() == View.VISIBLE) {
                    videoview2.setVisibility(View.GONE);
                   // rlVideo2.setVisibility(View.GONE);
                }
            }

            main.setVisibility(View.GONE);
            slideView.setVisibility(View.VISIBLE);
        });


        imageViewAttached = findViewById(R.id.imageViewAttached);
        rv_emoji = findViewById(R.id.rv_emoji);
        rv_emoji.setHasFixedSize(true);
        rv_emoji.setLayoutManager(new GridLayoutManager(this, 3));
//        liveMatchEmojiAdapter = new LiveMatchEmojiAdapter(mContext, LiveMatches.emojiModelsList, (image, name) -> {
//            if (iv_send.getVisibility() == View.GONE) {
//                iv_send.setVisibility(View.VISIBLE);
//            }
//            //edit_chat.setText("");
//
//            zoom_emoji_image = image;
//
//            if (imageViewAttached.getVisibility() == View.GONE) {
//                imageViewAttached.setVisibility(View.VISIBLE);
//            }
//
//            Glide.with(mContext).load(zoom_emoji_image).into(imageViewAttached);
//            rv_emoji.setVisibility(View.GONE);
//
////            if (Global.isOnline(mContext)) {
////                getSendCommentResponse("",
////                        zoom_emoji_image, "1");
////            } else {
////                Global.showDialog((Activity) mContext);
////            }
//        });
//        rv_emoji.setAdapter(liveMatchEmojiAdapter);
        iv_emoji = findViewById(R.id.iv_emoji);
        iv_emoji.setOnClickListener(v -> {

            if (rv_emoji.getVisibility() == View.VISIBLE) {
                rv_emoji.setVisibility(View.GONE);
            } else {
                rv_emoji.setVisibility(View.VISIBLE);
            }
        });

        rl_emoji_zoom = findViewById(R.id.rl_emoji_zoom);
        iv_emoji_zoom_close = findViewById(R.id.iv_emoji_zoom_close);
        iv_emoji_zoom_close.setOnClickListener(v -> {
            if (rl_emoji_zoom.getVisibility() == View.VISIBLE) {
                rl_emoji_zoom.setVisibility(View.GONE);
                rv_emoji.setVisibility(View.VISIBLE);
            }
        });

        iv_send_emoji = findViewById(R.id.iv_send_emoji);
        iv_emoji_zoom = findViewById(R.id.iv_emoji_zoom);

        // rl_emoji = findViewById(R.id.rl_emoji);


//        iv_send_emoji.setOnClickListener(v -> {
//            if (rl_emoji_zoom.getVisibility() == View.VISIBLE) {
//                rl_emoji_zoom.setVisibility(View.GONE);
//            }
//            if (Global.isOnline(mContext)) {
//                getSendCommentResponse("",
//                        zoom_emoji_image, "1");
//            } else {
//                Global.showDialog((Activity) mContext);
//            }
//        });


        iv_send = findViewById(R.id.iv_send);


        iv_send.setOnClickListener(v -> {

            sendSocketMessageLoad(edit_chat.getText().toString().trim());

            edit_chat.setText("");
            zoom_emoji_image = "";
            if (imageViewAttached.getVisibility() == View.VISIBLE) {
                imageViewAttached.setVisibility(View.GONE);
            }

//            if (iv_send.getVisibility() == View.VISIBLE) {
//                iv_send.setVisibility(View.GONE);
//            }
//            if (Global.isOnline(mContext)) {
//                getSendCommentResponse(edit_chat.getText().toString().trim(),
//                        "", "0");
//            } else {
//                Global.showDialog((Activity) mContext);
//            }
//            edit_chat.setText("");

        });


        edit_chat.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                //                    // Send the text of EditTeere
                String text = edit_chat.getText().toString();

                Toaster.customToast(text);
                // Perform your action with the text, like sending it to another activity or a server
                return true;
            }
            return false;
        });


        /*WatchParty ChatBox */

        edit_chat_watchparty = findViewById(R.id.edit_chat_watchparty);
        edit_chat_watchparty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Toaster.customToast(s+"");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    iv_send_watchparty.setVisibility(View.GONE);
                } else {
                    iv_send_watchparty.setVisibility(View.VISIBLE);
                }

                Timber.e("%s", s);


//                if (s.length() == 0) {
//                    rv_searchUser.setVisibility(View.GONE);
//                    searchUsername = "";
//                } else {
//                    String search = s.toString();
//                    String[] parts = search.split(" ");
//                    String lastWord = parts[parts.length - 1];
//
//                    if (search.contains("\n")) {
//                        parts = search.split("\n");
//                    } else {
//                        parts = search.split(" ");
//                    }
//
//                    if (search.startsWith(" ")) {
//                        lastWord = search;
//                    } else {
//                        lastWord = parts[parts.length - 1];
//                    }
//                    //Toaster.customToast(lastWord);
//                    if (lastWord.contains("@") && lastWord.length() > 3) {
//
//                        Matcher matcher = Pattern.compile("@\\s*(\\w+)").matcher(lastWord);
//
//                        while (matcher.find()) {
//                            if (searchUsername.isEmpty()) {
//                                if (Global.isOnline(mContext)) {
//                                    getUserSearchList(matcher.group(1));
//                                } else {
//                                    Global.showDialog(mActivity);
//                                }
//                            }
//                        }
//
//                    } else {
//                        rv_searchUser.setVisibility(View.GONE);
//                        searchUsername = "";
//                    }
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Toaster.customToast(s+"");

            }
        });

        rv_comment_watchparty = findViewById(R.id.rv_comment_watchparty);
        rv_comment_watchparty.setHasFixedSize(true);
        rv_comment_watchparty.setLayoutManager(new LinearLayoutManager(this));


        imageViewAttached_watchparty = findViewById(R.id.imageViewAttached_watchparty);
        rv_emoji_watchparty = findViewById(R.id.rv_emoji_watchparty);
        rv_emoji_watchparty.setHasFixedSize(true);
        rv_emoji_watchparty.setLayoutManager(new GridLayoutManager(this, 3));
      /*  liveMatchEmojiAdapter = new LiveMatchEmojiAdapter(mContext, LiveMatches.emojiModelsList, (image, name) -> {
            if (iv_send_watchparty.getVisibility() == View.GONE) {
                iv_send_watchparty.setVisibility(View.VISIBLE);
            }
            //edit_chat.setText("");

            zoom_emoji_image = image;

            if (imageViewAttached_watchparty.getVisibility() == View.GONE) {
                imageViewAttached_watchparty.setVisibility(View.VISIBLE);
            }

            Glide.with(mContext).load(zoom_emoji_image).into(imageViewAttached_watchparty);
            rv_emoji_watchparty.setVisibility(View.GONE);

//            if (Global.isOnline(mContext)) {
//                getSendCommentResponse("",
//                        zoom_emoji_image, "1");
//            } else {
//                Global.showDialog((Activity) mContext);
//            }

        });
        rv_emoji_watchparty.setAdapter(liveMatchEmojiAdapter);*/
        iv_emoji_watchparty = findViewById(R.id.iv_emoji_watchparty);
        iv_emoji_watchparty.setOnClickListener(v -> {

            if (li_emo_watchparty.getVisibility() == View.VISIBLE && rv_emoji_watchparty.getVisibility() == View.VISIBLE) {
                li_emo_watchparty.setVisibility(View.GONE);
                rv_emoji_watchparty.setVisibility(View.GONE);
            } else {
                rv_emoji_watchparty.setVisibility(View.VISIBLE);
                li_emo_watchparty.setVisibility(View.VISIBLE);
            }
        });

        iv_send_watchparty.setOnClickListener(v -> {

            sendSocketMessageLoad(edit_chat_watchparty.getText().toString().trim());

            edit_chat_watchparty.setText("");
            zoom_emoji_image = "";
            if (imageViewAttached_watchparty.getVisibility() == View.VISIBLE) {
                imageViewAttached_watchparty.setVisibility(View.GONE);
            }
        });

        iv_close_watchPartyChat.setOnClickListener(v -> {

            if (FROM.equalsIgnoreCase("4") && type.equals("create")) {
                ib_comment.setVisibility(View.GONE);
                ib_share.setVisibility(View.VISIBLE);
                liJoinWatchParty.setVisibility(View.VISIBLE);
                rlwatchPartyChat.setVisibility(View.GONE);

            } else if (SessionManager.getCreatedBy(prefs).equalsIgnoreCase(SessionManager.get_user_id(prefs))
                    && matchId.equalsIgnoreCase(SessionManager.getCreatedMatchId(prefs))) {
                ib_comment.setVisibility(View.GONE);
                ib_share.setVisibility(View.VISIBLE);
                liJoinWatchParty.setVisibility(View.VISIBLE);
                rlwatchPartyChat.setVisibility(View.GONE);
            } else if (matchId.equalsIgnoreCase(SessionManager.getCreatedMatchId(prefs)) && party_group_id.equalsIgnoreCase(SessionManager.getPartyGroupId(prefs))) {
                ib_comment.setVisibility(View.GONE);
                ib_share.setVisibility(View.VISIBLE);
                liJoinWatchParty.setVisibility(View.VISIBLE);
                rlwatchPartyChat.setVisibility(View.GONE);
            } else {
                ib_share.setVisibility(View.VISIBLE);
                ib_comment.setVisibility(View.VISIBLE);
                liJoinWatchParty.setVisibility(View.GONE);
                rlwatchPartyChat.setVisibility(View.GONE);
            }

        });


        // initialize all landscape mode commnet variable
//        rl_landscape_emoji_reclerview = findViewById(R.id.rl_landscape_emoji_reclerview);
//        rl_landscap = findViewById(R.id.rl_landscap);
//
//        iv_close_landscap = findViewById(R.id.iv_close_landscap);
//        iv_close_landscap.setOnClickListener(v -> {
//
//
//            //.....Split
////            li_cam.setVisibility(View.VISIBLE);
////            li_split.setVisibility(View.GONE);
//
//            //..without //.....Split
//            if (rl_landscap.getVisibility() == View.VISIBLE) {
//                rl_landscap.setVisibility(View.GONE);
//                rl_landscape_emoji_reclerview.setVisibility(View.GONE);
//            }
//
//        });
//
//        tv_watching_landscap = findViewById(R.id.tv_watching_landscap);
//        rv_comment_landscap = findViewById(R.id.rv_comment_landscap);
//        rv_comment_landscap.setHasFixedSize(true);
//        rv_comment_landscap.setLayoutManager(new LinearLayoutManager(this));
//        iv_emoji_landscap = findViewById(R.id.iv_emoji_landscap);
//
//        edit_chat_landscap = findViewById(R.id.edit_chat_landscap);
//        edit_chat_landscap.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                //Toaster.customToast(s+"");
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length() == 0) {
//                    iv_send_landscap.setVisibility(View.GONE);
//                } else {
//                    iv_send_landscap.setVisibility(View.VISIBLE);
//                }
//
//                if (s.length() == 0) {
//                    rv_searchUser.setVisibility(View.GONE);
//                    searchUsername = "";
//                } else {
//                    String search = s.toString();
//                    String[] parts = null;
//                    String lastWord = "";
//
//
//                    if (search.contains("\n")) {
//                        parts = search.split("\n");
//                    } else {
//                        parts = search.split(" ");
//                    }
//
//                    if (search.startsWith(" ")) {
//                        lastWord = search;
//                    } else {
//                        lastWord = parts[parts.length - 1];
//                    }
//
//                    //lastWord = parts[parts.length - 1];
//
//                    String finalWord = lastWord.substring(lastWord.lastIndexOf(" ") + 1);
//
//                    //Toaster.customToast(lastWord);
//                    if (finalWord.contains("@") && finalWord.length() > 3) {
//
//                        Matcher matcher = Pattern.compile("@\\s*(\\w+)").matcher(finalWord);
//
//                        while (matcher.find()) {
//                            if (searchUsername.isEmpty()) {
//                                if (Global.isOnline(mContext)) {
//                                    getUserSearchListLandscape(matcher.group(1));
//                                } else {
//                                    Global.showDialog(mActivity);
//                                }
//                            }
//                        }
//
//                    } else {
//                        rv_searchUser_landscape.setVisibility(View.GONE);
//                        searchUsername = "";
//                    }
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                //Toaster.customToast(s+"");
//
//            }
//        });
//
//        iv_send_landscap = findViewById(R.id.iv_send_landscap);
//        rl_emoji_landscap = findViewById(R.id.rl_emoji_landscap);
//
//        rv_emoji_landscap = findViewById(R.id.rv_emoji_landscap);
//        rv_emoji_landscap.setHasFixedSize(true);
//        rv_emoji_landscap.setLayoutManager(new GridLayoutManager(this, 2));
//        liveMatchEmojiAdapter = new LiveMatchEmojiAdapter(mContext, LiveMatches.emojiModelsList, (image, name) -> {
//            zoom_emoji_image = name;
//
//            Toaster.customToast(zoom_emoji_image);
//
//            Glide.with(mContext).load(zoom_emoji_image).into(imageViewAttached);
//
//            if (rl_landscape_emoji_reclerview.getVisibility() == View.VISIBLE) {
//                rl_landscape_emoji_reclerview.setVisibility(View.GONE);
//            }
//
////            rl_landscape_chat.setVisibility(View.GONE);
////
////            rl_emoji_zoom_landscap.setVisibility(View.VISIBLE);
////            Glide.with(mContext).load(image).into(iv_emoji_zoom_landscap);
////            if (Global.isOnline(mContext)) {
////                getSendCommentResponse("",
////                        zoom_emoji_image, "1");
////            } else {
////                Global.showDialog((Activity) mContext);
////            }
//        });
//        rv_emoji_landscap.setAdapter(liveMatchEmojiAdapter);
//
//        rl_emoji_zoom_landscap = findViewById(R.id.rl_emoji_zoom_landscap);
//        iv_emoji_zoom_landscap = findViewById(R.id.iv_emoji_zoom_landscap);
//
//        iv_emoji_zoom_close_landscap = findViewById(R.id.iv_emoji_zoom_close_landscap);
//        iv_emoji_zoom_close_landscap.setOnClickListener(v -> {
//            if (rl_emoji_zoom_landscap.getVisibility() == View.VISIBLE) {
//                rl_emoji_zoom_landscap.setVisibility(View.GONE);
//                rl_landscape_chat.setVisibility(View.VISIBLE);
//                rl_emoji_zoom_landscap.setVisibility(View.GONE);
//                rl_landscape_emoji_reclerview.setVisibility(View.GONE);
//            }
//        });
//
//        iv_send_emoji_landscap = findViewById(R.id.iv_send_emoji_landscap);
//        iv_send_emoji_landscap.setOnClickListener(v -> {
//            if (rl_emoji_zoom_landscap.getVisibility() == View.VISIBLE) {
//                rl_emoji_zoom_landscap.setVisibility(View.GONE);
//                rl_landscape_chat.setVisibility(View.VISIBLE);
//                rl_emoji_zoom_landscap.setVisibility(View.GONE);
//                rl_landscape_emoji_reclerview.setVisibility(View.GONE);
//            }
//            if (Global.isOnline(mContext)) {
//                getSendCommentResponse("",
//                        zoom_emoji_image, "1");
//            } else {
//                Global.showDialog((Activity) mContext);
//            }
//        });
//
//        iv_emoji_landscap.setOnClickListener(v -> {
//
//            if (rl_landscape_emoji_reclerview.getVisibility() == View.VISIBLE) {
//                rl_landscape_emoji_reclerview.setVisibility(View.GONE);
//            } else {
//                rl_landscape_emoji_reclerview.setVisibility(View.VISIBLE);
//            }
//        });
//
//        iv_send_landscap.setOnClickListener(v -> {
//            if (iv_send_landscap.getVisibility() == View.VISIBLE) {
//                iv_send_landscap.setVisibility(View.GONE);
//            }
//            if (Global.isOnline(mContext)) {
//                getSendCommentResponse(edit_chat_landscap.getText().toString().trim(),
//                        "", "0");
//            } else {
//                Global.showDialog((Activity) mContext);
//            }
//            edit_chat_landscap.setText("");
//
//        });

        /*Close all instance of landscap of comments here*/

        /*Socket initialize here*/

        /*Socket initialize here*/


    }

    @Override
    protected void onResume() {
//        if(isRecording){
//            mToggleButton.setChecked(false);
//        }
        super.onResume();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //socketManager.disconnect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //handler.removeCallbacks(runnable);
        //socketManager.disconnect();
    }

    @Override
    protected void onPause() {
        videoview.pausePlayer();
        videoview2.pausePlayer();
//        stopRecordingService();
//        if(isRecording){
//            mToggleButton.setChecked(false);
//        }

        super.onPause();

    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        // Your code here
//        if(ev.getOrientation()==0.0){
//            li_toggle.setVisibility(View.GONE);
//        }else{
//            if(li_toggle.getVisibility()==View.VISIBLE){
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        li_toggle.setVisibility(View.GONE);
//                    }
//                },10000);
//            }else{
//                li_toggle.setVisibility(View.VISIBLE);
//            }
//        }
//
//        return super.dispatchTouchEvent(ev);
//    }


    //... selected camera...switch method...
    void SwitchSelected(int i) {
        switch (i) {
            case 1:

                videoview.setSource(VideoURL);

                videoview2.pausePlayer();
                videoview2.setVisibility(View.GONE);
               // rlVideo2.setVisibility(View.GONE);

                videoview2.setShowFullScreen(false);
                videoview.setShowFullScreen(true);

                cam_1.setBackground(getResources().getDrawable(R.drawable.round_corner_red_camera));
                cam_1.setTextColor(getResources().getColor(R.color.light_text_color));

                cam_2.setTextColor(getResources().getColor(R.color.light_text_color));
                cam_2.setBackground(getResources().getDrawable(R.drawable.round_corner_blue_camera));

                cam_both.setTextColor(getResources().getColor(R.color.light_text_color));
                cam_both.setBackground(getResources().getDrawable(R.drawable.round_corner_blue_camera));
                tv_des.startAnimation(anim);
                tv_des.setText("CAMERA ONE ACTIVE");
                tv_des.setTextColor(getResources().getColor(R.color.blue_intro_color));
                selected = 1;
                break;
            case 2:

                videoview.setSource(VideoURL2);

                videoview2.pausePlayer();
                videoview2.setVisibility(View.GONE);
               // rlVideo2.setVisibility(View.GONE);

                videoview2.setShowFullScreen(false);
                videoview.setShowFullScreen(true);

                cam_2.setTextColor(getResources().getColor(R.color.light_text_color));
                cam_2.setBackground(getResources().getDrawable(R.drawable.round_corner_red_camera));

                cam_1.setTextColor(getResources().getColor(R.color.light_text_color));
                cam_1.setBackground(getResources().getDrawable(R.drawable.round_corner_blue_camera));

                cam_both.setTextColor(getResources().getColor(R.color.light_text_color));
                cam_both.setBackground(getResources().getDrawable(R.drawable.round_corner_blue_camera));

                tv_des.startAnimation(anim);
                tv_des.setText("CAMERA TWO ACTIVE");
                tv_des.setTextColor(getResources().getColor(R.color.blue_intro_color));
                selected = 2;
                break;

//            case 3:
//
//                videoview.setSource(VideoURL);
//
//                videoview2.setVisibility(View.VISIBLE);
//                videoview2.setSource(VideoURL2);
//
//                videoview2.setShowFullScreen(false);
//                videoview.setShowFullScreen(false);
//
//
//                cam_1.setTextColor(getResources().getColor(R.color.bckground));
//                cam_2.setTextColor(getResources().getColor(R.color.bckground));
//                cam_both.setTextColor(getResources().getColor(R.color.light_text_color));
//                cam_1.setBackground(getResources().getDrawable(R.drawable.round_border_red));
//                cam_2.setBackground(getResources().getDrawable(R.drawable.round_border_red));
//                cam_both.setBackground(getResources().getDrawable(R.drawable.round_corner_red));
//                tv_des.startAnimation(anim);
//                tv_des.setText("BOTH CAMERA ACTIVE");
//                tv_des.setTextColor(getResources().getColor(R.color.logWarn));
//                selected = 3;
//                break;

            default:
                videoview.setSource(VideoURL);

                videoview2.pausePlayer();
                videoview2.setVisibility(View.GONE);
              //  rlVideo2.setVisibility(View.GONE);

                cam_1.setBackground(getResources().getDrawable(R.drawable.round_corner_blue_camera));
                cam_1.setTextColor(getResources().getColor(R.color.light_text_color));
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
          //  rlVideo2.setVisibility(View.GONE);
            toolbar.setVisibility(View.GONE);
            bottomPanel.setVisibility(View.GONE);
            desc.setVisibility(View.GONE);
            extFab_book_live_streaming.setVisibility(View.GONE);
        } else {
            main.setVisibility(View.VISIBLE);
            slideView.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
            bottomPanel.setVisibility(View.VISIBLE);
            desc.setVisibility(View.VISIBLE);
            extFab_book_live_streaming.setVisibility(View.VISIBLE);
        }
        super.onConfigurationChanged(newConfig);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (isFullScreen) {
            if (selected == 1) {
                Intent intent = new Intent(this, Play_Live_Stream_Single.class);
                intent.putExtra("data", (Serializable) liveStreamingNewModel);
                intent.putExtra("FROM", "1");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                videoview.setShowFullScreen(false);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                Intent intent = new Intent(this, Play_Live_Stream_Single.class);
                intent.putExtra("data", (Serializable) liveStreamingNewModel);
                intent.putExtra("FROM", "2");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                videoview2.setShowFullScreen(false);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

        } else {
            if (FROM.equalsIgnoreCase("4")) {
                startActivity(new Intent(mContext, MainActivity.class).putExtra("type", "live_streaming"));
                finish();
            } else {
                finish();
            }
        }

    }


    private void getMatchDetails() {
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

                            if (FROM.equalsIgnoreCase("1")) {
                                SwitchSelected(1);
                            } else if (FROM.equalsIgnoreCase("2")) {
                                SwitchSelected(2);
                            } else {
                                SwitchSelected(3);
                            }

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
    }

    // get Live Comment listing.......
    private void getCommentList(int party_group_idd) {
        //progress.show();
        //loaderView.showLoader();
        //https://chat.criconetonline.com/getChat?match_id=5&party_grpup_id=12

        //Log.d("matchId",matchId);

        StringRequest postRequest = new StringRequest(Request.Method.GET, "https://chat.criconetonline.com/getChat?match_id=" + matchId
                + "&" + "party_group_id=" + party_group_idd,
                response -> {
                    // loaderView.hideLoader();
                    //progress.dismiss();
                    try {
                        Log.d("RequestedParam", "https://chat.criconetonline.com/getChat?match_id=" + matchId
                                + "&" + "party_group_id=" + party_group_idd);

                        Log.d("Comment Response", response);

                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.optString("status").equalsIgnoreCase("200")) {

                            created_by_name = jsonObject.getString("created_by_name");

                            if (created_by_name.isEmpty()) {
                                tv_watchPartyOwnerName.setVisibility(View.GONE);
                            } else {
                                tv_watchPartyOwnerName.setVisibility(View.VISIBLE);
                                tv_watchPartyOwnerName.setText("Party owner is " + created_by_name);
                            }


                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if (liveMatchCommentModels.size() > 0) {
                                liveMatchCommentModels.clear();
                            }

                            for (int i = 0; i < jsonArray.length(); i++) {
                                liveMatchCommentModels.add(new LiveMatchCommentModel(jsonArray.getJSONObject(i)));
                            }

                            //Log.d("Size1",liveMatchCommentModels.size()+"");

                            Collections.reverse(liveMatchCommentModels);

                            liveMatchCommentAdapter = new LiveMatchCommentAdapter(mContext, liveMatchCommentModels);
                            rv_comment.setAdapter(liveMatchCommentAdapter);
                            rv_comment_watchparty.setAdapter(liveMatchCommentAdapter);


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
                    //loaderView.hideLoader();
                    Global.msgDialog((Activity) mContext, getResources().getString(R.string.error_server));
                }
        ) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> param = new HashMap<String, String>();
////                param.put("user_id", SessionManager.get_user_id(prefs));
////                param.put("s", SessionManager.get_session_id(prefs));
//                param.put("match_id", liveStreamingNewModel.getId());
//
//                System.out.println("data   " + param);
//                return param;
//            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void sendSocketMessageLoad(String text) {

        JSONObject jsonObject = new JSONObject();
        try {
            int type;
            if (zoom_emoji_image.equalsIgnoreCase("")) {
                type = 0;
            } else {
                type = 1;
            }
            jsonObject.put("user_id", SessionManager.get_user_id(prefs));
            jsonObject.put("match_id", matchId);
            jsonObject.put("comment_text", text);
            jsonObject.put("emojiImage", zoom_emoji_image);
            jsonObject.put("type", type);
            jsonObject.put("avatar", SessionManager.get_image(prefs));
            jsonObject.put("username", SessionManager.get_user_name(prefs));
            jsonObject.put("name", SessionManager.get_name(prefs));
            jsonObject.put("party_group_id", party_group_id);
            jsonObject.put("party_group_txt", party_group_txt);

            Log.d("JSON", jsonObject.toString());

            socketManager.sendMessage(jsonObject.toString());

            // liveMatchCommentModels.add(0, new LiveMatchCommentModel(jsonObject));
            //liveMatchCommentAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    //.... add comment request to the server and get response..
    private void getSendCommentResponse(String text, String emoji, String type) {
        //progress.show();
        //loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.ADD_LIVE_STREAMING_COMMENT,
                response -> {
                    // loaderView.hideLoader();
                    //progress.dismiss();
                    try {
                        Log.d("Live Stream Response", response);
                        JSONObject jsonObject2, jsonObject = new JSONObject(response);
                        if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {

                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if (liveMatchCommentModels.size() > 0) {
                                liveMatchCommentModels.clear();
                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                liveMatchCommentModels.add(new LiveMatchCommentModel(jsonArray.getJSONObject(i)));
                            }
                            Collections.reverse(liveMatchCommentModels);
                            liveMatchCommentAdapter = new LiveMatchCommentAdapter(mContext, liveMatchCommentModels);

                            rv_comment.setAdapter(liveMatchCommentAdapter);
                            rv_comment_landscap.setAdapter(liveMatchCommentAdapter);


                            if (jsonObject.has("emoji")) {
                                //emojiModelsList = getEmojiLis(jsonObject.getJSONArray("emoji"));
                            }
//
//                            if (dataNew.size() == 0) {
//                                notfound.setVisibility(View.VISIBLE);
//                            } else {
//                                notfound.setVisibility(View.GONE);
//                                adapter = new LiveMatchAdapter(dataNew,getContext());
//                                weeklist.setAdapter(adapter);
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
                    //loaderView.hideLoader();
                    Global.msgDialog((Activity) mContext, getResources().getString(R.string.error_server));
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("match_id", liveStreamingNewModel.getId());
                param.put("comment_text", text);
                param.put("emojiImage", emoji);
                param.put("avtar", SessionManager.get_image(prefs));
                param.put("username", SessionManager.get_name(prefs));
                param.put("type", type);

                System.out.println("data   " + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    /*get search user list by api*/
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

                        if (searchUserArrayList.isEmpty()) {
                            //tv_notFound.setVisibility(View.VISIBLE);
                            rv_searchUser.setVisibility(View.GONE);
                        } else {
                            //tv_notFound.setVisibility(View.GONE);
                            rv_searchUser.setVisibility(View.VISIBLE);
                            rv_searchUser.setAdapter(new SearchUserAdapter(mContext, searchUserArrayList, new SearchUserAdapter.searchUserItemClick() {
                                @Override
                                public void getSearchUserName(String username) {

                                    searchUsername = "@" + username;
                                    if (edit_chat.getText().toString().trim().length() > 0) {
                                        rv_searchUser.setVisibility(View.GONE);
                                        Matcher matcher = Pattern.compile("@\\s*(\\w+)").matcher(edit_chat.getText().toString().trim());
                                        String find = "";
                                        while (matcher.find()) {
                                            find = matcher.group(1);
                                        }

                                        StringBuffer stringBuffer = new StringBuffer();

                                        String newText = "";

                                        if (edit_chat.getText().toString().trim().contains("\n")) {
                                            newText = remove(edit_chat.getText().toString().trim(), "@" + find);
                                        } else {
                                            newText = remove(edit_chat.getText().toString().trim(), "@" + find);
                                        }
                                        stringBuffer.append(newText);
                                        stringBuffer.append(searchUsername);

                                        edit_chat.setText(stringBuffer);

//                                        if(edit_chat.getText().toString().trim().startsWith("@")){
//                                            edit_chat.setText(remove(searchUsername+" "+edit_chat.getText().toString().trim(),"@"+find));
//                                        }else{
//                                            edit_chat.setText(remove(edit_chat.getText().toString().trim(),"@"+find)+" "+searchUsername);
//                                        }
                                        edit_chat.setSelection(edit_chat.getText().toString().length());

                                    } else {
                                        rv_searchUser.setVisibility(View.GONE);
                                        edit_chat.setText(searchUsername);
                                    }
                                }
                            }));
                        }
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog((Activity) mActivity, jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog((Activity) mActivity, getResources().getString(R.string.error_server));
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

                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    /*get search user list by api*/
    private void getUserSearchListLandscape(String searchKey) {
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

                        if (searchUserArrayList.isEmpty()) {
                            //tv_notFound.setVisibility(View.VISIBLE);
                            rv_searchUser_landscape.setVisibility(View.GONE);
                        } else {
                            //tv_notFound.setVisibility(View.GONE);
                            rv_searchUser_landscape.setVisibility(View.VISIBLE);
                            rv_searchUser_landscape.setAdapter(new SearchUserAdapter(mContext, searchUserArrayList, new SearchUserAdapter.searchUserItemClick() {
                                @Override
                                public void getSearchUserName(String username) {

                                    searchUsername = "@" + username;
                                    if (edit_chat_landscap.getText().toString().trim().length() > 0) {
                                        rv_searchUser_landscape.setVisibility(View.GONE);
                                        Matcher matcher = Pattern.compile("@\\s*(\\w+)").matcher(edit_chat_landscap.getText().toString().trim());
                                        String find = "";
                                        while (matcher.find()) {
                                            find = matcher.group(1);
                                        }
                                        StringBuffer stringBuffer = new StringBuffer();
                                        String newText = remove(edit_chat_landscap.getText().toString().trim(), "@" + find);
                                        stringBuffer.append(newText);
                                        stringBuffer.append(searchUsername);
                                        edit_chat_landscap.setText(stringBuffer.toString());
                                        /*if(edit_chat_landscap.getText().toString().trim().startsWith("@")){
                                            edit_chat_landscap.setText(remove(searchUsername+" "+edit_chat_landscap.getText().toString().trim(),"@"+find));
                                        }else{
                                            edit_chat_landscap.setText(remove(edit_chat_landscap.getText().toString().trim(),"@"+find)+" "+searchUsername);
                                        }*/
                                        edit_chat_landscap.setSelection(edit_chat_landscap.getText().toString().length());

                                    } else {
                                        rv_searchUser_landscape.setVisibility(View.GONE);
                                        edit_chat_landscap.setText(searchUsername);
                                    }
                                }
                            }));
                        }
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog((Activity) mActivity, jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog((Activity) mActivity, getResources().getString(R.string.error_server));
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

                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    public String remove(String str, String word) {


//        String msg[] = str.contains("\n") ? str.trim().split("\n") : str.trim().split(" ");
//
//        //String msg[] = str.trim().split("");
//        String new_str = "";
//
//        for (String words : msg) {
//
//                if (!words.equals(word)) {
//
//                    new_str += words + " ";
//                }
//
        String strNew = str.substring(0, str.length() - word.length());

        System.out.print(strNew);
        return strNew;
    }


    public void generateSharingLink() {

        //"https://www.criconet.com/upload/notification/live-streaming-notification.jpg"

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.criconet.com/livematch/" + liveStreamingNewModel.getId() + "?type=live_streaming" + "/" + matchId))
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
                        intent.setPackage("com.whatsapp");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Criconet");
                        intent.putExtra(Intent.EXTRA_TEXT, deeplink);
                        startActivity(Intent.createChooser(intent, "send"));
                    } else {
                        Toaster.customToast("Failed to share event.");
                    }
                });


    }

    public void createWatchPartySharingLink() {

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.criconet.com/livematch/" + "?type=create" + "/" + matchId + "/" + party_group_txt + "/" + party_group_id))
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

                        deeplink = liveStreamingNewModel.getTitle() + "\n" + "\n" + "*" + "Party started by: " + SessionManager.get_name(prefs) + "*" + "\n" + "\n" + shortLink;
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.setPackage("com.whatsapp");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Criconet");
                        intent.putExtra(Intent.EXTRA_TEXT, deeplink);
                        startActivity(Intent.createChooser(intent, "send"));
                    } else {
                        Toaster.customToast("Failed to share event.");
                    }
                });


    }

    /*Create Watch Party Api call here*/

    private void getCreateWatchParty() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.CREATE_WATCH_PARTY,
                response -> {
                    loaderView.hideLoader();
                    try {
                        Log.d("CreateWatchParty", response);
                        JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                        if (jsonObject.optString("status").equalsIgnoreCase("200")) {

                            JSONObject jsonData = jsonObject.getJSONObject("data");

                            party_group_txt = jsonData.getString("party_group_txt");
                            party_group_id = jsonData.getString("party_group_id");
                            created_by = jsonData.getString("created_by");
                            createdMatchId = jsonData.getString("match_id");


                            SessionManager.saveCreatedBy(prefs, created_by);
                            SessionManager.savePartyGroupId(prefs, party_group_id);
                            SessionManager.savePartyGroupTxt(prefs, party_group_txt);
                            SessionManager.saveCreatedMatchId(prefs, createdMatchId);

                            JSONObject payloadJson = new JSONObject();
                            payloadJson.put("party_group_txt", party_group_txt);
                            payloadJson.put("party_group_id", party_group_id);
                            payloadJson.put("user_id", SessionManager.get_user_id(prefs));

                            socketManager.createWatchParty(payloadJson);

                            createWatchPartySharingLink();

                            if (Global.isOnline(mContext)) {
                                getCommentList(Integer.parseInt(party_group_id));
                            } else {
                                Global.showDialog((Activity) mContext);
                            }
                            ib_comment.setVisibility(View.GONE);
                            ib_share.setVisibility(View.VISIBLE);
                            cam_both.setVisibility(View.GONE);
                            liCreateWatchParty.setVisibility(View.GONE);
                            liJoinWatchParty.setVisibility(View.VISIBLE);
                            //rlwatchPartyChat.setVisibility(View.VISIBLE);

                            //Toaster.customToast(party_group_txt+"///"+party_group_id);


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
                    loaderView.hideLoader();
                    Global.msgDialog((Activity) mContext, getResources().getString(R.string.error_server));
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("match_id", matchId);
                System.out.println("data   " + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }


    /*Get Emoji List*/
    private void getEmojiList() {
        StringRequest postRequest = new StringRequest(Request.Method.GET, Global.GET_EMOJI_LIST,
                response -> {
                    // loaderView.hideLoader();
                    //progress.dismiss();
                    try {

                        Log.d("Emoji Response", response);

                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.optString("api_status").equalsIgnoreCase("200")) {

                            JSONArray jsonArray = jsonObject.getJSONArray("emoji");

                            if (jsonObject.has("emoji")) {
                                emojiModelsList = getEmojiLis(jsonObject.getJSONArray("emoji"));

                                liveMatchEmojiAdapter = new LiveMatchEmojiAdapter(mContext, emojiModelsList, (image, name) -> {
                                    if (iv_send.getVisibility() == View.GONE) {
                                        iv_send.setVisibility(View.VISIBLE);
                                    }
                                    //edit_chat.setText("");

                                    zoom_emoji_image = image;

                                    if (imageViewAttached.getVisibility() == View.GONE) {
                                        imageViewAttached.setVisibility(View.VISIBLE);
                                    }

                                    Glide.with(mContext).load(zoom_emoji_image).into(imageViewAttached);
                                    rv_emoji.setVisibility(View.GONE);

                                });
                                rv_emoji.setAdapter(liveMatchEmojiAdapter);

                                liveMatchEmojiAdapter = new LiveMatchEmojiAdapter(mContext, emojiModelsList, (image, name) -> {
                                    if (iv_send_watchparty.getVisibility() == View.GONE) {
                                        iv_send_watchparty.setVisibility(View.VISIBLE);
                                    }
                                    //edit_chat.setText("");

                                    zoom_emoji_image = image;

                                    if (imageViewAttached_watchparty.getVisibility() == View.GONE) {
                                        imageViewAttached_watchparty.setVisibility(View.VISIBLE);
                                    }

                                    Glide.with(mContext).load(zoom_emoji_image).into(imageViewAttached_watchparty);
                                    rv_emoji_watchparty.setVisibility(View.GONE);

                                });
                                rv_emoji_watchparty.setAdapter(liveMatchEmojiAdapter);

                            }


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
                    //loaderView.hideLoader();
                    Global.msgDialog((Activity) mContext, getResources().getString(R.string.error_server));
                }
        ) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> param = new HashMap<String, String>();
////                param.put("user_id", SessionManager.get_user_id(prefs));
////                param.put("s", SessionManager.get_session_id(prefs));
//                param.put("match_id", liveStreamingNewModel.getId());
//
//                System.out.println("data   " + param);
//                return param;
//            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }


    public ArrayList<EmojiModel> getEmojiLis(JSONArray jsonArray) {
        ArrayList<EmojiModel> emojiModelsList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                emojiModelsList.add(new EmojiModel(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return emojiModelsList;
    }

}








