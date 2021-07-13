package com.google;

import java.util.*;

public class VideoPlayer {
  private String currentVideo = null; // name of the current video
  private Boolean paused = false; // flag for the current video
  private final VideoLibrary videoLibrary;
  private final HashMap<String,VideoPlaylist> playlists;

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
    this.playlists = new HashMap<>();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void showAllVideos() {
    ArrayList<Video> videos = (ArrayList)((ArrayList)videoLibrary.getVideos()).clone();
    Collections.sort(videos);
    System.out.println("Here's a list of all available videos:");

    for (Video video: videos) {
      System.out.print("  " + video.getTitle() + " (" + video.getVideoId() + ") [");
      List<String> tags = video.getTags();

      for (int i = 0; i < tags.size(); ++i) {
        System.out.print(tags.get(i));
        if (i < tags.size()-1) System.out.print(" ");
      }

      System.out.print("]");

      if (!video.flag.isEmpty())
        System.out.println(" - FLAGGED (reason: " + video.flag + ")");
      else
        System.out.println();
    }
  }

  public void playVideo(String videoId) {
    ArrayList<Video> videos = (ArrayList<Video>) videoLibrary.getVideos();

    for (Video video: videos) {
      if (video.getVideoId().equals(videoId)) {
        if (!video.flag.isEmpty()) {
          System.out.println("Cannot play video: Video is currently flagged (reason: " + video.flag + ")");
          return;
        }

        if (currentVideo != null) System.out.println("Stopping video: " + currentVideo);
        currentVideo = video.getTitle();
        System.out.println("Playing video: " + currentVideo);
        paused = false;
        return;
      }
    }

    System.out.println("Cannot play video: Video does not exist");
  }

  public void stopVideo() {
    paused = false;
    if (currentVideo == null)
      System.out.println("Cannot stop video: No video is currently playing");
    else {
      System.out.println("Stopping video: " + currentVideo);
      currentVideo = null;
    }
  }

  public void playRandomVideo() {
    if (currentVideo != null) stopVideo();
    ArrayList<Video> videos = new ArrayList<>();

    for (Video video: videoLibrary.getVideos())
      if (video.flag.isEmpty()) videos.add(video);

    if (videos.isEmpty()) {
      System.out.println("No videos available");
      return;
    }

    Random r = new Random();
    int idx = r.nextInt(videos.size());
    currentVideo = videos.get(idx).getTitle();
    System.out.println("Playing video: " + currentVideo);
  }

  public void pauseVideo() {
    if (paused) {
      System.out.println("Video already paused: " + currentVideo);
      return;
    }

    if (currentVideo == null) {
      System.out.println("Cannot pause video: No video is currently playing");
      return;
    }

    System.out.println("Pausing video: " + currentVideo);
    paused = true;
  }

  public void continueVideo() {
    if (currentVideo == null) {
      System.out.println("Cannot continue video: No video is currently playing");
      return;
    }

    if (!paused) {
      System.out.println("Cannot continue video: Video is not paused");
      return;
    }

    System.out.println("Continuing video: " + currentVideo);
    paused = false;
  }

  public void showPlaying() {
    if (currentVideo == null) {
      System.out.println("No video is currently playing");
      return;
    }

    ArrayList<Video> videos = (ArrayList<Video>) videoLibrary.getVideos();

    for (Video video: videos) {
      if (video.getTitle().equals(currentVideo)) {
        System.out.print("Currently playing: " + video.getTitle() + " (" + video.getVideoId() + ") [");

        for (int i = 0; i < video.getTags().size(); ++i) {
          System.out.print(video.getTags().get(i));
          if (i < video.getTags().size()-1) System.out.print(" ");
        }

        System.out.print("]");

        if (paused)
          System.out.println(" - PAUSED");
        else
          System.out.println();
      }
    }
  }

  /** Check if the playlist exists and get its name */
  private String checkAndGetPName(String playlistName) {
    for (String pname: playlists.keySet()) {
      if (pname.equalsIgnoreCase(playlistName))
        return pname;
    }

    return "";
  }

