package com.pb.criconet.Fragment.CoachFragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.pb.criconet.Activity.BlogActivity;
import com.pb.criconet.Activity.Coach.CoachDetailsActivity;
import com.pb.criconet.Activity.Coach.RegisterAsACoachProfileActivity;
import com.pb.criconet.Activity.Coach.RegisterAsAnECoachActivity;
import com.pb.criconet.Activity.MyBlogsActivity;
import com.pb.criconet.Activity.NoticeBoardActivity;
import com.pb.criconet.Activity.SavedPostActivity;
import com.pb.criconet.Activity.User.UserBookingHistory;
import com.pb.criconet.CommonUI.AddTrainingTipsActivity;
import com.pb.criconet.CommonUI.WebViewActivity;
import com.pb.criconet.R;
import com.pb.criconet.adapter.EcoachingAdapter.EcoachingListAdapter;
import com.pb.criconet.model.Coaching.CoachList;
import com.pb.criconet.model.pavilionModel.PageURL;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class CoachFragment extends Fragment {

    Animation animation;
    Animation animationn;
    RecyclerView rvCoach;
    PageURL pageURL;
    private SharedPreferences prefs;
    private RequestQueue queue;
    CustomLoaderView loaderView;
    private List<CoachList.Datum> mdata;


    public CoachFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coach, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        loaderView = CustomLoaderView.initialize(getActivity());

        RelativeLayout layout_nav = view.findViewById(R.id.layout_nav);
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_left_to_right);
        animationn = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_right_to_left);
        ImageView img_navigation = view.findViewById(R.id.img_navigation);
        img_navigation.setOnClickListener(v -> {
            layout_nav.startAnimation(animationn);
            layout_nav.setVisibility(View.VISIBLE);

        });

        ImageView img_close = view.findViewById(R.id.img_close);
        img_close.setOnClickListener(v -> {
            layout_nav.startAnimation(animation);
            layout_nav.setVisibility(View.GONE);
        });

        queue = Volley.newRequestQueue(getActivity());

        if (Global.isOnline(getActivity())) {
            getCoachList();
        } else {
            Global.showDialog(getActivity());
        }

        if (Global.isOnline(requireActivity())) {
            getPageUrl();
        } else {
            Global.showDialog(getActivity());
        }


        initView(view);

        drawerNavigation(layout_nav);


    }

    private void initView(View view) {
        rvCoach = view.findViewById(R.id.rvCoach);
        rvCoach.setHasFixedSize(true);
        rvCoach.setLayoutManager(new LinearLayoutManager(requireContext()));
       /* rvCoach.setAdapter(new EcoachingListAdapter(requireContext(), new EcoachingListAdapter.coachItemClickListener() {
            @Override
            public void viewDetails(int id) {
                getActivity().startActivity(new Intent(getActivity(), CoachDetailsActivity.class));
            }

            @Override
            public void bookCoach(int id) {

            }

            @Override
            public void shareCoach() {

            }
        }));*/
    }

    private void drawerNavigation(RelativeLayout layout_nav){

        CircleImageView profile_pic = layout_nav.findViewById(R.id.profile_pic);
        profile_pic.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), RegisterAsACoachProfileActivity.class));
        });

        TextView book_a_coac = layout_nav.findViewById(R.id.book_a_coac);
        book_a_coac.setOnClickListener(v -> {
           // layout_nav.startAnimation(animation);
            //layout_nav.setVisibility(View.GONE);
            startActivity(new Intent(getActivity(), RegisterAsAnECoachActivity.class));

        });

        TextView booking_his = layout_nav.findViewById(R.id.booking_his);
        booking_his.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), UserBookingHistory.class));
        });

        TextView training_ti = layout_nav.findViewById(R.id.training_ti);
        training_ti.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AddTrainingTipsActivity.class));
        });

        TextView about_us= layout_nav.findViewById(R.id.about_us);
        about_us.setOnClickListener(v -> {
            try {
                startActivity(new Intent(getActivity(), WebViewActivity.class)
                        .putExtra("URL",pageURL.getAboutUs().getString("url"))
                        .putExtra("title",pageURL.getAboutUs().getString("title")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        TextView partner_wit = layout_nav.findViewById(R.id.partner_wit);
        partner_wit.setOnClickListener(v -> {
            try {
                startActivity(new Intent(getActivity(), WebViewActivity.class)
                        .putExtra("URL",pageURL.getPartner().getString("url"))
                        .putExtra("title",pageURL.getPartner().getString("title")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        TextView campus_amba = layout_nav.findViewById(R.id.campus_amba);
        campus_amba.setOnClickListener(v -> {
            try {
                startActivity(new Intent(getActivity(), WebViewActivity.class)
                        .putExtra("URL",pageURL.getCampus_ambassador().getString("url"))
                        .putExtra("title",pageURL.getCampus_ambassador().getString("title")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        TextView blog = layout_nav.findViewById(R.id.blog);
        blog.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), BlogActivity.class));

        });

        TextView careers = layout_nav.findViewById(R.id.careers);
        careers.setOnClickListener(v -> {
            try {
                startActivity(new Intent(getActivity(), WebViewActivity.class)
                        .putExtra("URL",pageURL.getCarrer().getString("url"))
                        .putExtra("title",pageURL.getCarrer().getString("title")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        TextView faqs = layout_nav.findViewById(R.id.faqs);
        faqs.setOnClickListener(v -> {
            try {
                startActivity(new Intent(getActivity(), WebViewActivity.class)
                        .putExtra("URL",pageURL.getFaq().getString("url"))
                        .putExtra("title",pageURL.getFaq().getString("title")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        TextView media_relea = layout_nav.findViewById(R.id.media_relea);
        media_relea.setOnClickListener(v -> {
            try {
                startActivity(new Intent(getActivity(), WebViewActivity.class)
                        .putExtra("URL",pageURL.getMediaReleases().getString("url"))
                        .putExtra("title",pageURL.getMediaReleases().getString("title")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        TextView notice_boar = layout_nav.findViewById(R.id.notice_boar);
        notice_boar.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), NoticeBoardActivity.class));
        });

        TextView user_agreem = layout_nav.findViewById(R.id.user_agreem);
        user_agreem.setOnClickListener(v -> {
            try {
                startActivity(new Intent(getActivity(), WebViewActivity.class)
                        .putExtra("URL",pageURL.getUserAggreement().getString("url"))
                        .putExtra("title",pageURL.getUserAggreement().getString("title")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        TextView terms_of_us = layout_nav.findViewById(R.id.terms_of_us);
        terms_of_us.setOnClickListener(v -> {
            try {
                startActivity(new Intent(getActivity(), WebViewActivity.class)
                        .putExtra("URL",pageURL.getTearms().getString("url"))
                        .putExtra("title",pageURL.getTearms().getString("title")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        TextView privacy_pol = layout_nav.findViewById(R.id.privacy_pol);
        privacy_pol.setOnClickListener(v -> {
            try {
                startActivity(new Intent(getActivity(), WebViewActivity.class)
                        .putExtra("URL",pageURL.getTearms().getString("url"))
                        .putExtra("title",pageURL.getTearms().getString("title")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void getPageUrl() {
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "page_url", response -> {
            Log.d("PageURL Response", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("api_status").equalsIgnoreCase("200")) {
                    pageURL = new PageURL(jsonObject.getJSONObject("data"));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            error.printStackTrace();
        }) {

        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void getCoachList() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_coach_lists", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                // Global.dismissDialog(progressDialog);
                loaderView.hideLoader();
                try{
                    Gson gson = new Gson();
                    CoachList modelArrayList = gson.fromJson(response, CoachList.class);

                    if (modelArrayList.getApiStatus().equalsIgnoreCase("200")) {

                        rvCoach.setAdapter(new EcoachingListAdapter(requireContext(),modelArrayList.getData(), new EcoachingListAdapter.coachItemClickListener() {
                            @Override
                            public void viewDetails(int id,String coachId) {
                                getActivity().startActivity(new Intent(getActivity(), CoachDetailsActivity.class).putExtra("CoachId",coachId));
                            }

                            @Override
                            public void bookCoach(int id,String coachId) {
                                getActivity().startActivity(new Intent(getActivity(), CoachDetailsActivity.class).putExtra("CoachId",coachId));
                            }

                            @Override
                            public void shareCoach() {

                            }
                        }));
                    } else {
                        Toast.makeText(getActivity(), modelArrayList.getErrors().getErrorText(), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        }, error -> {
            error.printStackTrace();
            loaderView.hideLoader();
            Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
//                param.put("order_by", experience);
//                param.put("sort_by", price);
//                param.put("filter_cat", filterType);

                Log.d("Param",param.toString());
                //Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }
}