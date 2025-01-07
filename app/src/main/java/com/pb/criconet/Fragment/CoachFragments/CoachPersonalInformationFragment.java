package com.pb.criconet.Fragment.CoachFragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.mukesh.OtpView;
import com.pb.criconet.Activity.Academy.AddAcademyInformationActivity;
import com.pb.criconet.Activity.Coach.RegisterAsAnECoachActivity;
import com.pb.criconet.R;
import com.pb.criconet.adapter.SpecialitiesAdapter;
import com.pb.criconet.databinding.FragmentCoachPersonalInformationBinding;
import com.pb.criconet.model.AcademyModel.CoachLanguage;
import com.pb.criconet.model.Country;
import com.pb.criconet.model.DataModelSpecialities;
import com.pb.criconet.model.Language;
import com.pb.criconet.model.StreamingModel.City;
import com.pb.criconet.model.StreamingModel.States;
import com.pb.criconet.util.BookingHistoryDropDown;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.DataModel;
import com.pb.criconet.util.DropDownBlue;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.LanguageDropDown;
import com.pb.criconet.util.MultipartRequest;
import com.pb.criconet.util.SessionManager;
import com.pb.criconet.util.Toaster;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import timber.log.Timber;


public class CoachPersonalInformationFragment extends Fragment {

    FragmentCoachPersonalInformationBinding fragmentCoachPersonalInformationBinding;
    private ArrayList<DataModel> option_city = new ArrayList<>();
    private ArrayList<DataModel> option_state = new ArrayList<>();
    private ArrayList<DataModel> option_country = new ArrayList<>();
    private ArrayList<DataModel> option_language = new ArrayList<>();

    private RequestQueue queue;
    CustomLoaderView loaderView;
    private SharedPreferences prefs;

    private Uri photoUri;
    private String profileImagePath = "", imagePathBanner = "", FROM = "", countryID = "", stateID = "", cityID = "";
    private Cursor cursor;
    private int columnindex, i;
    private String file_pathid = "";

    private Country countryArrayList;
    private City citymodelArrayList;
    private States statemodelArrayList;
    ArrayList<String> coachLanguages = new ArrayList<>();
    private Language languageModelArrayList;
    ArrayList<Language.Datum> language = null;
    Dialog dialog;
    ArrayList<String> languageArray_new = new ArrayList<>();
    private StringBuilder langStringBuilder;
    String roleName = "", roleId = "", countryName = "", state_Name = "", city_Name = "";
    private String facebook = "", twitter = "", youtube = "", instagram = "", linkedIn = "", what_you_teach = "", skills_you_learn = "", about_coach_profile = "", achievement = "", username = "", password = "", gender = "", phoneNumber = "", pincode = "", address = "", landmark = "", email_String = "", mName = "", name_String, fname_String, lname_String, gender_String;
    private OtpView otpView;
    private String img_type = "";

    private String fromWhere="";
    JSONObject jsonObject_personal_info;

    //TODO get the image from gallery and display it
    ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    assert result.getData() != null;
                    Uri image_uri = result.getData().getData();
                    imagePathBanner = image_uri.toString();

                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    cursor = getActivity().getContentResolver().query(image_uri, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    columnindex = cursor.getColumnIndex(filePathColumn[0]);
                    file_pathid = cursor.getString(columnindex);
                    // Log.e("Attachment Path:", attachmentFile);
                    //URIid = Uri.parse("file://" + file_pathid);
                    imagePathBanner = file_pathid;
                    fragmentCoachPersonalInformationBinding.coverImg.setImageURI(image_uri);
                    updateImageTask(imagePathBanner);
                }
            });

    //TODO capture the image using camera and display it
    ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Bitmap inputImage = uriToBitmap(photoUri);
                        Bitmap rotated = rotateBitmap(inputImage);

                        String[] projection = {MediaStore.Images.Media.DATA};
                        @SuppressWarnings("deprecation")
                        Cursor cursor = getActivity().managedQuery(photoUri, projection, null, null, null);
                        int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        String capturedImageFilePath = cursor.getString(column_index_data);
                        imagePathBanner = capturedImageFilePath;
                        fragmentCoachPersonalInformationBinding.coverImg.setImageBitmap(rotated);
                        updateImageTask(imagePathBanner);

                    }
                }
            });

    //TODO get the image from gallery and display it on ProfilePic