  /** Check if the video is in the library and return its title */
  private String checkAndGetVTitle(String videoId) {
    for (Video video: videoLibrary.getVideos()) {
      if (video.getVideoId().equalsIgnoreCase(videoId))
        return video.getTitle();
    }

    return "";
  }

  public void createPlaylist(String playlistName) {
    if (!checkAndGetPName(playlistName).isEmpty()) {
      System.out.println("Cannot create playlist: A playlist with the same name already exists");
      return;
    }

    playlists.put(playlistName, new VideoPlaylist(playlistName));
    System.out.println("Successfully created new playlist: " + playlistName);
  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    String pvalid = checkAndGetPName(playlistName);

    if (pvalid.isEmpty()) {
      System.out.println("Cannot add video to " + playlistName + ": Playlist does not exist");
      return;
    }

    String vvalid = checkAndGetVTitle(videoId);

    if (vvalid.isEmpty()) {
      System.out.println("Cannot add video to " + playlistName + ": Video does not exist");
      return;
    }

    // check if the video is flagged
    for (Video video: videoLibrary.getVideos()) {
      if (video.getVideoId().equalsIgnoreCase(videoId)) {
        if (!video.flag.isEmpty()) {
          System.out.println("Cannot add video to " + playlistName + ": Video is currently flagged (reason: " +
                  video.flag + ")");
          return;
        }
      }
    }

    List<Video> added = playlists.get(pvalid).getVideos();

    for (Video video: added) {
      if (video.getTitle().equalsIgnoreCase(vvalid)) {
        System.out.println("Cannot add video to " + playlistName + ": Video already added");
        return;
      }
    }

    for (Video video: videoLibrary.getVideos()) {
      if (video.getTitle().equals(vvalid)) {
        playlists.get(pvalid).addVideo(video);
        System.out.println("Added video to " + playlistName + ": " + vvalid);
        return;
      }
    }
  }

  public void showAllPlaylists() {
    if (playlists.isEmpty()) {
      System.out.println("No playlists exist yet");
      return;
    }

    ArrayList<String> pnames = new ArrayList<>(playlists.keySet());

    Collections.sort(pnames);
    System.out.println("Showing all playlists:");

    for (String name: pnames)
      System.out.println(name);
  }

  public void showPlaylist(String playlistName) {
    String pvalid = checkAndGetPName(playlistName);

    if (pvalid.isEmpty()) {
      System.out.println("Cannot show playlist " + playlistName +": Playlist does not exist");
      return;
    }

    System.out.println("Showing playlist: " + playlistName);

    if (playlists.get(pvalid).getVideos().isEmpty())
      System.out.println("No videos here yet");
    else {
      for (Video video: playlists.get(pvalid).getVideos()) {
        System.out.print(video.toString());

        if (video.flag.isEmpty())
          System.out.println();
        else
          System.out.println(" - FLAGGED (reason: " + video.flag + ")");
      }
    }
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    String pvalid = checkAndGetPName(playlistName);

    if (pvalid.isEmpty()) {
      System.out.println("Cannot remove video from " + playlistName + ": Playlist does not exist");
      return;
    }

    String vvalid = checkAndGetVTitle(videoId);

    if (vvalid.isEmpty()) {
      System.out.println("Cannot remove video from " + playlistName + ": Video does not exist");
      return;
    }

    for (Video video: playlists.get(pvalid).getVideos()) {
      if (video.getVideoId().equalsIgnoreCase(videoId)) {
        System.out.println("Removed video from " + playlistName + ": " + video.getTitle());
        playlists.get(pvalid).getVideos().remove(video);
        return;
      }
    }

    System.out.println("Cannot remove video from " + playlistName + ": Video is not in playlist");
  }

  public void clearPlaylist(String playlistName) {
    String pvalid = checkAndGetPName(playlistName);

    if (!pvalid.isEmpty()) {
      System.out.println("Successfully removed all videos from " + playlistName);
      playlists.get(pvalid).getVideos().clear();
      return;
    }

    System.out.println("Cannot clear playlist " + playlistName + ": " + "Playlist does not exist");
  }

