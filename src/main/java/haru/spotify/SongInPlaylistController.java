package haru.spotify;

import haru.spotify.model.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SongInPlaylistController {

    public Label nameLabel;
    public Label artistLabel;
    public Label durationLabel;
    public Label timesListenedLabel;
    public Label dateLabel;
    public ImageView followIcon;
    public ImageView removeIcon;
    public Song thisSong;
    public Active fromPlaylist;

    public void createSongInPlaylistItem(Song song, Active active){
        nameLabel.setText(song.getName());
        artistLabel.setText(song.getArtist().getName());
        durationLabel.setText(song.getDuration().toString());
        timesListenedLabel.setText(String.valueOf(song.getTimesListened()));
        dateLabel.setText(active.getCreationDate().getFullDate());
        thisSong = song;
        fromPlaylist = active;

        if (SpotifyController.getUser().isFollowingSong(song))
            followIcon.setImage(new Image(getClass().getResourceAsStream(FollowingSystem.FOLLOWINGICON)));

        if (!PlaylistViewController.isEditable()) removeIcon.setVisible(false);
    }

    // ――――――――――― SongController Events ―――――――――――

    public void onArtistLabelMouseEntered(MouseEvent mouseEvent) {
        artistLabel.setTextFill(Color.rgb(30,215,96));
    }

    public void onArtistLabelMouseExited(MouseEvent mouseEvent) {
        artistLabel.setTextFill(Color.rgb(115,115,115));
    }

    public void onArtistLabelClickRedirect(MouseEvent mouseEvent) throws IOException {
        Artist selected = SpotifyController.findArtistByName(artistLabel.getText());
        Spotify.runArtistView(selected);
    }

    public void onFollowIconClick(MouseEvent actionEvent) {
        if(SpotifyController.getUser().isFollowingSong(SpotifyController.findSongByName(nameLabel.getText()))){
            followIcon.setImage(new Image(getClass().getResourceAsStream(FollowingSystem.FOLLOWABLEICON)));
            FollowingSystem.unfollowSong(SpotifyController.findSongByName(nameLabel.getText()));
        } else {
            followIcon.setImage(new Image(getClass().getResourceAsStream(FollowingSystem.FOLLOWINGICON)));
            FollowingSystem.followSong(SpotifyController.findSongByName(nameLabel.getText()));
        }
    }

    public void onPlaySongClick(MouseEvent mouseEvent) {
        Listening.setSong(SpotifyController.findSongByName(nameLabel.getText()));
    }

    public void onRemoveClick(MouseEvent mouseEvent) {
        PlaylistViewController playlistViewController = View.fxmlLoader.getController();
        int i = SpotifyController.findSongIndexInPlaylist(thisSong,fromPlaylist);
        fromPlaylist.getSongs().remove(i);
        playlistViewController.songsContainer.getChildren().remove(i);
        try {
            SpotifyController.st.executeUpdate("delete from anyade_cancion_playlist where playlist_id = " + fromPlaylist.getId() + " and cancion_id = " + thisSong.getId());
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
