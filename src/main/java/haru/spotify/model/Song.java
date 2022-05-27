package haru.spotify.model;

public class Song {

    private int id;
    private String name;
    private CustomDuration duration;
    private String path;
    private int timesListened;
    private Album album;
    private Artist artist;
    private Artist collabArtist;

    // Constructors

    public Song(int id, String name, CustomDuration duration, String path, int timesListened, Album album, Artist artist){
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.path = path;
        this.timesListened = timesListened;
        this.artist = artist;
        this.collabArtist = null;
        this.album = album;
        album.getSongs().add(this);
    }

    public Song(String name, Album album){
        this.id = 0;
        this.name = name;
        this.duration = null;
        this.path = null;
        this.timesListened = 0;
        this.artist = null;
        this.collabArtist = null;
        this.album = album;
    }

    @Override
    public String toString(){
        return id+" "+name+" "+duration+" "+path+" "+timesListened+" "+album+" "+artist+" "+collabArtist;
    }

    // Getters Setters

    public String getName() {
        return name;
    }

    public Artist getArtist() {
        return artist;
    }

    public Album getAlbum(){
        return  album;
    }

    public int getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public CustomDuration getDuration() {
        return duration;
    }

    public int getTimesListened() {
        return timesListened;
    }

    public Artist getCollabArtist() {
        return collabArtist;
    }

    public boolean isCollaborative(){
        return collabArtist != null;
    }

    public void setCollabArtist(Artist collabArtist) {
        this.collabArtist = collabArtist;
    }
}
