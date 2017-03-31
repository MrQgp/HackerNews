package com.hackernews.api;

import com.hackernews.model.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by garphing on 30/3/17.
 */

public interface ApiService {
    final String version = "v0";

    @GET(version + "/topstories.json")
    Call<List<Integer>> listTopStories();

    @GET(version + "/item/{itemId}.json")
    Call<Item> getItem(@Path("itemId") int itemId);
}
