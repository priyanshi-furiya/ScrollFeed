// NewsListActivity.java
package com.example.scrollfeed;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NewsListActivity extends AppCompatActivity {
    private ListView listView;
    private NewsAdapter adapter;
    private List<Article> articles;
    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        String category = getIntent().getStringExtra("category");
        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        if (databaseHelper.doesTableExist("articles")) {
            listView = findViewById(R.id.news_list_view);
            articles = databaseHelper.getArticlesByCategory(category);
            sortArticlesByPreference();
            adapter = new NewsAdapter(this, articles);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener((parent, view, position, id) -> {
                Article article = articles.get(position);
                updatePreference(article.getSentiment());
                Intent intent = new Intent(NewsListActivity.this, NewsDetailActivity.class);
                intent.putExtra("article", article);
                startActivity(intent);
            });
        } else {
            // Handle the case where the table does not exist
            Toast.makeText(this, "Table 'articles' does not exist in the database.", Toast.LENGTH_LONG).show();
        }
    }

    private void sortArticlesByPreference() {
        String preferredSentiment = sessionManager.getPreferredSentiment();
        Collections.sort(articles, new Comparator<Article>() {
            @Override
            public int compare(Article a1, Article a2) {
                if (preferredSentiment.equals("positive")) {
                    return Double.compare(a2.getSentiment(), a1.getSentiment());
                } else {
                    return Double.compare(a1.getSentiment(), a2.getSentiment());
                }
            }
        });
    }

    private void updatePreference(double sentiment) {
        String currentPreference = sessionManager.getPreferredSentiment();
        if (sentiment > 0 && !currentPreference.equals("positive")) {
            sessionManager.setPreferredSentiment("positive");
        } else if (sentiment < 0 && !currentPreference.equals("negative")) {
            sessionManager.setPreferredSentiment("negative");
        }
    }
}