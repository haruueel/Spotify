package haru.spotify;

import haru.spotify.model.CustomDate;
import haru.spotify.model.User;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {


    public Label usernameLabel;
    public Label passwordLabel;
    public Label emailLabel;
    public Label genderLabel;
    public Label birthLabel;
    public Label countryLabel;
    public Label zipLabel;
    public TextField usernameField;
    public TextField passwordField;
    public TextField emailField;
    public TextField birthField;
    public TextField countryField;
    public TextField zipField;
    public ChoiceBox<String> genderBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        genderBox.getItems().add("Male");
        genderBox.getItems().add("Female");
    }

    private boolean verifyData() throws SQLException {

        String username = usernameField.getText();
        if (username.equals("")) {
            usernameLabel.setTextFill(Color.rgb(255, 55, 55));
            return false;
        } else usernameLabel.setTextFill(Color.WHITE);

        String password = passwordField.getText();
        if (password.equals("")) {
            passwordLabel.setTextFill(Color.rgb(255, 55, 55));
            return false;
        } else passwordLabel.setTextFill(Color.WHITE);

        String email = emailField.getText();
        if (!email.contains("@")) {
            emailLabel.setTextFill(Color.rgb(255, 55, 55));
            return false;
        } else emailLabel.setTextFill(Color.WHITE);

        String gender = genderBox.getValue();
        if(gender==null){
            genderLabel.setTextFill(Color.rgb(255, 55, 55));
            return false;
        } else genderLabel.setTextFill(Color.WHITE);

        CustomDate bdate = null;
        try {
            bdate = new CustomDate(birthField.getText());
            birthLabel.setTextFill(Color.WHITE);
        } catch (ArrayIndexOutOfBoundsException e){
            birthLabel.setTextFill(Color.rgb(255, 55, 55));
        }

        String country = countryField.getText();
        if (country.equals("")) {
            countryLabel.setTextFill(Color.rgb(255, 55, 55));
            return false;
        } else countryLabel.setTextFill(Color.WHITE);

        String zip = zipField.getText();
        if (zip.equals("")) {
            zipLabel.setTextFill(Color.rgb(255, 55, 55));
            return false;
        } else zipLabel.setTextFill(Color.WHITE);

        SpotifyController.st.executeUpdate("insert into usuario(username,password,email,genero,fecha_nacimiento,pais,codigo_postal)  "+
                "values ('"+username+"','"+password+"','"+email+"','"+gender+"','"+bdate+"','"+country+"','"+zip+"');");
        ResultSet query = SpotifyController.st.executeQuery("select id from usuario where username like '"+username+"';");
        int uid = 0;
        if(query.next()) uid = query.getInt(1);

        boolean genderBool = !gender.equals("Male");
        User user = new User(uid,username,password,email,genderBool,bdate,country,zip, false);
        SpotifyController.getUsers().add(user);
        return true;
    }
    public void onRegisterClick(MouseEvent mouseEvent) throws IOException {
        try{
            if(verifyData()) Spotify.runLoginView();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
