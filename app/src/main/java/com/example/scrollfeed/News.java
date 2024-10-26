package com.example.scrollfeed;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class News extends AppCompatActivity {
    private ListView listView;
    private MenuAdapter menuAdapter;
    private List<MenuItem> menuItems;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news);

        ImageView imageView = findViewById(R.id.imageView2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        // Initialize menu items
        menuItems = new ArrayList<>();
        menuItems.add(new MenuItem(R.drawable.profile, "Profile"));
        menuItems.add(new MenuItem(R.drawable.discover, "Discover"));
        menuItems.add(new MenuItem(R.drawable.category, "Categories"));
        menuItems.add(new MenuItem(R.drawable.signout, "Sign Out"));
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
                    case 1:
                        Intent discoverIntent = new Intent(News.this, Notification.class);
                        startActivity(discoverIntent);
                        break;
                    case 2:
                        Intent categoriesIntent = new Intent(News.this, Categories.class);
                        startActivity(categoriesIntent);
                        break;
                    case 3:
                        Intent signOutIntent = new Intent(News.this, SignIn.class);
                        startActivity(signOutIntent);
                        break;
                }
                popupWindow.dismiss();
            }
        });
    }
}