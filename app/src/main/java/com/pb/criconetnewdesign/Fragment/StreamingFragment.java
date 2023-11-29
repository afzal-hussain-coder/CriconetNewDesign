package com.pb.criconetnewdesign.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.pb.criconetnewdesign.R;


public class StreamingFragment extends Fragment {



    public StreamingFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_streaming, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RelativeLayout layout_nav = view.findViewById(R.id.layout_nav);

        Animation animation_right = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_left_to_right);
        Animation animation_left = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_right_to_left);

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


    }
}