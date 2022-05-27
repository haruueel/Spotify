package haru.spotify.model;

import haru.spotify.SpotifyController;

import java.sql.SQLException;

public abstract class FollowingSystem {

    public static final String FOLLOWABLEICON = "icons/alter_followable.png";
    public static final String FOLLOWINGICON = "icons/alter_following.png";

    public static void followArtist(Artist artist){
        try {
            SpotifyController.st.executeUpdate("insert into sigue_artista(usuario_id, artista_id)\n" +
                    "values ("+SpotifyController.getUser().getId()+","+artist.getId()+");");
            SpotifyController.getFollowedArtists().add(artist);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void unfollowArtist(Artist artist){
        try {
            SpotifyController.st.executeUpdate("delete from sigue_artista\n" +
                    "where usuario_id="+SpotifyController.getUser().getId()+" and artista_id="+artist.getId()+";");
            SpotifyController.getFollowedArtists().remove(artist);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void followSong(Song song){
        try {
            SpotifyController.st.executeUpdate("insert into guarda_cancion(usuario_id, cancion_id)\n" +
                    "values (" + SpotifyController.getUser().getId() + "," + song.getId() + ");");
            SpotifyController.getFollowedSongs().add(song);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void unfollowSong(Song song){
        try {
            SpotifyController.st.executeUpdate("delete from guarda_cancion\n" +
                    "where usuario_id="+SpotifyController.getUser().getId()+" and cancion_id="+song.getId()+";");
            SpotifyController.getFollowedSongs().remove(song);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void followAlbum(Album album){
        try {
            SpotifyController.st.executeUpdate("insert into sigue_album(usuario_id, album_id)\n" +
                    "values ("+SpotifyController.getUser().getId()+","+album.getId()+");");
            SpotifyController.getFollowedAlbums().add(album);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void unfollowAlbum(Album album){
        try {
            SpotifyController.st.executeUpdate("delete from sigue_album\n" +
                    "where usuario_id="+SpotifyController.getUser().getId()+" and album_id="+album.getId()+";");
            SpotifyController.getFollowedAlbums().remove(album);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void followPlaylist(Active active){
        try {
            SpotifyController.st.executeUpdate("insert into sigue_playlist(usuario_id,playlist_id) values ("+SpotifyController.getUser().getId()+","+active.getId()+");");
            SpotifyController.getFollowedPlaylists().add(active);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void unfollowPlaylist(Active active){
        try{
            SpotifyController.st.executeUpdate("delete from sigue_playlist where usuario_id="+SpotifyController.getUser().getId()+" and playlist_id="+active.getId());
            SpotifyController.getFollowedPlaylists().remove(active);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
