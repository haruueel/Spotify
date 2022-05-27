package haru.spotify.model;

public abstract class Sample {

    private static final Album sampleAlbum = new Album("icons/albums/placeholder.png");
    private static final Song sampleSong = new Song("", sampleAlbum);

    public static Song getSampleSong(){
        return sampleSong;
    }

    public static Album getSampleAlbum() {
        return sampleAlbum;
    }
}
