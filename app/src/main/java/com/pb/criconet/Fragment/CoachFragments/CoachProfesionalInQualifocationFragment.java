package com.pb.criconet.Fragment.CoachFragments;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.pb.criconet.CustomeCamera.CustomeCameraActivity;
import com.pb.criconet.adapter.EcoachingAdapter.CoachSpecialitiesListAdapter;
import com.pb.criconet.databinding.FragmentCoachProfessionalInformationBinding;
import com.pb.criconet.model.Coaching.CoachSpecialitiesModel;
import com.pb.criconet.util.Global;

import java.util.List;

public class CoachProfesionalInQualifocationFragment extends Fragment {

    FragmentCoachProfessionalInformationBinding fragmentCoachProfessionalInformationBinding;
    public static Uri image_uri = null;
    private CoachSpecialitiesModel modelArrayList;
    private RequestQueue queue;
    private StringBuilder categoryId;


    public CoachProfesionalInQualifocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentCoachProfessionalInformationBinding = FragmentCoachProfessionalInformationBinding.inflate(inflater, container, false);
        return fragmentCoachProfessionalInformationBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        queue = Volley.newRequestQueue(requireContext());
        inItView();

        if (Global.isOnline(requireContext())) {
            getSpecialities();
        } else {
            Global.showDialog(requireActivity());
        }
    }

    private void inItView() {


        fragmentCoachProfessionalInformationBinding.tvCertificate.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), CustomeCameraActivity.class)
                    .putExtra("FROM", "CoachProfesionalInQualifocationFragment"));
        });

        fragmentCoachProfessionalInformationBinding.tvRemoveCertificate.setOnClickListener(v -> {
            image_uri = null;
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


        //todo Achievement component
        fragmentCoachProfessionalInformationBinding.editTextExpYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                fragmentCoachProfessionalInformationBinding.filledTextFieldExpYear.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    fragmentCoachProfessionalInformationBinding.filledTextFieldExpYear.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                fragmentCoachProfessionalInformationBinding.filledTextFieldExpYear.setErrorEnabled(false);
            }
        });


        //todo Achievement component
        fragmentCoachProfessionalInformationBinding.editTextExpMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                fragmentCoachProfessionalInformationBinding.filledTextFieldExpMonth.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    fragmentCoachProfessionalInformationBinding.filledTextFieldExpMonth.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                fragmentCoachProfessionalInformationBinding.filledTextFieldExpMonth.setErrorEnabled(false);
            }
        });


        //todo Achievement component
        fragmentCoachProfessionalInformationBinding.EditTextSkillsStudentsLearn.addTextChangedListener(new TextWatcher() {
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
        fragmentCoachProfessionalInformationBinding.EditTextOtherInfo.addTextChangedListener(new TextWatcher() {
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

        fragmentCoachProfessionalInformationBinding.flSave.setOnClickListener(v -> {

        });

        fragmentCoachProfessionalInformationBinding.rvSpecilities.setHasFixedSize(true);
        fragmentCoachProfessionalInformationBinding.rvSpecilities.setLayoutManager(new GridLayoutManager(requireContext(),3,GridLayoutManager.VERTICAL, false));

    }

    private void getSpecialities() {
        StringRequest postRequest = new StringRequest(Request.Method.GET, Global.URL + Global.GET_SPECIALITIES_CATEGORY, response -> {
            Gson gson = new Gson();
            modelArrayList = gson.fromJson(response, CoachSpecialitiesModel.class);
            if (modelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                fragmentCoachProfessionalInformationBinding.rvSpecilities.setAdapter(new CoachSpecialitiesListAdapter(requireActivity(), modelArrayList.getData(), new CoachSpecialitiesListAdapter.ItemListener() {
                    @Override
                    public void onItemClick(List<CoachSpecialitiesModel.Datum> item) {
                        categoryId = new StringBuilder();
                        String prefix = "";
                        for (CoachSpecialitiesModel.Datum itemm : modelArrayList.getData()) {
                            if (itemm.isCheck()) {
                                categoryId.append(prefix);
                                prefix = ",";
                                categoryId.append(itemm.getId());
                            }
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

    @Override
    public void onResume() {
        super.onResume();
        if (null != image_uri) {
            fragmentCoachProfessionalInformationBinding.tvCertificate.setVisibility(View.GONE);
            fragmentCoachProfessionalInformationBinding.tvRemoveCertificate.setVisibility(View.VISIBLE);
            fragmentCoachProfessionalInformationBinding.roundedIvCertificate.setVisibility(View.VISIBLE);
            fragmentCoachProfessionalInformationBinding.roundedIvCertificate.setImageURI(image_uri);
        }
    }

}