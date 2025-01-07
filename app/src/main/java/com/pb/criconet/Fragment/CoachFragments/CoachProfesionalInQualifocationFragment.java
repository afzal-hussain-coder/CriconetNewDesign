package com.pb.criconet.Fragment.CoachFragments;
import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.gson.Gson;
import com.pb.criconet.Activity.Coach.RegisterAsAnECoachActivity;
import com.pb.criconet.CustomeCamera.CustomeCameraActivity;
import com.pb.criconet.R;
import com.pb.criconet.adapter.EcoachingAdapter.CoachSpecialitiesListAdapter;
import com.pb.criconet.databinding.FragmentCoachProfessionalInformationBinding;
import com.pb.criconet.model.Coaching.CoachSpecialitiesModel;
import com.pb.criconet.model.Datum;
import com.pb.criconet.util.BookingHistoryDropDown;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.DataModel;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.MultipartRequest;
import com.pb.criconet.util.SessionManager;
import com.pb.criconet.util.Toaster;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import timber.log.Timber;

public class CoachProfesionalInQualifocationFragment extends Fragment {

    FragmentCoachProfessionalInformationBinding fragmentCoachProfessionalInformationBinding;
    public static Uri image_uri = null;
    private CoachSpecialitiesModel modelArrayList;
    private RequestQueue queue;
    CustomLoaderView loaderView;
    private SharedPreferences prefs;
    private StringBuilder categoryId;
    private String profileTitle = "", achievement = "", whatYouTeach = "", skillsStudentLearns = "", otherInformation = "", certificateTitle = "", amount = "";
    private Uri photoUri;
    private String profileImagePath = "", imagePathBanner = "", FROM = "", countryID = "", stateID = "", cityID = "",selectedYear = "", selectedMonth = "";
    private Cursor cursor;
    private int columnindex, i;
    private String file_pathid = "";
    private String isTeramsChecked="";
    private ArrayList<DataModel> optionYear = new ArrayList<>();
    private ArrayList<DataModel> optionMonth = new ArrayList<>();
    private ArrayList<Datum>editList_speclization=new ArrayList<>();
    String ImageExtension="";
    private String fromWhere="";


