package org.barakahchicago.barakah.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
    private String id;
    private String title;
    private String start_date;
    private String end_date;
    private String location;
    private String description;
    private String image;
    private String date_created;
    private String last_updated;

    protected Event(Parcel in) {
        id = in.readString();
        title = in.readString();
        start_date = in.readString();
        end_date = in.readString();
        location = in.readString();
        description = in.readString();
        image = in.readString();
        date_created = in.readString();
        last_updated = in.readString();
    }

    public Event() {

    }

    public static Event getTestInstance() {
        Event test = new Event();
        test.setTitle("test1");
        test.setId("1");
        test.setLocation("location");
        test.setDate_created("2016-11-30 00:00:00");
        test.setStart_date("2016-11-30 00:00:00");
        test.setEnd_date("2015-12-30 00:00:00");
        test.setLast_updated("2015-12-30 00:00:00");
        test.setDate_created("2015-12-30 00:00:00");
        test.setImage("http://www.keenthemes.com/preview/metronic/theme/assets/global/plugins/jcrop/demos/demo_files/image1.jpg");

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

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public boolean isEqualTo(Event event) {
        boolean equal = false;


        if (this.getId().equals(event.getId())) {
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
        dest.writeString(start_date);
        dest.writeString(end_date);
        dest.writeString(location);
        dest.writeString(description);
        dest.writeString(image);
        dest.writeString(date_created);
        dest.writeString(last_updated);
    }
}