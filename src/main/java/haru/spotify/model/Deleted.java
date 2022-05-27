package haru.spotify.model;

public class Deleted extends Playlist{

    private CustomDate deletedDate;

    public Deleted(int id, String title, CustomDate creationDate, User creator, boolean isSponsored, CustomDate deletedDate) {
        super(id, title, creationDate, creator, isSponsored);
        this.deletedDate = deletedDate;
    }

    public CustomDate getDeletedDate() {
        return deletedDate;
    }
}
