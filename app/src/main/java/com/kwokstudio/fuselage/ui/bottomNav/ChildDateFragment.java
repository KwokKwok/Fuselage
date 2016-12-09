package com.kwokstudio.fuselage.ui.bottomNav;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kwokstudio.fuselage.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChildDateFragment extends Fragment {


    public ChildDateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.date_picker, container, false);
    }

}
