package com.lexcorp.dhadakapp.model;

/**
 * Created by Boss on 7/3/2017.
 */
public class VideoListModel {

    public String youtubeCode;

    public String title;

    public VideoListModel() {
    }

    public VideoListModel(String title, String youtubeCode) {
        this.title = title;
        this.youtubeCode = youtubeCode;
    }
}
