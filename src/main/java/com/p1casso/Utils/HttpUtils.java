package com.p1casso.Utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class HttpUtils {
    private static final OkHttpClient OKHTTP_CLIENT = new OkHttpClient.Builder().connectTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS).writeTimeout(120, TimeUnit.SECONDS).build();


    /**
     * 发送GET请求
     *
     * @param url URL地址
     * @return
     */
    public static String Get(String url) {
        Request request = new Request.Builder().url(url).build();
        Response response;
        try {
            response = OKHTTP_CLIENT.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通用的请求方法
     *
     * @param url    请求地址
     * @param header 请求头
     * @return 请求的返回值
     */
    public static String okHttpGet(String url, Map<String, String> header) {
        // 添加头部信息
        Request.Builder builder = new Request.Builder().url(url);
        if (header != null && !header.isEmpty()) {
            header.forEach(builder::addHeader);
        }
        // 发送请求
        Request request = builder.get().build();
        try {
            Response response = OKHTTP_CLIENT.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            throw new RuntimeException("HTTP URL同步请求失败 URL:" + url, e);
        }
    }

    /**
     * 通用的请求方法
     *
     * @param url    请求地址
     * @param header 请求头
     * @return 请求的返回值
     */
    public static ResponseBody okHttpPost(String url, Map<String, String> header, FormBody.Builder body) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(url)
                .post(body.build());

        if (header != null) {
            header.forEach(builder::addHeader);
        }

        Request request = builder.build();
        Response response;
        response = client.newCall(request).execute();
        return response.body();
    }

}
