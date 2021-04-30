package com.example.collectdata.bean;

import java.util.Date;

public class SelfiesPhoto {
    private String photoId;
    private Date datetime;
    private String uri;

    public SelfiesPhoto(String photoId, Date datetime, String uri) {
        this.photoId = photoId;
        this.datetime = datetime;
        this.uri = uri;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "SelfiesPhoto{" +
                "photoId='" + photoId + '\'' +
                ", datetime=" + datetime +
                ", uri='" + uri + '\'' +
                '}';
    }
}
