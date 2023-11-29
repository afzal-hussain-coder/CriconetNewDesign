package com.pb.criconetnewdesign.IntroSlider.IntroFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pb.criconetnewdesign.R;

import java.util.HashMap;


public class IntroThirdFragment extends Fragment {
    private HashMap _$_findViewCache;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_intro_third, container, false);
    }

    public View _$_findCachedViewById(int var1) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }

        View var2 = (View)this._$_findViewCache.get(var1);
        if (var2 == null) {
            View var10000 = this.getView();
            if (var10000 == null) {
                return null;
            }

            var2 = var10000.findViewById(var1);
            this._$_findViewCache.put(var1, var2);
        }

        return var2;
    }

    public void _$_clearFindViewByIdCache() {
        if (this._$_findViewCache != null) {
            this._$_findViewCache.clear();
        }

    }

    // $FF: synthetic method
    public void onDestroyView() {
        super.onDestroyView();
        this._$_clearFindViewByIdCache();
    }
}