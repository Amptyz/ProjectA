package com.example.uidesign.ui.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;
import android.util.Patterns;

import com.example.Util.okHttpUtil;
import com.example.uidesign.data.LoginRepository;
import com.example.uidesign.data.Result;
import com.example.uidesign.data.model.LoggedInUser;
import com.example.uidesign.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import com.example.Util.okHttpUtil;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;//存储用户信息

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) throws IOException, InterruptedException {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(username, password);

        final String[] code = new String[1];

        
        String netUrl = "http://47.103.113.75:8080/sysUser/login";
        JSONObject user=new JSONObject();

        try {
            user.put("userName", username);
            user.put("password", password);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client=new OkHttpClient();
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), user.toString());

                Request request = new Request.Builder().url(netUrl).post(body).build();
                Call call = client.newCall(request);
                try {
                    Response response = call.execute();
                    System.out.println("adada");
                    System.out.println(response);
                    String resStr=response.body().string();
                    System.out.println(resStr);

                    JSONObject obj = new JSONObject(resStr);


                    String CodeStr = obj.getString("code");


                    String dataStr = obj.getString("data");

                    JSONObject dataobj = new JSONObject(dataStr);

                    if(CodeStr.equals("200")) {
                        String tokenn = dataobj.getString("token");
                        String uName = dataobj.getString("userName");
                        loginRepository.setToken(tokenn);
                        loginRepository.setUserName(uName);
                    }
                    Log.i("code", CodeStr);
                    code[0] =CodeStr;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        t.start();
        t.join();

        System.out.println(code[0]);
        if (code[0].equals("200")) {
            Log.i("sdsd", "sdfsdfdg!!!");
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));

        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }



    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    public LoginRepository getLoginRepository(){
        return loginRepository;
    }
}