package haru.spotify;

import haru.spotify.model.*;

import java.io.IOException;
import java.sql.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SpotifyController implements Initializable {

    @FXML
    public ScrollPane artistPane;
    public ImageView nowListeningImage;
    public Label nowListeningName;
    public ScrollPane favoritePane;
    public HBox artistContainer;
    public HBox favoriteContainer;
    public ImageView ad;
    public VBox mainContainer;
    public Label usernameLabel;
    public TextField searchField;
    public Button upgradeButton;
    public HBox libraryHbox;
    public ScrollPane playlistPane;
    public VBox playlistContainer;
    public Label playlistLabel;

    public static Statement st;
    private static User user;
    private static List<Artist> artists;
    private static List<Artist> followedArtists;
    private static List<Song> songs;
    private static List<Song> followedSongs;
    private static List<Album> albums;
    private static List<Album> followedAlbums;
    private static List<Active> activePlaylists;
    private static List<Active> followedPlaylists;
    private static List<Deleted> deletedPlaylists;
    private static List<Integer> premiumIDs;
    private static List<Integer> sponsoredPlaylistIDs;
    public static List<User> users;
    private int adNumber;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (user.isPremium()) upgradeButton.setVisible(false);
        usernameLabel.setText(user.getUsername());

        displayAd();
        displayAlbumsInPane();
        displayArtistsInPane();
        displayPlaylistInListPane();
        setPanesProperties();
        updateSong(Listening.getSong());

    }

    // Loads every id of sponsored playlists from database.
    public static void initSponsoredPlaylistsIDsFromDatabase() {
        sponsoredPlaylistIDs = new ArrayList<>();
        try {
            ResultSet query = st.executeQuery("select playlist_id from patrocinada;");
            while (query.next()) {
                sponsoredPlaylistIDs.add(query.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Loads every active playlist from database.
    public static void initActivePlaylistsFromDatabase() {
        activePlaylists = new ArrayList<>();
        try {
            ResultSet query = st.executeQuery("select p.id, p.titulo, p.fecha_creacion, p.usuario_id, a.es_compartida " +
                    "from playlist p inner join activa a on a.playlist_id = p.id;");
            while (query.next()) {
                int id = query.getInt(1);
                String title = query.getString(2);
                CustomDate creationDate = new CustomDate(query.getString(3));
                User user = findUserById(query.getInt(4));
                boolean isSponsored = findSponsoredId(id);
                int bit = query.getInt(5);
                boolean isCollaborative = bit != 0;

                Active activePlaylist = new Active(id, title, creationDate, user, isSponsored, isCollaborative);
                activePlaylists.add(activePlaylist);
            }

            for (Active active : activePlaylists){
                ResultSet query2 = st.executeQuery("select cancion_id from anyade_cancion_playlist where playlist_id="+active.getId()+";");
                while (query2.next()){
                    active.getSongs().add(findSongById(query2.getInt(1)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void initDeletedPlaylistsFromDatabase() {
        deletedPlaylists = new ArrayList<>();
        try {
            ResultSet query = st.executeQuery("select p.id, p.titulo, p.fecha_creacion, p.usuario_id, e.fecha_eliminacion " +
                    "from playlist p inner join eliminada e on e.playlist_id = p.id;");
            while (query.next()) {
                int id = query.getInt(1);
                String title = query.getString(2);
                CustomDate creationDate = new CustomDate(query.getString(3));
                User user = findUserById(query.getInt(4));
                boolean isSponsored = findSponsoredId(id);
                CustomDate deletedDate = new CustomDate(query.getString(5));

                Deleted deletedPlaylist = new Deleted(id, title, creationDate, user, isSponsored, deletedDate);
                deletedPlaylists.add(deletedPlaylist);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Loads every user from database.
    public static void initUsersFromDatabase() {
        users = new ArrayList<>();
        premiumIDs = new ArrayList<>();
        try {
            ResultSet query = st.executeQuery("select usuario_id from premium");
            while (query.next()) premiumIDs.add(query.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            ResultSet query = st.executeQuery("select * from usuario;");
            while (query.next()) {
                int id = query.getInt(1);
                String username = query.getString(2);
                String password = query.getString(3);
                String email = query.getString(4);
                boolean gender = !query.getString(5).equals("Male");
                CustomDate bDate = new CustomDate(query.getString(6));
                String country = query.getString(7);
                String zip = query.getString(8);
                boolean isPremium = findPremiumId(id);
                users.add(new User(id, username, password, email, gender, bDate, country, zip, isPremium));
            }
        } catch (SQLException e) {
            System.out.println("Error loading users.");
        }
    }

    // Loads every artist from database.
    public static void initArtistsFromDatabase() {
        artists = new ArrayList<>();
        try {
            ResultSet query = st.executeQuery("select * from artista;");
            while (query.next()) {
                int id = query.getInt(1);
                String name = query.getString(2);
                String imgPath = query.getString(3);
                int bit = query.getInt(4);
                boolean sponsored;
                if (bit == 0) sponsored = false;
                else sponsored = true;
                artists.add(new Artist(id, name, imgPath, sponsored));
            }
        } catch (SQLException e) {
            System.out.println("Error loading artists.");
        }
    }

    // Loads every album from database.
    public static void initAlbumsFromDatabase() {
        albums = new ArrayList<>();
        try {
            ResultSet query = st.executeQuery("select * from album;");
            while (query.next()) {
                int id = query.getInt(1);
                String[] year = query.getString(2).split("-");
                String name = query.getString(3);
                String imgPath = query.getString(4);
                boolean sponsored = query.getBoolean(5);
                CustomDate sponsorStartDate, sponsorEndDate;
                if (sponsored) {
                    sponsorStartDate = new CustomDate(query.getString(6));
                    sponsorEndDate = new CustomDate(query.getString(7));
                } else {
                    sponsorStartDate = new CustomDate("0-0-0");
                    sponsorEndDate = new CustomDate("0-0-0");
                }
                Artist artist = findArtistById(query.getInt(8));

                albums.add(new Album(id, year[0], name, imgPath, sponsored, sponsorStartDate, sponsorEndDate, artist));
            }
        } catch (SQLException e) {
            System.out.println("Error loading albums;");
        }
    }

    public static void initFollowedArtistsFromDatabase() {
        followedArtists = new ArrayList<>();
        for (Artist artist : artists) {
            try {
                ResultSet query = SpotifyController.st.executeQuery("select artista_id from sigue_artista\n" +
                        "where usuario_id=" + user.getId() + " and artista_id=+" + artist.getId() + ";");
                while (query.next()) {
                    followedArtists.add(findArtistById(query.getInt(1)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void initFollowedAlbumsFromDatabase() {
        followedAlbums = new ArrayList<>();
        for (Album album : albums) {
            try {
                ResultSet query = st.executeQuery("select album_id from sigue_album\n" +
                        "where usuario_id=" + user.getId() + " and album_id=" + album.getId() + ";");
                while (query.next()) {
                    followedAlbums.add(findAlbumById(query.getInt(1)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void initFollowedSongsFromDatabase() {
        followedSongs = new ArrayList<>();
        for (Song song : songs) {
            try {
                ResultSet query = SpotifyController.st.executeQuery("select cancion_id from guarda_cancion\n" +
                        "where usuario_id=" + user.getId() + " and cancion_id=+" + song.getId() + ";");
                while (query.next()) {
                    followedSongs.add(findSongById(query.getInt(1)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void initFollowedPlaylistsFromDatabase(){
        followedPlaylists = new ArrayList<>();
        for (Active playlist : activePlaylists){
            try {
                ResultSet query = st.executeQuery("select playlist_id from sigue_playlist " +
                        "where usuario_id=" + user.getId() +" and playlist_id="+playlist.getId());
                while (query.next()){
                    followedPlaylists.add(findActivePlaylistById(query.getInt(1)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Loads every song from database.
    public static void initSongsFromDatabase() {
        songs = new ArrayList<>();
        try {
            ResultSet query = st.executeQuery("select * from cancion");
            while (query.next()) {
                int id = query.getInt(1);
                String name = query.getString(2);
                CustomDuration duration = new CustomDuration(query.getString(3));
                String path = query.getString(4);
                int timesListened = query.getInt(5);
                Album album = findAlbumById(query.getInt(6));
                Artist artist = album.getArtist();

                Song song = new Song(id, name, duration, path, timesListened, album, artist);
                songs.add(song);
            }
        } catch (SQLException e) {
            System.out.println("Error loading songs;");
        }
    }

    // Sets collab artist to every song from database.
    public static void initCollabsFromDatabase() {
        try {
            for (Song song : songs) {
                ResultSet query = st.executeQuery("select artista_colaborador_id from colaboracion_artistica where cancion_id=" + song.getId() + ";");
                if (query.next()) {
                    int collabId = query.getInt(1);
                    Artist collaber = findArtistById(collabId);
                    song.setCollabArtist(collaber);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error loading collabs;");
        }
    }

    // Displays an ad if user is not premium.
    private void displayAd() {
        if (user.isPremium()) {
            ad.setVisible(false);
            ad.setFitHeight(1);
            ad.setFitWidth(1);
            mainContainer.setAlignment(Pos.TOP_LEFT);
        } else {
            ad.setImage(getRandomAd());
        }
    }

    // @favoritePane content
    private void displayAlbumsInPane() {
        try {
            for (Album album : albums) {
                if (album.isSponsored()) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("album.fxml"));

                    VBox vBox = fxmlLoader.load();
                    AlbumController albumController = fxmlLoader.getController();
                    albumController.createAlbumItem(album);

                    favoriteContainer.getChildren().add(vBox);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // @artistPane content
    private void displayArtistsInPane() {
        try {
            for (Artist artist : artists) {
                if (artist.isSponsored()) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("artist.fxml"));

                    HBox hBox = fxmlLoader.load();
                    ArtistController artistController = fxmlLoader.getController();
                    artistController.createArtistItem(artist);

                    artistContainer.getChildren().add(hBox);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayPlaylistInListPane() {
        try {
            ResultSet query = st.executeQuery("select a.playlist_id from activa a inner join playlist p on p.id = a.playlist_id where usuario_id=" + user.getId() + ";");
            while (query.next()) {
                Active playlist = findActivePlaylistById(query.getInt(1));
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("playlistinlist.fxml"));

                HBox hBox = fxmlLoader.load();
                PlaylistInListController playlistInListController = fxmlLoader.getController();
                playlistInListController.createPlaylistInListItem(playlist);

                playlistContainer.getChildren().add(hBox);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    // Sets the following properties to @favoritePane & @artistPane
    private void setPanesProperties() {
        favoritePane.setPannable(true);
        favoritePane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        artistPane.setPannable(true);
        artistPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        playlistPane.setPannable(true);
        playlistPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        playlistPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    private Image getRandomAd() {
        final int ADS = 4; // Amount of ads available in /icons/ads

        try {
            Image img;
            int random = (int) (Math.random() * ADS) + 1;
            adNumber = random;
            String path = "icons/ads/" + random + ".jpg";
            img = new Image(getClass().getResource(path).toExternalForm());
            return img;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addPlaylistToList(Active active){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("playlistinlist.fxml"));

            HBox hBox = fxmlLoader.load();
            PlaylistInListController playlistInListController = fxmlLoader.getController();
            playlistInListController.createPlaylistInListItem(active);

            playlistContainer.getChildren().add(hBox);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // ――――――――――― SpotifyController Utils ―――――――――――


    public static Artist findArtistByName(String name) {
        for (Artist artist : artists) {
            if (artist.getName().equalsIgnoreCase(name)) return artist;
        }
        return null;
    }

    public static Artist findArtistById(int id) {
        for (Artist artist : artists) {
            if (artist.getId() == id) return artist;
        }
        return null;
    }

    public static Album findAlbumById(int id) {
        for (Album album : albums) {
            if (album.getId() == id) return album;
        }
        return null;
    }

    public static Album findAlbumByName(String name) {
        for (Album album : albums) {
            if (album.getName().equalsIgnoreCase(name)) return album;
        }
        return null;
    }

    public static Song findSongByName(String name) {
        for (Song song : songs) {
            if (song.getName().equalsIgnoreCase(name)) return song;
        }
        return null;
    }

    public static Song findSongById(int id) {
        for (Song song : songs) {
            if (song.getId() == id) return song;
        }
        return null;
    }

    public static User findUserByName(String name) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(name)) return user;
        }
        return null;
    }

    public static User findUserById(int id) {
        for (User user : users) {
            if (user.getId() == id) return user;
        }
        return null;
    }

    public static boolean findPremiumId(int id) {
        for (int ID : premiumIDs) {
            if (ID == id) return true;
        }
        return false;
    }

    public static boolean findSponsoredId(int id) {
        for (int ID : sponsoredPlaylistIDs) {
            if (ID == id) return true;
        }
        return false;
    }

    public static Active findActivePlaylistById(int id) {
        for (Active active : activePlaylists) {
            if (active.getId() == id) return active;
        }
        return null;
    }

    public static Active findActivePlaylistByTitle(String name) {
        for (Active active : activePlaylists) {
            if (active.getTitle().equalsIgnoreCase(name)) return active;
        }
        return null;
    }

    public static int findSongIndexInPlaylist(Song song, Active active){
        for (int i = 0; i < active.getSongs().size(); i++){
            if (active.getSongs().get(i).getId()==song.getId()) return i;
        }
        return -1;
    }

    // ――――――――――― SpotifyController Getters and Setters ―――――――――――

    public static List<Song> getSongs() {
        return songs;
    }

    public static List<Artist> getArtists() {
        return artists;
    }

    public static List<Album> getAlbums() {
        return albums;
    }

    public static User getUser() {
        return user;
    }

    public static List<Artist> getFollowedArtists() {
        return followedArtists;
    }

    public static List<Song> getFollowedSongs() {
        return followedSongs;
    }

    public static List<Album> getFollowedAlbums() {
        return followedAlbums;
    }

    public static List<User> getUsers() {
        return users;
    }

    public static List<Integer> getPremiumIDs() {
        return premiumIDs;
    }

    public static void setUser(User user) {
        SpotifyController.user = user;
    }

    public static void setSt(Statement st) {
        SpotifyController.st = st;
    }

    public static List<Active> getActivePlaylists() {
        return activePlaylists;
    }

    public static List<Active> getFollowedPlaylists() {
        return followedPlaylists;
    }

    // ――――――――――― SpotifyController Events ―――――――――――

    public void onAdClick(MouseEvent mouseEvent) throws IOException {
        if (adNumber == 2) Spotify.runAlbumView(findAlbumByName("Familia"));
    }

    public void onEnterPressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            Spotify.runSearchView(searchField.getText());
        }
    }

    public void onGoBackClick(MouseEvent mouseEvent) throws IOException {
        Action.goBack();
    }

    public void onUpgradeClick(MouseEvent mouseEvent) throws IOException {
        Spotify.displayPayment();
    }

    public void updateSong(Song song) {
        Image songImg;
        try {
            songImg = new Image(getClass().getResource(song.getAlbum().getImgPath()).toExternalForm());
        } catch (NullPointerException e) {
            songImg = new Image(getClass().getResource("icons/albums/placeholder.png").toExternalForm());
        }
        nowListeningImage.setImage(songImg);
        nowListeningName.setText(song.getName());
    }

    public void onLibraryClick(MouseEvent mouseEvent) throws IOException {
        Spotify.runLibraryView();
    }

    public void onLibraryEntered(MouseEvent mouseEvent) {
        libraryHbox.getStyleClass().add("selected");
    }

    public void onLibraryExited(MouseEvent mouseEvent) {
        libraryHbox.getStyleClass().remove("selected");
    }

    public void onLogOutClick(MouseEvent mouseEvent) throws IOException {
        Spotify.logOutPress();
    }

    public void onMouseEnteredPlaylistLabel(MouseEvent mouseEvent) {
        playlistLabel.setTextFill(Color.rgb(30,215,96));
        playlistLabel.setText("•  Create  •");
    }

    public void onMouseExitedPlaylistLabel(MouseEvent mouseEvent) {
        playlistLabel.setTextFill(Color.rgb(161,161,161));
        playlistLabel.setText("•  Playlists  •");
    }

    public void onCreatePlaylistClick(MouseEvent mouseEvent) {
        Spotify.displayCreatePlaylist();
    }
}