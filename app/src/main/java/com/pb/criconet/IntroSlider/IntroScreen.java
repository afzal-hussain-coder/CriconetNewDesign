package com.pb.criconet.IntroSlider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.pb.criconet.IntroSlider.IntroFragment.IntroFirstFragment;
import com.pb.criconet.IntroSlider.IntroFragment.IntroFourthFragment;
import com.pb.criconet.IntroSlider.IntroFragment.IntroSecondFragment;
import com.pb.criconet.IntroSlider.IntroFragment.IntroThirdFragment;
import com.pb.criconet.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import kotlin.collections.CollectionsKt;

public class IntroScreen extends AppCompatActivity {
    private final ArrayList fragmentList = new ArrayList();
    private HashMap _$_findViewCache;
    int currentPage = 0;
    int indicatorPosition=0;
    Timer timer;
    final long DELAY_MS = 200;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 5000;
    TextView tv_getting;
    SharedPreferences prefs;
    boolean permission = false;
    int PERMISSION_ALL = 1;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        }

        setContentView(R.layout.activity_intro_screen);
        mContext = this;


        setViewPager();

        registerListeners();
    }
    private void setViewPager() {
        IntroSliderAdapter adapter = new IntroSliderAdapter(this);
        ViewPager2 var4 = findViewById(R.id.vpIntroSlider);
        var4.setAdapter(adapter);
        fragmentList.addAll(CollectionsKt.listOf(new IntroFirstFragment(), new IntroSecondFragment(),new IntroThirdFragment(),
                new IntroFourthFragment()));  // add into3 fragment if require , new Intro3Fragment()
        adapter.setFragmentList(fragmentList);
        ((IndicatorLayout) findViewById(R.id.indicatorLayout)).setIndicatorCount(adapter.getItemCount());
        ((IndicatorLayout) findViewById(R.id.indicatorLayout)).selectCurrentPosition(indicatorPosition);
        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (currentPage == fragmentList.size()) {
                currentPage = 0;
                indicatorPosition=0;
            }
            var4.setCurrentItem(currentPage++, true);
            ((IndicatorLayout) findViewById(R.id.indicatorLayout)).selectCurrentPosition(indicatorPosition++);
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

        var4.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                ((IndicatorLayout) findViewById(R.id.indicatorLayout)).selectCurrentPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

    }

    private void registerListeners() {
        try{
            //tv_getting.setOnClickListener(v -> sendIntent());
        }catch (Exception e){
            e.printStackTrace();
        }


    }




    /*void sendIntent() {

        if (SessionManager.get_check_login(prefs)) {
//                    if (SessionManager.get_check_agreement(prefs)) {
//                        Intent intent = new Intent(Splash.this, Verification.class);
//                        startActivity(intent);
//                        finish();
//                    } else {
            Intent intent = new Intent(IntroSliderActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
//                    }
        } else {
//                    Intent intent = new Intent(Splash.this, Welcome.class);
            Intent intent = new Intent(IntroSliderActivity.this, Login.class);
            startActivity(intent);
            finish();
        }

    }*/
}