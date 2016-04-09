package com.example.themesanasang.scandocnth;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenFragment extends Fragment {

    private String userid;

    public static ScreenFragment newInstance(String userid) {
        ScreenFragment fragment = new ScreenFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userid", userid);
        fragment.setArguments(bundle);
        return fragment;
    }

    public ScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_screen, container, false);
    }

}
