package com.pb.criconetnewdesign.Activity.Academy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.adapter.SpecialitiesAdapter;
import com.pb.criconetnewdesign.databinding.ActivityAcademyAddCoachBinding;
import com.pb.criconetnewdesign.databinding.ToolbarInnerpageBinding;
import com.pb.criconetnewdesign.model.DataModelSpecialities;
import com.pb.criconetnewdesign.util.DataModel;
import com.pb.criconetnewdesign.util.Global;
import com.pb.criconetnewdesign.util.Toaster;

import java.util.ArrayList;
import java.util.List;

public class AcademyAddCoachActivity extends AppCompatActivity {
    ActivityAcademyAddCoachBinding activityAcademyAddCoachBinding;
    Context mContext;
    Activity mActivity;
    private DataModelSpecialities modelArrayList;
    private RequestQueue queue;
    ArrayList<DataModelSpecialities.Datum> updateList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAcademyAddCoachBinding = ActivityAcademyAddCoachBinding.inflate(getLayoutInflater());
        setContentView(activityAcademyAddCoachBinding.getRoot());

        mContext = this;
        mActivity = this;

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityAcademyAddCoachBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.add_coach));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        queue = Volley.newRequestQueue(mContext);

        inItView();

        if (Global.isOnline(mContext)) {
            getSpecialities();
        } else {
            Global.showDialog(mActivity);
        }
    }

    private void inItView() {

        activityAcademyAddCoachBinding.rcvSpecialities.setHasFixedSize(true);
        activityAcademyAddCoachBinding.rcvSpecialities.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));

    }

    private void getSpecialities() {
        StringRequest postRequest = new StringRequest(Request.Method.GET, Global.URL + Global.GET_SPECIALITIES_CATEGORY, response -> {
            Gson gson = new Gson();
            modelArrayList = gson.fromJson(response, DataModelSpecialities.class);

            if (modelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                //Toaster.customToast(modelArrayList.getData().size()+"");
                updateList = (ArrayList<DataModelSpecialities.Datum>) modelArrayList.getData();
                activityAcademyAddCoachBinding.rcvSpecialities.setAdapter(new SpecialitiesAdapter(mContext, new ArrayList<>(), modelArrayList.getData(), new SpecialitiesAdapter.ItemListenerr() {
                    @Override
                    public void onItemClick(List<String> item) {
                        Toaster.customToast(item.get(0));
                    }
                }));
            }

        }, error -> {
            error.printStackTrace();
            Global.msgDialog(mActivity, "Error from server");
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }
}