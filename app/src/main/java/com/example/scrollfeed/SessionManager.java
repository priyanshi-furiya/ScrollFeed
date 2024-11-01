// SessionManager.java
package com.example.scrollfeed;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "user_preferences";
    private static final String KEY_PREFERRED_SENTIMENT = "preferred_sentiment";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setPreferredSentiment(String sentiment) {
        editor.putString(KEY_PREFERRED_SENTIMENT, sentiment);
        editor.apply();
    }

    public String getPreferredSentiment() {
        return preferences.getString(KEY_PREFERRED_SENTIMENT, "positive");
    }
}