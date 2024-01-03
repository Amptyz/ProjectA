package com.example.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.OrderAdapter;
import com.example.OrderItem;
import com.example.uidesign.R;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

    private OrderAdapter orderAdapter;

    public OrderFragment() {
        // Required empty public constructor
    }

    public static OrderFragment newInstance() {
        return new OrderFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        // 初始化 RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.orderRecyclerView);

        // 初始化适配器
        orderAdapter = new OrderAdapter(new ArrayList<>());

        // 设置 RecyclerView 的布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // 设置 RecyclerView 的适配器
        recyclerView.setAdapter(orderAdapter);

        return view;
    }

    // 删除 addOrderItem 和 prepareData 方法
}
