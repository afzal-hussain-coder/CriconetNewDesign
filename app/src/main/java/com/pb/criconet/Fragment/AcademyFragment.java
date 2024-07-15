package com.pb.criconet.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.pb.criconet.Activity.Academy.ManageAcademyCoachActivity;
import com.pb.criconet.Activity.Academy.ManageAttendanceActivity;
import com.pb.criconet.Activity.Academy.ManageStudentActivity;
import com.pb.criconet.Activity.Academy.ScheduleOnlineAcativity;
import com.pb.criconet.Activity.Academy.AcademyDetailsActivity;
import com.pb.criconet.Activity.BlogActivity;
import com.pb.criconet.Activity.NoticeBoardActivity;
import com.pb.criconet.CommonUI.WebViewActivity;
import com.pb.criconet.R;
import com.pb.criconet.adapter.AcademyAdapter.AcademyListAdapter;
import com.pb.criconet.model.AcademyModel.AcademyListModel;
import com.pb.criconet.model.pavilionModel.PageURL;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.SessionManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AcademyFragment extends Fragment {
    RecyclerView rvAcademy;
    private RequestQueue queue;
    CustomLoaderView loaderView;
    private SharedPreferences prefs;
    Animation animation;
    Animation animationn;
    PageURL pageURL;
    private ArrayList<AcademyListModel> academyListModels = new ArrayList<>();

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
        loaderView = CustomLoaderView.initialize(getActivity());
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (Global.isOnline(requireActivity())) {
            getPageUrl();
        } else {
            Global.showDialog(getActivity());
        }

        initView(view);
        drawerNavigation(layout_nav);

        if (Global.isOnline(getActivity())) {
            getAcademyList();
        } else {
            Global.showDialog(getActivity());
        }

    }

    private void initView(View view) {
        rvAcademy = view.findViewById(R.id.rvAcademy);
        rvAcademy.setHasFixedSize(true);
        rvAcademy.setLayoutManager(new LinearLayoutManager(requireContext()));

    }

    private void drawerNavigation(RelativeLayout layout_nav) {

        TextView schedule_on = layout_nav.findViewById(R.id.schedule_on);
        schedule_on.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ScheduleOnlineAcativity.class));
        });

        TextView manage_stud = layout_nav.findViewById(R.id.manage_stud);
        manage_stud.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ManageStudentActivity.class));
        });
//
        TextView manage_coac = layout_nav.findViewById(R.id.manage_coac);
        manage_coac.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ManageAcademyCoachActivity.class));
        });

        TextView manage_atte = layout_nav.findViewById(R.id.manage_atte);
        manage_atte.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ManageAttendanceActivity.class));
        });


        TextView about_us = layout_nav.findViewById(R.id.about_us);
        about_us.setOnClickListener(v -> {
            try {
                startActivity(new Intent(getActivity(), WebViewActivity.class)
                        .putExtra("URL", pageURL.getAboutUs().getString("url"))
                        .putExtra("title", pageURL.getAboutUs().getString("title")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        TextView partner_wit = layout_nav.findViewById(R.id.partner_wit);
        partner_wit.setOnClickListener(v -> {
            try {
                startActivity(new Intent(getActivity(), WebViewActivity.class)
                        .putExtra("URL", pageURL.getPartner().getString("url"))
                        .putExtra("title", pageURL.getPartner().getString("title")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        TextView campus_amba = layout_nav.findViewById(R.id.campus_amba);
        campus_amba.setOnClickListener(v -> {
            try {
                startActivity(new Intent(getActivity(), WebViewActivity.class)
                        .putExtra("URL", pageURL.getCampus_ambassador().getString("url"))
                        .putExtra("title", pageURL.getCampus_ambassador().getString("title")));
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
                        .putExtra("URL", pageURL.getCarrer().getString("url"))
                        .putExtra("title", pageURL.getCarrer().getString("title")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        TextView faqs = layout_nav.findViewById(R.id.faqs);
        faqs.setOnClickListener(v -> {
            try {
                startActivity(new Intent(getActivity(), WebViewActivity.class)
                        .putExtra("URL", pageURL.getFaq().getString("url"))
                        .putExtra("title", pageURL.getFaq().getString("title")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        TextView media_relea = layout_nav.findViewById(R.id.media_relea);
        media_relea.setOnClickListener(v -> {
            try {
                startActivity(new Intent(getActivity(), WebViewActivity.class)
                        .putExtra("URL", pageURL.getMediaReleases().getString("url"))
                        .putExtra("title", pageURL.getMediaReleases().getString("title")));
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
                        .putExtra("URL", pageURL.getUserAggreement().getString("url"))
                        .putExtra("title", pageURL.getUserAggreement().getString("title")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        TextView terms_of_us = layout_nav.findViewById(R.id.terms_of_us);
        terms_of_us.setOnClickListener(v -> {
            try {
                startActivity(new Intent(getActivity(), WebViewActivity.class)
                        .putExtra("URL", pageURL.getTearms().getString("url"))
                        .putExtra("title", pageURL.getTearms().getString("title")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        TextView privacy_pol = layout_nav.findViewById(R.id.privacy_pol);
        privacy_pol.setOnClickListener(v -> {
            try {
                startActivity(new Intent(getActivity(), WebViewActivity.class)
                        .putExtra("URL", pageURL.getTearms().getString("url"))
                        .putExtra("title", pageURL.getTearms().getString("title")));
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

    private void getAcademyList() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.ACADEMY_LIST, response -> {
            Log.d("AcademyResponse", response);

            try {
                loaderView.hideLoader();
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    AcademyListModel academyListModel;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        academyListModel = new AcademyListModel(jsonArray.getJSONObject(i));
                        academyListModels.add(academyListModel);
                    }

                    rvAcademy.setAdapter(new AcademyListAdapter(requireContext(), academyListModels, new AcademyListAdapter.academyItemClickListener() {
                        @Override
                        public void academyItemClickEvent(AcademyListModel academyListModel1) {
                            startActivity(new Intent(requireContext(), AcademyDetailsActivity.class).putExtra("AcademyDetails", academyListModel1));
                        }

                        @Override
                        public void academyContactClick(String number) {
                            Global.callApp(number, getContext());
                        }
                    }));

                } else {
                    loaderView.hideLoader();
                    Toast.makeText(getActivity(), jsonObject.getString("api_text"), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
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
                Log.e("Param", param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }
}