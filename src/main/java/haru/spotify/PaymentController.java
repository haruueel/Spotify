package haru.spotify;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PaymentController implements Initializable {
    public Label paymentLabel;

    private static boolean selectedPayment;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paymentLabel.setVisible(false);
        selectedPayment = false;
    }

    public void onPaypalClick(MouseEvent mouseEvent) {
        paymentLabel.setText("Successfully logged in Paypal.");
        paymentLabel.setVisible(true);
        selectedPayment = true;
    }

    public void onCcardClick(MouseEvent mouseEvent) {
        paymentLabel.setText("Successfully valitaded Credit Card.");
        paymentLabel.setVisible(true);
        selectedPayment = true;
    }

    public void onConfirmClick(MouseEvent mouseEvent) {
        if(!selectedPayment){
            paymentLabel.setVisible(true);
            paymentLabel.setText("You must selected a method first.");
            paymentLabel.setTextFill(Color.rgb(255, 55, 55));
        } else {
            try{
                SpotifyController.st.executeUpdate("delete from free where usuario_id = "+SpotifyController.getUser().getId()+";");
                SpotifyController.st.executeUpdate("insert into premium(fecha_renovación, usuario_id) values ('2030-12-31',"+SpotifyController.getUser().getId()+");");
                SpotifyController.getUser().becomePremium();
                SpotifyController.getPremiumIDs().add(SpotifyController.getUser().getId());
                Spotify.closePayment();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
