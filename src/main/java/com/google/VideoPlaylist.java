package com.google;

import java.util.ArrayList;
import java.util.List;

/** A class used to represent a Playlist */
class VideoPlaylist {
    String name;
    List<Video> videos = new ArrayList<Video>();

    public VideoPlaylist(String name) {
        this.name = name;
    }

    public void addVideo(Video video) {
        videos.add(video);
    }

    public List<Video> getVideos() {
        return videos;
    }

    public boolean containsVideo(String videoId) {
        for (Video video: videos)
            if (video.getVideoId().equalsIgnoreCase(videoId)) return true;

        return false;
    }
}