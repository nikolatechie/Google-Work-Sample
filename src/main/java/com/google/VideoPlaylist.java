package com.google;

import java.util.ArrayList;
import java.util.List;

/** A class used to represent a Playlist */
class VideoPlaylist {
    String name;
    List<Video> videos = new ArrayList<>();

    public VideoPlaylist(String name) {
        this.name = name;
    }

    public void addVideo(Video video) {
        videos.add(video);
    }

    public List<Video> getVideos() {
        return videos;
    }
}