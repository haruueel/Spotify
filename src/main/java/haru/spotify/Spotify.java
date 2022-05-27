package haru.spotify;

import haru.spotify.model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Spotify extends Application {

    public static Stage stageController;
    public static Stage paymentStage;
    public static Stage createPlaylistStage;
    public static Stage editPlaylistStage;
    public static Stage addSongStage;
    private static FXMLLoader editPlaylistFXMLLoader;
    public static Connection databaseConnection;

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        databaseConnection = getDatabaseConnection();
        SpotifyController.setSt(databaseConnection.createStatement());

        SpotifyController.initUsersFromDatabase();
        SpotifyController.initArtistsFromDatabase();
        SpotifyController.initAlbumsFromDatabase();
        SpotifyController.initSongsFromDatabase();
        SpotifyController.initCollabsFromDatabase();
        SpotifyController.initSponsoredPlaylistsIDsFromDatabase();
        SpotifyController.initActivePlaylistsFromDatabase();
        SpotifyController.initDeletedPlaylistsFromDatabase();

        FXMLLoader fxmlLoader = new FXMLLoader(Spotify.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 400);
        stage.setTitle("Spotify Login");
        stage.setScene(scene);
        stage.show();

        stageController = stage;

        fxmlLoader = new FXMLLoader(Spotify.class.getResource("payment.fxml"));
        scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage payment = new Stage();
        payment.setScene(scene);
        payment.initModality(Modality.APPLICATION_MODAL);

        paymentStage = payment;

        fxmlLoader = new FXMLLoader(Spotify.class.getResource("createplaylist.fxml"));
        scene = new Scene(fxmlLoader.load(), 361, 56);
        Stage createplaylist = new Stage();
        createplaylist.setScene(scene);
        createplaylist.initModality(Modality.APPLICATION_MODAL);

        createPlaylistStage = createplaylist;

        fxmlLoader = new FXMLLoader(Spotify.class.getResource("editplaylist.fxml"));
        scene = new Scene(fxmlLoader.load(), 361, 102);
        Stage editPlaylist = new Stage();
        editPlaylist.setScene(scene);
        editPlaylist.initModality(Modality.APPLICATION_MODAL);
        editPlaylistFXMLLoader = fxmlLoader;

        editPlaylistStage = editPlaylist;

        fxmlLoader = new FXMLLoader(Spotify.class.getResource("addsong.fxml"));
        scene = new Scene(fxmlLoader.load(), 361, 188);
        Stage addSong = new Stage();
        addSong.setScene(scene);
        addSong.initModality(Modality.APPLICATION_MODAL);

        addSongStage = addSong;

        Listening.setFirstSong(Sample.getSampleSong());
        View.view = "mainview";
    }

    public static void runLoginView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Spotify.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 400);
        stageController.setTitle("Spotify Login");
        stageController.setScene(scene);
    }

    public static void runSingupView() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(Spotify.class.getResource("register.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 600);
        stageController.setTitle("Spotify Login");
        stageController.setScene(scene);
    }

    public static void runMainView() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(Spotify.class.getResource("mainview.fxml"));
        View.fxmlLoader = fxmlLoader;
        View.view = "mainview";
        Scene scene = new Scene(fxmlLoader.load(), 1515, 830);
        stageController.setTitle("Spotify");
        stageController.setScene(scene);

        Action mainview = new Action("mainview",null);
        Action.addAction(mainview);
    }

    public static void runLibraryView() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(Spotify.class.getResource("libraryview.fxml"));
        View.fxmlLoader = fxmlLoader;
        View.view = "libraryview";
        Scene scene = new Scene(fxmlLoader.load(), 1515, 830);
        stageController.setTitle("Spotify");
        stageController.setScene(scene);

        Action libraryview = new Action("libraryview",null);
        Action.addAction(libraryview);
    }

    public static void runArtistView(Artist artist) throws IOException{
        ArtistViewController.setArtist(artist);
        FXMLLoader fxmlLoader = new FXMLLoader(Spotify.class.getResource("artistview.fxml"));
        View.fxmlLoader = fxmlLoader;
        View.view = "artistview";
        Scene scene = new Scene(fxmlLoader.load(), 1515, 830);
        stageController.setTitle("Spotify - "+artist.getName());
        stageController.setScene(scene);

        Action artistview = new Action("artistview", artist);
        Action.addAction(artistview);
    }

    public static void runAlbumView(Album album) throws IOException{
        AlbumViewController.setAlbum(album);
        FXMLLoader fxmlLoader = new FXMLLoader(Spotify.class.getResource("albumview.fxml"));
        View.fxmlLoader = fxmlLoader;
        View.view = "albumview";
        Scene scene = new Scene(fxmlLoader.load(), 1515, 830);
        stageController.setTitle("Spotify - "+album.getName());
        stageController.setScene(scene);

        Action albumview = new Action("albumview",album);
        Action.addAction(albumview);
    }

    public static void runSearchView(String search) throws IOException{
        SearchViewController.setSearch(search);
        FXMLLoader fxmlLoader = new FXMLLoader(Spotify.class.getResource("searchview.fxml"));
        View.fxmlLoader = fxmlLoader;
        View.view = "searchview";
        Scene scene = new Scene(fxmlLoader.load(), 1515, 830);
        stageController.setTitle("Spotify - Results for "+search);
        stageController.setScene(scene);

        Action searchview = new Action("searchview",search);
        Action.addAction(searchview);
    }

    public static void runPlaylistView(Active active) throws IOException{
        PlaylistViewController.setPlaylist(active);
        FXMLLoader fxmlLoader = new FXMLLoader(Spotify.class.getResource("playlistview.fxml"));
        View.fxmlLoader = fxmlLoader;
        View.view = "playlistview";
        Scene scene = new Scene(fxmlLoader.load(), 1515, 830);
        stageController.setTitle("Spotify - "+active.getTitle());
        stageController.setScene(scene);

        Action playlistview = new Action("playlistview",active);
        Action.addAction(playlistview);
    }

    public static void logOutPress() throws IOException{
        stageController.close();
        runLoginView();
        stageController.show();
    }

    public static void displayCreatePlaylist(){
        createPlaylistStage.showAndWait();
    }

    public static void displayEditPlaylist(Active active){
        EditPlaylistController.setPlaylist(active);
        EditPlaylistController editPlaylistController = editPlaylistFXMLLoader.getController();
        editPlaylistController.setStuff();
        editPlaylistStage.showAndWait();
    }

    public static void displayAddSong(Active active){
        AddSongController.setPlaylist(active);
        addSongStage.showAndWait();
    }

    public static void displayPayment(){
        paymentStage.showAndWait();
    }

    public static void closePayment() {
        paymentStage.close();
    }

    public static void closeCreatePlaylist(){
        createPlaylistStage.close();
    }

    public static void closeEditPlaylist(){
        editPlaylistStage.close();
    }

    public static void closeAddSong(){
        addSongStage.close();
    }

    public static Connection getDatabaseConnection(){
        try{
            return DriverManager.getConnection(Database.getUrl(), Database.getUser(), Database.getPassword());
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        launch();
    }
}