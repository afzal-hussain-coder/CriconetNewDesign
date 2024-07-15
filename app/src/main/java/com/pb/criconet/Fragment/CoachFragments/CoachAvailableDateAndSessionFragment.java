package com.pb.criconet.Fragment.CoachFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.pb.criconet.R;
import com.pb.criconet.adapter.EcoachingAdapter.SessionTimeListAdapter;
import com.pb.criconet.databinding.FragmentCoachDateSesssionBinding;
import com.pb.criconet.databinding.FragmentCoachProfessionalInformationBinding;


public class CoachAvailableDateAndSessionFragment extends Fragment {

   FragmentCoachDateSesssionBinding fragmentCoachDateSesssionBinding;
    Long date;
    Animation slide_down,slide_up;

    public CoachAvailableDateAndSessionFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentCoachDateSesssionBinding = FragmentCoachDateSesssionBinding.inflate(inflater, container, false);
        return fragmentCoachDateSesssionBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
    }

    private void initView() {
        fragmentCoachDateSesssionBinding.rvSessionTime.setHasFixedSize(true);
        fragmentCoachDateSesssionBinding.rvSessionTime.setLayoutManager(new GridLayoutManager(requireContext(),2));
        //fragmentCoachDateSesssionBinding.rvSessionTime.setAdapter(new SessionTimeListAdapter(requireContext()));

        fragmentCoachDateSesssionBinding.calendorView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {

            date = fragmentCoachDateSesssionBinding.calendorView.getDate();
            //Toaster.customToast("Year=" + year + " Month=" + month + " Day=" + dayOfMonth);
            slide_down = AnimationUtils.loadAnimation(requireContext(),
                    R.anim.slide_down);
            fragmentCoachDateSesssionBinding.liSessionLayout.setAnimation(slide_down);
            fragmentCoachDateSesssionBinding.liSessionLayout.setVisibility(View.VISIBLE);
        });
    }
}