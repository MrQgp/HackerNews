package com.hackernews.activity;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hackernews.R;
import com.hackernews.adapter.CommentAdapter;
import com.hackernews.adapter.TopStoryAdapter;
import com.hackernews.api.ApiClient;
import com.hackernews.model.Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.id.list;

public class CommentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Item> comments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);


        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);

        getSupportActionBar().setTitle(getString(R.string.label_comment));

        commentAdapter = new CommentAdapter(comments);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(commentAdapter);

        Integer storyId = getIntent().getExtras().getInt("StoryId");
        prepareData(storyId);

        SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
        swipeContainer.setEnabled(false);
    }

    private void prepareData(Integer storyId) {

        ApiClient.getInstance().getItem(storyId).enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                Item item = response.body();


                new AsyncTask<Integer, Void, List<Item>>() {
                    @Override
                    protected List<Item> doInBackground(Integer... kids) {
                        final List<Item> list = new ArrayList<Item>();
                        if(kids != null) {
                            for(Integer commentId : kids) {
                                    try {
                                        Item comment = ApiClient.getInstance().getItem(commentId).execute().body();
                                        list.add(comment);

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                commentAdapter.addAll(list);
                                                commentAdapter.notifyDataSetChanged();
                                            }
                                        });

                                        if(comment.getKids() != null){
                                            for(Integer kidId : comment.getKids()) {
                                                try {
                                                    Item commentKid = ApiClient.getInstance().getItem(kidId).execute().body();
                                                    if(commentKid != null) {
                                                        commentKid.setChild(true);
                                                        list.add(commentKid);
                                                    }
                                                }catch(IOException io){
                                                }
                                            }
                                        }
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                commentAdapter.addAll(list);
                                                commentAdapter.notifyDataSetChanged();
                                            }
                                        });

                                    } catch (IOException io) {

                                    }
                            }
                        }

                        return list;
                    }

                    @Override
                    protected void onPostExecute (List<Item> result){
//                        commentAdapter.clear();
//                        commentAdapter.addAll(result);
//                        commentAdapter.notifyDataSetChanged();
                    }

                }.execute(item.getKids());


            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {

            }
        });
    }
}
