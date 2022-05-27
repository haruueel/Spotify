package haru.spotify;

import haru.spotify.model.Artist;
import haru.spotify.model.FollowingSystem;
import haru.spotify.model.Listening;
import haru.spotify.model.Song;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.IOException;

public class SongInAlbumController {
    public Label nameLabel;
    public Label artistLabel;
    public Label durationLabel;
    public Label timesListenedLabel;
    public ImageView followIcon;

    public void createSongInAlbumItem(Song song){
        nameLabel.setText(song.getName());
        artistLabel.setText(song.getArtist().getName());
        durationLabel.setText(song.getDuration().toString());
        timesListenedLabel.setText(String.valueOf(song.getTimesListened()));

        if (SpotifyController.getUser().isFollowingSong(song))
            followIcon.setImage(new Image(getClass().getResourceAsStream(FollowingSystem.FOLLOWINGICON)));

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
}
