package haru.spotify.model;

import haru.spotify.Spotify;

import java.io.IOException;
import java.util.EmptyStackException;
import java.util.Stack;

public class Action {

    private static Stack<Action> actions = new Stack<>();

    private Object object;
    private String view;

    // Constructor

    public Action(String view, Object obj){
        this.object = obj;
        this.view = view;
    }

    // Functions

    public static void addAction(Action action){
        actions.push(action);
    }

    public static void goBack() throws IOException {
        try {
            actions.pop();
            Action lastAction = actions.pop();
            if (lastAction.getView().equalsIgnoreCase("searchview")) {
                String search = (String) lastAction.getObject();
                Spotify.runSearchView(search);
            }
            if (lastAction.getView().equalsIgnoreCase("artistview")) {
                Artist artist = (Artist) lastAction.getObject();
                Spotify.runArtistView(artist);
            }
            if (lastAction.getView().equalsIgnoreCase("albumview")) {
                Album album = (Album) lastAction.getObject();
                Spotify.runAlbumView(album);
            }
            if (lastAction.getView().equalsIgnoreCase("mainview")) {
                Spotify.runMainView();
            }
            if (lastAction.getView().equalsIgnoreCase("libraryview")){
                Spotify.runLibraryView();
            }
            if(lastAction.getView().equalsIgnoreCase("playlistview")){
                Active playlist = (Active) lastAction.getObject();
                Spotify.runPlaylistView(playlist);
            }
        } catch (EmptyStackException e){
            System.out.println("Can't go back.");
        }
    }

    // Getters Setters

    public Object getObject() {
        return object;
    }

    public String getView() {
        return view;
    }
}
