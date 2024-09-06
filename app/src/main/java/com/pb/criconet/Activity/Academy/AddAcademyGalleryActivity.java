package com.pb.criconet.Activity.Academy;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;

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
import com.pb.criconet.R;
import com.pb.criconet.adapter.AcademyAdapter.AcademyGalleryMultipleImageAdapter;
import com.pb.criconet.databinding.ActivityAddAcademyGalleryBinding;
import com.pb.criconet.databinding.ToolbarInnerpageBinding;
import com.pb.criconet.model.AcademyModel.AcademyGallery;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.HorizontalSpaceItemDecoration;
import com.pb.criconet.util.MultipartRequest;
import com.pb.criconet.util.SessionManager;
import com.pb.criconet.util.Toaster;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import timber.log.Timber;

public class AddAcademyGalleryActivity extends AppCompatActivity {
    ActivityAddAcademyGalleryBinding activityAddAcademyGalleryBinding;
    Context mContext;
    Activity mActivity;
    private RequestQueue queue;
    CustomLoaderView loaderView;
    private SharedPreferences prefs;
    ToolbarInnerpageBinding toolbarInnerpageBinding;
    private Uri photoUri, videoUri;
    ArrayList<AcademyGallery> mArrayUri = new ArrayList<>();
    private AcademyGalleryMultipleImageAdapter galleryAdapter;
    private String profileImagePath = "", imagePathBanner = "", selectedVideoPath = "",FROM="";
    private Cursor cursor;
    private int columnindex, i;
    private String file_pathid = "";


