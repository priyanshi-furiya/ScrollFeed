package com.example.scrollfeed;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "news_aggregator.db";
    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_PATH = "";
    private final Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        DATABASE_PATH = context.getDatabasePath(DATABASE_NAME).getPath();
        copyDatabase();
    }

    private void copyDatabase() {
        try {
            File dbFile = new File(DATABASE_PATH);
            if (!dbFile.exists()) {
                InputStream inputStream = context.getAssets().open(DATABASE_NAME);
                OutputStream outputStream = new FileOutputStream(DATABASE_PATH);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // No need to create tables as the database is pre-existing
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade if needed
    }

    public List<Article> getArticlesByCategory(String category) {
        List<Article> articles = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM articles WHERE category = ?", new String[]{category});

        if (cursor.moveToFirst()) {
            do {
                Article article = new Article();
                article.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                article.setSource(cursor.getString(cursor.getColumnIndexOrThrow("source")));
                article.setCategory(cursor.getString(cursor.getColumnIndexOrThrow("category")));
                article.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                article.setAuthors(cursor.getString(cursor.getColumnIndexOrThrow("authors")));
                article.setPublishedDate(cursor.getString(cursor.getColumnIndexOrThrow("published_date")));
                article.setContent(cursor.getString(cursor.getColumnIndexOrThrow("content")));
                article.setUrl(cursor.getString(cursor.getColumnIndexOrThrow("url")));
                article.setTopImage(cursor.getString(cursor.getColumnIndexOrThrow("top_image")));
                article.setSentiment(cursor.getDouble(cursor.getColumnIndexOrThrow("sentiment")));
                articles.add(article);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return articles;
    }

    public boolean doesTableExist(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{tableName});
        boolean tableExists = cursor.getCount() > 0;
        cursor.close();
        return tableExists;
    }
}