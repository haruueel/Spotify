package haru.spotify;

import haru.spotify.model.*;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreatePlaylistController {

    public TextField nameField;

    public void onEnterPressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            try {
                SpotifyController.st.executeUpdate("insert into playlist(titulo,numero_canciones,fecha_creacion,usuario_id) " +
                        "values ('" + nameField.getText() + "',0,now()," + SpotifyController.getUser().getId() + ");");
                ResultSet query = SpotifyController.st.executeQuery("select p.id, p.titulo, p.fecha_creacion, p.usuario_id, a.es_compartida " +
                        "from playlist p inner join activa a on a.playlist_id = p.id where p.titulo like '"+nameField.getText()+"';");
                while (query.next()) {
                    int id = query.getInt(1);
                    String title = query.getString(2);
                    CustomDate creationDate = new CustomDate(query.getString(3));
                    User user = SpotifyController.findUserById(query.getInt(4));
                    boolean isSponsored = SpotifyController.findSponsoredId(id);
                    int bit = query.getInt(5);
                    boolean isCollaborative = bit != 0;

                    Active newPlaylist = new Active(id, title, creationDate, user, isSponsored, isCollaborative);
                    SpotifyController.getActivePlaylists().add(newPlaylist);

                    if (View.view.equals("mainview")){
                        SpotifyController spotifyController = View.fxmlLoader.getController();
                        spotifyController.addPlaylistToList(newPlaylist);
                    }

                    if (View.view.equals("artistview")){
                        ArtistViewController artistViewController = View.fxmlLoader.getController();
                        artistViewController.addPlaylistToList(newPlaylist);
                    }

                    if (View.view.equals("albumview")){
                        AlbumViewController albumViewController = View.fxmlLoader.getController();
                        albumViewController.addPlaylistToList(newPlaylist);
                    }

                    if (View.view.equals("libraryview")){
                        LibraryViewController libraryViewController = View.fxmlLoader.getController();
                        libraryViewController.addPlaylistToList(newPlaylist);
                    }

                    if (View.view.equals("searchview")){
                        SearchViewController searchViewController = View.fxmlLoader.getController();
                        searchViewController.addPlaylistToList(newPlaylist);
                    }
                }
                Spotify.closeCreatePlaylist();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
}