    //TODO get the image from gallery and display it on ProfilePic
    ActivityResultLauncher<Intent> galleryProfileImageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        assert result.getData() != null;
                        Uri image_uri = result.getData().getData();
                        //profileImagePath = image_uri.toString();

                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        cursor = mActivity.getContentResolver().query(image_uri, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        columnindex = cursor.getColumnIndex(filePathColumn[0]);
                        file_pathid = cursor.getString(columnindex);
                        // Log.e("Attachment Path:", attachmentFile);
                       // URIid = Uri.parse("file://" + file_pathid);
                        profileImagePath = file_pathid;



                        activityAddAcademyGalleryBinding.profilePic.setImageURI(image_uri);
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
                        Cursor cursor = mActivity.managedQuery(photoUri, projection, null, null, null);
                        int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        String capturedImageFilePath = cursor.getString(column_index_data);
                        profileImagePath = capturedImageFilePath;


                        activityAddAcademyGalleryBinding.profilePic.setImageBitmap(rotated);

                    }
                }
            });

    //TODO get the image from gallery and display it
    ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        assert result.getData() != null;
                        Uri image_uri = result.getData().getData();
                        imagePathBanner = image_uri.toString();

                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        cursor = mActivity.getContentResolver().query(image_uri, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        columnindex = cursor.getColumnIndex(filePathColumn[0]);
                        file_pathid = cursor.getString(columnindex);
                        // Log.e("Attachment Path:", attachmentFile);
                        //URIid = Uri.parse("file://" + file_pathid);
                        imagePathBanner = file_pathid;


                        activityAddAcademyGalleryBinding.coverImg.setImageURI(image_uri);
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
                        Bitmap inputImage = uriToBitmap(photoUri);
                        Bitmap rotated = rotateBitmap(inputImage);

                        String[] projection = {MediaStore.Images.Media.DATA};
                        @SuppressWarnings("deprecation")
                        Cursor cursor = mActivity.managedQuery(photoUri, projection, null, null, null);
                        int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        String capturedImageFilePath = cursor.getString(column_index_data);
                        imagePathBanner = capturedImageFilePath;


                        imagePathBanner = result.getData().getData().toString();
                        activityAddAcademyGalleryBinding.coverImg.setImageBitmap(rotated);

                    }
                }
            });

    //TODO select multiple image from gallery
    ActivityResultLauncher<String> galleryMultipleLauncher = registerForActivityResult(
            new ActivityResultContracts.GetMultipleContents(),
            uriList -> {
                mArrayUri.clear();
                AcademyGallery academyGallery;
                JSONObject jsonObject = null;
                for (int i = 0; i < uriList.size(); i++) {
                    try {
                        jsonObject = new JSONObject();
                        jsonObject.put("id", i);
                        jsonObject.put("files", uriList.get(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    academyGallery = new AcademyGallery(jsonObject);
                    mArrayUri.add(academyGallery);
                }
                if (mArrayUri.size() > 0) {
                    activityAddAcademyGalleryBinding.hsGallery.setVisibility(View.GONE);
                    activityAddAcademyGalleryBinding.gvMultipleImage.setVisibility(View.VISIBLE);
                    galleryAdapter = new AcademyGalleryMultipleImageAdapter(mContext, mArrayUri, new AcademyGalleryMultipleImageAdapter.ItemListener() {
                        @Override
                        public void onItemClickk(int size, String _galleryId) {
                            if (mArrayUri.size() == 0) {
                                activityAddAcademyGalleryBinding.hsGallery.setVisibility(View.VISIBLE);
                                activityAddAcademyGalleryBinding.gvMultipleImage.setVisibility(View.GONE);
                            } else {
                                activityAddAcademyGalleryBinding.hsGallery.setVisibility(View.GONE);
                                activityAddAcademyGalleryBinding.gvMultipleImage.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    activityAddAcademyGalleryBinding.gvMultipleImage.setAdapter(galleryAdapter);
                } else {
                    activityAddAcademyGalleryBinding.hsGallery.setVisibility(View.VISIBLE);
                    activityAddAcademyGalleryBinding.gvMultipleImage.setVisibility(View.GONE);
                }

            }
    );

    //TODO select video from gallery of device
    ActivityResultLauncher<Intent> startActivityIntentVideoFromGallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent intent = result.getData();
                    assert intent != null;
                    //selectedVideoPath = result.getData().getData().toString();

                    selectedVideoPath = getPath(mContext, result.getData().getData());

                    activityAddAcademyGalleryBinding.vvVideo.setVideoURI(intent.getData());
                    activityAddAcademyGalleryBinding.vvVideo.start();
                }
            });

    // TODO capture video using camera
    private final ActivityResultLauncher<Intent> cameraLauncherVideo = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    assert result.getData() != null;
                    selectedVideoPath = result.getData().getData().toString();
                    activityAddAcademyGalleryBinding.vvVideo.setVideoURI(result.getData().getData());
                    activityAddAcademyGalleryBinding.vvVideo.start();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddAcademyGalleryBinding = ActivityAddAcademyGalleryBinding.inflate(getLayoutInflater());
        setContentView(activityAddAcademyGalleryBinding.getRoot());

        mContext = this;
        mActivity = this;

        toolbarInnerpageBinding = activityAddAcademyGalleryBinding.toolbar;
        toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.add_academy_gallery));
        toolbarInnerpageBinding.imgBack.setOnClickListener(v -> finish());

        loaderView = CustomLoaderView.initialize(mContext);
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        queue = Volley.newRequestQueue(mContext);

        //TODO ask for permission of camera upon first launch of application
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, 112);
            }
        }


        inItView();

    }

    //TODO initialize view in this method
    private void inItView() {
        activityAddAcademyGalleryBinding.btnSelectSingleImage.setOnClickListener(v -> openCameraAndGalleryDialog("Single"));
        activityAddAcademyGalleryBinding.btnSelectMultipleImage.setOnClickListener(v -> openCameraAndGalleryDialog("Multiple"));
        activityAddAcademyGalleryBinding.btnSelectMultipleVideo.setOnClickListener(v -> openCameraAndGalleryDialog("Video"));
        activityAddAcademyGalleryBinding.profilePic.setOnClickListener(v -> {
            openCameraAndGalleryDialog("Profile");
        });

        activityAddAcademyGalleryBinding.gvMultipleImage.setLayoutManager(new GridLayoutManager(mContext, 1, LinearLayoutManager.HORIZONTAL, false));
        activityAddAcademyGalleryBinding.gvMultipleImage.addItemDecoration(new HorizontalSpaceItemDecoration(25));


        if(getIntent()!=null){

            FROM = getIntent().getStringExtra("FROM");
            if(FROM.equalsIgnoreCase("2")){
                toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.Update_Academy_Gallery));
                mArrayUri = (ArrayList<AcademyGallery>) getIntent().getSerializableExtra("Image");
                if (mArrayUri.size() > 0) {
                    activityAddAcademyGalleryBinding.hsGallery.setVisibility(View.GONE);
                    activityAddAcademyGalleryBinding.gvMultipleImage.setVisibility(View.VISIBLE);
                    galleryAdapter = new AcademyGalleryMultipleImageAdapter(mContext, mArrayUri, new AcademyGalleryMultipleImageAdapter.ItemListener() {
                        @Override
                        public void onItemClickk(int size,String _galleryId) {

                            if (Global.isOnline(mContext)) {
                                deleteGallery(_galleryId);
                            } else {
                                Global.showDialog(mActivity);
                            }
                            if (mArrayUri.size() == 0) {
                                activityAddAcademyGalleryBinding.hsGallery.setVisibility(View.VISIBLE);
                                activityAddAcademyGalleryBinding.gvMultipleImage.setVisibility(View.GONE);
                            } else {
                                activityAddAcademyGalleryBinding.hsGallery.setVisibility(View.GONE);
                                activityAddAcademyGalleryBinding.gvMultipleImage.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    activityAddAcademyGalleryBinding.gvMultipleImage.setAdapter(galleryAdapter);
                } else {
                    activityAddAcademyGalleryBinding.hsGallery.setVisibility(View.VISIBLE);
                    activityAddAcademyGalleryBinding.gvMultipleImage.setVisibility(View.GONE);
                }
                profileImagePath = getIntent().getStringExtra("Logo");
                Glide.with(mActivity).load(profileImagePath).into(activityAddAcademyGalleryBinding.profilePic);
                imagePathBanner= getIntent().getStringExtra("Banner");
                Glide.with(mActivity).load(imagePathBanner).into(activityAddAcademyGalleryBinding.coverImg);
                selectedVideoPath = getIntent().getStringExtra("Video");


                if(!selectedVideoPath.isEmpty()){
                    MediaController mediaController = new MediaController(this);
                    mediaController.setAnchorView(activityAddAcademyGalleryBinding.vvVideo);
                    activityAddAcademyGalleryBinding.vvVideo.setMediaController(mediaController);
                    activityAddAcademyGalleryBinding.vvVideo.setVideoPath(selectedVideoPath);
                    activityAddAcademyGalleryBinding.vvVideo.start();
                }
            }else{
                toolbarInnerpageBinding.toolbartext.setText(mContext.getResources().getString(R.string.add_academy_gallery));
            }


        }


        activityAddAcademyGalleryBinding.flSubmit.setOnClickListener(v -> {

            if (profileImagePath.isEmpty()) {
                Toaster.customToast("Logo is required");
            } else if (imagePathBanner.isEmpty()) {
                Toaster.customToast("Upload valid banner image");
            }
            else if(mArrayUri.size()==0){
                Toaster.customToast("Upload Gallery image");
            }
            else {
                if (Global.isOnline(mContext)) {
                    getAddGalleryResponse();
                } else {
                    Global.showDialog(mActivity);
                }
            }

        });
    }


    //TODO open camera and gallery using this bottomSliderDialog
    public void openCameraAndGalleryDialog(String selectedPostType) {

        final BottomSheetDialog dialog = new BottomSheetDialog(mActivity, R.style.BottomSheetDialogTheme);
        dialog.setContentView(R.layout.dialog_camera_selector);

        TextView tv_choose = dialog.findViewById(R.id.tv_choose);
        TextView tv_camera = dialog.findViewById(R.id.tv_camera);
        TextView tvGallery = dialog.findViewById(R.id.tvGallery);
        TextView tvCancel = dialog.findViewById(R.id.tvCancel);

        if (selectedPostType.equalsIgnoreCase("Single")) {

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
        } else if (selectedPostType.equalsIgnoreCase("Multiple")) {
            tv_camera.setVisibility(View.GONE);

            Objects.requireNonNull(tvGallery).setOnClickListener(v -> {
                dialog.dismiss();
                mArrayUri.clear();
                multipleGallery();
            });
            Objects.requireNonNull(tvCancel).setOnClickListener(v -> {
                dialog.dismiss();
            });
        } else if (selectedPostType.equalsIgnoreCase("Video")) {
            tv_camera.setOnClickListener(v -> {
                dialog.dismiss();

                launchVideoCamera();
            });
            tvGallery.setOnClickListener(v -> {
                dialog.dismiss();
                openVideoFromGallery();
            });
            Objects.requireNonNull(tvCancel).setOnClickListener(v -> {
                dialog.dismiss();
            });
        } else if (selectedPostType.equalsIgnoreCase("Profile")) {
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
        photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        cameraActivityResultLauncher.launch(cameraIntent);
    }

    //TODO takes single image from gallery
    private void openGallerySingleImage() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryActivityResultLauncher.launch(galleryIntent);
    }


    //TODO takes URI of the image and returns bitmap
    private Bitmap uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(selectedFileUri, "r");
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
        Cursor cur = getContentResolver().query(photoUri, orientationColumn, null, null, null);
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

    //TODO select multiple image from gallery
    public void multipleGallery() {
        galleryMultipleLauncher.launch("image/*");
    }

    //TODO takes video from gallery
    private void openVideoFromGallery() {

        Intent intent = new Intent();
        intent.setType("video/mp4*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityIntentVideoFromGallery.launch(intent);
    }

    //TODO takes video recording using camera
    private void launchVideoCamera() {
        Intent chooserIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        cameraLauncherVideo.launch(chooserIntent);
    }

    //TODO takes profile image from camera
    private void openCameraProfileImage() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        cameraProfileImageActivityResultLauncher.launch(cameraIntent);
    }

    //TODO takes profile image from gallery
    private void openGalleryProfileImage() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryProfileImageActivityResultLauncher.launch(galleryIntent);
    }


    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    public void getAddGalleryResponse() {
        try {
            loaderView.showLoader();

            Log.d("Total",mArrayUri.size()+"?"+profileImagePath+"/"+imagePathBanner);

            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            Timber.e("Chuncked %b", entity.isChunked());
            entity.addPart("user_id", new StringBody(SessionManager.get_user_id(prefs)));
            entity.addPart("s", new StringBody(SessionManager.get_session_id(prefs)));
            entity.addPart("academy_id", new StringBody(SessionManager.get_academyId(prefs)));

            if(mArrayUri.size()==0){

            }else{

                for (int j = 0; j < mArrayUri.size(); j++) {
                    File filee = new File(mArrayUri.get(j).getFiles().toString());
                    FileBody fileBodyy = new FileBody(filee);
                    entity.addPart("gallery_images[" + (j) + "]", fileBodyy);
                }
            }
            if (!(profileImagePath.equals("") || profileImagePath == null)) {
                File file_logo = new File(profileImagePath);
                FileBody fileBody_logo = new FileBody(file_logo);
                entity.addPart("logo", fileBody_logo);
            }
            if (!imagePathBanner.isEmpty()) {
                File file_new = new File(imagePathBanner);
                FileBody fileBody_new = new FileBody(file_new);
                entity.addPart("banner_image", fileBody_new);
            }

            if (!selectedVideoPath.isEmpty()) {
                File file = new File(selectedVideoPath);
                FileBody fileBody = new FileBody(file);
                entity.addPart("gallery_video", fileBody);
            }


            MultipartRequest req = new MultipartRequest(Global.URL + Global.ACADEMY_ADD_GALLERY,
                    response -> {
                        try {
                            loaderView.hideLoader();
                            Timber.e(response);
                            JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {
//                            JSONArray array = jsonObject.getJSONArray("posts");
                                //Global.msgDialog(mActivity, jsonObject.optString("message"));
                                Toaster.customToast(jsonObject.getString("message"));

                                new Handler().postDelayed((Runnable) () -> {

//                                        if(FROM.equalsIgnoreCase("2")){
//                                            startActivity(new Intent(mActivity, ManageAcademyDashboardActivity.class));
//                                            finish();
//                                        }else{
//                                            startActivity(new Intent(mActivity, MainActivity.class));
//                                            finish();
//                                        }


                                }, 2000);

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(mActivity, jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
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

    private void deleteGallery(String _galleryId) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.DELETE_ACADEMY_GALLERY, response -> {
            Log.d("ResponseDeleteGallery", response);

            try {
                loaderView.hideLoader();
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                    Toaster.customToast(jsonObject.getString("message"));
                    //new Handler().postDelayed((Runnable) this::finish, 100);
                } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                    Global.msgDialog(mActivity, jsonObject.getJSONObject("errors").getString("error_text"));
                } else {
                    Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
            Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("academy_id", SessionManager.get_academyId(prefs));
                param.put("gallery_id", _galleryId);
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

