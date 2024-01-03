package com.example.Util;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public class okHttpUtil {

    private static final OkHttpClient client;

    private okHttpUtil(){throw new UnsupportedOperationException("静态工具类不允许被实例化");}

    static {
        client=new OkHttpClient();
    }

    public static Response Post(String url, JSONObject json) throws InterruptedException {

        final Response[] response = new Response[1];
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

                Request request = new Request.Builder().url(url).post(body).build();
                Call call = client.newCall(request);
                try {
                    response[0] = call.execute();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        t.start();
        t.join();
        return response[0];
    }

}
