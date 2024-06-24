package com.pb.criconetnewdesign.Fragment.CoachFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.databinding.FragmentCoachPersonalInformationBinding;
import com.pb.criconetnewdesign.util.BookingHistoryDropDown;
import com.pb.criconetnewdesign.util.DataModel;
import com.pb.criconetnewdesign.util.LanguageDropDown;

import java.util.ArrayList;


public class CoachPersonalInformationFragment extends Fragment {

    FragmentCoachPersonalInformationBinding fragmentCoachPersonalInformationBinding;
    private ArrayList<DataModel> option_city = new ArrayList<>();
    private ArrayList<DataModel> option_state = new ArrayList<>();
    private ArrayList<DataModel> option_country = new ArrayList<>();
    private ArrayList<DataModel> option_language = new ArrayList<>();

    public CoachPersonalInformationFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentCoachPersonalInformationBinding = FragmentCoachPersonalInformationBinding.inflate(inflater, container, false);
        return fragmentCoachPersonalInformationBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //todo city component
        option_city.add(new DataModel("Delhi"));
        option_city.add(new DataModel("Gorakhpur"));
        option_city.add(new DataModel("Patna"));
        fragmentCoachPersonalInformationBinding.dropCity.setOptionList(option_city);
        fragmentCoachPersonalInformationBinding.dropCity.setClickListener(new BookingHistoryDropDown.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name,String id) {


            }


            @Override
            public void onDismiss() {
            }
        });


        //todo state component
        option_state.add(new DataModel("Bihar"));
        option_state.add(new DataModel("Punjab"));
        option_state.add(new DataModel("West Bengal"));
        fragmentCoachPersonalInformationBinding.dropState.setOptionList(option_state);
        fragmentCoachPersonalInformationBinding.dropState.setClickListener(new BookingHistoryDropDown.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name,String id) {


            }


            @Override
            public void onDismiss() {
            }
        });


        //todo country component
        option_country.add(new DataModel("India"));
        option_country.add(new DataModel("Japan"));
        option_country.add(new DataModel("Dubai"));
        fragmentCoachPersonalInformationBinding.dropCountry.setOptionList(option_country);
        fragmentCoachPersonalInformationBinding.dropCountry.setClickListener(new BookingHistoryDropDown.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name,String id) {


            }


            @Override
            public void onDismiss() {
            }
        });

        //todo language component
        option_language.add(new DataModel("Hindi"));
        option_language.add(new DataModel("English"));
        option_language.add(new DataModel("Arabic"));
        fragmentCoachPersonalInformationBinding.dropLanguage.setOptionList(option_language);
        fragmentCoachPersonalInformationBinding.dropLanguage.setClickListener(new LanguageDropDown.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name) {


            }


            @Override
            public void onDismiss() {
            }
        });


        //todo firstName component
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

        //todo MiddleName component
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

        //todo LastName component
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

        //todo Address1 component
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

        //todo Address2 component
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

        //todo Pincode component
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

        //todo Facebook component
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

        //todo Twitter component
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

        //todo Instagram component
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

        //todo LinkedIn component
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

        fragmentCoachPersonalInformationBinding.flSave.setOnClickListener(v -> {

        });


    }
}