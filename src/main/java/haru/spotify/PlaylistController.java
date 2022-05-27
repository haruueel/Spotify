package haru.spotify;

import haru.spotify.model.Active;
import haru.spotify.model.Album;
import haru.spotify.model.Artist;
import haru.spotify.model.Listening;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;

public class PlaylistController {
    public Label nameLabel;
    public VBox container;

    public void createPlaylistItem(Active playlist){
        nameLabel.setText(playlist.getTitle());
    }

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

    public void onClickRedirect(MouseEvent mouseEvent) throws IOException {
        Spotify.runPlaylistView(SpotifyController.findActivePlaylistByTitle(nameLabel.getText()));
    }
}