    //TODO get the image from gallery and display it
    ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {

                    assert result.getData() != null;
                    Uri image_uri = result.getData().getData();
                    fragmentCoachProfessionalInformationBinding.tvCertificate.setVisibility(View.GONE);
                    fragmentCoachProfessionalInformationBinding.tvRemoveCertificate.setVisibility(View.VISIBLE);



                   // imagePathBanner = image_uri.toString();

                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getActivity().getContentResolver().query(image_uri, filePathColumn, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String filePath = cursor.getString(columnIndex);
                        cursor.close();

                        imagePathBanner = filePath;

                        if (imagePathBanner != null && imagePathBanner.contains(".")) {
                            ImageExtension = imagePathBanner.substring(imagePathBanner.lastIndexOf(".") + 1);
                        } else {
                            Log.e("Extension Error", "Invalid file path or no extension found");
                        }
                        Log.d("FilePath", "File path is: " + filePath);
                    } else {
                        Log.e("Cursor Error", "Cursor is null");
                    }

                    if(ImageExtension.equalsIgnoreCase("gif")){
                        Toaster.customToast("gif image not allowed for certificate!");
                        fragmentCoachProfessionalInformationBinding.roundedIvCertificate.setVisibility(View.GONE);
                    }else{
                        fragmentCoachProfessionalInformationBinding.roundedIvCertificate.setVisibility(View.VISIBLE);
                        Glide.with(getActivity())
                                .load(image_uri)
                                .into(fragmentCoachProfessionalInformationBinding.roundedIvCertificate);
                    }


                }
            });

    //TODO capture the image using camera and display it
    ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        String[] projection = {MediaStore.Images.Media.DATA};
                        @SuppressWarnings("deprecation")
                        Cursor cursor = getActivity().managedQuery(photoUri, projection, null, null, null);
                        int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        String capturedImageFilePath = cursor.getString(column_index_data);
                        imagePathBanner = capturedImageFilePath;
                        fragmentCoachProfessionalInformationBinding.tvCertificate.setVisibility(View.GONE);
                        fragmentCoachProfessionalInformationBinding.tvRemoveCertificate.setVisibility(View.VISIBLE);
                        fragmentCoachProfessionalInformationBinding.roundedIvCertificate.setVisibility(View.VISIBLE);
                        //fragmentCoachProfessionalInformationBinding.roundedIvCertificate.setImageURI(photoUri);
                        Glide.with(getActivity()).load(imagePathBanner)
                                .into(fragmentCoachProfessionalInformationBinding.roundedIvCertificate);

                    }
                }
            });

    public CoachProfesionalInQualifocationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            fromWhere = getArguments().getString("FROM");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentCoachProfessionalInformationBinding = FragmentCoachProfessionalInformationBinding.inflate(inflater, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                    ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, 112);
            }
        }

        // Retrieve the argument to set the button visibility
        boolean showSaveButton = getArguments() != null && getArguments().getBoolean("showSaveButton", false);
        fragmentCoachProfessionalInformationBinding.flSave.setVisibility(showSaveButton ? View.VISIBLE : View.GONE);

        return fragmentCoachProfessionalInformationBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        loaderView = CustomLoaderView.initialize(getActivity());
        queue = Volley.newRequestQueue(getActivity());

        inItView();

        if (Global.isOnline(requireContext())) {
            getSpecialities();
        } else {
            Global.showDialog(requireActivity());
        }

        if(fromWhere.equalsIgnoreCase("1")){
            fragmentCoachProfessionalInformationBinding.flSave.setVisibility(View.VISIBLE);
        }else{
            fragmentCoachProfessionalInformationBinding.flSave.setVisibility(View.GONE);
        }

        if(fromWhere.equalsIgnoreCase("1")){
            if (Global.isOnline(getActivity())) {
                getUsersDetails();
            } else {
                Global.showDialog(getActivity());
            }
        }
    }

    private void inItView() {


        fragmentCoachProfessionalInformationBinding.tvCertificate.setOnClickListener(v -> {

            openCameraAndGalleryDialog();

        });

        fragmentCoachProfessionalInformationBinding.tvRemoveCertificate.setOnClickListener(v -> {
            imagePathBanner = "";
            if (Global.isOnline(getActivity())) {
                removeCertificate();
            } else {
                Global.showDialog(getActivity());
            }

            fragmentCoachProfessionalInformationBinding.tvCertificate.setVisibility(View.VISIBLE);
            fragmentCoachProfessionalInformationBinding.roundedIvCertificate.setVisibility(View.GONE);
            fragmentCoachProfessionalInformationBinding.tvRemoveCertificate.setVisibility(View.GONE);
        });


        //todo firstName component
        fragmentCoachProfessionalInformationBinding.editTextCoachProfileTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                fragmentCoachProfessionalInformationBinding.filledTextFieldCoachProfileTitle.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    fragmentCoachProfessionalInformationBinding.filledTextFieldCoachProfileTitle.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                fragmentCoachProfessionalInformationBinding.filledTextFieldCoachProfileTitle.setErrorEnabled(false);
            }
        });

        //todo Achievement component
        fragmentCoachProfessionalInformationBinding.editTextAchievement.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                fragmentCoachProfessionalInformationBinding.filledTextFieldAchievement.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    fragmentCoachProfessionalInformationBinding.filledTextFieldAchievement.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                fragmentCoachProfessionalInformationBinding.filledTextFieldAchievement.setErrorEnabled(false);
            }
        });

        fragmentCoachProfessionalInformationBinding.editTextWhatYouTeach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                fragmentCoachProfessionalInformationBinding.filledInputLayoutWhatYouTeach.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    fragmentCoachProfessionalInformationBinding.filledInputLayoutWhatYouTeach.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                fragmentCoachProfessionalInformationBinding.filledInputLayoutWhatYouTeach.setErrorEnabled(false);
            }
        });



        //todo Achievement component
        fragmentCoachProfessionalInformationBinding.editTextSkillsStudentsLearn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                fragmentCoachProfessionalInformationBinding.filledInputLayoutSkillsStudentsLearn.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    fragmentCoachProfessionalInformationBinding.filledInputLayoutSkillsStudentsLearn.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                fragmentCoachProfessionalInformationBinding.filledInputLayoutSkillsStudentsLearn.setErrorEnabled(false);
            }
        });


        //todo Achievement component
        fragmentCoachProfessionalInformationBinding.editTextOtherInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                fragmentCoachProfessionalInformationBinding.filledInputLayoutOtherInfo.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    fragmentCoachProfessionalInformationBinding.filledInputLayoutOtherInfo.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                fragmentCoachProfessionalInformationBinding.filledInputLayoutOtherInfo.setErrorEnabled(false);
            }
        });


        //todo Achievement component
        fragmentCoachProfessionalInformationBinding.editTextCoachProfileTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                fragmentCoachProfessionalInformationBinding.filledTextFieldCertificateTitle.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    fragmentCoachProfessionalInformationBinding.filledTextFieldCertificateTitle.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                fragmentCoachProfessionalInformationBinding.filledTextFieldCertificateTitle.setErrorEnabled(false);
            }
        });


        //todo Achievement component
        fragmentCoachProfessionalInformationBinding.editTextAmountPerSession.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                fragmentCoachProfessionalInformationBinding.filledTextFieldAmountPerSession.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    fragmentCoachProfessionalInformationBinding.filledTextFieldAmountPerSession.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                fragmentCoachProfessionalInformationBinding.filledTextFieldAmountPerSession.setErrorEnabled(false);
            }
        });

        for(int i =0;i<12 ;i++){

            if(i==0 || i ==1){
                optionMonth.add(new DataModel(i+" month"));
            }else{
                optionMonth.add(new DataModel(i+" months"));
            }

        }
        fragmentCoachProfessionalInformationBinding.dropMonth.setOptionList(optionMonth);

        fragmentCoachProfessionalInformationBinding.dropMonth.setClickListener(new BookingHistoryDropDown.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name, String id) {
                selectedMonth = name;

                if (selectedMonth.contains("months")) {
                    selectedMonth = selectedMonth.replace("months", "");
                    Toaster.customToast(selectedMonth);
                } else if (selectedMonth.contains("month")) {
                    selectedMonth = selectedMonth.replace("month", "");
                    Toaster.customToast(selectedMonth);
                }
            }


            @Override
            public void onDismiss() {
            }
        });

        for(int i=0;i<30;i++){
            if(i==0 || i ==1){
                optionYear.add(new DataModel(i+" year"));
            }else{
                optionYear.add(new DataModel(i+" years"));
            }
        }
        fragmentCoachProfessionalInformationBinding.dropYear.setOptionList(optionYear);

        fragmentCoachProfessionalInformationBinding.dropYear.setClickListener(new BookingHistoryDropDown.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name, String id) {
                selectedYear = name;


                if (selectedYear.contains("years")) {
                    selectedYear = selectedYear.replace("years", "");
                    Toaster.customToast(selectedYear);
                } else if (selectedYear.contains("year")) {
                    selectedYear = selectedYear.replace("year", "");
                    Toaster.customToast(selectedYear);
                }


            }


            @Override
            public void onDismiss() {
            }
        });

        fragmentCoachProfessionalInformationBinding.cbTearms.setOnClickListener(view -> {
            if(((CompoundButton) view).isChecked()){
                isTeramsChecked="1";
            } else {
                isTeramsChecked="";
            }
        });

        fragmentCoachProfessionalInformationBinding.flSave.setOnClickListener(v -> {
            if (checkValidation()) {
                if (Global.isOnline(getActivity())) {
                    updateCoachExp(imagePathBanner);
                } else {
                    Global.showDialog(getActivity());
                }
            }
        });

        fragmentCoachProfessionalInformationBinding.rvSpecilities.setHasFixedSize(true);
        fragmentCoachProfessionalInformationBinding.rvSpecilities.setLayoutManager(new GridLayoutManager(requireContext(),3,GridLayoutManager.VERTICAL, false));

    }

    //TODO open camera and gallery using this bottomSliderDialog
    public void openCameraAndGalleryDialog() {

        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogTheme);
        dialog.setContentView(R.layout.dialog_camera_selector);

        TextView tv_choose = dialog.findViewById(R.id.tv_choose);
        TextView tv_camera = dialog.findViewById(R.id.tv_camera);
        TextView tvGallery = dialog.findViewById(R.id.tvGallery);
        TextView tvCancel = dialog.findViewById(R.id.tvCancel);

        Objects.requireNonNull(tv_camera).setOnClickListener(v -> {
            dialog.dismiss();
            openCameraSingleImage();
        });
        tvGallery.setOnClickListener(v -> {
            dialog.dismiss();
            openGallerySingleImage();
        });
        tvCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();

    }

    private void getSpecialities() {
        StringRequest postRequest = new StringRequest(Request.Method.GET, Global.URL + Global.GET_SPECIALITIES_CATEGORY, response -> {
            Gson gson = new Gson();
            modelArrayList = gson.fromJson(response, CoachSpecialitiesModel.class);
            if (modelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                fragmentCoachProfessionalInformationBinding.rvSpecilities.setAdapter(new CoachSpecialitiesListAdapter(requireActivity(),new ArrayList<>(), modelArrayList.getData(), new CoachSpecialitiesListAdapter.ItemListener() {
                    @Override
                    public void onItemClick(List<String> item) {
                        categoryId = new StringBuilder();
                        String prefix = "";
                        for (String itemm : item) {
                            categoryId.append(prefix);
                            prefix = ",";
                            categoryId.append(itemm);
                        }
                    }
                }));
            }

        }, error -> {
            error.printStackTrace();
            Global.msgDialog(requireActivity(), "Error from server");
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private boolean checkValidation() {
        profileTitle = fragmentCoachProfessionalInformationBinding.editTextCoachProfileTitle.getText().toString().trim();
        achievement = fragmentCoachProfessionalInformationBinding.editTextAchievement.getText().toString().trim();
        whatYouTeach = fragmentCoachProfessionalInformationBinding.editTextWhatYouTeach.getText().toString().trim();
        skillsStudentLearns = fragmentCoachProfessionalInformationBinding.editTextSkillsStudentsLearn.getText().toString().trim();
        otherInformation = fragmentCoachProfessionalInformationBinding.editTextOtherInfo.getText().toString().trim();
        certificateTitle = fragmentCoachProfessionalInformationBinding.editTextCertificateTitle.getText().toString().trim();
        amount = fragmentCoachProfessionalInformationBinding.editTextAmountPerSession.getText().toString().trim();

        //Log.d("CtId",categoryId+"");

        if (categoryId == null) {
            Toaster.customToast(getActivity().getResources().getString(R.string.Please_choose_your_specialities));
            return false;
        } else if (!Global.validateLengthofCoachRegisterProfileTitle(profileTitle)) {
            fragmentCoachProfessionalInformationBinding.filledTextFieldCoachProfileTitle.setErrorEnabled(true);
            fragmentCoachProfessionalInformationBinding.filledTextFieldCoachProfileTitle.setError(getActivity().getResources().getString(R.string.Fill_your_profile_title_long));
            fragmentCoachProfessionalInformationBinding.filledTextFieldCoachProfileTitle.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.app_light_red)));
            return false;
        }else if (achievement.length()<8) {
            fragmentCoachProfessionalInformationBinding.filledTextFieldAchievement.setErrorEnabled(true);
            fragmentCoachProfessionalInformationBinding.filledTextFieldAchievement.setError(getActivity().getResources().getString(R.string.enter_achievement));
            fragmentCoachProfessionalInformationBinding.filledTextFieldAchievement.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.app_light_red)));
            return false;
        }
        else if (selectedYear.isEmpty() && selectedMonth.isEmpty()) {
            Toaster.customToast(getActivity().getResources().getString(R.string.Select_year_and_month));
            return false;
        }
