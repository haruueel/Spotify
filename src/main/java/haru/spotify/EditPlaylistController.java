package haru.spotify;

import haru.spotify.model.Active;
import haru.spotify.model.View;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditPlaylistController {

    public TextField nameField;
    public CheckBox collaborativeCheckbox;

    private static Active playlist;

    public void setStuff() {
        nameField.setText(playlist.getTitle());
        collaborativeCheckbox.setSelected(playlist.isCollaborative());
    }
    public static void setPlaylist(Active playlist) {
        EditPlaylistController.playlist = playlist;
    }

    public void onSaveChangesClick(MouseEvent mouseEvent) {
        String newTitle = nameField.getText();
        boolean isCollaborative = collaborativeCheckbox.isSelected();

        playlist.setTitle(newTitle);
        playlist.setCollaborative(isCollaborative);

        int bit;
        if (isCollaborative) bit = 1;
        else bit = 0;
        try{
            SpotifyController.st.executeUpdate("update playlist set titulo = '"+newTitle+"' where id = "+playlist.getId()+";");
            SpotifyController.st.executeUpdate("update activa set es_compartida = "+bit+" where playlist_id = "+playlist.getId()+";");

            PlaylistViewController playlistViewController = View.fxmlLoader.getController();
            playlistViewController.playlistNameLabel.setText(newTitle);

            Spotify.closeEditPlaylist();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
