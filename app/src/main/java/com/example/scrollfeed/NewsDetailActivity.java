package com.example.scrollfeed;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class NewsDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        Article article = (Article) getIntent().getSerializableExtra("article");

        TextView title = findViewById(R.id.news_detail_title);
        TextView content = findViewById(R.id.news_detail_content);
        ImageView image = findViewById(R.id.news_detail_image);

        title.setText(article.getTitle());
        content.setText(article.getContent());
        Glide.with(this).load(article.getTopImage()).into(image);
    }
}