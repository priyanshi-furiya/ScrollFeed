package com.example.scrollfeed;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Categories extends AppCompatActivity {
    private MenuAdapter menuAdapter;
    private List<MenuItem> menuItems;
    private PopupWindow popupWindow;

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

        ImageView imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        menuItems = new ArrayList<>();
        menuItems.add(new MenuItem(R.drawable.profile, "Profile"));
        menuItems.add(new MenuItem(R.drawable.discover, "Refresh"));
        menuItems.add(new MenuItem(R.drawable.signout, "Sign Out"));

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
    private void showPopupMenu(View anchorView) {
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_menu, null);

        ListView popupListView = popupView.findViewById(R.id.popup_menu_list);
        menuAdapter = new MenuAdapter(this, menuItems);
        popupListView.setAdapter(menuAdapter);

        int popupWidth = 600;
        popupWindow = new PopupWindow(popupView,
                popupWidth,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true);

        int xOffset = 50;
        popupWindow.showAsDropDown(anchorView, xOffset, 0);
        popupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:
                        Intent profileIntent = new Intent(Categories.this, Profile.class);
                        startActivity(profileIntent);
                        break;
                    case 1:
                        refreshDB();
                        break;
                    case 2:
                        signOut();
                        break;
                }
                popupWindow.dismiss();
            }
        });
    }
    private void signOut() {
        // Clear session
        SessionManager sessionManager = new SessionManager(this);
        sessionManager.logout();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(Categories.this, SignIn.class);
        startActivity(intent);
        finish();
    }
    private void refreshDB() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Step 1: Fetch the database file from the Python server
                    URL url = new URL("http://192.168.149.30:5000/get_db");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                    FileOutputStream fileOutputStream = new FileOutputStream(getDatabasePath("news_aggregator.db"));

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                    }

                    fileOutputStream.close();
                    inputStream.close();
                    connection.disconnect();

                    // Step 2: Reopen the database connection
                    DatabaseHelper dbHelper = new DatabaseHelper(Categories.this);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}