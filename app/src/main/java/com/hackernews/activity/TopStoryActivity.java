package com.hackernews.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.hackernews.R;
import com.hackernews.adapter.TopStoryAdapter;
import com.hackernews.api.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopStoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TopStoryAdapter topStoryAdapter;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_story);

        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);

        getSupportActionBar().setTitle(getString(R.string.label_top_story));

        topStoryAdapter = new TopStoryAdapter(this, new ArrayList<Integer>());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(topStoryAdapter);

        prepareData();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prepareData();
            }
        });

    }

    private void prepareData(){
        ApiClient.getInstance().listTopStories().enqueue(new Callback<List<Integer>>() {
            @Override
            public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                swipeContainer.setRefreshing(false);
                topStoryAdapter.clear();
                topStoryAdapter.addAll(response.body());
                topStoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Integer>> call, Throwable t) {
                swipeContainer.setRefreshing(false);
            }
        });
    }
}
