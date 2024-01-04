package com.example.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.AudioAdapter;
import com.example.Data.MainViewModel;
import com.example.MainActivity;
import com.example.Util.File.MyFile;
import com.example.Util.VerticalDividerItemDecoration;
import com.example.uidesign.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AudioFragment extends Fragment {

    MainViewModel mainViewModel;
    //recyclerView
    private RecyclerView audioRecycleView;
    private AudioAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<MyFile> myFileList = new ArrayList<>();

   // private OrderAdapter orderAdapter;

    public AudioFragment() {
        // Required empty public constructor
    }

    public static AudioFragment newInstance() {
        return new AudioFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio, container, false);

        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        // 初始化 RecyclerView
        audioRecycleView = view.findViewById(R.id.audioRecyclerView);

        audioRecycleView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        audioRecycleView.setLayoutManager(layoutManager);

        adapter = new AudioAdapter(getActivity(),myFileList);

        adapter.SetOnItemClickListener(new AudioAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {

                Toast.makeText(getActivity(), "点击了第"+(position+1)+"条", Toast.LENGTH_SHORT).show();

                mainViewModel.currentFile = myFileList.get(position).getFileName();
                toUploadFragment();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(getActivity(), "长按了第"+(position+1)+"条", Toast.LENGTH_SHORT).show();
            }
        });
        audioRecycleView.setAdapter(adapter);
        initMyFile(getContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC));



        return view;
    }

    //获取文件列表，并提醒adpater
    private void initMyFile(File externalStorageDirectory) {
        myFileList.clear();
        List<File> files = new ArrayList<>();
        Collections.addAll(files, externalStorageDirectory.listFiles());
        for (File file : files) {
            String fileName = file.getName();
            MyFile myFile = new MyFile(fileName);
            myFileList.add(myFile);
        }
        adapter.notifyDataSetChanged();
    }

    public void toUploadFragment(){
        MainActivity mainActivity=(MainActivity)getActivity();

        mainActivity.setFragmentUpload();
    }
}
