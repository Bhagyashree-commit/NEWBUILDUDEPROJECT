package com.blucorsys.app.labourcontractorapp.Labour;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blucorsys.app.labourcontractorapp.R;


public class ApplyJobFragment extends Fragment {


    public ApplyJobFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_apply_job, container, false);
        return v;
    }
}