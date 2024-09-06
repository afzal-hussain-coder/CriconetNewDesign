package com.pb.criconet.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import android.view.animation.TranslateAnimation;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pb.criconet.Activity.Academy.AcademyContactUsActivity;
import com.pb.criconet.Activity.Academy.AcademyUpcomingSessionActivity;
import com.pb.criconet.Activity.Academy.AddAcademyGalleryActivity;
import com.pb.criconet.Activity.Academy.AddAcademyInformationActivity;
import com.pb.criconet.Activity.Academy.ManageAcademyCoachActivity;
import com.pb.criconet.Activity.Academy.ManageAttendanceActivity;
import com.pb.criconet.Activity.Academy.ManageStudentActivity;
import com.pb.criconet.Activity.Academy.ScheduleOnlineAcativity;
import com.pb.criconet.Activity.Academy.AcademyDetailsActivity;
import com.pb.criconet.Activity.Academy.ViewAttendanceActivity;
import com.pb.criconet.Activity.BlogActivity;
import com.pb.criconet.Activity.Coach.RegisterAsAnECoachActivity;
import com.pb.criconet.Activity.LoginActivity;
import com.pb.criconet.Activity.NoticeBoardActivity;
import com.pb.criconet.Activity.User.UserProfileActivity;
import com.pb.criconet.CommonUI.AddTrainingTipsActivity;
import com.pb.criconet.CommonUI.SettingsActivity;
import com.pb.criconet.CommonUI.TrainingTipsActivity;
import com.pb.criconet.CommonUI.WebViewActivity;
import com.pb.criconet.R;
import com.pb.criconet.adapter.AcademyAdapter.AcademyListAdapter;
import com.pb.criconet.model.AcademyModel.AcademyListModel;
import com.pb.criconet.model.pavilionModel.PageURL;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.SessionManager;
import com.pb.criconet.util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;


public class AcademyFragment extends Fragment {
    RecyclerView rvAcademy;
    private RequestQueue queue;
    CustomLoaderView loaderView;
    private SharedPreferences prefs;
    Animation animation;
    Animation animationn;
    PageURL pageURL;
    private ArrayList<AcademyListModel> academyListModels = new ArrayList<>();
    String academyId ="";
    RoundedImageView img_banner;
    CircleImageView image;
    TextView tvCoachName;
    TextView tvCoacExp;

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


        if(!SessionManager.get_academyId(prefs).isEmpty()){
            if (Global.isOnline(getActivity())) {
                getAcademyDetails();
            } else {
                Global.showDialog(getActivity());
            }
        }

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


        img_banner=layout_nav.findViewById(R.id.img_banner);
        image=layout_nav.findViewById(R.id.image);
        tvCoachName=layout_nav.findViewById(R.id.tvCoachName);
        tvCoacExp=layout_nav.findViewById(R.id.tvCoacExp);

        MaterialCardView mcMyAcademy = layout_nav.findViewById(R.id.mcMyAcademy);

        TextView tvMyAcademy = layout_nav.findViewById(R.id.tvMyAcademy);
        tvMyAcademy.setOnClickListener(v -> {
            if(mcMyAcademy.getVisibility() == View.VISIBLE){
                // Slide up and hide
                //mcMyAcademy.startAnimation(slideUpAnimation(mcMyAcademy.getWidth()));
                mcMyAcademy.setVisibility(View.GONE);
            }else{
                // Slide down and show
                //mcMyAcademy.startAnimation(slideDownAnimation(mcMyAcademy.getWidth()));
                mcMyAcademy.setVisibility(View.VISIBLE);

            }


        });

