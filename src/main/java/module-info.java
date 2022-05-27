module com.example.spotify {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens haru.spotify to javafx.fxml;
    exports haru.spotify;
    exports haru.spotify.model;
    opens haru.spotify.model to javafx.fxml;
}