package haru.spotify.model;

public abstract class Database {

    private static final String URL = "jdbc:mysql://127.0.0.1:33006/spotifyMiguel";
    private static final String USER = "root";
    private static final String PASSWORD = "dbrootpass";

    public static String getPassword() {
        return PASSWORD;
    }

    public static String getUrl() {
        return URL;
    }

    public static String getUser() {
        return USER;
    }
}
