package com.pb.criconet.Activity.Academy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.chip.Chip;
import com.pb.criconet.R;
import com.pb.criconet.databinding.ActivityManageAcademyCoachDetailsBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.model.AcademyModel.AcademyListModel;
import com.pb.criconet.model.AcademyModel.AcademyStudenModel;
import com.pb.criconet.model.AcademyModel.ManageCoachesModel;
import com.pb.criconet.util.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class ManageAcademyCoachDetailsActivity extends AppCompatActivity {
    ActivityManageAcademyCoachDetailsBinding activityManageAcademyCoachDetailsBinding;
    Context mContext;
    Activity mActivity;
    String FROM = "";
    ManageCoachesModel manageCoachesModel;
    AcademyListModel.AcademyCoaches academyCoaches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityManageAcademyCoachDetailsBinding = ActivityManageAcademyCoachDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityManageAcademyCoachDetailsBinding.getRoot());

        mContext = this;
        mActivity = this;

        ToolbarInnerpageBinding toolbarInnerpageBinding = activityManageAcademyCoachDetailsBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.coach_details));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());


        inItView();
        getIntentValue();
    }

    private void getIntentValue() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            FROM = extras.getString("FROM");

            if(FROM.equalsIgnoreCase("1")){
                activityManageAcademyCoachDetailsBinding.flCall.setVisibility(View.GONE);
                activityManageAcademyCoachDetailsBinding.flUpdate.setVisibility(View.GONE);
                if ((getIntent().getBundleExtra("Certificate") == null)) {

                } else {
                    Bundle args = getIntent().getBundleExtra("Certificate");
                    academyCoaches = (AcademyListModel.AcademyCoaches) args.getSerializable("ARRAYLIST");

                    Glide.with(mContext).load(academyCoaches.getAvatar()).into(activityManageAcademyCoachDetailsBinding.ivRoundedProfile);
                    activityManageAcademyCoachDetailsBinding.tvCoachName.setText(academyCoaches.getName());
                    activityManageAcademyCoachDetailsBinding.tvCoachExp.setText(academyCoaches.getExp_years()+" Yrs Experience");

                    activityManageAcademyCoachDetailsBinding.flCall.setOnClickListener(v -> {
                        Intent phone_intent = new Intent(Intent.ACTION_CALL);
                        phone_intent.setData(Uri.parse("tel:" + academyCoaches.getPhone()));
                        mContext.startActivity(phone_intent);
                    });


//                if (manageCoachesModel.geta == 0) {
//                    activityManageAcademyCoachDetailsBinding.ratingBar.setVisibility(View.GONE);
//                } else {
//                    activityManageAcademyCoachDetailsBinding.ratingBar.setVisibility(View.VISIBLE);
//                    activityManageAcademyCoachDetailsBinding.ratingBar.setRating((float) jsonObjectData.getInt("rating"));
//                }

                    String s = academyCoaches.getCat_title();

                    String[] arr = s.split(",");
                    // Convert the array into arraylist
                    ArrayList<String> specializationArray
                            = new ArrayList<>(Arrays.asList(arr));



                    for (String chipText : specializationArray) {
                        Chip chip = new Chip(mContext);
                        chip.setText(chipText);
                        chip.setChipBackgroundColorResource(R.color.white);
                        chip.setChipStrokeColorResource(R.color.purple_700);
                        chip.setChipStrokeWidth(2.0f);
                        chip.setTextAppearance(R.style.MyChipTextAppearance);
                        activityManageAcademyCoachDetailsBinding.chipSpecializationGroup.addView(chip);
                    }

                    if (academyCoaches.getAbout_coach_profile().isEmpty()) {
                        activityManageAcademyCoachDetailsBinding.liBio.setVisibility(View.GONE);
                    } else {
                        activityManageAcademyCoachDetailsBinding.liBio.setVisibility(View.VISIBLE);
                        activityManageAcademyCoachDetailsBinding.tvBioDetails.setText(academyCoaches.getAbout_coach_profile());
                    }

                    if (academyCoaches.getLang().isEmpty()) {
                        activityManageAcademyCoachDetailsBinding.liLanguage.setVisibility(View.GONE);
                    } else {
                        activityManageAcademyCoachDetailsBinding.liLanguage.setVisibility(View.VISIBLE);
                        activityManageAcademyCoachDetailsBinding.tvLanguage.setText(academyCoaches.getLang());
                    }

                    if (academyCoaches.getAchievement().isEmpty()) {
                        activityManageAcademyCoachDetailsBinding.liAchievement.setVisibility(View.GONE);
                    } else {
                        activityManageAcademyCoachDetailsBinding.liAchievement.setVisibility(View.VISIBLE);
                        activityManageAcademyCoachDetailsBinding.tvTvAchievementDetails.setText(academyCoaches.getAchievement());
                    }

                    if (academyCoaches.getSkills_you_learn().isEmpty()) {
                        activityManageAcademyCoachDetailsBinding.liWhatYouLean.setVisibility(View.GONE);
                    } else {
                        activityManageAcademyCoachDetailsBinding.liWhatYouLean.setVisibility(View.VISIBLE);
                        activityManageAcademyCoachDetailsBinding.tvWhatYourLearnDetails.setText(academyCoaches.getSkills_you_learn());
                    }

                    if (academyCoaches.getWhat_you_teach().isEmpty()) {
                        activityManageAcademyCoachDetailsBinding.liCourseOutline.setVisibility(View.GONE);
                    } else {
                        activityManageAcademyCoachDetailsBinding.liCourseOutline.setVisibility(View.VISIBLE);
                        activityManageAcademyCoachDetailsBinding.tvCourseOutlineDetails.setText(academyCoaches.getWhat_you_teach());
                    }


//                if (jsonObjectData.getJSONArray("certificate").length() == 0) {
//                    activityManageAcademyCoachDetailsBinding.liCertificate.setVisibility(View.GONE);
//                } else {
//                    activityManageAcademyCoachDetailsBinding.liCertificate.setVisibility(View.VISIBLE);
//                    Glide.with(mContext).load(jsonObjectData.getJSONArray("certificate").getJSONObject(0).getString("certificate_url"))
//                            .into(activityManageAcademyCoachDetailsBinding.roundedIvCertificate);
//                    activityManageAcademyCoachDetailsBinding.roundedIvCertificate.setOnClickListener(v -> {
//                        try {
//                            imageViewDialog(jsonObjectData.getJSONArray("certificate").getJSONObject(0).getString("certificate_url"));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    });
//                }

//                activityManageAcademyCoachDetailsBinding.ivZoom.setOnClickListener(v -> {
//                    try {
//                        imageViewDialog(manageCoachesModel.getC);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                });




                }
            }else if(FROM.equalsIgnoreCase("3")){
                activityManageAcademyCoachDetailsBinding.flCall.setVisibility(View.VISIBLE);
                activityManageAcademyCoachDetailsBinding.flUpdate.setVisibility(View.VISIBLE);
                if ((getIntent().getBundleExtra("Certificate") == null)) {

                } else {
                    Bundle args = getIntent().getBundleExtra("Certificate");
                    manageCoachesModel = (ManageCoachesModel) args.getSerializable("ARRAYLIST");

                    Glide.with(mContext).load(manageCoachesModel.getAvatar()).into(activityManageAcademyCoachDetailsBinding.ivRoundedProfile);
                    activityManageAcademyCoachDetailsBinding.tvCoachName.setText(manageCoachesModel.getName());
                    activityManageAcademyCoachDetailsBinding.tvCoachExp.setText(manageCoachesModel.getExp_years()+" Yrs Experience");

                    activityManageAcademyCoachDetailsBinding.flCall.setOnClickListener(v -> {
                        Intent phone_intent = new Intent(Intent.ACTION_CALL);
                        phone_intent.setData(Uri.parse("tel:" + manageCoachesModel.getPhone()));
                        mContext.startActivity(phone_intent);
                    });


//                if (manageCoachesModel.geta == 0) {
//                    activityManageAcademyCoachDetailsBinding.ratingBar.setVisibility(View.GONE);
//                } else {
//                    activityManageAcademyCoachDetailsBinding.ratingBar.setVisibility(View.VISIBLE);
//                    activityManageAcademyCoachDetailsBinding.ratingBar.setRating((float) jsonObjectData.getInt("rating"));
//                }

                    ArrayList<ManageCoachesModel.Specializationcategory>teamsArrayList_info = manageCoachesModel.getTeamsArrayList_info();

                    ArrayList<String> specializationArray = new ArrayList<>();

                    for (int i = 0; i < teamsArrayList_info.size(); i++) {
                        specializationArray.add(teamsArrayList_info.get(i).getTitle());
                    }


                    for (String chipText : specializationArray) {
                        Chip chip = new Chip(mContext);
                        chip.setText(chipText);
                        chip.setChipBackgroundColorResource(R.color.white);
                        chip.setChipStrokeColorResource(R.color.purple_700);
                        chip.setChipStrokeWidth(2.0f);
                        chip.setTextAppearance(R.style.MyChipTextAppearance);
                        activityManageAcademyCoachDetailsBinding.chipSpecializationGroup.addView(chip);
                    }

                    if (manageCoachesModel.getAbout_coach_profile().isEmpty()) {
                        activityManageAcademyCoachDetailsBinding.liBio.setVisibility(View.GONE);
                    } else {
                        activityManageAcademyCoachDetailsBinding.liBio.setVisibility(View.VISIBLE);
                        activityManageAcademyCoachDetailsBinding.tvBioDetails.setText(manageCoachesModel.getAbout_coach_profile());
                    }

                    if (manageCoachesModel.getLang().isEmpty()) {
                        activityManageAcademyCoachDetailsBinding.liLanguage.setVisibility(View.GONE);
                    } else {
                        activityManageAcademyCoachDetailsBinding.liLanguage.setVisibility(View.VISIBLE);
                        activityManageAcademyCoachDetailsBinding.tvLanguage.setText(manageCoachesModel.getLang());
                    }

                    if (manageCoachesModel.getAchievement().isEmpty()) {
                        activityManageAcademyCoachDetailsBinding.liAchievement.setVisibility(View.GONE);
                    } else {
                        activityManageAcademyCoachDetailsBinding.liAchievement.setVisibility(View.VISIBLE);
                        activityManageAcademyCoachDetailsBinding.tvTvAchievementDetails.setText(manageCoachesModel.getAchievement());
                    }

                    if (manageCoachesModel.getSkills_you_learn().isEmpty()) {
                        activityManageAcademyCoachDetailsBinding.liWhatYouLean.setVisibility(View.GONE);
                    } else {
                        activityManageAcademyCoachDetailsBinding.liWhatYouLean.setVisibility(View.VISIBLE);
                        activityManageAcademyCoachDetailsBinding.tvWhatYourLearnDetails.setText(manageCoachesModel.getSkills_you_learn());
                    }

                    if (manageCoachesModel.getWhat_you_teach().isEmpty()) {
                        activityManageAcademyCoachDetailsBinding.liCourseOutline.setVisibility(View.GONE);
                    } else {
                        activityManageAcademyCoachDetailsBinding.liCourseOutline.setVisibility(View.VISIBLE);
                        activityManageAcademyCoachDetailsBinding.tvCourseOutlineDetails.setText(manageCoachesModel.getWhat_you_teach());
                    }


//                if (jsonObjectData.getJSONArray("certificate").length() == 0) {
//                    activityManageAcademyCoachDetailsBinding.liCertificate.setVisibility(View.GONE);
//                } else {
//                    activityManageAcademyCoachDetailsBinding.liCertificate.setVisibility(View.VISIBLE);
//                    Glide.with(mContext).load(jsonObjectData.getJSONArray("certificate").getJSONObject(0).getString("certificate_url"))
//                            .into(activityManageAcademyCoachDetailsBinding.roundedIvCertificate);
//                    activityManageAcademyCoachDetailsBinding.roundedIvCertificate.setOnClickListener(v -> {
//                        try {
//                            imageViewDialog(jsonObjectData.getJSONArray("certificate").getJSONObject(0).getString("certificate_url"));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    });
//                }

//                activityManageAcademyCoachDetailsBinding.ivZoom.setOnClickListener(v -> {
//                    try {
//                        imageViewDialog(manageCoachesModel.getC);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                });




                }
            }




        }
    }

    private void inItView() {

        activityManageAcademyCoachDetailsBinding.flUpdate.setOnClickListener(v -> {
            Intent intent =new Intent(mContext, AcademyAddCoachActivity.class);
            Bundle args = new Bundle();
            try{
                args.putSerializable("ARRAYLIST",(Serializable)manageCoachesModel);
                intent.putExtra("Certificate",args);
                intent.putExtra("FROM","3");
            }catch(Exception e){
                e.printStackTrace();
            }
            startActivity(intent);
        });
    }

    void imageViewDialog(String url) {

        final Dialog dialog = new Dialog(mContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.profiledialog);
        dialog.setCancelable(true);
        PhotoView img = (PhotoView) dialog.findViewById(R.id.image_view);
        ImageView del = (ImageView) dialog.findViewById(R.id.del);
//        img.setScale(3);
        Glide.with(mContext).load(url)
                .into(img);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}