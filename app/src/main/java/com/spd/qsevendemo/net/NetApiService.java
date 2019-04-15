package com.spd.qsevendemo.net;

import com.spd.qsevendemo.bean.BalanceResult;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NetApiService {

    /**
     * 上传
     *
     * @param balanceBean 上传的json字符串
     * @return 上传结果
     */
    @POST(Urls.UPLOAD)
    Observable<BalanceResult> upload(@Body RequestBody balanceBean);
}
