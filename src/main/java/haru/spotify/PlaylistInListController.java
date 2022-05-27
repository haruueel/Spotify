package haru.spotify;

import haru.spotify.model.Active;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.IOException;

public class PlaylistInListController {
    public Label nameLabel;

    public void createPlaylistInListItem(Active playlist){
        nameLabel.setText(playlist.getTitle());
    }

    public void onMouseClickRedirect(MouseEvent mouseEvent) throws IOException {
        Spotify.runPlaylistView(SpotifyController.findActivePlaylistByTitle(nameLabel.getText()));
    }

    public void onMouseEnteredNameLabel(MouseEvent mouseEvent) {
        nameLabel.setTextFill(Color.rgb(30,215,96));
    }

    public void onMouseExitedNameLabel(MouseEvent mouseEvent) {
        nameLabel.setTextFill(Color.rgb(161,161,161));
    }
}
