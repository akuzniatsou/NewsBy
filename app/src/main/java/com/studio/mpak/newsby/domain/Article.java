package com.studio.mpak.newsby.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrei Kuzniatsou
 */
public class Article implements Parcelable {

    private int id;
    private List<String> categories = new ArrayList<>();
    private String title;
    private String articleUrl;
    private String imageUrl;
    private String date;
    private String views;
    private String author;
    private String comments;
    private String content;
    private String description;

    private Article prev = null;
    private Article next = null;
    private List<Article> related = new ArrayList<>();

    public Article() {
    }

    private Article(Parcel parcel) {
        this();
        id = parcel.readInt();
        parcel.readStringList(categories);
        title = parcel.readString();
        articleUrl = parcel.readString();
        imageUrl = parcel.readString();
        date = parcel.readString();
        views = parcel.readString();
        author = parcel.readString();
        comments = parcel.readString();
        content = parcel.readString();
        description = parcel.readString();
        prev = parcel.readParcelable(Article.class.getClassLoader());
        next = parcel.readParcelable(Article.class.getClassLoader());
        parcel.readList(related, Article.class.getClassLoader());
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getCategories() {
        if (categories == null) {
            return new ArrayList<>();
        }
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Article> getRelated() {
        return related;
    }

    public void setRelated(List<Article> related) {
        this.related = related;
    }

    public Article getPrev() {
        return prev;
    }

    public void setPrev(Article prev) {
        this.prev = prev;
    }

    public Article getNext() {
        return next;
    }

    public void setNext(Article next) {
        this.next = next;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeStringList(categories);
        parcel.writeString(title);
        parcel.writeString(articleUrl);
        parcel.writeString(imageUrl);
        parcel.writeString(date);
        parcel.writeString(views);
        parcel.writeString(author);
        parcel.writeString(comments);
        parcel.writeString(content);
        parcel.writeString(description);
        parcel.writeParcelable(next, flags);
        parcel.writeParcelable(prev, flags);
        parcel.writeTypedList(related);
    }

    public static final Creator<Article> CREATOR = new
            Creator<Article>() {
                public Article createFromParcel(Parcel in) {
                    return new Article(in);
                }

                public Article[] newArray(int size) {
                    return new Article[size];
                }
            };

}
