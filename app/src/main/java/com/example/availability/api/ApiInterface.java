package com.example.availability.api;
import static com.example.availability.constants.CONTENT_TYPE;
import static com.example.availability.constants.SERVER_KEY;

import com.example.availability.model.pushnotifcation;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {
    @Headers({"Authorization:key="+SERVER_KEY,"Content-Type:"+CONTENT_TYPE})
    @POST("fcm/send")
    Call<pushnotifcation>sendNotification(@Body pushnotifcation notifcation);

}