//    ActivityResultLauncher<Intent> galleryProfileImageActivityResultLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    if (result.getResultCode() == Activity.RESULT_OK) {
//                        assert result.getData() != null;
//                        Uri image_uri = result.getData().getData();
//                        //profileImagePath = image_uri.toString();
//
//                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//                        cursor = getActivity().getContentResolver().query(image_uri, filePathColumn, null, null, null);
//                        cursor.moveToFirst();
//                        columnindex = cursor.getColumnIndex(filePathColumn[0]);
//                        file_pathid = cursor.getString(columnindex);
//
//                        profileImagePath = file_pathid;
//                        fragmentCoachPersonalInformationBinding.profilePic.setImageURI(image_uri);
//
//                        updateImageTask(profileImagePath);
//                    }
//                }
//            });

    ActivityResultLauncher<Intent> galleryProfileImageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();

                        // Display the image using the URI
                        fragmentCoachPersonalInformationBinding.profilePic.setImageURI(imageUri);

                        // Handle the URI for further operations
                        try {
                            String filePath = copyUriToInternalStorage(imageUri);
                            if (filePath != null) {
                                updateImageTask(filePath); // Upload or process image
                            } else {
                                Toaster.customToast("Failed to copy the image");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });


    //TODO capture the image using camera and display it on ProfilePic
    ActivityResultLauncher<Intent> cameraProfileImageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Bitmap inputImage = uriToBitmap(photoUri);
                        Bitmap rotated = rotateBitmap(inputImage);
                        //profileImagePath = photoUri.toString();

                        String[] projection = {MediaStore.Images.Media.DATA};
                        @SuppressWarnings("deprecation")
                        Cursor cursor = getActivity().managedQuery(photoUri, projection, null, null, null);
                        int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        String capturedImageFilePath = cursor.getString(column_index_data);
                        profileImagePath = capturedImageFilePath;
                        fragmentCoachPersonalInformationBinding.profilePic.setImageBitmap(rotated);

                        updateImageTask(profileImagePath);

                    }
                }
            });

    public CoachPersonalInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            // Retrieve the data from the arguments
            Bundle bundle = getArguments();
            if (bundle != null) {
                fromWhere = bundle.getString("FROM");

//                // Receiving the JSONObject
//                String jsonObjectString  = bundle.getString("Personal");
//
//                Toaster.customToast(jsonObjectString);
//
//                try {
//                    if (jsonObjectString != null) {
//                        jsonObject_personal_info = new JSONObject(jsonObjectString);
//                        Toaster.customToast(fromWhere+"//"+jsonObject_personal_info.get("first_name"));
//                        if(fromWhere.equalsIgnoreCase("1")){
//                            setData(jsonObject_personal_info);
//                        }
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

            }
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentCoachPersonalInformationBinding = FragmentCoachPersonalInformationBinding.inflate(inflater, container, false);
        //TODO ask for permission of camera upon first launch of application
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                    ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, 112);
            }
        }
        return fragmentCoachPersonalInformationBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        loaderView = CustomLoaderView.initialize(getActivity());
        queue = Volley.newRequestQueue(getActivity());

        if (Global.isOnline(getActivity())) {
            getCountry();
        } else {
            Global.showDialog(getActivity());
        }

        if (Global.isOnline(getActivity())) {
            getLanguage();
        } else {
            Global.showDialog(getActivity());
        }

        if(fromWhere.equalsIgnoreCase("1")){
            if (Global.isOnline(getActivity())) {
                getUsersDetails();
            } else {
                Global.showDialog(getActivity());
            }
        }


        //Todo firstName component
        fragmentCoachPersonalInformationBinding.editTextFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                fragmentCoachPersonalInformationBinding.filledTextFieldFirstName.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    fragmentCoachPersonalInformationBinding.filledTextFieldFirstName.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                fragmentCoachPersonalInformationBinding.filledTextFieldFirstName.setErrorEnabled(false);
            }
        });

        //Todo MiddleName component
        fragmentCoachPersonalInformationBinding.editTextMiddleName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                fragmentCoachPersonalInformationBinding.filledTextFieldMiddleName.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    fragmentCoachPersonalInformationBinding.filledTextFieldMiddleName.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                fragmentCoachPersonalInformationBinding.filledTextFieldMiddleName.setErrorEnabled(false);
            }
        });

        //Todo LastName component
        fragmentCoachPersonalInformationBinding.editTextLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                fragmentCoachPersonalInformationBinding.filledTextFieldLastName.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    fragmentCoachPersonalInformationBinding.filledTextFieldLastName.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                fragmentCoachPersonalInformationBinding.filledTextFieldLastName.setErrorEnabled(false);
            }
        });

        //Todo LastName component
        fragmentCoachPersonalInformationBinding.editPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                fragmentCoachPersonalInformationBinding.filledTextPhone.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    fragmentCoachPersonalInformationBinding.filledTextPhone.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                fragmentCoachPersonalInformationBinding.filledTextPhone.setErrorEnabled(false);
            }
        });

        //Todo Address1 component
        fragmentCoachPersonalInformationBinding.editTextAddress1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                fragmentCoachPersonalInformationBinding.filledTextFieldAddress1.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    fragmentCoachPersonalInformationBinding.filledTextFieldAddress1.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                fragmentCoachPersonalInformationBinding.filledTextFieldAddress1.setErrorEnabled(false);
            }
        });

        //Todo Address2 component
        fragmentCoachPersonalInformationBinding.editTextAddress2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                fragmentCoachPersonalInformationBinding.filledTextFieldAddress2.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    fragmentCoachPersonalInformationBinding.filledTextFieldAddress2.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                fragmentCoachPersonalInformationBinding.filledTextFieldAddress2.setErrorEnabled(false);
            }
        });

        //Todo Pincode component
        fragmentCoachPersonalInformationBinding.editTextPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                fragmentCoachPersonalInformationBinding.filledTextFieldPincode.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    fragmentCoachPersonalInformationBinding.filledTextFieldPincode.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                fragmentCoachPersonalInformationBinding.filledTextFieldPincode.setErrorEnabled(false);
            }
        });

        //Todo Facebook component
        fragmentCoachPersonalInformationBinding.editTextFacebook.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                fragmentCoachPersonalInformationBinding.filledTextFieldFacebook.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    fragmentCoachPersonalInformationBinding.filledTextFieldFacebook.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                fragmentCoachPersonalInformationBinding.filledTextFieldFacebook.setErrorEnabled(false);
            }
        });

        //Todo Twitter component
        fragmentCoachPersonalInformationBinding.editTextTwitter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                fragmentCoachPersonalInformationBinding.filledTextFieldTwitter.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    fragmentCoachPersonalInformationBinding.filledTextFieldTwitter.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                fragmentCoachPersonalInformationBinding.filledTextFieldTwitter.setErrorEnabled(false);
            }
        });

        //Todo Instagram component
        fragmentCoachPersonalInformationBinding.editTextInstagram.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                fragmentCoachPersonalInformationBinding.filledTextFieldInstagram.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    fragmentCoachPersonalInformationBinding.filledTextFieldInstagram.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                fragmentCoachPersonalInformationBinding.filledTextFieldInstagram.setErrorEnabled(false);
            }
        });

        //Todo LinkedIn component
        fragmentCoachPersonalInformationBinding.editTextLinked.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                fragmentCoachPersonalInformationBinding.filledTextFieldLinked.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    fragmentCoachPersonalInformationBinding.filledTextFieldLinked.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                fragmentCoachPersonalInformationBinding.filledTextFieldLinked.setErrorEnabled(false);
            }
        });

        fragmentCoachPersonalInformationBinding.coverImg.setOnClickListener(v -> {
            openCameraAndGalleryDialog("Banner");
        });
        fragmentCoachPersonalInformationBinding.profilePic.setOnClickListener(v -> {
            openCameraAndGalleryDialog("Profile");
        });

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_select_language);
        dialog.setCancelable(false);

        fragmentCoachPersonalInformationBinding.textViewLanguage.setOnClickListener(v -> {
            dialog.show();
        });

        fragmentCoachPersonalInformationBinding.flSave.setOnClickListener(v -> {
            if (checkValidation()) {
                if (Global.isOnline(getActivity())) {
                    submitCoachPersonalInfo();
                } else {
                    Global.showDialog(getActivity());
                }
            }
        });


    }


    //TODO open camera and gallery using this bottomSliderDialog
    public void openCameraAndGalleryDialog(String selectedPostType) {

        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogTheme);
        dialog.setContentView(R.layout.dialog_camera_selector);

        TextView tv_choose = dialog.findViewById(R.id.tv_choose);
        TextView tv_camera = dialog.findViewById(R.id.tv_camera);
        TextView tvGallery = dialog.findViewById(R.id.tvGallery);
        TextView tvCancel = dialog.findViewById(R.id.tvCancel);

        if (selectedPostType.equalsIgnoreCase("Banner")) {
            img_type ="Banner";
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
        } else if (selectedPostType.equalsIgnoreCase("Profile")) {
            img_type ="Profile";
            tv_camera.setOnClickListener(v -> {
                dialog.dismiss();

                openCameraProfileImage();
            });
            tvGallery.setOnClickListener(v -> {
                dialog.dismiss();
                openGalleryProfileImage();
            });
            Objects.requireNonNull(tvCancel).setOnClickListener(v -> {
                dialog.dismiss();
            });
        } else {
            Toaster.customToast("No Selection!");
        }

        dialog.show();

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

    //TODO takes profile image from camera
    private void openCameraProfileImage() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        photoUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        cameraProfileImageActivityResultLauncher.launch(cameraIntent);
    }

    //TODO takes profile image from gallery
    private void openGalleryProfileImage() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryProfileImageActivityResultLauncher.launch(galleryIntent);
    }

    //TODO takes URI of the image and returns bitmap
    private Bitmap uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getActivity().getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //TODO rotate image if image captured on samsung devices
    //TODO Most phone cameras are landscape, meaning if you take the photo in portrait, the resulting photos will be rotated 90 degrees.
    @SuppressLint("Range")
    public Bitmap rotateBitmap(Bitmap input) {
        String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};
        Cursor cur = getActivity().getContentResolver().query(photoUri, orientationColumn, null, null, null);
        int orientation = -1;
        if (cur != null && cur.moveToFirst()) {
            orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
        }
        Log.d("tryOrientation", orientation + "");
        Matrix rotationMatrix = new Matrix();
        rotationMatrix.setRotate(orientation);
        Bitmap cropped = Bitmap.createBitmap(input, 0, 0, input.getWidth(), input.getHeight(), rotationMatrix, true);
        return cropped;
    }

    private void getCountry() {
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_countries", response -> {
            Log.d("ResponseCountry", response);
            Gson gson = new Gson();
            countryArrayList = gson.fromJson(response, Country.class);
            if (countryArrayList.getApiStatus().equalsIgnoreCase("200")) {
                ArrayList<String> country = new ArrayList<>();
                country.add("Country");
                for (Country.Datum data : countryArrayList.getData()) {
                    country.add(data.getName());
                    option_country.add(new DataModel(data.getName(), data.getId()));
                }

                fragmentCoachPersonalInformationBinding.dropCountry.setOptionList(option_country);
                fragmentCoachPersonalInformationBinding.dropCountry.setClickListener(new BookingHistoryDropDown.onClickInterface() {
                    @Override
                    public void onClickAction() {
                    }

                    @Override
                    public void onClickDone(String name, String id) {
                        countryID = id;
                        if (Global.isOnline(getActivity())) {
                            getState(countryID);
                        } else {
                            Global.showDialog(getActivity());
                        }
                    }


                    @Override
                    public void onDismiss() {
                    }
                });
            }

        }, error -> {
            error.printStackTrace();
            Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
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

    private void getState(String countryId) {
        option_state.clear();
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.GET, Global.URL + "get_states" + "&country_id=" + countryId, response -> {
            Log.d("ResponseState", response);
            if (!response.isEmpty()) {
                loaderView.hideLoader();
                Gson gson = new Gson();
                statemodelArrayList = gson.fromJson(response, States.class);
                if (statemodelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                    ArrayList<String> state = new ArrayList<>();
                    state.add("States/UT");
                    for (States.Datum data : statemodelArrayList.getData()) {
                        state.add(data.getName());
                        option_state.add(new DataModel(data.getName(), data.getId()));
                    }

                    fragmentCoachPersonalInformationBinding.dropState.setOptionList(option_state);
                    fragmentCoachPersonalInformationBinding.dropState.setClickListener(new BookingHistoryDropDown.onClickInterface() {
                        @Override
                        public void onClickAction() {
                        }

                        @Override
                        public void onClickDone(String name, String id) {
                            stateID = id;

                            if (Global.isOnline(getActivity())) {
                                getCity(stateID);
                            } else {
                                Global.showDialog(getActivity());
                            }
                        }


                        @Override
                        public void onDismiss() {
                        }
                    });
                }
            }


        }, error -> {
            loaderView.hideLoader();
            error.printStackTrace();
            Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void getCity(String stateId) {
        option_city.clear();
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.GET, Global.URL + "get_cities" + "&state_id=" + stateId, response -> {
            Log.d("ResponseCity", response);
            loaderView.hideLoader();
            Gson gson = new Gson();
            citymodelArrayList = gson.fromJson(response, City.class);
            if (citymodelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                ArrayList<String> city = new ArrayList<>();
                city.add("City");
                for (City.Datum data : citymodelArrayList.getData()) {
                    city.add(data.getName());
                    option_city.add(new DataModel(data.getName(), data.getId()));
                }

                fragmentCoachPersonalInformationBinding.dropCity.setOptionList(option_city);
                fragmentCoachPersonalInformationBinding.dropCity.setClickListener(new BookingHistoryDropDown.onClickInterface() {
                    @Override
                    public void onClickAction() {
                    }

                    @Override
                    public void onClickDone(String name, String id) {
                        cityID = id;
                    }


                    @Override
                    public void onDismiss() {
                    }
                });
            }

        }, error -> {
            loaderView.hideLoader();
            error.printStackTrace();
            Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void getLanguage() {
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.GET_LANGUAGE, response -> {
            Timber.d(response);
            Gson gson = new Gson();
            languageModelArrayList = gson.fromJson(response, Language.class);
            if (languageModelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                language = new ArrayList<>();
                language.addAll(languageModelArrayList.getData());
                languageSelectionDialog(language, new ArrayList<>());

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

    private void languageSelectionDialog(ArrayList<Language.Datum> languageArray, ArrayList<String> coachLanguages) {
        TextView btnOk = dialog.findViewById(R.id.btnOk);
        RecyclerView rv_language = dialog.findViewById(R.id.rv_language);
        rv_language.setHasFixedSize(true);
        rv_language.setLayoutManager(new LinearLayoutManager(getActivity()));
        //Log.d("ReSize",coachLanguages.size()+"");
        selectCoachLanguageAdapter selectCoachLanguageAdapter = new selectCoachLanguageAdapter(languageArray, coachLanguages, getActivity(), languageArrayy -> {
            Log.d("SizeSelected", languageArrayy.size() + "");
            languageArray_new = languageArrayy;
        });
        rv_language.setAdapter(selectCoachLanguageAdapter);
        //TextView btnCancel = dialog.findViewById(R.id.btnCancel);
//        btnCancel.setOnClickListener(view -> {
//            dialog.dismiss();
//        });
        btnOk.setOnClickListener(view -> {

            langStringBuilder = new StringBuilder();
            String prefix = "";

            for (int i = 0; i < languageArray_new.size(); i++) {
                langStringBuilder.append(prefix);
                prefix = " , ";
                langStringBuilder.append(languageArray_new.get(i));
            }

            //Log.d("size",langStringBuilder.toString());

            fragmentCoachPersonalInformationBinding.textViewLanguage.setText(langStringBuilder.toString());
            dialog.dismiss();
        });

        //dialog.show();

    }

    public static class selectCoachLanguageAdapter extends RecyclerView.Adapter<selectCoachLanguageAdapter.MyViewHolder> {
        Context context;
        ArrayList<Language.Datum> languageArray;
        ArrayList<String> checkedArray;
        ArrayList<String> coachLanguages;
        selectCoachLanguageAdapter.languageSelectionListner languageSelectionListner;

        public selectCoachLanguageAdapter(ArrayList<Language.Datum> languageArray, ArrayList<String> coachLanguages, Context context, selectCoachLanguageAdapter.languageSelectionListner languageSelectionListner) {
            this.context = context;
            this.languageArray = languageArray;
            this.languageSelectionListner = languageSelectionListner;
            this.coachLanguages = coachLanguages;
            checkedArray = new ArrayList<>();
        }

        @Override
        public selectCoachLanguageAdapter.@NotNull MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // inflate the item Layout
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_coach_spinner_item, parent, false);
            return new selectCoachLanguageAdapter.MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(selectCoachLanguageAdapter.MyViewHolder holder, final int position) {
            Language.Datum coachLanguage = languageArray.get(position);

            holder.textView.setText(coachLanguage.getName_eng());
            holder.checkBox.setChecked(coachLanguage.isSelected());

            Log.d("size", coachLanguages.size() + "");


            for (int j = 0; j < coachLanguages.size(); j++) {
                if (coachLanguages.get(j).equalsIgnoreCase(coachLanguage.getName_eng())) {
                    holder.checkBox.setChecked(true);
                    checkedArray.add(coachLanguages.get(j));
                    languageSelectionListner.itemChcked(checkedArray);
                    break;
                }
            }


            holder.checkBox.setOnClickListener(view -> {
                CheckBox myCheckBox = (CheckBox) view;
                Language.Datum coachLanguage1 = languageArray.get(position);

                if (myCheckBox.isChecked()) {
                    coachLanguage1.setSelected(true);
                    checkedArray.add(coachLanguage1.getName_eng());
                } else if (!myCheckBox.isChecked()) {
                    coachLanguage1.setSelected(false);
                    checkedArray.remove(coachLanguage1.getName_eng());
                }
                languageSelectionListner.itemChcked(checkedArray);
            });


//            holder.liLanguage.setOnClickListener(v -> {
//                // CheckBox myCheckBox = (CheckBox) v;
//                Language.Datum coachLanguage1 = languageArray.get(position);
//
//                if (holder.checkBox.isChecked()) {
//                    holder.checkBox.setChecked(false);
//                    coachLanguage1.setSelected(true);
//                    checkedArray.add(coachLanguage1.getName_eng());
//                } else if (!holder.checkBox.isChecked()) {
//                    holder.checkBox.setChecked(true);
//                    coachLanguage1.setSelected(false);
//                    checkedArray.remove(coachLanguage1.getName_eng());
//                }
//                languageSelectionListner.itemChcked(checkedArray);
//            });


        }

        @Override
        public int getItemCount() {
            return languageArray.size();
        }

        static class MyViewHolder extends RecyclerView.ViewHolder {
            CheckBox checkBox;
            TextView textView;
            LinearLayout liLanguage;

            MyViewHolder(View itemView) {
                super(itemView);
                checkBox = itemView.findViewById(R.id.radio);
                textView = itemView.findViewById(R.id.textView);
                liLanguage = itemView.findViewById(R.id.liLanguage);

            }
        }

        interface languageSelectionListner {
            void itemChcked(ArrayList<String> languageArray);
        }
    }


    private boolean checkValidation() {
        fname_String = fragmentCoachPersonalInformationBinding.editTextFirstName.getText().toString().trim();
        lname_String = fragmentCoachPersonalInformationBinding.editTextLastName.getText().toString().trim();
        mName = fragmentCoachPersonalInformationBinding.editTextMiddleName.getText().toString().trim();
        address = fragmentCoachPersonalInformationBinding.editTextAddress1.getText().toString().trim();
        landmark = fragmentCoachPersonalInformationBinding.editTextAddress2.getText().toString().trim();
        pincode = fragmentCoachPersonalInformationBinding.editTextPincode.getText().toString().trim();
        phoneNumber = fragmentCoachPersonalInformationBinding.editPhone.getText().toString().trim();
        facebook = fragmentCoachPersonalInformationBinding.editTextFacebook.getText().toString().trim();
        twitter = fragmentCoachPersonalInformationBinding.editTextTwitter.getText().toString().trim();
        linkedIn = fragmentCoachPersonalInformationBinding.editTextLinked.getText().toString().trim();
        instagram = fragmentCoachPersonalInformationBinding.editTextInstagram.getText().toString().trim();
        //amount = etAmount.getText().toString().trim();

        if (!Global.validateLengthofCoachRegisterr(fname_String)) {
            fragmentCoachPersonalInformationBinding.filledTextFieldFirstName.setErrorEnabled(true);
            fragmentCoachPersonalInformationBinding.filledTextFieldFirstName.setError("Name can't be empty!");
            fragmentCoachPersonalInformationBinding.filledTextFieldFirstName.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.app_light_red)));
            return false;
        }
//        else if (!Global.validateLengthofCoachRegisterr(mName)) {
//            Toaster.customToast(mContext.getResources().getString(R.string.Enter_Mid_Name_at_least_3_character));
//            return false;
//        }
        else if (!Global.validateLengthofCoachRegisterr(lname_String)) {
            fragmentCoachPersonalInformationBinding.filledTextFieldLastName.setErrorEnabled(true);
            fragmentCoachPersonalInformationBinding.filledTextFieldLastName.setError(getActivity().getResources().getString(R.string.Enter_Last_Name_at_least_3_character));
            fragmentCoachPersonalInformationBinding.filledTextFieldLastName.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.app_light_red)));
            return false;
        } else if (fragmentCoachPersonalInformationBinding.editPhone.getText().toString().isEmpty()) {
            fragmentCoachPersonalInformationBinding.filledTextPhone.setErrorEnabled(true);
            fragmentCoachPersonalInformationBinding.filledTextPhone.setError(getActivity().getResources().getString(R.string.enter_phone_number));
            fragmentCoachPersonalInformationBinding.filledTextPhone.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.app_light_red)));
            return false;
        } else if (!Global.isValidMobile(phoneNumber)) {
            fragmentCoachPersonalInformationBinding.filledTextPhone.setErrorEnabled(true);
            fragmentCoachPersonalInformationBinding.filledTextPhone.setError(getActivity().getResources().getString(R.string.error_invalid_phone));
            fragmentCoachPersonalInformationBinding.filledTextPhone.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.app_light_red)));
            return false;
        } else if (phoneNumber.equalsIgnoreCase("0000000000") || phoneNumber.startsWith("1111111111") ||
                phoneNumber.equalsIgnoreCase("2222222222") || phoneNumber.equalsIgnoreCase("3333333333") ||
                phoneNumber.equalsIgnoreCase("4444444444") || phoneNumber.equalsIgnoreCase("5555555555") ||
                phoneNumber.equalsIgnoreCase("6666666666") || phoneNumber.equalsIgnoreCase("7777777777") ||
                phoneNumber.equalsIgnoreCase("8888888888") || phoneNumber.equalsIgnoreCase("9999999999")) {

            fragmentCoachPersonalInformationBinding.filledTextPhone.setErrorEnabled(true);
            fragmentCoachPersonalInformationBinding.filledTextPhone.setError(getActivity().getResources().getString(R.string.error_invalid_phone));
            fragmentCoachPersonalInformationBinding.filledTextPhone.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.app_light_red)));

            return false;
        } else if (fragmentCoachPersonalInformationBinding.textViewLanguage.getText().toString().isEmpty()) {
            Toaster.customToast(getResources().getString(R.string.Select_Language_You_Speak));
            return false;
        } else if (!Global.isValidAddress(address)) {
            fragmentCoachPersonalInformationBinding.filledTextFieldAddress1.setErrorEnabled(true);
            fragmentCoachPersonalInformationBinding.filledTextFieldAddress1.setError(getActivity().getResources().getString(R.string.enter_address));
            fragmentCoachPersonalInformationBinding.filledTextFieldAddress1.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.app_light_red)));
            return false;
        } else if (!Global.isValidAddress(landmark)) {
            fragmentCoachPersonalInformationBinding.filledTextFieldAddress2.setErrorEnabled(true);
            fragmentCoachPersonalInformationBinding.filledTextFieldAddress2.setError(getActivity().getResources().getString(R.string.enter_land_mark));
            fragmentCoachPersonalInformationBinding.filledTextFieldAddress2.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.app_light_red)));
            return false;
        } else if (countryID.isEmpty()) {
            Toaster.customToast(getResources().getString(R.string.Select_Country));
            return false;
        } else if (stateID.isEmpty()) {
            Toaster.customToast(getResources().getString(R.string.select_stat));
            return false;
        } else if (cityID.isEmpty()) {
            Toaster.customToast(getResources().getString(R.string.Select_state_to_select_city));
            return false;
        } else if (pincode.isEmpty()) {
            fragmentCoachPersonalInformationBinding.filledTextFieldPincode.setErrorEnabled(true);
            fragmentCoachPersonalInformationBinding.filledTextFieldPincode.setError(getActivity().getResources().getString(R.string.enter_pincode));
            fragmentCoachPersonalInformationBinding.filledTextFieldPincode.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            return false;
        } else if (pincode.equalsIgnoreCase("111111") || pincode.equalsIgnoreCase("222222") ||
                pincode.equalsIgnoreCase("333333") || pincode.equalsIgnoreCase("444444") ||
                pincode.equalsIgnoreCase("555555") || pincode.equalsIgnoreCase("666666") ||
                pincode.equalsIgnoreCase("777777") || pincode.equalsIgnoreCase("888888") ||
                pincode.equalsIgnoreCase("999999") || pincode.equalsIgnoreCase("000000")) {
            fragmentCoachPersonalInformationBinding.filledTextFieldPincode.setErrorEnabled(true);
            fragmentCoachPersonalInformationBinding.filledTextFieldPincode.setError(getActivity().getResources().getString(R.string.enter_pincodee));
            fragmentCoachPersonalInformationBinding.filledTextFieldPincode.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            return false;
        } else if (!Global.isValidPincode(pincode)) {
            fragmentCoachPersonalInformationBinding.filledTextFieldPincode.setErrorEnabled(true);
            fragmentCoachPersonalInformationBinding.filledTextFieldPincode.setError(getActivity().getResources().getString(R.string.enter_pincodee));
            fragmentCoachPersonalInformationBinding.filledTextFieldPincode.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        }

        return true;
    }

    public void submitCoachPersonalInfo() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.UPDATE_COACH_PERSONAL_INFO,
                new Response.Listener<String>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(String response) {
                        Timber.d(response);
                        loaderView.hideLoader();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                // Global.msgDialog(mActivity, "Profile saved successfully!");

                                if (jsonObject.has("data")) {
                                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                    phoneNumber = jsonObjectData.getString("temp_mobile_no");
                                    SessionManager.save_name(prefs, jsonObjectData.getString("username"));
                                    SessionManager.save_emailid(prefs, jsonObjectData.getString("email"));
                                    SessionManager.savePhone(prefs, jsonObjectData.getString("phone_number"));
                                    SessionManager.savePhoneCode(prefs, jsonObjectData.getString("phone_code"));
                                    SessionManager.save_sex(prefs, jsonObjectData.getString("gender"));
                                    SessionManager.save_image(prefs, jsonObjectData.getString("avatar"));
                                    SessionManager.save_cover(prefs, jsonObjectData.getString("cover"));
                                    SessionManager.save_mobile_verified(prefs, jsonObjectData.getString("is_mobile_verified"));
                                    SessionManager.save_profiletype(prefs, jsonObjectData.getString("profile_type"));

                                    String is_contact_verify = jsonObjectData.getString("is_mobile_verified");
                                    if (is_contact_verify.equalsIgnoreCase("0")) {
                                        EmailOtpDialog(phoneNumber);
                                    } else {
                                        congratsDialog();
                                    }
                                }

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(getActivity(), jsonObject.optString("errors"));
                            } else {
                                Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> {
                    error.printStackTrace();
                    loaderView.hideLoader();
                    Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
//                Global.msgDialog(EditProfile.this, "Internet connection is slow");
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("first_name", fname_String);
                param.put("last_name", lname_String);
                param.put("mid_name", mName);
                param.put("address", address);
                param.put("address2", landmark);
                param.put("country_id", countryID);
                param.put("state_id", stateID);
                param.put("city_id", cityID);
                param.put("pincode", pincode);
                param.put("phone_number", phoneNumber);
                param.put("twitter", twitter);
                param.put("facebook", facebook);
                param.put("linkedin", linkedIn);
                param.put("instagram", instagram);
                param.put("youtube", youtube);
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("language_id", fragmentCoachPersonalInformationBinding.textViewLanguage.getText().toString().trim());


                System.out.println("data   :" + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }


    private void EmailOtpDialog(String mobile) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_email_verification);
        dialog.setCancelable(false);


        TextView tvOTPTime = dialog.findViewById(R.id.tv_otp_time);
        TextView btResend = dialog.findViewById(R.id.btn_resend);
        FrameLayout btContinue = dialog.findViewById(R.id.fl_btn_continue);
        ImageView img_close = dialog.findViewById(R.id.img_close);
        LinearLayout lay_otp_expire = dialog.findViewById(R.id.lay_otp_expire);
        CountryCodePicker ccp = dialog.findViewById(R.id.ccp);
        EditText edit_phone = dialog.findViewById(R.id.edit_phone);

        startTimer(tvOTPTime, btResend, lay_otp_expire);

        btResend.setOnClickListener(view -> {
            resendOTP(mobile);
            lay_otp_expire.setVisibility(View.VISIBLE);
            startTimer(tvOTPTime, btResend, lay_otp_expire);
        });
        otpView = dialog.findViewById(R.id.otp_view);
        btContinue.setOnClickListener(v -> {
            lay_otp_expire.setVisibility(View.VISIBLE);
            if (Objects.requireNonNull(otpView.getText()).toString().isEmpty()) {
                Toaster.customToast(getString(R.string.code_msg));
            } else if (otpView.getText().toString().length() != 4) {
                Toaster.customToast(getString(R.string.code_invalid));
            } else {
                sendVerifyOtp(otpView.getText().toString().trim(), dialog);
            }
        });

        img_close.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();

    }

    private void resendOTP(String mobile) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.RESEND_OTP,
                response -> {
                    Timber.tag("login response").e("%s", response);
                    loaderView.hideLoader();
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {

                        } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                            Toaster.customToast(jsonObject.optJSONObject("errors").optString("error_text"));
                        } else {
                            Toaster.customToast(getResources().getString(R.string.socket_timeout));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    loaderView.hideLoader();
                    Toaster.customToast(getResources().getString(R.string.socket_timeout));
//                Global.msgDialog(Signup.this, "Internet connection is slow");
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("phone_number", mobile);
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                System.out.println("data   " + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void sendVerifyOtp(String otp, Dialog dialog) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.OTP_VERIFY,
                response -> {
                    Timber.tag("login response").e("%s", response);
                    loaderView.hideLoader();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {
                            dialog.dismiss();
                            if (jsonObject.has("data")) {
                                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                phoneNumber = jsonObjectData.getString("temp_mobile_no");
                                SessionManager.save_name(prefs, jsonObjectData.getString("username"));
                                SessionManager.save_emailid(prefs, jsonObjectData.getString("email"));
                                SessionManager.save_mobile(prefs, jsonObjectData.getString("phone_number"));
                                SessionManager.savePhoneCode(prefs, jsonObjectData.getString("phone_code"));
                                SessionManager.save_sex(prefs, jsonObjectData.getString("gender"));
                                SessionManager.save_image(prefs, jsonObjectData.getString("avatar"));
                                SessionManager.save_cover(prefs, jsonObjectData.getString("cover"));
                                SessionManager.save_profiletype(prefs, jsonObjectData.getString("profile_type"));
                                // Toaster.customToast(jsonObjectData.getString("is_mobile_verified"));
                                SessionManager.save_mobile_verified(prefs, jsonObjectData.getString("is_mobile_verified"));
//                                    JSONObject ambassadorProfile = jsonObjectData.getJSONObject("ambassadorProfile");
//
//                                    if(ambassadorProfile.length()>0){
//                                        SessionManager.save_is_ambassador(prefs,"1");
//                                        SessionManager.save_is_amb_name(prefs,ambassadorProfile.getString("name"));
//                                        SessionManager.save_is_amb_fullname(prefs,ambassadorProfile.getString("full_name"));
//                                        SessionManager.save_is_amb_email(prefs,ambassadorProfile.getString("email"));
//                                        SessionManager.save_mobile(prefs,ambassadorProfile.getString("phone_number"));
//                                        SessionManager.save_is_amb_college(prefs,ambassadorProfile.getString("school_college_name"));
//                                        SessionManager.save_is_amb_highestQ(prefs,ambassadorProfile.getString("height_qualification"));
//                                        SessionManager.save_is_ambs_have_you_org_event_flag(prefs,ambassadorProfile.getString("have_you_org_event_flag"));
//                                        SessionManager.save_is_ambs_have_you_org_event_txt(prefs,ambassadorProfile.getString("have_you_org_event_txt"));
//                                        SessionManager.save_is_ambs_innovative_thing(prefs,ambassadorProfile.getString("innovative_thing"));
//                                        SessionManager.save_is_ambs_how_many_hrs_per_week(prefs,ambassadorProfile.getString("how_many_hrs_per_week"));
//                                        SessionManager.save_is_ambs_passionate_thing(prefs,ambassadorProfile.getString("passionate_thing"));
//                                        SessionManager.save_is_ambs_do_you_want_campus_ambassdor(prefs,ambassadorProfile.getString("do_you_want_campus_ambassdor"));
//                                        SessionManager.save_is_ambs_thing_you_are_know_criconet(prefs,ambassadorProfile.getString("thing_you_are_know_criconet"));
//                                    }else{
//                                        SessionManager.save_is_ambassador(prefs,"0");
//                                    }

                                congratsDialog();

                            }

                        } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                            Toaster.customToast(jsonObject.optJSONObject("errors").optString("error_text"));
                        } else {
                            Toaster.customToast(getResources().getString(R.string.socket_timeout));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    loaderView.hideLoader();
                    Toaster.customToast(getResources().getString(R.string.socket_timeout));
//                Global.msgDialog(Signup.this, "Internet connection is slow");
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("otp", otp);
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                System.out.println("data   " + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void startTimer(TextView tvOTPTime, TextView btResend, LinearLayout lay_otp_expire) {
        new CountDownTimer(180000, 1000) {

            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                tvOTPTime.setText("00 : " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                lay_otp_expire.setVisibility(View.GONE);
                btResend.setVisibility(View.VISIBLE);
            }

        }.start();
    }

    private void congratsDialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_congratulation);
        dialog.setCancelable(false);
        TextView tv_cancel_confirmed_msg = dialog.findViewById(R.id.tv_cancel_confirmed_msg);
        tv_cancel_confirmed_msg.setText(getResources().getString(R.string.Information_saved_successfully_Please_proceed_to_continue_your_registration));
        FrameLayout btContinue = dialog.findViewById(R.id.fl_done);

        btContinue.setOnClickListener(v -> {
            dialog.dismiss();

            ((RegisterAsAnECoachActivity) getActivity()).switchToNextFragment(1);

        });

        dialog.show();
    }

    public void updateImageTask(String path) {
        loaderView.showLoader();
        try {
            MultipartEntity entity = new MultipartEntity();
            entity.addPart("user_id", new StringBody(SessionManager.get_user_id(prefs)));
            entity.addPart("s", new StringBody(SessionManager.get_session_id(prefs)));
            if (!(path.equals("") || path == null)) {
                File file = new File(path);
                FileBody fileBody = new FileBody(file);
                if (img_type.equals("Profile")) {
                    //Toaster.customToast(img_type);
                    entity.addPart("image_type", new StringBody("avatar"));
                    entity.addPart("image", fileBody);
                } else if(img_type.equalsIgnoreCase("Banner")) {
                    entity.addPart("image_type", new StringBody("cover"));
                    entity.addPart("image", fileBody);
                }
            }
            MultipartRequest req = new MultipartRequest(Global.URL + "update_profile_picture", response -> {
                try {
                    loaderView.hideLoader();
                    Log.d("UpadteProfileIamge",response);
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
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

    public void getUsersDetails() {
        //loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_user_data",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Timber.d(response);
                        //loaderView.hideLoader();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                JSONObject object = jsonObject.getJSONObject("coach_data");

                                if (object.has("personal_info")) {
                                    JSONObject jsonObject_personal_info = object.getJSONObject("personal_info");
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
                error -> {
                    error.printStackTrace();
                    //loaderView.hideLoader();
                    Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
//                Global.msgDialog(EditProfile.this, "Internet connection is slow");
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

    private void setData(JSONObject data) {
        if (data.has("languages")) {

            try {
                JSONArray jsonArray = data.getJSONArray("languages");
                JSONObject jsonObject;
                CoachLanguage coachLanguage;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    coachLanguage = new CoachLanguage(jsonObject);
                    coachLanguages.add(coachLanguage.getName_eng());
                }
                languageSelectionDialog(language, coachLanguages);

                langStringBuilder = new StringBuilder();
                String prefix = "";
                for (String item : coachLanguages) {
                    langStringBuilder.append(prefix);
                    prefix = ",";
                    langStringBuilder.append(item);
                }
                //Log.d("size",langStringBuilder.toString());

                fragmentCoachPersonalInformationBinding.textViewLanguage.setText(langStringBuilder.toString());

                //Log.d("size",coachLanguages.size()+"");


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Toaster.customToast("//"+data.optString("first_name"));
        if (data.has("first_name")) {
            fragmentCoachPersonalInformationBinding.editTextFirstName.setText(data.optString("first_name"));
        }
        if (data.has("last_name")) {
            fragmentCoachPersonalInformationBinding.editTextLastName.setText(data.optString("last_name"));
        }
        if (data.has("mid_name")) {
            try {
                fragmentCoachPersonalInformationBinding.editTextMiddleName.setText(data.getString("mid_name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (data.has("cover")) {
            imagePathBanner = data.optString("cover");
            Glide.with(getActivity()).load(data.optString("cover")).placeholder(R.drawable.image_placeholder).into(fragmentCoachPersonalInformationBinding.coverImg);
        }
        if (data.has("avatar")) {
            profileImagePath = data.optString("avatar");
            Glide.with(getActivity()).load(data.optString("avatar")).placeholder(R.drawable.placeholder_user).into(fragmentCoachPersonalInformationBinding.profilePic);
        }

        if (data.has("country_name")) {
            countryName = data.optString("country_name");
            countryID = data.optString("country_id");
            getState(countryID);
            fragmentCoachPersonalInformationBinding.dropCountry.setText(countryName);
        }
        if (data.has("state_name")) {
            state_Name = data.optString("state_name");
            stateID = data.optString("state_id");
            getCity(stateID);
            fragmentCoachPersonalInformationBinding.dropState.setText(state_Name);
        }
        if (data.has("city_name")) {
            city_Name = data.optString("city_name");
            cityID = data.optString("city_id");
            fragmentCoachPersonalInformationBinding.dropCity.setText(city_Name);
        }

        if (data.has("pincode")) {
            fragmentCoachPersonalInformationBinding.editTextPincode.setText(data.optString("pincode"));
        }
        if (data.has("phone_number")) {
            fragmentCoachPersonalInformationBinding.editPhone.setText(data.optString("phone_number"));
        }
        if (data.has("address")) {
            fragmentCoachPersonalInformationBinding.editTextAddress1.setText(data.optString("address"));
        }
        if (data.has("address2")) {
            fragmentCoachPersonalInformationBinding.editTextAddress2.setText(data.optString("address2"));
        }

        if (data.has("facebook")) {
            fragmentCoachPersonalInformationBinding.editTextFacebook.setText(data.optString("facebook"));
        }
        if (data.has("twitter")) {
            fragmentCoachPersonalInformationBinding.editTextTwitter.setText(data.optString("twitter"));
        }

        if (data.has("linkedin")) {
            fragmentCoachPersonalInformationBinding.editTextLinked.setText(data.optString("linkedin"));
        }
        if (data.has("instagram")) {
            fragmentCoachPersonalInformationBinding.editTextInstagram.setText(data.optString("instagram"));
        }

//
//        "facebook": "https://www.facebook.com/criconetonline12/",
//                "twitter": "Afzal Hussain",
//                "linkedin": "Afzal Hussain",
//                "instagram": "hafzal446",
//                "youtube": "https://youtube.com/channel/UCEDb-adU-R4urGLkXlMileg",


    }


    private String getFilePathFromUri(Uri uri) {
        String filePath = null;

        if (uri != null) {
            // Check if the Uri scheme is "content" and use ContentResolver
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                // Try to get the file path using the ContentResolver
                Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                if (cursor != null) {
                    try {
                        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        if (cursor.moveToFirst()) {
                            filePath = cursor.getString(columnIndex);
                        }
                    } catch (IllegalArgumentException e) {
                        // Fallback for Android 10 (Scoped Storage), or if columnIndex doesn't exist
                        filePath = copyUriToInternalStorage(uri);
                    } finally {
                        cursor.close();
                    }
                }
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                // Directly use the file path if the Uri scheme is "file"
                filePath = uri.getPath();
            }
        }

        return filePath;
    }

    private String copyUriToInternalStorage(Uri uri) {
        try {
            // Open InputStream using ContentResolver
            InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                // Create a temporary file in the internal cache directory
                File tempFile = new File(getActivity().getCacheDir(), "temp_image.jpg");
                FileOutputStream outputStream = new FileOutputStream(tempFile);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                // Close the streams
                outputStream.close();
                inputStream.close();

                // Return the path of the copied file in cache directory
                return tempFile.getAbsolutePath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}