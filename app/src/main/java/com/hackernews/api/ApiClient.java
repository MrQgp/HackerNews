package com.hackernews.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by garphing on 30/3/17.
 */

public class ApiClient {

    private static final String BASE_URL = "https://hacker-news.firebaseio.com/";
    private static ApiService mInstance;

    public static ApiService getInstance(){
        if(mInstance == null)
        {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://hacker-news.firebaseio.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            mInstance = retrofit.create(ApiService.class);
        }
        return mInstance;
    }

}
