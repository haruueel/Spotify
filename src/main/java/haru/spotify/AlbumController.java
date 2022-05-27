package haru.spotify;

import haru.spotify.model.Album;
import haru.spotify.model.Artist;
import haru.spotify.model.Song;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;

public class AlbumController {
    public ImageView img;
    public ImageView icon;
    public VBox container;
    public Label nameLabel;
    public Label typeLabel;

    public void createAlbumItem(Album album) {
        Image image;
        try {
            image = new Image(getClass().getResourceAsStream(album.getImgPath()));
        } catch (NullPointerException e) {
            image = new Image(getClass().getResourceAsStream("icons/albums/placeholder.png"));
        }

        img.setImage(image);
        nameLabel.setText(album.getName());
        if (album.getSongs().size()<2) {
            typeLabel.setText("S I N G L E");
            icon.setImage(new Image(getClass().getResourceAsStream("icons/equalizer.png")));
        }
    }

    // ――――――――――― AlbumController Events ―――――――――――

    public void onMouseEntered(MouseEvent mouseEvent) {
        container.setBackground(new Background(new BackgroundFill(Color.rgb(46, 46, 46), new CornerRadii(10), Insets.EMPTY)));
    }

    public void onMouseExited(MouseEvent mouseEvent) {
        container.setBackground(new Background(new BackgroundFill(Color.rgb(31, 31, 31), new CornerRadii(10), Insets.EMPTY)));
    }

    public void onClickRedirect(MouseEvent mouseEvent) throws IOException {
        Album selected = SpotifyController.findAlbumByName(nameLabel.getText());
        Spotify.runAlbumView(selected);
    }

    public void onMouseEnteredNameLabel(MouseEvent mouseEvent) {
        nameLabel.setTextFill(Color.rgb(30,215,96));
    }

    public void onMouseExitedNameLabel(MouseEvent mouseEvent) {
        nameLabel.setTextFill(Color.rgb(255,255,255));
    }
}
