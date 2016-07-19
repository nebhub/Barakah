package org.barakahchicago.barakah.model;

public class Message {


    private String id;
    private String title;
    private String message;
    private String image;
    private String audio;
    private String video;
    private String date_created;
    private String last_updated;
    private String end_publish;

    public static Message getTestInstance() {
        Message test = new Message();
        test.setId("1");
        test.setTitle("test message title");
        test.setMessage("test message body");
        test.setAudio("test message audio");
        test.setVideo("test message video");
        test.setImage("test message image");
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
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

    public boolean isEqualTo(Message message) {
        boolean equal = false;


        if (this.getId().equals(message.getId())) {
            equal = true;


            return equal;

        }


        return equal;
    }

}
