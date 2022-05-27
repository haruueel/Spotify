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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ArtistViewController implements Initializable {

    public Button upgradeButton;
    public ImageView nowListeningImage;
    public Label nowListeningName;
    public ImageView followIcon;
    public ImageView artistIcon;
    public Label playlistLabel;
    public Label usernameLabel;
    public Label artistNameLabel;
    public Label songsTitleLabel;
    public Label albumsTitleLabel;
    public ScrollPane mainPane;
    public ScrollPane albumPane;
    public ScrollPane songPane;
    public ScrollPane playlistPane;
    public HBox homeHbox;
    public HBox libraryHbox;
    public HBox songContainer;
    public HBox albumContainer;
    public TextField searchField;
    public VBox playlistContainer;


    private static Artist artist;
    private static List<Song> songs;
    private static List<Album> albums;
    private boolean foundAnyCollabs;
    private boolean foundAnyAlbums;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(SpotifyController.getUser().isPremium()) upgradeButton.setVisible(false);
        Image img;
        try {
            img = new Image(getClass().getResource(artist.getImgPath()).toExternalForm());
        } catch (NullPointerException e){
            img = new Image(getClass().getResourceAsStream("icons/artists/placeholder.png"));
        }
        usernameLabel.setText(SpotifyController.getUser().getUsername());
        artistIcon.setImage(img);
        artistNameLabel.setText(artist.getName());

        songs = new ArrayList<>(SpotifyController.getSongs());
        albums = new ArrayList<>(SpotifyController.getAlbums());

        if (SpotifyController.getUser().isFollowingArtist(artist))
            followIcon.setImage(new Image(getClass().getResourceAsStream(FollowingSystem.FOLLOWINGICON)));

        foundAnyCollabs = false;
        foundAnyAlbums = false;
        displaySongsInPane();
        displayAlbumsInPane();
        displayPlaylistInListPane();
        setPanesProperties();
        updateSong(Listening.getSong());

        if (!foundAnyAlbums)  {
            albumPane.setMaxHeight(1);
            albumPane.setMaxWidth(1);
            albumPane.setVisible(false);
        }
        if(foundAnyCollabs) songsTitleLabel.setText(artist.getName()+"'s collabs");
        else songsTitleLabel.setVisible(false);
        albumsTitleLabel.setText(artist.getName()+"'s albums");
    }

    // @albumPane content
    private void displayAlbumsInPane(){
        try {
            for (Album album : albums){
                if (album.getArtist().getName().equals(artist.getName())){
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("album.fxml"));

                    VBox vBox = fxmlLoader.load();
                    AlbumController albumController = fxmlLoader.getController();
                    albumController.createAlbumItem(album);

                    albumContainer.getChildren().add(vBox);
                    foundAnyAlbums = true;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    // @songPane content
    private void displaySongsInPane(){
        try {
            for (Song song : songs){
                if (song.isCollaborative()){
                    if (song.getCollabArtist().getName().equals(artist.getName())){
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource("song.fxml"));

                        VBox vBox = fxmlLoader.load();
                        SongController songController = fxmlLoader.getController();
                        songController.createSongItem(song);

                        songContainer.getChildren().add(vBox);
                        foundAnyCollabs = true;
                    }
                }
            }
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

                playlistContainer.getChildren().add(hBox);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    // Sets the following properties to @songPane, @albumPane & @mainPane
    private void setPanesProperties(){
        mainPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        mainPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        albumPane.setPannable(true);
        albumPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        songPane.setPannable(true);
        songPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        playlistPane.setPannable(true);
        playlistPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        playlistPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    public static void setArtist(Artist artist) {
        ArtistViewController.artist = artist;
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

    // ――――――――――― ArtistViewController Events ―――――――――――

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

    public void onFollowIconClick(MouseEvent actionEvent) {
        if(SpotifyController.getUser().isFollowingArtist(artist)){
            followIcon.setImage(new Image(getClass().getResourceAsStream(FollowingSystem.FOLLOWABLEICON)));
            FollowingSystem.unfollowArtist(artist);
        } else {
            followIcon.setImage(new Image(getClass().getResourceAsStream(FollowingSystem.FOLLOWINGICON)));
            FollowingSystem.followArtist(artist);
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
