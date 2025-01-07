package com.pb.criconet.Activity.Academy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pb.criconet.R;
import com.pb.criconet.adapter.AcademyAdapter.AcademyCoachAdapter;
import com.pb.criconet.adapter.AcademyAdapter.ManageCoachAdapter;
import com.pb.criconet.databinding.ActivityAcademyCoachListBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.model.AcademyModel.AcademyListModel;
import com.pb.criconet.model.AcademyModel.ManageCoachesModel;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class AcademyCoachListActivity extends AppCompatActivity {
    ActivityAcademyCoachListBinding activityAcademyCoachListBinding;
    CustomLoaderView loaderView;
    private RequestQueue queue;
    private SharedPreferences prefs;

    Context mContext;
    Activity mActivity;
    String academyId = "";
    ArrayList<AcademyListModel.AcademyCoaches> academyCoachesArrayList = null;
    AcademyListModel.AcademyCoaches academyCoaches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAcademyCoachListBinding = ActivityAcademyCoachListBinding.inflate(getLayoutInflater());
        setContentView(activityAcademyCoachListBinding.getRoot());

        mContext = this;
        mActivity = this;

        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        loaderView = CustomLoaderView.initialize(mContext);
        queue = Volley.newRequestQueue(mContext);

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityAcademyCoachListBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(R.string.coach_list);
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> {
            finish();
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            academyCoachesArrayList = (ArrayList<AcademyListModel.AcademyCoaches>) bundle.getSerializable("ACADEMY_LIST");
        }

        initView();
    }

    private void initView() {

        activityAcademyCoachListBinding.rvAcademyCoach.setHasFixedSize(true);
        activityAcademyCoachListBinding.rvAcademyCoach.setLayoutManager(new GridLayoutManager(mActivity, 2));
        activityAcademyCoachListBinding.rvAcademyCoach.setAdapter(new AcademyCoachAdapter(mContext, academyCoachesArrayList, (manageCoachesModel) -> {

            Intent intent = new Intent(mContext, ManageAcademyCoachDetailsActivity.class);
            Bundle args = new Bundle();
            try {
                args.putSerializable("ARRAYLIST", (Serializable) manageCoachesModel);
                intent.putExtra("Certificate", args);
                intent.putExtra("FROM", "1");
            } catch (Exception e) {
                e.printStackTrace();
            }
            startActivity(intent);

        }));


        if (academyCoachesArrayList.isEmpty()) {
            activityAcademyCoachListBinding.tvnotfound.setVisibility(View.VISIBLE);
            activityAcademyCoachListBinding.rvAcademyCoach.setVisibility(View.GONE);
        } else {
            activityAcademyCoachListBinding.tvnotfound.setVisibility(View.GONE);
            activityAcademyCoachListBinding.rvAcademyCoach.setVisibility(View.VISIBLE);
        }
    }

}


