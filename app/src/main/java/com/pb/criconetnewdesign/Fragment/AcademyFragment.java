package com.pb.criconetnewdesign.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pb.criconetnewdesign.Activity.AcademyDetailsActivity;
import com.pb.criconetnewdesign.Activity.BlogActivity;
import com.pb.criconetnewdesign.Activity.NoticeBoardActivity;
import com.pb.criconetnewdesign.CommonUI.WebViewActivity;
import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.adapter.AcademyAdapter.AcademyListAdapter;
import com.pb.criconetnewdesign.adapter.EcoachingAdapter.EcoachingListAdapter;
import com.pb.criconetnewdesign.model.pavilionModel.PageURL;
import com.pb.criconetnewdesign.util.Global;

import org.json.JSONException;
import org.json.JSONObject;


public class AcademyFragment extends Fragment {
    RecyclerView rvAcademy;
    private RequestQueue queue;
    Animation animation;
    Animation animationn;
    PageURL pageURL;
    public AcademyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_academy, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        if (Global.isOnline(requireActivity())) {
            getPageUrl();
        } else {
            Global.showDialog(getActivity());
        }

        initView(view);
        drawerNavigation(layout_nav);

    }

    private void initView(View view) {
        rvAcademy = view.findViewById(R.id.rvAcademy);
        rvAcademy.setHasFixedSize(true);
        rvAcademy.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvAcademy.setAdapter(new AcademyListAdapter(requireContext(), new AcademyListAdapter.academyItemClickListener() {
            @Override
            public void academyItemClickEvent() {
                startActivity(new Intent(requireContext(), AcademyDetailsActivity.class));
            }
        }));
    }

    private void drawerNavigation(RelativeLayout layout_nav){

//        TextView book_a_coac = layout_nav.findViewById(R.id.book_a_coac);
//        book_a_coac.setOnClickListener(v -> {
//            layout_nav.startAnimation(animation);
//            layout_nav.setVisibility(View.GONE);
//        });
//
//        TextView my_blogs = layout_nav.findViewById(R.id.my_blogs);
//        my_blogs.setOnClickListener(v -> {
//            startActivity(new Intent(getActivity(), MyBlogsActivity.class));
//        });
//
//        TextView saved_posts = layout_nav.findViewById(R.id.saved_posts);
//        saved_posts.setOnClickListener(v -> {
//            startActivity(new Intent(getActivity(), SavedPostActivity.class));
//        });

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
}