  public void deletePlaylist(String playlistName) {
    String pvalid = checkAndGetPName(playlistName);

    if (!pvalid.isEmpty()) {
      System.out.println("Deleted playlist: " + playlistName);
      playlists.remove(pvalid);
      return;
    }

    System.out.println("Cannot delete playlist " + playlistName + ": " + "Playlist does not exist");
  }

  /** Helper function that prints results of the search */
  private void printSearchResults(String search, ArrayList<Video> videos) {
    if (videos.isEmpty()) {
      System.out.println("No search results for " + search);
      return;
    }

    System.out.println("Here are the results for " + search + ":");
    Collections.sort(videos);

    for (int i = 0; i < videos.size(); ++i)
      System.out.println(i+1 + ") " + videos.get(i).toString());

    System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
    System.out.println("If your answer is not a valid number, we will assume it's a no.");
    Scanner in = new Scanner(System.in);
    String choice = in.nextLine();
    int idx;

    try {
      idx = Integer.parseInt(choice);
    }
    catch (Exception e) {
      return;
    }

    if (idx > 0 && idx <= videos.size())
      playVideo(videos.get(idx-1).getVideoId());
  }

  public void searchVideos(String searchTerm) {
    String search = searchTerm;
    ArrayList<Video> videos = new ArrayList<>();
    searchTerm = searchTerm.toLowerCase();

    for (Video video: videoLibrary.getVideos()) {
      if (!video.flag.isEmpty()) continue;
      String lcTitle = video.getTitle();
      lcTitle = lcTitle.toLowerCase();

      if (lcTitle.contains(searchTerm)) {
        videos.add(video);
      }
    }

    printSearchResults(search, videos);
  }

  public void searchVideosWithTag(String videoTag) {
    String vtag = videoTag;
    videoTag = videoTag.toLowerCase();
    ArrayList<Video> videos = new ArrayList<>();

    for (Video video: videoLibrary.getVideos()) {
      if (!video.flag.isEmpty()) continue;
      List<String> tags = video.getTags();

      for (String tag: tags) {
        if (tag.equalsIgnoreCase(videoTag)) {
          videos.add(video);
          break;
        }
      }
    }

    printSearchResults(vtag, videos);
  }

  private Video getVideoById(String videoId) {
    for (Video video: videoLibrary.getVideos()) {
      if (video.getVideoId().equalsIgnoreCase(videoId))
        return video;
    }

    return null;
  }

  public void flagVideo(String videoId) {
    if (checkAndGetVTitle(videoId).isEmpty()) {
      System.out.println("Cannot flag video: Video does not exist");
      return;
    }

    Video pvideo = getVideoById(videoId);

    if (!pvideo.flag.isEmpty()) {
      System.out.println("Cannot flag video: Video is already flagged");
      return;
    }

    if (currentVideo != null && currentVideo.equalsIgnoreCase(pvideo.getTitle()))
      stopVideo();

    pvideo.flag = "Not supplied";
    System.out.println("Successfully flagged video: " + pvideo.getTitle() + " (reason: " +
            pvideo.flag + ")");
  }

  public void flagVideo(String videoId, String reason) {
    if (checkAndGetVTitle(videoId).isEmpty()) {
      System.out.println("Cannot flag video: Video does not exist");
      return;
    }

    Video pvideo = getVideoById(videoId);

    if (!pvideo.flag.isEmpty()) {
      System.out.println("Cannot flag video: Video is already flagged");
      return;
    }

    if (currentVideo != null && currentVideo.equalsIgnoreCase(pvideo.getTitle()))
      stopVideo();

    pvideo.flag = reason;
    System.out.println("Successfully flagged video: " + pvideo.getTitle() + " (reason: " +
            pvideo.flag + ")");
  }

  public void allowVideo(String videoId) {
    if (checkAndGetVTitle(videoId).isEmpty()) {
      System.out.println("Cannot remove flag from video: Video does not exist");
      return;
    }

    Video pvideo = getVideoById(videoId);

    if (pvideo.flag.isEmpty()) {
      System.out.println("Cannot remove flag from video: Video is not flagged");
      return;
    }

    pvideo.flag = "";
    System.out.println("Successfully removed flag from video: " + pvideo.getTitle());
  }
}