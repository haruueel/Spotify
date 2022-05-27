package haru.spotify;

import haru.spotify.model.Active;
import haru.spotify.model.Song;
import haru.spotify.model.View;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.sql.SQLException;

public class AddSongController {

    public TextField songField;
    public Label songName;
    public Label artistName;
    public Button addButton;

    private static Active playlist;
    private static Song song;
    private static boolean foundAnySong;

    public static void setPlaylist(Active playlist) {
        AddSongController.playlist = playlist;
        foundAnySong = false;
    }

    public void onAddClick(MouseEvent mouseEvent) throws IOException {
        if(foundAnySong) {
            try {
                PlaylistViewController playlistViewController = View.fxmlLoader.getController();
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("songinplaylist.fxml"));

                HBox hBox = fxmlLoader.load();
                SongInPlaylistController songInPlaylistController = fxmlLoader.getController();
                songInPlaylistController.createSongInPlaylistItem(song, playlist);
                playlistViewController.songsContainer.getChildren().add(hBox);

                playlist.getSongs().add(song);
                SpotifyController.st.executeUpdate("insert into anyade_cancion_playlist values (" + playlist.getId() + "," + song.getId() + "," + SpotifyController.getUser().getId() + ",now());");
            } catch (SQLException e){
                e.printStackTrace();
            }
            Spotify.closeAddSong();
        }
    }

    public void onSearchClick(MouseEvent mouseEvent) {
        Song song = SpotifyController.findSongByName(songField.getText());
        if(song!=null){
            songName.setText(song.getName());
            artistName.setText(song.getArtist().getName());
            AddSongController.song = song;
            foundAnySong = true;
            addButton.setBackground(new Background(new BackgroundFill(Color.rgb(30,215,96), new CornerRadii(100), Insets.EMPTY)));
        } else {
            songName.setText("None");
            artistName.setText("Nobody");
            foundAnySong = false;
            addButton.setBackground(new Background(new BackgroundFill(Color.rgb(215, 30, 52), new CornerRadii(100), Insets.EMPTY)));
        }
    }
}
