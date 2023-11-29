package com.pb.criconetnewdesign.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pb.criconetnewdesign.Fragment.AcademyFragment;
import com.pb.criconetnewdesign.Fragment.CoachFragment;
import com.pb.criconetnewdesign.Fragment.PavilionFragment;
import com.pb.criconetnewdesign.Fragment.StreamingFragment;
import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.model.pavilionModel.PageURL;
import com.pb.criconetnewdesign.util.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    Context mContext;
    Activity mActivity;


    ImageView img_p,img_e,img_s,img_a;
    LinearLayout li_p,li_e,li_s,li_a;
    RelativeLayout li_mP,li_mE,li_mS,li_mA;

    int clickItemNavigation=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        mContext = this;
        PavilionFragment fragment1 = new PavilionFragment();
        moveToFragment(fragment1);
        inItView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(clickItemNavigation==0){
            PavilionFragment fragment1 = new PavilionFragment();
            moveToFragment(fragment1);
        }else if(clickItemNavigation==1){
            CoachFragment fragment1 = new CoachFragment();
            moveToFragment(fragment1);
        }else if(clickItemNavigation==2){
            StreamingFragment fragment1 = new StreamingFragment();
            moveToFragment(fragment1);
        }else if(clickItemNavigation==3){
            AcademyFragment fragment1 = new AcademyFragment();
            moveToFragment(fragment1);
        }

    }


    private void inItView() {

        li_p = findViewById(R.id.li_p);
        li_e = findViewById(R.id.li_e);
        li_s = findViewById(R.id.li_s);
        li_a = findViewById(R.id.li_a);

        img_p = findViewById(R.id.img_p);
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
        img_e = findViewById(R.id.img_e);
        img_s = findViewById(R.id.img_s);
        img_a = findViewById(R.id.img_a);

        li_mP = findViewById(R.id.li_mP);
        li_mE = findViewById(R.id.li_mE);
        li_mS = findViewById(R.id.li_mS);
        li_mA = findViewById(R.id.li_mA);

        li_mP.setOnClickListener(v -> {
           clickItemNavigation =0;
            PavilionFragment fragment1 = new PavilionFragment();
            moveToFragment(fragment1);

            if(img_e.getVisibility()== View.VISIBLE){
                img_e.setVisibility(View.GONE);
                img_e.clearAnimation();
            }
            if(li_e.getVisibility() == View.GONE){
                li_e.setVisibility(View.VISIBLE);
            }

            if(img_p.getVisibility()== View.GONE){
                img_p.setVisibility(View.VISIBLE);
                img_p.startAnimation(animation);
            }
            if(li_p.getVisibility() == View.VISIBLE){
                li_p.setVisibility(View.GONE);
            }

            if(img_s.getVisibility()== View.VISIBLE){
                img_s.setVisibility(View.GONE);
                img_s.clearAnimation();
            }
            if(li_s.getVisibility() == View.GONE){
                li_s.setVisibility(View.VISIBLE);
            }

            if(img_a.getVisibility()== View.VISIBLE){
                img_a.setVisibility(View.GONE);
                img_a.clearAnimation();
            }
            if(li_a.getVisibility() == View.GONE){
                li_a.setVisibility(View.VISIBLE);
            }

        });
        li_mE.setOnClickListener(v -> {
            clickItemNavigation=1;
            CoachFragment fragment1 = new CoachFragment();
            moveToFragment(fragment1);

            if(img_e.getVisibility()== View.GONE){
                img_e.setVisibility(View.VISIBLE);
                img_e.startAnimation(animation);
            }
            if(li_e.getVisibility() == View.VISIBLE){
                li_e.setVisibility(View.GONE);
            }

            if(img_p.getVisibility()== View.VISIBLE){
                img_p.setVisibility(View.GONE);
                img_p.clearAnimation();
            }
            if(li_p.getVisibility() == View.GONE){
                li_p.setVisibility(View.VISIBLE);
            }

            if(img_s.getVisibility()== View.VISIBLE){
                img_s.setVisibility(View.GONE);
                img_s.clearAnimation();
            }
            if(li_s.getVisibility() == View.GONE){
                li_s.setVisibility(View.VISIBLE);
            }

            if(img_a.getVisibility()== View.VISIBLE){
                img_a.setVisibility(View.GONE);
                img_a.clearAnimation();
            }
            if(li_a.getVisibility() == View.GONE){
                li_a.setVisibility(View.VISIBLE);
            }
        });
        li_mS.setOnClickListener(v -> {
            clickItemNavigation=2;
            StreamingFragment fragment1 = new StreamingFragment();
            moveToFragment(fragment1);

            if(img_e.getVisibility()== View.VISIBLE){
                img_e.setVisibility(View.GONE);
                img_e.clearAnimation();
            }
            if(li_e.getVisibility() == View.GONE){
                li_e.setVisibility(View.VISIBLE);
            }

            if(img_s.getVisibility()== View.GONE){
                img_s.setVisibility(View.VISIBLE);
                img_s.startAnimation(animation);
            }
            if(li_s.getVisibility() == View.VISIBLE){
                li_s.setVisibility(View.GONE);
            }

            if(img_p.getVisibility()== View.VISIBLE){
                img_p.setVisibility(View.GONE);
                img_p.clearAnimation();
            }
            if(li_p.getVisibility() == View.GONE){
                li_p.setVisibility(View.VISIBLE);
            }

            if(img_a.getVisibility()== View.VISIBLE){
                img_a.setVisibility(View.GONE);
                img_a.clearAnimation();
            }
            if(li_a.getVisibility() == View.GONE){
                li_a.setVisibility(View.VISIBLE);
            }
        });
        li_mA.setOnClickListener(v -> {
            clickItemNavigation=3;
            AcademyFragment fragment1 = new AcademyFragment();
            moveToFragment(fragment1);

            if(img_e.getVisibility()== View.VISIBLE){
                img_e.setVisibility(View.GONE);
                img_e.clearAnimation();
            }
            if(li_e.getVisibility() == View.GONE){
                li_e.setVisibility(View.VISIBLE);
            }

            if(img_s.getVisibility()== View.VISIBLE){
                img_s.setVisibility(View.GONE);
                img_s.clearAnimation();
            }
            if(li_s.getVisibility() == View.GONE){
                li_s.setVisibility(View.VISIBLE);
            }

            if(img_p.getVisibility()== View.VISIBLE){
                img_p.setVisibility(View.GONE);
                img_p.clearAnimation();
            }
            if(li_p.getVisibility() == View.GONE){
                li_p.setVisibility(View.VISIBLE);
            }

            if(img_a.getVisibility()== View.GONE){
                img_a.setVisibility(View.VISIBLE);
                img_a.startAnimation(animation);
            }
            if(li_a.getVisibility() == View.VISIBLE){
                li_a.setVisibility(View.GONE);
            }
        });

    }

    private void moveToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(clickItemNavigation==0){
            PavilionFragment fragment1 = new PavilionFragment();
            moveToFragment(fragment1);
        }else if(clickItemNavigation==1){
            CoachFragment fragment1 = new CoachFragment();
            moveToFragment(fragment1);
        }else if(clickItemNavigation==2){
            StreamingFragment fragment1 = new StreamingFragment();
            moveToFragment(fragment1);
        }else if(clickItemNavigation==3){
            AcademyFragment fragment1 = new AcademyFragment();
            moveToFragment(fragment1);
        }

        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //MainActivity.super.onBackPressed();
                        finishAffinity();
                        System.exit(0);
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }


}