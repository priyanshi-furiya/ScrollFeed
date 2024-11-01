package com.example.scrollfeed;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class NewsAdapter extends BaseAdapter {
    private Context context;
    private List<Article> articles;

    public NewsAdapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    @Override
    public int getCount() {
        return articles.size();
    }

    @Override
    public Object getItem(int position) {
        return articles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.news_item, parent, false);
            holder = new ViewHolder();
            holder.newsTitle = convertView.findViewById(R.id.news_title);
            holder.newsSource = convertView.findViewById(R.id.news_source);
            holder.newsImage = convertView.findViewById(R.id.news_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Article article = articles.get(position);
        holder.newsTitle.setText(article.getTitle());
        holder.newsSource.setText(article.getSource());
        Glide.with(context).load(article.getTopImage()).into(holder.newsImage);

        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra("article", article);
            context.startActivity(intent);
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView newsTitle;
        TextView newsSource;
        ImageView newsImage;
    }
}