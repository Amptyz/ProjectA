package com.example.network;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class APIService {
    private static final String BASE_URL = "http://47.103.113.75:8080";


    //登录方法API
    public static JSONObject login(String username, String password)  throws IOException, InterruptedException {
        String netUrl = BASE_URL + "/sysUser/login";

        JSONObject user=new JSONObject();
        final JSONObject[] ans = {new JSONObject()};

        try {
            user.put("userName", username);
            user.put("password", password);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        System.out.println("dddddddddddddddd");
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client=new OkHttpClient();
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), user.toString());

                Request request = new Request.Builder().url(netUrl).post(body).build();
                Call call = client.newCall(request);
                try {
                    Response response = call.execute();
                    //System.out.println("adadaaaaaaaaaaaaaaaaaaaa");
                    //System.out.println(response);
                    String resStr=response.body().string();
                    System.out.println(resStr);

                    //System.out.println("---------------------------------------");
                    JSONObject obj = new JSONObject(resStr);


                    String CodeStr = obj.getString("code");
                    String dataStr = obj.getString("data");

                    JSONObject dataobj = new JSONObject(dataStr);

                    if(CodeStr.equals("200")) {
                        ans[0]=obj;

                        String tokenn = dataobj.getString("token");
                        String uName = dataobj.getString("userName");
                        //loginRepository.setToken(tokenn);
                        //loginRepository.setUserName(uName);
                    }
                    //Log.i("code", CodeStr);
                    // code[0] =CodeStr;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t.start();
        t.join();
        // System.out.println(code[0]);
        return ans[0];
    }
}












    /*
    public static JSONObject login(String username, String password)  throws IOException, InterruptedException {
        String netUrl = BASE_URL + "/sysUser/login";
        JSONObject user=new JSONObject();

        final JSONObject[] ans = {new JSONObject()};

        try {
            user.put("userName", username);
            user.put("password", password);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        System.out.println("dddddddddddddddd");
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client=new OkHttpClient();
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), user.toString());

                Request request = new Request.Builder().url(netUrl).post(body).build();
                Call call = client.newCall(request);
                try {
                    Response response = call.execute();
                    System.out.println("adadaaaaaaaaaaaaaaaaaaaa");
                    System.out.println(response);
                    String resStr=response.body().string();
                    System.out.println(resStr);

                    System.out.println("---------------------------------------");
                    JSONObject obj = new JSONObject(resStr);


                    String CodeStr = obj.getString("code");
                    String dataStr = obj.getString("data");

                    JSONObject dataobj = new JSONObject(dataStr);

                    if(CodeStr.equals("200")) {
                        ans[0]=obj;

                        String tokenn = dataobj.getString("token");
                        String uName = dataobj.getString("userName");
                        //loginRepository.setToken(tokenn);
                        //loginRepository.setUserName(uName);
                    }
                    //Log.i("code", CodeStr);
                   // code[0] =CodeStr;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
       // System.out.println(code[0]);
        return ans[0];
    }
}
/*

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

* */


