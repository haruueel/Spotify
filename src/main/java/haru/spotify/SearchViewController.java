package haru.spotify;

import haru.spotify.model.*;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SearchViewController implements Initializable {

    public Button upgradeButton;
    public HBox homeHbox;
    public HBox libraryHbox;
    public HBox artistContainer;
    public HBox albumContainer;
    public HBox songContainer;
    public HBox playlistContainer;
    public Label usernameLabel;
    public Label resultStringLabel;
    public TextField searchField;
    public ScrollPane mainPane;
    public ScrollPane artistPane;
    public ScrollPane albumPane;
    public ScrollPane songPane;
    public ScrollPane playlistPane;
    public ScrollPane sidePlaylistPane;
    public ImageView nowListeningImage;
    public Label nowListeningName;
    public Label playlistLabel;
    public VBox sidePlaylistContainer;

    private static String search;
    private static boolean foundAnyArtist;
    private static boolean foundAnyAlbum;
    private static boolean foundAnySong;
    private static boolean foundAnyPlaylist;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(SpotifyController.getUser().isPremium()) upgradeButton.setVisible(false);
        usernameLabel.setText(SpotifyController.getUser().getUsername());
        resultStringLabel.setText("Search results for \""+search+"\":");

        displayArtistsInPane();
        displayAlbumsInPane();
        displaySongsInPane();
        displayPlaylistInPane();
        displayPlaylistInListPane();
        setPanesProperties();
        checkEmptyPanes();
        updateSong(Listening.getSong());
    }

    private void displayArtistsInPane(){
        try {
            ResultSet query = SpotifyController.st.executeQuery("select id from artista where nombre like '%"+search+"%' limit 20;");
            if (query.isBeforeFirst()) {
                while (query.next()) {
                    Artist artist = SpotifyController.findArtistById(query.getInt(1));
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("artist.fxml"));

                    HBox hBox = fxmlLoader.load();
                    ArtistController artistController = fxmlLoader.getController();
                    artistController.createArtistItem(artist);

                    artistContainer.getChildren().add(hBox);
                }
                foundAnyArtist = true;
            } else foundAnyArtist = false;
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void displayAlbumsInPane(){
        try {
            ResultSet query = SpotifyController.st.executeQuery("select id from album where titulo like '%"+search+"%' limit 20;");
            if (query.isBeforeFirst()) {
                while (query.next()) {
                    Album album = SpotifyController.findAlbumById(query.getInt(1));
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("album.fxml"));

                    VBox vBox = fxmlLoader.load();
                    AlbumController albumController = fxmlLoader.getController();
                    albumController.createAlbumItem(album);

                    albumContainer.getChildren().add(vBox);
                } foundAnyAlbum = true;
            }  else foundAnyAlbum = false;

            ResultSet query2 = SpotifyController.st.executeQuery("select a.id from album a\n" +
                    "inner join artista ar on ar.id = a.artista_id\n" +
                    "where ar.nombre like '%"+search+"%' limit 20;");
            if (query2.isBeforeFirst()) {
                while (query2.next()) {
                    Album album = SpotifyController.findAlbumById(query2.getInt(1));
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("album.fxml"));

                    VBox vBox = fxmlLoader.load();
                    AlbumController albumController = fxmlLoader.getController();
                    albumController.createAlbumItem(album);

                    albumContainer.getChildren().add(vBox);
                } foundAnyAlbum = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displaySongsInPane(){
        try {
            ResultSet query = SpotifyController.st.executeQuery("select id from cancion where titulo like '%"+search+"%' limit 20;");
            if (query.isBeforeFirst()) {
                while(query.next()) {
                    Song song = SpotifyController.findSongById(query.getInt(1));
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("song.fxml"));

                    VBox vBox = fxmlLoader.load();
                    SongController songController = fxmlLoader.getController();
                    songController.createSongItem(song);

                    songContainer.getChildren().add(vBox);
                }
                foundAnySong = true;
            } else foundAnySong = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayPlaylistInPane(){
        try{
            ResultSet query = SpotifyController.st.executeQuery("select playlist_id from activa a inner join playlist p " +
                    "on p.id = a.playlist_id where p.titulo like '%"+search+"%' limit 20 ;");
            if (query.isBeforeFirst()){
                while (query.next()){
                    Active active = SpotifyController.findActivePlaylistById(query.getInt(1));
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("playlist.fxml"));

                    VBox vBox = fxmlLoader.load();
                    PlaylistController playlistController = fxmlLoader.getController();
                    playlistController.createPlaylistItem(active);

                    playlistContainer.getChildren().add(vBox);
                }
                foundAnyPlaylist = true;
            } else foundAnyPlaylist = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayPlaylistInListPane() {
        try {
            ResultSet query = SpotifyController.st.executeQuery("select a.playlist_id from activa a inner join playlist p on p.id = a.playlist_id where usuario_id=" + SpotifyController.getUser().getId() + ";");
            while (query.next()) {
                Active playlist = SpotifyController.findActivePlaylistById(query.getInt(1));
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("playlistinlist.fxml"));

                HBox hBox = fxmlLoader.load();
                PlaylistInListController playlistInListController = fxmlLoader.getController();
                playlistInListController.createPlaylistInListItem(playlist);

                sidePlaylistContainer.getChildren().add(hBox);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void checkEmptyPanes(){
        if (!foundAnyArtist) {
            artistPane.setMaxHeight(1);
            artistPane.setMaxWidth(1);
            artistPane.setVisible(false);
        }

        if (!foundAnyAlbum) {
            albumPane.setMaxHeight(1);
            albumPane.setMaxWidth(1);
            albumPane .setVisible(false);
        }

        if (!foundAnySong) {
            songPane.setMaxHeight(1);
            songPane.setMaxWidth(1);
            songPane.setVisible(false);
        }

        if (!foundAnyPlaylist) {
            playlistPane.setMaxHeight(1);
            playlistPane.setMaxWidth(1);
            playlistPane.setVisible(false);
        }
    }
    private void setPanesProperties(){
        mainPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        mainPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        artistPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        artistPane.setPannable(true);
        albumPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        albumPane.setPannable(true);
        songPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        songPane.setPannable(true);
        playlistPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        playlistPane.setPannable(true);

        sidePlaylistPane.setPannable(true);
        sidePlaylistPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sidePlaylistPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    public static void setSearch(String search){
        SearchViewController.search = search;
    }

    public void addPlaylistToList(Active active){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("playlistinlist.fxml"));

            HBox hBox = fxmlLoader.load();
            PlaylistInListController playlistInListController = fxmlLoader.getController();
            playlistInListController.createPlaylistInListItem(active);

            sidePlaylistContainer.getChildren().add(hBox);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // ――――――――――― SearchViewController Events ―――――――――――

    public void onHomeClick(MouseEvent mouseEvent) throws IOException {
        Spotify.runMainView();
    }

    public void onHomeMouseEntered(MouseEvent mouseEvent) {
        homeHbox.getStyleClass().add("selected");
    }

    public void onHomeMouseExited(MouseEvent mouseEvent) {
        homeHbox.getStyleClass().remove("selected");
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

    public void onLibraryClick(MouseEvent mouseEvent) throws IOException {
        Spotify.runLibraryView();
    }

    public void onLibraryEntered(MouseEvent mouseEvent) {
        libraryHbox.getStyleClass().add("selected");
    }

    public void onLibraryExited(MouseEvent mouseEvent) {
        libraryHbox.getStyleClass().remove("selected");
    }

    public void updateSong(Song song){
        Image songImg;
        try {
            songImg = new Image(getClass().getResource(song.getAlbum().getImgPath()).toExternalForm());
        } catch (NullPointerException e){
            songImg = new Image(getClass().getResource("icons/albums/placeholder.png").toExternalForm());
        }
        nowListeningImage.setImage(songImg);
        nowListeningName.setText(song.getName());
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


