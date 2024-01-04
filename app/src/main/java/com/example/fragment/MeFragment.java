package com.example.fragment;

import static com.example.fragment.HomeFragment.ARG_PARAM1;
import static com.example.fragment.HomeFragment.ARG_PARAM2;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.Data.MainViewModel;
import com.example.uidesign.R;

public class MeFragment extends Fragment {
    MainViewModel mainViewModel;
    private Button button1;
    private Button button2;
    private Button button3;

    private TextView displayName;

    public MeFragment() {
        // Required empty public constructor
    }

    public static MeFragment newInstance(String param1, String param2) {
        MeFragment fragment = new MeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        // 获取按钮引用
        button1 = view.findViewById(R.id.button1);
        button2 = view.findViewById(R.id.button2);
        button3 = view.findViewById(R.id.button3);
        displayName = view.findViewById(R.id.displayName);

        displayName.setText(mainViewModel.getUserName());
        // 为按钮设置点击事件监听器
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动Activity1
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动Activity2

            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动Activity3

            }
        });

        return view;
    }
}
