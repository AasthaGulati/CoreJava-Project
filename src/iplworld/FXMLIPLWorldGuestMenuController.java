
package iplworld;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class FXMLIPLWorldGuestMenuController implements Initializable {

    @FXML
    private ImageView ivCSK;
    @FXML
    private ImageView ivKingsX1Punjab;
    @FXML
    private ImageView ivMumbaiIndians;
    @FXML
    private ImageView ivRoyalChallengersBanglore;
    @FXML
    private ImageView ivIPLLogo;
    @FXML
    private ImageView ivRajasthanRoyals;
    @FXML
    private ImageView ivKolkataKnightRiders;
    @FXML
    private ImageView ivSunrisesHyderabad;
    @FXML
    private ImageView ivDelhiCapitals;

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }   
    
    private void buttonClick(MouseEvent event, String fxml) {
        try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.setTitle("IPL World");
            Parent root = FXMLLoader.load(getClass().getResource(fxml));/* Exception */
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
        }
    }


    @FXML
    private void fetchTop10Batsmen(MouseEvent event) {
         buttonClick(event,"Top10Batsmen.fxml");
    }

    @FXML
    private void fetchTop10Bowlers(MouseEvent event) {
         buttonClick(event,"Top10Bowlers.fxml");
    }

    @FXML
    private void searchOrViewPlayer(MouseEvent event) {
        buttonClick(event,"FXMLSearchPlayer.fxml");
    }

    @FXML
    private void exitIplWorld(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    private void viewTeamTrophyCounts(MouseEvent event) {
        buttonClick(event,"FXMLTeamsBarChart.fxml");
    }
    
}
