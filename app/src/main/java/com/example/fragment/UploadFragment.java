package com.example.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.Data.MainViewModel;

public class UploadFragment extends Fragment {

    MainViewModel mainViewModel;

    private String audioFilePath;
    private String fileName;
    public UploadFragment() {
        // Required empty public constructor
    }

    public static UploadFragment newInstance() {
        return new UploadFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return null;
    }

    public void setAudioFilePath(String filePath){
        audioFilePath = filePath;
    }
}
