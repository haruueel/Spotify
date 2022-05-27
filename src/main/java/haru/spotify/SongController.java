package haru.spotify;

import haru.spotify.model.Album;
import haru.spotify.model.Artist;
import haru.spotify.model.Listening;
import haru.spotify.model.Song;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;

public class SongController {

    public VBox container;
    @FXML
    private ImageView img;
    @FXML
    private Label nameLabel;
    @FXML
    private Label artistLabel;

    public void createSongItem(Song song){
        Image image;
        try {
            image = new Image(getClass().getResourceAsStream(song.getAlbum().getImgPath()));
        } catch (NullPointerException e) {
            image = new Image(getClass().getResourceAsStream("icons/albums/placeholder.png"));
        }
        img.setImage(image);
        nameLabel.setText(song.getName());
        if (song.getCollabArtist()==null) {
            artistLabel.setText(song.getArtist().getName());
        } else artistLabel.setText(song.getArtist().getName()+", "+song.getCollabArtist().getName());
    }

    // ――――――――――― SongController Events ―――――――――――

    public void onMouseEntered(MouseEvent mouseEvent) {
        container.setBackground(new Background(new BackgroundFill(Color.rgb(46, 46, 46), new CornerRadii(10), Insets.EMPTY)));
    }

    public void onMouseExited(MouseEvent mouseEvent){
        container.setBackground(new Background(new BackgroundFill(Color.rgb(31, 31, 31), new CornerRadii(10), Insets.EMPTY)));
    }

    public void onMouseEnteredNameLabel(MouseEvent mouseEvent) {
        nameLabel.setTextFill(Color.rgb(30,215,96));
    }

    public void onMouseExitedNameLabel(MouseEvent mouseEvent) {
        nameLabel.setTextFill(Color.rgb(255,255,255));
    }

    public void onMouseEnteredArtistLabel(MouseEvent mouseEvent) {
        artistLabel.setTextFill(Color.rgb(255,255,255));
    }

    public void onMouseExitedArtistLabel(MouseEvent mouseEvent) {
        artistLabel.setTextFill(Color.rgb(161,161,161));
    }

    public void onArtistLabelClickRedirect(MouseEvent mouseEvent) throws IOException {
        Artist selected = SpotifyController.findSongByName(nameLabel.getText()).getArtist();
        Spotify.runArtistView(selected);
    }

    public void onAlbumLogoClickRedirect(MouseEvent mouseEvent) throws IOException {
        Album album = SpotifyController.findSongByName(nameLabel.getText()).getAlbum();
        Spotify.runAlbumView(album);
    }

    public void onPlaySongClick(MouseEvent mouseEvent) {
        Listening.setSong(SpotifyController.findSongByName(nameLabel.getText()));
    }
}