//        else if(selectedYear.equalsIgnoreCase("0") && selectedMonth.equalsIgnoreCase("0")){
//            Toaster.customToast(getActivity().getResources().getString(R.string.Select_month));
//            return false;
//        }
        else if (!Global.validateLengthofCoachRegisterProfileTitle(whatYouTeach)) {
            fragmentCoachProfessionalInformationBinding.filledInputLayoutWhatYouTeach.setErrorEnabled(true);
            fragmentCoachProfessionalInformationBinding.filledInputLayoutWhatYouTeach.setError(getActivity().getResources().getString(R.string.enter_what_you_te));
            fragmentCoachProfessionalInformationBinding.filledInputLayoutWhatYouTeach.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.app_light_red)));
            return false;
        }else if (!Global.validateLengthofCoachRegisterProfileTitle(skillsStudentLearns)) {
            fragmentCoachProfessionalInformationBinding.filledInputLayoutSkillsStudentsLearn.setErrorEnabled(true);
            fragmentCoachProfessionalInformationBinding.filledInputLayoutSkillsStudentsLearn.setError(getActivity().getResources().getString(R.string.enter_skills_stud));
            fragmentCoachProfessionalInformationBinding.filledInputLayoutSkillsStudentsLearn.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.app_light_red)));
            return false;
        }else if (!Global.validateLengthofCoachRegisterProfileTitle(otherInformation)) {
            fragmentCoachProfessionalInformationBinding.filledInputLayoutOtherInfo.setErrorEnabled(true);
            fragmentCoachProfessionalInformationBinding.filledInputLayoutOtherInfo.setError(getActivity().getResources().getString(R.string.enter_other_infor));
            fragmentCoachProfessionalInformationBinding.filledInputLayoutOtherInfo.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.app_light_red)));
            return false;
        } else if(amount.isEmpty()){
            fragmentCoachProfessionalInformationBinding.filledTextFieldAmountPerSession.setErrorEnabled(true);
            fragmentCoachProfessionalInformationBinding.filledTextFieldAmountPerSession.setError(getActivity().getResources().getString(R.string.Enter_Amount_session));
            fragmentCoachProfessionalInformationBinding.filledTextFieldAmountPerSession.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.app_light_red)));
            return false;
        }
        else if (amount.equalsIgnoreCase("0") || Float.parseFloat(amount)<1.0000) {
            fragmentCoachProfessionalInformationBinding.filledTextFieldAmountPerSession.setErrorEnabled(true);
            fragmentCoachProfessionalInformationBinding.filledTextFieldAmountPerSession.setError(getActivity().getResources().getString(R.string.Enter_Amount_sessionn));
            fragmentCoachProfessionalInformationBinding.filledTextFieldAmountPerSession.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.app_light_red)));
            return false;
        }
