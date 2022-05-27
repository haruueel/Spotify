package haru.spotify.model;

public class Artist {

    private int id;
    private String name;
    private String imgPath;
    private boolean sponsored;

    public Artist(int id, String name, String imgPath, boolean sponsored){
        this.id = id;
        this.name = name;
        this.imgPath = imgPath;
        this.sponsored = sponsored;
    }

    public String toString(){
        return id+" "+name+" "+imgPath;
    }

    // Getters n Setters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImgPath() {
        return imgPath;
    }

    public boolean isSponsored() {
        return sponsored;
    }
}