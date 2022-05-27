package haru.spotify;

import haru.spotify.model.User;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.IOException;

public class LoginController {
    public TextField passwordField;
    public TextField usernameField;
    public Label usernameLabel;
    public Label passwordLabel;
    public Button loginButton;

    public void onLoginClick(MouseEvent mouseEvent) throws IOException {

        String usernameInput = usernameField.getText();
        String passwordInput = passwordField.getText();

        try {
            String checkUsername = SpotifyController.findUserByName(usernameInput).getUsername();

            User user = SpotifyController.findUserByName(usernameInput);

            usernameLabel.setText("Username");
            usernameLabel.setTextFill(Color.WHITE);

            if (user.getPassword().equals(passwordInput)) {
                SpotifyController.setUser(user);
                SpotifyController.initFollowedArtistsFromDatabase();
                SpotifyController.initFollowedSongsFromDatabase();
                SpotifyController.initFollowedAlbumsFromDatabase();
                SpotifyController.initFollowedPlaylistsFromDatabase();
                Spotify.runMainView();
            } else {
                passwordLabel.setText("Wrong password.");
                passwordLabel.setTextFill(Color.rgb(255, 55, 55));
            }
        } catch (NullPointerException e) {;
            usernameLabel.setText("Wrong useraname.");
            usernameLabel.setTextFill(Color.rgb(255, 55, 55));
        }
    }

    public void onSignupClick(MouseEvent mouseEvent) throws IOException {
        Spotify.runSingupView();
    }

    public void onEnterPressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            onLoginClick(null);
        }
    }
}
