package com.pb.criconetnewdesign;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.danikula.videocache.HttpProxyCacheServer;
import com.google.firebase.FirebaseApp;
import com.pb.criconetnewdesign.model.CurrentUserSettings;

import timber.log.Timber;

public class AGApplication extends Application {
    //...15-07-22 for Notification channel creation..
    public static final String CHANNEL_ID = "exampleServiceChannel";

    private CurrentUserSettings mVideoSettings = new CurrentUserSettings();

   // private static GoogleAnalytics sAnalytics;
   // private static Tracker sTracker;

    // private final Logger log = LoggerFactory.getLogger(this.getClass());
    /*private RtcEngine mRtcEngine;
    private EngineConfig mConfig;
    private MyEngineEventHandler mEventHandler;*/
    @SuppressLint("StaticFieldLeak")
    private static Context _Context;
    //private static ChatManager mChatManager;

    //    public RtcEngine rtcEngine() {
//        return mRtcEngine;
//    }
//
//    public EngineConfig config() {
//        return mConfig;
//    }
    public static Context getContext() {
        return _Context;
    }

    public CurrentUserSettings userSettings() {
        return mVideoSettings;
    }

//    public void addEventHandler(AGEventHandler handler) {
//        mEventHandler.addEventHandler(handler);
//    }
//
//    public void remoteEventHandler(AGEventHandler handler) {
//        mEventHandler.removeEventHandler(handler);
//    }

    private static HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy() {
        return proxy;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _Context=this;
        //createRtcEngine();
        Timber.plant(new Timber.DebugTree());
        proxy = new HttpProxyCacheServer(_Context);
        //mChatManager = new ChatManager(this);
        //mChatManager.init();
        //mChatManager.enableOfflineMessage(true);

        //sAnalytics = GoogleAnalytics.getInstance(this);
        FirebaseApp.initializeApp(this);
        createNotificationChannel();

    }
//    /**
//     * Gets the default {@link Tracker} for this {@link Application}.
//     * @return tracker
//     */
//    synchronized public Tracker getDefaultTracker() {
//        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
//        if (sTracker == null) {
//            sTracker = sAnalytics.newTracker(R.xml.global_tracker);
//        }
//        return sTracker;
//    }
//    public static ChatManager getChatManager() {
//        return mChatManager;
//    }

    /*private void createRtcEngine() {
        Context context = getApplicationContext();
        String appId = context.getString(R.string.agora_app_id);
        if (TextUtils.isEmpty(appId)) {
            throw new RuntimeException("NEED TO use your App ID, get your own ID at https://dashboard.agora.io/");
        }

        mEventHandler = new MyEngineEventHandler();
        try {
            // Creates an RtcEngine instance
            mRtcEngine = RtcEngine.create(context, appId, mEventHandler);
        } catch (Exception e) {
            //log.error(Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
//
//          Sets the channel profile of the Agora RtcEngine.
//          The Agora RtcEngine differentiates channel profiles and applies different optimization
//          algorithms accordingly. For example, it prioritizes smoothness and low latency for a
//          video call, and prioritizes video quality for a video broadcast.

        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION);

        // Enables the video module.
        mRtcEngine.enableVideo();
        //rtcEngine().enableVirtualBackground(true,Constant.VIRTUAL_BACKGROUND_SOURCE);

//          Enables the onAudioVolumeIndication callback at a set time interval to report on which
//          users are speaking and the speakers' volume.
//          Once this method is enabled, the SDK returns the volume indication in the
//          onAudioVolumeIndication callback at the set time interval, regardless of whether any user
//          is speaking in the channel.

        mRtcEngine.enableAudioVolumeIndication(200, 3, false);

        mConfig = new EngineConfig();
    }*/

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, "Example Service Channel",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(serviceChannel);
        }
    }




// /////////////////////////////////////////////////////////

}