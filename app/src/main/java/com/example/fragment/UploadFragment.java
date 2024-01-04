package com.example.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.Data.MainViewModel;
import com.example.uidesign.R;

public class UploadFragment extends Fragment {

    MainViewModel mainViewModel;

    private TextView textView;
    private ImageButton btnUpload;
    private ImageButton btnPlay;
    private ImageButton btnDelete;
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
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        textView = view.findViewById(R.id.textView1);
        textView.setText(mainViewModel.currentFile);
        btnPlay = view.findViewById(R.id.btnPlay);
        btnUpload = view.findViewById(R.id.btnUpload);
        btnDelete = view.findViewById(R.id.btnDelete);
        return null;
    }

    public void setAudioFilePath(String filePath){
        audioFilePath = filePath;
    }
}
