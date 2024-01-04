package com.example.fragment;

import static androidx.core.content.PermissionChecker.checkSelfPermission;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.widget.ImageButton;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.Data.MainViewModel;
import com.example.Util.Record.Recorder;
import com.example.service.RecordingService;
import com.example.uidesign.R;
import com.example.uidesign.ui.login.LoginViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    //VM
    MainViewModel mainViewModel;
    LoginViewModel loginViewModel;
    //权限相关
    String[] mPermissions = new String[]{Manifest.permission.RECORD_AUDIO};

    private Dialog micDialog;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    static final String ARG_PARAM1 = "param1";
    static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        Recorder rec = new Recorder(getContext());
        mainViewModel.setRecorder(rec);
        mainViewModel.isRecordBtnActive=false;
        // 录音键
        ImageButton btnPlace = view.findViewById(R.id.btnRecord);

        btnPlace.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //initPermission();
                if(!mainViewModel.isRecordBtnActive) {
                    int flag = checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO);

                    if (flag != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(mPermissions, 1);
                    } else {

                        showPermissionOnToast();
//                        mainViewModel.start();
                        startRecord();
                    }
                }else{
                    //关闭录音
//                    mainViewModel.stop();
                    stopRecord();
                }
                mainViewModel.isRecordBtnActive = !mainViewModel.isRecordBtnActive;
                btnPlace.setActivated(mainViewModel.isRecordBtnActive);

                //注释原来的跳转
                //Intent intent = new Intent(getActivity(), OrderPlace.class);
                //startActivity(intent);
            }
        });





        ImageButton btnReceive = view.findViewById(R.id.btnReceive);
        btnReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainViewModel.play("SecondTest.wav");

//                Intent intent = new Intent(getActivity(), OrderReceive.class);
//                startActivity(intent);
            }
        });


        return view;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        boolean hasPermissionDismiss = false;//有权限没有通过
        if(requestCode == 1){


            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                mainViewModel.start();
                showPermissionOnToast();
                startRecord();
            } else {
                // Permission Denied
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }

        }
    }



    private void dismissMicDialog() {
        if (micDialog != null) {
            micDialog.dismiss();
            micDialog = null;
        }
    }

    private void showPermissionOnToast() {
        Toast.makeText(getContext(), "麦克风权限开启成功", Toast.LENGTH_SHORT).show();
    }

    private void showPermissionOffToast() {
        Toast.makeText(getContext(), "麦克风权限开启失败", Toast.LENGTH_SHORT).show();
    }

    private void startRecord() {
        Intent intent = new Intent(requireActivity(), RecordingService.class);
        requireActivity().startService(intent);
    }

    private void stopRecord() {
        Intent intent = new Intent(requireActivity(), RecordingService.class);
        requireActivity().stopService(intent);
    }

}
