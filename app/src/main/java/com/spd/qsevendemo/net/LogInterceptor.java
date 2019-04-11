package com.spd.qsevendemo.net;

import android.support.annotation.NonNull;

import com.spd.qsevendemo.utils.Logcat;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author :Reginer in  2017/9/11 17:51.
 *         联系方式:QQ:282921012
 *         功能描述:日志
 */
public class LogInterceptor implements Interceptor {
    @SuppressWarnings("ConstantConditions")
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Logcat.d("request:" + request.toString());
        Response response = chain.proceed(chain.request());
        MediaType mediaType = response.body().contentType();
        String content = response.body().string();
        Logcat.d("response :" + content);
        return response.newBuilder().body(ResponseBody.create(mediaType, content)).build();
    }
}

