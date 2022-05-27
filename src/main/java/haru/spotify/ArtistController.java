package haru.spotify;

import haru.spotify.model.Artist;
import haru.spotify.model.FollowingSystem;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.io.IOException;

public class ArtistController {

    public ImageView img;
    public ImageView followIcon;
    public Label nameLabel;
    public HBox container;

    public void createArtistItem(Artist artist){
        Image image;
        try {
            image = new Image(getClass().getResourceAsStream(artist.getImgPath()));
        } catch (NullPointerException e) {
            image = new Image(getClass().getResourceAsStream("icons/artists/placeholder.png"));
        }

        img.setImage(image);
        nameLabel.setText(artist.getName());

        if (SpotifyController.getUser().isFollowingArtist(artist))
            followIcon.setImage(new Image(getClass().getResourceAsStream(FollowingSystem.FOLLOWINGICON)));
    }

    // ――――――――――― ArtistController Events ―――――――――――

    public void onMouseEntered(MouseEvent mouseEvent) {
        container.setBackground(new Background(new BackgroundFill(Color.rgb(46, 46, 46), new CornerRadii(100), Insets.EMPTY)));
    }

    public void onMouseExited(MouseEvent mouseEvent) {
        container.setBackground(new Background(new BackgroundFill(Color.rgb(31, 31, 31), new CornerRadii(100), Insets.EMPTY)));
    }

    public void onMouseEnteredNameLabel(MouseEvent mouseEvent) {
        nameLabel.setTextFill(Color.rgb(30,215,96));
    }

    public void onMouseExitedNameLabel(MouseEvent mouseEvent) {
        nameLabel.setTextFill(Color.rgb(255,255,255));
    }

    public void onClickRedirect(MouseEvent mouseEvent) throws IOException {
        Artist selected = SpotifyController.findArtistByName(nameLabel.getText());
        Spotify.runArtistView(selected);
    }
}
