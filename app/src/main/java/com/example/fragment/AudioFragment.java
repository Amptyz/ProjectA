package com.example.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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
import com.example.SummaryActivity;
import com.example.Util.File.MyFile;
import com.example.Util.VerticalDividerItemDecoration;
import com.example.network.APIService;
import com.example.uidesign.R;
import com.example.uidesign.data.model.ClassRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AudioFragment extends Fragment {

    MainViewModel mainViewModel;
    //recyclerView
    private RecyclerView audioRecycleView;
    private AudioAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ClassRecord> myRecordList = new ArrayList<>();
    private List<ClassRecord> myRecordList2 = new ArrayList<>();
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

        adapter = new AudioAdapter(getActivity(),myRecordList);

        adapter.SetOnItemClickListener(new AudioAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {

                Toast.makeText(getActivity(), "点击了第"+(position+1)+"条", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(getActivity(), "长按了第"+(position+1)+"条", Toast.LENGTH_SHORT).show();
            }
        });
        audioRecycleView.setAdapter(adapter);
        initRecord();



        return view;
    }




    //获取文件列表，并提醒adpater
    @SuppressLint("NotifyDataSetChanged")
    public void initRecord() {
        myRecordList2.clear();
        postRecords();
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onResume() {

        super.onResume();


    }

    public void postRecords(){
        Callback summaryCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("FailSummary", "FailSummary!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseData = response.body().string();

                try {

                    JSONObject obj = new JSONObject(responseData);
                    JSONArray dataArray = obj.getJSONArray("data");
                    for(int i=0;i<3;i++) {
                        String summary = dataArray.getJSONObject(i).getString("summary");
                        String oringinText = dataArray.getJSONObject(i).getString("txt");
                        myRecordList2.add(new ClassRecord(i,"新课程",oringinText,summary,"2024-01-05"));
                        Log.i("summary", summary);
                    }
                    myRecordList.clear();
                    for(int i=0;i<3;i++) {
                        myRecordList.add(myRecordList2.get(i));
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        };

        String token = mainViewModel.getToken();
        try {
            APIService.getAllRecords(token,summaryCallback);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

}
