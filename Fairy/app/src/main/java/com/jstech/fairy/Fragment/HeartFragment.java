package com.jstech.fairy.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jstech.fairy.R;

/**
 * Created by SONY on 2017-09-25.
 */

public class HeartFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";   //  Position값 받아올 구분자
    public static final int POSITION_SETTING = 2;
    private int mPage;

    //  Constructor
    public HeartFragment(){

    }

    //  Constructor
    @SuppressLint("ValidFragment")
    public HeartFragment(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        this.setArguments(args);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        if(mPage == POSITION_SETTING){
            view = inflater.inflate(R.layout.fragment_heart, container, false);
        }

        return view;
    }

}
