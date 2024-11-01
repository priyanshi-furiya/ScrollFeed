package com.example.scrollfeed;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "user_preferences";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE_NUMBER = "phone_number";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_PREFERRED_SENTIMENT = "preferred_sentiment";
    private static final String KEY_PROFILE_IMAGE = "profile_image";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setUserDetails(String username, String email, String phoneNumber) {
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONE_NUMBER, phoneNumber);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public String getUsername() {
        return preferences.getString(KEY_USERNAME, null);
    }

    public String getEmail() {
        return preferences.getString(KEY_EMAIL, null);
    }

    public String getPhoneNumber() {
        return preferences.getString(KEY_PHONE_NUMBER, null);
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
    public void setPreferredSentiment(String sentiment) {
        editor.putString(KEY_PREFERRED_SENTIMENT, sentiment);
        editor.apply();
    }

    public String getPreferredSentiment() {
        return preferences.getString(KEY_PREFERRED_SENTIMENT, "positive");
    }
    public void setProfileImage(String profileImage) {
        editor.putString(KEY_PROFILE_IMAGE, profileImage);
        editor.apply();
    }

    public String getProfileImage() {
        return preferences.getString(KEY_PROFILE_IMAGE, null);
    }
}