package com.example.scrollfeed;

public class MenuItem {
    private int icon;
    private String text;

    public MenuItem(int icon, String text) {
        this.icon = icon;
        this.text = text;
    }

    public int getIcon() {
        return icon;
    }

    public String getText() {
        return text;
    }
}