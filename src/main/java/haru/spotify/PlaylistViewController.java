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

public class PlaylistViewController implements Initializable {

    public Button upgradeButton;
    public ImageView nowListeningImage;
    public Label nowListeningName;
    public HBox homeHbox;
    public HBox libraryHbox;
    public Label usernameLabel;
    public ImageView followIcon;
    public ImageView playlistIcon;
    public ImageView editIcon;
    public ImageView addIcon;
    public Label playlistNameLabel;
    public Label creatorNameLabel;
    public Label dateLabel;
    public Label numberOfSongsLabel;
    public Label playlistLabel;
    public ScrollPane songsPane;
    public ScrollPane playlistPane;
    public VBox songsContainer;
    public TextField searchField;
    public VBox playlistContainer;

    private static Active playlist;
    private static boolean editable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(SpotifyController.getUser().isPremium()) upgradeButton.setVisible(false);

        usernameLabel.setText(SpotifyController.getUser().getUsername());
        playlistNameLabel.setText(playlist.getTitle());
        creatorNameLabel.setText(playlist.getCreator().getUsername());
        dateLabel.setText("·  "+playlist.getCreationDate().getFullDate());
        numberOfSongsLabel.setText("·  "+playlist.getSongs().size()+" songs");

        if(SpotifyController.getUser().isFollowingPlaylist(playlist))
            followIcon.setImage(new Image(getClass().getResourceAsStream(FollowingSystem.FOLLOWINGICON)));

        if(playlist.getCreator().getId()!=SpotifyController.getUser().getId() && !playlist.isCollaborative()){
            makeNoEditable();
            editIcon.setVisible(false);
            addIcon.setVisible(false);
        } else makeEditable();

        displaySongsInPane();
        displayPlaylistInListPane();
        setPanesProperties();
        updateSong(Listening.getSong());
    }

    // @songsContainer content
    private void displaySongsInPane(){
        try{
            for (Song song : playlist.getSongs()){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("songinplaylist.fxml"));

                HBox hBox = fxmlLoader.load();
                SongInPlaylistController songInPlaylistController = fxmlLoader.getController();
                songInPlaylistController.createSongInPlaylistItem(song,playlist);
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

    // todo
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
    public static void setPlaylist(Active playlist){
        PlaylistViewController.playlist = playlist;
    }

    public static boolean isEditable() {
        return editable;
    }

    public static void makeNoEditable(){
        editable = false;
    }

    public static void makeEditable(){
        editable = true;
    }

    // ――――――――――― PlaylistViewController Events ―――――――――――

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

    public void onFollowIconClick(MouseEvent mouseEvent) {
        if(SpotifyController.getUser().isFollowingPlaylist(playlist)){
            followIcon.setImage(new Image(getClass().getResourceAsStream(FollowingSystem.FOLLOWABLEICON)));
            FollowingSystem.unfollowPlaylist(playlist);
        } else {
            followIcon.setImage(new Image(getClass().getResourceAsStream(FollowingSystem.FOLLOWINGICON)));
            FollowingSystem.followPlaylist(playlist);
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

    public void onEditClick(MouseEvent mouseEvent) {
        Spotify.displayEditPlaylist(playlist);
    }

    public void onAddClick(MouseEvent mouseEvent) {
        Spotify.displayAddSong(playlist);
    }
}

