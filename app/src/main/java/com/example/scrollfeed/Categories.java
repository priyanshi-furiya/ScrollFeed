package com.example.scrollfeed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Categories extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.categories);

        Button tr = findViewById(R.id.trending);
        Button sp = findViewById(R.id.sports);
        Button en = findViewById(R.id.entertainment);
        Button te = findViewById(R.id.technology);
        Button bu = findViewById(R.id.business);
        Button na = findViewById(R.id.national);
        Button in = findViewById(R.id.international);

        View.OnClickListener categoryClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = "";
                int id = v.getId();
                if (id == R.id.trending) {
                    category = "top";
                } else if (id == R.id.sports) {
                    category = "sports";
                } else if (id == R.id.entertainment) {
                    category = "entertainment";
                } else if (id == R.id.technology) {
                    category = "technology";
                } else if (id == R.id.business) {
                    category = "business";
                } else if (id == R.id.national) {
                    category = "national";
                } else if (id == R.id.international) {
                    category = "international";
                }
                Intent intent = new Intent(Categories.this, NewsListActivity.class);
                intent.putExtra("category", category);
                startActivity(intent);
            }
        };

        tr.setOnClickListener(categoryClickListener);
        sp.setOnClickListener(categoryClickListener);
        en.setOnClickListener(categoryClickListener);
        te.setOnClickListener(categoryClickListener);
        bu.setOnClickListener(categoryClickListener);
        na.setOnClickListener(categoryClickListener);
        in.setOnClickListener(categoryClickListener);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.categories), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}