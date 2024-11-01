package com.example.scrollfeed;

import java.io.Serializable;

public class Article implements Serializable {
    private int id;
    private String source;
    private String category;
    private String title;
    private String authors;
    private String publishedDate;
    private String content;
    private String url;
    private String topImage;
    private double sentiment;

    public void setId(int id) {
        this.id = id;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTopImage(String topImage) {
        this.topImage = topImage;
    }

    public void setSentiment(double sentiment) {
        this.sentiment = sentiment;
    }

    public int getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public String getTopImage() {
        return topImage;
    }

    public double getSentiment() {
        return sentiment;
    }
    @Override
    public String toString() {
        return title;
    }
}