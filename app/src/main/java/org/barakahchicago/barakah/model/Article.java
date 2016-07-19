package org.barakahchicago.barakah.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Article implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
    private String id;
    private String title;
    private String author;
    private String body;
    private String image;
    private String date_created;
    private String last_updated;
    private String end_publish;

    protected Article(Parcel in) {
        id = in.readString();
        title = in.readString();
        author = in.readString();
        body = in.readString();
        image = in.readString();
        date_created = in.readString();
        last_updated = in.readString();
        end_publish = in.readString();
    }

    public Article() {

    }

    public static Article getTestInstance() {
        Article test = new Article();
        test.setId("1");
        test.setTitle("test article title");
        test.setBody("test article body");
        test.setAuthor("test article author");
        test.setImage("image");
        test.setDate_created("2015-11-30 00:00:00");
        test.setLast_updated("2015-11-30 00:00:00");
        test.setEnd_publish("2017-12-30 00:00:00");

        return test;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }

    public String getEnd_publish() {
        return end_publish;
    }

    public void setEnd_publish(String end_publish) {
        this.end_publish = end_publish;
    }

    public boolean isEqualTo(Article article) {
        boolean equal = false;

        if (this.getId().equals(article.getId())) {
            equal = true;


            return equal;

        }


        return equal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(body);
        dest.writeString(image);
        dest.writeString(date_created);
        dest.writeString(last_updated);
        dest.writeString(end_publish);
    }
}