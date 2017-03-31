package com.hackernews;

import com.hackernews.api.ApiClient;
import com.hackernews.api.ApiService;
import com.hackernews.model.Item;

import org.junit.Test;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by garphing on 30/3/17.
 */

public class ApiClientTest {

    @Test
    public void topStories_isNotNull() throws Exception {

        Call<List<Integer>> topStories = ApiClient.getInstance().listTopStories();
        assertNotNull(topStories.execute().body());
    }

    @Test
    public void getItem_isNotNull() throws Exception {

        Call<Item> item = ApiClient.getInstance().getItem(13991851);
        assertNotNull(item.execute().body());
    }

    @Test
    public void item13991851Type_isStory() throws Exception {

        Call<Item> item = ApiClient.getInstance().getItem(13991851);
        assertEquals("story", item.execute().body().getType());
    }
}
