package com.example.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.AudioAdapter;
import com.example.MainActivity;
import com.example.Util.File.MyFile;
import com.example.Util.VerticalDividerItemDecoration;
import com.example.uidesign.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AudioFragment extends Fragment {

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

        // 初始化 RecyclerView
        audioRecycleView = view.findViewById(R.id.audioRecyclerView);

        audioRecycleView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        audioRecycleView.setLayoutManager(layoutManager);

        adapter = new AudioAdapter(getActivity(),myFileList);

        adapter.SetOnItemClickListener(new AudioAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View veiw, int position) {
                Toast.makeText(getActivity(), "点击了第"+(position+1)+"条", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(getActivity(), "长按了第"+(position+1)+"条", Toast.LENGTH_SHORT).show();
            }
        });

        audioRecycleView.setAdapter(adapter);
        audioRecycleView.addItemDecoration(new VerticalDividerItemDecoration(getContext()));
        initMyFile(getContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC));

//        // 初始化适配器
//        orderAdapter = new OrderAdapter(new ArrayList<>());
//
//        // 设置 RecyclerView 的布局管理器
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        audioRecycleView.setLayoutManager(layoutManager);
//
//        // 设置 RecyclerView 的适配器
//        audioRecycleView.setAdapter(orderAdapter);

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
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.viewPager,new UploadFragment(),null)
                .addToBackStack(null)
                .commit();
    }
}
