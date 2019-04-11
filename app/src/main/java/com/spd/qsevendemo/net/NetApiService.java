package com.spd.qsevendemo.net;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface NetApiService {

    /**
     * 检测更新
     *
     * @return @return {@link UpdateResult}
     */
    @POST(Urls.UPDATE2)
    @FormUrlEncoded
    Observable<UpdateResult> checkUpdate2(@FieldMap Map<String, String> versionNumber);
}
