package haru.spotify.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Playlist {

    private int id;
    private String title;
    private CustomDate creationDate;
    private User creator;
    private boolean isSponsored;
    private List<Song> songs;

    Playlist(int id, String title, CustomDate creationDate, User creator, boolean isSponsored){
        this.id = id;
        this.title = title;
        this.creationDate = creationDate;
        this.creator = creator;
        this.isSponsored = isSponsored;
        songs = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public CustomDate getCreationDate() {
        return creationDate;
    }

    public User getCreator() {
        return creator;
    }

    public boolean isSponsored() {
        return isSponsored;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}