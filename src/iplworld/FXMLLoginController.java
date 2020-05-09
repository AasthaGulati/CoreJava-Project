
package iplworld;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class FXMLLoginController implements Initializable {

    @FXML
    private JFXTextField txtUsername;
    @FXML
    private JFXPasswordField pfPassword;
    @FXML
    private ImageView ivIPLLogo;
    @FXML
    private ImageView ivPassword;
    @FXML
    private ImageView ivUsername;
    @FXML
    private Label lblMessage;
    @FXML
    private Label lblPleaseTryAgain;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    private Connection makeMySqlConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3307/IPLWorld", "root", "Pass@123");
        } catch (Exception ex) {
            return null;
        }
    }

    @FXML
    private void loginIPLWorld(ActionEvent event) {
           
       
        try {
            Connection conIPLWorld = makeMySqlConnection();
            PreparedStatement psLogin = conIPLWorld.prepareStatement("Select count(1) from Users Where IsActive=1 and UserName=? and Password=?");
           
            psLogin.setString(1,txtUsername.getText());
            psLogin.setString(2,pfPassword.getText());
            ResultSet rsLogin = psLogin.executeQuery();
          //  String Username = txtUsername.getText();
            //String Password = pfPassword.getText();
            if(rsLogin.next()){
            if (rsLogin.getInt("count(1)")>=1) {
                
                  Parent root = FXMLLoader.load(getClass().getResource("FXMLIPLWorldMenu.fxml"));
                Scene scene = new Scene(root);
                IPLWorld.stage.setScene(scene);
                IPLWorld.stage.show();
            
                // showMessage("Login");
            }
            else {
            pfPassword.setText(null);
            lblMessage.setText("Login Failed");
            lblPleaseTryAgain.setText("Please Try Again!!");
        }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
       
    }

    private void showMessage(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("IPL World");
        a.setContentText(msg);
        a.show();

    }

    @FXML
    private void loginAsAGuest(ActionEvent event) {
        try {
                Parent root = FXMLLoader.load(getClass().getResource("FXMLIPLWorldGuestMenu.fxml"));
                Scene scene = new Scene(root);
                IPLWorld.stage.setScene(scene);
                IPLWorld.stage.show();
               
            
        } catch (Exception e) {
        }
    }

}
