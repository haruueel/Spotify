package haru.spotify.model;

import haru.spotify.*;

public abstract class Listening {

    private static Song song;

    public static Song getSong(){
        return song;
    }


    public static void setFirstSong(Song song){
        Listening.song = song;
    }
    public static void setSong(Song song){
        Listening.song = song;
        updateListening();
    }

    private static void updateListening(){
        if (View.view.equalsIgnoreCase("albumview")) {
            AlbumViewController albumViewController = View.fxmlLoader.getController();
            albumViewController.updateSong(song);
        }

        if (View.view.equalsIgnoreCase("searchview")){
            SearchViewController searchViewController = View.fxmlLoader.getController();
            searchViewController.updateSong(song);
        }

        if (View.view.equalsIgnoreCase("libraryview")){
            LibraryViewController libraryViewController = View.fxmlLoader.getController();
            libraryViewController.updateSong(song);
        }

        if (View.view.equalsIgnoreCase("mainview")){
            SpotifyController spotifyController = View.fxmlLoader.getController();
            spotifyController.updateSong(song);
        }

        if (View.view.equalsIgnoreCase("artistview")){
            ArtistViewController artistViewController = View.fxmlLoader.getController();
            artistViewController.updateSong(song);
        }

        if(View.view.equalsIgnoreCase("playlistview")){
            PlaylistViewController playlistViewController = View.fxmlLoader.getController();
            playlistViewController.updateSong(song);
        }
    }
}
