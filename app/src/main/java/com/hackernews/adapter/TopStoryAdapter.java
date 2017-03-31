package com.hackernews.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hackernews.R;
import com.hackernews.activity.CommentActivity;
import com.hackernews.api.ApiClient;
import com.hackernews.model.Item;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.id;
import static android.R.id.list;

/**
 * Created by garphing on 30/3/17.
 */

public class TopStoryAdapter extends RecyclerView.Adapter<TopStoryAdapter.ViewHolder> {

    private Context context;
    private List<Integer> topStories;
    private Map<String, Item> topStoryMap = new HashMap<>();


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView line1, line2, order;

        public ViewHolder(View view) {
            super(view);
            order = (TextView) view.findViewById(R.id.row_top_story_tv_order);
            line1 = (TextView) view.findViewById(R.id.row_top_story_tv_1);
            line2 = (TextView) view.findViewById(R.id.row_top_story_tv_2);
        }
    }


    public TopStoryAdapter(Context context, List<Integer> topStories) {
        this.context = context;
        this.topStories = topStories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_top_story, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Integer itemId = topStories.get(position);
        Item item = topStoryMap.get(itemId.toString());
        if(item == null) {
            ApiClient.getInstance().getItem(itemId).enqueue(new Callback<Item>() {
                @Override
                public void onResponse(Call<Item> call, Response<Item> response) {
                    Item item = response.body();
                    topStoryMap.put(itemId.toString(), item);
                    setView(holder, position+1, item);
                }

                @Override
                public void onFailure(Call<Item> call, Throwable t) {

                }
            });
        }

        setView(holder, position+1,  item);

    }

    @Override
    public int getItemCount() {
        return topStories.size();
    }

    public void clear() {
        topStories.clear();
    }

    public void addAll(List<Integer> list) {
        topStories.addAll(list);
    }

    private void setView(ViewHolder holder, int order, final Item item) {
        if (item == null) {
            holder.order.setText("");
            holder.line1.setText("");
            holder.line2.setText("");
        } else {
            holder.order.setText(order + ".");
            String hostHtml = " <small><font color='grey'>(<br/>)</font></small>";
            try {
                String host = new URL(item.getUrl()).getHost().replace("www.", "");
                hostHtml = hostHtml.replace("<br/>", host);
            }catch(MalformedURLException ex){
            }

            holder.line1.setText(Html.fromHtml(item.getTitle() + hostHtml));
            CharSequence relavetime = DateUtils.getRelativeTimeSpanString(item.getTime(), System.currentTimeMillis()/1000L, DateUtils.SECOND_IN_MILLIS);
            String line2Text = item.getScore() + " points by " + item.getBy() + " " + relavetime + " | " + item.getDescendants() + " comments";
            holder.line2.setText(line2Text);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CommentActivity.class);
                    intent.putExtra("StoryId", item.getId());
                    context.startActivity(intent);
                }
            });
        }
    }

}