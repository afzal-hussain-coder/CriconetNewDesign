package com.pb.criconet.Activity.Streaming;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.youtube.player.YouTubePlayer;
import com.pb.criconet.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import timber.log.Timber;

public class YoutubePlayerActivity extends AppCompatActivity{



       /* YouTubeBaseActivity implements  YouTubePlayer.OnInitializedListener{*/
    private static final String TAG = "YoutubePlayerActivity";
    YouTubePlayerView player_view;
    //YouTubePlayer.OnInitializedListener mOnInitializedListener;

    // New code here..
    //http://youtu.be/<VIDEO_ID>
    public String VIDEO_ID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_youtube_player);
//        if(getSupportActionBar() != null){
//            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//            getSupportActionBar().setHomeButtonEnabled(false);
//            getSupportActionBar().hide();
//        }
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        mTitle.setText(getResources().getString(R.string.Recorded_Matches));*/
        Bundle bundle = getIntent().getExtras();
        VIDEO_ID=getVideoKey(bundle.getString("url"));
        player_view = (YouTubePlayerView) findViewById(R.id.player_view);
        //player_view.initialize(YoutubeConfig.getApi_Key(), this);

        /*new code add on 21-08-23*/
        player_view.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                String videoId = getVideoKey(bundle.getString("url"));
                youTubePlayer.loadVideo(videoId, 0);
            }
        });


//
//        mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
//            @Override
//            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//                Timber.d("onInitializationSuccess: ");
//                Bundle bundle = getIntent().getExtras();
//                youTubePlayer.loadVideo(getVideoKey(bundle.getString("url")));
//                //youTubePlayer.setFullscreen(true);
//            }
//
//            @Override
//            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//                Timber.d("onInitializationFailure: ");
//            }
//        };
//
//        player_view.initialize(YoutubeConfig.getApi_Key(), mOnInitializedListener);

    }

    private String getVideoKey(String url) {
        String key = "";
        if (url.contains("v=")) {
            key=url.substring(url.lastIndexOf("v=")+2);
        }else{
            key=url.substring(url.lastIndexOf("/")+1);
        }
        Timber.e(key);
        return key;
    }

   /* @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
       *//** add listeners to YouTubePlayer instance **//*
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(playbackEventListener);
        youTubePlayer.setFullscreen(true);
        *//** Start buffering **//*
        if (!b) {
            youTubePlayer.cueVideo(VIDEO_ID);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toaster.customToast("Failed to Initialize!");
    }*/

    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {

        @Override
        public void onBuffering(boolean arg0) {
        }

        @Override
        public void onPaused() {
        }

        @Override
        public void onPlaying() {
        }

        @Override
        public void onSeekTo(int arg0) {
        }

        @Override
        public void onStopped() {
        }

    };

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {

        @Override
        public void onAdStarted() {
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason arg0) {
        }

        @Override
        public void onLoaded(String arg0) {
        }

        @Override
        public void onLoading() {
        }

        @Override
        public void onVideoEnded() {
        }

        @Override
        public void onVideoStarted() {
        }
    };
}
