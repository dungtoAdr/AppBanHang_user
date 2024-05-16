package com.example.appbanhang1.retrofit;

import com.example.appbanhang1.model.NotiResponse;
import com.example.appbanhang1.model.NotiSendData;
import com.example.appbanhang1.model.NotiResponse;
import com.example.appbanhang1.model.NotiSendData;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiPushNofication {
    @Headers(
            {
                "Content-Type: application/json",
                    "Authorization: key=AAAAdgP-n9A:APA91bHm628RfalP5fyVV_ZHL43mQfJi1zlZvTRwSbjyUGOJe4oX2FN0OfKtg4gfP_Aw1jpaM7fhQlKC_xm4QFYDU1_SNvlVImDc4hCgxM51dbUvBa1hmDBEpv51njULUGdi4-I_ep9r"
            }
    )
    @POST("fcm/send")
    Observable<NotiResponse> sendNofitication(@Body NotiSendData data);
}
