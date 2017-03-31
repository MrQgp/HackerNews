package com.hackernews.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hackernews.R;
import com.hackernews.api.ApiClient;
import com.hackernews.model.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by garphing on 30/3/17.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<Item> comments;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView line1, line2;
        private View viewSpace, arrow;

        public ViewHolder(View view) {
            super(view);
            viewSpace = view.findViewById(R.id.row_comment_view);
            arrow = view.findViewById(R.id.row_comment_iv);
            line1 = (TextView) view.findViewById(R.id.row_comment_tv_1);
            line2 = (TextView) view.findViewById(R.id.row_comment_tv_2);
        }
    }

    public CommentAdapter(List<Item> comments) {
        this.comments = comments;
    }

    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_comment, parent, false);

        return new CommentAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CommentAdapter.ViewHolder holder, final int position) {
        final Item item = comments.get(position);
        if(item.isChild() == true){
            holder.viewSpace.setVisibility(View.VISIBLE);
        }else{
            holder.viewSpace.setVisibility(View.GONE);
        }

        if(item.getText() == null || item.getText().isEmpty()){
            ApiClient.getInstance().getItem(item.getId()).enqueue(new Callback<Item>() {
                @Override
                public void onResponse(Call<Item> call, Response<Item> response) {
                    Item item = response.body();
                    comments.set(position, item);
                    setView(holder, item);
                }

                @Override
                public void onFailure(Call<Item> call, Throwable t) {

                }
            });
        }else{
            setView(holder, item);
        }



    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void clear() {
        comments.clear();
    }

    public void addAll(List<Item> list) {
        comments.addAll(list);
    }

    private void setView(ViewHolder holder, Item item) {
        CharSequence relavetime = DateUtils.getRelativeTimeSpanString(item.getTime(), System.currentTimeMillis()/1000L, DateUtils.SECOND_IN_MILLIS);
        String line1Text = item.getBy() + " " + relavetime;
        holder.line1.setText(line1Text);
        holder.line2.setText(Html.fromHtml(item.getText()));
        holder.arrow.setVisibility(View.VISIBLE);
    }
}
