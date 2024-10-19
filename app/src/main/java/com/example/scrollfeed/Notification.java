package com.example.scrollfeed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Notification extends AppCompatActivity {
    private ListView listView;
    private MenuAdapter menuAdapter;
    private List<MenuItem> menuItems;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);

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
    }
}