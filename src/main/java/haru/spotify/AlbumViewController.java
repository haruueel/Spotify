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

public class AlbumViewController implements Initializable {

    public Button upgradeButton;
    public ImageView nowListeningImage;
    public Label nowListeningName;
    public HBox homeHbox;
    public HBox libraryHbox;
    public Label usernameLabel;
    public Label albumTypeLabel;
    public ImageView icon;
    public ImageView followIcon;
    public ImageView albumIcon;
    public ImageView artistIcon;
    public Label albumNameLabel;
    public Label artistNameLabel;
    public Label yearLabel;
    public Label numberOfSongsLabel;
    public Label playlistLabel;
    public ScrollPane songsPane;
    public ScrollPane playlistPane;
    public VBox songsContainer;
    public TextField searchField;
    public VBox playlistContainer;

    private static Album album;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(SpotifyController.getUser().isPremium()) upgradeButton.setVisible(false);
        Image albumImg, artistImg;
        try {
            albumImg = new Image(getClass().getResource(album.getImgPath()).toExternalForm());
        } catch (NullPointerException e){
            albumImg = new Image(getClass().getResource("icons/albums/placeholder.png").toExternalForm());
        }

        try {
            artistImg = new Image(getClass().getResource(album.getArtist().getImgPath()).toExternalForm());
        } catch (NullPointerException e){
            artistImg = new Image(getClass().getResource("icons/artists/placeholder.png").toExternalForm());
        }

        usernameLabel.setText(SpotifyController.getUser().getUsername());
        albumIcon.setImage(albumImg);
        albumNameLabel.setText(album.getName());
        artistIcon.setImage(artistImg);
        artistNameLabel.setText(album.getArtist().getName());
        yearLabel.setText("·  "+album.getYear());
        if (album.getSongs().size()>1) numberOfSongsLabel.setText("·  "+album.getSongs().size()+" songs");
        else {
            numberOfSongsLabel.setText("·  "+album.getSongs().size()+" song");
            albumTypeLabel.setText("S I N G L E");
            icon.setImage(new Image(getClass().getResourceAsStream("icons/white_equalizer.png")));
        }
        if(SpotifyController.getUser().isFollowingAlbum(album))
            followIcon.setImage(new Image(getClass().getResourceAsStream(FollowingSystem.FOLLOWINGICON)));

        displaySongsInPane();
        displayPlaylistInListPane();
        setPanesProperties();
        updateSong(Listening.getSong());
    }

    // @songsContainer content
    private void displaySongsInPane(){
        try{
            for (Song song : album.getSongs()){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("songinalbum.fxml"));

                HBox hBox = fxmlLoader.load();
                SongInAlbumController songInAlbumController = fxmlLoader.getController();
                songInAlbumController.createSongInAlbumItem(song);

                songsContainer.getChildren().add(hBox);
            }
        } catch (Exception e){
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

                playlistContainer.getChildren().add(hBox);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
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

    // Sets the following properties to @songsPane
    private void setPanesProperties(){
        songsPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        playlistPane.setPannable(true);
        playlistPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        playlistPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }
    public static void setAlbum(Album album){
        AlbumViewController.album = album;
    }

    // ――――――――――― AlbumViewController Events ―――――――――――

    public void onHomeClick(MouseEvent mouseEvent) throws IOException {
        Spotify.runMainView();
    }

    public void onHomeMouseEntered(MouseEvent mouseEvent) {
        homeHbox.getStyleClass().add("selected");
    }

    public void onHomeMouseExited(MouseEvent mouseEvent) {
        homeHbox.getStyleClass().remove("selected");
    }

    public void onArtistNameMouseEntered(MouseEvent mouseEvent) {
        artistNameLabel.setTextFill(Color.rgb(30,215,96));
    }

    public void onArtistNameMouseExited(MouseEvent mouseEvent) {
        artistNameLabel.setTextFill(Color.rgb(255,255,255));
    }

    public void onArtistNameClickRedirect(MouseEvent mouseEvent) throws IOException {
        Artist selected = album.getArtist();
        Spotify.runArtistView(selected);
    }

    public void onEnterPressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            Spotify.runSearchView(searchField.getText());
        }
    }

    public void onFollowIconClick(MouseEvent mouseEvent) {
        if(SpotifyController.getUser().isFollowingAlbum(album)){
            followIcon.setImage(new Image(getClass().getResourceAsStream(FollowingSystem.FOLLOWABLEICON)));
            FollowingSystem.unfollowAlbum(album);
        } else {
            followIcon.setImage(new Image(getClass().getResourceAsStream(FollowingSystem.FOLLOWINGICON)));
            FollowingSystem.followAlbum(album);
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
