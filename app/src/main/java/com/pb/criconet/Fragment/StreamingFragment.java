package com.pb.criconet.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

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
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.pb.criconet.Activity.BlogActivity;
import com.pb.criconet.Activity.Coach.RegisterAsAnECoachActivity;
import com.pb.criconet.Activity.LoginActivity;
import com.pb.criconet.Activity.NoticeBoardActivity;
import com.pb.criconet.Activity.Streaming.ArchievMatchActivity;
import com.pb.criconet.Activity.Streaming.BookLiveStreamingActivity;
import com.pb.criconet.Activity.Streaming.LiveMatchesActivity;
import com.pb.criconet.Activity.User.UserProfileActivity;
import com.pb.criconet.CommonUI.SettingsActivity;
import com.pb.criconet.CommonUI.WebViewActivity;
import com.pb.criconet.R;
import com.pb.criconet.databinding.FragmentStreamingBinding;
import com.pb.criconet.model.pavilionModel.PageURL;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.SessionManager;
import com.pb.criconet.util.Toaster;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;


public class StreamingFragment extends Fragment {
    private RequestQueue queue;
    Animation animation_right;
    Animation animation_left;
    PageURL pageURL;
    private SharedPreferences prefs;
    FragmentStreamingBinding fragmentStreamingBinding;


    public StreamingFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentStreamingBinding = FragmentStreamingBinding.inflate(inflater, container, false);
        return fragmentStreamingBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RelativeLayout layout_nav = view.findViewById(R.id.layout_nav);
        animation_right = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_left_to_right);
        animation_left = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_right_to_left);


        ImageView img_navigation = view.findViewById(R.id.img_navigation);
        img_navigation.setOnClickListener(v -> {
            layout_nav.startAnimation(animation_left);
            layout_nav.setVisibility(View.VISIBLE);

        });

        ImageView img_close = view.findViewById(R.id.img_close);
        img_close.setOnClickListener(v -> {
            layout_nav.startAnimation(animation_right);
            layout_nav.setVisibility(View.GONE);
        });

        queue = Volley.newRequestQueue(getActivity());
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (Global.isOnline(requireActivity())) {
            getPageUrl();
        } else {
            Global.showDialog(getActivity());
        }
        drawerNavigation(layout_nav);

        fragmentStreamingBinding.VideoView.setSource("https://www.criconet.com/assets/criconet-video.mp4");

        fragmentStreamingBinding.flBookLiveStream.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(),BookLiveStreamingActivity.class));
        });
        fragmentStreamingBinding.flViewLiveMatches.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), LiveMatchesActivity.class));
        });
        fragmentStreamingBinding.tvHowDoesItWork.setOnClickListener(v -> {
            try {
                startActivity(new Intent(getActivity(),WebViewActivity.class).putExtra("URL",pageURL.getLiveStreaming().getString("url"))
                        .putExtra("title",pageURL.getLiveStreaming().getString("title")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        fragmentStreamingBinding.tvViewArchive.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ArchievMatchActivity.class));
        });


    }

    @Override
    public void onPause() {
        super.onPause();
        fragmentStreamingBinding.VideoView.pausePlayer();
    }

    private void drawerNavigation(RelativeLayout layout_nav){


        TextView xyz_academy = layout_nav.findViewById(R.id.xyz_academy);
        xyz_academy.setOnClickListener(v -> {
            if (SessionManager.get_profiletype(prefs).equalsIgnoreCase("coach")) {
                startActivity(new Intent(getActivity(), RegisterAsAnECoachActivity.class));
                //finish();
            }else{
                startActivity(new Intent(getActivity(), UserProfileActivity.class).putExtra("FROM","2"));
                // finish();
            }
        });

        CircleImageView profile_pic = layout_nav.findViewById(R.id.profile_pic);

        if (SessionManager.get_image(prefs).isEmpty()) {
            Glide.with(getActivity()).load(SessionManager.get_image(prefs)).placeholder(getActivity().getResources().getDrawable(R.drawable.user_default)).error(getActivity().getResources().getDrawable(R.drawable.user_default)).into(profile_pic);
        } else {
            Glide.with(getActivity()).load(SessionManager.get_image(prefs)).placeholder(getActivity().getResources().getDrawable(R.drawable.user_default)).error(getActivity().getResources().getDrawable(R.drawable.user_default)).into(profile_pic);
        }

        TextView tvProfileName = layout_nav.findViewById(R.id.tvProfileName);
        tvProfileName.setText(SessionManager.get_name(prefs));

        ImageView iv_settings = layout_nav.findViewById(R.id.iv_settings);
        iv_settings.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
        });

        TextView book_live_s = layout_nav.findViewById(R.id.book_live_s);
        book_live_s.setOnClickListener(v -> {
           startActivity(new Intent(getActivity(), BookLiveStreamingActivity.class));
        });

        layout_nav.findViewById(R.id.view_live_m).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(),LiveMatchesActivity.class));
        });

        layout_nav.findViewById(R.id.view_archiv).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(),ArchievMatchActivity.class));
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
        //loaderView.showLoader();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, Global.URL + "logout", json,
                response -> {
                    Log.v("logout reponse", "" + response);
//                        {"status":"Success"}
                   // loaderView.hideLoader();
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
           // loaderView.hideLoader();
            Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        queue.add(jsonObjectRequest);
    }
}