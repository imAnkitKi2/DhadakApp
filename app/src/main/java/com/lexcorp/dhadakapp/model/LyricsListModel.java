package com.lexcorp.dhadakapp.model;

import java.io.Serializable;

/**
 * Created by Boss on 7/3/2017.
 */
public class LyricsListModel implements Serializable {

    public String songName;

    public String lyrics;

    public LyricsListModel() {
    }

    public LyricsListModel(String songName, String lyrics) {
        this.songName = songName;
        this.lyrics = lyrics;
    }
}
