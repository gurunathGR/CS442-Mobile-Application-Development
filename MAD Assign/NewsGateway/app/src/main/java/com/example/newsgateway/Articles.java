package com.example.newsgateway;

import java.io.Serializable;

public class Articles implements Serializable {
    String aAuthor;
    String aTitle;
    String aDescription;
    String aUrlToImage;
    String aPublishedAt;
    String articleUrl;

    public String getaAuthor()
    {
        return aAuthor;
    }

    public void setaAuthor(String aAuthor) {
        this.aAuthor = aAuthor;
    }

    public String getaTitle() {
        return aTitle;
    }

    public void setaTitle(String aTitle) {
        this.aTitle = aTitle;
    }

    public String getaDescription() {
        return aDescription;
    }

    public void setaDescription(String aDescription) {
        this.aDescription = aDescription;
    }

    public String getaUrlToImage() {
        return aUrlToImage;
    }

    public void setaUrlToImage(String aUrlToImage) {
        this.aUrlToImage = aUrlToImage;
    }

    public String getaPublishedAt() {
        return aPublishedAt;
    }

    public void setaPublishedAt(String aPublishedAt) {
        this.aPublishedAt = aPublishedAt;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }




}

