package com.example.network;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import java.io.File;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;




/*todo  待实现的后端接口

登录接口   返回token
localhost:8080/sysUser/login
47.103.113.75:8080/sysUser/login
{
    "userName":"888",
    "password":"123456"
}     okkkkkkkkkkkkkkkkkk


//只需要传token
开始和结束课堂的标记    返回标准结果提示
47.103.113.75:8080/record/start
localhost:8080/record/start

//只需要传token
47.103.113.75:8080/record/end
localhost:8080/record/end
{
    "code": 200,
    "msg": "Success",
    "data": "结束class标记成功"
}



// 需要传token和文件   文件要传递form-data   发文件给服务器
47.103.113.75:8080/record/upload 添加记录
注意只有这个接口不传json    信息




//只需要传token   核心接口   返回值的result即为概括
47.103.113.75:8080/record/get   查询全部记录
localhost:8080/record/get
{
    "result": "课堂主题：喂，你好你好喂，关键词：\n\n概要：。",
    "totalrecord": 8,
    "username": "王"
}



*/





public class APIService {
    private static final String BASE_URL = "http://47.103.113.75:8080";


    //通用的方法   不是api     输入地址和token   获取一个json
    /*
    private static JSONObject sendPostRequest(String url, String token) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "");
        //空身体
        Request request = new Request.Builder()
                .url(url)
                .addHeader("token", token) // 修改这里，将 "Authorization" 改为 "token"
                .post(body)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();
        String resStr = response.body().string();
        return new JSONObject(resStr);
    }
    */
    //使用okhttp的异步回调函数实现   通用的简单请求函数
    private static void sendPostRequestAsync(String url, String token, Callback callback) throws JSONException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "");

        Request request = new Request.Builder()
                .url(url)
                .addHeader("token", token)
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }



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
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client=new OkHttpClient();
                RequestBody body = RequestBody.create
                        (MediaType.parse("application/json; charset=utf-8"),
                                user.toString());

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


    public static void startClass(String token, Callback callback) throws JSONException {
        String netUrl = BASE_URL + "/record/start";
        sendPostRequestAsync(netUrl, token, callback);
    }

    public static void endClass(String token, Callback callback) throws JSONException {
        String netUrl = BASE_URL + "/record/end";
        sendPostRequestAsync(netUrl, token, callback);
    }


    //传文件api    token在头    file在form-data
    public static void addRecord(String token, File file, Callback callback) throws IOException, JSONException {
        String netUrl = BASE_URL + "/record/upload";
        OkHttpClient client = new OkHttpClient();

        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();

        Request request = new Request.Builder()
                .url(netUrl)
                .addHeader("token", token)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(callback);
    }






    public static void getAllRecords(String token, Callback callback) throws JSONException {
        String netUrl = BASE_URL + "/record/get";
        sendPostRequestAsync(netUrl, token, callback);
    }



}


