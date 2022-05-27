package haru.spotify.model;

import haru.spotify.SpotifyController;

import java.sql.Date;

public class User {

    private int id;
    private String username;
    private String password;
    private String email;
    private boolean gender; // 0 = Male, 1 = Female
    private CustomDate bDate;
    private String country;
    private String zip;
    private boolean isPremium;

    // Constructor

    public User(int id, String username, String password, String email, boolean gender, CustomDate bDate, String country, String zip, boolean isPremium) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.bDate = bDate;
        this.country = country;
        this.zip = zip;
        this.isPremium = isPremium;
    }

    @Override
    public String toString(){
        String genderString;
        if (gender) genderString = "Female";
        else genderString = "Male";
        return id+" "+username+" "+password+" "+email+" "+genderString+" "+bDate+" "+country+" "+zip;
    }

    // Methods

    public boolean isFollowingSong(Song song){
        for (Song song1 : SpotifyController.getFollowedSongs()){
            if (song1.getId() == song.getId()) return true;
        }
        return false;
    }

    public boolean isFollowingArtist(Artist artist){
        for (Artist artist1 : SpotifyController.getFollowedArtists()){
            if (artist1.getId() == artist.getId()) return true;
        }
        return false;
    }

    public boolean isFollowingAlbum(Album album){
        for (Album album1 : SpotifyController.getFollowedAlbums()){
            if (album1.getId() == album.getId()) return true;
        }
        return false;
    }

    public boolean isFollowingPlaylist(Active playlist){
        for (Active playlist1 : SpotifyController.getFollowedPlaylists()){
            if (playlist1.getId() == playlist.getId()) return true;
        }
        return false;
    }

    public void becomePremium(){
        isPremium = true;
    }

    // Getters Setters

    public int getId() {
        return id;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public String getEmail() {
        return email;
    }

    public CustomDate getBirthDate() {
        return bDate;
    }

    public String getCountry() {
        return country;
    }

    public String getZip() {
        return zip;
    }

    public String getGender(){
        String genderString;
        if (gender) return "Female";
        else return "Male";
    }

    public boolean isMale(){
        return !gender;
    }

    public boolean isFemale(){
        return gender;
    }

    public boolean isPremium() {
        return isPremium;
    }
}
