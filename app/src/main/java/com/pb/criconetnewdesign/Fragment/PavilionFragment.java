package com.pb.criconetnewdesign.Fragment;

import static android.app.Activity.RESULT_OK;

import static com.pb.criconetnewdesign.util.Global.POST_TYPE_IMAGE;
import static com.pb.criconetnewdesign.util.Global.POST_TYPE_LINK;
import static com.pb.criconetnewdesign.util.Global.POST_TYPE_MULTI_IMAGE;
import static com.pb.criconetnewdesign.util.Global.POST_TYPE_TEXT;
import static com.pb.criconetnewdesign.util.Global.POST_TYPE_VIDEO;
import static com.pb.criconetnewdesign.util.Global.POST_TYPE_YOUTUBE;
import static com.pb.criconetnewdesign.util.Global.PRIVACY_EVERYONE;
import static com.pb.criconetnewdesign.util.Global.PRIVACY_MY_FOLLOWERS;
import static com.pb.criconetnewdesign.util.Global.PRIVACY_ONLY_ME;
import static com.pb.criconetnewdesign.util.Global.PRIVACY_PEOPLE_I_FOLLOW;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.allattentionhere.autoplayvideos.AAH_CustomRecyclerView;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mancj.slideup.SlideUp;
import com.pb.criconetnewdesign.Activity.BlogActivity;
import com.pb.criconetnewdesign.Activity.Coach.RegisterAsACoachProfileActivity;
import com.pb.criconetnewdesign.Activity.FeedDetailsActivity;
import com.pb.criconetnewdesign.Activity.LoginActivity;
import com.pb.criconetnewdesign.Activity.MyBlogsActivity;
import com.pb.criconetnewdesign.Activity.NoticeBoardActivity;
import com.pb.criconetnewdesign.Activity.SavedPostActivity;
import com.pb.criconetnewdesign.Activity.User.UserProfileActivity;
import com.pb.criconetnewdesign.CommonUI.AmbassadorProgramActivity;
import com.pb.criconetnewdesign.CommonUI.WebViewActivity;
import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.adapter.PavilionAdapter.HomeAdapter;
import com.pb.criconetnewdesign.adapter.PavilionAdapter.SearchUserAdapter;
import com.pb.criconetnewdesign.inteface.pavilionInterface.PostListeners;
import com.pb.criconetnewdesign.model.pavilionModel.NewPostModel;
import com.pb.criconetnewdesign.model.pavilionModel.PageURL;
import com.pb.criconetnewdesign.model.pavilionModel.SearchUser;
import com.pb.criconetnewdesign.util.CustomLoaderView;
import com.pb.criconetnewdesign.util.Global;
import com.pb.criconetnewdesign.util.MultipartRequest;
import com.pb.criconetnewdesign.util.RecycleViewPaginationScrollListener;
import com.pb.criconetnewdesign.util.SessionManager;
import com.pb.criconetnewdesign.util.Toaster;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class PavilionFragment extends Fragment implements PostListeners {
    //        implements BSImagePicker.OnMultiImageSelectedListener,
    private static final int CAMERA_REQUEST_id = 2015;
    private static final int PICK_IMAGE_id = 100;
    private static final int CAPTURE_VIDEO = 3015;
    private static final int PICK_VIDEO = 300;
    private SharedPreferences prefs;
    private AAH_CustomRecyclerView post_list;
    private RequestQueue queue;
    public ArrayList<NewPostModel> modelArrayList;
    private HomeAdapter adapter;
    private ImageView up_image;
    LinearLayout add_photo, add_video;
    private TextView notfound;
    private EditText up_text;
    private String feedText = "";
    private String postType = "";
    private Cursor cursor;
    private int columnindex, i;
    private Uri URIid = null;
    private Uri selectedImageid, mCapturedImageURIid;
    private String file_pathid = "", image_pathid = "";
    private String postFile = "";
    private String filemanagerstring = " ";
    private String after_post_id = "0";
    private int postPrivacy = 0;
    private Spinner spn_privacy;
    private String url_link, url_title, url_content, url_image;
    CustomLoaderView loaderView;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<String> images;
    private boolean isLoading = false;
    // total no. of pages to load. Initial load is page 0, after which
    // If current page is the last page (Pagination will stop after this page load)
    private boolean isLastPage = false;

    /*search user view initialize here*/
    private RecyclerView rv_searchUser;
    private SearchUserAdapter searchUserAdapter;
    private ArrayList<SearchUser> searchUserArrayList = new ArrayList<>();
    private String searchUsername = "";
    PageURL pageURL;
    String gameSettingsStataus = "";

    ImageView iv_logout;
    FrameLayout fl_post;


    ActivityResultLauncher<Intent> videolaunchercamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (null != result.getData()) {
                        try {

                            postFile = getRealPathFromURI(result.getData().getData(), requireContext());

                            // Bitmap bitmap = getVideoThumbnail(requireContext(), result.getData().getData());
                            //Toaster.customToast(bitmap + "");
                            up_image.setVisibility(View.VISIBLE);
                            // Glide.with(requireContext()).load(postFile).into(up_image);

                            Uri videoUri = Uri.fromFile(new File(postFile));

                            Bitmap thumbnail = getVideoThumbnail(getContext(), videoUri);

                            if (thumbnail != null) {
                                up_image.setImageBitmap(thumbnail);
                            }

                            //up_image.setImageBitmap(bitmap);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        List<Uri> selectedImages = new ArrayList<>();
                        if (result.getData() != null && result.getData().getData() != null) {
                            // Single image selection
                            selectedImages.add(result.getData().getData());
                        } else if (result.getData() != null && result.getData().getClipData() != null) {
                            // Multiple image selection
                            ClipData clipData = result.getData().getClipData();
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                selectedImages.add(clipData.getItemAt(i).getUri());
                            }
                        }
                        // Now you can handle the selectedImages list
                        // For example, you can iterate through it and display the images in imageView

                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(requireContext().getContentResolver().openInputStream(selectedImages.get(0)));
                            up_image.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        Glide.with(getActivity()).load(selectedImages.get(0)).into(up_image);

                    }
                }
            });

    public PavilionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_pavallion, container, false);

        RelativeLayout layout_nav = view.findViewById(R.id.layout_nav);

        Animation animation_right = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_left_to_right);
        Animation animation_left = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_right_to_left);

        Animation animation_down = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
        Animation animation_up = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);

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

        iv_logout = view.findViewById(R.id.iv_logout);
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

        ImageView img_add_post = view.findViewById(R.id.img_add_post);
        ImageView img_close_post = view.findViewById(R.id.img_close_post);
        RelativeLayout rl_add_post = view.findViewById(R.id.rl_add_post);
        rl_add_post.setVisibility(View.GONE);

        img_add_post.setOnClickListener(v -> {
            rl_add_post.startAnimation(animation_down);
            rl_add_post.setVisibility(View.VISIBLE);

            new Handler().postDelayed(() -> img_close_post.setVisibility(View.VISIBLE), 700);
            new Handler().postDelayed(() -> img_add_post.setVisibility(View.GONE), 300);


        });

        img_close_post.setOnClickListener(v -> {
            new Handler().postDelayed(() -> img_add_post.setVisibility(View.VISIBLE), 600);
            img_close_post.setVisibility(View.GONE);
            rl_add_post.startAnimation(animation_up);
            rl_add_post.setVisibility(View.GONE);
        });

        loaderView = CustomLoaderView.initialize(getActivity());
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        queue = Volley.newRequestQueue(getActivity());

        initializeView(view);

        drawerNavigation(layout_nav);

        return view;
    }


    private void initializeView(View rootView) {
        rv_searchUser = rootView.findViewById(R.id.rv_searchUser);
        rv_searchUser.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_searchUser.setHasFixedSize(true);

        fl_post = rootView.findViewById(R.id.fl_post);
        post_list = rootView.findViewById(R.id.post_list);
        up_image = rootView.findViewById(R.id.up_image);
        add_photo = rootView.findViewById(R.id.add_photo);
        add_video = rootView.findViewById(R.id.add_video);
        notfound = rootView.findViewById(R.id.notfound);
        up_text = rootView.findViewById(R.id.up_text);
        spn_privacy = rootView.findViewById(R.id.spn_privacy);

        post_list = rootView.findViewById(R.id.post_list);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        post_list.addItemDecoration(itemDecorator);
        post_list.setHasFixedSize(true);
        notfound = rootView.findViewById(R.id.notfound);
        modelArrayList = new ArrayList<>();
        adapter = new HomeAdapter(getActivity(), modelArrayList, this);
        mLayoutManager = new LinearLayoutManager(getActivity());

        post_list.setLayoutManager(mLayoutManager);
        post_list.setItemAnimator(new DefaultItemAnimator());

        post_list.setActivity(requireActivity()); //todo before setAdapter
        //optional - to play only first visible video
        post_list.setPlayOnlyFirstVideo(true);

        // false by default
        //optional - by default we check if url ends with ".mp4". If your urls do not end with mp4, you can set this param to false and implement your own check to see if video points to url
//        post_list.setCheckForMp4(false); //true by default

        //optional - download videos to local storage (requires "android.permission.WRITE_EXTERNAL_STORAGE" in manifest or ask in runtime)
        //post_list.setDownloadPath(Environment.getExternalStorageDirectory() + "/MyVideo"); // (Environment.getExternalStorageDirectory() + "/Video") by default
        //post_list.setDownloadVideos(true); // false by default
        post_list.setVisiblePercent(90); // percentage of View that needs to be visible to start playing
        post_list.setAdapter(adapter);
        //call this functions when u want to start autoplay on loading async lists (eg firebase)
        post_list.smoothScrollBy(0, 1);
        post_list.smoothScrollBy(0, -1);

        if (Global.isOnline(requireActivity())) {
            getFeed();
            getPageUrl();
            //System.out.println("xxxxxxxxxx getFeed " + after_post_id + "xxxxxxxxxx");
        } else {
            Global.showDialog(getActivity());
        }


        ResetFeed();

        up_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("%s", s.toString());
                /*open whe 05-010-23 for doing cricket scoring layout live*/
                if (s.length() == 0) {
                    rv_searchUser.setVisibility(View.GONE);
                    searchUsername = "";
                } else {


                    String search = s.toString();
                    String[] parts = null;
                    String lastWord = "";


                    if (search.contains("\n")) {
                        parts = search.split("\n");
                    } else {
                        parts = search.split(" ");
                    }
                    //Toaster.customToast(s.length()+"?"+s.toString()+"..."+parts.length);

                    if (search.startsWith(" ")) {
                        lastWord = search;
                    } else {
                        lastWord = parts[parts.length - 1];
                    }

                    //   lastWord = parts[parts.length - 1];
//
                    String finalWord = lastWord.substring(lastWord.lastIndexOf(" ") + 1);


                    // Toaster.customToast(lastWordd);

                    if (finalWord.contains("@") && finalWord.length() > 3) {

                        Matcher matcher = Pattern.compile("@\\s*(\\w+)").matcher(finalWord);

                        while (matcher.find()) {
                            if (searchUsername.isEmpty()) {
                                if (Global.isOnline(getActivity())) {
                                    getUserSearchList(matcher.group(1));
                                } else {
                                    Global.showDialog(getActivity());
                                }
                            }
                        }
                    } else {
                        rv_searchUser.setVisibility(View.GONE);
                        searchUsername = "";
                    }
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 0) {
//                    img_close.setVisibility(View.GONE);
                    if (!postType.equalsIgnoreCase(POST_TYPE_IMAGE) &&
                            !postType.equalsIgnoreCase(POST_TYPE_MULTI_IMAGE) &&
                            !postType.equalsIgnoreCase(POST_TYPE_VIDEO)) {
                        if (s.toString().startsWith("https://") || s.toString().startsWith("http://")) {
                            if (s.toString().contains("youtube") || s.toString().contains("youtu.be")) {
                                postType = POST_TYPE_YOUTUBE;
                            } else {
                                postType = POST_TYPE_LINK;
                                getURLDetails(s.toString());
                            }
                        } else {
                            postType = POST_TYPE_TEXT;
                        }
                    }
                } else {

                    if (!postType.equalsIgnoreCase("Image") && !postType.equalsIgnoreCase("multi_image") && !postType.equalsIgnoreCase("Video")) {
                        postType = "";
//                        img_close.setVisibility(View.VISIBLE);
                        //setHasOptionsMenu(false);
                    }
                }

                if (rv_searchUser.getVisibility() == View.VISIBLE) {
                    rv_searchUser.setVisibility(View.GONE);
                }
//                Toast.makeText(getActivity(), postType, Toast.LENGTH_SHORT).show();
            }

        });


        post_list.addOnScrollListener(new RecycleViewPaginationScrollListener(mLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
//                    page++;
                after_post_id = modelArrayList.get(modelArrayList.size() - 1).getId();
                getFeed();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

        });

        add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postType = POST_TYPE_IMAGE;

                //now = System.currentTimeMillis();
                //System.out.println(now+"taken time");

                openCameraAndGalleryDialog(postType);
                //selectImage();

            }
        });

        add_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postType = POST_TYPE_VIDEO;
                openCameraAndGalleryDialog(postType);
                // selectVideo();
            }
        });

        fl_post.setOnClickListener(v -> {
            searchUsername = "";
            feedText = up_text.getText().toString().trim();
            if (postType.equals(POST_TYPE_VIDEO)) {
                //uploadVideoToServer(feedText);
                PostFeedFinal(feedText);
                //new UploadFileToServer().execute();
            } else {
                PostFeedFinal(feedText);
            }

        });

    }

    private void drawerNavigation(RelativeLayout layout_nav) {

        TextView xyz_academy = layout_nav.findViewById(R.id.xyz_academy);
        xyz_academy.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), UserProfileActivity.class));
        });

        TextView super_6 = layout_nav.findViewById(R.id.super_6);
        super_6.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), WebViewActivity.class).putExtra("URL", "Game"));
        });

        TextView my_blogs = layout_nav.findViewById(R.id.my_blogs);
        my_blogs.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), MyBlogsActivity.class));
        });

        TextView saved_posts = layout_nav.findViewById(R.id.saved_posts);
        saved_posts.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SavedPostActivity.class));
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
//            try {
//                startActivity(new Intent(getActivity(), WebViewActivity.class)
//                        .putExtra("URL",pageURL.getCampus_ambassador().getString("url"))
//                        .putExtra("title",pageURL.getCampus_ambassador().getString("title")));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            startActivity(new Intent(getActivity(), AmbassadorProgramActivity.class));
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

    private void getFeed() {
        //progress.show();
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "home_posts", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("homeResponse", response);
                loaderView.hideLoader();
                try {
                    JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                        JSONArray array = jsonObject.getJSONArray("posts");
                        if (array.length() < 1) {
                            isLastPage = true;
                        }
                        modelArrayList.addAll(NewPostModel.fromJson(array));

                        isLoading = false;

                        adapter.notifyDataSetChanged();

                        if (after_post_id.equals("0")) {
//                                if (page == 1) {
                            post_list.smoothScrollBy(0, 1);
                            post_list.smoothScrollBy(0, -1);

                            if (modelArrayList.size() == 0) {
                                notfound.setVisibility(View.VISIBLE);
                            } else {
                                notfound.setVisibility(View.GONE);
                            }
                        }

                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(getActivity(), jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loaderView.hideLoader();
                Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("after_post_id", after_post_id);
                param.put("s", SessionManager.get_session_id(prefs));

                //Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
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

    private void checkAppSettings() {
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.GET_APP_SETTINGS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("AppSettingsResponse", response);
                try {


                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("api_text").equalsIgnoreCase("success")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");


                        if (jsonObject1.has("game")) {
                            try {
                                gameSettingsStataus = jsonObject1.getString("game");
                                // gameSettingsStataus="1";

                            } catch (JSONException jsonException) {
                                jsonException.printStackTrace();
                            }
                        }
//                        if (jsonObject1.has("booking_status")) {
//                            try {
//                                booking_status = jsonObject1.getInt("booking_status");
//                                // gameSettingsStataus="1";
//
//                            } catch (JSONException jsonException) {
//                                jsonException.printStackTrace();
//                            }
//                        }
//
//
//                        if (jsonObject1.has("referral_code")) {
//                            try {
//                                referral_code_status = jsonObject1.getString("referral_code");
//                            } catch (JSONException jsonException) {
//                                jsonException.printStackTrace();
//                            }
//                        }
//                        if (jsonObject1.has("count_notifications")) {
//                            try {
//                                notificationCount = jsonObject1.getString("count_notifications");
//                                notification_badge.setText(notificationCount);
//                                if(notificationCount.equalsIgnoreCase("0")){
//                                    notification_badge.setVisibility(View.GONE);
//                                }else{
//                                    notification_badge.setVisibility(View.VISIBLE);
//                                }
//                            } catch (JSONException jsonException) {
//                                jsonException.printStackTrace();
//                            }
//                        }

                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        //Toaster.customToast(jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        //Toaster.customToast(getResources().getString(R.string.socket_timeout));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                /*json.put("user_id", SessionManager.get_user_id(prefs));
                json.put("s", SessionManager.get_session_id(prefs));*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //Global.msgDialog((Activity) mActivity, "Error from server");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                System.out.println("dat" + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void checkPrivacy() {
        switch (spn_privacy.getSelectedItemPosition()) {
            case 0:
                postPrivacy = PRIVACY_EVERYONE;
                break;
            case 1:
                postPrivacy = PRIVACY_ONLY_ME;
                break;
            case 2:
                postPrivacy = PRIVACY_PEOPLE_I_FOLLOW;
                break;
            case 3:
                postPrivacy = PRIVACY_MY_FOLLOWERS;
                break;
            default:
                postPrivacy = PRIVACY_EVERYONE;
                break;
        }
    }

    private void openCamera() {

        try {
            String fileName = "profile.jpg";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            mCapturedImageURIid = getActivity().getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURIid);
            startActivityForResult(intent, CAMERA_REQUEST_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void multiGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
        galleryIntent.setType("image/*");
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Enable multiple selection
        galleryActivityResultLauncher.launch(galleryIntent);
    }


    private Bitmap getVideoThumbnail(Context context, Uri videoUri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(context, videoUri);

            // Replace "timeInMicroseconds" with the desired time in microseconds
            long timeInMicroseconds = 1000000; // 1 second
            Bitmap bitmap = retriever.getFrameAtTime(timeInMicroseconds, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);

            if (bitmap != null) {
                // Resize the bitmap if needed
                int desiredWidth = 200; // Adjust as needed
                int desiredHeight = 200; // Adjust as needed
                bitmap = Bitmap.createScaledBitmap(bitmap, desiredWidth, desiredHeight, false);
            }

            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String getRealPathFromURI(Uri uri, Context context) {
        Uri returnUri = uri;
        Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);

        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));

        File file = new File(context.getFilesDir(), name);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();


            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }

            inputStream.close();
            outputStream.close();
            Log.e("File Path", "Path " + file.getAbsolutePath());

        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getAbsolutePath();
    }

    private void openCameraVideo() {
//        File saveFolder = new File(Environment.getExternalStorageDirectory(), "Utopiaxxx");
//        try {
//            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//            takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
//
//            if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
//                startActivityForResult(takeVideoIntent, CAPTURE_VIDEO);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        videolaunchercamera.launch(intent);
    }

    private void openGalleryVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_id) {
                try {
                    selectedImageid = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    cursor = getActivity().getContentResolver().query(selectedImageid, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    columnindex = cursor.getColumnIndex(filePathColumn[0]);
                    file_pathid = cursor.getString(columnindex);
                    URIid = Uri.parse("file://" + file_pathid);
                    postFile = file_pathid;
                    cursor.close();
                    System.out.println("cccccccc   " + postFile);
                    up_image.setVisibility(View.VISIBLE);
                    Glide.with(getActivity()).load(postFile).into(up_image);
                    setHasOptionsMenu(true);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == CAMERA_REQUEST_id) {
                try {
                    String[] projection = {MediaStore.Images.Media.DATA};
                    @SuppressWarnings("deprecation")
                    Cursor cursor = getActivity().managedQuery(mCapturedImageURIid, projection, null, null, null);
                    int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String capturedImageFilePath = cursor.getString(column_index_data);
                    postFile = capturedImageFilePath;
                    up_image.setVisibility(View.VISIBLE);
                    Glide.with(getActivity()).load(postFile).into(up_image);

                    System.out.println("cccccccc   " + postFile);
                    setHasOptionsMenu(true);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (requestCode == PICK_VIDEO) {
                Uri selectedImageUri = data.getData();

                Cursor cursor = null;
                try {
                    String[] proj = {MediaStore.Images.Media.DATA};
                    CursorLoader loader = new CursorLoader(getActivity(), selectedImageUri, proj, null, null, null);
                    cursor = loader.loadInBackground();
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String file_path = cursor.getString(column_index);

                    filemanagerstring = file_path;
                    postFile = filemanagerstring;
                    cursor.close();
                    System.out.println("selectedVideoPath y " + filemanagerstring);
//                dialog_camera.dismiss();
                    if (filemanagerstring != null) {
                        loaderView.showLoader();
                        //progress.show();
                        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(file_path, MediaStore.Images.Thumbnails.MINI_KIND);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        thumb.compress(Bitmap.CompressFormat.PNG, 40, stream);
//                    uploadVideo_taskimage(Global.URL, filemanagerstring, byteArray);
                        try {
                            FileBody filebodyVideo = new FileBody(new File(filemanagerstring));
                            long kblength = new File(filemanagerstring).length();
                            kblength = kblength / 1024;
                            long mblength = kblength / 1024;
                            System.out.println("file.mblength() = " + mblength);
//                            if (mblength > 51) {
//                                System.out.println("file.length() = " + mblength);
//                                Global.msgDialog(getActivity(), getResources().getString(R.string.File_Size_Too_Large_Must_be_less_than_50_MB));
//                                //progress.dismiss();
//                                loaderView.hideLoader();
//                            } else {
//                                up_image.setVisibility(View.VISIBLE);
//                                up_image.setImageBitmap(thumb);
//                                //progress.dismiss();
//                                loaderView.hideLoader();
//                                setHasOptionsMenu(true);
//                            }
                            up_image.setVisibility(View.VISIBLE);
                            up_image.setImageBitmap(thumb);
                            //progress.dismiss();
                            loaderView.hideLoader();
                            setHasOptionsMenu(true);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }

                // Log.e("Attachment Path:", attachmentFile);

            } else if (requestCode == CAPTURE_VIDEO) {
                Uri selectedVideo = data.getData();

                String[] filePathColumn = {MediaStore.Video.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedVideo, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnindex = cursor.getColumnIndex(filePathColumn[0]);
                filemanagerstring = cursor.getString(columnindex);
                cursor.close();
                System.out.println("filemanagerstring x  " + filemanagerstring);
                postFile = filemanagerstring;
//                dialog_camera.dismiss();
                if (filemanagerstring != null) {
                    //progress.show();
                    loaderView.showLoader();
//                    Bitmap bitmap = ((BitmapDrawable) imgPreview.getDrawable()).getBitmap();
                    Bitmap video_thumbnail = ThumbnailUtils.createVideoThumbnail(filemanagerstring, MediaStore.Video.Thumbnails.MINI_KIND);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    video_thumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);

//                    uploadVideo_taskimage(Global.URL, filemanagerstring, byteArray);
                    try {
                        FileBody filebodyVideo = new FileBody(new File(filemanagerstring));
                        long kblength = new File(filemanagerstring).length();
                        kblength = kblength / 1024;
                        long mblength = kblength / 1024;
                        System.out.println("file.mblength() = " + mblength);
//                        if (mblength > 51) {
//                            System.out.println("file.length() = " + mblength);
//                            Global.msgDialog(getActivity(), getResources().getString(R.string.File_Size_Too_Large_Must_be_less_than_50_MB));
//                            //progress.dismiss();
//                            loaderView.hideLoader();
//                        } else {
//                            up_image.setVisibility(View.VISIBLE);
//                            up_image.setImageBitmap(video_thumbnail);
//                            //progress.dismiss();
//                            loaderView.hideLoader();
//                            setHasOptionsMenu(true);
//                        }

                        up_image.setVisibility(View.VISIBLE);
                        up_image.setImageBitmap(video_thumbnail);
                        //progress.dismiss();
                        loaderView.hideLoader();
                        setHasOptionsMenu(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
//        super.onActivityResult(requestCode, resultCode, data);
    }

    public void DeleteFeed(final String id, int pos) {
        //loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "delete_post", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // loaderView.hideLoader();
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {

                        //ResetFeed();
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(getActivity(), jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                // loaderView.hideLoader();
                Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("post_id", id);
                param.put("s", SessionManager.get_session_id(prefs));
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }


    private void likeFeed(final String id) {
        //progress.show();
        //loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "like_on_post", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Pavilion", String.valueOf(response));
                //loaderView.hideLoader();
                //progress.dismiss();
                try {
                    JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
//                        ResetFeed();
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(getActivity(), jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //progress.dismiss();
                //loaderView.hideLoader();
                Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
//                Global.msgDialog(Login.this, "Internet connection is slow");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
//        "user_id:1703
//        s:1
//        post_id:8939"
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("post_id", id);
//                param.put("s", "1");
                param.put("s", SessionManager.get_session_id(prefs));
                Log.e("Pavilion", param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void dislikeFeed(final String id) {
        // progress.show();
        //loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "unlike_on_post", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Pavilion", String.valueOf(response));
                //progress.dismiss();
                // loaderView.hideLoader();
                try {
                    JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
//                        ResetFeed();
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(getActivity(), jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //progress.dismiss();
                //loaderView.hideLoader();
                Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
//                Global.msgDialog(Login.this, "Internet connection is slow");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
//        "user_id:1703
//        s:1
//        post_id:8939"
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("post_id", id);
//                param.put("s", "1");
                param.put("s", SessionManager.get_session_id(prefs));
                Log.e("Pavilion", param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    public void ReportFeed(final String id, final String message) {
        // progress.show();
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "report_post", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Pavilion", String.valueOf(response));
                loaderView.hideLoader();
                // progress.dismiss();
                try {
                    JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                        Global.msgDialog(getActivity(), getResources().getString(R.string.Post_reported_Successfully));
//                        Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
//                        ResetFeed();
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(getActivity(), jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loaderView.hideLoader();
                //progress.dismiss();
                Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
//                param.put("s", "1");
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("post_id", id);
                param.put("report_text", message);
                Log.e("Pavilion", param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    private void getURLDetails(final String url) {
        //progress.show();
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "fetch_url", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Pavilion", String.valueOf(response));
                //progress.dismiss();
                loaderView.hideLoader();
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
//                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {

                    postType = POST_TYPE_LINK;
                    url_title = jsonObject.getString("title");
                    url_link = jsonObject.getString("url");
                    url_content = jsonObject.getString("content");
                    url_image = jsonObject.getJSONArray("images").getString(0);


//                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
//                        Global.msgDialog(getActivity(), jsonObject.optJSONObject("errors").optString("error_text"));
//                    } else {
//                        Global.msgDialog(getActivity(), "Error in server");
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Pavilion", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //progress.dismiss();
                loaderView.hideLoader();
                Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
//                Global.msgDialog(Login.this, "Internet connection is slow");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("url", url);
//                param.put("s", "1");
                param.put("s", SessionManager.get_session_id(prefs));
                Log.e("Pavilion", param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    public void PostFeedFinal(final String postText) {
        try {
            checkPrivacy();
            //progress.show();
            loaderView.showLoader();

            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            Log.e("Pavilion", String.valueOf(entity.isChunked()));
//            entity.addPart("s", new StringBody("1"));
            entity.addPart("user_id", new StringBody(SessionManager.get_user_id(prefs)));
            entity.addPart("s", new StringBody(SessionManager.get_session_id(prefs)));
//            entity.addPart("post_id", new StringBody(SessionManager.get_session_id(prefs)));
            entity.addPart("postPrivacy", new StringBody(String.valueOf(postPrivacy))); //{0: public, 3 : only me}

            switch (postType) {
                case POST_TYPE_IMAGE:
                    entity.addPart("postText", new StringBody(postText));

                    if (!postFile.isEmpty()) {
                        File file = new File(postFile);
                        FileBody fileBody = new FileBody(file);
                        entity.addPart("postFile", fileBody);
                    }
                    break;
                case POST_TYPE_VIDEO:
                    entity.addPart("postText", new StringBody(postText));

                    if (!postFile.isEmpty()) {
                        File file = new File(postFile);
                        FileBody fileBody = new FileBody(file);
                        entity.addPart("postVideo", fileBody);
                        //                        iStream = getActivity().getContentResolver().openInputStream(Uri.parse(postFile));
//                        InputStream iStream = getActivity().getContentResolver().openInputStream(file.toURI());
//                        byte[] body = getBytes(iStream);
//                        entity.addPart("postVideo", new ByteArrayBody(body, "postVideo"));
                    }
                    break;
                case POST_TYPE_MULTI_IMAGE:
                    entity.addPart("postText", new StringBody(postText));
                    for (int j = 0; j < images.size(); j++) {
                        File file = new File(images.get(j));
                        FileBody fileBody = new FileBody(file);
                        entity.addPart("postPhotos[" + (j) + "]", fileBody);
                    }
                    break;
                case POST_TYPE_YOUTUBE:
                    entity.addPart("postText", new StringBody(postText));
                    break;
                case POST_TYPE_LINK:
                    entity.addPart("url_link", new StringBody(url_link));
                    entity.addPart("url_title", new StringBody(url_title));
                    entity.addPart("url_content", new StringBody(url_content));
                    entity.addPart("postText", new StringBody(postText));
                    entity.addPart("url_image", new StringBody(url_image));
//                    if (!(url_image.equals(""))) {
//                        File file = new File(url_image);
//                        FileBody fileBody = new FileBody(file);
//                        entity.addPart("url_image", fileBody);
//                    }
                    break;
                case POST_TYPE_TEXT:
                    // POST_TYPE_TEXT
                    if (!postFile.isEmpty()) {
                        File file = new File(postFile);
                        FileBody fileBody = new FileBody(file);
                        entity.addPart("postFile", fileBody);
                    }
                    entity.addPart("postText", new StringBody(postText));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + postType);
            }

            MultipartRequest req = new MultipartRequest(Global.URL + "new_post",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                //progress.dismiss();
                                loaderView.hideLoader();
                                up_text.setText("");
                                up_image.setImageURI(null);
                                Log.e("Pavilion", response);
                                JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                                if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {

                                    Log.d("PostResponse", jsonObject + "");
//                            JSONArray array = jsonObject.getJSONArray("posts");
                                    ResetFeed();
//                            Global.msgDialog(getActivity(), jsonObject.optString("msg"));
                                } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                    Global.msgDialog(getActivity(), jsonObject.optJSONObject("errors").optString("error_text"));
                                } else {
                                    Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //progress.dismiss();
                            loaderView.hideLoader();
                            error.printStackTrace();
                        }
                    },
                    entity);

            Log.d("PostEntity", entity.toString());


            int socketTimeout = 50000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            req.setRetryPolicy(policy);
            queue.add(req);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void ResetFeed() {
        feedText = "";
        postType = "";
        postFile = "";
        after_post_id = "0";

        isLoading = false;
        isLastPage = false;

        modelArrayList = new ArrayList<>();
        adapter = new HomeAdapter(getActivity(), modelArrayList, this);
        mLayoutManager = new LinearLayoutManager(getActivity());

        post_list.setLayoutManager(mLayoutManager);
        post_list.setItemAnimator(new DefaultItemAnimator());

        post_list.setActivity(requireActivity()); //todo before setAdapter
        //optional - to play only first visible video
        post_list.setPlayOnlyFirstVideo(true);

        // false by default
        //optional - by default we check if url ends with ".mp4". If your urls do not end with mp4, you can set this param to false and implement your own check to see if video points to url
//        post_list.setCheckForMp4(false); //true by default

        //optional - download videos to local storage (requires "android.permission.WRITE_EXTERNAL_STORAGE" in manifest or ask in runtime)
        //post_list.setDownloadPath(Environment.getExternalStorageDirectory() + "/MyVideo"); // (Environment.getExternalStorageDirectory() + "/Video") by default
        //post_list.setDownloadVideos(true); // false by default
        post_list.setVisiblePercent(90); // percentage of View that needs to be visible to start playing
        post_list.setAdapter(adapter);
        //call this functions when u want to start autoplay on loading async lists (eg firebase)
        post_list.smoothScrollBy(0, 1);
        post_list.smoothScrollBy(0, -1);

        if (Global.isOnline(requireActivity())) {
            getFeed();
        } else {
            Global.showDialog(getActivity());
        }
    }

    @Override
    public void onLikeClickListener(NewPostModel post) {
        likeFeed(post.getId());
    }

    @Override
    public void onDislikeClickListener(NewPostModel post) {
        dislikeFeed(post.getId());
    }

    @Override
    public void onCommentClickListener(NewPostModel post) {
        Log.e("Pavilion", post.toString());
        Intent intent = new Intent(getActivity(), FeedDetailsActivity.class);
        intent.putExtra("feed_id", post.getId());
        startActivity(intent);
    }

    @Override
    public void onShareClickListener(NewPostModel post) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, post.getDetails_url());
//        shareIntent.setType("text/html");
        shareIntent.setType("text/plain");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share"));
    }

    @Override
    public void onReportFeedListener(String id, String message) {
        ReportFeed(id, message);
    }

    @Override
    public void onDeleteFeedListener(String id) {
        // DeleteFeed(id);
    }


    @Override
    public void onPostDeleteFeedListener(String id, int pos) {
        modelArrayList.remove(pos);
        adapter.notifyItemRemoved(pos);
        DeleteFeed(id, pos);
    }

    @Override
    public void onEditFeedListener(String id, String text) {
        //editPostDialog(id,text);
    }

    public void editPostDialog(String id, String text) {

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_post_dialog);
        final EditText input = (EditText) dialog.findViewById(R.id.editxt);
        input.setText(text);
        TextView cancel = dialog.findViewById(R.id.cancel);
        TextView update = dialog.findViewById(R.id.update);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Global.validateLength(input.getText().toString(), 5)) {
                    input.setError(getActivity().getResources().getString(R.string.page_descriptionn));
                } else {
                    input.setError(null);
                    if (!postType.equalsIgnoreCase(POST_TYPE_IMAGE) &&
                            !postType.equalsIgnoreCase(POST_TYPE_MULTI_IMAGE) &&
                            !postType.equalsIgnoreCase(POST_TYPE_VIDEO)) {
                        if (text.startsWith("https://") || text.startsWith("http://")) {
                            if (text.contains("youtube") || text.contains("youtu.be")) {
                                postType = POST_TYPE_YOUTUBE;
                            } else {
                                postType = POST_TYPE_LINK;
                                getURLDetails(text);
                            }
                        } else {
                            postType = POST_TYPE_TEXT;
                        }
                    }
                    if (Global.isOnline(getActivity())) {
                        editPostFeed(id, input.getText().toString().trim());
                    } else {
                        Toaster.customToast(getResources().getString(R.string.no_internet));
                    }

                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    public void editPostFeed(String postid, String postText) {
        try {
            checkPrivacy();
            loaderView.showLoader();

            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            Log.e("Pavalion", entity.isChunked() + "");
            entity.addPart("user_id", new StringBody(SessionManager.get_user_id(prefs)));
            entity.addPart("s", new StringBody(SessionManager.get_session_id(prefs)));
            entity.addPart("post_id", new StringBody(postid));
            entity.addPart("postPrivacy", new StringBody(String.valueOf(postPrivacy))); //{0: public, 3 : only me}

            switch (postType) {
                case POST_TYPE_IMAGE:
                    entity.addPart("postText", new StringBody(postText));
                    if (!postFile.isEmpty()) {
                        File file = new File(postFile);
                        FileBody fileBody = new FileBody(file);
                        entity.addPart("postFile", fileBody);
                    }
                    break;
                case POST_TYPE_VIDEO:
                    entity.addPart("postText", new StringBody(postText));
                    if (!postFile.isEmpty()) {
                        File file = new File(postFile);
                        FileBody fileBody = new FileBody(file);
                        entity.addPart("postVideo", fileBody);
                    }
                    break;
                case POST_TYPE_MULTI_IMAGE:
                    entity.addPart("postText", new StringBody(postText));
                    for (int j = 0; j < images.size(); j++) {
                        File file = new File(images.get(j));
                        FileBody fileBody = new FileBody(file);
                        entity.addPart("postPhotos[" + (j) + "]", fileBody);
                    }
                    break;
                case POST_TYPE_YOUTUBE:
                    entity.addPart("postText", new StringBody(postText));
                    break;
                case POST_TYPE_LINK:
                    entity.addPart("url_link", new StringBody(url_link));
                    entity.addPart("url_title", new StringBody(url_title));
                    entity.addPart("url_content", new StringBody(url_content));
                    entity.addPart("postText", new StringBody(postText));
                    entity.addPart("url_image", new StringBody(url_image));
                    break;
                case POST_TYPE_TEXT:
                    // POST_TYPE_TEXT
                    if (!postFile.isEmpty()) {
                        File file = new File(postFile);
                        FileBody fileBody = new FileBody(file);
                        entity.addPart("postFile", fileBody);
                    }
                    entity.addPart("postText", new StringBody(postText));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + postType);
            }

            MultipartRequest req = new MultipartRequest(Global.URL + "new_post",
                    response -> {
                        try {

                            loaderView.hideLoader();
                            up_text.setText("");
                            up_image.setImageURI(null);
                            Log.e("Pavilion", response);
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {
                                ResetFeed();
                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(getActivity(), jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        loaderView.hideLoader();
                        error.printStackTrace();
                    },
                    entity);

            int socketTimeout = 50000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            req.setRetryPolicy(policy);
            queue.add(req);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onProfileClickListener(NewPostModel post) {
//        if (post.getPublisher_type().equalsIgnoreCase("user")) {
//            Intent intent = new Intent(getActivity(), UserDetails.class);
//            intent.putExtra("user_id", post.getPublisherId());
//            intent.putExtra("FROM", "2");
//            startActivity(intent);
//            getActivity().finish();
//        } else {
//            Intent intent = new Intent(getActivity(), PagesDetails.class);
//            intent.putExtra("page_id", post.getPublisherId());
//            intent.putExtra("FROM", "2");
//            startActivity(intent);
//            getActivity().finish();
//        }
    }

    /*get search user list by api*/
    private void getUserSearchList(String searchKey) {
        //loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.SEARCH_USER_LIST_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SearchUser Response", response);
                //loaderView.hideLoader();
                searchUserArrayList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("users");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            searchUserArrayList.add(new SearchUser(jsonArray.getJSONObject(i)));
                        }

                        if (searchUserArrayList.isEmpty()) {
                            //tv_notFound.setVisibility(View.VISIBLE);
                            rv_searchUser.setVisibility(View.GONE);
                        } else {
                            //tv_notFound.setVisibility(View.GONE);
                            rv_searchUser.setVisibility(View.VISIBLE);
                            rv_searchUser.setAdapter(new SearchUserAdapter(getActivity(), searchUserArrayList, username -> {
                                searchUsername = "@" + username;
                                if (up_text.getText().toString().trim().length() > 0) {
                                    rv_searchUser.setVisibility(View.GONE);
                                    Matcher matcher = Pattern.compile("@\\s*(\\w+)").matcher(up_text.getText().toString().trim());
                                    String find = "";
                                    while (matcher.find()) {
                                        find = matcher.group(1);
                                    }

                                    StringBuffer stringBuffer = new StringBuffer();

                                    String newText = "";
                                    newText = remove(up_text.getText().toString().trim(), "@" + find);
                                    stringBuffer.append(newText);
                                    stringBuffer.append(searchUsername);

                                    up_text.setText(stringBuffer);

                                    up_text.setSelection(up_text.getText().toString().length());

                                } else {
                                    rv_searchUser.setVisibility(View.GONE);
                                    up_text.setText(searchUsername);
                                }
                            }));
                        }
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog((Activity) getActivity(), jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog((Activity) getActivity(), getResources().getString(R.string.error_server));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, error -> {
            error.printStackTrace();
            //loaderView.hideLoader();
            Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", "1387");
                param.put("s", "d46e3041de65228dfe11f66de56f5f02c22e6cf639f96877e600810a3075ec1ddd53728122917009d19a006fd6d25d23c93d3bf4e48eb25f");
                param.put("search_key", searchKey);

                Log.e("Pavilion", param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    public String remove(String str, String word) {
        String strNew = str.substring(0, str.length() - word.length());

        System.out.print(strNew);
        return strNew;
    }

    @Override
    public void onStop() {
        super.onStop();
        post_list.stopVideos();
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


    /**
     * open camera and gallery using this bottomSliderDialog
     */
    public void openCameraAndGalleryDialog(String selectedPostType) {

        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogTheme);
        dialog.setContentView(R.layout.dialog_camera_selector);

        TextView tv_choose = dialog.findViewById(R.id.tv_choose);
        TextView tv_camera = dialog.findViewById(R.id.tv_camera);
        TextView tvGallery = dialog.findViewById(R.id.tvGallery);
        TextView tvCancel = dialog.findViewById(R.id.tvCancel);


        switch (selectedPostType) {
            case "image":
                tv_camera.setOnClickListener(v -> {
                    dialog.dismiss();
                    openCamera();
                });
                tvGallery.setOnClickListener(v -> {
                    dialog.dismiss();

                    multiGallery();
                });
                tvCancel.setOnClickListener(v -> {
                    postType = "";
                    dialog.dismiss();
                });
                break;
            case "video":
                tv_camera.setOnClickListener(v -> {
                    dialog.dismiss();
                    openCameraVideo();
                });
                tvGallery.setOnClickListener(v -> {
                    dialog.dismiss();
                    openGalleryVideo();
                });
                tvCancel.setOnClickListener(v -> {
                    postType = "";
                    dialog.dismiss();
                });
                break;
            default:
                System.out.println("Default case");
        }

        // System.out.println("MilesSeconds"+""+(System.currentTimeMillis()-now)+"");

        dialog.show();

    }

}