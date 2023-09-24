package com.example.availability.api;

import static com.example.availability.constants.BASE_URL;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Converter.*;


public class ApiUtilities {
    private static Retrofit retrofit=null;
    public  static ApiInterface getClient(){
        if (retrofit== null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


        }
        return  retrofit.create(ApiInterface.class);
    }
}
