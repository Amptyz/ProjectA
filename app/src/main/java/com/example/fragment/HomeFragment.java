package com.example.fragment;

import static androidx.core.content.PermissionChecker.checkSelfPermission;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.util.Log;
import android.widget.ImageButton;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Data.MainViewModel;
import com.example.Util.Record.Recorder;
import com.example.receiver.RecordReceiver;
import com.example.network.APIService;
import com.example.receiver.RecordReceiver;
import com.example.service.RecordingService;
import com.example.uidesign.R;
import com.example.uidesign.ui.login.LoginViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    Callback getRecordsCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.i("FailCall", "Failure!");
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {

            String responseData = response.body().string();

            try {
                JSONObject obj = new JSONObject(responseData);
                String summary = obj.getString("result");
                Log.i("Summary", summary);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }



        }
    };
    //VM
    MainViewModel mainViewModel;
    private RecordReceiver recordReceiver = new RecordReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String recordPath = intent.getStringExtra("recordPath");
            Toast.makeText(getContext(), "成功获得广播！" + recordPath, Toast.LENGTH_SHORT).show();
            Log.i("broadcast", recordPath);

            Callback uploadCallback = new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("FailCall", "Failure!");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String responseData = response.body().string();
                    try {
                        JSONObject obj = new JSONObject(responseData);
                        String code = obj.getString("code");
                        Log.i("upLoad", code);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            };
            Callback endCallback = new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("FailCall", "Failure!");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String responseData = response.body().string();
                    try {
                        JSONObject obj = new JSONObject(responseData);
                        String code = obj.getString("code");
                        Log.i("END", code);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            };


            String token = mainViewModel.getToken();
            File file = new File(recordPath);
            try {

                APIService.addRecord(token, file, uploadCallback);
                APIService.endClass(token,endCallback);
                APIService.getAllRecords(token,getRecordsCallback);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }
    };
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

    private ImageButton btnRecord;
    private ImageButton btnClass;
    private TextView textRecord;
    private TextView textClass;

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
        //注册动态广播

        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.example.receiver.RecordReceiver");
        getActivity().getApplicationContext().registerReceiver(recordReceiver, intentFilter);


        //获取mainViewModel
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        Recorder rec = new Recorder(getContext());
        mainViewModel.setRecorder(rec);
        mainViewModel.isRecordBtnActive = false;
        mainViewModel.isClassStarted = false;

        btnRecord = view.findViewById(R.id.btnRecord);
        btnClass = view.findViewById(R.id.btnReceive);
        textRecord = view.findViewById(R.id.textView0);
        textClass = view.findViewById(R.id.textView1);

        setRecordButtonCallback();
        setClassButtonCallback();

        disableRecord();

        return view;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        boolean hasPermissionDismiss = false;//有权限没有通过
        if(requestCode == 1){


            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                mainViewModel.start();
                showPermissionOnToast();
                try {
                    startRecord();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
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

    private void startRecord() throws JSONException {
        //发送startclass
        Callback startCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("FailCall", "Failure!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseData = response.body().string();

                Log.i("Start", "start");

            }
        };
        String token = mainViewModel.getToken();

        APIService.startClass(token, startCallback);


        Intent intent = new Intent(requireActivity(), RecordingService.class);
        intent.putExtra("baseFilePath", Objects.requireNonNull(requireActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC)).getAbsolutePath());
        requireActivity().startService(intent);
    }

    private void stopRecord() {
        Intent intent = new Intent(requireActivity(), RecordingService.class);
        intent.putExtra("baseFilePath", Objects.requireNonNull(requireActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC)).getAbsolutePath());
        requireActivity().stopService(intent);
    }

    private void enableRecord() {
        btnRecord.setEnabled(true);
        btnRecord.setAlpha(1f);
        textRecord.setAlpha(1f);
    }

    private void disableRecord() {
        btnRecord.setEnabled(false);
        btnRecord.setAlpha(0.2f);
        textRecord.setAlpha(0.2f);
    }

    private void setRecordButtonCallback() {
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mainViewModel.isRecordBtnActive) {
                    int flag = checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO);
                    if (flag != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(mPermissions, 1);
                    } else {
                        showPermissionOnToast();
                        try {
                            startRecord();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    textRecord.setText("停止挂机");
                }else{
                    stopRecord();
                    textRecord.setText("开始挂机");
                }
                mainViewModel.isRecordBtnActive = !mainViewModel.isRecordBtnActive;
                btnRecord.setActivated(mainViewModel.isRecordBtnActive);
            }
        });
    }

    private void setClassButtonCallback() {
        btnClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mainViewModel.isClassStarted) {
                    mainViewModel.isClassStarted = true;
                    enableRecord();
                    textClass.setText("结束课程");
                } else {
                    mainViewModel.isClassStarted = false;
                    if (mainViewModel.isRecordBtnActive) {
                        btnRecord.callOnClick();
                    }
                    disableRecord();
                    textClass.setText("创建新课程");
                }
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getApplicationContext().unregisterReceiver(recordReceiver);
    }
}
