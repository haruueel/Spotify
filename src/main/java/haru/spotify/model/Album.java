package haru.spotify.model;

import java.util.ArrayList;
import java.util.List;

public class Album {

    private int id;
    private String year;
    private String name;
    private String imgPath;
    private boolean sponsored;
    private CustomDate sponsorStartDate;
    private CustomDate sponsorEndDate;
    private Artist artist;
    private List<Song> songs = new ArrayList<>();

    // Constructors

    public Album(int id, String year, String name, String imgPath, boolean sponsored, CustomDate sponsorStartDate, CustomDate sponsorEndDate, Artist artist){
        this.id = id;
        this.year = year;
        this.name = name;
        this.imgPath = imgPath;
        this.sponsored = sponsored;
        this.sponsorStartDate = sponsorStartDate;
        this.sponsorEndDate = sponsorEndDate;
        this.artist = artist;
    }

    public Album(String imgPath){
        this.id = 0;
        this.year = null;
        this.name = null;
        this.imgPath = imgPath;
        this.sponsored = false;
        this.sponsorStartDate = null;
        this.sponsorEndDate = null;
        this.artist = null;
    }

    @Override
    public String toString(){
        return id+" "+year+" "+name+" "+imgPath+" "+sponsored+" "+sponsorStartDate+" "+sponsorEndDate+" "+artist;
    }

    // Getters Setters

    public int getId() {
        return id;
    }

    public String getYear() {
        return year;
    }

    public String getName() {
        return name;
    }

    public Artist getArtist() {
        return artist;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public String getImgPath() {
        return imgPath;
    }

    public CustomDate getSponsorEndDate() {
        return sponsorEndDate;
    }

    public CustomDate getSponsorStartDate() {
        return sponsorStartDate;
    }

    public boolean isSponsored() {
        return sponsored;
    }
}