//        else if(isTeramsChecked.equalsIgnoreCase("")){
//            Toaster.customToast(getActivity().getResources().getString(R.string.Please_check_tearms));
//            return false;
//        }

        return true;
    }

    public void updateCoachExp(String path) {
        loaderView.showLoader();
        try {

            MultipartEntity entity = new MultipartEntity();
            entity.addPart("user_id", new StringBody(SessionManager.get_user_id(prefs)));
            entity.addPart("s", new StringBody(SessionManager.get_session_id(prefs)));
            entity.addPart("profile_title", new StringBody(profileTitle));
            entity.addPart("coach_category_id", new StringBody(String.valueOf(categoryId)));
            entity.addPart("exp_years", new StringBody(selectedYear.trim()));
            entity.addPart("exp_months", new StringBody(selectedMonth.trim()));
            entity.addPart("about_coach_profile", new StringBody(otherInformation));
            entity.addPart("cuurency_code", new StringBody("INR"));
            entity.addPart("charge_amount", new StringBody(amount));
            entity.addPart("certificate_title", new StringBody(certificateTitle));
            entity.addPart("what_you_teach", new StringBody(whatYouTeach));
            entity.addPart("skills_you_learn", new StringBody(skillsStudentLearns));
            entity.addPart("achievement", new StringBody(achievement));
            entity.addPart("is_agree", new StringBody(isTeramsChecked));

            Log.e("CertificatePath",path);

            if (!(path.equals("") || path == null)) {
                File file = new File(path);
                FileBody fileBody = new FileBody(file);
                entity.addPart("certificate_path", fileBody);
            }

            //Log.e("MultipartParams", entity.toString());


            MultipartRequest req = new MultipartRequest(Global.URL + Global.ADD_COACH_QUALIFICATION, response -> {
                try {
                    Timber.d(response);
                    loaderView.hideLoader();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                        //SessionManager.save_profileType(prefs,"coach");
                        if (jsonObject.has("data")) {
                            JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                            SessionManager.save_profiletype(prefs, jsonObjectData.getString("profile_type"));
                            Toaster.customToast(jsonObject.optString("msg"));
                            isTeramsChecked="";
                            ((RegisterAsAnECoachActivity) getActivity()).switchToNextFragment(2);
                            (fragmentCoachProfessionalInformationBinding.cbTearms).setChecked(false);

                        }

                        if(imagePathBanner.isEmpty()){
                            fragmentCoachProfessionalInformationBinding.tvCertificate.setText(getActivity().getResources().getString(R.string.certificate_upload));
                        }else{
                            fragmentCoachProfessionalInformationBinding.tvCertificate.setText(getActivity().getResources().getString(R.string.remove_certificate));
                        }

//                        new Handler().postDelayed(() -> {
//                            Intent intent = new Intent(mContext, CoachSloatAvailabilityActivity.class);
//                            startActivity(intent);
//                        },2000);

                        //getUsersDetails(SessionManager.get_user_id(prefs));

                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(getActivity(), jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, error -> {
                loaderView.hideLoader();
                error.printStackTrace();
            }, entity);

            Timber.d(entity.toString());

            int socketTimeout = 30000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            req.setRetryPolicy(policy);

            queue.add(req);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //TODO takes Single image from camera
    private void openCameraSingleImage() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        photoUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        cameraActivityResultLauncher.launch(cameraIntent);
    }

    //TODO takes single image from gallery
    private void openGallerySingleImage() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryActivityResultLauncher.launch(galleryIntent);
    }

    private void removeCertificate() {
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.REMOVE_CERTIFICATE, response -> {
            Timber.d(response);
            try {
                Timber.d(response);
                loaderView.hideLoader();
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {

                    Toaster.customToast(jsonObject.optString("msg"));
                    fragmentCoachProfessionalInformationBinding.tvCertificate.setText(getActivity().getResources().getString(R.string.certificate_upload));
                    fragmentCoachProfessionalInformationBinding.roundedIvCertificate.setImageURI(null);
                    //getUsersDetails(SessionManager.get_user_id(prefs));

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
            Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    public void getUsersDetails() {
        //loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_user_data",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("getUserDetails",response);
                        //loaderView.hideLoader();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                JSONObject object = jsonObject.getJSONObject("coach_data");

                                if(object.has("coach_info")){
                                    JSONObject jsonObject_personal_info= object.getJSONObject("coach_info");
                                   setData(jsonObject_personal_info);
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
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        // loaderView.hideLoader();
                        Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
//                Global.msgDialog(EditProfile.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("user_profile_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                System.out.println("data   :" + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    private void setData(JSONObject  data) {

        if (data.has("profile_title")) {
            profileTitle = data.optString("profile_title");
            fragmentCoachProfessionalInformationBinding.editTextCoachProfileTitle.setText(data.optString("profile_title"));
        }

        if (data.has("achievement")) {

            achievement = data.optString("achievement");
            fragmentCoachProfessionalInformationBinding.editTextAchievement.setText(data.optString("achievement"));
        }
        if (data.has("skills_you_learn")) {
            try {
                skillsStudentLearns = data.getString("skills_you_learn");
                fragmentCoachProfessionalInformationBinding.editTextSkillsStudentsLearn.setText(data.getString("skills_you_learn"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (data.has("what_you_teach")) {
            whatYouTeach = data.optString("what_you_teach");
            fragmentCoachProfessionalInformationBinding.editTextWhatYouTeach.setText(Html.fromHtml(data.optString("what_you_teach")));
        }
        if (data.has("about_coach_profile")) {
            otherInformation = data.optString("about_coach_profile");
            fragmentCoachProfessionalInformationBinding.editTextOtherInfo.setText(data.optString("about_coach_profile"));
        }

        if (data.has("certificate_title")) {
            certificateTitle = data.optString("certificate_title");
            fragmentCoachProfessionalInformationBinding.editTextCertificateTitle.setText(data.optString("certificate_title"));
        }

        if(data.has("certificate_url")){


           // fragmentCoachProfessionalInformationBinding.editTextCertificateTitle.setText(data.optString("certificate_title"));
            imagePathBanner=data.optString("certificate_url");
            Glide.with(getActivity()).load(data.optString("certificate_url"))
                    .into(fragmentCoachProfessionalInformationBinding.roundedIvCertificate);
            if(data.optString("certificate_url").isEmpty()){
                fragmentCoachProfessionalInformationBinding.tvRemoveCertificate.setText(getActivity().getResources().getString(R.string.certificate_upload));
            }else{
                fragmentCoachProfessionalInformationBinding.tvCertificate.setText(getActivity().getResources().getString(R.string.remove_certificate));
            }



//            if((data.optString("certificate_url").isEmpty())){
//                rl_certificate.setVisibility(View.GONE);
//            }else{
//                rl_certificate.setVisibility(View.VISIBLE);
//                tvCertificate.setText("Certificate: "+data.optString("certificate_title") );
//                Glide.with(mContext).load(data.optString("certificate_url"))
//                        .into(img_certificate);
//                img_certificate.setOnClickListener(v -> {
//                    imageViewDialog(data.optString("certificate_url"));
//                });
//
//            }
                /*JSONArray jsonArray=jsonObject_professional_info.getJSONArray("certificate");
                JSONObject modelJson;
                object = new ArrayList<>();
                // Process each result in json array, decode and convert to CardModel object
                for (int i = 0; i < jsonArray.length(); i++) {

                        modelJson = jsonArray.getJSONObject(i);

                    CoachList.certificate model = new CoachList.certificate(modelJson);
                    if (model != null) {
                        object.add(model);
                    }
                }
                Log.d("link",object.size()+"");*/

        }
        /*if(data.has("certificate")){
            try {
                JSONArray jsonArray=data.getJSONArray("certificate");
                JSONObject modelJson;
                object = new ArrayList<>();
                // Process each result in json array, decode and convert to CardModel object
                for (int i = 0; i < jsonArray.length(); i++) {

                    modelJson = jsonArray.getJSONObject(i);

                    CoachList.certificate model = new CoachList.certificate(modelJson);
                    if (model != null) {
                        object.add(model);
                    }
                }
                etcertificate_title.setText(object.get(0).getTitle());
                imagepath=object.get(0).getCertificate_url();
                Glide.with(getActivity()).load(object.get(0).getCertificate_url())
                        .into(iv_upload_certificate);
                tv_click_uploadCertificate.setText(getActivity().getResources().getString(R.string.remove_certificate));
                Log.d("linkpro",object.size()+"/"+object.get(0).getCertificate_url());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/

        if (data.has("exp_years")) {

            String yearName="";
            if(data.optString("exp_years").equalsIgnoreCase("0")){
                yearName = data.optString("exp_years") + " " + "year";
            }else if(data.optString("exp_years").equalsIgnoreCase("1")){
                yearName = data.optString("exp_years") + " " + "year";
            }else if(data.optString("exp_years").equalsIgnoreCase("")){
                yearName = "0" + " " + "year";
            }
            else{
                yearName = data.optString("exp_years") + " " + "years";
            }

            selectedYear = yearName;
            if(yearName.contains("year")){
                selectedYear = yearName.replace("year","").trim();
            }if(yearName.contains("years")){
                selectedYear = yearName.replace("years","").trim();
            }


            fragmentCoachProfessionalInformationBinding.dropYear.setText(yearName);

        }
        if (data.has("exp_months")) {
            String monthName="";
            if(data.optString("exp_months").equalsIgnoreCase("0") || data.optString("exp_months").equalsIgnoreCase("1")){
                monthName = data.optString("exp_months") + " " + "month";
            }else if(data.optString("exp_months").equalsIgnoreCase("")){
                monthName = "0" + " " + "month";
            }
            else{
                monthName = data.optString("exp_months") + " " + "months";
            }


            selectedMonth = monthName;
            if(monthName.contains("month")){
                selectedMonth = monthName.replace("month","").trim();
            }if(monthName.contains("months")){
                selectedMonth = monthName.replace("months","").trim();
            }
            fragmentCoachProfessionalInformationBinding.dropMonth.setText(monthName);
        }

//        if (data.has("cuurency_code")) {
//            String currencyName = data.optString("cuurency_code");
//            selectedCurency = currencyName.replace("?","").trim();
//            spinerCurency.setText(selectedCurency);
//        }

        if (data.has("charge_amount")) {
            amount = data.optString("charge_amount");
            fragmentCoachProfessionalInformationBinding.editTextAmountPerSession.setText(data.optString("charge_amount"));
        }


        if (data.has("Specialization_category")) {
            try {
                JSONArray jsonArray = data.getJSONArray("Specialization_category");
                JSONObject jsonObject = null;
                Datum coachLanguage = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    coachLanguage = new Datum(jsonObject);
                    Log.d("speclization", jsonObject.getString("title") + "/" + jsonObject.getString("id"));
                    // coachLanguage= new DataModel(jsonObject);
                    editList_speclization.add(coachLanguage);
                }


                fragmentCoachProfessionalInformationBinding.rvSpecilities.setAdapter(new CoachSpecialitiesListAdapter(requireActivity(),editList_speclization, modelArrayList.getData(), new CoachSpecialitiesListAdapter.ItemListener() {
                    @Override
                    public void onItemClick(List<String> item) {
                        categoryId = new StringBuilder();
                        String prefix = "";
                        for (String itemm : item) {
                            categoryId.append(prefix);
                            prefix = ",";
                            categoryId.append(itemm);
                        }
                    }
                }));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void setSaveButtonVisibility(boolean isVisible) {
        if (fragmentCoachProfessionalInformationBinding.flSave != null) {
            fragmentCoachProfessionalInformationBinding.flSave.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }


}