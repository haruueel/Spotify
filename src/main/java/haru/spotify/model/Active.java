package haru.spotify.model;

public class Active extends Playlist {

    private boolean isCollaborative;

    public Active(int id, String title, CustomDate creationDate, User creator, boolean isSponsored, boolean isCollaborative) {
        super(id, title, creationDate, creator, isSponsored);
        this.isCollaborative = isCollaborative;
    }

    public String toString(){
        return this.getTitle();
    }

    public boolean isCollaborative() {
        return isCollaborative;
    }

    public void setCollaborative(boolean collaborative) {
        isCollaborative = collaborative;
    }
}