        mcMyAcademy.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), AcademyDetailsActivity.class)
                    .putExtra("FROM","2").putExtra("AcademyId",academyId));
        });


        TextView tvViewAttendance = layout_nav.findViewById(R.id.tvViewAttendance);
        tvViewAttendance.setOnClickListener(v -> {
         startActivity(new Intent(getActivity(), ViewAttendanceActivity.class));
        });


        TextView upcoming_se = layout_nav.findViewById(R.id.upcoming_se);
        upcoming_se.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AcademyUpcomingSessionActivity.class).putExtra("FROM","Upcoming"));
        });

        TextView past_sessio = layout_nav.findViewById(R.id.past_sessio);
        past_sessio.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AcademyUpcomingSessionActivity.class).putExtra("FROM","Past"));
        });


        TextView xyz_academy = layout_nav.findViewById(R.id.xyz_academy);
        xyz_academy.setOnClickListener(v -> {
            if (SessionManager.get_profiletype(prefs).equalsIgnoreCase("coach")) {
                startActivity(new Intent(getActivity(), RegisterAsAnECoachActivity.class));
                //finish();
            } else {
                startActivity(new Intent(getActivity(), UserProfileActivity.class).putExtra("FROM", "2"));
                // finish();
            }

        });

        TextView tvAddTrainingTips = layout_nav.findViewById(R.id.tvAddTrainingTips);
        tvAddTrainingTips.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AddTrainingTipsActivity.class));
        });

        TextView training_ti = layout_nav.findViewById(R.id.training_ti);
        training_ti.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), TrainingTipsActivity.class));
        });

        CircleImageView profile_pic = layout_nav.findViewById(R.id.profile_pic);



        if (SessionManager.get_image(prefs).isEmpty()) {
            Glide.with(getActivity()).load(SessionManager.get_image(prefs)).placeholder(getActivity().getResources().getDrawable(R.drawable.user_default)).error(getActivity().getResources().getDrawable(R.drawable.user_default)).into(profile_pic);
        } else {
            Glide.with(getActivity()).load(SessionManager.get_image(prefs)).placeholder(getActivity().getResources().getDrawable(R.drawable.user_default)).error(getActivity().getResources().getDrawable(R.drawable.user_default)).into(profile_pic);
        }

        TextView tvProfileName = layout_nav.findViewById(R.id.tvProfileName);
        tvProfileName.setText(SessionManager.get_name(prefs));

        ImageView iv_call = layout_nav.findViewById(R.id.iv_call);
        iv_call.setOnClickListener(v -> {
            //Toaster.customToast("call");
        });

        ImageView iv_settings = layout_nav.findViewById(R.id.iv_settings);
        iv_settings.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
        });

        TextView schedule_on = layout_nav.findViewById(R.id.tvScheduleOnlineSession);
        schedule_on.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ScheduleOnlineAcativity.class));
        });

        TextView addAcademy = layout_nav.findViewById(R.id.tvAddAcademy);
        addAcademy.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AddAcademyGalleryActivity.class).putExtra("FROM", "1"));
            // startActivity(new Intent(getActivity(), AddAcademyInformationActivity.class).putExtra("FROM","1"));
        });

        TextView manage_acad = layout_nav.findViewById(R.id.manage_acad);
        manage_acad.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AddAcademyInformationActivity.class).putExtra("FROM", "2"));
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

        TextView ground_owne = layout_nav.findViewById(R.id.ground_owne);
        ground_owne.setOnClickListener(v -> {

            try {
                startActivity(new Intent(getActivity(), WebViewActivity.class)
                        .putExtra("URL", pageURL.getGroundOwner().getString("url"))
                        .putExtra("title", pageURL.getGroundOwner().getString("title")).putExtra("Name", ""));
            } catch (JSONException e) {
                e.printStackTrace();
            }


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

        if (SessionManager.get_academyId(prefs).isEmpty()) {
            manage_acad.setVisibility(View.GONE);
            manage_stud.setVisibility(View.GONE);
            manage_coac.setVisibility(View.GONE);
            manage_atte.setVisibility(View.GONE);
            upcoming_se.setVisibility(View.GONE);
            past_sessio.setVisibility(View.GONE);
            tvMyAcademy.setVisibility(View.GONE);
            schedule_on.setVisibility(View.GONE);
            tvViewAttendance.setVisibility(View.GONE);
            tvAddTrainingTips.setVisibility(View.GONE);
            training_ti.setVisibility(View.VISIBLE);
            addAcademy.setVisibility(View.VISIBLE);
            ground_owne.setVisibility(View.VISIBLE);
        } else {
            if(SessionManager.get_getRoleId(prefs).equalsIgnoreCase("0")){
                manage_acad.setVisibility(View.GONE);
                manage_stud.setVisibility(View.GONE);
                manage_coac.setVisibility(View.GONE);
                manage_atte.setVisibility(View.GONE);
                schedule_on.setVisibility(View.GONE);
                upcoming_se.setVisibility(View.VISIBLE);
                past_sessio.setVisibility(View.VISIBLE);
                tvMyAcademy.setVisibility(View.VISIBLE);
                tvViewAttendance.setVisibility(View.VISIBLE);
                tvAddTrainingTips.setVisibility(View.GONE);
                training_ti.setVisibility(View.VISIBLE);
                addAcademy.setVisibility(View.GONE);
                ground_owne.setVisibility(View.VISIBLE);
            }else{
                manage_acad.setVisibility(View.VISIBLE);
                manage_stud.setVisibility(View.VISIBLE);
                manage_coac.setVisibility(View.VISIBLE);
                manage_atte.setVisibility(View.VISIBLE);
                upcoming_se.setVisibility(View.VISIBLE);
                past_sessio.setVisibility(View.VISIBLE);
                schedule_on.setVisibility(View.VISIBLE);
                tvMyAcademy.setVisibility(View.GONE);
                tvViewAttendance.setVisibility(View.GONE);
                tvAddTrainingTips.setVisibility(View.VISIBLE);
                training_ti.setVisibility(View.VISIBLE);
                addAcademy.setVisibility(View.GONE);
                ground_owne.setVisibility(View.VISIBLE);
            }
        }

        ImageView iv_logout = layout_nav.findViewById(R.id.iv_logout);
        iv_logout.setOnClickListener(v -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setTitle("");
            alertDialog.setMessage(getResources().getString(R.string.Do_you_really_want_to_logout));
            alertDialog.setPositiveButton(getResources().getString(R.string.Yes),
                    (dialog, which) -> {
                        if (Global.isOnline(getActivity())) {
                            logout();
                        } else {
                            Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_LONG).show();
                        }
                    });
            alertDialog.setNegativeButton(getResources().getString(R.string.No),
                    (dialog, which) -> {
                    });
            alertDialog.show();
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
                            startActivity(new Intent(requireContext(), AcademyDetailsActivity.class).putExtra("AcademyDetails", academyListModel1)
                                    .putExtra("FROM","1"));
                        }

                        @Override
                        public void academyContactClick(String academyId, String number) {
                            startActivity(new Intent(getActivity(), AcademyContactUsActivity.class).putExtra("FROM", "2").putExtra("ACADEMY_ID", academyId));

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

    public void logout() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        final JSONObject json = new JSONObject();
        try {
            json.put("user_id", SessionManager.get_user_id(prefs));
            json.put("s", SessionManager.get_session_id(prefs));
            //Log.e(" data  : ", "" + json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        loaderView.showLoader();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, Global.URL + "logout", json,
                response -> {
                    Log.v("logout reponse", "" + response);
//                        {"status":"Success"}
                    loaderView.hideLoader();
                    try {
                        JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                        if (jsonObject.getString("api_status").equals("200")) {
                            SessionManager.dataclear(prefs);
                            SessionManager.save_check_agreement(prefs, true);
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        } else if (jsonObject.getString("api_status").equals("400")) {
                            Toaster.customToast(jsonObject.optJSONObject("errors").optString("error_text"));
                        } else {
                            Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> {
            error.printStackTrace();
            loaderView.hideLoader();
            Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        queue.add(jsonObjectRequest);
    }

    private void getAcademyDetails() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.GET_ACADEMY_DETAILS, response -> {

            loaderView.hideLoader();
            Log.d("MyAcademy Response",response);
            try {
                JSONObject jsonObject = new JSONObject(response);

                if (jsonObject.getString("status").equalsIgnoreCase("200")) {

                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    //academyListModel = new AcademyListModel(jsonObject1);

                    academyId = jsonObject1.getString("id");

//                    coachName = jsonObject1.getString("name");
//                    show_recoding_btn = jsonObject1.getString("show_recoding_btn");
//                    join_session_btn = jsonObject1.getString("join_session_btn");
//                    live_streaming_url = jsonObject1.getString("live_streaming_url");
//                    channel_id = jsonObject1.getString("channel_id");

                    if (!jsonObject1.getString("banner_image").isEmpty()) {
                        Glide.with(getActivity()).load(jsonObject1.getString("banner_image"))
                                .into(img_banner);
                    } else {
                        Glide.with(getActivity()).load(R.drawable.bg2)
                                .into(img_banner);
                    }
                    if (!jsonObject1.getString("logo").isEmpty()) {
                        Glide.with(getActivity()).load(jsonObject1.getString("logo"))
                                .into(image);
                    } else {
                        Glide.with(getActivity()).load(R.drawable.placeholder_user)
                                .into(image);
                    }

//                    Toaster.customToast(jsonObject1.getString("verified"));
//                    if (jsonObject1.getString("verified").equalsIgnoreCase("1")) {
//                        iv_verified.setVisibility(View.VISIBLE);
//                        if(jsonObject1.getString("verified").equalsIgnoreCase("1") &&
//                                jsonObject1.getString("criconet_verified").equalsIgnoreCase("1")){
//                            iv_verified.setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary));
//                        }else{
//                            iv_verified.setColorFilter(ContextCompat.getColor(mContext, R.color.verified_user_color));
//                        }
//                    } else {
//                        iv_verified.setVisibility(View.GONE);
//                        iv_verified.setImageTintList(ContextCompat.getColorStateList(mContext, R.color.bckground_light));
//                    }

                    tvCoachName.setText(Global.capitizeString(jsonObject1.getString("name")));
                    tvCoacExp.setText(jsonObject1.getString("address"));
//                    tvCoacTitle.setText(jsonObject1.getString("cat_title"));
//                    tvDiscription.setText(jsonObject1.getString("short_desc"));
//                    tvPrice.setText(mContext.getString(R.string.fees) + ": " + "\u20B9" + jsonObject1.getString("fee") + "/" + mContext.getString(R.string.month));


                } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                    Global.msgDialog(getActivity(), jsonObject.optJSONObject("errors").optString("error_text"));
                } else {
                    Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }, error -> {
            error.printStackTrace();
            loaderView.hideLoader();
            Global.msgDialog((Activity) getActivity(), getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("academy_id", SessionManager.get_academyId(prefs));
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }
}